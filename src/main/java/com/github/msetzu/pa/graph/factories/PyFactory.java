package com.github.msetzu.pa.graph.factories;

import com.github.msetzu.pa.graph.*;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class PyFactory {
	private List<String> statements = new ArrayList<>();
	private List<String> graphParameters = new ArrayList<>();
	private List<String> constants = new ArrayList<>();
	private List<String> foos = new ArrayList<>();
	private StringBuilder py = new StringBuilder("");

	public String generate(Graph g) {
		List<Node> roots = g.getRoots();
		roots.forEach(this::generate);

		// Header
		py = py.append(header());
		// Parameters
		IntStream.range(0, graphParameters.size() - 1).forEach(i ->
						py = py.append("\t").append(graphParameters.get(i))
							.append(" = numpy.matrix(sys.argv[").append(i + 1).append("])\n"));
		// Constants
		constants.forEach(c -> py = py.append(c));
		// Assignments
		for (int i = statements.size() - 1; i >= 0; i--) py = py.append("\t").append(statements.get(i));
		// return
		py = py.append("\treturn ").append("[").append(seq(roots.stream().map(Node::getName).collect(toList()))).append("]\n\n");
		// Auxiliary functions
		foos.forEach(foo -> py = py.append(foo));
		return py.toString();
	}

	private void generate(Node node) {
		String name = node.getName();

		// ComputationalNode
		if (node instanceof ComputationalNode) {
			ComputationalNode compNode = (ComputationalNode) node;
			Operation op = compNode.operation();
			List<Node> children = compNode.inputs();
			List<Node> parametrizedChildren = children.stream().filter(child ->
											child instanceof InputNode && !(((InputNode) child).get().isPresent()))
											.collect(toList());

			if (parametrizedChildren.size() == 0) {
				statements.add(assign(name, operation(op, children.stream().map(Node::getName).collect(toList()))));
				children.forEach(this::generate);
			} else {
				foos.add(foo(name, children.stream().map(Node::getName).collect(toList()), op));
				statements.add(fooAssignment(name, parametrizedChildren.stream().map(Node::getName).collect(toList())));
				children.forEach(this::generate);
			}
		} else {
			// InputNode
			if (node instanceof InputNode && ((InputNode) node).get().isPresent())
				constants.add(valAssignment(name, matrix(((InputNode) node).get().get().matrix())));
			// Parametrized InputNode
			if (node instanceof InputNode && !((InputNode) node).get().isPresent()) graphParameters.add(name);
		}
	}

	private String header() { return "import numpy\n\nif __name__ == \"__main__\":\n"; }

	private String assign(String name, String val) { return name + " = " + val + "\n"; }

	private String valAssignment(String assigning, String assigned) {
		return assigning + " = numpy.matrix('" + assigned + "')\n";
	}

	private String fooAssignment(String nodeName, List<String> parameters) {
		return nodeName + " = " + nodeName + "_foo(" + seq(parameters)  + ")\n";
	}

	public String operation(Operation op, List<String> operands) {
		switch (op) {
			case SUM: return operands.stream().reduce("", (a, b) -> a + " + " + b).substring(3);
			default: // ("numpy("){k - 1} + "op1,op2)" + (", " + op_i + ")"){k - 2}
				String mul = "numpy.matmul(";
				StringBuilder res = new StringBuilder("");
				res = res.append(operands.size() > 2 ? Collections.nCopies(operands.size() - 2, mul) : mul);
				res = res.append(operands.get(0)).append(", ").append(operands.get(1)).append(")");

				if (operands.size() > 2) res = res.append(operands.subList(2, operands.size()).stream()
												.map(o -> ", " + o + ")").reduce(String::concat));

				return operands.size() > 2 ? res.substring(mul.length()) : res.toString();
		}
	}

	private String matrix(float[][] m) {
		StringBuilder s = new StringBuilder("[");

		for (float[] row : m) {
			for (Float f : row) { s.append(f).append(" "); }
			s.setLength(s.length() - 1);
			s.append("; ");
		}
		s.setLength(s.length() - 2); s.append("]");
		return s.toString();
	}

	private String foo(String nodeName, List<String> parameters, Operation op) {
		return	"def " + nodeName + "_foo(" + seq(parameters)  + "):\n" +
				"\treturn " + operation(op, parameters) + "\n\n";
	}

	private String seq(List<String> elements) {
		String s = elements.stream().map(p -> p + ", ").reduce(String::concat).get();
		return s.substring(0, s.length() - 2);
	}
}