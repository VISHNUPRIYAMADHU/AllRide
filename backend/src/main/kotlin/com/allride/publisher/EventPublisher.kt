package com.allride.publisher

import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    val fileEventChannel: Channel<String>
)
