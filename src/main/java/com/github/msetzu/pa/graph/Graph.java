package com.github.msetzu.pa.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
	private List<Node> roots;
	private Map<Node, List<Graph>> children;

	public List<Node> getRoots() { return roots; }

	public Map<Node, List<Graph>> getChildren() { return children; }

	public Graph(Node root) { roots = new ArrayList<>(); roots.add(root); children = new HashMap<>(); }

	public Graph(List<Node> roots) {
		this.roots = roots; this.children = new HashMap<>();
		roots.forEach(root -> this.children.put(root, new ArrayList<>()));
	}

	public Graph(List<Node> roots, Map<Node, List<Graph>> children) { this.roots = roots; this.children = children; }

	public boolean equals(Object o) {
		try { return o instanceof Graph && roots.stream().allMatch(root ->
				((Graph) o).roots.stream().anyMatch(otherRoot -> otherRoot.name.equals(root.name)
				&& ((Graph) o).children.get(otherRoot).equals(children.get(root))));
		} catch (NullPointerException e) { return false; }
	}

	public List<Graph> childrenOf(Node root) { return children.getOrDefault(root, new ArrayList<>()); }

	public boolean isValid() {
		return validChildren() && hasFreeVariables(names(this))
			&& !(hasChildren(this)) && hasValidDimensions();
	}

	private boolean hasValidDimensions() {
		try {
			for (Node root : roots) dimensions(root, new HashMap<>());
			return true;
		} catch (IllegalArgumentException e) { return false; }
	}

	public Integer[] dimensions(Node n, Map<Node, Integer[]> dimensionsMap) throws IllegalArgumentException {
		if (n instanceof InputNode) {
			if (dimensionsMap.containsKey(n)) {
				if (dimensionsMap.get(n)[0] != n.getM() || dimensionsMap.get(n)[1] != n.getN())
					throw new IllegalArgumentException();

				return dimensionsMap.get(n);
		} else { dimensionsMap.put(n, new Integer[]{n.getM(), n.getN()}); return new Integer[]{n.getM(), n.getN()}; }
		} else {
			ComputationalNode node = (ComputationalNode) n;

			if (node.inputs().size() == 1) {
				dimensionsMap.put(node, dimensionsMap.get(node.inputs().get(0)));
				return dimensionsMap.get(node.inputs().get(0));
			}
			for (int i = 0, k = node.inputs().size(); i < k - 1; i++) {
				Integer[] leftOperand = dimensions(node.inputs().get(i), dimensionsMap);
				Integer[] rightOperand = dimensions(node.inputs().get(i + 1), dimensionsMap);
				Integer[] supposedDimensions = Operation.dimensions(node.operation(), leftOperand, rightOperand);

				if (!(dimensionsMap.containsKey(n))) {
					dimensionsMap.put(n, supposedDimensions);
					return supposedDimensions;
				} else {
					if (!Operation.isValid(node.operation(), leftOperand, rightOperand)) throw new IllegalArgumentException();
					else return supposedDimensions;
		}}}
		return dimensionsMap.get(n);
	}

	public Set<String> names(Graph g) {
		Set<String> s = g.roots.stream().map(Node::getName).collect(Collectors.toSet());
		g.roots.stream().filter(root -> g.children.get(root) != null)
				.map(g.children::get).forEach(children -> children.forEach(child -> s.addAll(names(child))));
		return s;
	}

	public boolean hasFreeVariables(Set<String> names) { return names.stream().allMatch(this::hasFreeVariables); }

	public boolean hasFreeVariables(String name) {
		return roots.stream().map(Node::getName).anyMatch(name::equals) || roots.stream().anyMatch(root ->
			children.getOrDefault(root, new ArrayList<>()).stream().anyMatch(child -> child.hasFreeVariables(name)));
	}

	public boolean hasChildren(Graph g) {
		return roots.stream().anyMatch(root -> children.getOrDefault(root, new ArrayList<>()).stream().anyMatch(child -> child.equals(g) || child.hasChildren(g)));
	}

	private boolean validChildren() { return children.size() == 0 || roots.stream().allMatch(root -> children.get(root).stream().allMatch(Graph::isValid)); }
}