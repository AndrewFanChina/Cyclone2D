package game.tutorial.c2d;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_ChangeFocus;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L03-04���л������¼�----- <br>
 * <br>
 * @author AndrewFan
 * 
 */
public class L03_04_ChangeFocusEvent extends C2D_SceneUtil implements LessonUtil
{
	public L03_04_ChangeFocusEvent()
	{
	}
	public String getBvrNodeName()
	{
		return "L03_04_ChangeFocusEvent";
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
		
		
		C2D_ViewUtil vShowInfor = addView(80, 80, 30, 500, 100, 0);
		final C2D_TextBox  txt = vShowInfor.addTxtBox("��ʾ��Ϣ", 0, 0, 1,0, 0xFF);
		
		w_picBox3.Events_ChangeFocus().add(new C2D_Event_ChangeFocus()
		{
			protected boolean doEvent(C2D_Widget carrier, boolean focused, C2D_Widget another)
			{
				if(focused)
				{
					txt.setText(""+System.currentTimeMillis());	
				}
				return false;
			}
		});
		w_picBox3.Events_Button().add(new C2D_Event_Button()
		{
			protected boolean doEvent(C2D_Widget carrier, int btnState)
			{
				if(btnState==C2D_Consts.Btn_PressBegin)
				{
					getStageAt().popScene();
				}
				return false;
			}
		});
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
