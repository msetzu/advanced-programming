package com.github.msetzu.pa.graph.json;

import com.github.msetzu.pa.graph.Operation;
import java.util.*;
import java.util.regex.*;

public class JSONParser {
	private static Pattern pat;
	private static Matcher mtc;
	private String json;
	private Map<String, Map<Token, String>> nodes = new HashMap<>();
	private Stack<String> names 		= new Stack<>();
	private Stack<String> types 		= new Stack<>();
	private Stack<String> shapes 	= new Stack<>();
	private Stack<String> inputs 	= new Stack<>();
	private Stack<Operation> operators 	= new Stack<>();
	private Token lastActive = Token.C_BRACKET;				// Last active tokens
	private Optional<String> currentNode = Optional.empty();	// Active node

	public JSONParser(String json) { this.json = json; }

	public void parse() throws IllegalArgumentException {
		match(Token.O_BRACKET).matchSequence(Element.NODE).match(Token.C_BRACKET);
	}

	private JSONParser matchNode() throws IllegalArgumentException {
		match(Token.NAME).match(Token.COLON).match(Token.O_BRACKET).match(Token.TYPE).match(Token.TYPECASE);
		lastActive = Token.NAME;

		if (types.peek().contains("input"))
			return match(Token.COMMA).match(Token.SHAPE).match(Token.O_SQ_BRACKET).match(Token.INT)
					.match(Token.COMMA).match(Token.INT).match(Token.C_SQ_BRACKET).match(Token.C_BRACKET);
		else
			return	match(Token.COMMA).match(Token.OP).match(Token.OPERATION).match(Token.O_SQ_BRACKET)
					.matchSequence(Element.COMBINED).match(Token.C_SQ_BRACKET).match(Token.C_BRACKET);
	}

	private JSONParser match(Token t) throws IllegalArgumentException {
		if (json.isEmpty() && t.equals(Token.C_BRACKET)) return this;

		String sp = " *"; String num = "0*[1-9]+(\\.[0-9]+)?";
		String vector = sp + "\\[" + sp + num + sp + "(," + sp + num + sp + ")*" + sp + "]" + sp;
		String matrix = sp + "\\[" + sp + vector + sp + "(," + sp + vector + sp + ")*" + sp + "]" + sp;

		switch (t) {
			case O_BRACKET: pat = Pattern.compile("^(" + sp + "\\{" + sp + ")"); break;
			case C_BRACKET: pat = Pattern.compile("^(" + sp + "}" + sp + ")"); break;
			case O_SQ_BRACKET: pat = Pattern.compile("^(" + sp + "\\[ *)"); break;
			case C_SQ_BRACKET: pat = Pattern.compile("^(" + sp + "]" + sp + ")"); break;
			case SPACES: pat = Pattern.compile("^(" + sp + ")"); break;
			case COMMA: pat = Pattern.compile("^(" + sp + "," + sp + ")"); break;
			case COLON: pat = Pattern.compile("^(" + sp + ":" + sp + ")"); break;
			case NAME: pat = Pattern.compile("^(" + sp + "\"[a-z]+\"" + sp + ")"); break;
			case FLOAT: pat = Pattern.compile("^(0*[1-9]+(\\.[1-9]+)?)"); break;
			case INT: pat = Pattern.compile("^(" + sp + "[0-9]+" + sp + ")"); break;
			case MATRIX: pat = Pattern.compile("^(" + matrix + ")"); break;
			case TYPE: pat = Pattern.compile("^(" + sp + "\"type\"" + sp + ":" + sp + ")"); break;
			case TYPECASE: pat = Pattern.compile("^(" + sp + "(\"input\")|(\"comp\"))"); break;
			case SHAPE: pat = Pattern.compile("^(" + sp + "\"shape\"" + sp + ":" + sp + ")"); break;
			case INPUT:	pat = Pattern.compile("^(" + sp + "\"input\"" + sp + ":" + sp + ")"); break;
			case COMP: pat = Pattern.compile("^(" + sp + "\"comp\"" + sp + "," + sp + ")"); break;
			case OP: pat = Pattern.compile("^(" + sp + "\"op\"" + sp + ":" + sp + ")"); break;
			case OPERATION: pat = Pattern.compile("^(" + sp + "\"((mult)|(sum))\"" + sp + ","
										+ sp + "\"in\"" + sp + ":" + sp + ")"); break;
		}
		mtc = pat.matcher(json);
		int index = mtc.find() ? mtc.start() : -1;

		if (index == -1) throw new IllegalArgumentException(t.name());
		String val = json.substring(mtc.start(), mtc.end());

		switch (t) {
			case C_BRACKET:
				if (lastActive.equals(Token.END) || names.size() == 0) return this;
				HashMap<Token, String> map = new HashMap<>();

				map.put(Token.NAME, names.peek());
				nodes.put(names.pop(), map);

				if (types.peek().contains("input")) {
					map.put(Token.TYPECASE, Token.INPUT.name().replace("\"",""));
					map.put(Token.SHAPE, shapes.get(shapes.size() - 2) + " " + shapes.peek());
					shapes.pop(); shapes.pop();
				} else {
					map.put(Token.TYPECASE, Token.COMP.name());
					map.put(Token.OP, operators.peek().name());
					map.put(Token.INPUT, inputs.stream().map(in -> in.replace("\"","") + "~").reduce("", String::concat));
					inputs = new Stack<>();
				}
				types.pop();
				currentNode = Optional.empty();
				lastActive = Token.C_BRACKET;
				break;
			case NAME:
				if (lastActive.equals(Token.NAME)) inputs.push(val);
				else names.push(val.replace("\"", ""));

				currentNode.ifPresent(v -> currentNode = Optional.of(v));
				break;
			case FLOAT: case MATRIX:
				inputs.push(val); break;
			case INT: 		shapes.push(val); break;
			case TYPECASE: 	types.push(val); break;
			case OPERATION: operators.push(val.contains("mul") ? Operation.MUL : Operation.SUM); break;
		}

		json = json.substring(mtc.end());
		return this;
	}

	private JSONParser matchSequence(Element e) throws IllegalArgumentException {
		switch (e) {
			case EPSILON: return match(Token.C_BRACKET);
			case NODE:
				try { return match(Token.C_BRACKET); }
				catch (IllegalArgumentException emptySequence) {
					matchNode();					// Sequence with >= 1 elements.
					try { return match(Token.COMMA).matchNode().match(Token.COMMA).matchSequence(Element.NODE); }
					catch (IllegalArgumentException nonEmptySequence) {
						switch (nonEmptySequence.getMessage()) {
							case "COMMA": lastActive = Token.END; return match(Token.C_BRACKET);
							case "NODE": throw nonEmptySequence;	// Malformed JSON.
					}}
				}
			case COMBINED:
				try { return match(Token.MATRIX).match(Token.COMMA).matchSequence(Element.COMBINED); }
				catch (IllegalArgumentException exception) {
					switch (exception.getMessage()) {
						case "MATRIX":
							try { return match(Token.NAME).match(Token.COMMA).matchSequence(Element.COMBINED); }
							catch (IllegalArgumentException matrixException) {
								switch (matrixException.getMessage()) {
									case "NAME": throw matrixException;	// Not a matrix, nor a name, must be malformed
									case "COMMA": return this;
						}}
						case "COMMA": return this;						// No comma, must be last element.
					}
				}}
		throw new IllegalArgumentException("SWITCH");
	}

	public Map<String, Map<Token, String>> getNodes() { return nodes; }
}

enum Element {EPSILON, NODE, COMBINED }