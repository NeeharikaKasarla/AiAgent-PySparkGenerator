# AiAgent-PySparkGenerator

A Spring Boot microservice that generates **PySpark scripts** from natural language requirements using OpenAI models.

---

## Features
- Converts requirements into ready-to-run PySpark code
- Supports JSON Schema–validated structured outputs (with models that support it)
- REST API endpoint for integration
- Safety guard checks (e.g., blocks `toPandas()` or dangerous file ops)

---

## Prerequisites
- Java 21+
- Maven 3+
- An OpenAI API Key ([create one here](https://platform.openai.com/api-keys))
- (Optional) Docker / LocalStack for testing with local S3 buckets

---

## Setup

1. Clone the repo from dev.
2. Configure your API key.
- In src/main/resources/application.yml, replace this:
```
  openai:
    base-url: https://api.openai.com/v1
    model: gpt-4o-mini
    api-key: # ADD YOUR API KEY HERE
```
3. Build & run the service:
`mvn spring-boot:run`
- The service will start on http://localhost:8090


## Usage(Local testing with Postman)

### Endpoint
```
POST http://localhost:8090/v1/pyspark/generate
Headers -> Content-Type : application/json
```
### Request Example
```
{
  "requirement": "Quarterly sales summary in USD excluding refunds with YoY growth",
  "filePath": "s3://bucket/transactions_2024.csv",
  "fileFormat": "csv",
  "schema": {
    "transaction_id": "string",
    "customer_id": "string",
    "amount": "double",
    "timestamp": "timestamp"
  },
  "includeSteps": true
}
```

### Response Example
```
{
  "pyspark_code": "from pyspark.sql import SparkSession ...",
  "steps": [
    "Initialize Spark session",
    "Read CSV",
    "Filter refunds",
    "Aggregate sales by quarter"
  ],
  "riskLevel": "LOW",
  "notes": "Safe to execute"
}
```
