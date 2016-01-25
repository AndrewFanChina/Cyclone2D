package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L03-01、最常用的图片框----- <br>
 * 图片框是最基本的组件形式，如果我们需要显示一张图片，需要使用C2D_PictureBox。<br>
 * 它可以使用多种方式创建和很方便地被显示在屏幕上。<br>
 * @author AndrewFan
 * 
 */
public class L03_05_SceneEvent extends C2D_SceneUtil implements LessonUtil
{
	public L03_05_SceneEvent()
	{
	}
	public String getBvrNodeName()
	{
		return "L03_05_SceneEvent";
	}
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
		//视图2的ZOrder设置为2，则会遮挡视图1
		C2D_ViewUtil stayView2 = addInnerView(20, 200, 2, 0x880000);
		//设置视图2的位置
		stayView2.setPosTo(20, 100);
		//在视图1中添加一个白色视图
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20,1, 0xFFFFFF);
		//在白色视图中添加一个图片框
		C2D_PicBox p1=new C2D_PicBox("horse.png");
		moveView.addChild(p1);
		p1.setToParentCenter();
		//添加移动事件
		moveView.Events_KeyPress().add(m_moveEvent);
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
