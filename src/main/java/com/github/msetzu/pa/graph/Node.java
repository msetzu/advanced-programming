package com.github.msetzu.pa.graph;

import java.util.HashMap;

public abstract class Node<V> extends HashMap<String, V> {
	protected String name;

	protected Node(String name) { this.name = name; }

	public String getName() { return name; }

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Node && this.name.equals(((Node)obj).name);
	}
}
