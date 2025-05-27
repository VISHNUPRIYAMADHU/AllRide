package com.allride.publisher
import com.allride.event.FileUploadedEvent
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    val fileEventChannel: Channel<FileUploadedEvent>
)
