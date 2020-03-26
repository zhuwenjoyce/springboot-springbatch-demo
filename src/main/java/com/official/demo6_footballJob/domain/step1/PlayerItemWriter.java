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

package com.official.demo6_footballJob.domain.step1;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class PlayerItemWriter implements ItemWriter<Player> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private PlayerDao playerDao;

	@Override
	public void write(List<? extends Player> players) throws Exception {
		int i = 0;
		for (Player player : players) {
			logger.info("["+(++i)+"] playerloadStep -- playerWriter save Player :::  " + JSONObject.toJSONString(player));
			playerDao.savePlayer(player);
		}
	}

	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

}
