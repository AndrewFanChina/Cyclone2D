package game.tutorial.c2d;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * -----L01-01����̨�ͳ�������-----<br>
 * C2D����������һ����̨�ĸ��������J2me�еĻ���(Canvas)����Android<br>
 * �е�GL������ͼ(GLSurfaceView)����̨�̳������ǣ����Կ�������̨�Ͻ��л滭��<br>
 * ��̨�ı�����һ������ջ������̨��������һĻĻ�ĳ���������ͬʱֻ��չʾһĻ������<br>
 * Ҳ������ջ�п��Դ��ڶ��������������̨ͬʱֻ��һ����λ�ڳ���ջ����ģ������ܱ����к���ʾ��<br>
 * �������в�ͬ��Ա����C2D�б��趨Ϊ������������羫�顢�ı���ͼƬ��ȡ�<br>
 * ��ʾ����Ҫ��ʾ�����ļ��غ�ж�ء���ʾ�����ء�����ǰ̨�ͺ�̨�Ȳ�����<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_01_Scene extends C2D_Scene implements LessonUtil,C2D_Consts
{
	private static int pageID = 0;
	public L01_01_Scene()
	{
		m_iFlag = pageID;
		pageID++;
	}
	public String getBvrNodeName()
	{
		return "L01_01_Scene";
	}

	C2D_Event_KeyPress m_mykeyEvent = new C2D_Event_KeyPress()
	{
		protected boolean doEvent(C2D_Widget carrier, int keyCode)
		{
			C2D_Stage stage = getStageAt();
			if (stage != null)
			{
				if (keyCode == key_right)
				{
					stage.pushScene(new L01_01_Scene());
				}
				if (keyCode == key_left)
				{
					stage.popScene();
				}
				stage.releaseInput();
			}
			return false;
		}
	};

	/**
	 * ��������ӵ���̨(����ջ)ʱ����
	 */
	protected void onAddedToStage()
	{
		log("onAddedToStage");
		if (this.isEmpty())
		{
			//�趨������ɫ
			setBGColor(C2D_Math.getRandomColor(0x88));
			//ҳ���ǩ�ı�
			C2D_TextBox tb = addTxtBox("<  ����" + m_iFlag + "  >", 0, 0, 1, 0xFFFFFFFF);
			tb.setToParentCenter();
			//ע�ᰴ���¼�
			this.Events_KeyPress().add(m_mykeyEvent);
			//������Ұ�ť
			C2D_PicBox left = addPicBox("arrow_left.png", 0, User_Size.m_height/2, 10,LEFT|VCENTER);
			C2D_PicBox right = addPicBox("arrow_right.png", User_Size.m_width, User_Size.m_height/2, 11,RIGHT|VCENTER);
			left.setFocusable(true);
			right.setFocusable(true);
			left.Events_Button().add(new C2D_Event_Button()
			{
				protected boolean doEvent(C2D_Widget carrier, int btnState)
				{
					if(btnState==Btn_PressBegin)
					{
						C2D_Stage stage = getStageAt();
						if(stage!=null)
						{
							stage.releaseInput();
							stage.popScene();	
						}
					}
					return false;
				}
			});
			right.Events_Button().add(new C2D_Event_Button()
			{
				protected boolean doEvent(C2D_Widget carrier, int btnState)
				{
					if(btnState==Btn_PressBegin)
					{
						C2D_Stage stage = getStageAt();
						if(stage!=null)
						{
							stage.releaseInput();
							stage.pushScene(new L01_01_Scene());	
						}
					}
					return false;
				}
			});
		}
	}

	/**
	 * ����������̨(����ջ)�Ƴ�ʱ����
	 */
	protected void onRemovedFromStage()
	{
		log("onRemovedFromStage");
		onRelease();
	}

	/**
	 * ��������������̨ʱ����
	 */
	protected void onSentBack()
	{
		log("onSentBack");
	}

	/**
	 * ������������ǰ̨ʱ����
	 */
	protected void onSentTop()
	{
		log("onSentTop");
	}

	/**
	 * ����������ʾʱ����
	 */
	protected void onShown()
	{
		log("onShown");
	}

	/**
	 * ������������ʱ����
	 */
	protected void onHidden()
	{
		log("onHidden");
	}

	public void log(String infor)
	{
		C2D_Debug.log("----����" + m_iFlag + " " + infor);
	}
}
