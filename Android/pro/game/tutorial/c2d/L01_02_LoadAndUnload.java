package game.tutorial.c2d;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Image;

/**
 * -----L01-02�����غ�ж����Դ----- <br>
 * ���غ�ж��һ��λ�������ڳ����ļ��غ�ж���ڼ䡣 <br>
 * ��onAddToStage��ʱ��ִ����Դ�ļ��ء� ��onRemovedFromStage��ʱ��ִ����Դ��ж�ء� <br>
 * ���ϣ���ڲ��Ƴ���ǰ����������£���ʾ�����������ֵ��ĵ�ǰ����ռ����Դ���������£������ڵ�ǰ����<br>
 * ��onSentBack(������̨)������ж�ز����ذ���Դ��Ȼ����onSentTop(��ֲǰ̨)��ʱ���ٴμ��ػ�����<br>
 * ��ʱ��Ҳ������onHidden��onShown����������Щ����������ע�⵱ǰ������onHidden����ʱ�����³���<br>
 * ����֮������ʹ�����᲻�ܱܿ��ڴ�߷壬��������������ŵ����������������ĳ����л��������̷�Ӧ����˲���<br>
 * Ƶ����һЩ����Ĳ�����������������һЩ���ر������߻ָ��ؼ�״̬��<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_02_LoadAndUnload extends C2D_Scene  implements LessonUtil
{
	private static int pageID = 0;

	public L01_02_LoadAndUnload()
	{
		m_iFlag = pageID;
		pageID++;
	}
	public String getBvrNodeName()
	{
		return "L01_02_LoadAndUnload";
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
					stage.pushScene(new L01_02_LoadAndUnload());
				}
				if (keyCode == key_left)
				{
					stage.popScene();
				}
				/**���������Բ������ͷŰ�����Ч��*/
//				stage.releaseKeys();
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
		if(this.isEmpty())
		{
			//ҳ���ǩ�ı�
			C2D_TextBox tb = addTxtBox("<  ����" + m_iFlag + "  >", 0, 0, 1, 0x0000FF);
			tb.setToParentCenter();
			//������ת�¼�
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
							stage.pushScene(new L01_02_LoadAndUnload());
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
		//ж�ر���ͼƬ
		this.setBGImage((C2D_Image)null);
	}
	/**
	 * ������������ǰ̨ʱ����
	 */
	protected void onSentTop()
	{
		log("onSentTop");
		//��ӱ���ͼƬ
		this.setBGImage("bg"+(m_iFlag%2)+".jpg");
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
		C2D_Debug.log("----" + m_iFlag + " " + infor);
	}
}
