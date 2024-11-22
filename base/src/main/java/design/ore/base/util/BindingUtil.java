package design.ore.base.util;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.control.TextFormatter;

public class BindingUtil
{
	public static DoubleBinding zeroDoubleBinding() { return Bindings.createDoubleBinding(() -> 0.0); }
	public static StringExpression stringExpression(String str) { return Bindings.createStringBinding(() -> str); } 
	public static BooleanBinding booleanBinding(boolean bool) { return Bindings.createBooleanBinding(() -> bool); }
	
	private static TextFormatter<?> decimalFormatter = null;
	public static TextFormatter<?> getDecimalFormatter(int decimalPlaces)
	{
		if(decimalFormatter == null) decimalFormatter =  new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{ return Pattern.compile("(\\+|-)?(\\d*|\\d+\\.\\d{0," + decimalPlaces + "})").matcher(change.getControlNewText()).matches() ? change : null; });
		
		return decimalFormatter;
	}

	private static TextFormatter<?> integerFormatter = null;
	public static TextFormatter<?> getIntegerFormatter()
	{
		if(integerFormatter == null) integerFormatter =   new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{ return Pattern.compile("(\\+|-)?(\\d*)").matcher(change.getControlNewText()).matches() ? change : null; });
		
		return integerFormatter;
	}

	private static TextFormatter<?> oneToNinetyNineFormatter = null;
	public static TextFormatter<?> get0to99IntegerFormatter()
	{
		if(oneToNinetyNineFormatter == null) oneToNinetyNineFormatter =   new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{ return Pattern.compile("(^$)|(^(0?[1-9]|[1-9][0-9])$)").matcher(change.getControlNewText()).matches() ? change : null; });
		
		return oneToNinetyNineFormatter;
	}
}
