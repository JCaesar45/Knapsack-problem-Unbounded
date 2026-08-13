package com.wolf.sales;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class KnapsackServiceTest {

    private final KnapsackService service = new KnapsackService();

    @Test
    public void testClassicCases() {
        List<Item> items = List.of(
            new Item("panacea", 3000, 0.3, 0.025),
            new Item("ichor", 1800, 0.2, 0.015),
            new Item("gold", 2500, 2, 0.002)
        );
        assertEquals(54500, service.solve(items, 25, 0.25).maxValue(), 0.1);
        assertEquals(88400, service.solve(items, 55, 0.25).maxValue(), 0.1);
        assertEquals(42500, service.solve(items, 25, 0.15).maxValue(), 0.1);
        assertEquals(75900, service.solve(items, 35, 0.35).maxValue(), 0.1);
        assertEquals(43200, service.solve(items, 15, 0.25).maxValue(), 0.1);
    }
}
