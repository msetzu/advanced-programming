package com.github.msetzu.pa.graph.compilers;

import com.github.msetzu.pa.graph.ComputationalNode;
import java.util.List;

// Compile the suited statement according to the operation
public class OperationCompiler {
	public String compile(ComputationalNode node, List<String> operands) {
		SumCompiler sumCompiler = new SumCompiler();
		MulCompiler mulCompiler = new MulCompiler();

		switch (node.operation()) {
			case SUM: return sumCompiler.compile(operands);
			default: return mulCompiler.compile(operands);
		}
	}

	String val(String raw) { return raw.startsWith("[") ? "numpy.matrix('" + raw + "')" : raw; }
}