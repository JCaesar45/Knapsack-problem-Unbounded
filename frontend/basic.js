function knapsackUnbounded(items, maxweight, maxvolume) {
 const getScale = x => {
 const s = x.toString();
 if (s.includes('e')) return 1000;
 const idx = s.indexOf('.');
 return idx === -1 ? 1 : Math.pow(10, s.length - idx - 1);
 };

 let wScale = 1, vScale = 1;
 for (const it of items) {
 wScale = Math.max(wScale, getScale(it.weight));
 vScale = Math.max(vScale, getScale(it.volume));
 }
 wScale = Math.max(wScale, getScale(maxweight));
 vScale = Math.max(vScale, getScale(maxvolume));

 const W = Math.round(maxweight * wScale);
 const V = Math.round(maxvolume * vScale);
 const weights = items.map(it => Math.round(it.weight * wScale));
 const volumes = items.map(it => Math.round(it.volume * vScale));
 const values = items.map(it => it.value);

 const dp = new Float64Array((W + 1) * (V + 1)).fill(0);

 for (let i = 0; i < items.length; i++) {
 const wi = weights[i], vi = volumes[i], val = values[i];
 for (let w = wi; w <= W; w++) {
 const row = w * (V + 1);
 const prev = (w - wi) * (V + 1);
 for (let v = vi; v <= V; v++) {
 const cand = dp[prev + v - vi] + val;
 if (cand > dp[row + v]) dp[row + v] = cand;
 }
 }
 }

 return dp[W * (V + 1) + V];
}
