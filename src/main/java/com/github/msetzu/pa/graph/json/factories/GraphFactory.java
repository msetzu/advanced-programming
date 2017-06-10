package com.github.msetzu.pa.graph.json.factories;

import java.util.*;
import com.github.msetzu.pa.graph.*;
import com.github.msetzu.pa.graph.json.Token;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class GraphFactory {
	private Set<Node> nodes = new HashSet<>();
	private Map<String, Map<Token, String>> map;
	private final Map<String, Graph> builtNodes = new HashMap<>();

	public Optional<Graph> graph(Map<String, Map<Token, String>> map) throws IllegalArgumentException {
		this.map = map;
		try {
			for (String k : map.keySet())  { node(k); }

			return Optional.of(new Graph(roots(), childrenOf(roots())));
		} catch (ClassCastException e) { return Optional.empty(); }
	}

	private Optional<Graph> node(String nodeName) {
		Graph g = new Graph();

		if (!(builtNodes.containsKey(nodeName))) {
			Node node;
			Map<Token, String> nodeMap = map.get(nodeName);
			switch (nodeMap == null ? "INPUT" : map.get(nodeName).get(Token.TYPECASE)) {
				case "INPUT":
					String[] shape = nodeMap == null ? new String[]{"-1", "-1"} : nodeMap.get(Token.SHAPE).split(" ");
					final int m = Integer.parseInt(shape[0]);
					final int n = Integer.parseInt(shape[1]);
					node = nodeMap == null ? new InputNode(nodeName, m, n, Optional.of(matrixOf(nodeName)))
							: new InputNode(nodeName, m, n, Optional.empty());
					g = new Graph(node);
					nodes.add(node);
					builtNodes.put(nodeName, g);
					break;
				case "COMP":
					Operation op = nodeMap.get(Token.OP).equals("SUM") ? Operation.SUM : Operation.MUL;
					Set<String> childrenString = Arrays.stream(nodeMap.get(Token.INPUT).replace("\"", "")
												.split("~")).collect(toSet());

					List<Graph> childrenGraphs = new ArrayList<>();
					for (String child : childrenString) {
						if (!(builtNodes.containsKey(child))) {
							Optional<Graph> childGraph = node(child);
							if (!childGraph.isPresent()) return Optional.empty();
							childrenGraphs.add(childGraph.get()); builtNodes.put(child, childGraph.get());
						} else { childrenGraphs.add(builtNodes.get(child)); }
					}

					List<Node> childrenNodes = new ArrayList<>();
					childrenString.forEach(childName -> childrenNodes.add(nodes.stream().filter(childNode ->childNode.getName().equals(childName)).findFirst().get()));

					node = new ComputationalNode(nodeName, op, childrenNodes); nodes.add(node);

					List<Node> roots = new ArrayList<>(); roots.add(node);
					HashMap<Node, List<Graph>> children = new HashMap<>();
					children.put(node, childrenGraphs);
					g = new Graph(roots, children);
					builtNodes.put(nodeName, g);
					break;
		}} else g = builtNodes.get(nodeName);
		return Optional.of(g);
	}

	private Matrix matrixOf(String nodeName) {
		int m = (int) nodeName.chars().mapToObj(i -> (char) i).filter(c -> c == ']').count() - 1;
		int n = 1;
		List<Long> cols = Arrays.stream(nodeName.split("], *\\[")).map(s -> s.chars().mapToObj(i -> (char) i)
						.filter(c -> c == ',').count()).distinct().collect(toList());

		if (cols.size() > 1) throw new IllegalArgumentException();
		else n = Math.toIntExact(cols.get(0)) + 1;

		Matrix matrix = new Matrix(m, n);
		List<String[]> entries = Arrays.stream(nodeName.split("], *\\[")).map(s -> s.replace("[", "")
						.replace("]", "")).map(s -> s.split(",")).collect(toList());

		for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) matrix.set(i, j, Float.parseFloat(entries.get(i)[j]));
		return matrix;
	}

	private List<Node> roots() {
		Map<Node, Boolean> bitMap = new HashMap<>();
		nodes.forEach(node -> bitMap.put(node, true));

		nodes.stream().filter(ComputationalNode.class::isInstance).map(ComputationalNode.class::cast)
			.forEach(node -> node.inputs().forEach(child -> bitMap.put(child, false)));

		return bitMap.keySet().stream().filter(bitMap::get).map(ComputationalNode.class::cast).collect(toList());
	}

	private Map<Node, List<Graph>> childrenOf(List<Node> roots) throws ClassCastException {
		Map<Node, List<Graph>> m = new HashMap<>();

		roots.forEach(root -> {
			List<Graph> children = new ArrayList<>();
			((ComputationalNode)root).inputs().forEach(child -> children.add(builtNodes.get(child.getName())));
			m.put(root, children);
		});
		return m;
	}
}
