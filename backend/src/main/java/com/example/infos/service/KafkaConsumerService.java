package com.example.infos.service;

import com.example.infos.model.ConsumedMessage;
import com.example.infos.repository.ConsumedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ConsumedMessageRepository consumedMessageRepository;

    @KafkaListener(
            topics = "${app.kafka.topic:demo-topic}",
            groupId = "${spring.kafka.consumer.group-id:infos-app-group}"
    )
    @Transactional
    public void consume(ConsumerRecord<String, String> record) {
        String id = record.key();

        ConsumedMessage message = ConsumedMessage.builder()
                .id(id)
                .payload(record.value())
                .topic(record.topic())
                .partition(record.partition())
                .offset(record.offset())
                .consumedAt(LocalDateTime.now())
                .build();

        consumedMessageRepository.save(message);

        log.info("Persisted Kafka message id={} topic={} partition={} offset={}",
                id, record.topic(), record.partition(), record.offset());
    }
}