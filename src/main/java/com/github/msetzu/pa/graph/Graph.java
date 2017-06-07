package com.github.msetzu.pa.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

public class Graph extends HashSet<Node> {
	private Node root;
	private HashSet<Graph> children;

	public Graph(Node... nodes) { this.addAll(Arrays.asList(nodes));	}

	public boolean isValid() {
		Stream<String> names = children.stream().map((graph) -> graph.root.getName());


		// Name
		boolean freeVariables = hasFreeVariable(names);
		// Acyclic
		boolean acyclic = !(hasChildren(this));
		// Children validity
		boolean childrenValidity = validChildren();

		return acyclic && childrenValidity && freeVariables;
	}

	private boolean hasFreeVariable(Stream<String> names) {
		return names.allMatch(name -> children.stream().anyMatch(child -> child.hasFreeVariable(name)));
	}

	private boolean hasFreeVariable(String name) {
		return root.name.equals(name) || children.stream().anyMatch(child -> child.hasFreeVariable(name));
	}

	private boolean hasChildren(Graph g) {
		return root.name.equals(g.root.getName()) || children.stream().anyMatch(child -> child.hasChildren(g));
	}

	private boolean validChildren() {
		return children.size() == 0 || children.stream().allMatch(Graph::validChildren);
	}
}
