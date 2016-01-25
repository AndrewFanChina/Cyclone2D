package c2d.frame.base;

import c2d.frame.event.C2D_EventPool_SysLoop;
import c2d.frame.event.C2D_Event_Stage;
import c2d.frame.ext.scene.C2D_ErrorScene;
import c2d.frame.util.C2D_WidgetUtil;
import c2d.lang.app.C2D_Canvas;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Graphics;

/**
 * 舞台类 是C2D_Camnvas的子类。是一个画布。一般来说，一个游戏只需要一个舞台。 从C2D_APP进入应用程序，系统要求返回一个可以使用的舞台。
 * 
 * 舞台有以下事件： ---onEnter(); 舞台使用栈结构来进行管理场景，在进入舞台的第一步，应该建立一个场景，并且将场景压栈。
 * 每个舞台有两个栈，一个是场景栈，一个是对话框栈。先只考虑场景栈的概念。 场景栈用来实现多层级菜单结构，只有栈顶的场景单元才可以被执行逻辑和显示出来。
 * 舞台可以使用pushXXX的方法压入场景，使用popXXX的方法来弹出场景。弹出的场景并不意味着它的资源被释放掉， 因为你还可能在未来某个时刻再次压入它。
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Stage extends C2D_Canvas
{
	public C2D_Stage()
	{
	}

	/** 场景栈 **/
	protected C2D_SceneStack m_sceneStack;
	/**
	 * 对话框栈
	 */
	protected C2D_DialogStack m_dialogStack;
	/**
	 * 需要刷新整个界若干次
	 */
	protected int m_needRepaintTime = 0;
	/** 上一个顶层场景 */
	private C2D_Scene m_lastTop;
	/** 错误页面 */
	private C2D_ErrorScene m_errorScene;
	/** 是否发生了错误 */
	private boolean m_errOccured = false;

	/**
	 * 更新场景节点
	 */
	protected final void onUpdate_C2D()
	{
		try
		{
			doUpdate();
		}
		catch (Exception e)
		{
			String infor = "运行时发生异常:\n";
			String exp = "未知异常";
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
	 * 执行更新
	 */
	private void doUpdate()
	{
		// 系统循环
		if (m_Events_SysLoop != null)
		{
			m_Events_SysLoop.onCalled();
		}
		// 用户逻辑更新
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
		// 检测场景变化
		observeScene();
		// 自动逻辑更新
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
		// 刷屏次数更新
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
	 * 停止循环线程，随后会调用onClose_C2D()方法。一般不需要调用这个方法， C2D_App.ShutDown()也到完成游戏循环的终止
	 */
	public void stopLoop()
	{
		super.stopLoop();
	}

	// 检测场景变化
	private void observeScene()
	{
		// 检查新场景的显示
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
		// 检查场景的绘制
		if (current != null)
		{
			current.c2d_afterPaint();
		}
	}

	/**
	 * 绘制场景节点
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
	/** 此函数内部使用 */
	protected final void onPause_C2D()
	{
	}
	/** 此函数内部使用 */
	protected final void onResume_C2D()
	{
	}

	/**
	 * 进行一次全局刷新
	 */
	public final void repaintOnce()
	{
		m_needRepaintTime = 1;
	}

	/**
	 * 进行time次屏幕刷新，在屏幕非同步绘图情况下可能需要用到这个函数。
	 * 
	 * @param time
	 *            刷新次数
	 */
	public final void repaintByTime(int time)
	{
		m_needRepaintTime = time;
	}

	/**
	 * 进入应用
	 */
	protected void onEnter_C2D()
	{
		C2D_WidgetUtil.Init();
	}

	/**
	 * 返回场景栈当前大小
	 * 
	 * @return 场景栈当前大小
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
	 * 清除所有事件，释放资源的时候可能需要调用
	 */
	public void clearEvents()
	{
	}

	/**
	 * 卸载资源
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
	 * 加载指定的场景destScene，以场景transScene作为过渡
	 * 
	 * @param destScene
	 *            目标场景
	 * @param transScene
	 *            过渡场景
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
	 * 获取当前场景
	 * 
	 * @return 当前场景
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
	 * 获取当前对话框
	 * 
	 * @return 当前对话框
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
	 * 加载指定的场景
	 * 
	 * @param nextScene
	 *            要加载的场景
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
	 * 查看当前是否没有场景
	 * 
	 * @return 是否没有场景
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
	 * 加载指定的对话框
	 * 
	 * @param nextDialog
	 *            要加载的对话框
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
	 * 转入错误场景
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

	/** 舞台事件 */
	private C2D_Event_Stage m_stageEvent;

	/**
	 * 设置线程循环状态
	 */
	public void setLoopState(int state)
	{
		super.setLoopState(state);
		if (m_stageEvent != null)
		{
			m_stageEvent.doEvent(this, m_loopState);
		}
	}

	/** 注册舞台事件 */
	public void setStageEvent(C2D_Event_Stage event)
	{
		m_stageEvent = event;
	}

	/** 事件池-系统循环 */
	private C2D_EventPool_SysLoop m_Events_SysLoop;

	/**
	 * 获得系统循环事件池
	 * 
	 * @return 系统循环
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
	 * 处理场景的隐藏和显示，系统函数
	 */
	protected void onWindowVisibilityChanged(int visibility)
	{
		super.onWindowVisibilityChanged(visibility);
	}

}
