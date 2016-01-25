package game.tutorial.c2d;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_Patch9;
import c2d.frame.event.C2D_Event_Update;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

public class L05_02_Patch9 extends C2D_SceneUtil
{
	public L05_02_Patch9()
	{
	}

	protected void onAddedToStage()
	{
		setBGColor(0);
		C2D_Image img = C2D_Image.createImage("bg_n.png","imgs_sns/");
		C2D_ImageClip imgClip=new C2D_ImageClip(img);
		imgClip.setContentRect(1, 62, 150, 150);
		final C2D_Patch9 p3_1=new C2D_Patch9(imgClip);
		this.addChild(p3_1);
		p3_1.setPosTo(0, 0);
		p3_1.setSize(375,175);
		
		final C2D_Patch9 p3_2=new C2D_Patch9(imgClip);
		this.addChild(p3_2);
		p3_2.setContentRect(152, 68, 36, 44);
		p3_2.setSize(200,44);
		p3_2.setPosTo(0, 150);
		
		this.Events_Update().add(new C2D_Event_Update()
		{
			int x=0;
			protected boolean doEvent(C2D_Widget carrier)
			{
				if(x<90)
				{
					p3_1.setPosTo(x,x);
					p3_2.setPosTo(x,x+150);
					x++;
					return false;
				}
				return true;
			}
		});
	}

	protected void onRemovedFromStage()
	{

	}

}
