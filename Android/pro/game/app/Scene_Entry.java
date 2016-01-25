package game.app;

import game.core.util.Scene_List;
import game.tutorial.c2d.Scene_Lenssons;
import c2d.plat.gfx.C2D_Graphics;

public class Scene_Entry extends Scene_List
{
	private static final String Items[]=new String[]
	{
		"C2D引擎教程"
	};
	public Scene_Entry()
	{
		super(Items);
		m_lessonsScene=new Scene_Lenssons();
	}
	public Scene_Lenssons m_lessonsScene;
	
	public boolean onItemClicked(int itemID)
	{
		MainStage stage = (MainStage)getStageAt();
		switch(itemID)
		{
		case 0:
			openScene(m_lessonsScene);
			return true;
		}
		return false;
	}

	public void onRelease()
	{
		super.onRelease();
		if(m_lessonsScene!=null)
		{
			m_lessonsScene.doRelease(this);
			m_lessonsScene=null;
		}
	}
	public void onPaint(C2D_Graphics g)
	{
		super.onPaint(g);
	}
	public String getBvrNodeName()
	{
		return "入口场景";
	}
}
