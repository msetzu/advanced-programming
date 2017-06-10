package com.github.msetzu.pa.graph;

import java.util.List;

public class ComputationalNode extends Node<Operation> {
	private final List<Node> inputs;

	public ComputationalNode(String name, Operation operation, List<Node> inputs) {
		super(name, -1, -1);
		put(name, operation);
		this.inputs = inputs;
	}

	public Operation operation() { return this.get(this.name); }

	public List<Node> inputs() { return inputs; }
}