/*
 * Copyright 2006-2007 the original author or authors.
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

package com.official.demo4_customerFilterJob.domain;

import com.official.demo4_customerFilterJob.dao.CustomerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author Lucas Ward
 *
 */
public class CustomerUpdateWriter implements ItemWriter<CustomerUpdate> {
	private static Logger logger = LoggerFactory.getLogger(CustomerUpdateWriter.class);

	private CustomerDao customerDao;
	
	@Override
	public void write(List<? extends CustomerUpdate> items) throws Exception {
		for(CustomerUpdate customerUpdate : items){
			if(customerUpdate.getOperation() == CustomerOperation.ADD){
				customerDao.insertCustomer(customerUpdate.getCustomerName(), customerUpdate.getCredit());
				logger.info(" writer 执行了.... ADD CustomerName=" + customerUpdate.getCustomerName());
			}
			else if(customerUpdate.getOperation() == CustomerOperation.UPDATE){
				customerDao.updateCustomer(customerUpdate.getCustomerName(), customerUpdate.getCredit());
				logger.info(" writer 执行了.... UPDATE CustomerName=" + customerUpdate.getCustomerName());
			}
		}
		
		//flush and/or clear resources
	}
	
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

}
