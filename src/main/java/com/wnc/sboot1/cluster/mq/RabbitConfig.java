
package com.wnc.sboot1.cluster.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig
{

    @Bean
    public Queue helloQueue()
    {
        return new Queue( "zb8_news_history_date" );
    }

}
