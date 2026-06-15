package com.example.infos.service;

import com.example.infos.model.ConsumedMessage;
import com.example.infos.repository.ConsumedMessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private ConsumedMessageRepository consumedMessageRepository;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void consumePersistsKafkaRecordInDatabase() {
        ConsumerRecord<String, String> record =
                new ConsumerRecord<>("demo-topic", 0, 12L, "key", "{\"event\":\"created\"}");

        kafkaConsumerService.consume(record);

        ArgumentCaptor<ConsumedMessage> captor = ArgumentCaptor.forClass(ConsumedMessage.class);
        verify(consumedMessageRepository).save(captor.capture());

        ConsumedMessage saved = captor.getValue();
        assertThat(saved.getPayload()).isEqualTo("{\"event\":\"created\"}");
        assertThat(saved.getTopic()).isEqualTo("demo-topic");
        assertThat(saved.getPartition()).isZero();
        assertThat(saved.getOffset()).isEqualTo(12L);
        assertThat(saved.getConsumedAt()).isNotNull();
    }
}
