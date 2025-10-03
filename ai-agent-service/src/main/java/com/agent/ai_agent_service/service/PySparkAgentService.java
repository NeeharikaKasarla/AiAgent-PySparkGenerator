package com.agent.ai_agent_service.service;

import com.agent.ai_agent_service.api.PySparkGenerateRequest;
import com.agent.ai_agent_service.api.PySparkGenerateResponse;
import jakarta.validation.Valid;

import com.agent.ai_agent_service.llm.OpenAiClient;
import com.agent.ai_agent_service.llm.PromptBuilder;
import com.agent.ai_agent_service.llm.PySparkJsonSchema;
import com.agent.ai_agent_service.safety.PySparkSafety;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PySparkAgentService {
    private final OpenAiClient openAi;
    private final String outputBasePath;
    private final boolean defaultIncludeSteps;

    public PySparkAgentService(OpenAiClient openAi,
                               @Value("${agent.outputBasePath}") String outputBasePath,
                               @Value("${agent.includeSteps:true}") boolean defaultIncludeSteps) {
        this.openAi = openAi;
        this.outputBasePath = outputBasePath;
        this.defaultIncludeSteps = defaultIncludeSteps;
    }

    public PySparkGenerateResponse generate(PySparkGenerateRequest req) throws Exception {
        boolean includeSteps = req.includeSteps() != null ? req.includeSteps() : defaultIncludeSteps;

        String prompt = PromptBuilder.build(req.requirement(), req.filePath(), req.fileFormat(),
                req.schema(), outputBasePath, includeSteps);

        JsonNode json = openAi.generateJson(prompt, PySparkJsonSchema.SCHEMA);

        String code = json.path("pyspark_code").asText();
        String risk = json.path("riskLevel").asText("LOW");
        String notes = json.path("notes").asText(null);

        PySparkSafety.assertSafe(code);

        List<String> steps = new ArrayList<>();
        if (json.has("steps")) json.get("steps").forEach(n -> steps.add(n.asText()));

        return new PySparkGenerateResponse(code, steps.isEmpty()?null:steps, risk, notes);
    }
}

