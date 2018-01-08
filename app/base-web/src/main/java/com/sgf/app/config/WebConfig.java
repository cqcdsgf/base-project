package com.sgf.app.config;

import com.sgf.app.demo.crud.dao.InMemoryMessageRepository;
import com.sgf.app.demo.crud.domain.Message;
import com.sgf.app.demo.crud.dao.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by sgf on 2018\1\8 0008.
 */
@Configuration
public class WebConfig {

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

    @Bean
    public Converter<String, Message> messageConverter() {
        return new Converter<String, Message>() {
            @Override
            public Message convert(String id) {
                return messageRepository().findMessage(Long.valueOf(id));
            }
        };
    }


}
