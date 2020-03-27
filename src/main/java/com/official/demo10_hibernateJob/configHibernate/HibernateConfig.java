package com.official.demo10_hibernateJob.configHibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class HibernateConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.datasource.oracle.driverClassName}")
    public String dataSourceDriverClassName;
    @Value("${spring.datasource.oracle.url}")
    public String dataSourceUrl;
    @Value("${spring.datasource.oracle.username}")
    public String dataSourceUsername;
    @Value("${spring.datasource.oracle.password}")
    public String dataSourcePassword;

    @SuppressWarnings("rawtypes")
    @Bean("dataSource")
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dataSourceDriverClassName);
        dataSourceBuilder.url(dataSourceUrl);
        dataSourceBuilder.username(dataSourceUsername);
        dataSourceBuilder.password(dataSourcePassword);
        return dataSourceBuilder.build();
    }

    @Bean("hibernateSessionFactory")
    public LocalSessionFactoryBean getLocalSessionFactoryBean(
            @Qualifier("dataSource") DataSource dataSource
    ){
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.format_sql","true");
        properties.setProperty("hibernate.allow_update_outside_transaction","true");

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(properties);
        sessionFactory.setMappingResources("data/demo10/mapping/CustomerCredit.hbm.xml");

        return sessionFactory;
    }

    @Bean("hibernateTransactionManager")
    public HibernateTransactionManager getHibernateTransactionManager(
            @Qualifier("hibernateSessionFactory") LocalSessionFactoryBean hibernateSessionFactory
    ){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(hibernateSessionFactory.getObject());
        return transactionManager;
    }

    /* 事务拦截器 */
    @Bean("hibernate_txAdvice")
    public TransactionInterceptor txAdvice(@Qualifier("hibernateTransactionManager") HibernateTransactionManager hibernateTransactionManager) {

        Map<String, TransactionAttribute> txMap = new HashMap<>();

        /* 只读事务，不做更新操作 */
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        txMap.put("get*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("list*", readOnlyTx);

        /* 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务 */
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(
                TransactionDefinition.PROPAGATION_REQUIRED,
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setReadOnly(false);
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
        rollbackRules.add(new RollbackRuleAttribute(Exception.class));
        requiredTx.setRollbackRules(rollbackRules);
        requiredTx.setTimeout(5); // 5 秒超时
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("build*", requiredTx);
        txMap.put("*", requiredTx);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(txMap);
        return new TransactionInterceptor(hibernateTransactionManager, source);
    }

    /** 切面拦截规则 参数会自动从容器中注入 */
    @Bean("hibernate_pointcutAdvisor")
    public DefaultPointcutAdvisor defaultPointcutAdvisor(@Qualifier("hibernate_txAdvice") TransactionInterceptor hibernate_txAdvice) {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setAdvice(hibernate_txAdvice);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (* com.official.demo10_hibernateJob.*.*(..))"); // com.official.demo10_hibernateJob
        pointcutAdvisor.setPointcut(pointcut);
        return pointcutAdvisor;
    }
}
