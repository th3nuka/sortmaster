package com.sortmaster.dto;

import java.time.Instant;
import java.util.List;

public class HealthResponse {
    private String status;
    private String application;
    private String version;
    private Instant timestamp;
    private int algorithmCount;
    private List<String> supportedAlgorithms;

    public HealthResponse() {
    }

    public HealthResponse(String status, String application, String version, int algorithmCount, List<String> supportedAlgorithms) {
        this.status = status;
        this.application = application;
        this.version = version;
        this.timestamp = Instant.now();
        this.algorithmCount = algorithmCount;
        this.supportedAlgorithms = supportedAlgorithms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getAlgorithmCount() {
        return algorithmCount;
    }

    public void setAlgorithmCount(int algorithmCount) {
        this.algorithmCount = algorithmCount;
    }

    public List<String> getSupportedAlgorithms() {
        return supportedAlgorithms;
    }

    public void setSupportedAlgorithms(List<String> supportedAlgorithms) {
        this.supportedAlgorithms = supportedAlgorithms;
    }
}
