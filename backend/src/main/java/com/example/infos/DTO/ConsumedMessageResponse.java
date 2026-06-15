package com.example.infos.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConsumedMessageResponse {
    private String id;
    private String payload;
    private String topic;
    private int partition;
    private long offset;
    private LocalDateTime consumedAt;
}


