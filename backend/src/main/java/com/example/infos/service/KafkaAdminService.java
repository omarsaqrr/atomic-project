package com.example.infos.service;

import com.example.infos.DTO.InfosResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service responsible for querying Kafka cluster metadata.
 * Follows Single Responsibility Principle — only Kafka admin operations here.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAdminService {

    private final AdminClient adminClient;

    /**
     * Fetches cluster ID from the Kafka broker.
     */
    public String fetchClusterId() {
        try {
            DescribeClusterResult result = adminClient.describeCluster();
            return result.clusterId().get();
        } catch (Exception e) {
            log.error("Failed to fetch Kafka cluster ID", e);
            return "unknown";
        }
    }

    /**
     * Lists all available topics with their partition and replication metadata.
     */
    public List<InfosResponse.TopicInfo> fetchTopics() {
        try {
            Set<String> topicNames = adminClient.listTopics().names().get();
            Map<String, TopicDescription> descriptions =
                    adminClient.describeTopics(topicNames).allTopicNames().get();

            List<InfosResponse.TopicInfo> topics = new ArrayList<>();
            descriptions.forEach((name, desc) -> topics.add(
                    InfosResponse.TopicInfo.builder()
                            .name(name)
                            .partitions(desc.partitions().size())
                            .replicationFactor((short) desc.partitions().get(0).replicas().size())
                            .build()
            ));
            return topics;
        } catch (Exception e) {
            log.error("Failed to fetch Kafka topics", e);
            return List.of();
        }
    }
}