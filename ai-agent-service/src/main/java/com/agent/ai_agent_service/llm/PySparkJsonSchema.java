package com.agent.ai_agent_service.llm;

public class PySparkJsonSchema {
    public static final String SCHEMA = """
  {
    "name": "pyspark_draft",
    "schema": {
      "type": "object",
      "properties": {
        "pyspark_code": {"type": "string"},
        "steps": {"type": "array", "items": {"type": "string"}},
        "riskLevel": {"type": "string", "enum": ["LOW", "MEDIUM", "HIGH"]},
        "notes": {"type": "string"}
      },
      "required": ["pyspark_code", "steps", "riskLevel", "notes"],
      "additionalProperties": false
    },
    "strict": true
  }
  """;
}

