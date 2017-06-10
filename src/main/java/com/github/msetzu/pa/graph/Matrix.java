package com.github.msetzu.pa.graph;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Matrix {
	private final float[][] matrix;
	private final int m;
	private final int n;

	public Matrix(int m, int n) {
		this.matrix = new float[m][n]; this.m = m; this.n = n;
		IntStream.range(0,m).forEach(row -> Arrays.fill(matrix[row], 0));
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Matrix)) return false;
		Matrix other = (Matrix) obj;

		return (other.getM() == this.m && other.getN() == this.n) && IntStream.range(0, m)
				.allMatch(row -> Arrays.equals(this.matrix[row], ((Matrix)other).matrix[row]));
	}

	public int getM() { return m; }

	public int getN() { return n; }

	public void set(int i, int j, float e) { matrix[i][j] = e; }

	public float[][] matrix() { return matrix; }
}