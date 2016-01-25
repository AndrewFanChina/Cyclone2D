package game.tutorial.c2d;

import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L01-04、缓冲视图优缺点----- <br>
 * 缓冲是图是视图的子类，它本身具有和视图一样的功能。但是其实现使用了一张图片作为缓冲。<br>
 * 这样做的目的是为了加速渲染，当视图内部子控件较多较复杂，但是又非频繁更新时，使用缓冲视图可以带来渲染性能的提升。<br>
 * 其实现的原理，就是将内部子控件全部绘制到缓冲图上，而一起更新到屏幕。这样避免了每次更新屏幕时，即使其内部无变化，<br>
 * 也需要进行对所有子节点重新进行渲染的冗余操作。<br>
 * 但是要注意其带来的缺点，增加了内存的使用。另外当其内部频繁变化时，使用缓冲视图反而会增加渲染负担。<br>
 * @author AndrewFan
 * 
 */
public class L01_04_BufferedView extends C2D_SceneUtil implements LessonUtil
{
	public L01_04_BufferedView()
	{
	} 
	public String getBvrNodeName()
	{
		return "L01_04_BufferedView";
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
		stayView1.setStrFlag("stayView1");
		//视图2的ZOrder设置为2，则会遮挡视图1
		C2D_ViewUtil stayView2 = addInnerView(20, 200, 2, 0x880000);
		stayView2.setStrFlag("stayView2");
		//设置视图2的位置
		stayView2.setPosTo(20, 100);
		//在视图1中添加一个白色视图
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20,1, 0xFFFFFF);
		moveView.setStrFlag("moveView");
		//在白色视图中添加一个绿色视图
		moveView.addInnerView(50, 50, 1, 0x00FF00).setFlag(123).setStrFlag("greenView");
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
