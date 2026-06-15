package com.example.infos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumed_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumedMessage {

    @Id
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String topic;

    @Column(name = "kafka_partition", nullable = false)
    private int partition;

    @Column(name = "message_offset", nullable = false)
    private long offset;

    @Column(name = "consumed_at", nullable = false)
    private LocalDateTime consumedAt;
}
