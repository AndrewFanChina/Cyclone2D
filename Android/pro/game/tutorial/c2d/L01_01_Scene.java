package game.tutorial.c2d;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * -----L01-01、舞台和场景概念-----<br>
 * C2D世界的最顶层有一个舞台的概念，来自于J2me中的画布(Canvas)或者Android<br>
 * 中的GL表面视图(GLSurfaceView)，舞台继承自它们，所以可以在舞台上进行绘画。<br>
 * 舞台的本质是一个场景栈，在舞台可以上演一幕幕的场景，但是同时只能展示一幕场景，<br>
 * 也即场景栈中可以存在多个场景，但是舞台同时只有一个（位于场景栈顶层的）场景能被运行和显示。<br>
 * 场景中有不同演员，在C2D中被设定为各种组件，比如精灵、文本框、图片框等。<br>
 * 本示例主要演示场景的加载和卸载、显示和隐藏、移至前台和后台等操作。<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_01_Scene extends C2D_Scene implements LessonUtil,C2D_Consts
{
	private static int pageID = 0;
	public L01_01_Scene()
	{
		m_iFlag = pageID;
		pageID++;
	}
	public String getBvrNodeName()
	{
		return "L01_01_Scene";
	}

	C2D_Event_KeyPress m_mykeyEvent = new C2D_Event_KeyPress()
	{
		protected boolean doEvent(C2D_Widget carrier, int keyCode)
		{
			C2D_Stage stage = getStageAt();
			if (stage != null)
			{
				if (keyCode == key_right)
				{
					stage.pushScene(new L01_01_Scene());
				}
				if (keyCode == key_left)
				{
					stage.popScene();
				}
				stage.releaseInput();
			}
			return false;
		}
	};

	/**
	 * 当场景添加到舞台(场景栈)时发生
	 */
	protected void onAddedToStage()
	{
		log("onAddedToStage");
		if (this.isEmpty())
		{
			//设定背景颜色
			setBGColor(C2D_Math.getRandomColor(0x88));
			//页面标签文本
			C2D_TextBox tb = addTxtBox("<  场景" + m_iFlag + "  >", 0, 0, 1, 0xFFFFFFFF);
			tb.setToParentCenter();
			//注册按键事件
			this.Events_KeyPress().add(m_mykeyEvent);
			//添加左右按钮
			C2D_PicBox left = addPicBox("arrow_left.png", 0, User_Size.m_height/2, 10,LEFT|VCENTER);
			C2D_PicBox right = addPicBox("arrow_right.png", User_Size.m_width, User_Size.m_height/2, 11,RIGHT|VCENTER);
			left.setFocusable(true);
			right.setFocusable(true);
			left.Events_Button().add(new C2D_Event_Button()
			{
				protected boolean doEvent(C2D_Widget carrier, int btnState)
				{
					if(btnState==Btn_PressBegin)
					{
						C2D_Stage stage = getStageAt();
						if(stage!=null)
						{
							stage.releaseInput();
							stage.popScene();	
						}
					}
					return false;
				}
			});
			right.Events_Button().add(new C2D_Event_Button()
			{
				protected boolean doEvent(C2D_Widget carrier, int btnState)
				{
					if(btnState==Btn_PressBegin)
					{
						C2D_Stage stage = getStageAt();
						if(stage!=null)
						{
							stage.releaseInput();
							stage.pushScene(new L01_01_Scene());	
						}
					}
					return false;
				}
			});
		}
	}

	/**
	 * 当场景从舞台(场景栈)移除时发生
	 */
	protected void onRemovedFromStage()
	{
		log("onRemovedFromStage");
		onRelease();
	}

	/**
	 * 当场景被移至后台时发生
	 */
	protected void onSentBack()
	{
		log("onSentBack");
	}

	/**
	 * 当场景被移至前台时发生
	 */
	protected void onSentTop()
	{
		log("onSentTop");
	}

	/**
	 * 当场景被显示时发生
	 */
	protected void onShown()
	{
		log("onShown");
	}

	/**
	 * 当场景被隐藏时发生
	 */
	protected void onHidden()
	{
		log("onHidden");
	}

	public void log(String infor)
	{
		C2D_Debug.log("----场景" + m_iFlag + " " + infor);
	}
}
