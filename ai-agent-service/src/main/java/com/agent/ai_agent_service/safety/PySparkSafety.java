package com.agent.ai_agent_service.safety;

public class PySparkSafety {
    public static void assertSafe(String code) {
        String lc = code.toLowerCase();
        if (lc.contains("topandas()")) throw new IllegalArgumentException("Unsafe: toPandas()");
        if (lc.contains("os.remove")) throw new IllegalArgumentException("Unsafe: file deletion");
        if (lc.contains("spark.sql(")) throw new IllegalArgumentException("Unsafe: Spark SQL not allowed");
    }
}

