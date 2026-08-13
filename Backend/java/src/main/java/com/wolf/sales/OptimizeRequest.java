package com.wolf.sales;

import java.util.List;

public record OptimizeRequest(List<Item> items, double maxWeight, double maxVolume) {}
