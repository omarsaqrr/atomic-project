package com.example.infos.service;

import com.example.infos.DTO.InfosResponse;
import com.example.infos.model.KafkaInfo;
import com.example.infos.repository.KafkaInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service that coordinates Kafka metadata retrieval and persistence.
 * Open/Closed: extend by adding new services without modifying this class.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InfosService {

    private final KafkaAdminService kafkaAdminService;
    private final KafkaInfoRepository kafkaInfoRepository;

    /**
     * Fetches live Kafka cluster info, persists topics to the DB,
     * and returns a structured response.
     */
    @Transactional
    public InfosResponse getInfos() {
        log.info("Fetching Kafka cluster infos...");

        String clusterId = kafkaAdminService.fetchClusterId();
        List<InfosResponse.TopicInfo> topics = kafkaAdminService.fetchTopics();
        LocalDateTime now = LocalDateTime.now();

        // Persist each topic snapshot to the database
        topics.forEach(topic -> {
            KafkaInfo entity = KafkaInfo.builder()
                    .topic(topic.getName())
                    .partitions(topic.getPartitions())
                    .replicationFactor(topic.getReplicationFactor())
                    .fetchedAt(now)
                    .build();
            kafkaInfoRepository.save(entity);
        });

        log.info("Fetched {} topics from cluster {}", topics.size(), clusterId);

        return InfosResponse.builder()
                .brokerVersion("Apache Kafka")
                .clusterId(clusterId)
                .topics(topics)
                .fetchedAt(now)
                .build();
    }
}