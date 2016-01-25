package c2d.frame.base;

import bvr.path.C2D_BvrNode;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_EventPool_Scene;
import c2d.frame.util.C2D_SceneTracker;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Graphics;

/**
 * --onAddedToStage()当场景被添加到舞台时会发生。
 * 这个方法里面主要用来加载资源。要注意的时候，不要重复加载资源，因为这个方法可能会被多次调用，多次加载和移除。
 * --onRemovedFromStage()当场景从舞台移除时会发生。
 * 
 * @author AndrewFan
 */
public abstract class C2D_Scene extends C2D_ViewUtil implements C2D_BvrNode
{
	/** 场景状态 --添加到舞台 -- */
	public static final int SCENE_AddToStage = 0;
	/** 场景状态 --从舞台移除 -- */
	public static final int SCENE_RemovedFromStage = 1;
	/** 场景状态 --移动至前台 -- */
	public static final int SCENE_SentTop = 2;
	/** 场景状态 --移动至后台 -- */
	public static final int SCENE_SentBack = 3;
	/** 场景状态 --自身被显示 -- */
	public static final int SCENE_Shown = 4;
	/** 场景状态 --自身被隐藏 -- */
	public static final int SCENE_Hidden = 5;
	/** 场景状态 --完成第一次绘画 -- */
	public static final int SCENE_Painted = 6;
	/** 事件池-场景显示或隐藏 */
	private C2D_EventPool_Scene m_Events_Scene;
	/** 当前焦点控件 */
	private C2D_Widget m_focusedNow;
	/** 下一个焦点控件 */
	private C2D_Widget m_focusedNext;
	/** 临时计算顶点 */
	private C2D_PointF fwPosNew = new C2D_PointF();
	private C2D_PointF fwPosNow = new C2D_PointF();
	private C2D_PointF fwPosTry = new C2D_PointF();
	/**
	 * 需要不断刷新
	 */
	public boolean m_alwaysRepaint = false;
	/** 方向优先的搜索方式 */
	public static int SearchFocus_DIR = 0;
	/** 综合距离的搜索方式 */
	public static int SearchFocus_DIC = 1;
	/** 视觉感官的搜索方式 */
	public static int SearchFocus_LOOK = 2;
	/** ZOrder排序的搜索方式 */
	public static int SearchFocus_ZORDER = 3;
	/** 在移动光标时的查找类型 */
	protected int m_searchType = SearchFocus_LOOK;
	/**
	 * 过渡场景
	 */
	protected C2D_TransScene m_transScene;
	/** 当前场景的加载进度 */
	protected int m_process;
	/** 是否正处于过渡场景 */
	protected boolean m_inLoading;
	/** 父容器 */
	protected C2D_SceneStack m_pStack;
	private int m_painted = -1;

	/**
	 * 构造场景
	 */
	public C2D_Scene()
	{
		setName(this.toString());
		C2D_SceneTracker.TrackScene(this);
	}

	/**
	 * 设置当前场景所在的舞台
	 * 
	 * @param stageAt
	 *            所在的舞台
	 */
	void setStage(C2D_Stage stageAt)
	{
		m_atStage = stageAt;
		if (m_atStage != null)
		{
			m_pStack = m_atStage.m_sceneStack;
		}
	}

	/**
	 * 计算当前场景所在的舞台对象， 当场景被加入舞台之后，才会存在此对象
	 * 
	 * @return 所在的舞台对象
	 */
	public C2D_Stage accountStage()
	{
		return m_atStage;
	}

	/**
	 * 计算所在的场景对象
	 * 
	 * @return 所在的场景对象
	 */
	public C2D_Scene accountScene()
	{
		return this;
	}

	/**
	 * 检查是否处于加载中
	 * 
	 * @return
	 */
	protected boolean onLoadingUpdate()
	{
		// 检查加载完成
		if (m_transScene != null && !m_inLoading)
		{
			m_transScene.c2d_removeFromStage();
			m_transScene = null;
		}
		// 更新加载逻辑
		if (m_transScene != null)
		{
			if (m_transScene != null)
			{
				m_transScene.onUpdate(m_transScene.m_atStage);
			}
			// 场景的自动逻辑，由于场景有可能被切换过，所以重新获得
			if (m_transScene != null)
			{
				m_transScene.onAutoUpdate();
			}
			return true;
		}
		return false;
	}

	/**
	 * 返回当前拥有焦点的控件，每个场景只能拥有一个或者没有焦点控件
	 * 
	 * @return 前拥有焦点的控件
	 */
	public C2D_Widget getFoucusedWidget()
	{
		if (m_focusedNow != null && m_focusedNow.m_focused)
		{
			return m_focusedNow;
		}
		return null;
	}

