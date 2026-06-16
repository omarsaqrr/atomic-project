package com.example.infos.repository;

import com.example.infos.model.ConsumedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumedMessageRepository extends JpaRepository<ConsumedMessage, String> {

    List<ConsumedMessage> findTop20ByOrderByConsumedAtDesc();
}
