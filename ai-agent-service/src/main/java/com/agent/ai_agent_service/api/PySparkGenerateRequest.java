package com.agent.ai_agent_service.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record PySparkGenerateRequest(
        @NotBlank String requirement,
        @NotBlank String filePath,
        @NotBlank String fileFormat,
        @NotNull Map<String,String> schema,
        Boolean includeSteps
) {}
