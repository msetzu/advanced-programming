package com.github.msetzu.pa.graph;

import java.util.HashMap;

public abstract class Node<V> extends HashMap<String, V> {
	protected String name;
	protected int m;
	protected int n;

	public int getM() { return m; }

	public int getN() { return n; }

	protected Node(String name, int m, int n) { this.name = name; this.m = m; this.n = n; }

	public String getName() { return name; }
}