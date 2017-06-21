package com.github.msetzu.pa.graph;

import java.util.HashMap;

public abstract class Node<V> extends HashMap<String, V> {
	String name;
	private int m;
	private int n;

	int getM() { return m; }

	int getN() { return n; }

	protected Node(String name, int m, int n) { this.name = name; this.m = m; this.n = n; }

	public String getName() { return name; }
}