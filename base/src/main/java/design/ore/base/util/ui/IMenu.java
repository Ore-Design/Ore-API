package design.ore.base.util.ui;

import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * An interface that provides needed functionality for the {@link Navigation} class.
 * Typically implemented in an extension of some JavaFX layout class, e.g. {@link VBox}
 */
public interface IMenu
{
	/**
	 * Getter for the associated Navigation object controlling this IMenu.
	 * @return the controlling Navigation parent.
	 */
	public Navigation getNavigation();
	/**
	 * Setter for the associated Navigation object controlling this IMenu.
	 * @param navigation   the controlling Navigation parent.
	 */
	public void setNavigation(Navigation navigation);
	/**
	 * Called whenever the IMenu is shown/entered by the Navigation.
	 * 
	 * @param args   a list of optional objects passed as parameters. Type checking should ALWAYS be done before casts.
	 */
	public void enter(Object... args);
	/**
	 * Called whenever the IMenu is hidden/exited by the Navigation.
	 */
	public void exit();
	/**
	 * 
	 * @return the ID of the IMenu. This should be unique to each application instance.
	 */
	public String getMenuID();
	public Pane getRoot();
	
	public default void navigate(String id, Object... params) { getNavigation().navigate(id, params); }
	public default boolean matches(IMenu m) { return m.getMenuID().equalsIgnoreCase(getMenuID()); }
	public default void startTask(Task<?> t)
	{
	    Thread thread = new Thread(t);
	    thread.setDaemon(true);
	    thread.start();
	}
}