package com.github.msetzu.pa.graph.json;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JSONParserTest {
	@Test
	public void parse() throws Exception {
		JSONParser parser;
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

		
		(new JSONParser(testString1)).parse();
		assertTrue(true);
		(new JSONParser(testString2)).parse();
		assertTrue(true);
		(new JSONParser(testString3)).parse();
		assertTrue(true);
		try {
			(new JSONParser(testString4)).parse();
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
		(new JSONParser(testString5)).parse();
		assertTrue(true);
		(new JSONParser(testString6)).parse();
		assertTrue(true);
	}
}