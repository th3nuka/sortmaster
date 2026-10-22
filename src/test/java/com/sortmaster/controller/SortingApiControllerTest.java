package com.sortmaster.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SortingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/health should return UP status")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("SortMaster — DSA Sorting Academy"));
    }

    @Test
    @DisplayName("GET /api/algorithms should return registered algorithms")
    void testGetAllAlgorithms() throws Exception {
        mockMvc.perform(get("/api/algorithms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /api/sort should sort array with Bubble Sort")
    void testSortEndpoint() throws Exception {
        String jsonPayload = """
                {
                    "algorithmId": "bubble-sort",
                    "array": [5, 2, 8, 1, 9],
                    "detailLevel": "NORMAL"
                }
                """;

        mockMvc.perform(post("/api/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.algorithmId").value("bubble-sort"))
                .andExpect(jsonPath("$.sortedArray[0]").value(1))
                .andExpect(jsonPath("$.sortedArray[4]").value(9))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.statistics.comparisons").isNumber());
    }

    @Test
    @DisplayName("POST /api/sort should sort array in DESCENDING order")
    void testSortEndpointDescending() throws Exception {
        String jsonPayload = """
                {
                    "algorithmId": "bubble-sort",
                    "array": [5, 2, 8, 1, 9],
                    "order": "DESCENDING"
                }
                """;

        mockMvc.perform(post("/api/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.algorithmId").value("bubble-sort"))
                .andExpect(jsonPath("$.order").value("DESCENDING"))
                .andExpect(jsonPath("$.sortedArray[0]").value(9))
                .andExpect(jsonPath("$.sortedArray[4]").value(1));
    }

    @Test
    @DisplayName("POST /api/sort should return 400 for invalid sorting order")
    void testSortInvalidOrder() throws Exception {
        String jsonPayload = """
                {
                    "algorithmId": "bubble-sort",
                    "array": [5, 2, 8],
                    "order": "SIDEWAYS"
                }
                """;

        mockMvc.perform(post("/api/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Unsupported sorting order: 'SIDEWAYS'")));
    }

    @Test
    @DisplayName("POST /api/compare should compare algorithms in DESCENDING order")
    void testCompareEndpointDescending() throws Exception {
        String jsonPayload = """
                {
                    "algorithmIds": ["bubble-sort", "quick-sort"],
                    "array": [5, 2, 8, 1, 9],
                    "order": "DESCENDING"
                }
                """;

        mockMvc.perform(post("/api/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order").value("DESCENDING"))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].sortedArray[0]").value(9))
                .andExpect(jsonPath("$.results[0].sortedArray[4]").value(1));
    }

    @Test
    @DisplayName("POST /api/sort should return 404 for unknown algorithm")
    void testSortUnknownAlgorithm() throws Exception {
        String jsonPayload = """
                {
                    "algorithmId": "magic-sort-nonexistent",
                    "array": [3, 1, 2]
                }
                """;

        mockMvc.perform(post("/api/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
