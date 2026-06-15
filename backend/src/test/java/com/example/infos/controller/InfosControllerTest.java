package com.example.infos.controller;

import com.example.infos.DTO.InfosResponse;
import com.example.infos.service.InfosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfosController.class)
class InfosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InfosService infosService;

    @Test
    void getInfosReturnsKafkaMetadata() throws Exception {
        when(infosService.getInfos()).thenReturn(
                InfosResponse.builder()
                        .brokerVersion("Apache Kafka")
                        .clusterId("cluster-123")
                        .topics(List.of(
                                InfosResponse.TopicInfo.builder()
                                        .name("demo-topic")
                                        .partitions(1)
                                        .replicationFactor((short) 1)
                                        .build()
                        ))
                        .fetchedAt(LocalDateTime.parse("2026-06-13T12:00:00"))
                        .build()
        );

        mockMvc.perform(get("/api/infos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brokerVersion").value("Apache Kafka"))
                .andExpect(jsonPath("$.clusterId").value("cluster-123"))
                .andExpect(jsonPath("$.topics[0].name").value("demo-topic"))
                .andExpect(jsonPath("$.topics[0].partitions").value(1))
                .andExpect(jsonPath("$.topics[0].replicationFactor").value(1));
    }
}