# Stratton Oakmont Terminal

> "The only thing standing between you and your goal is the story you keep telling yourself." — Jordan Belfort

A luxury-grade, multi-language implementation of the **unbounded knapsack problem** constrained by both **weight** and **volume**. Built to impress: CEOs, CTOs, clients, hackers, and the engineers reviewing your GitHub at 2 AM.

## What It Does

You are a traveler in Shangri-La. You can carry a limited total weight and volume. Each item has a known dollar value. You may take unlimited whole units of any item. This terminal tells you the exact allocation that maximizes value.

## Product Structure

```
wolf-of-wallstreet-dev/
├── index.html              # Single-page luxury application
├── README.md               # This playbook
├── backend/
│   ├── python/
│   │   ├── main.py         # FastAPI microservice
│   │   └── requirements.txt
│   └── java/
│       ├── pom.xml
│       └── src/main/java/com/wolf/sales/
│           ├── StrattonKnapsackApplication.java
│           ├── KnapsackController.java
│           ├── KnapsackService.java
│           ├── OptimizeRequest.java
│           ├── OptimizeResponse.java
│           └── Item.java
└── typescript/
    └── knapsack.ts         # Strictly typed module
```

## Algorithm

The implementation uses a 2D unbounded dynamic programming approach.

1. **Decimal scaling**: All floating-point weights, volumes, and capacities are multiplied by the largest power of ten required to make them integers.
2. **DP table**: `dp[w][v]` stores the maximum value achievable with weight capacity `w` and volume capacity `v`.
3. **Recurrence**: For every item, update `dp[w][v] = max(dp[w][v], dp[w-weight][v-volume] + value)`.
4. **Backtracking**: Reconstruct the item counts from the final state.

Time complexity: **O(n · W · V)** where W and V are the scaled capacities.
Space complexity: **O(W · V)**.

## Quick Start

### Frontend

```bash
cd wolf-of-wallstreet-dev
python3 -m http.server 8000
# open http://localhost:8000
```

### Python API

```bash
cd backend/python
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8001
```

Test it:

```bash
curl -X POST http://localhost:8001/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"name":"panacea","value":3000,"weight":0.3,"volume":0.025},
      {"name":"ichor","value":1800,"weight":0.2,"volume":0.015},
      {"name":"gold","value":2500,"weight":2,"volume":0.002}
    ],
    "max_weight": 25,
    "max_volume": 0.25
  }'
```

### Java API

```bash
cd backend/java
./mvnw spring-boot:run
# or mvn spring-boot:run
```

Endpoint: `POST http://localhost:8082/optimize`

### TypeScript Module

```bash
npm install -D typescript vitest
npx tsc --init
npx vitest
```

## References

Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). *Introduction to algorithms* (4th ed.). MIT Press.

Kellerer, H., Pferschy, U., & Pisinger, D. (2004). *Knapsack problems*. Springer.

Martello, S., & Toth, P. (1990). *Knapsack problems: Algorithms and computer implementations*. John Wiley & Sons.
