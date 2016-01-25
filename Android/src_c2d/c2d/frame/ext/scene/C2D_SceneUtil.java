package c2d.frame.ext.scene;

import c2d.frame.base.C2D_Dialog;
import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_TransScene;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_KeyPress;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

public class C2D_SceneUtil extends C2D_Scene implements C2D_Consts
{
	/** �����˳��¼� */
	private C2D_Event_KeyPress m_keyExitEvent;
	/**
	 * ���ɳ���
	 */
	private static C2D_TransScene m_TransScene;

	/**
	 * ���ù��ɳ���
	 * 
	 * @param transScene
	 */
	public static void setTransScene(C2D_TransScene transScene)
	{
		m_TransScene = transScene;
	}

	/**
	 * ��ȡ��ǰ�Ĺ��ɳ���
	 * 
	 * @return ���ɳ���
	 */
	public static C2D_TransScene getTransScene()
	{
		return m_TransScene;
	}

	/**
	 * ���÷���ҳ�棨���ؼ�������
	 * 
	 * @param backScene
	 */
	public void setBackScene(final C2D_Scene backScene)
	{
		Events_Update().add(new C2D_Event_Update()
		{
			protected boolean doEvent(C2D_Widget carrier)
			{
				C2D_Stage stage = getStageAt();
				if (stage != null && stage.isKeyCancle())
				{
					stage.releaseKeys();
					transToScene(backScene);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Ϊĳ����ť�ؼ���ָ����¸�ҳ��
	 * 
	 * @param wiget
	 *            �ؼ�
	 * @param nextScene
	 *            �¸�ҳ��
	 */
	protected void bindNextScene(C2D_Widget wiget, final C2D_Scene nextScene)
	{
		wiget.Events_Button().add(new C2D_Event_Button()
		{
			C2D_Scene m_nextScene = nextScene;
			protected boolean doEvent(C2D_Widget carrier, int btnState)
			{
				if (btnState == C2D_Consts.Btn_PressBegin)
				{
					if(m_nextScene!=null)
					{
						if(m_nextScene instanceof C2D_Dialog)
						{
							C2D_Stage stage=getStageAt();
							if(stage!=null)
							{
								stage.pushDialog(((C2D_Dialog)m_nextScene));
							}
							
						}
						else
						{
							transToScene(m_nextScene);
						}
					}
				}
				return false;
			}
		});
	}

	/**
	 * �л���ָ���������Ὣ��ǰ��������
	 * 
	 * @param nextScene
	 *            ��һ������
	 */
	public void goToScene(C2D_Scene nextScene)
	{
		C2D_Stage stage = getStageAt();
		if (stage != null)
		{
			stage.popScene();
			stage.pushScene(nextScene);
			stage.releaseKeys();
		}
	}

	/**
	 * ���ɵ�ָ���������Ὣ��ǰ��������
	 * 
	 * @param nextScene
	 *            ��һ������
	 */
	public void transToScene(C2D_Scene nextScene)
	{
		C2D_Stage stage = getStageAt();
		if (stage != null)
		{
			stage.popScene();
			stage.pushScene(nextScene, m_TransScene);
			stage.releaseKeys();
		}
	}

	/**
	 * ��ָ��ָ������
	 * 
	 * @param nextScene
	 *            ��һ������
	 */
	public void openScene(C2D_Scene nextScene)
	{
		C2D_Stage stage = getStageAt();
		if (stage != null)
		{
			stage.pushScene(nextScene);
			stage.releaseKeys();
		}
	}

	/**
	 * ���÷��ذ���
	 * 
	 * @param backKey
	 *            ����
	 */
	public void setBackKey(final int backKey)
	{
		if (m_keyExitEvent != null)
		{
			Events_KeyPress().remove(m_keyExitEvent);
			m_keyExitEvent = null;
		}
		m_keyExitEvent = new C2D_Event_KeyPress()
		{
			protected boolean doEvent(C2D_Widget carrier, int keyCode)
			{
				if (keyCode == backKey)
				{
					C2D_Stage stage = getStageAt();
					if (stage != null)
					{
						close();
						if (stage.noScene())
						{
							stage.stopLoop();
						}
					}
				}
				return false;
			}
		};
		Events_KeyPress().add(m_keyExitEvent);
	}

	protected void onAddedToStage()
	{
		C2D_Debug.log(">>>-onAddedToStage----" + this.getName());
	}

	protected void onRemovedFromStage()
	{
		C2D_Debug.log(">>>-onRemovedFromStage----" + this.getName());
		doRelease();
	}

	protected void onHidden()
	{
		C2D_Debug.log(">>>-onHidden----" + this.getName());
	}

	protected void onShown()
	{
		C2D_Debug.log(">>>-onShown----" + this.getName());
	}

	protected void onSentTop()
	{
		C2D_Debug.log(">>>-onSentTop----" + this.getName());
	}

	protected void onSentBack()
	{
		C2D_Debug.log(">>>-onSentBack----" + this.getName());
	}
	
}
