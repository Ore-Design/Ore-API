package design.ore.base.util.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.dlsc.keyboardfx.KeyboardView;

import design.ore.base.util.Log;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * A controller class that manages a collection of UI menus, as well as a few other handy things.
 */
public class Navigation
{
	IMenu currentMenu;
	
	/**
	 * This StackPane wraps all {@link IMenu} root nodes inside of it (see {@link IMenu.getRoot() getRoot})
	 */
	StackPane loadStack;
	
	/**
	 * This list contains all {@link IMenu}s inside of it.
	 */
	final List<IMenu> menus = new ArrayList<IMenu>();
	
	final Scene scene;
	@Getter private final Stage stage;
	
	/**
	 * An optional {@link KeyboardView}, used if Navigation is initialized in tablet mode.
	 */
	KeyboardView keyboard;
	
	/**
	 * A utility property that can be used for dynamic resizing.
	 */
	private final ReadOnlyDoubleWrapper fontSizeProperty = new ReadOnlyDoubleWrapper(10);
	public ReadOnlyDoubleProperty fontSizeProperty() { return fontSizeProperty.getReadOnlyProperty(); }
	
	@Getter private final SimpleStringProperty stylesheetProperty;
	
	/**
	 * @param stage                the stage for this Navigation to control, typically the Stage passed in from JavaFX Application start() method.
	 * @param initialMenus         a collection of all IMenu instances, which should have their root Panes initialized by this point.
	 * @param stylesheetProperty   an optional SimpleStringProperty to be used as the current stylesheet selection.
	 * @param tabletMode           true if the table mode on-screen keyboard should be used, otherwise false.
	 * @param maximized            true if the application should be maximized on initalization, otherwise false.
	 */
	public Navigation(Stage stage, Collection<? extends IMenu> initialMenus, SimpleStringProperty stylesheetProperty, boolean tabletMode, boolean maximized)
	{
		this.stage = stage;
		menus.addAll(initialMenus);
		
		currentMenu = menus.get(0);
		
		loadStack = new StackPane(currentMenu.getRoot());
		loadStack.setAlignment(Pos.TOP_LEFT);
		fontSizeProperty.addListener((obs, oldVal, newVal) -> loadStack.setStyle("-fx-font-size: " + newVal + "px"));
		
		scene = new Scene(loadStack);
		scene.widthProperty().addListener((obs, oldVal, newVal) ->
		{
			if(newVal.doubleValue() < scene.getHeight()) fontSizeProperty.set((scene.getHeight() / 130.0));
			else fontSizeProperty.set((newVal.doubleValue() / 150.0));
		});
		scene.heightProperty().addListener((obs, oldVal, newVal) ->
		{
			if(newVal.doubleValue() < scene.getWidth()) fontSizeProperty.set((scene.getWidth() / 150.0));
			else fontSizeProperty.set((newVal.doubleValue() / 130.0));
		});
		
		if(stylesheetProperty != null) this.stylesheetProperty = stylesheetProperty;
		else this.stylesheetProperty = new SimpleStringProperty("");
		bindUIToStylesheet(scene);
		
		if(tabletMode)
		{
			keyboard = new KeyboardView();
			keyboard.setOnClose(() -> keyboard.setVisible(false));
			keyboard.setDarkMode(true);
			keyboard.setVisible(false);
			keyboard.maxHeightProperty().bind(loadStack.heightProperty().multiply(0.35));
			StackPane.setAlignment(keyboard, Pos.BOTTOM_CENTER);
			
			loadStack.getChildren().add(keyboard);
			
			scene.focusOwnerProperty().addListener((obs, oldVal, newVal) ->
			{
				if(newVal != null) keyboard.setVisible(newVal instanceof TextField || newVal instanceof TextArea);
			});
		}

		loadStack.prefWidthProperty().bind(scene.widthProperty());
		loadStack.prefHeightProperty().bind(scene.heightProperty());
		
		for(IMenu m : menus)
		{
			m.getRoot().prefWidthProperty().bind(loadStack.widthProperty());
			m.getRoot().prefHeightProperty().bind(loadStack.heightProperty());
		}
		
		stage.setScene(scene);
		if(maximized) stage.setMaximized(true);
		
		stage.show();
		
		currentMenu.enter();
	}
	
	/**
	 * Navigates to the {@link IMenu} with the provided ID, and passes an
	 * optional collection of Objects as parameters.
	 * 
	 * @param id       the id of the IMenu to navigate to.
	 * @param params   an optional collection of Objects to pass in as parameters to the menu.
	 */
	public void navigate(String id, Object... params)
	{
		Optional<IMenu> target = menus.stream().filter(m -> m.getMenuID().equals(id)).findFirst();
		if(target.isEmpty()) Log.getLogger().info("No menu exists with ID " + id + "!");
		else
		{
			currentMenu.exit();
			currentMenu = target.get();

			loadStack.getChildren().set(0, currentMenu.getRoot());
			
			currentMenu.enter(params);
		}
	}
	
	/**
	 * Binds a given JavaFX UI element to the stylesheetProperty.
	 * This does nothing unless the Object passed in is/extends a JavaFX Scene
	 * or JavaFX Parent object.
	 * 
	 * @param ui   the UI element to be bound.
	 */
	public void bindUIToStylesheet(Object ui)
	{
		if(ui instanceof Scene) ((Scene) ui).getStylesheets().add(stylesheetProperty.getValue());
		else if(ui instanceof Parent) ((Parent) ui).getStylesheets().add(stylesheetProperty.getValue());
		
		stylesheetProperty.addListener((obs, oldVal, newVal) ->
		{
			if(ui instanceof Scene)
			{
				((Scene) ui).getStylesheets().remove(oldVal);
				((Scene) ui).getStylesheets().add(newVal);
			}
			else if(ui instanceof Parent)
			{
				((Parent) ui).getStylesheets().remove(oldVal);
				((Parent) ui).getStylesheets().add(newVal);
			}
		});
	}
}
