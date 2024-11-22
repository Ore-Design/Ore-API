package design.ore.base.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.controlsfx.control.Notifications;

import design.ore.base.util.ui.Navigation;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;

public class NotifUtil
{
	@Getter private static Navigation navigation;
	
	public static void initialize(Navigation navigation) throws Exception
	{
		if(NotifUtil.navigation != null) throw new Exception("NotifUtil has already been initialized!");
		
		NotifUtil.navigation = navigation;
	}
	
	public static <T> T runOnApplicationThread(Callable<T> call)
	{
		if(Platform.isFxApplicationThread())
		{
			try { return call.call(); }
			catch (Exception e)
			{
				Log.getLogger().warn(Log.formatThrowable("Error running callable!", e));
				return null;
			}
		}
		else
		{
			final FutureTask<T> task = new FutureTask<>(new Callable<>()
			{
				@Override
				public T call() throws Exception { return call.call(); }
			});
			Platform.runLater(task);
			
			try { return task.get(); }
			catch (InterruptedException | ExecutionException e)
			{
				Log.getLogger().warn(Log.formatThrowable("Error running alert!", e));
				return null;
			}
		}
	}
	
	public static void notify(String title, String message, double seconds)
	{
		runOnApplicationThread(() ->
		{
			try
			{
				Notifications thresholdNotif = Notifications.create().title("Multiple Changes").text("Multiple changes affected!").hideAfter(Duration.seconds(5))
					.owner(navigation.getStage()).position(Pos.TOP_RIGHT);
				
				Notifications.create().threshold(3, thresholdNotif).title(title).text(message).hideAfter(Duration.seconds(seconds))
					.owner(navigation.getStage()).position(Pos.TOP_RIGHT).show();
			}
			catch (Exception e) { Log.getLogger().warn(Log.formatThrowable("Error running notification!", e)); }
			return null;
		});
	}
	
	public static Alert confirm(String title, String message)
	{
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.initOwner(navigation.getStage());
		confirm.setTitle("Confirm");
		confirm.setHeaderText(title);
		confirm.setContentText(message);
		navigation.bindParentToStylesheet(confirm.getDialogPane());
		confirm.initStyle(StageStyle.UNDECORATED);
		confirm.setGraphic(null);
		
		return confirm;
	}
	
	public static Alert info(String title, String message)
	{
		Alert info = new Alert(AlertType.INFORMATION);
		info.initOwner(navigation.getStage());
		info.setTitle("Confirm");
		info.setHeaderText(title);
		info.setContentText(message);
		navigation.bindParentToStylesheet(info.getDialogPane());
		info.initStyle(StageStyle.UNDECORATED);
		info.setGraphic(null);
		
		return info;
	}
	
	public static Alert warn(String title, String message)
	{
		Alert warn = new Alert(AlertType.WARNING);
		warn.initOwner(navigation.getStage());
		warn.setTitle("Confirm");
		warn.setHeaderText(title);
		warn.setContentText(message);
		navigation.bindParentToStylesheet(warn.getDialogPane());
		warn.initStyle(StageStyle.UNDECORATED);
		warn.setGraphic(null);
		
		return warn;
	}
	
	public static Alert error(String title, String message)
	{
		Alert error = new Alert(AlertType.ERROR);
		error.initOwner(navigation.getStage());
		error.setTitle("Error");
		error.setHeaderText(title);
		error.setContentText(message);
		navigation.bindParentToStylesheet(error.getDialogPane());
		error.initStyle(StageStyle.UNDECORATED);
		error.setGraphic(null);
		
		return error;
	}
}
