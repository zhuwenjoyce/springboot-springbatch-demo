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

package com.official.demo6_footballJob.domain.step3;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcPlayerSummaryDao implements ItemWriter<PlayerSummary> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String INSERT_SUMMARY = "INSERT into PLAYER_SUMMARY(ID, YEAR_NO, COMPLETES, ATTEMPTS, PASSING_YARDS, PASSING_TD, "
			+ "INTERCEPTIONS, RUSHES, RUSH_YARDS, RECEPTIONS, RECEPTIONS_YARDS, TOTAL_TD) "
			+ "values(:id, :year, :completes, :attempts, :passingYards, :passingTd, "
			+ ":interceptions, :rushes, :rushYards, :receptions, :receptionYards, :totalTd)";

    private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Override
	public void write(List<? extends PlayerSummary> summaries) {

		int i = 0;
		for (PlayerSummary summary : summaries) {
			logger.info("["+(++i)+"] playerSummarizationStep -- JdbcPlayerSummaryDao save summary :::  " + JSONObject.toJSONString(summary));

			MapSqlParameterSource args = new MapSqlParameterSource().addValue("id", summary.getId()).addValue("year",
					summary.getYear()).addValue("completes", summary.getCompletes()).addValue("attempts",
					summary.getAttempts()).addValue("passingYards", summary.getPassingYards()).addValue("passingTd",
					summary.getPassingTd()).addValue("interceptions", summary.getInterceptions()).addValue("rushes",
					summary.getRushes()).addValue("rushYards", summary.getRushYards()).addValue("receptions",
					summary.getReceptions()).addValue("receptionYards", summary.getReceptionYards()).addValue(
					"totalTd", summary.getTotalTd());

            namedParameterJdbcTemplate.update(INSERT_SUMMARY, args);
		}
	}

    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
