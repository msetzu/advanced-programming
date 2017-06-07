package com.github.msetzu.pa.graph.test;

import java.util.List;

public class ComputationalNode extends Node<Operation> {
	private final Operation operation;
	private final List<Node> inputs;

	public ComputationalNode(String name, Operation operation, List<Node> inputs) {
		super(name);
		this.operation = operation;
		this.inputs = inputs;
	}

	public Operation operation() { return operation; }

	public List<Node> inputs() { return inputs; }
}
