package com.example.infos.controller;

import com.example.infos.DTO.ConsumedMessageResponse;
import com.example.infos.DTO.PublishMessageResponse;
import com.example.infos.service.ConsumedMessageService;
import com.example.infos.service.KafkaProducerService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducerService kafkaProducerService;
    private final ConsumedMessageService consumedMessageService;


    @PostMapping
    public ResponseEntity<PublishMessageResponse> publish(@RequestBody JsonNode body) {
        PublishMessageResponse response = kafkaProducerService.publish(body);
        URI uri = URI.create(response.getId());
        return  ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ConsumedMessageResponse>> getConsumedMessages() {
        return ResponseEntity.ok(consumedMessageService.getRecentMessages());
    }
}


