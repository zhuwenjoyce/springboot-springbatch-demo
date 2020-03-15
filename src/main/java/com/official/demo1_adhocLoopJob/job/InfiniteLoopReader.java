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

package com.official.demo1_adhocLoopJob.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;

import java.util.Random;

/**
 * ItemReader implementation that will continually return a new object. It's
 * generally useful for testing interruption.
 * 
 * @author Lucas Ward
 * 
 */
public class InfiniteLoopReader implements ItemReader<Object> {
	private static Logger logger = LoggerFactory.getLogger(InfiniteLoopReader.class);

	@Nullable
	@Override
	public Object read() throws Exception {
		int i = new Random().nextInt(100);
		String readStr = "read随机数: " + i;
		logger.info(readStr);
		return readStr;
	}
}
