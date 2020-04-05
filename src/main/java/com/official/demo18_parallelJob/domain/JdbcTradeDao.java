/*
 * Copyright 2006-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.official.demo18_parallelJob.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import javax.sql.DataSource;


/**
 * Writes a Trade object to a database
 *
 * @author Robert Kasanicky
 */
public class JdbcTradeDao {
	private Log log = LogFactory.getLog(JdbcTradeDao.class);
    /**
     * template for inserting a row
     */
    private static final String INSERT_TRADE_RECORD = "INSERT INTO TRADE (id, version, isin, quantity, price, customer) VALUES (?, 0, ?, ? ,?, ?)";

    /**
     * handles the processing of SQL query
     */
    private JdbcOperations jdbcTemplate;

    /**
     * database is not expected to be setup for auto increment
     */
    private DataFieldMaxValueIncrementer incrementer;

	public void writeTrade(Trade trade) {
        Long id = incrementer.nextLongValue();
        log.debug("Processing: " + trade);
        jdbcTemplate.update(INSERT_TRADE_RECORD,
				id, trade.getIsin(), trade.getQuantity(), trade.getPrice(),
				trade.getCustomer());
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setIncrementer(DataFieldMaxValueIncrementer incrementer) {
        this.incrementer = incrementer;
    }

}
