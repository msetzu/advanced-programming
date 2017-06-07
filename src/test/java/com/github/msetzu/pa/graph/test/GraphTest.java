package com.github.msetzu.pa.graph.test;

import com.github.msetzu.pa.graph.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {
	@Test
	public void graphTest() {
		InputNode nA = new InputNode("a");
		InputNode nB = new InputNode("b");
		InputNode nC = new InputNode("c");

		//List<Node> inputsD = new ArrayList<>();
		List<Node> inputsE = new ArrayList<>();

		//inputsD.add(nA);
		//inputsD.add(nB);
		inputsE.add(nC);

		//ComputationalNode nD = new ComputationalNode("d", Operation.MINUS, inputsD);
		ComputationalNode nE = new ComputationalNode("e", Operation.SUM, inputsE);

		List<Node> inputsF = new ArrayList<>();
		//inputsF.add(nD);
		inputsF.add(nE);

		ComputationalNode nF = new ComputationalNode("f", Operation.MULTIPLY, inputsF);
		ComputationalNode nG = new ComputationalNode("g", Operation.MULTIPLY, inputsF);

		Graph g = new Graph();

		assertFalse(g.contains(nA));
		assertFalse(g.contains(nB));
		assertFalse(g.contains(nC));
		//assertFalse(g.contains(nD));
		assertFalse(g.contains(nE));
		assertFalse(g.contains(nF));
		assertTrue(g.size() == 0);

		g = new Graph(nA, nB, nC, nE, nF);
		assertTrue(g.contains(nA));
		assertTrue(g.contains(nB));
		assertTrue(g.contains(nC));
		//assertTrue(g.contains(nD));
		assertTrue(g.contains(nE));
		assertTrue(g.contains(nF));
		assertTrue(g.size() == 5);

		assertFalse(g.contains(nG));
	}

}