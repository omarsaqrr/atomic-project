package com.example.infos.controller;

import com.example.infos.DTO.InfosResponse;
import com.example.infos.service.InfosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/infos")
@RequiredArgsConstructor
public class InfosController {

    private final InfosService infosService;

    @GetMapping
    public ResponseEntity<InfosResponse> getInfos() {
        InfosResponse response = infosService.getInfos();
        return ResponseEntity.ok(response);
    }
}