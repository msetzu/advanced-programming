package com.github.msetzu.pa.graph;

public enum Operation {
	SUM,
	MULTIPLY;

	public static Matrix compute(Matrix a, Matrix b, Operation op) throws IllegalStateException {
		switch (op) {
			case SUM:
				if (a.getM() != b.getM() || a.getN() != a.getN()) throw new IllegalStateException();
				break;
			case MULTIPLY:
				if (a.getN() != b.getM()) throw new IllegalStateException();
				break;
		}

		Matrix r = new Matrix(0,0);

		switch (op) {
			case SUM:
				r = new Matrix(a.getM(), a.getN());
				
				for (int i = 0, m = a.getM(), n = a.getN(); i < m; i++) for (int j = 0; j < n; j++)
					r.set(i, j, a.get(i,j) + b.get(i,j));
			break;

			case MULTIPLY:
				r = new Matrix(a.getM(), b.getN());
				
				for (int i = 0, aM = a.getM(), aN = a.getN(); i < aM; i++)
					for (int j = 0, bN = b.getN(); j < bN; j++)
						for (int k = 0; k < aN; k++)
							r.set(i, j, r.get(i,j) + a.get(i, k) * b.get(k, j));
			break;
		}
		return r;
	}
}