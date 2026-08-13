package com.wolf.sales;

import java.util.List;
import java.util.Map;

public record OptimizeResponse(
        double maxValue,
        double totalWeight,
        double totalVolume,
        List<Map<String, Object>> items
) {}
