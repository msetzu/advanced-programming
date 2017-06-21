package com.github.msetzu.pa.graph.json.factories;

import com.github.msetzu.pa.graph.ComputationalNode;
import com.github.msetzu.pa.graph.Graph;
import com.github.msetzu.pa.graph.InputNode;
import com.github.msetzu.pa.graph.Node;
import com.github.msetzu.pa.graph.json.JSONParser;
import com.github.msetzu.pa.graph.json.JSONParserTest;
import com.github.msetzu.pa.graph.json.Token;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class GraphFactoryTest {
	@Test
	public void graph() throws Exception {
		String testString1 = "{" +
				"\"a\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"c\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"a\", [[1]]]}" +
				"}   ";
		String testString2 = "{" +
				"\"a\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"c\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"a\", [[2]]]}," +
				"\"d\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [[[2]], [[2]]]}," +
				"\"e\": {\"type\": \"comp\", \"op\": \"mult\", \"in\": [\"c\", \"c\"]}" +
				"} ";
		String testString3 = " {   } ";
		String testString4 = "{ \"a\": {\"type\": \"input\", \"shape\": [1,1]}, }  ";
		String testString5 = "{\"a\": {\"type\": \"input\", \"shape\": [1,1]},\"b\": {\"type\": \"input\", \"shape\": [1,1]}}";
		String testString6 = "{" +
				"\"a\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"b\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"c\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"a\", \"b\"]}," +
				"\"d\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"b\", [[1]], [[1, 2]], [[2]]]}," +
				"\"e\": {\"type\": \"comp\", \"op\": \"mult\", \"in\": [\"c\", \"d\"]}" +
				"}   ";
		
		JSONParser jp1 = new JSONParser(testString1);
		jp1.parse();
		Map<String, Map<Token, String>> m1 = jp1.getNodes();
		GraphFactory gf1 = new GraphFactory(m1);
		Graph g1 = gf1.graph().get();
		
		JSONParser jp2 = new JSONParser(testString2);
		jp2.parse();
		Map<String, Map<Token, String>> m2 = jp2.getNodes();
		GraphFactory gf2 = new GraphFactory(m2);
		Graph g2 = gf2.graph().get();

		JSONParser jp3 = new JSONParser(testString3);
		jp3.parse();
		Map<String, Map<Token, String>> m3 = jp3.getNodes();
		GraphFactory gf3 = new GraphFactory(m3);
		Graph g3 = gf3.graph().get();

		try {
			JSONParser jp4 = new JSONParser(testString4);
			jp4.parse();
			assertTrue(false);
		} catch (Exception e) {}

		JSONParser jp5 = new JSONParser(testString5);
		jp5.parse();
		Map<String, Map<Token, String>> m5 = jp5.getNodes();
		m5 = jp5.getNodes();

		JSONParser jp6 = new JSONParser(testString6);
		jp6.parse();
		Map<String, Map<Token, String>> m6 = jp6.getNodes();
		GraphFactory gf6 = new GraphFactory(m6);
		Graph g6 = gf6.graph().get();

		assertTrue(m1.keySet().contains("a"));
		assertFalse(m1.keySet().contains("b"));
		assertTrue(m1.keySet().contains("c"));
		assertFalse(m1.keySet().contains("d"));
		assertFalse(m1.keySet().contains("e"));
		
		assertTrue(g1.names(g1).stream().anyMatch("a"::equals));
		assertFalse(g1.names(g1).stream().anyMatch("b"::equals));
		assertTrue(g1.names(g1).stream().anyMatch("c"::equals));
		assertFalse(g1.names(g1).stream().anyMatch("d"::equals));
		assertFalse(g1.names(g1).stream().anyMatch("e"::equals));
		assertTrue(g1.names(g1).stream().noneMatch("f"::equals));
		assertTrue(g1.names(g1).stream().noneMatch("g"::equals));
		assertTrue(g1.names(g1).stream().noneMatch("h"::equals));
		
		assertTrue(g2.names(g2).stream().anyMatch("a"::equals));
		assertTrue(g2.names(g2).stream().anyMatch("c"::equals));
		assertTrue(g2.names(g2).stream().anyMatch("d"::equals));
		assertTrue(g2.names(g2).stream().anyMatch("e"::equals));
		assertTrue(g2.names(g2).stream().noneMatch("b"::equals));
		assertTrue(g2.names(g2).stream().noneMatch("f"::equals));
		assertTrue(g2.names(g2).stream().noneMatch("g"::equals));
		assertTrue(g2.names(g2).stream().noneMatch("h"::equals));
		
		assertTrue(g3.names(g3).stream().noneMatch("a"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("b"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("c"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("d"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("e"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("f"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("g"::equals));
		assertTrue(g3.names(g3).stream().noneMatch("h"::equals));
		
		assertTrue(g6.names(g6).stream().anyMatch("a"::equals));
		assertTrue(g6.names(g6).stream().anyMatch("b"::equals));
		assertTrue(g6.names(g6).stream().anyMatch("c"::equals));
		assertTrue(g6.names(g6).stream().anyMatch("d"::equals));
		assertTrue(g6.names(g6).stream().anyMatch("e"::equals));
		assertTrue(g6.names(g6).stream().noneMatch("f"::equals));
		assertTrue(g6.names(g6).stream().noneMatch("g"::equals));
		assertTrue(g6.names(g6).stream().noneMatch("h"::equals));

		HashSet<String> ac = new HashSet<String>(); ac.add("a"); ac.add("c");
		HashSet<String> ah = new HashSet<String>(); ah.add("a"); ah.add("h");
		HashSet<String> fc = new HashSet<String>(); fc.add("f"); fc.add("c");
		HashSet<String> fa = new HashSet<String>(); fa.add("a"); fa.add("f");
		HashSet<String> fac = new HashSet<String>(); fac.add("a"); fac.add("f"); fac.add("c");
		HashSet<String> abc = new HashSet<String>(); abc.add("a"); abc.add("b"); abc.add("c");
		HashSet<String> bcf = new HashSet<String>(); bcf.add("f"); bcf.add("b"); bcf.add("c");
		
		assertTrue(g1.hasFreeVariables("a"));
		assertFalse(g1.hasFreeVariables("b"));

		assertFalse(g3.hasFreeVariables("a"));
		assertFalse(g3.hasFreeVariables("b"));
		assertFalse(g3.hasFreeVariables("c"));
		assertFalse(g3.hasFreeVariables("d"));
		assertFalse(g3.hasFreeVariables("e"));
		assertFalse(g3.hasFreeVariables("g"));
		assertFalse(g3.hasFreeVariables(abc));
		assertFalse(g3.hasFreeVariables(bcf));

		assertTrue(g6.hasFreeVariables("a"));
		assertTrue(g6.hasFreeVariables("b"));
		assertTrue(g6.hasFreeVariables("c"));
		assertTrue(g6.hasFreeVariables("d"));
		assertTrue(g6.hasFreeVariables("e"));
		assertFalse(g6.hasFreeVariables("g"));
		assertTrue(g6.hasFreeVariables(abc));
		assertFalse(g6.hasFreeVariables(bcf));
		assertFalse(g6.hasFreeVariables(ah));
		assertFalse(g6.hasFreeVariables("h"));
		assertFalse(g6.hasFreeVariables("i"));

		assertTrue(g1.isValid());
		assertTrue(g2.isValid());
		assertTrue(g3.isValid());
		assertTrue(g6.isValid());
	}

	@Test
	public void matrix() throws Exception {
		String s1 = "[[1,1],[1,2],[1,2,3],[1,1]]";
		String s2 = "[[1,1,1,1,1],[1,2,2,2,2]]";
		String s3 = "[[1],[1],[1],[1]]";
		String s4 = "[[1,1],[1,2],[1,3],[1,1]]";

		try {
			//new GraphFactory().matrixOf(s1);
			//assertTrue(false);
		} catch (IllegalArgumentException e) { assertTrue(true);}
	}
}