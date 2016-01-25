package game.tutorial.c2d;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Image;

/**
 * -----L01-02、加载和卸载资源----- <br>
 * 加载和卸载一般位于设置于场景的加载和卸载期间。 <br>
 * 在onAddToStage的时候，执行资源的加载。 在onRemovedFromStage的时候，执行资源的卸载。 <br>
 * 如果希望在不移除当前场景的情况下，显示其它场景，又担心当前场景占用资源过大的情况下，可以在当前场景<br>
 * 的onSentBack(移至后台)方法中卸载部分重磅资源，然后在onSentTop(移植前台)的时候，再次加载回来。<br>
 * 有时候也可以在onHidden和onShown方法中做这些操作，但是注意当前场景的onHidden发生时间在新场景<br>
 * 加载之后，所以使用它会不能避开内存高峰，不过这个方法的优点是它不跟随连续的场景切换做出立刻反应，因此不会<br>
 * 频繁做一些冗余的操作，可以用来设置一些开关变量或者恢复控件状态。<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_02_LoadAndUnload extends C2D_Scene  implements LessonUtil
{
	private static int pageID = 0;

	public L01_02_LoadAndUnload()
	{
		m_iFlag = pageID;
		pageID++;
	}
	public String getBvrNodeName()
	{
		return "L01_02_LoadAndUnload";
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
					stage.pushScene(new L01_02_LoadAndUnload());
				}
				if (keyCode == key_left)
				{
					stage.popScene();
				}
				/**开关这句测试不主动释放按键的效果*/
//				stage.releaseKeys();
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
		if(this.isEmpty())
		{
			//页面标签文本
			C2D_TextBox tb = addTxtBox("<  场景" + m_iFlag + "  >", 0, 0, 1, 0x0000FF);
			tb.setToParentCenter();
			//增加跳转事件
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
							stage.pushScene(new L01_02_LoadAndUnload());
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
		//卸载背景图片
		this.setBGImage((C2D_Image)null);
	}
	/**
	 * 当场景被移至前台时发生
	 */
	protected void onSentTop()
	{
		log("onSentTop");
		//添加背景图片
		this.setBGImage("bg"+(m_iFlag%2)+".jpg");
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
		C2D_Debug.log("----" + m_iFlag + " " + infor);
	}
}
