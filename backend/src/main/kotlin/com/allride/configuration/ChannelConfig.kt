package com.allride.configuration

import com.allride.event.FileUploadedEvent
import kotlinx.coroutines.channels.Channel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ChannelConfig {
    @Bean
    open fun fileEventChannel(): Channel<FileUploadedEvent> = Channel(Channel.UNLIMITED)
}
