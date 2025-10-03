package com.agent.ai_agent_service.api;

import java.util.List;

public record PySparkGenerateResponse(
        String pyspark_code,
        List<String> steps,
        String riskLevel,
        String notes
) {}
