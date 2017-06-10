package com.github.msetzu.pa.graph.test;

import com.github.msetzu.pa.graph.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class NodeTest {
	/*@Test
	public void nodeTest() {
		InputNode nA = new InputNode("a", Optional.empty());
		InputNode nB = new InputNode("b", Optional.empty());
		InputNode nC = new InputNode("c", Optional.empty());
		
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

		ComputationalNode nF = new ComputationalNode("f", Operation.MUL, inputsF);

		assertTrue(nA.getName().equals("a"));
		assertTrue(nB.getName().equals("b"));
		assertTrue(nC.getName().equals("c"));
		//assertTrue(nD.getName().equals("d"));
		assertTrue(nE.getName().equals("e"));
		assertTrue(nF.getName().equals("f"));

		//assertTrue(nD.inputs().equals(inputsD));
		assertTrue(nE.inputs().equals(inputsE));
		assertTrue(nF.inputs().equals(inputsF));
	}*/
}