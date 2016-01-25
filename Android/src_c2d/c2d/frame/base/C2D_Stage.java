package c2d.frame.base;

import c2d.frame.event.C2D_EventPool_SysLoop;
import c2d.frame.event.C2D_Event_Stage;
import c2d.frame.ext.scene.C2D_ErrorScene;
import c2d.frame.util.C2D_WidgetUtil;
import c2d.lang.app.C2D_Canvas;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Graphics;

/**
 * ��̨�� ��C2D_Camnvas�����ࡣ��һ��������һ����˵��һ����Ϸֻ��Ҫһ����̨�� ��C2D_APP����Ӧ�ó���ϵͳҪ�󷵻�һ������ʹ�õ���̨��
 * 
 * ��̨�������¼��� ---onEnter(); ��̨ʹ��ջ�ṹ�����й��������ڽ�����̨�ĵ�һ����Ӧ�ý���һ�����������ҽ�����ѹջ��
 * ÿ����̨������ջ��һ���ǳ���ջ��һ���ǶԻ���ջ����ֻ���ǳ���ջ�ĸ�� ����ջ����ʵ�ֶ�㼶�˵��ṹ��ֻ��ջ���ĳ�����Ԫ�ſ��Ա�ִ���߼�����ʾ������
 * ��̨����ʹ��pushXXX�ķ���ѹ�볡����ʹ��popXXX�ķ��������������������ĳ���������ζ��������Դ���ͷŵ��� ��Ϊ�㻹������δ��ĳ��ʱ���ٴ�ѹ������
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Stage extends C2D_Canvas
{
	public C2D_Stage()
	{
	}

	/** ����ջ **/
	protected C2D_SceneStack m_sceneStack;
	/**
	 * �Ի���ջ
	 */
	protected C2D_DialogStack m_dialogStack;
	/**
	 * ��Ҫˢ�����������ɴ�
	 */
	protected int m_needRepaintTime = 0;
	/** ��һ�����㳡�� */
	private C2D_Scene m_lastTop;
	/** ����ҳ�� */
	private C2D_ErrorScene m_errorScene;
	/** �Ƿ����˴��� */
	private boolean m_errOccured = false;

	/**
	 * ���³����ڵ�
	 */
	protected final void onUpdate_C2D()
	{
		try
		{
			doUpdate();
		}
		catch (Exception e)
		{
			String infor = "����ʱ�����쳣:\n";
			String exp = "δ֪�쳣";
			if (e != null)
			{
				e.printStackTrace();
				String msg = e.getMessage();
				if (msg != null)
				{
					exp = msg;
				}
				else
				{
					exp = e.toString();
				}
			}
			infor += exp;
			showErrorScene(infor);
		}
	}

	/**
	 * ִ�и���
	 */
	private void doUpdate()
	{
		// ϵͳѭ��
		if (m_Events_SysLoop != null)
		{
			m_Events_SysLoop.onCalled();
		}
		// �û��߼�����
		do
		{
			if (m_dialogStack != null && m_dialogStack.onUpdate())
			{
				break;
			}
			if (m_sceneStack != null && m_sceneStack.onUpdate())
			{
				break;
			}
		}
		while (false);
		// ��ⳡ���仯
		observeScene();
		// �Զ��߼�����
		if (m_dialogStack != null)
		{
			m_dialogStack.onAutoUpdate();
		}
		if (m_sceneStack != null)
		{
			m_sceneStack.onAutoUpdate();
		}
		C2D_Scene cs = currentScene();
		C2D_Dialog cd = currentDialog();
		// ˢ����������
		if (m_needRepaintTime > 0 || (cs != null && cs.m_alwaysRepaint) || (cd != null && cd.m_alwaysRepaint))
		{
			m_needRepaint = true;
			if (m_needRepaintTime > 0)
			{
				m_needRepaintTime--;
			}
		}
		if ((m_dialogStack ==null || m_dialogStack.isEmpty()) && (m_sceneStack ==null || m_sceneStack.isEmpty()))
		{
			stopLoop();
		}
	}

	/**
	 * ֹͣѭ���̣߳��������onClose_C2D()������һ�㲻��Ҫ������������� C2D_App.ShutDown()Ҳ�������Ϸѭ������ֹ
	 */
	public void stopLoop()
	{
		super.stopLoop();
	}

	// ��ⳡ���仯
	private void observeScene()
	{
		// ����³�������ʾ
		C2D_Scene current = currentScene();
		if ((current != null && !current.equals(m_lastTop)) || (current == null && m_lastTop != null))
		{
			if (m_lastTop != null)
			{
				m_lastTop.c2d_hide();
			}
			if (current != null)
			{
				current.c2d_show();
			}
			m_lastTop = current;
		}
		// ��鳡���Ļ���
		if (current != null)
		{
			current.c2d_afterPaint();
		}
	}

	/**
	 * ���Ƴ����ڵ�
	 */
	protected final void onPaint_C2D(C2D_Graphics g)
	{
		if (m_loopState == LOOP_NotStarted)
		{
			g.fillRect(0, 0, getWidth(), getHeight(), 0);
		}
		if (m_loopState == LOOP_Inited)
		{
			if(m_sceneStack!=null)
			{
				m_sceneStack.onPaint_C2D(g);	
			}
			if(m_dialogStack!=null)
			{
				m_dialogStack.onPaint_C2D(g);
			}
		}
	}
	/** �˺����ڲ�ʹ�� */
	protected final void onPause_C2D()
	{
	}
	/** �˺����ڲ�ʹ�� */
	protected final void onResume_C2D()
	{
	}

	/**
	 * ����һ��ȫ��ˢ��
	 */
	public final void repaintOnce()
	{
		m_needRepaintTime = 1;
	}

	/**
	 * ����time����Ļˢ�£�����Ļ��ͬ����ͼ����¿�����Ҫ�õ����������
	 * 
	 * @param time
	 *            ˢ�´���
	 */
	public final void repaintByTime(int time)
	{
		m_needRepaintTime = time;
	}

	/**
	 * ����Ӧ��
	 */
	protected void onEnter_C2D()
	{
		C2D_WidgetUtil.Init();
	}

	/**
	 * ���س���ջ��ǰ��С
	 * 
	 * @return ����ջ��ǰ��С
	 */
	public int getSceneCount()
	{
		if(m_sceneStack==null)
		{
			return 0;
		}
		return m_sceneStack.getSize();
	}

	/**
	 * ��������¼����ͷ���Դ��ʱ�������Ҫ����
	 */
	public void clearEvents()
	{
	}

	/**
	 * ж����Դ
	 */
	public boolean doRelease()
	{
		if (m_sceneStack != null)
		{
			m_sceneStack.doRelease();
			m_sceneStack=null;
		}
		if (m_dialogStack != null)
		{
			m_dialogStack.doRelease();
			m_dialogStack=null;
		}
		clearEvents();
		m_lastTop = null;
		if (m_errorScene != null)
		{
			m_errorScene.doRelease();
			m_errorScene=null;
		}
		return true;
	}

	public void popScene()
	{
		if (m_sceneStack != null)
		{
			m_sceneStack.popScene();
		}
	}

	public void popDialog()
	{
		if (m_dialogStack != null)
		{
			m_dialogStack.popScene();
		}
	}

	/**
	 * ����ָ���ĳ���destScene���Գ���transScene��Ϊ����
	 * 
	 * @param destScene
	 *            Ŀ�곡��
	 * @param transScene
	 *            ���ɳ���
	 */
	public void pushScene(C2D_Scene destScene, C2D_TransScene transScene)
	{
		if(m_sceneStack==null)
		{
			m_sceneStack = new C2D_SceneStack(this);
		}
		m_sceneStack.pushScene(destScene, transScene);
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return ��ǰ����
	 */
	public C2D_Scene currentScene()
	{
		if (m_sceneStack == null)
		{
			return null;
		}
		return m_sceneStack.currentScene();
	}

	/**
	 * ��ȡ��ǰ�Ի���
	 * 
	 * @return ��ǰ�Ի���
	 */
	public C2D_Dialog currentDialog()
	{
		if (m_dialogStack == null)
		{
			return null;
		}
		return (C2D_Dialog) m_dialogStack.currentScene();
	}

	/**
	 * ����ָ���ĳ���
	 * 
	 * @param nextScene
	 *            Ҫ���صĳ���
	 */
	public void pushScene(C2D_Scene nextScene)
	{
		if(m_sceneStack==null)
		{
			m_sceneStack = new C2D_SceneStack(this);
		}
		m_sceneStack.pushScene(nextScene);
	}

	/**
	 * �鿴��ǰ�Ƿ�û�г���
	 * 
	 * @return �Ƿ�û�г���
	 */
	public boolean noScene()
	{
		if(m_sceneStack==null)
		{
			return true;
		}
		return m_sceneStack.isEmpty();
	}
	
	/**
	 * ����ָ���ĶԻ���
	 * 
	 * @param nextDialog
	 *            Ҫ���صĶԻ���
	 */
	public void pushDialog(C2D_Dialog nextDialog)
	{
		if(m_dialogStack==null)
		{
			m_dialogStack = new C2D_DialogStack(this);
		}
		m_dialogStack.pushScene(nextDialog);
	}

	/**
	 * ת����󳡾�
	 * 
	 * @param s
	 */
	protected void showErrorScene(String s)
	{
		if(m_errorScene == null)
		{
			m_errorScene = new C2D_ErrorScene();
		}
		if (!m_errorScene.equals(currentScene()))
		{
			pushScene(m_errorScene);
		}
		m_errOccured = true;
		m_errorScene.showErrorInfor(s);
	}

	/** ��̨�¼� */
	private C2D_Event_Stage m_stageEvent;

	/**
	 * �����߳�ѭ��״̬
	 */
	public void setLoopState(int state)
	{
		super.setLoopState(state);
		if (m_stageEvent != null)
		{
			m_stageEvent.doEvent(this, m_loopState);
		}
	}

	/** ע����̨�¼� */
	public void setStageEvent(C2D_Event_Stage event)
	{
		m_stageEvent = event;
	}

	/** �¼���-ϵͳѭ�� */
	private C2D_EventPool_SysLoop m_Events_SysLoop;

	/**
	 * ���ϵͳѭ���¼���
	 * 
	 * @return ϵͳѭ��
	 */
	public C2D_EventPool_SysLoop Events_SysLoop()
	{
		if (m_Events_SysLoop == null)
		{
			m_Events_SysLoop = new C2D_EventPool_SysLoop(this);
		}
		return m_Events_SysLoop;
	}
	/**
	 * �����������غ���ʾ��ϵͳ����
	 */
	protected void onWindowVisibilityChanged(int visibility)
	{
		super.onWindowVisibilityChanged(visibility);
	}

}
