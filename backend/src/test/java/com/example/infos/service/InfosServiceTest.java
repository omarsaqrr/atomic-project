package com.example.infos.service;

import com.example.infos.DTO.InfosResponse;
import com.example.infos.model.KafkaInfo;
import com.example.infos.repository.KafkaInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfosServiceTest {

    @Mock
    private KafkaAdminService kafkaAdminService;

    @Mock
    private KafkaInfoRepository kafkaInfoRepository;

    @InjectMocks
    private InfosService infosService;

    @Test
    void getInfosReturnsKafkaMetadataAndPersistsTopics() {
        when(kafkaAdminService.fetchClusterId()).thenReturn("cluster-123");
        when(kafkaAdminService.fetchTopics()).thenReturn(List.of(
                InfosResponse.TopicInfo.builder()
                        .name("demo-topic")
                        .partitions(1)
                        .replicationFactor((short) 1)
                        .build()
        ));

        InfosResponse response = infosService.getInfos();

        assertThat(response.getClusterId()).isEqualTo("cluster-123");
        assertThat(response.getTopics()).hasSize(1);
        assertThat(response.getTopics().getFirst().getName()).isEqualTo("demo-topic");
        assertThat(response.getFetchedAt()).isNotNull();
        verify(kafkaInfoRepository).save(any(KafkaInfo.class));
    }
}
