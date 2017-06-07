package com.github.msetzu.pa.graph;

public class Matrix {
	private final float[][] matrix;
	private final int m;
	private final int n;

	public Matrix(int m, int n) {
		this.matrix = new float[m][n];
		this.m = m;
		this.n = n;

		for (int i = 0; i < m; i++) for (int j = 0; j < n; j++)
			matrix[i][j] = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Matrix)) return false;
		Matrix other = (Matrix) obj;
		
		if (other.getM() != this.m || other.getN() != this.n) return false;

		for (int i = 0; i < this.m; i++) {
			for (int j = 0; j < this.n; j++) {
				if (this.matrix[i][j] != other.matrix[i][j])
					return false;
			}
		}

		return true;
	}
	
	public int getM() { return m; }

	public int getN() { return n; }

	float get(int i, int j) throws IllegalArgumentException {
		if (i >= m || j >= n)
			throw new IllegalArgumentException();

		return matrix[i][j];
	}

	void set(int i, int j, float e) throws IllegalArgumentException {
		if (i >= m || j >= n)
			throw new IllegalArgumentException();

		matrix[i][j] = e;
	}
}