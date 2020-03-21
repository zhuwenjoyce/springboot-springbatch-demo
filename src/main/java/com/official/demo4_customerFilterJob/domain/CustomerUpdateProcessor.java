package com.official.demo4_customerFilterJob.domain;

import com.official.demo4_customerFilterJob.dao.CustomerDao;
import com.official.demo4_customerFilterJob.log.InvalidCustomerLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;

import static com.official.demo4_customerFilterJob.domain.CustomerOperation.*;

/**
 * @author Lucas Ward
 *
 */
public class CustomerUpdateProcessor implements ItemProcessor<CustomerUpdate, CustomerUpdate> {
	private static Logger logger = LoggerFactory.getLogger(CustomerUpdateProcessor.class);

	private CustomerDao customerDao;
	private InvalidCustomerLogger invalidCustomerLogger;
	
	@Nullable
	@Override
	public CustomerUpdate process(CustomerUpdate item) throws Exception {
		logger.info(" processor 执行了....CustomerName=" + item.getCustomerName());
		if(item.getOperation() == DELETE){
			//delete is not supported
			invalidCustomerLogger.log(item);
			return null;
		}
		
		CustomerCredit customerCredit = customerDao.getCustomerByName(item.getCustomerName());
		
		if(item.getOperation() == ADD && customerCredit == null){
			return item;
		}
		else if(item.getOperation() == ADD && customerCredit != null){
			//veto processing
			invalidCustomerLogger.log(item);
			return null;
		}
		
		if(item.getOperation() == UPDATE && customerCredit != null){
			return item;
		}
		else if(item.getOperation() == UPDATE && customerCredit == null){
			//veto processing
			invalidCustomerLogger.log(item);
			return null;
		}
		
		//if an item makes it through all these checks it can be assumed to be bad, logged, and skipped
		invalidCustomerLogger.log(item);
		return null;
	}
	
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	
	public void setInvalidCustomerLogger(
			InvalidCustomerLogger invalidCustomerLogger) {
		this.invalidCustomerLogger = invalidCustomerLogger;
	}

}
