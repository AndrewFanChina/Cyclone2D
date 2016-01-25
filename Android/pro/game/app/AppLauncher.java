package game.app;

import c2d.lang.app.C2D_App;
import c2d.lang.app.C2D_Canvas;

public class AppLauncher extends C2D_App
{
	public static MainStage mainCanvas = null;
	protected C2D_Canvas onCreate_C2D()
	{
		if (mainCanvas == null)
		{
			mainCanvas = new MainStage();
		}
		return mainCanvas;
	}
	
	protected void onCloseApp_C2D()
	{
		
	}

	@Override
	protected void onPauseApp_C2D()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResumeApp_C2D()
	{
		// TODO Auto-generated method stub
		
	}
}
