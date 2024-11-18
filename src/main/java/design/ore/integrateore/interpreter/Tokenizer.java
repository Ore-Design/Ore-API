package design.ore.integrateore.interpreter;

import java.util.ArrayList;
import java.util.List;

import design.ore.integrateore.ScriptOre;
import design.ore.integrateore.interpreter.enums.CharType;
import design.ore.integrateore.interpreter.enums.TokenType;
import design.ore.integrateore.interpreter.pojos.CharToken;
import design.ore.integrateore.interpreter.pojos.Token;

public class Tokenizer
{
	public Tokenizer()
	{
	}
	
	public List<Token> tokenize(String text)
	{
		return mergeCharTokens(getCharTokens(text));
	}
	
	private List<Token> mergeCharTokens(List<CharToken> charTokens)
	{
		List<Token> tokens = new ArrayList<>();
		
		while(charTokens.size() > 0)
		{
			if(charTokens.getFirst().getType() == CharType.NEW_LINE)
			{
				tokens.add(new Token(TokenType.NEW_LINE, "\n"));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.SPACE)
			{
				tokens.add(new Token(TokenType.SPACE, " "));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.OPEN_BRACKET)
			{
				tokens.add(new Token(TokenType.OPEN_BRACKET, "{"));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.CLOSE_BRACKET)
			{
				tokens.add(new Token(TokenType.CLOSE_BRACKET, "}"));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.OPEN_PARENTHESIS)
			{
				tokens.add(new Token(TokenType.OPEN_PARENTHESIS, "("));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.CLOSE_PARENTHESIS)
			{
				tokens.add(new Token(TokenType.CLOSE_PARENTHESIS, ")"));
				sublist(charTokens, 1);
				continue;
			}
			if(charTokens.getFirst().getType() == CharType.EQUALS)
			{
				tokens.add(new Token(TokenType.EQUALS, "="));
				sublist(charTokens, 1);
				continue;
			}
			
			if(charTokens.getFirst().getType() == CharType.PLUS)
			{
				tokens.add(new Token(TokenType.EQUALS, "="));
				sublist(charTokens, 1);
				continue;
			}
			
			if(charTokens.size() > ParserConstants.conditionalStatementMetKeyword.length() && textFromToken(charTokens, ParserConstants.conditionalStatementMetKeyword.length()).equals(ParserConstants.conditionalStatementMetKeyword))
			{
				tokens.add(new Token(TokenType.THEN, ParserConstants.conditionalStatementMetKeyword));
				sublist(charTokens, ParserConstants.conditionalStatementMetKeyword.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.conditionalStatementIfKeyword.length() && textFromToken(charTokens, ParserConstants.conditionalStatementIfKeyword.length()).equals(ParserConstants.conditionalStatementIfKeyword))
			{
				tokens.add(new Token(TokenType.IF, ParserConstants.conditionalStatementIfKeyword));
				sublist(charTokens, ParserConstants.conditionalStatementIfKeyword.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.conditionalStatementElseIfKeyword.length() && textFromToken(charTokens, ParserConstants.conditionalStatementElseIfKeyword.length()).equals(ParserConstants.conditionalStatementElseIfKeyword))
			{
				tokens.add(new Token(TokenType.ELSEIF, ParserConstants.conditionalStatementElseIfKeyword));
				sublist(charTokens, ParserConstants.conditionalStatementElseIfKeyword.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.conditionalStatementElseKeyword.length() && textFromToken(charTokens, ParserConstants.conditionalStatementElseKeyword.length()).equals(ParserConstants.conditionalStatementElseKeyword))
			{
				tokens.add(new Token(TokenType.ELSE, ParserConstants.conditionalStatementElseKeyword));
				sublist(charTokens, ParserConstants.conditionalStatementElseKeyword.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.conditionalStatementWhileKeyword.length() && textFromToken(charTokens, ParserConstants.conditionalStatementWhileKeyword.length()).equals(ParserConstants.conditionalStatementWhileKeyword))
			{
				tokens.add(new Token(TokenType.WHILE, ParserConstants.conditionalStatementWhileKeyword));
				sublist(charTokens, ParserConstants.conditionalStatementWhileKeyword.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.trueLiteral.length() && textFromToken(charTokens, ParserConstants.trueLiteral.length()).equals(ParserConstants.trueLiteral))
			{
				tokens.add(new Token(TokenType.TRUEFALSE_LITERAL, ParserConstants.trueLiteral));
				sublist(charTokens, ParserConstants.trueLiteral.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.falseLiteral.length() && textFromToken(charTokens, ParserConstants.falseLiteral.length()).equals(ParserConstants.falseLiteral))
			{
				tokens.add(new Token(TokenType.TRUEFALSE_LITERAL, ParserConstants.falseLiteral));
				sublist(charTokens, ParserConstants.falseLiteral.length());
				continue;
			}
			
			if(charTokens.size() > ParserConstants.returnKeyword.length() && textFromToken(charTokens, ParserConstants.returnKeyword.length()).equals(ParserConstants.returnKeyword))
			{
				tokens.add(new Token(TokenType.RETURN, ParserConstants.returnKeyword));
				sublist(charTokens, ParserConstants.returnKeyword.length());
				continue;
			}

			if(charTokens.size() > ParserConstants.decimalVariableType.length() && textFromToken(charTokens, ParserConstants.decimalVariableType.length()).equals(ParserConstants.decimalVariableType))
			{
				tokens.add(new Token(TokenType.DECIMAL_DECLARATION, ParserConstants.decimalVariableType));
				sublist(charTokens, ParserConstants.decimalVariableType.length());
				continue;
			}

			if(charTokens.size() > ParserConstants.integerVariableType.length() && textFromToken(charTokens, ParserConstants.integerVariableType.length()).equals(ParserConstants.integerVariableType))
			{
				tokens.add(new Token(TokenType.INTEGER_DECLARATION, ParserConstants.integerVariableType));
				sublist(charTokens, ParserConstants.integerVariableType.length());
				continue;
			}

			if(charTokens.size() > ParserConstants.textVariableType.length() && textFromToken(charTokens, ParserConstants.textVariableType.length()).equals(ParserConstants.textVariableType))
			{
				tokens.add(new Token(TokenType.TEXT_DECLARATION, ParserConstants.textVariableType));
				sublist(charTokens, ParserConstants.textVariableType.length());
				continue;
			}

			if(charTokens.size() > ParserConstants.truefalseVariableType.length() && textFromToken(charTokens, ParserConstants.truefalseVariableType.length()).equals(ParserConstants.truefalseVariableType))
			{
				tokens.add(new Token(TokenType.TRUEFALSE_DECLARATION, ParserConstants.truefalseVariableType));
				sublist(charTokens, ParserConstants.truefalseVariableType.length());
				continue;
			}
			
			if(charTokens.get(0).getType() == CharType.NUMBER)
			{
				ScriptOre.logger.debug("Testing for number literal");
				int endNumberIndex = 1;
				boolean decimalFound = false;
				boolean endsOnDecimal = false;
				while(charTokens.size() > endNumberIndex)
				{	
					if(charTokens.get(endNumberIndex).getType() != CharType.NUMBER && charTokens.get(endNumberIndex).getType() != CharType.PERIOD) break;
					if(charTokens.get(endNumberIndex).getType() == CharType.PERIOD)
					{
						if(decimalFound) break;
						else
						{
							decimalFound = true;
							endsOnDecimal = true;
							endNumberIndex += 1;
						}
					}
					else if(endsOnDecimal)
					{
						endsOnDecimal = false;
						endNumberIndex += 1;
					}
					else endNumberIndex += 1;
				}
				
				if(endNumberIndex == 0) endNumberIndex += 1;

				if(endsOnDecimal)
				{
					tokens.add(new Token(TokenType.UNDEFINED, textFromToken(charTokens, endNumberIndex)));
					sublist(charTokens, endNumberIndex);
				}
				else if(decimalFound)
				{
					tokens.add(new Token(TokenType.DECIMAL_LITERAL, textFromToken(charTokens, endNumberIndex)));
					sublist(charTokens, endNumberIndex);
				}
				else
				{
					tokens.add(new Token(TokenType.INTEGER_LITERAL, textFromToken(charTokens, endNumberIndex)));
					sublist(charTokens, endNumberIndex);
				}
				
				continue;
			}
			
			if(charTokens.get(0).getType() == CharType.PERCENT)
			{
				ScriptOre.logger.debug("Testing for spec ref");
				int endSpecIndex = 1;
				boolean found = false;
				while(true)
				{
					if(charTokens.size() > endSpecIndex + 1)
					{
						if(charTokens.get(endSpecIndex).getType() != CharType.LETTER) break;
						if(charTokens.get(endSpecIndex).getType() == CharType.PERCENT)
						{

							tokens.add(new Token(TokenType.SPEC, textFromToken(charTokens, endSpecIndex)));
							sublist(charTokens, endSpecIndex);
							found = true;
							
							break;
						}
						else endSpecIndex++;
					}
					else break;
				}

				if(!found)
				{
					tokens.add(new Token(TokenType.UNDEFINED, textFromToken(charTokens, endSpecIndex)));
					sublist(charTokens, endSpecIndex);
				}
				
				continue;
			}
			
			if(charTokens.get(0).getType() == CharType.QUOTATIONS)
			{
				ScriptOre.logger.debug("Testing for text literal");
				int endTextIndex = 1;
				boolean found = false;
				while(charTokens.size() > endTextIndex)
				{	
					if(charTokens.get(endTextIndex).getType() == CharType.QUOTATIONS)
					{
						endTextIndex += 1;
						found = true;
						break;
					}
					else if(charTokens.get(endTextIndex).getType() == CharType.NEW_LINE) break;
					else endTextIndex += 1;
				}
				
				if(endTextIndex == 0) endTextIndex += 1;

				if(found)
				{
					tokens.add(new Token(TokenType.TEXT_LITERAL, textFromToken(charTokens, endTextIndex)));
					sublist(charTokens, endTextIndex);
				}
				else
				{
					tokens.add(new Token(TokenType.UNDEFINED, textFromToken(charTokens, endTextIndex)));
					sublist(charTokens, endTextIndex);
				}
				
				continue;
			}
			
			if(charTokens.get(0).getType() == CharType.LETTER)
			{
				ScriptOre.logger.debug("Testing for variable");
				int endVariableIndex = 0;
				while(charTokens.size() > ++endVariableIndex)
				{
					if(charTokens.get(endVariableIndex).getType() == CharType.NEW_LINE || charTokens.get(endVariableIndex).getType() == CharType.SPACE) break;
				}

				Token newToken = new Token(TokenType.VARIABLE, textFromToken(charTokens, endVariableIndex));
				ScriptOre.logger.debug("Adding new variable token: '" + newToken.getTokenString() + "'");
				tokens.add(newToken);
				
				sublist(charTokens, endVariableIndex);
				
				continue;
			}
			
			tokens.add(new Token(TokenType.UNDEFINED, "" + charTokens.getFirst().getTokenChar()));
			sublist(charTokens, 1);
		}
		
		return tokens;
	}
	
	private void sublist(List<CharToken> tokens, int count)
	{
		for(int x = 0 ; x < count ; x++)
		{
			tokens.remove(0);
		}
	}
	
	private List<CharToken> getCharTokens(String line)
	{
		List<CharToken> charTokens = new ArrayList<>();
		
		for(char ch : line.toCharArray())
		{
			if(ch == '\n')
			{
				charTokens.add(new CharToken(CharType.NEW_LINE, ch));
				continue;
			}
			if(ch == ' ')
			{
				charTokens.add(new CharToken(CharType.SPACE, ch));
				continue;
			}
			if(Character.isLetter(ch))
			{
				charTokens.add(new CharToken(CharType.LETTER, ch));
				continue;
			}
			if(Character.isDigit(ch))
			{
				charTokens.add(new CharToken(CharType.NUMBER, ch));
				continue;
			}
			if(ch == '%')
			{
				charTokens.add(new CharToken(CharType.PERCENT, ch));
				continue;
			}
			if(ch == '.')
			{
				charTokens.add(new CharToken(CharType.PERIOD, ch));
				continue;
			}
			if(ParserConstants.numberOperatorChars.contains(ch))
			{
				charTokens.add(new CharToken(CharType.NUMBER_OPERATOR, ch));
				continue;
			}
			if(ch == '+')
			{
				charTokens.add(new CharToken(CharType.PLUS, ch));
				continue;
			}
			if(ch == '(')
			{
				charTokens.add(new CharToken(CharType.OPEN_PARENTHESIS, ch));
				continue;
			}
			if(ch == ')')
			{
				charTokens.add(new CharToken(CharType.CLOSE_PARENTHESIS, ch));
				continue;
			}
			if(ch == '{')
			{
				charTokens.add(new CharToken(CharType.OPEN_BRACKET, ch));
				continue;
			}
			if(ch == '}')
			{
				charTokens.add(new CharToken(CharType.CLOSE_BRACKET, ch));
				continue;
			}
			if(ch == '"')
			{
				charTokens.add(new CharToken(CharType.QUOTATIONS, ch));
				continue;
			}
			if(ch == '=')
			{
				charTokens.add(new CharToken(CharType.EQUALS, ch));
				continue;
			}
			
			charTokens.add(new CharToken(CharType.UNDEFINED, ch));
		}
		
		return charTokens;
	}
	
	private String textFromToken(List<CharToken> charTokens, int length)
	{
		if(charTokens.size() <= length - 1) return null;
			
		String val = "";
		for(int x = 0 ; x < length ; x++)
		{
			val += charTokens.get(x).getTokenChar();
		}
		
		return val;
	}
}
