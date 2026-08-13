from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import List
import math

app = FastAPI(title="Stratton Knapsack API")


class Item(BaseModel):
    name: str
    value: float
    weight: float = Field(gt=0)
    volume: float = Field(gt=0)


class OptimizeRequest(BaseModel):
    items: List[Item]
    max_weight: float = Field(gt=0)
    max_volume: float = Field(gt=0)


def _decimal_scale(values: List[float]) -> int:
    max_scale = 0
    for v in values:
        s = f"{v:.10f}".rstrip("0")
        if "." in s:
            max_scale = max(max_scale, len(s.split(".", 1)[1]))
    return 10 ** max_scale


def knapsack_unbounded(items: List[Item], max_weight: float, max_volume: float):
    scale = _decimal_scale(
        [i.weight for i in items]
        + [i.volume for i in items]
        + [max_weight, max_volume]
    )
    W = round(max_weight * scale)
    V = round(max_volume * scale)
    weights = [round(i.weight * scale) for i in items]
    volumes = [round(i.volume * scale) for i in items]
    values = [i.value for i in items]
    n = len(items)

    dp = [0.0] * ((W + 1) * (V + 1))

    for idx in range(n):
        wi, vi, val = weights[idx], volumes[idx], values[idx]
        for w in range(wi, W + 1):
            row = w * (V + 1)
            prev = (w - wi) * (V + 1)
            for v in range(vi, V + 1):
                cand = dp[prev + v - vi] + val
                if cand > dp[row + v]:
                    dp[row + v] = cand

    w, v = W, V
    counts = [0] * n
    while w > 0 and v > 0:
        moved = False
        for idx in range(n):
            wi, vi = weights[idx], volumes[idx]
            if w >= wi and v >= vi and math.isclose(
                dp[w * (V + 1) + v],
                dp[(w - wi) * (V + 1) + v - vi] + values[idx],
                abs_tol=0.5,
            ):
                counts[idx] += 1
                w -= wi
                v -= vi
                moved = True
                break
        if not moved:
            break

    total_value = dp[W * (V + 1) + V]
    total_weight = sum(c * items[i].weight for i, c in enumerate(counts))
    total_volume = sum(c * items[i].volume for i, c in enumerate(counts))
    return {
        "max_value": total_value,
        "total_weight": total_weight,
        "total_volume": total_volume,
        "items": [
            {"name": items[i].name, "count": c}
            for i, c in enumerate(counts) if c > 0
        ],
    }


@app.post("/optimize")
def optimize(req: OptimizeRequest):
    return knapsack_unbounded(req.items, req.max_weight, req.max_volume)