	/**
	 * 将指定的控件设置为当前拥有焦点的控件，每个场景只能拥有一个或者没有焦点控件。 如果给一个不能拥有焦点的控件设置焦点，不会有任何改变。
	 * 
	 * @param widget
	 *            前拥有焦点的控件
	 */
	public void setFocusedWidget(C2D_Widget widget)
	{
		if (widget == null)
		{
			return;
		}
		if (!contains(widget))
		{
			return;
		}
		if (!widget.getFocusable())
		{
			return;
		}
		// 处理光标转换
		if (m_focusedNow != null)
		{
			m_focusedNow.m_focused = false;
		}
		C2D_Widget orgFox = m_focusedNow;
		m_focusedNow = widget;
		if (m_focusedNow != null)
		{
			m_focusedNow.m_focused = true;
		}
		// 处理事件
		if (orgFox != null)
		{
			orgFox.lostFocus(m_focusedNow);
		}
		if (m_focusedNow != null)
		{
			m_focusedNow.gotFocus(orgFox);
		}
	}

	/** 如果当前焦点处于围困视图，则使用围困寻找 */
	private C2D_View m_besiegeView;

	/**
	 * 尝试向指定控件的指定方向移动焦点，注意搜索方式
	 * 在指定的视图内寻找，位于指定控件的某个方向上的可以拥有焦点并且非隐藏的控件的集合 <br>
	 * 尝试向指定控件的指定方向移动焦点，注意隐藏的控件不能获得焦点<br>
	 * 方向优先的搜索方式：<br>
	 * 向上寻找时，可以选中上方所有范围内<Y，Y最接近的控件 向下寻找时，可以选中下方所有范围内>Y，Y最接近的控件
	 * 向左寻找时，可以选中左方所有范围内<X，X最接近的控件 向右寻找时，可以选中右方所有范围内>X，X最接近的控件 <br>
	 * 综合距离的搜索方式：<br>
	 * 向上寻找时，可以选中上方所有范围内<Y，X距离+Y距离接近的控件 向下寻找时，可以选中下方所有范围内>Y，X距离+Y距离接近的控件
	 * 向左寻找时，可以选中左方所有范围内<X，X距离+Y距离接近的控件 向右寻找时，可以选中右方所有范围内>X，X距离+Y距离接近的控件<br>
	 * 视觉感官的搜索方式：（即以指向方向的距离作为优先正量，垂直指向方向的距离作为负量的指标来衡量）<br>
	 * 向上寻找时，可以选中上方所有范围内<Y，Y距离+X方向偏差*2相对小的控件
	 * 向下寻找时，可以选中下方所有范围内>Y，Y距离+X方向偏差*2相对小的控件
	 * 向左寻找时，可以选中左方所有范围内<X，X距离+Y方向偏差*2相对小的控件
	 * 向右寻找时，可以选中右方所有范围内>X，X距离+Y方向偏差*2相对小的控件<br>
	 * @param widget
	 *            指定的控件
	 * @param direction
	 *            指定控件的指定方向，0,1,2,3分别对应上右下左
	 * @return 是否改变过光标
	 */
	public boolean moveFocus(C2D_Widget widget, int direction)
	{
		if (widget == null)
		{
			return false;
		}
		// 确定指定控件的左上角
		fwPosNow.setValue(widget.getLeftTop());
		// 尝试寻找指定方向上是否含有可以拥有焦点的控件
		m_focusedNext = null;
		m_besiegeView = null;
		if (m_focusedNow != null)
		{
			C2D_View vNow = m_focusedNow.getParentNode();
			if (vNow != null && vNow.getBesiege())
			{
				m_besiegeView = vNow;
			}
		}
		searchFoxWidget(this, widget, direction);
		m_besiegeView = null;
		if (m_focusedNext != null)
		{
			setFocusedWidget(m_focusedNext);
			m_focusedNext = null;
			return true;
		}
		return false;
	}

