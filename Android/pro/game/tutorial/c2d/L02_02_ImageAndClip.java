package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L02-02��ͼƬ��ͼƬ�п�----- <br>
 * ���ڽ���ͼƬ�Ĵ�����ʽ���ڴ�ռ�ã�ÿ��C2D_Image��������һ����ԴͼƬ��<br>
 * ͼƬ���Ը��ݴ�jar����Я������Դ��ֱ�ӱ�����������Ҳ���Դ�������м��أ�<br>
 * ������ʹ��������ͼ�ϳɣ������γ�C2D_Image��ͼƬ����Ϸ��һ���������ڴ�<br>
 * ������Դ������Ҫ��������ͼƬ���ڴ�ռ�á�
 * @author AndrewFan
 * 
 */
public class L02_02_ImageAndClip extends C2D_SceneUtil implements LessonUtil
{
	public L02_02_ImageAndClip()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_02_ImageAndClip";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		//���ڵı���ɫ
		setBGColor(0x0);
		//��һ��ͼƬ��ʹ���ļ���������Ĭ���ļ��д���res/imgs_other�ļ���
		C2D_PicBox p1=new C2D_PicBox("horse.png");
		addChild(p1,1);
		p1.setPosTo(100, 20);
		//�ڶ���ͼƬ��ʹ�ô����õ�ͼƬ����
		C2D_Image img1 = C2D_Image.createImage("superman.png");
		C2D_PicBox p2=new C2D_PicBox(img1);
		addChild(p2,2);
		p2.setPosTo(200, 20);
		//������ͼƬ��ʹ�ô����õ�ͼƬ�п鹹��
		C2D_ImageClip ic1=new C2D_ImageClip(img1);
		ic1.setContentRect(50, 0, 70, 95);
		C2D_PicBox p3=new C2D_PicBox(ic1);
		addChild(p3,3);
		p3.setPosTo(400, 20);
		//���ĸ�ͼƬ��ʹ�ô����õ�����ͼƬ����
		C2D_Image img4 = C2D_Image.createHttpImage("http://www.baidu.com/img/bdlogo.gif");
		C2D_PicBox p4=new C2D_PicBox(img4);
		addChild(p4,4);
		p4.setPosTo(200, 350);
		
		//����ƶ��¼�
		p3.Events_KeyPress().add(m_moveEvent);
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
