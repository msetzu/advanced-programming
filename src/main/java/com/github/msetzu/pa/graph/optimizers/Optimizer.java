package com.github.msetzu.pa.graph.optimizers;

import com.github.msetzu.pa.graph.*;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.stream.Stream;

public class Optimizer {
	public Graph optimize(Graph g) {
		g.getRoots().forEach(root -> optimize(root, g, g));
		return g;
	}

	private void optimize(Node node, Graph nodeGraph, Graph g) {
		if (node instanceof InputNode) return;

		ArrayList<Graph> boundaryGraphs = new ArrayList<>();
		ComputationalNode compNode = (ComputationalNode) node;
		Iterator<Graph> i = nodeGraph.childrenOf(node).stream().filter(child -> child.getRoots().get(0)
							instanceof ComputationalNode).iterator();

		while (i.hasNext()) {
			Graph child = i.next();
			Node childRoot = child.getRoots().get(0);
			ComputationalNode compChild = (ComputationalNode) childRoot;

			if (!(compChild.operation().equals((compNode.operation())))) {
				boundaryGraphs.add(child);
			} else if (parents(childRoot, g, new HashSet<>()).size() == 1) {
				int pos = compNode.inputs().indexOf(childRoot);

				compNode.inputs().remove(pos);						// Remove old child
				compNode.inputs().addAll(pos, compChild.inputs());	// Replace with its children
				nodeGraph.getChildren().get(compNode).remove(pos);							// Remove old graph
				nodeGraph.getChildren().put(compNode, merge(node, nodeGraph, child, pos));	// Replace with its children
			}
			boundaryGraphs.forEach(boundary -> optimize(childRoot, child, g));
		}
	}

	private List<Graph> merge(Node parent, Graph graph, Graph child, int position) {
		Node childRoot = child.getRoots().get(0);
		List<Graph> newChildren = graph.getChildren().get(parent);
		List<Graph> previousChildren = newChildren.subList(0, position);
		List<Graph> followingChildren = newChildren.subList(position, newChildren.size());

		return Stream.of(previousChildren, child.childrenOf(childRoot), followingChildren)
						.flatMap(List::stream).collect(toList());
	}

	private Set<ComputationalNode> parents(Node n, Graph g, Set<ComputationalNode> parents) {
		parents.addAll(g.getRoots().stream().filter(ComputationalNode.class::isInstance)
						.map(ComputationalNode.class::cast).filter(r -> r.inputs().contains(n))
						.collect(toList()));
		g.getRoots().stream().filter(ComputationalNode.class::isInstance).map(ComputationalNode.class::cast)
					.forEach(r -> parents.addAll(g.childrenOf(r).stream().map(c -> parents(n, c, parents))
					.flatMap(Set::stream).collect(toSet())));
		return parents;
	}
}