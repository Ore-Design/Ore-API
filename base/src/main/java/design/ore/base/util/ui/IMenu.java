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
	 * @return the controlling Navigation parent.
	 */
	public Navigation getNavigation();
	
	/**
	 * @param navigation   the controlling Navigation parent.
	 */
	public void setNavigation(Navigation navigation);
	
	/**
	 * Called whenever the IMenu is shown/entered by the controlling {@link Navigation}.
	 * 
	 * @param args   a list of optional objects passed as parameters. Type checking should ALWAYS be done before casts.
	 */
	public void enter(Object... args);
	
	/**
	 * Called whenever the IMenu is hidden/exited by the controlling {@link Navigation}.
	 */
	public void exit();
	
	/**
	 * @return the ID of the IMenu. This should be unique to each application instance.
	 */
	public String getMenuID();
	
	/**
	 * @return the JavaFX Pane used as the menu's root.
	 */
	public Pane getRoot();
	
	/**
	 * Utility method that acts as a duplicate of {@link Navigation#navigate(String, Object...)}.
	 * @param id       the id of the IMenu to navigate to.
	 * @param params   an optional collection of Objects to pass in as parameters to the menu.
	 */
	public default void navigate(String id, Object... params) { getNavigation().navigate(id, params); }
	
	/**
	 * Checks if two IMenu IDs match.
	 * @param menu   the menu to check against.
	 * @return       true if the two menu IDs match, otherwise false.
	 */
	public default boolean matches(IMenu menu) { return menu.getMenuID().equalsIgnoreCase(getMenuID()); }
	
	/**
	 * Utility method that starts a new JavaFX Task on a daemon thread.
	 * @param task   the task to run in a new thread.
	 */
	public default void startTask(Task<?> task)
	{
	    Thread thread = new Thread(task);
	    thread.setDaemon(true);
	    thread.start();
	}
}