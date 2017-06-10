package com.github.msetzu.pa.graph;

import java.util.Optional;

public class InputNode extends Node<Optional<Matrix>> {
	public InputNode(String name, int m, int n, Optional<Matrix> val) {
		super(name, m, n);
		this.put(name, val);
	}

	public Optional<Matrix> get() { return super.get(name); }
}