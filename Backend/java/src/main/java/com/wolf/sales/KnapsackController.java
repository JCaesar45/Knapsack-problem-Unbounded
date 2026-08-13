package com.wolf.sales;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimize")
public class KnapsackController {

    private final KnapsackService service;

    public KnapsackController(KnapsackService service) {
        this.service = service;
    }

    @PostMapping
    public OptimizeResponse optimize(@RequestBody OptimizeRequest request) {
        return service.solve(request.items(), request.maxWeight(), request.maxVolume());
    }
}
