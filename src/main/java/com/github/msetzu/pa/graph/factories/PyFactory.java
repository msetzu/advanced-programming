package com.github.msetzu.pa.graph.factories;

import com.github.msetzu.pa.graph.*;
import com.github.msetzu.pa.graph.compilers.OperationCompiler;

import java.util.*;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;

public class PyFactory {
	private List<String> statements = new ArrayList<>();
	private List<String> graphParameters = new ArrayList<>();
	private StringBuilder py = new StringBuilder("");
	private Set<Node> generatedNodes = new HashSet<>();
	private OperationCompiler compiler = new OperationCompiler();

	public String generate(Graph g) {
		List<Node> roots = g.getRoots();
		roots.forEach(this::generate);

		py = py.append(header());
		IntStream.range(0, graphParameters.size()).forEach(i ->
						py = py.append("\t").append(graphParameters.get(i))
							.append(" = numpy.matrix(sys.argv[").append(i + 1).append("])\n"));
		// Assignments
		for (int i = statements.size() - 1; i >= 0; i--) py = py.append("\t").append(statements.get(i));
		// return
		py = py.append("\treturn ").append("[").append(seq(roots.stream().map(Node::getName).collect(toList()))).append("]\n\n");

		return py.toString();
	}

	private void generate(Node node) {
		if (generatedNodes.contains(node)) return;
		String name = node.getName();

		if (node instanceof ComputationalNode) {
			ComputationalNode compNode = (ComputationalNode) node;
			List<Node> children = compNode.inputs();

			statements.add(assign(name, compiler.compile(compNode, children.stream().map(Node::getName).collect(toList()))));
			children.forEach(this::generate);
		} else
			if (!(name.startsWith("["))) graphParameters.add(name);

		generatedNodes.add(node);
	}

	private String header() { return "import numpy\n\nif __name__ == \"__main__\":\n"; }

	private String assign(String name, String val) { return name + " = " + val + "\n"; }

	private String seq(List<String> elements) {
		String s = elements.stream().map(p -> p + ", ").reduce("", String::concat);
		return s.substring(0, s.length() - 2);
	}
}