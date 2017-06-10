package com.github.msetzu.pa.graph;

public enum Operation {
	SUM, MUL;

	public static boolean isValid(Operation op, Integer[] lOp, Integer[] rOp) {
		switch (op) {case SUM: return lOp[0] == rOp[0] && lOp[1] == rOp[1]; default: return lOp[1] == rOp[0];}
	}

	public static Integer[] dimensions(Operation op, Integer[] lOp, Integer[] rOp) {
		switch (op) { case SUM: return lOp; default:	return new Integer[]{lOp[0], rOp[1]}; }
	}
}