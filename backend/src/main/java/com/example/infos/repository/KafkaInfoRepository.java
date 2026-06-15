package com.example.infos.repository;

import com.example.infos.model.KafkaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KafkaInfoRepository extends JpaRepository<KafkaInfo, Long> {
}

