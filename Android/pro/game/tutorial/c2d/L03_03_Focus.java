package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L03-03�����ĸ���----- <br>
 * ÿ���ؼ��϶�����ӵ��һ����꣬���Ĭ����һ���򵥵�ͼƬ�п顣<br>
 * ��ͨ����UI�����д�����У�����Ӧ�þ���ʹ�����Ƕ���Ĺ����ʵ����Ӧ���ܡ�<br>
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
		//���ڵı���ɫ
		setBGColor(0x0);
		//����ж����Դ
		if(m_picBox==null)
		{
			m_picBox=C2D_Image.createImage("picBox.png");
		}
		if(m_focus==null)
		{
			m_focus=C2D_Image.createImage("focus.png");
			m_focusC = new C2D_ImageClip(m_focus);
		}
		//���һ��ͼƬ����Ϊ��ť
		w_picBox1 = addPicBox(m_picBox, 320, 60, 1);
		w_picBox1.setFocusImage(m_focusC, 0, 0);
		
		w_picBox2 = addPicBox(m_picBox, 400, 160, 2);
		w_picBox2.setFocusImage(m_focusC, 0, 0);
		
		w_picBox3 = addPicBox(m_picBox, 100, 260, 3);
		w_picBox3.setFocusImage(m_focusC, 0, 0);
		
		//���õ�ǰ�Ľ���
		getSceneAt().setFocusedWidget(w_picBox1);
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
