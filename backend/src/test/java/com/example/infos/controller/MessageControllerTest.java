package com.example.infos.controller;

import com.example.infos.DTO.PublishMessageResponse;
import com.example.infos.service.ConsumedMessageService;
import com.example.infos.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @MockBean
    private ConsumedMessageService consumedMessageService;

    @Test
    void publishMessageReturnsCreated() throws Exception {
        when(kafkaProducerService.publish(any()))
                .thenReturn(PublishMessageResponse.builder()
                        .id("message-123")
                        .build());

        mockMvc.perform(post("/api/messages")
                        .contentType("application/json")
                        .content("{\"message\":\"hello\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "message-123"));
    }

    @Test
    void getConsumedMessagesReturnsOk() throws Exception {
        when(consumedMessageService.getRecentMessages()).thenReturn(List.of());

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk());
    }
}