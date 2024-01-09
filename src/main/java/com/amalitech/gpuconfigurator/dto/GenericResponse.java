package com.amalitech.gpuconfigurator.dto;
import lombok.Builder;

@Builder
public record GenericResponse(int status, String message) {}
