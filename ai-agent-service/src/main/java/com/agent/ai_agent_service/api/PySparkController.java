package com.agent.ai_agent_service.api;

import com.agent.ai_agent_service.service.PySparkAgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pyspark")
public class PySparkController {
    private final PySparkAgentService service;
    public PySparkController(PySparkAgentService service) { this.service = service; }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public PySparkGenerateResponse generate(@Valid @RequestBody PySparkGenerateRequest req) throws Exception {
        return service.generate(req);
    }
}

