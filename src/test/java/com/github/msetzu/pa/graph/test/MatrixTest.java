package com.github.msetzu.pa.graph.test;

import com.github.msetzu.pa.graph.Matrix;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MatrixTest {
	@Test
	public void matrixTest() {
		int n = (new Random()).nextInt(9) + 1;
		int m = (new Random()).nextInt(9) + 1;
		Matrix a = new Matrix(m, n);
		Matrix b = new Matrix(m, n);

		assertEquals(a, a);
		assertEquals(b, b);
		assertEquals(a, b);
		assertEquals(b, a);
	}
}