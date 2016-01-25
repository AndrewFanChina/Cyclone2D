package game.tutorial.c2d;

import c2d.frame.com.shape.C2D_Segement;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.mod.C2D_Consts;
/**
 * -----L02-03、基本的形状控件----- <br>
 * C2D支持简单的形状，如线条(C2D_Segement)，要注意线条的表现形式，它的坐标规定了一个起点，<br>
 * 端点(End)代表了相对于起点的“相对”位移。矩形(C2D_Rectangle)，可以拥有填充颜色和边框颜色，<br>
 * 圆形(C2D_Circle)可以用来建立椭圆或者圆形，也可以拥有边框和填充颜色。<br>
 * @author AndrewFan
 *
 */
public class L02_03_Shapes extends C2D_SceneUtil implements LessonUtil, C2D_Consts
{

	public L02_03_Shapes()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_03_Shapes";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		// 设置背景颜色
		setBGColor(0xFF);
		// 添加多个线段(线段无锚点概念)
		C2D_ViewUtil stayView1 = addInnerView(60, 40, 1, 0x666666);
		float w = stayView1.getWidth();
		float h = stayView1.getHeight();
		final int aa[][] =
		{
				new int[]{ 200, 0 },// 向右
				new int[]{ 0, 200 },// 向上
				new int[]{ -200, 0 },// 向左
				new int[]{ 0, -200 },// 向下
				new int[]{ 141, -141 },// 向右上
				new int[]{ 141, 141 },// 向右下
				new int[]{ -141, 141 },// 向左下
				new int[]{ -141, -141 },// 向左上
		};
		for (int i = 0; i < aa.length; i++)
		{
			final C2D_Segement testI = new C2D_Segement();
			stayView1.addChild(testI);
			testI.setZOrder(1000 + i);
			testI.setPosTo(w / 2, h / 2);
			testI.setColor(C2D_Math.getRandomColor());
			testI.setPointEnd(aa[i][0], aa[i][1]);
			testI.setFlag(i);
			testI.Events_KeyPress().add(m_moveEvent);
		}
		// 添加矩形--------------------------------
		// 绿色
		stayView1.addRectBox(w / 2, h / 2, 10, 80, 60, LEFT | TOP, 0x00FF00);
		// 黄色
		stayView1.addRectBox(w / 2, h / 2, 11, 80, 60, RIGHT | TOP, 0xFFFF00);
		// 天蓝色
		stayView1.addRectBox(w / 2, h / 2, 12, 80, 60, LEFT | BOTTOM, 0x00FFFF);
		// 洋红色
		stayView1.addRectBox(w / 2, h / 2, 13, 80, 60, RIGHT | BOTTOM, 0xFF00FF);
		// 添加带边框的矩形
		stayView1.addBorderRectBox(w / 2, h / 2, 5, 360, 280, HVCENTER, 0x0, 0xFFFFFF);// .Events_KeyPress().add(m_moveEvent);;
		// 添加圆形--------------------------------
		stayView1.addCircleBox(w / 2, h / 2, 15, 120, 80, HVCENTER, 0x630015).Events_KeyPress().add(m_moveEvent);
		//设置返回键退出
		setBackKey(key_back);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}
	
}
