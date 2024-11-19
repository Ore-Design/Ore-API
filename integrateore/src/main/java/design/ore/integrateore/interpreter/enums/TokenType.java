package design.ore.integrateore.interpreter.enums;

public enum TokenType
{
	// Multi Character
	VARIABLE,
	SPEC,
	RETURN,
	DECIMAL_DECLARATION,
	INTEGER_DECLARATION,
	TEXT_DECLARATION,
	TRUEFALSE_DECLARATION,
	BOOLEAN_OPERATOR,
	IF,
	ELSEIF,
	ELSE,
	WHILE,
	THEN,
	INTEGER_LITERAL,
	DECIMAL_LITERAL,
	TEXT_LITERAL,
	TRUEFALSE_LITERAL,
	// Single Character
	PLUS,
	OPEN_BRACKET,
	CLOSE_BRACKET,
	OPEN_PARENTHESIS,
	CLOSE_PARENTHESIS,
	EQUALS,
	SPACE,
	NEW_LINE,
	NUMBER_OPERATOR,
	// Undefined
	UNDEFINED,
}