	/**
	 * 在指定的视图内寻找焦点
	 * 
	 * @param view
	 *            寻找范围
	 * @param widget
	 *            指定控件
	 * @param direction
	 *            指定方向
	 */
	private void searchFoxWidget(C2D_View view, C2D_Widget widget, int direction)
	{
		if (view == null || widget == null || !view.getVisible())
		{
			return;
		}
		int size = view.m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget child = (C2D_Widget) view.m_nodeList.elementAt(i);
			if (m_besiegeView == null || m_besiegeView.equals(view))
			{
				if (!widget.equals(child) && child.getFocusable() && child.getVisible())
				{
					fwPosTry.setValue(child.getLeftTop());
					boolean pass = false;
					if (m_searchType == SearchFocus_DIR)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y > fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) < C2D_Math
											.abs(fwPosNew.m_x - fwPosNow.m_x)));
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x
									&& fwPosTry.m_y >= fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math
											.abs(fwPosNew.m_y - fwPosNow.m_y)));
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) < C2D_Math
											.abs(fwPosNew.m_x - fwPosNow.m_x)));
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x
									&& fwPosTry.m_y >= fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math
											.abs(fwPosNew.m_y - fwPosNow.m_y)));
							break;
						}
					}
					else if (m_searchType == SearchFocus_DIC)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x && (m_focusedNext == null || tryDicSearch());
							break;
						}
					}
					else if (m_searchType == SearchFocus_LOOK)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y && (m_focusedNext == null || tryLookSearch(0, -1));
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x && (m_focusedNext == null || tryLookSearch(1, 0));
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y && (m_focusedNext == null || tryLookSearch(0, 1));
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x && (m_focusedNext == null || tryLookSearch(-1, 0));
							break;
						}
					}
					else if (m_searchType == SearchFocus_ZORDER)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = child.m_zOrder > m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder < m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_left:
							pass = child.m_zOrder < m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder > m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_right:
							pass = child.m_zOrder > m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder < m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_down:
							pass = child.m_zOrder < m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder > m_focusedNext.m_zOrder);
							break;

						}
					}
					if (pass)
					{
						m_focusedNext = child;
						fwPosNew.setValue(fwPosTry);
					}
				}
			}
			if (child instanceof C2D_View)
			{
				searchFoxWidget((C2D_View) child, widget, direction);
			}
		}
	}

	private boolean tryDicSearch()
	{
		return C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) + C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math.abs(fwPosNew.m_x - fwPosNow.m_x)
				+ C2D_Math.abs(fwPosNew.m_y - fwPosNow.m_y);
	}

	private boolean tryLookSearch(int dirX, int dirY)
	{
		int weightNew = getLookWeight(dirX,dirY,fwPosNew,fwPosNow);
		int weightTry = getLookWeight(dirX,dirY,fwPosTry,fwPosNow);
		return weightTry<weightNew;
	}
	
	private int getLookWeight(int dirX, int dirY,C2D_PointF pos1,C2D_PointF pos2)
	{
		float weightDir = C2D_Math.abs(dirX * C2D_Math.abs(pos1.m_x - pos2.m_x)) + C2D_Math.abs(dirY * C2D_Math.abs(pos1.m_y - pos2.m_y));
		float weightNeg = C2D_Math.abs(dirX * C2D_Math.abs(pos1.m_y - pos2.m_y)) + C2D_Math.abs(dirY * C2D_Math.abs(pos1.m_x - pos2.m_x));
		return (int)(weightDir+weightNeg*2);
	}

	/**
	 * 获得显示事件池
	 * 
	 * @return 显示事件池
	 */
	public C2D_EventPool_Scene Events_Scene()
	{
		if (m_Events_Scene == null)
		{
			m_Events_Scene = new C2D_EventPool_Scene(this);
		}
		return m_Events_Scene;
	}

	public void setSearchType(int searchType)
	{
		if (searchType < SearchFocus_DIC || searchType > SearchFocus_ZORDER)
		{
			return;
		}
		this.m_searchType = searchType;
	}

	/**
	 * 清空所有的事件，在资源释放时可能需要调用
	 */
	public void clearEvents()
	{
		super.clearEvents();
		Events_Scene().clear();
	}

	/**
	 * 清除所有子节点
	 */
	public void removeAllChild()
	{
		super.removeAllChild();
		m_focusedNow = null;
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.doRelease();
			m_Events_Scene = null;
		}
		m_atStage = null;
		m_focusedNow = null;
	}

	/**
	 * 关闭，注意，只有处于当前最顶端的场景才允许被关闭
	 * 
	 * @return 是否关闭成功
	 */
	public boolean close()
	{
		C2D_Stage stage = m_atStage;
		if (stage == null)
		{
			return false;
		}
		C2D_Scene top = stage.currentScene();
		if (!this.equals(top))
		{
			return false;
		}
		stage.popScene();
		stage.releaseKeys();
		return true;
	}

	/**
	 * 重置加载进度
	 */
	public void ressetProcess()
	{
		m_process = 0;
	}

	/**
	 * 设置加载进度
	 * 
	 * @param value
	 */
	public void setProcess(int value)
	{
		int p = m_process;
		m_process = value;
		m_process = C2D_Math.limitNumber(m_process, 0, 100);
		C2D_Debug.log("--->>load process:"+value);
		if (p != m_process && m_transScene != null)
		{
			m_transScene.onProcessChanged(m_process);
		}
	}

	/**
	 * 增加加载进度
	 * 
	 * @param value
	 */
	public void incProcess(int value)
	{
		int p = m_process;
		m_process += value;
		m_process = C2D_Math.limitNumber(m_process, 0, 100);
		if (p != m_process && m_transScene != null)
		{
			m_transScene.onProcessChanged(m_process);
		}
	}

	/**
	 * 获得加载进度
	 * 
	 * @return
	 */
	public int getProcess()
	{
		return m_process;
	}

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
	 */
	protected void onPaint(C2D_Graphics g)
	{
		if (m_inLoading)
		{
			if (m_transScene != null)
			{
				m_transScene.onPaint(g);
			}
			return;
		}
		super.onPaint(g);
		if (m_painted < 0)
		{
			m_painted = 0;
		}
	}

	protected void readyLoading()
	{
		m_inLoading = true;
		m_process = 0;
	}

	public void endLoading()
	{
		m_inLoading = false;
	}

	/**
	 * 当完成将目标场景加入当前舞台，针对目标场景进行一些初始化
	 * 
	 * @param stage
	 *            目标场景
	 */
	protected void afterPushStage(C2D_Stage stage)
	{
		if (m_width <= 0 || m_height <= 0)
		{
			setSize(C2D_Stage.User_Size);
		}
		setStage(stage);
		c2d_addTostage();
		c2d_sentTop();
		layoutChanged();
		onAutoUpdate();
	}

	/**
	 * 注册过渡场景
	 * 
	 * @param transScene
	 *            过渡场景
	 */
	protected void registerLoder(C2D_TransScene transScene)
	{
		m_transScene = transScene;
	}
	/**
	 * 是否是顶层场景
	 * @return 是否是顶层场景
	 */
	public boolean isTop()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null)
		{
			return false;
		}
		return this.equals(stage.currentScene());
	}
	/**
	 * 自动更新
	 */
	protected final void onAutoUpdate()
	{
		super.onAutoUpdate();
	}

	/**
	 * 放入场景
	 */
	protected final void c2d_addTostage()
	{
		onAddedToStage();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_AddToStage);
		}
	}

	/**
	 * 从场景移除
	 */
	protected final void c2d_removeFromStage()
	{
		onRemovedFromStage();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_RemovedFromStage);
		}
	}

	/**
	 * 移到舞台后端
	 */
	protected final void c2d_sentBack()
	{
		onSentBack();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_SentBack);
		}
	}

	/**
	 * 移到舞台前端
	 */
	protected final void c2d_sentTop()
	{
		onSentTop();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_SentTop);
		}
	}

	/**
	 * 隐藏
	 */
	protected final void c2d_hide()
	{
		onHidden();
		// 处理自身的隐藏事件
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_Hidden);
		}
		m_painted = -1;
	}

	/**
	 * 显示
	 */
	protected final void c2d_show()
	{
		onShown();
		// 处理自身的显示事件
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_Shown);
		}
		layoutChanged();
	}

	/**
	 * 检查被重绘
	 */
	protected void c2d_afterPaint()
	{
		if (m_painted == 0)
		{
			// 处理自身的重绘完成事件
			if (m_Events_Scene != null)
			{
				m_Events_Scene.onCalled(SCENE_Painted);
			}
			m_painted = 1;
		}
	}
	/**
	 * 当场景被添加到舞台
	 */
	protected abstract void onAddedToStage();

	/**
	 * 当场景被从舞台中移除
	 */
	protected abstract void onRemovedFromStage();

	/**
	 * 当场景被移动至幕后时调用，即被其它场景覆盖时调用
	 */
	protected abstract void onSentBack();

	/**
	 * 当场景被移动至前台时调用，这个操作会在onAddToStage之后被 调用，也会在其加载到场景后，当其上部覆盖场景移除时调用。
	 */
	protected abstract void onSentTop();

	/**
	 * 当在舞台上显示，onShow并不是跟随onAddToStage一起出现， 而是体现其真实性，即当场景真正在舞台上出现之后才会被调用，所以
	 * 当连续进行场景的压栈或者出栈时不会引起此方法的连锁调用，而只有 当场景的压栈出栈操作完成之后的自动更新逻辑中，才会执行真正被显
	 * 示的场景的onShown()。
	 */
	protected abstract void onShown();

	/**
	 * 当在舞台上隐藏，onHide并不是跟随onAddToStage一起出现， 而是体现其真实性，即当场景真正在舞台上出现之后才会被调用，所以
	 * 当连续进行场景的压栈或者出栈时不会引起此方法的连锁调用，而只有 当场景的压栈出栈操作完成之后的自动更新逻辑中，才会执行真正被隐
	 * 藏的场景的onHidden()。
	 */
	protected abstract void onHidden();
	
	/**
	 * 设置一直刷新
	 * 
	 * @param alwaysPaint
	 *            是否一直刷新
	 */
	public final void setAlwaysPaint(boolean alwaysPaint)
	{
		m_alwaysRepaint = alwaysPaint;
	}

	public boolean beSkipped()
	{
		return false;
	}

	public String getBvrNodeName()
	{
		return "unnamed";
	}
}
