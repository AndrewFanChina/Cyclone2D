package c2d.frame.ext.scene;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.event.C2D_Event_Update;

public class C2D_FpsScene extends C2D_SceneUtil
{
	protected C2D_PicTextBox fpsText;
	protected C2D_View fpsView;
	protected C2D_View createFPS(boolean useBuffer,C2D_PTC imgTextConfig,int zOrder)
	{
		if(fpsText==null)
		{
			fpsText=new C2D_PicTextBox(imgTextConfig);
			fpsText.setText("FPS:6000");
			if(useBuffer)
			{
				fpsView = new C2D_View();
				fpsView.setBGColor(0xFF);
			}
			else
			{
				fpsView = new C2D_View();
			}
			addChild(fpsView);
			fpsView.setSize(fpsText.getPageWidth(),fpsText.getPageHeight());
			fpsView.setPosTo(10, 108);
			fpsView.setAnchor(0);
			fpsView.addChild(fpsText);
			fpsView.setZOrder(zOrder);
			
			C2D_Event_Update fpsEvent = new C2D_Event_Update()
			{
				public boolean doEvent(C2D_Widget carrier)
				{
					if(fpsText!=null)
					{
						C2D_Stage stage=fpsText.getStageAt();
						if(stage!=null)
						{
							String fps="FPS:"+stage.getFps();
							fpsText.setText(fps);
						}
					}
					return false;
				}
			};
			fpsText.Events_Update().add(fpsEvent);	
		}
		return fpsView;
	}
	/**
	 * Ð¶ÔØ×ÊÔ´
	 */
	public void onRelease()
	{
		super.onRelease();
		fpsText = null;
		fpsView = null;
	}
}
