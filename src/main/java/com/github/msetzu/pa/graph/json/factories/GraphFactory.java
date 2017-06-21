package com.github.msetzu.pa.graph.json.factories;

import com.github.msetzu.pa.graph.*;
import com.github.msetzu.pa.graph.json.Token;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.*;

public class GraphFactory {
	private Map<String, Node> nodes = new HashMap<>();
	private Map<String, Map<Token, String>> map;
	private final Map<String, Graph> graphs = new HashMap<>();

	GraphFactory(Map<String, Map<Token, String>> map) { this.map = map; }

	public Optional<Graph> graph() throws IllegalArgumentException {
		try {
			map.keySet().forEach(this::node);
			return Optional.of(new Graph(roots(), childrenOf(roots())));
		} catch (ClassCastException e) { return Optional.empty(); }
	}

	private Optional<Graph> node(String nodeName) {
		if ((graphs.containsKey(nodeName))) return Optional.of(graphs.get(nodeName));
		else {
			Graph g;
			Node node;
			Map<Token, String> nodeMap = map.get(nodeName);
			switch (nodeMap == null ? "INPUT" : map.get(nodeName).get(Token.TYPECASE)) {
				case "INPUT":
					String[] shape = nodeMap == null ? new String[]{"-1", "-1"} : nodeMap.get(Token.SHAPE).split(" ");
					final int m = Integer.parseInt(shape[0]);
					final int n = Integer.parseInt(shape[1]);
					node = nodeMap == null 	? new InputNode(nodeName, m, n, Optional.of(matrixOf(nodeName)))
											: new InputNode(nodeName, m, n, Optional.empty());

					g = new Graph(node);
					nodes.put(nodeName, node);
					graphs.put(nodeName, g);
					return Optional.of(g);
				default:
					Operation op = nodeMap.get(Token.OP).equals("SUM") ? Operation.SUM : Operation.MUL;
					Set<String> childrenString = Arrays.stream(nodeMap.get(Token.INPUT).split("~")).collect(toSet());
					List<Graph> childrenGraphs = new ArrayList<>();
					List<String> vacantChildren = childrenString.stream().filter(c -> !(graphs.containsKey(c)))
														.collect(toList());
					List<String> builtChildren = childrenString.stream().filter(graphs::containsKey).collect(toList());

					builtChildren.forEach(child -> childrenGraphs.add(graphs.get(child)));
					vacantChildren.stream().map(this::node).filter(Optional::isPresent).map(Optional::get)
						.forEach(child -> {
							childrenGraphs.add(child);
							graphs.put(child.getRoots().get(0).getName(), child);
					});

					node = new ComputationalNode(nodeName, op, childrenString.stream().map(nodes::get).collect(toList()));
					nodes.put(nodeName, node);
					List<Node> roots = new ArrayList<>(); roots.add(node);
					HashMap<Node, List<Graph>> children = new HashMap<>();

					g = new Graph(roots, children);
					children.put(node, childrenGraphs);
					graphs.put(nodeName, g);
					return Optional.of(g);
		}}
	}

	private Matrix matrixOf(String nodeName) {
		int m = (int) nodeName.chars().mapToObj(i -> (char) i).filter(c -> c == ']').count() - 1;
		int n;
		List<Long> cols = Arrays.stream(nodeName.split("], *\\[")).map(s -> s.chars().mapToObj(i -> (char) i)
								.filter(c -> c == ',').count()).distinct().collect(toList());

		if (cols.size() > 1) throw new IllegalArgumentException();
		else n = Math.toIntExact(cols.get(0)) + 1;

		Matrix matrix = new Matrix(m, n);
		List<String[]> entries = Arrays.stream(nodeName.split("], *\\[")).map(s -> s.replace("[", "")
						.replace("]", "")).map(s -> s.split(",")).collect(toList());

		for (int i=0; i<m; i++) for (int j=0; j<n; j++) matrix.set(i, j, Float.parseFloat(entries.get(i)[j]));
		return matrix;
	}

	private List<Node> roots() {
		Map<Node, Boolean> bitMap = new HashMap<>();
		nodes.values().forEach(node -> bitMap.put(node, true));

		nodes.values().stream().filter(ComputationalNode.class::isInstance).map(ComputationalNode.class::cast)
						.forEach(node -> node.inputs().forEach(child -> bitMap.put(child, false)));

		return bitMap.keySet().stream().filter(bitMap::get).map(ComputationalNode.class::cast).collect(toList());
	}

	private Map<Node, List<Graph>> childrenOf(List<Node> roots) throws ClassCastException {
		Map<Node, List<Graph>> m = new HashMap<>();

		roots.stream().map(ComputationalNode.class::cast).forEach(root ->
			m.put(root, root.inputs().stream().map(Node::getName).map(graphs::get).collect(toList())));
		return m;
	}
}