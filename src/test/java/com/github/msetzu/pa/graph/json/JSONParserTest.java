package com.github.msetzu.pa.graph.json;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JSONParserTest {
	@Test
	public void parse() throws Exception {
		JSONParser parser = new JSONParser();
		String testString1 = "{" +
				"\"a\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"b\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"c\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"a\", \"b\"]}," +
				"\"d\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"b\", [[1]]]}," +
				"\"e\": {\"type\": \"comp\", \"op\": \"mult\", \"in\": [\"c\", \"d\"]}" +
				"}   ";
		String testString2 = "{" +
				"\"a\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"b\": {\"type\": \"input\", \"shape\": [1,1]}," +
				"\"c\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"a\", \"b\"]}," +
				"\"d\": {\"type\": \"comp\", \"op\": \"sum\", \"in\": [\"b\", [[1]]]}," +
				"\"e\": {\"type\": \"comp\", \"op\": \"mult\", \"in\": [\"c\", \"d\"]}" +
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


		//System.out.println("Graph 1");
		parser.parse(testString1);
		assertTrue(true);
		//System.out.println("\n\n\n");
		//System.out.println("Graph 2");
		parser.parse(testString2);
		assertTrue(true);
		//System.out.println("\n\n\n");
		//System.out.println("Graph 3");
		parser.parse(testString3);
		assertTrue(true);
		//System.out.println("\n\n\n");
		//System.out.println("Graph 4");
		try {
			parser.parse(testString4);
			assertTrue(false);
			//System.out.println("\n\n\n");
		} catch (Exception e) {
			//System.out.println("FAILED");
			assertTrue(true);
			//System.out.println("\n\n\n");
		}
		//System.out.println("Graph 5");
		parser.parse(testString5);
		assertTrue(true);
		//System.out.println("Graph 6");
		parser.parse(testString6);
		assertTrue(true);
		//System.out.println("\n\n\n");
	}
}