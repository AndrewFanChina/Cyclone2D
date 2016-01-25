package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L03-01����õ�ͼƬ��----- <br>
 * ͼƬ����������������ʽ�����������Ҫ��ʾһ��ͼƬ����Ҫʹ��C2D_PictureBox��<br>
 * ������ʹ�ö��ַ�ʽ�����ͺܷ���ر���ʾ����Ļ�ϡ�<br>
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
		//���ڵı���ɫ
		setBGColor(0x0);
		//�ڳ�������������ڲ���ͼ
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		//��ͼ2��ZOrder����Ϊ2������ڵ���ͼ1
		C2D_ViewUtil stayView2 = addInnerView(20, 200, 2, 0x880000);
		//������ͼ2��λ��
		stayView2.setPosTo(20, 100);
		//����ͼ1�����һ����ɫ��ͼ
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20,1, 0xFFFFFF);
		//�ڰ�ɫ��ͼ�����һ��ͼƬ��
		C2D_PicBox p1=new C2D_PicBox("horse.png");
		moveView.addChild(p1);
		p1.setToParentCenter();
		//����ƶ��¼�
		moveView.Events_KeyPress().add(m_moveEvent);
		//���÷��ؼ��˳�
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
