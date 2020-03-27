/*
 * Copyright 2006-2019 the original author or authors.
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

package com.official.demo10_hibernateJob.domain;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

/**
 * Increases customer's credit by a fixed amount.
 * 
 * @author Robert Kasanicky
 */
public class CustomerCreditIncreaseProcessor implements ItemProcessor<Object[], CustomerCredit> {
	public static final BigDecimal FIXED_AMOUNT = new BigDecimal("5");

	@Nullable
	@Override
	public CustomerCredit process(Object[] objArr) throws Exception {
		CustomerCredit credit = new CustomerCredit();
		credit.setId(Integer.valueOf(objArr[0].toString()));
		credit.setName(objArr[1].toString());
		credit.setCredit(new BigDecimal(objArr[2].toString()));
		return credit;
	}

}
