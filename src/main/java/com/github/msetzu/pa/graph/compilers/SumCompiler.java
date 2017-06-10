package com.github.msetzu.pa.graph.compilers;

import java.util.List;

class SumCompiler extends OperationCompiler {
	String compile(List<String> operands) {
		return operands.stream().reduce("", (a, b) -> val(a) + " + " + val(b)).substring(3);
	}
}