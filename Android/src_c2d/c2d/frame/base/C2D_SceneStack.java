package c2d.frame.base;

import c2d.lang.math.C2D_Stack;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_SceneStack extends C2D_Object
{
	/** 当前舞台 */
	protected C2D_Stage m_stage;
	/** 栈列表*/
	C2D_Stack m_stack;
	/**
	 * 构造函数
	 * 
	 * @param stage
	 *            当前舞台
	 */
	public C2D_SceneStack(C2D_Stage stage)
	{
		m_stage = stage;
	}
	/**
	 * 将当前的顶部场景切换到指定的场景，相当于先移除掉当前的顶层场景，
	 * 但不执行次级场景的onSentTop方法，而立刻加载指定的场景。参见
	 * pushScene和popScene方法，本方法相当于一次popScene和
	 * pushScene的叠加效果，并且中间去除了次级场景的onSentTop
	 * 和onSentBack方法。
	 * 
	 * @param destScene
	 *            目标场景
	 * @param transScene
	 *            过渡场景
	 */
	public void switchScene(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if (destScene == null)
		{
			return;
		}
		if(m_stack==null)
		{
			m_stack=new C2D_Stack();
		}
		// 移除顶部的场景
		C2D_Scene current = currentScene();
		if (current != null)
		{
			m_stack.pop();
			current.c2d_removeFromStage();
			current.setStage(null);
		}
		doPush(destScene, transScene);

	}
	/**
	 * 执行目标场景的压栈和加载
	 * @param destScene 目标场景
	 * @param transScene 过渡场景
	 */
	private void doPush(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if(m_stage==null)
		{
			return;
		}
		if(m_stack==null)
		{
			m_stack=new C2D_Stack();
		}
		// 记录过渡场景(实际上没做压栈操作)
		if (transScene != null)
		{
			destScene.registerLoder(transScene);
			transScene.onLoadBegin();
			transScene.afterPushStage(m_stage);
		}
		// 压入新场景
		m_stack.push(destScene);
		//开始加载
		if (transScene == null)
		{
			destScene.afterPushStage(m_stage);
			m_stage.repaintOnce();
		}
		else
		{
			destScene.readyLoading();
			new Thread(new Runnable()
			{
				public void run()
				{
					destScene.afterPushStage(m_stage);
					m_stage.repaintOnce();
					destScene.endLoading();
					transScene.onLoadEnd();
				}
			}).start();
		}
	}
	/**
	 * 将指定的目标场景压入栈，相当于会将场景搬到舞台。
	 * 加载时会指定给场景与舞台相同的尺寸。如果目前已经存在场景，会先执行已经存
	 * 在场景的onSentBack方法，后执行目标场景的onSentTop方法，接着执行
	 * 目标场景的onAddToStage方法，最后，本轮循环结束，如果本轮循环确定某
	 * 些场景发生了变化，则先执行被隐藏的场景的onHidden方法，再执行当前顶部
	 * 场景的onShown方法。
	 * @param scene
	 *            场景对象
	 */
	public void pushScene(C2D_Scene scene)
	{
		pushScene(scene,null);
	}

	/**
	 * 将指定的目标场景压入栈，相当于会将场景搬到舞台。
	 * 加载时会指定给场景与舞台相同的尺寸。如果目前已经存在场景，会先执行已经存
	 * 在场景的onSentBack方法，后执行目标场景的onSentTop方法，接着执行
	 * 目标场景的onAddToStage方法，最后，本轮循环结束，如果本轮循环确定某
	 * 些场景发生了变化，则先执行被隐藏的场景的onHidden方法，再执行当前顶部
	 * 场景的onShown方法。
	 * 如果通过过渡场景执行加载，过渡场景在目标场景加载完成之后自动会被移除显示。
	 * 
	 * @param destScene
	 *            目标场景
	 * @param transScene
	 *            过渡场景
	 */
	public void pushScene(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if (destScene == null)
		{
			return;
		}
		// 隐藏当前场景
		C2D_Scene current = currentScene();
		if(current!=null)
		{
			current.c2d_sentBack();
		}
		doPush(destScene, transScene);
	}

	/**
	 * 弹出当前场景
	 */
	public void popScene()
	{
		C2D_Scene scene = currentScene();
		if (scene == null||m_stack==null||m_stage==null)
		{
			return;
		}
		m_stack.pop();
		// 移除顶部的场景
		scene.c2d_removeFromStage();
		scene.setStage(null);
		// 显示被覆盖的场景
		scene = currentScene();
		if (scene!=null)
		{
			scene.c2d_sentTop();
			scene.layoutChanged();
		}
		m_stage.repaintOnce();
	}

	/**
	 * 获取当前场景
	 * 
	 * @return 场景对象
	 */
	public C2D_Scene currentScene()
	{
		if(m_stack==null)
		{
			return null;
		}
		return (C2D_Scene) m_stack.peek();
	}

	/**
	 * 绘制场景节点
	 */
	protected void onPaint_C2D(C2D_Graphics g)
	{
		C2D_Scene scene = currentScene();
		if (scene == null)
		{
			return;
		}
		scene.onPaint(g);
	}

	/**
	 * 进行更新，并返回是否要屏蔽后续更新逻辑
	 * 
	 * @return 是否需要屏蔽后续更新逻辑
	 */
	public boolean onUpdate()
	{
		C2D_Scene scene = currentScene();
		if (scene != null)
		{
			if (!scene.onLoadingUpdate())
			{
				scene.onUpdate(m_stage);
			}
		}
		return false;
	}
	/**
	 * 自动更新
	 */
	public void onAutoUpdate()
	{
		C2D_Scene current = currentScene();
		if (current != null)
		{
			current.onAutoUpdate();
		}
	}
	/**
	 * 执行释放
	 */
	public void onRelease()
	{
		popAll();
		m_stack=null;
	}

	public int getSize()
	{
		if(m_stack==null)
		{
			return 0;
		}
		return  m_stack.getSize();
	}
	/**
	 * 查看是否为空
	 * @return
	 */
	public boolean isEmpty()
	{
		if(m_stack==null)
		{
			return true;
		}
		return  m_stack.isEmpty();
	}
	/**
	 * 弹出所有场景
	 */
	public void popAll()
	{
		if(m_stack!=null)
		{
			int sc = m_stack.getSize();
			for (int i = 0; i < sc; i++)
			{
				popScene();
			}
			m_stack.clear();	
		}
	}


}
