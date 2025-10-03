package com.agent.ai_agent_service.llm;

import java.util.Map;
import java.util.stream.Collectors;

public class PromptBuilder {
    public static String build(String requirement, String filePath, String fileFormat,
                               Map<String,String> schema, String outputBasePath, boolean includeSteps) {
        String schemaStr = schema.entrySet().stream()
                .map(e -> e.getKey()+":"+e.getValue()).collect(Collectors.joining(", "));

        return """
      You are a senior PySpark data engineer.

      Requirement: %s
      Input file: %s (format: %s)
      Schema: %s

      Rules:
      - Use PySpark DataFrame API only (no spark.sql).
      - Never modify or delete source file.
      - Write output as Parquet under: %s
      - %s
      """.formatted(
                requirement, filePath, fileFormat, schemaStr, outputBasePath,
                includeSteps ? "Include human-readable steps." : "Do not include steps."
        );
    }
}
