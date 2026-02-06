package org.e4s.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.e4s.kafka.entity.MessageEntity;
import org.e4s.kafka.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageListenerTest {

    @Mock
    private MessageRepository messageRepository;

    private KafkaMessageListener listener;

    @BeforeEach
    void setUp() {
        listener = new KafkaMessageListener(messageRepository);
    }

    @Test
    void testListen_ShouldSaveMessageToDatabase() {
        String topic = "test-topic";
        String payload = "test-message";
        int partition = 0;
        long offset = 100L;

        ConsumerRecord<String, String> record = new ConsumerRecord<>(topic, partition, offset, "key", payload);
        when(messageRepository.save(any(MessageEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        listener.listen(record);

        ArgumentCaptor<MessageEntity> captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageRepository, times(1)).save(captor.capture());

        MessageEntity savedEntity = captor.getValue();
        assertEquals(topic, savedEntity.getTopic());
        assertEquals(payload, savedEntity.getBody());
        assertEquals(partition, savedEntity.getPartition());
        assertEquals(offset, savedEntity.getOffset());
        assertNotNull(savedEntity.getCreatedAt());
    }

    @Test
    void testListen_ShouldRethrowExceptionOnSaveFailure() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("test-topic", 0, 0L, "key", "payload");
        when(messageRepository.save(any(MessageEntity.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> listener.listen(record));

        verify(messageRepository, times(1)).save(any(MessageEntity.class));
    }
}