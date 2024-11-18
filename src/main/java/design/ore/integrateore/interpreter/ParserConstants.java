package design.ore.integrateore.interpreter;

import java.util.HashSet;
import java.util.Set;

public class ParserConstants
{	
	public static final String specRefWrapperChar = "%";
	public static final String assignmentChar = "=";
	
	public static final String openBracket = "{";
	public static final String closeBracket = "}";
	
	public static final Set<String> brackets = Set.of(openBracket, closeBracket);
	
	public static final String openParenthesis = "(";
	public static final String closeParenthesis = ")";
	
	public static final Set<String> parenthesis = Set.of(openParenthesis, closeParenthesis);

	public static final String trueLiteral = "true";
	public static final String falseLiteral = "false";
	
	public static final Set<String> booleanConstants = Set.of(trueLiteral, falseLiteral);
	
	public static final String integerVariableType = "integer";
	public static final String decimalVariableType = "decimal";
	public static final String textVariableType = "text";
	public static final String truefalseVariableType = "truefalse";
	
	public static final Set<String> variableTypes = Set.of(integerVariableType, decimalVariableType, textVariableType, truefalseVariableType);
	public static final String returnKeyword = "RETURN";

	public static final String plusKeyword = "+";
	public static final Set<String> numberOperators = Set.of("-", "*", "/");
	public static final Set<Character> numberOperatorChars = Set.of('-', '*', '/');
	public static final Set<String> truefalseOperators = Set.of("EQUALS", "AND", "OR", "NOT", "GREATERTHAN", "LESSTHAN", "GREATERTHANOREQUAL", "LESSTHANOREQUAL");
	public static final Set<String> allOperators;
	static {
		allOperators = new HashSet<String>();
		allOperators.add(plusKeyword);
		allOperators.addAll(numberOperators);
		allOperators.addAll(truefalseOperators);
	}

	public static final String conditionalStatementIfKeyword = "IF";
	public static final String conditionalStatementElseIfKeyword = "ELSEIF";
	public static final String conditionalStatementElseKeyword = "ELSE";
	public static final String conditionalStatementWhileKeyword = "WHILE";
	public static final Set<String> conditionalStatementStarts = Set.of(conditionalStatementIfKeyword, conditionalStatementElseIfKeyword, conditionalStatementElseKeyword, conditionalStatementWhileKeyword);
	public static final String conditionalStatementMetKeyword = "THEN";
}
