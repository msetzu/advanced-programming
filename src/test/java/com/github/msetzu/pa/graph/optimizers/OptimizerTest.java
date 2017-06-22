package com.github.msetzu.pa.graph.optimizers;

import com.github.msetzu.pa.graph.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OptimizerTest {
	@Test
	public void optimize() throws Exception {
		InputNode nA = new InputNode("a", 1, 1, Optional.empty());
		InputNode nB = new InputNode("b", 1, 1, Optional.empty());
		InputNode nC = new InputNode("c", 1, 1, Optional.empty());
		InputNode nD = new InputNode("d", 1, 1, Optional.empty());

		List<Node> inputsE = new ArrayList<>();
		List<Node> inputsF = new ArrayList<>();
		List<Node> inputsG = new ArrayList<>();

		inputsE.add(nA);
		inputsE.add(nB);
		inputsF.add(nC);
		inputsF.add(nA);

		ComputationalNode nE = new ComputationalNode("e", Operation.SUM, inputsE);
		ComputationalNode nF = new ComputationalNode("f", Operation.MUL, inputsF);
		inputsG.add(nE);
		inputsG.add(nF);
		ComputationalNode nG = new ComputationalNode("g", Operation.MUL, inputsG);

		List<Node> roots = new ArrayList<>();
		roots.add(nG);

		ArrayList<Node> aRoots = new ArrayList<>();	aRoots.add(nA);
		Graph aGraph = new Graph(aRoots);

		ArrayList<Node> bRoots = new ArrayList<>();	bRoots.add(nB);
		Graph bGraph = new Graph(bRoots);

		ArrayList<Node> cRoots = new ArrayList<>();	cRoots.add(nC);
		Graph cGraph = new Graph(cRoots);

		ArrayList<Node> dRoots = new ArrayList<>();	dRoots.add(nD);
		Graph dGraph = new Graph(dRoots);

		ArrayList<Node> eRoots = new ArrayList<>();	eRoots.add(nE);
		ArrayList<Graph> eChildren = new ArrayList<>(); eChildren.add(aGraph); eChildren.add(bGraph);

		ArrayList<Node> fRoots = new ArrayList<>();	fRoots.add(nF);
		ArrayList<Graph> fChildren = new ArrayList<>(); fChildren.add(aGraph); fChildren.add(cGraph);

		Map<Node, List<Graph>> eMap = new HashMap<>();
		Map<Node, List<Graph>> fMap = new HashMap<>();
		eMap.put(nE, eChildren);
		fMap.put(nF, fChildren);

		Graph eGraph = new Graph(eRoots, eMap);
		Graph fGraph = new Graph(fRoots, fMap);

		ArrayList<Node> gRoots = new ArrayList<>();	gRoots.add(nG);
		ArrayList<Graph> gChildren = new ArrayList<>(); gChildren.add(eGraph); gChildren.add(fGraph);
		Map<Node, List<Graph>> gMap = new HashMap<>();
		gMap.put(nG, gChildren);

		Graph gGraph = new Graph(gRoots, gMap);


		assertEquals(nG.inputs().size(), 2);
		(new Optimizer()).optimize(gGraph);

		/*assertEquals((new Optimizer()).parents(nG, gGraph, new HashSet<>()).size(), 0);
		assertEquals((new Optimizer()).parents(nE, gGraph, new HashSet<>()).size(), 1);
		assertEquals((new Optimizer()).parents(nF, gGraph, new HashSet<>()).size(), 1);
		assertEquals((new Optimizer()).parents(nB, gGraph, new HashSet<>()).size(), 1);
		assertEquals((new Optimizer()).parents(nC, gGraph, new HashSet<>()).size(), 1);
		assertEquals((new Optimizer()).parents(nA, gGraph, new HashSet<>()).size(), 2);*/
	}

}