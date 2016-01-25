package game.tutorial.c2d;

import c2d.frame.com.shape.C2D_Segement;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.mod.C2D_Consts;
/**
 * -----L02-03����������״�ؼ�----- <br>
 * C2D֧�ּ򵥵���״��������(C2D_Segement)��Ҫע�������ı�����ʽ����������涨��һ����㣬<br>
 * �˵�(End)��������������ġ���ԡ�λ�ơ�����(C2D_Rectangle)������ӵ�������ɫ�ͱ߿���ɫ��<br>
 * Բ��(C2D_Circle)��������������Բ����Բ�Σ�Ҳ����ӵ�б߿�������ɫ��<br>
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
		// ���ñ�����ɫ
		setBGColor(0xFF);
		// ��Ӷ���߶�(�߶���ê�����)
		C2D_ViewUtil stayView1 = addInnerView(60, 40, 1, 0x666666);
		float w = stayView1.getWidth();
		float h = stayView1.getHeight();
		final int aa[][] =
		{
				new int[]{ 200, 0 },// ����
				new int[]{ 0, 200 },// ����
				new int[]{ -200, 0 },// ����
				new int[]{ 0, -200 },// ����
				new int[]{ 141, -141 },// ������
				new int[]{ 141, 141 },// ������
				new int[]{ -141, 141 },// ������
				new int[]{ -141, -141 },// ������
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
		// ��Ӿ���--------------------------------
		// ��ɫ
		stayView1.addRectBox(w / 2, h / 2, 10, 80, 60, LEFT | TOP, 0x00FF00);
		// ��ɫ
		stayView1.addRectBox(w / 2, h / 2, 11, 80, 60, RIGHT | TOP, 0xFFFF00);
		// ����ɫ
		stayView1.addRectBox(w / 2, h / 2, 12, 80, 60, LEFT | BOTTOM, 0x00FFFF);
		// ���ɫ
		stayView1.addRectBox(w / 2, h / 2, 13, 80, 60, RIGHT | BOTTOM, 0xFF00FF);
		// ��Ӵ��߿�ľ���
		stayView1.addBorderRectBox(w / 2, h / 2, 5, 360, 280, HVCENTER, 0x0, 0xFFFFFF);// .Events_KeyPress().add(m_moveEvent);;
		// ���Բ��--------------------------------
		stayView1.addCircleBox(w / 2, h / 2, 15, 120, 80, HVCENTER, 0x630015).Events_KeyPress().add(m_moveEvent);
		//���÷��ؼ��˳�
		setBackKey(key_back);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}
	
}
