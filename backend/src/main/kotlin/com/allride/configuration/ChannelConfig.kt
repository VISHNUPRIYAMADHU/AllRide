package com.allride.configuration

import kotlinx.coroutines.channels.Channel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ChannelConfig {
    @Bean
   open fun fileEventChannel(): Channel<String> = Channel(Channel.UNLIMITED)
}