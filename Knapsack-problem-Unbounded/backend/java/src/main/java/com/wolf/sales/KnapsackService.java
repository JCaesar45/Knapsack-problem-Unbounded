package com.wolf.sales;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KnapsackService {

    public OptimizeResponse solve(List<Item> items, double maxWeight, double maxVolume) {
        int scale = decimalScale(items, maxWeight, maxVolume);
        int W = (int) Math.round(maxWeight * scale);
        int V = (int) Math.round(maxVolume * scale);
        int n = items.size();

        int[] weights = new int[n];
        int[] volumes = new int[n];
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = (int) Math.round(items.get(i).weight() * scale);
            volumes[i] = (int) Math.round(items.get(i).volume() * scale);
            values[i] = items.get(i).value();
        }

        double[] dp = new double[(W + 1) * (V + 1)];

        for (int idx = 0; idx < n; idx++) {
            int wi = weights[idx];
            int vi = volumes[idx];
            double val = values[idx];
            for (int w = wi; w <= W; w++) {
                int row = w * (V + 1);
                int prev = (w - wi) * (V + 1);
                for (int v = vi; v <= V; v++) {
                    double cand = dp[prev + v - vi] + val;
                    int index = row + v;
                    if (cand > dp[index]) dp[index] = cand;
                }
            }
        }

        int w = W, v = V;
        int[] counts = new int[n];
        while (w > 0 && v > 0) {
            boolean moved = false;
            for (int idx = 0; idx < n; idx++) {
                int wi = weights[idx];
                int vi = volumes[idx];
                if (w >= wi && v >= vi &&
                        Math.abs(dp[w * (V + 1) + v] - (dp[(w - wi) * (V + 1) + v - vi] + values[idx])) < 0.5) {
                    counts[idx]++;
                    w -= wi;
                    v -= vi;
                    moved = true;
                    break;
                }
            }
            if (!moved) break;
        }

        double totalWeight = 0, totalVolume = 0;
        List<Map<String, Object>> allocation = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (counts[i] > 0) {
                totalWeight += counts[i] * items.get(i).weight();
                totalVolume += counts[i] * items.get(i).volume();
                Map<String, Object> map = new HashMap<>();
                map.put("name", items.get(i).name());
                map.put("count", counts[i]);
                allocation.add(map);
            }
        }

        return new OptimizeResponse(dp[W * (V + 1) + V], totalWeight, totalVolume, allocation);
    }

    private int decimalScale(List<Item> items, double maxWeight, double maxVolume) {
        int max = 0;
        for (Item it : items) {
            max = Math.max(max, scale(it.weight()));
            max = Math.max(max, scale(it.volume()));
        }
        max = Math.max(max, scale(maxWeight));
        max = Math.max(max, scale(maxVolume));
        return (int) Math.pow(10, max);
    }

    private int scale(double value) {
        String s = Double.toString(value);
        int idx = s.indexOf('.');
        return idx == -1 ? 0 : s.length() - idx - 1;
    }
}
