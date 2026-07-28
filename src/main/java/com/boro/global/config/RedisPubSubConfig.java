package com.boro.global.config;

import com.boro.domain.chat.service.command.RedisSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {


    // Redis에 발행(publish)된 메시지 처리를 위한 리스너 설정
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory,
            @Qualifier("chatMessageListenerAdapter")
            MessageListenerAdapter chatMessageListenerAdapter,
            @Qualifier("chatRoomUpdateListenerAdapter")
            MessageListenerAdapter chatRoomUpdateListenerAdapter
    ){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatMessageListenerAdapter, new PatternTopic("chat.room.*"));
        container.addMessageListener(chatRoomUpdateListenerAdapter, new PatternTopic("chat-room.update.*")
        );
        return container;
    }

    // 실제 메시지를 처리하는 subscriber 설정 추가
    @Bean("chatMessageListenerAdapter")
    public MessageListenerAdapter chatMessageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    @Bean("chatRoomUpdateListenerAdapter")
    public MessageListenerAdapter chatRoomUpdateListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleChatRoomUpdate");
    }
}
