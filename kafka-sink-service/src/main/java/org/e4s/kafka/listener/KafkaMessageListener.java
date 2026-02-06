package org.e4s.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.e4s.kafka.entity.MessageEntity;
import org.e4s.kafka.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

    private final MessageRepository messageRepository;

    public KafkaMessageListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, String> record) {
        logger.info("Received message from topic: {}, partition: {}, offset: {}",
                record.topic(), record.partition(), record.offset());

        try {
            MessageEntity entity = new MessageEntity(
                    record.key(),
                    record.value(),
                    record.topic(),
                    record.partition(),
                    record.offset()
            );

            messageRepository.save(entity);

            logger.info("Successfully saved message from topic: {}, partition: {}, offset: {}",
                    record.topic(), record.partition(), record.offset());
        } catch (Exception e) {
            logger.error("Error processing message from topic: {}, partition: {}, offset: {}",
                    record.topic(), record.partition(), record.offset(), e);
            throw e;
        }
    }
}