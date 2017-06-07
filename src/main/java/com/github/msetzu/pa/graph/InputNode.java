package com.github.msetzu.pa.graph;

import java.util.Optional;

public class InputNode extends Node<Optional<Matrix>> {
	public InputNode(String name) { super(name); }

	public InputNode(String name, Matrix val) {
		super(name);
		this.put(name, Optional.of(val));
	}
}
