package design.ore.base.util;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class UIUtil
{
	public static ImageView colorize(ImageView img, ObjectProperty<Color> color)
	{
		ImageView checkClip = new ImageView(img.getImage());
		img.setClip(checkClip);
		img.setPreserveRatio(true);
		checkClip.setPreserveRatio(true);
		checkClip.fitWidthProperty().bind(img.fitWidthProperty());
		
        img.effectProperty().bind(Bindings.createObjectBinding(() ->
        {
        	ColorAdjust monochrome = new ColorAdjust();
        	monochrome.setSaturation(-1.0);
        	return new Blend(BlendMode.MULTIPLY, monochrome, new ColorInput( 0, 0, img.getImage().getWidth(), img.getImage().getHeight(), color.getValue()));
        }, color));
        
        return img;
	}
	
	public static void checkboxMatchSize(CheckBox box)
	{
		box.heightProperty().addListener(l -> ((Region) box.lookup(".mark")).setPadding(new Insets((box.getHeight() * 0.35))));
	}
}
