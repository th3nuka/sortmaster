package com.sortmaster.controller;

import com.sortmaster.dto.*;
import com.sortmaster.model.AlgorithmMetadata;
import com.sortmaster.service.AlgorithmRegistry;
import com.sortmaster.service.SortingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SortingApiController {

    private final SortingService sortingService;
    private final AlgorithmRegistry registry;

    public SortingApiController(SortingService sortingService, AlgorithmRegistry registry) {
        this.sortingService = sortingService;
        this.registry = registry;
    }

    @GetMapping("/algorithms")
    public ResponseEntity<List<AlgorithmMetadata>> getAllAlgorithms() {
        return ResponseEntity.ok(sortingService.getAllAlgorithms());
    }

    @GetMapping("/algorithms/{id}")
    public ResponseEntity<AlgorithmMetadata> getAlgorithm(@PathVariable String id) {
        return ResponseEntity.ok(sortingService.getAlgorithmMetadata(id));
    }

    @PostMapping("/sort")
    public ResponseEntity<SortResponse> sortArray(@Valid @RequestBody SortRequest request) {
        return ResponseEntity.ok(sortingService.sort(request));
    }

    @PostMapping("/compare")
    public ResponseEntity<ComparisonResponse> compareAlgorithms(@RequestBody ComparisonRequest request) {
        return ResponseEntity.ok(sortingService.compare(request));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        return ResponseEntity.ok(new HealthResponse(
                "UP",
                "SortMaster — DSA Sorting Academy",
                "1.0.0",
                registry.size(),
                registry.getAllIds()
        ));
    }
}
