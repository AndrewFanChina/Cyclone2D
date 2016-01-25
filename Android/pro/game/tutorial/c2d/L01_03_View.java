package game.tutorial.c2d;

import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L01-03、视图的树状结构----- <br>
 * C2D中有两类控件，第一类是普通控件，它们存在于第二类控件容器（视图 C2D_View）中。<br>
 * 1、树状结构的归属关系。<br>
 * 视图占据了画布上的一块矩形区域，它本身也是一个控件容器，内部可以容纳其它的子控件。<br>
 * 容器内部的控件层级呈树状分布，场景也是一个视图，它占据整个画布区域，是树状图的根节点，<br>
 * 场景内部的视图是其子节点，叶节点是各类具体的控件，如文本框，图片框，精灵等。<br>
 * 视图可以加入子节点(addChild)，可以移除节点(removeChild) ，获得节点(getChildAt)。<br>
 * 注意getChildAt是根据兄弟节点之间的排序(ZOrder)来获取的。如果需要取回之前存入容器中的控件，<br>
 * 应该使用getChildByFlag(String)或者getChildByFlag(int)方法，因为在容器中添加或<br>
 * 者移除控件会引起子节点的重新排序。<br>
 * 场景节点(C2D_Scene)是控件树的根节点，控件树中所有的控件都可以使用getSceneAt()追溯到其存在的场景，<br>
 * 也可以使用getStageAt()追溯到所存在的舞台。<br>
 * 2、相对父容器的平面坐标。<br>
 * 所有控件都继承自最顶层的C2D_Widget类,均拥有平面坐标m_x、m_y、锚点m_anchor、<br>
 * m_ZOrder深度排序值等基本属性。它们的平面坐标均是相对坐标，相对于其父容器 。<br>
 * 因此，当父容器被移动时，所有的子树将跟随一起移动。在获取这些属性的时候，需要使用函 数getXXX，<br>
 * 设置的时候需要使用setXXX，即使继承的类也需要这样调用，一定不能直接 改变属性值。<br>
 * 3、锚点坐标<br>
 * 锚点m_anchor用于设定当前控件的对齐方式，对于有面积概念的控件，此值均会生效。例如视图、图片框、文本框等。<br>
 * 目前无面积概念的控件只有精灵(C2D_Sprite)和线段(C2D_Segement)。<br>
 * 4、自动排序的深度坐标<br>
 * Z排序值被用于兄弟节点之间的排序，此数值越小，控件越处于下层。 <br>
 * 也即深度坐标(m_ZOrder)则体现了兄弟节点间的深度关系，此值越小，将越深入屏幕内部，越容易被其它节点所覆盖。<br>
 * 5、视图的其它属性<br>
 * 视图除了上层可以添加各类普通控件，下层还可以拥有背景色和背景图片，如果你不设置它们， 视图将成为一个透明的容器。<br>
 * 注意本节使用了C2D_SceneUtil，它是C2D_Scene的便利子类，可以使用它的一些便利方法。<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_03_View extends C2D_SceneUtil implements LessonUtil
{
	public L01_03_View()
	{
	}
	public String getBvrNodeName()
	{
		return "L01_03_View";
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
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20, 1, 0xFFFFFF);
		//在白色视图中添加一个绿色视图
		moveView.addInnerView(50, 50, 1, 0x00FF00).setFlag(123);
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
