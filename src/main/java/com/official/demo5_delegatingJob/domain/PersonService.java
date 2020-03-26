/*
 * Copyright 2006-2014 the original author or authors.
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

package com.official.demo5_delegatingJob.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class that contains logic that would normally be be contained in
 * {@link org.springframework.batch.item.ItemReader} and
 * {@link javax.batch.api.chunk.ItemWriter}.
 * 
 * @author tomas.slanina
 * @author Robert Kasanicky
 */
public class PersonService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int GENERATION_LIMIT = 10;

	private int generatedCounter = 0;
	private int processedCounter = 0;

	// reader
	public Person getData() {
		if (generatedCounter >= GENERATION_LIMIT) {
			return null;
		}

		Person person = new Person();
		Address address = new Address();
		Child child = new Child();
		List<Child> children = new ArrayList<>(1);

		children.add(child);

		person.setFirstName("John" + generatedCounter);
		person.setAge(20 + generatedCounter);
		address.setCity("Johnsville" + generatedCounter);
		child.setName("Little Johny" + generatedCounter);

		person.setAddress(address);
		person.setChildren(children);

		generatedCounter++;
		logger.info("generatedCounter === "+generatedCounter);

		return person;
	}

	/*
	 * Badly designed method signature which accepts multiple implicitly related
	 * arguments instead of a single Person argument.
	 */
	// writer
	public void processPerson(String name, String city) {
		processedCounter++;
		logger.info("processedCounter ::: "+processedCounter);
	}

	public int getReturnedCount() {
		return generatedCounter;
	}

	public int getReceivedCount() {
		return processedCounter;
	}
}
