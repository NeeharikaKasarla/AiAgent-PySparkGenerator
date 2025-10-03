package com.agent.ai_agent_service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OpenAiClient {
    @Value("${openai.base-url}") String baseUrl;
    @Value("${openai.model}") String model;
    @Value("${openai.api-key}") String apiKey;

    private final OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper om = new ObjectMapper();

    public JsonNode generateJson(String prompt, String schema) throws IOException {
        var body = Map.of(
                "model", model,
                // for chat completions:
                "messages", new Object[]{ Map.of("role","user","content", prompt) },
                // for schema-capable models
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", om.readValue(schema, Map.class)
                )
        );

        var req = new Request.Builder()
                .url(baseUrl + (model.contains("gpt-4.1") ? "/responses" : "/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(om.writeValueAsBytes(body), MediaType.get("application/json")))
                .build();

        try (var resp = http.newCall(req).execute()) {
            String bodyString = resp.body() != null ? resp.body().string() : "";
            if (!resp.isSuccessful()) {
                throw new IOException("OpenAI error: " + resp.code() + " | " + bodyString);
            }

            JsonNode payload = om.readTree(bodyString);
            System.out.println("OpenAI raw response: " + payload.toPrettyString());

            // Case 1: /responses API
            JsonNode responsesNode = payload.at("/output/0/content/0/text");
            if (!responsesNode.isMissingNode()) {
                return om.readTree(responsesNode.asText());
            }

            // Case 2: /chat/completions API
            JsonNode chatNode = payload.at("/choices/0/message/content");
            if (!chatNode.isMissingNode()) {
                return om.readTree(chatNode.asText());
            }

            throw new IOException("OpenAI response missing expected content node: " + payload.toPrettyString());
        }
    }
}

