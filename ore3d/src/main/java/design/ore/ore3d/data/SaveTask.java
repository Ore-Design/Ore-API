package design.ore.ore3d.data;

import design.ore.base.util.Log;
import javafx.concurrent.Task;

public abstract class SaveTask<T> extends Task<T>
{
	@Override
	protected void failed()
	{
		super.failed();
		
		updateProgress(100, 100);
		updateMessage("Failed - " + getException().getMessage());
		Log.getLogger().warn(Log.formatThrowable("Update taks failed!", getException()));
	}
}
