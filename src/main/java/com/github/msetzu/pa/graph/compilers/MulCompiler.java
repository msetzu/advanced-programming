package com.github.msetzu.pa.graph.compilers;

import java.util.Collections;
import java.util.List;

class MulCompiler extends OperationCompiler {
	String compile(List<String> operands) {
		String mul = "numpy.matmul(";
		StringBuilder res = new StringBuilder("");
		res = res.append(operands.size() > 2 ? Collections.nCopies(operands.size(), mul)
				.stream().reduce(String::concat).get() : mul);
		res = res.append(val(operands.get(0))).append(", ").append(val(operands.get(1))).append(")");

		if (operands.size() > 2)
			res =  res.append(operands.subList(2, operands.size()).stream().map(o -> ", " + val(o) + ")").reduce("", String::concat));

		return operands.size() > 2 ? res.substring(mul.length()) : res.toString();
	}
}