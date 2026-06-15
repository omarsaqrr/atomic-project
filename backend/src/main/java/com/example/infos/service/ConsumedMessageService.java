package com.example.infos.service;

import com.example.infos.DTO.ConsumedMessageResponse;
import com.example.infos.model.ConsumedMessage;
import com.example.infos.repository.ConsumedMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumedMessageService {

    private final ConsumedMessageRepository consumedMessageRepository;

    @Transactional(readOnly = true)
    public List<ConsumedMessageResponse> getRecentMessages() {
        return consumedMessageRepository.findTop20ByOrderByConsumedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    private ConsumedMessageResponse toResponse(ConsumedMessage message) {
        return ConsumedMessageResponse.builder()
                .id(message.getId())
                .payload(message.getPayload())
                .topic(message.getTopic())
                .partition(message.getPartition())
                .offset(message.getOffset())
                .consumedAt(message.getConsumedAt())
                .build();
    }
}
