package com.ventuit.adminstrativeapp.shared.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.ventuit.adminstrativeapp.shared.services.implementations.SeedServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/seed")
@RequiredArgsConstructor
public class SeedController {

    private final SeedServiceImpl seedService;

    @PostMapping
    public ResponseEntity<Void> seed() {
        this.seedService.seed();
        return ResponseEntity.status(201).build();
    }

}
