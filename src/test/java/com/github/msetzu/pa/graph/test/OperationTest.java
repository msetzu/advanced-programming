package com.github.msetzu.pa.graph.test;

import com.github.msetzu.pa.graph.Matrix;
import com.github.msetzu.pa.graph.Operation;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;


public class OperationTest {
	@Test
	public void compute() throws Exception {
		int n = (new Random()).nextInt(9) + 1;
		int m = (new Random()).nextInt(9) + 1;
		Matrix a = new Matrix(m, n);
		Matrix b = new Matrix(m, n);

		assertEquals(a.getM(), Operation.compute(a, a, Operation.SUM).getM());

		//System.out.println("RES: " + Operation.compute(a, a, Operation.SUM).get());
		assertEquals(a, Operation.compute(a, a, Operation.SUM));
		assertEquals(b, Operation.compute(b, b, Operation.SUM));
	}

}