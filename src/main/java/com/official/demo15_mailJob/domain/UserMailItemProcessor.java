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
package com.official.demo15_mailJob.domain;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

/**
 * @author Dan Garrette
 * @author Dave Syer
 * 
 * @since 2.1
 */
public class UserMailItemProcessor implements
        ItemProcessor<User, SimpleMailMessage> {

    /**
     * @see org.springframework.batch.item.ItemProcessor#process(Object)
     */
    @Nullable
	@Override
	public SimpleMailMessage process(User user ) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo( user.getEmail() );
        message.setFrom( "179657283@qq.com" );
        message.setSubject( user.getName() + "'s Account Info" );
        message.setSentDate( new Date() );
        message.setText( "Hello " + user.getName() );
       return message;
    }
}
