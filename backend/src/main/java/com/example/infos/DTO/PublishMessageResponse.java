package com.example.infos.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishMessageResponse {
    private String id;
    private String topic;
    private int partition;
    private long offset;
    private String message;
}
