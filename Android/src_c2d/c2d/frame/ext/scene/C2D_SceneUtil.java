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
	/** 按键退出事件 */
	private C2D_Event_KeyPress m_keyExitEvent;
	/**
	 * 过渡场景
	 */
	private static C2D_TransScene m_TransScene;

	/**
	 * 设置过渡场景
	 * 
	 * @param transScene
	 */
	public static void setTransScene(C2D_TransScene transScene)
	{
		m_TransScene = transScene;
	}

	/**
	 * 获取当前的过渡场景
	 * 
	 * @return 过渡场景
	 */
	public static C2D_TransScene getTransScene()
	{
		return m_TransScene;
	}

	/**
	 * 设置返回页面（返回键触发）
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
	 * 为某个按钮控件绑定指向的下个页面
	 * 
	 * @param wiget
	 *            控件
	 * @param nextScene
	 *            下个页面
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
	 * 切换到指定场景，会将当前场景弹出
	 * 
	 * @param nextScene
	 *            下一个场景
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
	 * 过渡到指定场景，会将当前场景弹出
	 * 
	 * @param nextScene
	 *            下一个场景
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
	 * 打开指定指定场景
	 * 
	 * @param nextScene
	 *            下一个场景
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
	 * 设置返回按键
	 * 
	 * @param backKey
	 *            按键
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
