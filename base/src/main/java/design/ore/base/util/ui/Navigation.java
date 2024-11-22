package design.ore.base.util.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.dlsc.keyboardfx.KeyboardView;

import design.ore.base.util.Log;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
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
	StackPane loadStack;
	final List<IMenu> menus = new ArrayList<IMenu>();
	
	final Scene scene;
	@Getter private final Stage stage;
	
	KeyboardView keyboard;
	@Getter private final SimpleStringProperty stylesheetProperty;
	private final ReadOnlyDoubleWrapper fontSizeProperty = new ReadOnlyDoubleWrapper(10);
	
	/**
	 * @return the read-only font size property.
	 */
	public ReadOnlyDoubleProperty fontSizeProperty() { return fontSizeProperty.getReadOnlyProperty(); }
	
	/**
	 * @param stage                the stage for this Navigation to control, typically the Stage passed in from JavaFX Application {@link Application#start(Stage)} method.
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
		menus.forEach(menu -> menu.setNavigation(this));
		
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
		bindSceneToStylesheet(scene);
		
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
			menuLockedDialogs.forEach(diag -> Platform.runLater(() -> diag.close()));
			menuLockedDialogs.clear();

			menuLockedSubStages.forEach(stage -> Platform.runLater(() -> stage.close()));
			menuLockedSubStages.clear();
			
			currentMenu.exit();
			currentMenu = target.get();

			loadStack.getChildren().set(0, currentMenu.getRoot());
			
			currentMenu.enter(params);
		}
	}
	
	/**
	 * Binds a given JavaFX {@link Scene} element to the stylesheetProperty.
	 * 
	 * @param scene   the {@link Scene} to be bound.
	 */
	public void bindSceneToStylesheet(Scene scene)
	{
		scene.getStylesheets().add(stylesheetProperty.getValue());
		
		stylesheetProperty.addListener((obs, oldVal, newVal) ->
		{
			scene.getStylesheets().remove(oldVal);
			scene.getStylesheets().add(newVal);
		});
	}
	
	/**
	 * Binds a given JavaFX {@link Parent} element to the stylesheetProperty.
	 * 
	 * @param parent   the {@link Parent} to be bound.
	 */
	public void bindParentToStylesheet(Parent parent)
	{
		parent.getStylesheets().add(stylesheetProperty.getValue());
		
		stylesheetProperty.addListener((obs, oldVal, newVal) ->
		{
			parent.getStylesheets().remove(oldVal);
			parent.getStylesheets().add(newVal);
		});
	}
	
	private final List<Dialog<?>> menuLockedDialogs = new ArrayList<>();
	public void registerMenuLockedDialog(Dialog<?> dialog) { menuLockedDialogs.add(dialog); }
	
	private final List<Stage> menuLockedSubStages = new ArrayList<>();
	public void registerMenuLockedSubStage(Stage stage) { menuLockedSubStages.add(stage); }
}
