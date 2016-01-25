package game.tutorial.c2d;

import game.core.util.Scene_List;
import c2d.frame.base.C2D_Scene;
import c2d.lang.math.C2D_Array;

public class Scene_Lenssons extends Scene_List
{
	// 当前条目
	static String Items[] = new String[]
	{ 
		"L01_01、舞台和场景概念", "L01_02、加载和卸载资源",
		"L01_03、视图的树状结构", "L01_04、缓冲视图优缺点",
		"L02_01、最常用的图片框", "L02_02、图片和图片切块",
		"L02_03、基本的形状控件", "L02_04、使用系统文本框", 
		"L02_05、使用图片文本框", "L02_06、按钮与按钮事件",
		"L03_01、基础的更新事件", "L03_02、控件的按键事件",
		"L03_03、光标表现与机制", "L03_04、光标的切换事件",
		"L03_05、伴随场景的事件",
		"L04_01、精灵动画的控制", "L04_02、精灵动画的事件",
		"L05_01、三片图使用方法", "L05_02、九片图使用方法",
		"L05_03、列表的使用方法", "L05_04、列表项滚动事件",
		"L05_05、列表项点击事件",	 "L05_06、数字文本输入框", 
		"L05_07、精灵按钮与事件", "L05_08、选择框使用方法",
		"L06_01、管理图片的列表", "L06_02、图片文本配置表",
		"L06_03、资源的夺权机制",
		"L07_01、场景上的对话框", "L07_02、友好的过渡场景",
		"L07_03、输入文本组合框", "L07_04、选择组合框与事件",
	};
	C2D_Array m_lessons;

	public Scene_Lenssons()
	{
		super(Items);
	}
	public void onAddedToStage()
	{
		super.onAddedToStage();
		if(m_lessons==null)
		{
			m_lessons = new C2D_Array();
			m_lessons.addElement(new L01_01_Scene());
			m_lessons.addElement(new L01_02_LoadAndUnload());
			m_lessons.addElement(new L01_03_View());
			m_lessons.addElement(new L01_04_BufferedView());
			m_lessons.addElement(new L02_01_PictrueBox());
			m_lessons.addElement(new L02_02_ImageAndClip());
			m_lessons.addElement(new L02_03_Shapes());
			m_lessons.addElement(new L02_04_TxtBox());
			m_lessons.addElement(new L02_05_PictureTxtBox());
			m_lessons.addElement(new L02_06_ButtomAndEvent());
			m_lessons.addElement(new L03_01_UpdateEvent());
			m_lessons.addElement(new L03_02_KeyPressEvent());
			m_lessons.addElement(new L03_03_Focus());
			m_lessons.addElement(new L03_04_ChangeFocusEvent());
			m_lessons.addElement(new L03_05_SceneEvent());
			m_lessons.addElement(new L04_01_Sprites());
		}
		
	}

	public boolean onItemClicked(int itemID)
	{
		openScene((C2D_Scene) m_lessons.elementAt(itemID));
		return true;
	}

	public void onRelease()
	{
		super.onRelease();
		if(m_lessons!=null)
		{
			int size=m_lessons.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Scene si=(C2D_Scene)m_lessons.elementAt(i);
				if(si!=null)
				{
					si.doRelease(this);
				}
			}
			m_lessons.clear();
			m_lessons=null;
		}
	}
	
}
