package com.github.msetzu.pa.graph.optimizers;

import com.github.msetzu.pa.graph.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

public class Optimizer {
	public Graph optimize(Graph g) {
		g.getRoots().forEach(root -> optimize(root, g));
		return g;
	}

	private void optimize(Node node, Graph nodeGraph) {
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
			} else {
				int pos = compNode.inputs().indexOf(childRoot);

				compNode.inputs().remove(pos);						// Remove old child
				compNode.inputs().addAll(pos, compChild.inputs());	// Replace with its children
				nodeGraph.getChildren().get(compNode).remove(pos);							// Remove old graph
				nodeGraph.getChildren().put(compNode, merge(node, nodeGraph, child, pos));	// Replace with its children
			}
			boundaryGraphs.forEach(boundary -> optimize(childRoot, child));
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
}