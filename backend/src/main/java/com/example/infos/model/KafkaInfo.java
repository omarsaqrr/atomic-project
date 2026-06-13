package com.example.infos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "kafka_infos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private int partitions;

    @Column(name = "replication_factor", nullable = false)
    private short replicationFactor;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;
}