interface Item {
  name: string;
  value: number;
  weight: number;
  volume: number;
}

interface KnapsackResult {
  maxValue: number;
  totalWeight: number;
  totalVolume: number;
  allocation: Record<string, number>;
}

function getDecimalScale(values: number[]): number {
  let maxScale = 0;
  for (const v of values) {
    const s = v.toString();
    if (s.includes("e")) {
      maxScale = Math.max(maxScale, 1000);
      continue;
    }
    const idx = s.indexOf(".");
    if (idx !== -1) maxScale = Math.max(maxScale, s.length - idx - 1);
  }
  return 10 ** maxScale;
}

export function knapsackUnbounded(
  items: Item[],
  maxWeight: number,
  maxVolume: number
): KnapsackResult {
  const scale = getDecimalScale([
    ...items.flatMap((i) => [i.weight, i.volume]),
    maxWeight,
    maxVolume,
  ]);

  const W = Math.round(maxWeight * scale);
  const V = Math.round(maxVolume * scale);
  const weights = items.map((i) => Math.round(i.weight * scale));
  const volumes = items.map((i) => Math.round(i.volume * scale));
  const values = items.map((i) => i.value);
  const n = items.length;

  const dp = new Float64Array((W + 1) * (V + 1)).fill(0);

  for (let idx = 0; idx < n; idx++) {
    const wi = weights[idx];
    const vi = volumes[idx];
    const val = values[idx];
    for (let w = wi; w <= W; w++) {
      const row = w * (V + 1);
      const prev = (w - wi) * (V + 1);
      for (let v = vi; v <= V; v++) {
        const cand = dp[prev + v - vi] + val;
        const i = row + v;
        if (cand > dp[i]) dp[i] = cand;
      }
    }
  }

  let w = W;
  let v = V;
  const counts = new Array(n).fill(0);
  while (w > 0 && v > 0) {
    let moved = false;
    for (let idx = 0; idx < n; idx++) {
      const wi = weights[idx];
      const vi = volumes[idx];
      if (
        w >= wi &&
        v >= vi &&
        Math.abs(dp[w * (V + 1) + v] - (dp[(w - wi) * (V + 1) + v - vi] + values[idx])) < 0.5
      ) {
        counts[idx]++;
        w -= wi;
        v -= vi;
        moved = true;
        break;
      }
    }
    if (!moved) break;
  }

  const maxValue = dp[W * (V + 1) + V];
  const totalWeight = counts.reduce((a, c, i) => a + c * items[i].weight, 0);
  const totalVolume = counts.reduce((a, c, i) => a + c * items[i].volume, 0);
  const allocation: Record<string, number> = {};
  counts.forEach((c, i) => {
    if (c > 0) allocation[items[i].name] = c;
  });

  return { maxValue, totalWeight, totalVolume, allocation };
}
