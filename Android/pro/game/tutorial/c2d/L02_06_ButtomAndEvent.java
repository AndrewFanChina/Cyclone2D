package game.tutorial.c2d;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_Button;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L02-06、按钮与按钮事件----- <br>
 * 
 * @author AndrewFan
 * 
 */
public class L02_06_ButtomAndEvent extends C2D_SceneUtil implements LessonUtil
{
	C2D_Image m_imgBtn;
	C2D_ImageClip m_focusClip;
	C2D_ImageClip m_floatClip;
	C2D_ImageClip m_pressedClip;

	public L02_06_ButtomAndEvent()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_06_ButtomAndEvent";
	}
	C2D_Event_Button m_btnEvent=new C2D_Event_Button()
	{
		protected boolean doEvent(C2D_Widget carrier, int btnState)
		{
			C2D_Debug.logDebug("btnState:"+btnState);
			int flag=carrier.getFlag()-10;
			switch(flag)
			{
			case 0:
				C2D_Debug.logDebug("来自按钮一");
				break;
			case 1:
				C2D_Debug.logDebug("来自按钮二");
				break;
			case 2:
				C2D_Debug.logDebug("来自按钮三");
				break;
			case 3:
				C2D_Debug.logDebug("来自按钮四");
				break;
			case 4:
				C2D_Debug.logDebug("来自按钮五");
				break;
			case 5:
				C2D_Debug.logDebug("来自按钮六");
				break;
			}
			return false;
		}
	};
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		//纯黑的背景色
		setBGColor(0x0);
		//在场景中添加两个内部视图
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		//加载资源
		m_imgBtn = C2D_Image.createImage("btns.png");
		int w = m_imgBtn.bitmapWidth();
		int h = m_imgBtn.bitmapHeight()/ 3;
		m_focusClip = new C2D_ImageClip(m_imgBtn);
		m_focusClip.setContentRect(0, 0, w, h);
		m_floatClip = new C2D_ImageClip(m_imgBtn);
		m_floatClip.setContentRect(0, h, w, h);
		m_pressedClip = new C2D_ImageClip(m_imgBtn);
		m_pressedClip.setContentRect(0, 2 * h, w, h);
		//创建按钮
		for (int i = 0; i < 6; i++)
		{
			C2D_Button m_btn = new C2D_Button(m_floatClip, m_focusClip, m_pressedClip);
			stayView1.addChild(m_btn, 10 + i);
			m_btn.setPosTo(C2D_Math.getRandom(100,200), 100 + i * 40);
			m_btn.setFlag(10 + i);
			m_btn.Events_Button().add(m_btnEvent);
		}
		getSceneAt().setFocusedWidget(stayView1.getChildByFlag(10));
		//设置返回键退出
		setBackKey(key_back);
	}

	public void onRelease()
	{
		super.onRelease();
		m_imgBtn = null;
		m_focusClip = null;
		m_floatClip = null;
		m_pressedClip = null;
	}
}
