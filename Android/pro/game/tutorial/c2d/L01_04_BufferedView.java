package game.tutorial.c2d;

import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L01-04��������ͼ��ȱ��----- <br>
 * ������ͼ����ͼ�����࣬��������к���ͼһ���Ĺ��ܡ�������ʵ��ʹ����һ��ͼƬ��Ϊ���塣<br>
 * ��������Ŀ����Ϊ�˼�����Ⱦ������ͼ�ڲ��ӿؼ��϶�ϸ��ӣ������ַ�Ƶ������ʱ��ʹ�û�����ͼ���Դ�����Ⱦ���ܵ�������<br>
 * ��ʵ�ֵ�ԭ�����ǽ��ڲ��ӿؼ�ȫ�����Ƶ�����ͼ�ϣ���һ����µ���Ļ������������ÿ�θ�����Ļʱ����ʹ���ڲ��ޱ仯��<br>
 * Ҳ��Ҫ���ж������ӽڵ����½�����Ⱦ�����������<br>
 * ����Ҫע���������ȱ�㣬�������ڴ��ʹ�á����⵱���ڲ�Ƶ���仯ʱ��ʹ�û�����ͼ������������Ⱦ������<br>
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
		//���ڵı���ɫ
		setBGColor(0x0);
		//�ڳ�������������ڲ���ͼ
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		stayView1.setStrFlag("stayView1");
		//��ͼ2��ZOrder����Ϊ2������ڵ���ͼ1
		C2D_ViewUtil stayView2 = addInnerView(20, 200, 2, 0x880000);
		stayView2.setStrFlag("stayView2");
		//������ͼ2��λ��
		stayView2.setPosTo(20, 100);
		//����ͼ1�����һ����ɫ��ͼ
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20,1, 0xFFFFFF);
		moveView.setStrFlag("moveView");
		//�ڰ�ɫ��ͼ�����һ����ɫ��ͼ
		moveView.addInnerView(50, 50, 1, 0x00FF00).setFlag(123).setStrFlag("greenView");
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
