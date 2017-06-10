package com.github.msetzu.pa.graph.test;

import com.github.msetzu.pa.graph.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GraphTest {
	@Test
	public void graphTest() {
		InputNode nA = new InputNode("a", 1, 1, Optional.empty());
		InputNode nB = new InputNode("b", 1, 1, Optional.empty());
		InputNode nC = new InputNode("c", 1, 1, Optional.empty());
		InputNode nD = new InputNode("d", 1, 1, Optional.empty());

		List<Node> inputsE = new ArrayList<>();
		List<Node> inputsG = new ArrayList<>();
		List<Node> inputsF = new ArrayList<>();

		inputsE.add(nA);
		inputsE.add(nB);
		inputsF.add(nA);
		inputsF.add(nC);

		ComputationalNode nE = new ComputationalNode("e", Operation.SUM, inputsE);
		ComputationalNode nF = new ComputationalNode("f", Operation.MUL, inputsF);
		inputsG.add(nE);
		inputsG.add(nF);
		ComputationalNode nG = new ComputationalNode("g", Operation.MUL, inputsG);

		List<Node> roots = new ArrayList<>();
		roots.add(nG);

		ArrayList<Node> aRoots = new ArrayList<>();	aRoots.add(nA);
		Graph aGraph = new Graph(aRoots);
		assertEquals(aGraph.getRoots().size(), 1);
		assertEquals(aGraph.getRoots(), aRoots);
		assertEquals(aGraph.getChildren().get(nA).size(), 0);

		ArrayList<Node> bRoots = new ArrayList<>();	bRoots.add(nB);
		Graph bGraph = new Graph(bRoots);
		assertEquals(bGraph.getRoots().size(), 1);
		assertEquals(bGraph.getRoots(), bRoots);
		assertEquals(bGraph.getChildren().get(nB).size(), 0);

		ArrayList<Node> cRoots = new ArrayList<>();	cRoots.add(nC);
		Graph cGraph = new Graph(cRoots);
		assertEquals(cGraph.getRoots().size(), 1);
		assertEquals(cGraph.getRoots(), cRoots);
		assertEquals(cGraph.getChildren().get(nC).size(), 0);

		ArrayList<Node> dRoots = new ArrayList<>();	dRoots.add(nD);
		Graph dGraph = new Graph(dRoots);
		assertEquals(dGraph.getRoots().size(), 1);
		assertEquals(dGraph.getRoots(), dRoots);
		assertEquals(dGraph.getChildren().get(nD).size(), 0);

		ArrayList<Node> eRoots = new ArrayList<>();	eRoots.add(nE);
		ArrayList<Graph> eChildren = new ArrayList<>(); eChildren.add(aGraph); eChildren.add(bGraph);
		assertEquals(eChildren.size(), 2);

		ArrayList<Node> fRoots = new ArrayList<>();	fRoots.add(nF);
		ArrayList<Graph> fChildren = new ArrayList<>(); fChildren.add(aGraph);
		assertEquals(fChildren.size(), 1);
		fChildren.add(cGraph);
		assertEquals(fChildren.size(), 2);


		Map<Node, List<Graph>> eMap = new HashMap<>();
		Map<Node, List<Graph>> fMap = new HashMap<>();
		eMap.put(nE, eChildren);
		fMap.put(nF, fChildren);

		Graph eGraph = new Graph(eRoots, eMap);
		assertEquals(eGraph.getRoots().size(), 1);
		assertEquals(eGraph.getRoots(), eRoots);
		assertEquals(eGraph.getChildren().get(nE), eChildren);
		assertEquals(eChildren.size(), 2);
		assertEquals(eGraph.getChildren().get(nE).size(), 2);

		Graph fGraph = new Graph(fRoots, fMap);
		assertEquals(fGraph.getRoots().size(), 1);
		assertEquals(fGraph.getRoots(), fRoots);
		assertEquals(fGraph.getChildren().get(nF).size(), 2);
		//fGraph.getRoots().forEach(root -> System.out.println(root.getName() + ":" + ((ComputationalNode)root).inputs().size()));

		ArrayList<Node> gRoots = new ArrayList<>();	gRoots.add(nG);
		ArrayList<Graph> gChildren = new ArrayList<>(); gChildren.add(eGraph); gChildren.add(fGraph);
		Map<Node, List<Graph>> gMap = new HashMap<>();
		gMap.put(nG, gChildren);

		Graph gGraph = new Graph(gRoots, gMap);
		assertEquals(gGraph.getRoots().size(), 1);
		assertEquals(gGraph.getRoots(), gRoots);
		assertEquals(gGraph.getChildren().get(nG).size(), 2);

		assertNotEquals(aGraph, bGraph);
		assertNotEquals(bGraph, cGraph);
		assertNotEquals(aGraph, cGraph);
		assertNotEquals(aGraph, dGraph);
		assertNotEquals(bGraph, dGraph);
		assertTrue(gChildren.size() == 2);
		assertTrue(gMap.get(nG).size() == 2);
		
		assertTrue(aGraph.names(aGraph).stream().anyMatch("a"::equals));
		assertTrue(aGraph.names(aGraph).stream().noneMatch("b"::equals));
		assertTrue(eGraph.names(aGraph).stream().noneMatch("f"::equals));
		assertTrue(eGraph.names(aGraph).stream().noneMatch("g"::equals));
		assertTrue(eGraph.names(aGraph).stream().noneMatch("h"::equals));

		assertTrue(bGraph.names(bGraph).stream().noneMatch("a"::equals));
		assertTrue(bGraph.names(bGraph).stream().anyMatch("b"::equals));
		assertTrue(bGraph.names(bGraph).stream().noneMatch("c"::equals));
		assertTrue(eGraph.names(bGraph).stream().noneMatch("f"::equals));
		assertTrue(eGraph.names(bGraph).stream().noneMatch("g"::equals));
		assertTrue(eGraph.names(bGraph).stream().noneMatch("h"::equals));

		assertTrue(cGraph.names(cGraph).stream().noneMatch("a"::equals));
		assertTrue(cGraph.names(cGraph).stream().noneMatch("b"::equals));
		assertTrue(cGraph.names(cGraph).stream().anyMatch("c"::equals));
		assertTrue(cGraph.names(cGraph).stream().noneMatch("d"::equals));
		assertTrue(eGraph.names(cGraph).stream().noneMatch("f"::equals));
		assertTrue(eGraph.names(cGraph).stream().noneMatch("g"::equals));
		assertTrue(eGraph.names(cGraph).stream().noneMatch("h"::equals));

		assertTrue(eGraph.names(eGraph).stream().anyMatch("a"::equals));
		assertTrue(eGraph.names(eGraph).stream().anyMatch("b"::equals));
		assertTrue(eGraph.names(eGraph).stream().noneMatch("c"::equals));
		assertTrue(eGraph.names(eGraph).stream().anyMatch("e"::equals));
		assertTrue(eGraph.names(eGraph).stream().noneMatch("f"::equals));
		assertTrue(eGraph.names(eGraph).stream().noneMatch("g"::equals));
		assertTrue(eGraph.names(eGraph).stream().noneMatch("h"::equals));

		assertTrue(fGraph.names(fGraph).stream().anyMatch("a"::equals));
		assertTrue(fGraph.names(fGraph).stream().noneMatch("b"::equals));
		assertTrue(fGraph.names(fGraph).stream().anyMatch("c"::equals));
		assertTrue(fGraph.names(fGraph).stream().noneMatch("e"::equals));
		assertTrue(fGraph.names(fGraph).stream().anyMatch("f"::equals));
		assertTrue(fGraph.names(fGraph).stream().noneMatch("g"::equals));
		assertTrue(fGraph.names(fGraph).stream().noneMatch("h"::equals));

		assertTrue(gGraph.names(gGraph).stream().anyMatch("b"::equals));
		assertTrue(gGraph.names(gGraph).stream().anyMatch("a"::equals));
		assertTrue(gGraph.names(gGraph).stream().anyMatch("c"::equals));
		assertTrue(gGraph.names(gGraph).stream().anyMatch("e"::equals));
		assertTrue(gGraph.names(gGraph).stream().anyMatch("f"::equals));
		assertTrue(gGraph.names(gGraph).stream().anyMatch("g"::equals));
		assertTrue(gGraph.names(gGraph).stream().noneMatch("h"::equals));

		assertTrue(aGraph.hasFreeVariables("a"));
		assertFalse(aGraph.hasFreeVariables("b"));

		assertTrue(bGraph.hasFreeVariables("b"));
		assertFalse(bGraph.hasFreeVariables("a"));

		HashSet<String> ac = new HashSet<String>(); ac.add("a"); ac.add("c");
		HashSet<String> ah = new HashSet<String>(); ah.add("a"); ah.add("h");
		HashSet<String> fc = new HashSet<String>(); fc.add("f"); fc.add("c");
		HashSet<String> fa = new HashSet<String>(); fa.add("a"); fa.add("f");
		HashSet<String> fac = new HashSet<String>(); fac.add("a"); fac.add("f"); fac.add("c");
		HashSet<String> abc = new HashSet<String>(); abc.add("a"); abc.add("b"); abc.add("c");
		HashSet<String> bcf = new HashSet<String>(); bcf.add("f"); bcf.add("b"); bcf.add("c");

		assertTrue(fGraph.hasFreeVariables("a"));
		assertTrue(fGraph.hasFreeVariables("c"));
		assertTrue(fGraph.hasFreeVariables("f"));
		assertTrue(fGraph.hasFreeVariables(fc));
		assertTrue(fGraph.hasFreeVariables(fc));
		assertTrue(fGraph.hasFreeVariables(fa));
		assertTrue(fGraph.hasFreeVariables(fac));
		assertFalse(fGraph.hasFreeVariables("b"));

		assertTrue(gGraph.hasFreeVariables("a"));
		assertTrue(gGraph.hasFreeVariables("b"));
		assertTrue(gGraph.hasFreeVariables("c"));
		assertFalse(gGraph.hasFreeVariables("d"));
		assertTrue(gGraph.hasFreeVariables("e"));
		assertTrue(gGraph.hasFreeVariables("g"));
		assertTrue(gGraph.hasFreeVariables(abc));
		assertTrue(gGraph.hasFreeVariables(bcf));
		assertFalse(gGraph.hasFreeVariables(ah));
		assertFalse(gGraph.hasFreeVariables("h"));
		assertFalse(gGraph.hasFreeVariables("i"));

		assertTrue(gGraph.hasChildren(eGraph));
		assertTrue(gGraph.hasChildren(fGraph));
		assertTrue(gGraph.hasChildren(aGraph));
		assertTrue(gGraph.hasChildren(bGraph));
		assertTrue(gGraph.hasChildren(cGraph));
		assertFalse(gGraph.hasChildren(dGraph));

		assertTrue(aGraph.isValid());
		assertTrue(bGraph.isValid());
		assertTrue(cGraph.isValid());
		assertTrue(eGraph.isValid());
		assertTrue(fGraph.isValid());
		assertTrue(gGraph.isValid());
	}

	@Test
	public void testComputeDimensions() {
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

		HashMap<Node, Integer[]> dimensionMap = new HashMap<>();
		for (int i = 0, k = gRoots.size(); i < k; i++) gGraph.dimensions(roots.get(i), dimensionMap);

		assertEquals((int) dimensionMap.get(nA)[0], 1); assertEquals((int) dimensionMap.get(nA)[1], 1);
		assertEquals((int) dimensionMap.get(nB)[0], 1); assertEquals((int) dimensionMap.get(nB)[1], 1);
		assertEquals((int) dimensionMap.get(nC)[0], 1); assertEquals((int) dimensionMap.get(nC)[1], 1);
		assertEquals((int) dimensionMap.get(nE)[0], 1); assertEquals((int) dimensionMap.get(nE)[1], 1);
		assertEquals((int) dimensionMap.get(nF)[0], 1); assertEquals((int) dimensionMap.get(nF)[1], 1);
		assertEquals((int) dimensionMap.get(nG)[0], 1); assertEquals((int) dimensionMap.get(nG)[1], 1);

		nB = new InputNode("b", 1, 5, Optional.empty());
		nA = new InputNode("a", 1, 5, Optional.empty());
		nC = new InputNode("c", 5, 3, Optional.empty());
		nD = new InputNode("d", 1, 1, Optional.empty());

		dimensionMap = new HashMap<>();
		for (int i = 0, k = gRoots.size(); i < k; i++) gGraph.dimensions(roots.get(i), dimensionMap);
	}

	@Test
	public void testDimensions() {
		InputNode nB = new InputNode("b", 1, 5, Optional.empty());
		InputNode nA = new InputNode("a", 1, 5, Optional.empty());
		InputNode nC = new InputNode("c", 5, 3, Optional.empty());
		InputNode nD = new InputNode("d", 1, 1, Optional.empty());

		List<Node> inputsE = new ArrayList<>();
		List<Node> inputsF = new ArrayList<>();
		List<Node> inputsG = new ArrayList<>();

		inputsE.add(nA);
		inputsE.add(nB);
		inputsF.add(nA);
		inputsF.add(nC);

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
		HashMap<Node, Integer[]> dimensionMap = new HashMap<>();
		for (int i = 0, k = gRoots.size(); i < k; i++) gGraph.dimensions(roots.get(i), dimensionMap);

		assertEquals((int) dimensionMap.get(nA)[0], 1); assertEquals((int) dimensionMap.get(nA)[1], 5);
		assertEquals((int) dimensionMap.get(nB)[0], 1); assertEquals((int) dimensionMap.get(nB)[1], 5);
		assertEquals((int) dimensionMap.get(nC)[0], 5); assertEquals((int) dimensionMap.get(nC)[1], 3);
		assertEquals((int) dimensionMap.get(nE)[0], 1); assertEquals((int) dimensionMap.get(nE)[1], 5);
		assertEquals((int) dimensionMap.get(nF)[0], 1); assertEquals((int) dimensionMap.get(nF)[1], 3);
		assertEquals((int) dimensionMap.get(nG)[0], 1); assertEquals((int) dimensionMap.get(nG)[1], 3);
	}
}