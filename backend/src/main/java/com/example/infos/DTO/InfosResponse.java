package com.example.infos.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InfosResponse {
    private String brokerVersion;
    private String clusterId;
    private List<TopicInfo> topics;
    private LocalDateTime fetchedAt;

    @Data
    @Builder
    public static class TopicInfo {
        private String name;
        private int partitions;
        private short replicationFactor;
    }
}