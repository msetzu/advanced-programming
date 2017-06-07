package com.github.msetzu.pa.graph.test;

import java.util.List;
import java.util.Optional;

enum Operation {
	SUM,
	MINUS,
	MULTIPLY;

	public Optional<Float> compute(List<Float> factors) { return factors.stream().reduce((a, b) -> a + b); }
	public Optional<Float> minus(List<Float> factors) { return factors.stream().reduce((a, b) -> a - b); }
	public Optional<Float> multiply(List<Float> factors) { return factors.stream().reduce((a, b) -> a * b); }
}
