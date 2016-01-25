package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L03-03、光标的概念----- <br>
 * 每个控件上都可以拥有一个光标，光标默认是一个简单的图片切块。<br>
 * 在通常的UI界面编写过程中，我们应该尽量使用我们定义的光标来实现相应功能。<br>
 * @author AndrewFan
 * 
 */
public class L03_03_Focus extends C2D_SceneUtil implements LessonUtil
{
	public L03_03_Focus()
	{
	}
	public String getBvrNodeName()
	{
		return "L03_03_Focus";
	}
	private C2D_Image m_picBox,m_focus;
	private C2D_PicBox w_picBox1,w_picBox2,w_picBox3;
	private C2D_ImageClip m_focusC;
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		//纯黑的背景色
		setBGColor(0x0);
		//加载卸载资源
		if(m_picBox==null)
		{
			m_picBox=C2D_Image.createImage("picBox.png");
		}
		if(m_focus==null)
		{
			m_focus=C2D_Image.createImage("focus.png");
			m_focusC = new C2D_ImageClip(m_focus);
		}
		//添加一个图片框，作为按钮
		w_picBox1 = addPicBox(m_picBox, 320, 60, 1);
		w_picBox1.setFocusImage(m_focusC, 0, 0);
		
		w_picBox2 = addPicBox(m_picBox, 400, 160, 2);
		w_picBox2.setFocusImage(m_focusC, 0, 0);
		
		w_picBox3 = addPicBox(m_picBox, 100, 260, 3);
		w_picBox3.setFocusImage(m_focusC, 0, 0);
		
		//设置当前的焦点
		getSceneAt().setFocusedWidget(w_picBox1);
		//设置返回键退出
		setBackKey(key_back);
	}

	protected void onPaint(C2D_Graphics g)
	{
		super.onPaint(g);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
		if(m_picBox!=null)
		{
			m_picBox.releaseBitmap();
			m_picBox=null;
		}
		if(m_focus!=null)
		{
			m_focus.releaseBitmap();
			m_focus=null;
		}
	}

	protected void onSentBack()
	{
	}

	protected void onSentTop()
	{
	}

	protected void onShown()
	{
	}

	protected void onHidden()
	{
	}
}
