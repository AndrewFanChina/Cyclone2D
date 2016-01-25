package c2d.frame.base;

import c2d.frame.event.C2D_EventPool_Button;
import c2d.frame.event.C2D_EventPool_ChangeFocus;
import c2d.frame.event.C2D_EventPool_KeyPress;
import c2d.frame.event.C2D_EventPool_MotionEnd;
import c2d.frame.event.C2D_EventPool_Touch;
import c2d.frame.event.C2D_EventPool_Update;
import c2d.frame.event.C2D_EventPool_WaitTime;
import c2d.frame.event.C2D_Event_RemovedFromView;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;
import c2d.lang.math.type.C2D_TouchData;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectI;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.mod.physic.C2D_Motion;
import c2d.mod.physic.C2D_PhysicBox;
import c2d.mod.physic.C2D_PhysicBoxCreator;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 每个场景内部使用树状结构进行管理。每个控件都是树状控件集合结构中的一个节点。 控件分两类。
 * 第一类是普通控件，它们存在于第二类控件容器（视图）中。每个控件均拥有坐标、锚点、Z排序值等基本属性。 它们的坐标均是相对坐标，相对于其父容器
 * ，因此，当父容器被移动时，所有的子树将跟随一起移动。在获取这些属性的时候， 需要使用函
 * 数getXXX，设置的时候需要使用setXXX，即使继承的类也需要这样调用，一定不能直接 改变属性值。Z排序
 * 值被用于兄弟节点之间的排序，此数值越小，控件越处于下层。 第二类控件是视图，它是特殊的控件（场景也是视图）。 视图是
 * 一个容器，它可以加入子节点(addChild)，可以移除节点(removeChild) ，获得节点(getChildAt)。
 * 场景节点(C2D_Scene)是控件树的根节点，所有的控件树都可以追溯到其存在的场景，只有舞台(C2D_Stage)的当
 * 前场景才可以显示。可以拥有下层背景色和中背景图片，上层可以添加各类普通控件，如果你不设置背景色和背景图片， 视图将 成为一个透明的容器。
 * 
 * @author AndrewFan
 */
public abstract class C2D_Widget extends C2D_Object implements C2D_Order, C2D_Consts
{
	/** 名称，用于标记控件 */
	protected String m_widgetName;
	/** 整数表示的X坐标.这个坐标将跟随m_xF发生变化 */
	public float m_x;
	/** 整数表示的Y坐标,这个坐标将跟随m_yF发生变化 */
	public float m_y;
	/**
	 * 整数表示的相对顶层视图的X坐标，这个坐标将跟随m_x发生变化 注意如果在其父类层中含有缓冲视图(BufferedView)的话，这个坐标
	 * 将变成相对于最接近自身的缓冲视图
	 */
	protected float m_xToTop;
	/**
	 * 整数表示的相对顶层视图的Y坐标，这个坐标将跟随m_y发生变化 注意如果在其父类层级中含有缓冲视图(BufferedView)的话，这个坐标
	 * 将变成相对于最接近自身的缓冲视图
	 */
	protected float m_yToTop;
	/** z排序 */
	protected int m_zOrder;
	/** 控件左上角点相对根节点坐标，注意不是锚点坐标 */
	protected C2D_PointF m_LT2Root = new C2D_PointF();
	/** 是否需要更新XY平面相对坐标 */
	protected boolean m_needUpdatePos = true;
	/** 是否需要重新获取左上角数据 */
	protected boolean m_needUpdateLT = true;
	/** 是否需要更新可见性 */
	protected boolean m_needUpdateVisible = false;
	/** 锚点坐标. */
	protected int m_anchor = 0;
	/** 是否可见，用于标识控件的可见状态 */
	protected boolean m_visible = true;
	/** 是否被父类控件所隐藏 */
	protected boolean m_hiddenByParent = false;
	/** 是否在摄像机镜头中，当需要用到卷轴地图，则需要使用到这个变量 */
	public boolean m_inCamera = true;
	/** 无论是否在屏幕范围内，始终执行更新逻辑 */
	public boolean m_alwaysUpdate;
	/** 父节点 */
	protected C2D_View m_parentNode;
	/** 是否需要对子树进行Z排序 */
	protected boolean m_needOrder = true;
	/**
	 * 处理焦点，当处理焦点时，按键信息将只会被主动发送到当前焦点组件 ，进行处理，但是用户可以在Widget的更新事件中提前截获按键并消除
	 */
	protected boolean m_focusable = false;
	/** 当前控件是否拥有焦点 **/
	protected boolean m_focused = false;
	/** 焦点图片，当控件成为焦点时，将显示次图片 */
	protected C2D_ImageClip m_focusImgClip;
	/** 焦点图片的位置坐标，相对于当前控件坐标 */
	protected float m_focusX, m_focusY;
	/** 当前控件的热区，热区是相对于当前控件的坐标的一块区域 */
	protected C2D_RectF m_hotRegion;
	/** 是否激活热区 */
	protected boolean m_useHotRegion = false;
	/** 事件池-移动完毕 */
	private C2D_EventPool_MotionEnd m_Events_MotionEnd;
	/** 事件池-获得焦点 */
	protected C2D_EventPool_ChangeFocus m_Events_Focus;
	/** 所有移动对象集合，这些移动都是同时进行 */
	private C2D_Array m_motions;
	/** 用户自定义标记. */
	protected int m_iFlag = -1;
	/** 用户自定义标记. */
	protected String m_strFlag;
	/** 物理绑定盒 */
	public C2D_PhysicBox m_physicBox;
	/** 事件池-等待时间 */
	private C2D_EventPool_WaitTime m_Events_WaitTime;
	/** 事件池-更新 */
	private C2D_EventPool_Update m_Events_Update;
	/** 按钮事件池 */
	protected C2D_EventPool_Button m_Events_Button;
	/** 按键事件池 */
	protected C2D_EventPool_KeyPress m_Events_KeyPress;
	/** 触摸事件池 */
	protected C2D_EventPool_Touch m_Events_Touch;
	/** 当从父容器移除时所响应的事件 */
	protected C2D_Event_RemovedFromView m_removedEvt;
	/** 公用变量 */
	protected static C2D_PointF point_com1 = new C2D_PointF();
	protected static C2D_PointF point_com2 = new C2D_PointF();
	protected static C2D_RectI rect_com1 = new C2D_RectI();
	/** 所在的舞台 */
	protected C2D_Stage m_atStage;
	/** 所在的场景 */
	protected C2D_Scene m_atScene;

	/**
	 * 获取控件名称
	 * 
	 * @return 控件名称
	 */
	public String getName()
	{
		return m_widgetName;
	}

	/**
	 * 设置控件名称
	 * 
	 * @param widgetName
	 */
	public void setName(String widgetName)
	{
		this.m_widgetName = widgetName;
	}

	/**
	 * 返回zOrder作为排序权值
	 */
	public int getOrderValue(int orderType)
	{
		return m_zOrder;
	}

	/**
	 * 返回zOrder
	 */
	public int getZOrder()
	{
		return m_zOrder;
	}

	/**
	 * 获取自己位于父容器中的ID
	 * 
	 * @return ID
	 */
	public int getID()
	{
		if (m_parentNode != null)
		{
			return m_parentNode.m_nodeList.indexOf(this);
		}
		return -1;
	}

	/**
	 * 设置排序权值，越小代表越靠近屏幕内部，不会立刻重新排序，会在本轮循环的绘图之前获得正确的顺序。
	 * 如果需要立刻获得正确的顺序，需要手动调用其父容器的orderChildren()
	 * 
	 * @param zOrder
	 */
	public void setZOrder(int zOrder)
	{
		m_zOrder = zOrder;
		layoutChanged();
		if (m_parentNode != null)
		{
			m_parentNode.m_needOrder = true;
		}
	}

	/**
	 * 获取X坐标.
	 * 
	 * @return float X坐标
	 */
	public float getX()
	{
		return m_x;
	}

	/**
	 * 获取相对顶层节点的绝对X坐标.
	 * 
	 * @return float X坐标
	 */
	public float getXToTop()
	{
		return m_xToTop;
	}

	/**
	 * 设置X坐标.
	 * 
	 * @param posX
	 *            新X坐标
	 * @return 返回是否发生改变
	 */
	public boolean setXTo(float posX)
	{
		if (m_x == posX)
		{
			return false;
		}
		// 更新绝对坐标
		m_x = posX;
		layoutChanged();
		return true;
	}

	/**
	 * 获取Y坐标.
	 * 
	 * @return float Y坐标
	 */
	public float getY()
	{
		return m_y;
	}

	/**
	 * 获取相对顶层节点的绝对Y坐标.
	 * 
	 * @return float Y坐标
	 */
	public float getYToTop()
	{
		return m_yToTop;
	}

	/**
	 * 获取控件的绝对坐标，注意如果在其父类层中含有缓冲视图(BufferedView)的话，这个坐标 将变成相对于最接近自身的缓冲视图
	 * 
	 * @param value
	 *            存放返回结果的顶点对象
	 * @return 是否正常获得
	 */
	public boolean getPosToTop(C2D_PointF value)
	{
		if (value == null)
		{
			return false;
		}
		value.setValue(m_xToTop, m_yToTop);
		return true;
	}

	/**
	 * 设置Y坐标.
	 * 
	 * @param posY
	 *            新X坐标
	 * @return 是否成功设置
	 */
	public boolean setYTo(float posY)
	{
		m_y = posY;
		layoutChanged();
		return true;
	}

	/**
	 * 设置控件到指定位置.
	 * 
	 * @param x
	 *            float X坐标
	 * @param y
	 *            float Y坐标
	 * @return 是否成功设置
	 */
	public boolean setPosTo(float x, float y)
	{
		m_x = x;
		m_y = y;
		layoutChanged();
		return true;
	}

	/**
	 * 设置控件到指定位置.使用相对父容器的百分比坐标
	 * 
	 * @param x
	 *            float X百分比坐标
	 * @param y
	 *            float Y百分比坐标
	 */
	public boolean setPosPer(float x, float y)
	{
		if (m_parentNode == null)
		{
			return false;
		}
		return setPosTo(m_parentNode.m_width * x / 100, m_parentNode.m_height * y / 100);
	}

	/**
	 * 设置控件到指定位置.
	 * 
	 * @param pos
	 *            坐标
	 */
	public boolean setPosTo(C2D_PointF pos)
	{
		if (pos == null)
		{
			return false;
		}
		m_x = pos.m_x;
		m_y = pos.m_y;
		layoutChanged();
		return true;
	}

	/**
	 * 设置X坐标在当前基础上进行偏移.
	 * 
	 * @param offset
	 *            偏移坐标
	 */
	public void setXBy(float offset)
	{
		// 更新绝对坐标
		m_x += offset;
		layoutChanged();
	}

	/**
	 * 设置Y坐标在当前基础上进行偏移.
	 * 
	 * @param offset
	 *            偏移坐标
	 */
	public void setYBy(float offset)
	{
		// 更新绝对坐标
		m_y += offset;
		layoutChanged();
	}

	/**
	 * 设置X、Y坐标在当前基础上进行偏移.
	 * 
	 * @param offsetX
	 *            X方向偏移坐标
	 * @param offsetY
	 *            Y方向偏移坐标
	 */
	public void setPosBy(float offsetX, float offsetY)
	{
		// 更新绝对坐标
		m_x += offsetX;
		m_y += offsetY;
		layoutChanged();
	}

	/**
	 * 刷新坐标，这将把精确坐标的值传递给整数坐标， 并计算相对顶层视图的坐标和更新显示和占用区域。
	 * 一般来说，这个函数默认会在组件相关信息发生变化时自动被调用，无需手动调用。
	 */
	public void refreshPos()
	{
		if (m_parentNode != null)
		{
			m_parentNode.getPosByChild(point_com1);
			m_xToTop = point_com1.m_x + m_x;
			m_yToTop = point_com1.m_y + m_y;
		}
		else
		{
			m_xToTop = m_x;
			m_yToTop = m_y;
		}
	}

	/**
	 * 设置锚点坐标.
	 * 
	 * @param anchor
	 *            新锚点坐标
	 */
	public void setAnchor(int anchor)
	{
		m_anchor = anchor;
		layoutChanged();
	}

	/**
	 * 获取锚点坐标
	 * 
	 * @return 锚点坐标
	 */
	public int getAnchor()
	{
		return m_anchor;
	}

	/**
	 * 返回父节点
	 * 
	 * @return 父节点
	 */
	public C2D_View getParentNode()
	{
		return m_parentNode;
	}

	/**
	 * 当改变布局，需要重新更新坐标，左上角坐标等。
	 */
	public void layoutChanged()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
		getStageAt();
		if (m_atStage != null)
		{
			m_atStage.m_needRepaint = true;
		}
		noticeParent();
	}

	/**
	 * 告知父类改变
	 */
	protected void noticeParent()
	{
		if (m_parentNode != null)
		{
			m_parentNode.noticedByChild();
		}
	}

	/**
	 * 设置需要重置位置
	 */
	protected void setUpdatePos()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
	}

	protected static C2D_RectI rectR1 = new C2D_RectI();

	/**
	 * 获取所在的舞台对象
	 * 
	 * @return 所在的舞台对象
	 */
	public C2D_Stage getStageAt()
	{
		if (m_atStage == null)
		{
			m_atStage = accountStage();
		}
		return m_atStage;
	}

	/**
	 * 计算所在的舞台对象
	 * 
	 * @return 所在的舞台对象
	 */
	public C2D_Stage accountStage()
	{
		C2D_Widget topElement = this;
		while (topElement.m_parentNode != null)
		{
			topElement = topElement.m_parentNode;
		}
		if (!(topElement instanceof C2D_Scene))
		{
			return null;
		}
		m_atStage = topElement.accountStage();
		return m_atStage;
	}

	/**
	 * 返回所在的场景对象
	 * 
	 * @return 所在的场景对象
	 */
	public C2D_Scene getSceneAt()
	{
		if (m_atScene == null)
		{
			m_atScene = accountScene();
		}
		return m_atScene;
	}

	/**
	 * 计算所在的场景对象
	 * 
	 * @return 所在的场景对象
	 */
	public C2D_Scene accountScene()
	{
		C2D_Widget topElement = this;
		while (topElement.m_parentNode != null)
		{
			topElement = topElement.m_parentNode;
		}
		if (!(topElement instanceof C2D_Scene))
		{
			return null;
		}
		m_atScene = topElement.accountScene();
		return m_atScene;
	}

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
	 */
	protected abstract void onPaint(C2D_Graphics g);

	protected static C2D_Array array_Paint = new C2D_Array();

	/**
	 * 绘制焦点，使用自身的可视区域
	 * 
	 * @param g
	 *            画笔
	 * @param layW
	 *            控件占据的宽度
	 * @param layH
	 *            控件占据的高度
	 */
	protected void paintFocus(C2D_Graphics g, float layW, float layH)
	{
		// 绘制焦点
		if (m_focused && m_focusImgClip != null)
		{
			C2D_PointF pointF = C2D_GdiGraphics.applyAnchor(m_xToTop, m_yToTop, layW, layH, m_anchor);
			m_focusImgClip.draw(pointF.m_x + m_focusX, pointF.m_y + m_focusY);
		}
	}

	/**
	 * 绘制焦点，使用自身的可视区域的双区域限定
	 * 
	 * @param g
	 *            画笔
	 * @param layW
	 *            控件占据的宽度
	 * @param layH
	 *            控件占据的高度
	 * @param rects
	 *            可视区域集合
	 */
	protected void paintFocus(C2D_Graphics g, int layW, int layH, C2D_Array rects)
	{
		boolean paintFox = (m_focused && m_focusImgClip != null && g.applyClip(m_xToTop, m_yToTop, layW, layH, m_anchor, null, point_com2));
		if (paintFox)
		{
			array_Paint.removeAllElements();
			array_Paint.addElement(null);
			m_focusImgClip.draw(point_com2.m_x + m_focusX, point_com2.m_y + m_focusY, 0);
			array_Paint.removeAllElements();
		}
	}

	private C2D_Array m_motionsBuf = new C2D_Array();

	/**
	 * 遍历所有子节点，执行更新
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		updateSelf(stage);
	}

	/**
	 * 自由行动，进行物理运动计算
	 */
	public void autoMotion()
	{
		if (m_physicBox != null)
		{
			m_physicBox.autoMotion();
		}
	}

	/**
	 * 更新自己
	 * 
	 * @param stage
	 */
	protected void updateSelf(C2D_Stage stage)
	{
		// 处理移动
		if (m_motions != null)
		{
			m_motionsBuf.clear();
			// 标记事件
			int numMotion = m_motions.size();
			for (int i = 0; i < numMotion; i++)
			{
				m_motionsBuf.addElement(m_motions.m_datas[i]);
			}
			// 开始执行
			int numMotionBuf = m_motions.size();
			for (int i = 0; i < numMotionBuf; i++)
			{
				C2D_Motion m_currentMotion = (C2D_Motion) m_motionsBuf.m_datas[i];
				if (m_currentMotion != null && !m_currentMotion.isOver())
				{
					if (m_currentMotion.doMotion(stage.getTimePassed()))
					{
						// 移动完毕事件
						if (m_Events_MotionEnd != null)
						{
							m_Events_MotionEnd.onCalled(m_currentMotion);
						}
						// 移除当前移动
						if (m_motions != null)
						{
							m_motions.remove(m_currentMotion);
						}
						m_currentMotion.doRelease();
					}
				}
			}
		}
		// 处理等待
		if (m_Events_WaitTime != null)
		{
			m_Events_WaitTime.onCalled(stage.getTimePassed());
		}
		// 处理更新
		if (m_Events_Update != null)
		{
			m_Events_Update.onCalled();
		}
		// 处理按键响应
		processKeyCalls(stage);
		// 处理导航事件响应（方向键）
		processNavigations(stage);
		// 处理按钮响应
		processBtnCalls(stage);
	}

	/**
	 * 处理按键响应。 此方法将被系统调用，不应该在外部手动调用。
	 * 
	 * @param stage
	 */
	protected void processKeyCalls(C2D_Stage stage)
	{
		int keyCode = stage.getSingleKey();
		if (keyCode >= C2D_Device.key_up && keyCode < C2D_Device.key_other)
		{
			if (m_Events_KeyPress != null)
			{
				m_Events_KeyPress.onCalled(keyCode);
			}
		}
	}

	/**
	 * 响应在控件上操作上右下左按键，实现焦点的移动。 此方法将被系统调用，不应该在外部手动调用。
	 * 
	 * @param stage
	 *            所在场景
	 */
	protected void processNavigations(C2D_Stage stage)
	{
		if (m_atScene == null || !m_focused || !m_visible)
		{
			return;
		}
		// 处理方向键
		int keyCode = stage.getSingleKey();
		if (!anyKeyUsed(keyCode))
		{
			if (keyCode >= C2D_Device.key_up && keyCode <= C2D_Device.key_left)
			{
				if (m_atScene.moveFocus(this, keyCode))
				{
					stage.releaseKeys();
				}
			}
		}
	}

	/**
	 * 处理组件的按钮响应。 此方法将被系统调用，不应该在外部手动调用。
	 * 
	 * @param stage
	 *            所在场景
	 */
	protected void processBtnCalls(C2D_Stage stage)
	{
		if (!m_visible||m_Events_Button == null)
		{
			return;
		}
		m_Events_Button.processBtnCall(stage);
	}

	/**
	 * 是否某个按键已经被使用，如果已经被具体控件使用的，则优先被具体控件响应
	 * 
	 * @param key
	 *            按键
	 * @return 是否被使用
	 */
	public boolean anyKeyUsed(int key)
	{
		return false;
	}

	/** 当前控件的热区相应计算临时变量 */
	private static C2D_RectF CountHotRegion = new C2D_RectF();

	/**
	 * 检测是否热区拥有触点
	 * 
	 * @return 返回触点
	 */
	public boolean hasTouchPoint()
	{
		return getTouchPoint()!=null;
	}
	private static C2D_PointF M_cp=new C2D_PointF();
	/**
	 * 检测是否热区拥有触点，如果拥有则返回触点
	 * 
	 * @return 返回触点
	 */
	public C2D_PointF getTouchPoint()
	{
		C2D_Stage stage = getStageAt();
		if (m_hotRegion == null || stage == null || stage.m_curTouchData == null)
		{
			return null;
		}
		// 进行热区计算
		C2D_TouchData td = stage.m_curTouchData;
		if (td.m_touchCount == 0)
		{
			return null;
		}
		CountHotRegion.setValue(m_hotRegion);
		C2D_PointF plt = getLeftTop();
		CountHotRegion.m_x = plt.m_x;
		CountHotRegion.m_y = plt.m_y;
		for (int i = 0; i < td.m_touchStates.length; i++)
		{
			if (td.m_touchStates[i])
			{
				if (td.m_touchPoints[i].inRegion(CountHotRegion))
				{
					M_cp.setValue(td.m_touchPoints[i]);
					return M_cp;
				}
			}
		}
		return null;
	}
	/**
	 * 检测是否与制定的触点位置交叉
	 * 
	 * @return 是否与制定的触点位置交叉
	 */
	public boolean crossWithTouchPoint(C2D_PointF point)
	{
		C2D_Stage stage = getStageAt();
		if (m_hotRegion == null || stage == null ||point == null)
		{
			return false;
		}
		// 进行热区计算
		CountHotRegion.setValue(m_hotRegion);
		C2D_PointF plt = getLeftTop();
		CountHotRegion.m_x = plt.m_x;
		CountHotRegion.m_y = plt.m_y;
		return (point.inRegion(CountHotRegion));
	}
	/**
	 * 是否可见.
	 * 
	 * @return boolean 是否可见
	 */
	public boolean getVisible()
	{
		return m_visible;
	}

	/**
	 * 设置是否可见.
	 * 
	 * @param visibleNew
	 *            是否可见
	 */
	public void setVisible(boolean visibleNew)
	{
		if (m_visible != visibleNew)
		{
			m_visible = visibleNew;
			layoutChanged();
		}
	}

	/**
	 * 设置是否位于当前镜头中，只有镜头中的对象才可以被显示
	 * 
	 * @param inCamera
	 *            是否位于当前镜头中
	 */
	public void setInCamera(boolean inCamera)
	{
		m_inCamera = inCamera;
	}

	/**
	 * 查看是否位于当前镜头中，只有镜头中的对象才可以被显示
	 * 
	 * @return 是否位于当前镜头中
	 */
	public boolean isInCamera()
	{
		return m_inCamera;
	}

	protected static C2D_RectF rectFTemp = new C2D_RectF();

	/**
	 * 获取控件占据区域的左上角的世界坐标，<br>
	 * 对于无区域概念的控件来说，是其绝对坐标，对于视图来说，是其左上角绝对坐标 。<br>
	 * 注意如果在其父类层级中含有缓冲视图(BufferedView)的话，<br>
	 * 这个坐标将变成相对于最接近自身的缓冲视图。
	 * 
	 * @return 左上角点的世界坐标
	 */
	protected C2D_PointF getLeftTop()
	{
		if (m_needUpdateLT)
		{
			rectFTemp.m_x = m_xToTop;
			rectFTemp.m_y = m_yToTop;
			rectFTemp.m_width = getWidth();
			rectFTemp.m_height = getHeight();
			C2D_PointF newPos = C2D_Graphics.applyAnchor(rectFTemp, m_anchor);
			m_LT2Root.m_x = newPos.m_x;
			m_LT2Root.m_y = newPos.m_y;
			m_needUpdateLT = false;
		}
		return m_LT2Root;
	}

	protected abstract float getWidth();

	protected abstract float getHeight();

	/**
	 * 是否没有被隐藏，这将检查所有父节点， 确保自己没有被任意一个父节点所隐藏
	 * 
	 * @return boolean 是否允许显示
	 */
	public boolean allowedShow()
	{
		return m_visible && !m_hiddenByParent;
	}

	/**
	 * 更新可见性
	 */
	public void refreshVisible()
	{
		C2D_Widget p = m_parentNode;
		while (p != null)
		{
			if (!p.m_visible)
			{
				m_hiddenByParent = true;
				break;
			}
			p = p.m_parentNode;
		}
	}

	/**
	 * 根据指定的时间，执行匀速移动指定的位移(全部距离)，移动完毕触发“移动结束事件”
	 * 
	 * @param offX
	 *            全部X偏移
	 * @param offY
	 *            全部Y偏移
	 * @param time
	 *            花费的时间，毫秒为单位
	 * @param a
	 *            加速度
	 */
	public void moveBy(float offX, float offY, int time, float a)
	{
		addMotion(new C2D_Motion(this, offX, offY, a, time));
	}

	/**
	 * 添加动作,不会允许重复添加
	 * 
	 * @param motion
	 *            动作
	 */
	public void addMotion(C2D_Motion motion)
	{
		if (motion == null)
		{
			return;
		}
		if (m_motions == null)
		{
			m_motions = new C2D_Array();
		}
		if (!m_motions.contains(motion))
		{
			m_motions.addElement(motion);
		}
	}

	/**
	 * 移除所有的动作
	 */
	public void clearMotions()
	{
		if (m_motions != null)
		{
			int size = m_motions.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Object oI = (C2D_Object) m_motions.elementAt(i);
				if (oI != null)
				{
					oI.doRelease();
				}
			}
			m_motions.removeAllElements();
			m_motions = null;
		}
	}

	/**
	 * 根据指定的时间，执行匀速移动指定的位移(全部距离)，移动完毕触发“移动结束事件”
	 * 
	 * @param offX
	 *            X偏移
	 * @param offY
	 *            Y偏移
	 * @param time
	 *            花费的时间，毫秒为单位
	 */
	public void moveBy(float offX, float offY, int time)
	{
		moveBy(offX, offY, time, 0);
	}

	/**
	 * 根据指定的时间，匀速移动到的目标位置，移动完毕触发“移动结束事件”
	 * 
	 * @param destX
	 *            目标点X坐标
	 * @param destY
	 *            目标点Y坐标
	 * @param time
	 *            花费的时间，毫秒为单位
	 */
	public void moveTo(float destX, float destY, int time)
	{
		float offX = destX - m_x;
		float offY = destY - m_y;
		moveBy(offX, offY, time, 0);
	}

	/**
	 * 根据指定的时间，匀速移动到的目标位置，移动完毕触发“移动结束事件”
	 * 
	 * @param destX
	 *            目标点X坐标
	 * @param destY
	 *            目标点Y坐标
	 * @param time
	 *            花费的时间，毫秒为单位
	 * @param a
	 *            加速度
	 */
	public void moveTo(float destX, float destY, int time, float a)
	{
		float offX = destX - m_x;
		float offY = destY - m_y;
		moveBy(offX, offY, time, a);
	}

	/**
	 * 自动更新
	 */
	protected void onAutoUpdate()
	{
		// 刷新子树XY平面坐标
		if (m_needUpdatePos)
		{
			refreshPos();
			m_needUpdatePos = false;
		}
		// 刷新可见性
		if (m_needUpdateVisible)
		{
			refreshVisible();
			m_hiddenByParent = false;
		}
		refreshHotRegion(false);
	}

	/**
	 * 获取是否处理焦点，当处理焦点时，按键信息将只会被主动发送到当前焦点组件 ，进行处理，但是用户可以在Widget的更新事件中提前截获按键并消除
	 * 
	 * @return 是否可以拥有焦点
	 */
	public boolean getFocusable()
	{
		return m_focusable;
	}

	/**
	 * 检测是否拥有焦点
	 * 
	 * @return 是否拥有焦点
	 */
	public boolean isFocused()
	{
		return m_focused;
	}

	/**
	 * 设置是否可以拥有焦点，当处理焦点时，按键信息将只会被主动发送到当前焦点组件 ，进行处理，但是用户可以在Widget的更新事件中提前截获按键并消除
	 * 
	 * @param focusable
	 *            是否可以拥有焦点
	 */
	public void setFocusable(boolean focusable)
	{
		this.m_focusable = focusable;
		this.setUse_HotRegion(true);
	}

	/**
	 * 设置焦点图片以及焦点图片的相对位置，当传入存在的焦点切片对象，会自动让控件可以拥有焦点， 传入空的焦点切片对象，会自动让控件不能拥有焦点
	 * 
	 * @param imgClip
	 *            焦点切片
	 * @param x
	 *            焦点切片的X坐标
	 * @param y
	 *            焦点切片的Y坐标
	 */
	public void setFocusImage(C2D_ImageClip imgClip, float x, float y)
	{
		if (m_focusImgClip != null)
		{
			m_focusImgClip.doRelease(this);
			m_focusImgClip = null;
		}
		m_focusImgClip = imgClip;
		// m_focusable = m_focusImgClip != null;
		m_focusX = x;
		m_focusY = y;
		layoutChanged();
	}

	/**
	 * 让当前控件拥有焦点，这个函数将自动将控件设置成可以拥有焦点。 将当前场景的焦点控件变成自身。你不应该手动调用这个函数。
	 * 
	 * @param another
	 *            原焦点
	 */
	protected void gotFocus(C2D_Widget another)
	{
		C2D_Scene scene = getSceneAt();
		if (scene == null)
		{
			C2D_Debug.log("【error】in gotFocus()-->widget is not in scene tree!");
			return;
		}
		if(m_Events_Button!=null)
		{
			m_Events_Button.onFocused(true, another);
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.onCalled(true, another);
		}
		if (m_focusImgClip != null)// TODO 可能会引起更新迟滞
		{
			layoutChanged();
		}
	}
	/**
	 * 失去焦点，这个函数将自动将控件设置成可以拥有焦点。 你不应该手动调用这个函数，那样的话，当前页面将失去拥有焦点的控件
	 * 
	 * @param another
	 *            目标焦点
	 */
	protected void lostFocus(C2D_Widget another)
	{
		C2D_Stage stage = getStageAt();
		C2D_Scene scene = getSceneAt();
		if (stage == null || scene == null)
		{
			C2D_Debug.log("【error】in lostFocus()-->widget is not in scene tree!");
			return;
		}
		if(m_Events_Button!=null)
		{
			m_Events_Button.onFocused(false, another);
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.onCalled(false, another);
		}
		if (m_focusImgClip != null)// TODO 可能会引起更新迟滞
		{
			layoutChanged();
		}
	}

	/**
	 * 此函数将会将位置设置到父容器的中心，并且采用水平和垂直居中对齐的锚点。
	 */
	public void setToParentCenter()
	{
		if (m_parentNode != null)
		{
			setPosTo(m_parentNode.getWidth() / 2, m_parentNode.getHeight() / 2);
			setAnchor(C2D_Consts.HVCENTER);
		}
	}

	/**
	 * 设置热区位置，如果之前没有热区，将会创建热区
	 * 
	 * @param _x
	 *            相对自身坐标的热区左上角X坐标
	 * @param _y
	 *            相对自身坐标的热区左上角Y做标牌
	 * @param _w
	 *            热区宽度
	 * @param _h
	 *            热区高度
	 */
	public void setHotRegion(float _x, float _y, float _w, float _h)
	{
		if (m_hotRegion == null)
		{
			m_hotRegion = new C2D_RectF();
		}
		m_hotRegion.setValue(_x, _y, _w, _h);
	}

	/**
	 * 设置热区位置，如果之前没有热区，将会创建热区
	 * 
	 * @param region
	 *            被设置的热区数据
	 */
	public void setHotRegion(C2D_RectF region)
	{
		if (m_hotRegion == null)
		{
			m_hotRegion = new C2D_RectF();
		}
		m_hotRegion.setValue(region);
	}

	/**
	 * 刷新热区，如果热区启用的话。
	 * 
	 * @param alwaysUpdate
	 *            即便是已经有热区，也立刻刷新
	 */
	public void refreshHotRegion(boolean alwaysUpdate)
	{
		if (m_useHotRegion)
		{
			if (m_hotRegion == null || alwaysUpdate)
			{
				setHotRegion(getXToTop(), getYToTop(), getWidth(), getHeight());
			}
		}
	}

	/**
	 * 获得移动结束事件池(TODO 当多个motion完毕再添加motion时，不清空会出现问题。需要修改)
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_MotionEnd Events_MotionEnd()
	{
		if (m_Events_MotionEnd == null)
		{
			m_Events_MotionEnd = new C2D_EventPool_MotionEnd(this);
			m_Events_MotionEnd.transHadler(this);
			;
		}
		return m_Events_MotionEnd;
	}

	/**
	 * 改变焦点事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_ChangeFocus Events_ChangeFocus()
	{
		if (m_Events_Focus == null)
		{
			m_Events_Focus = new C2D_EventPool_ChangeFocus(this);
		}
		return m_Events_Focus;
	}

	/**
	 * 获得更新事件池
	 * 
	 * @return 更新事件池
	 */
	public C2D_EventPool_Update Events_Update()
	{
		if (m_Events_Update == null)
		{
			m_Events_Update = new C2D_EventPool_Update(this);
		}
		return m_Events_Update;
	}

	/**
	 * 获得等待结束事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_WaitTime Events_WaitEnd()
	{
		if (m_Events_WaitTime == null)
		{
			m_Events_WaitTime = new C2D_EventPool_WaitTime(this);
		}
		return m_Events_WaitTime;
	}

	/**
	 * 获得按钮事件池
	 * 
	 * @return 显示事件池
	 */
	public C2D_EventPool_Button Events_Button()
	{
		if (m_Events_Button == null)
		{
			m_Events_Button = new C2D_EventPool_Button(this);
		}
		return m_Events_Button;
	}

	/**
	 * 获得按键事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_KeyPress Events_KeyPress()
	{
		if (m_Events_KeyPress == null)
		{
			m_Events_KeyPress = new C2D_EventPool_KeyPress(this);
		}
		return m_Events_KeyPress;
	}
	
	/**
	 * 获得触屏事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_Touch Events_Touch()
	{
		if (m_Events_Touch == null)
		{
			m_Events_Touch = new C2D_EventPool_Touch(this);
		}
		return m_Events_Touch;
	}

	/**
	 * 清空所有的事件，在资源释放时可能需要调用
	 */
	public void clearEvents()
	{
		Events_Button().clear();
		Events_ChangeFocus().clear();
		Events_MotionEnd().clear();
		Events_Update().clear();
		Events_WaitEnd().clear();
		Events_KeyPress().clear();
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		// m_widgetName = null;
		m_parentNode = null;
		if (m_focusImgClip != null)
		{
			m_focusImgClip.doRelease();
			m_focusImgClip = null;
		}
		m_atStage = null;
		m_atScene = null;
		if (m_Events_MotionEnd != null)
		{
			m_Events_MotionEnd.doRelease();
			m_Events_MotionEnd = null;
		}
		if (m_Events_Focus != null)
		{
			m_Events_Focus.doRelease();
			m_Events_Focus = null;
		}
		clearMotions();
		if (m_Events_WaitTime != null)
		{
			m_Events_WaitTime.doRelease();
			m_Events_WaitTime = null;
		}
		if (m_Events_Update != null)
		{
			m_Events_Update.doRelease();
			m_Events_Update = null;
		}
		if (m_Events_Button != null)
		{
			m_Events_Button.doRelease();
			m_Events_Button = null;
		}
		if (m_Events_KeyPress != null)
		{
			m_Events_KeyPress.doRelease();
			m_Events_KeyPress = null;
		}
		if (m_physicBox != null)
		{
			m_physicBox.doRelease(this);
			m_physicBox = null;
		}
	}

	/**
	 * 设置用户自定义标记
	 * 
	 * @param flag
	 *            用户自定义标记
	 */
	public C2D_Widget setFlag(int flag)
	{
		m_iFlag = flag;
		return this;
	}

	/**
	 * 设置用户自定义标记
	 * 
	 * @param flag
	 *            用户自定义标记
	 */
	public C2D_Widget setStrFlag(String flag)
	{
		m_strFlag = flag;
		return this;
	}

	/**
	 * 获取用户自定义标记
	 * 
	 * @return 用户自定义标记
	 */
	public int getFlag()
	{
		return m_iFlag;
	}

	/**
	 * 获取用户自定义标记
	 * 
	 * @return 用户自定义标记
	 */
	public String getStrFlag()
	{
		return m_strFlag;
	}

	/**
	 * 绑定物理碰撞盒
	 * 
	 * @param phyBox
	 */
	public void bindPhysicBox(C2D_PhysicBox phyBox)
	{
		if (m_physicBox != null)
		{
			m_physicBox.doRelease(this);
			m_physicBox = null;
		}
		m_physicBox = phyBox;
		if (m_physicBox != null)
		{
			m_physicBox.transHadler(this);
			;
			m_physicBox.setBoundWidget(this);
		}
	}

	/**
	 * 绑定物理碰撞盒
	 * 
	 * @param phyBox
	 */
	public void bindPhysicBox(C2D_PhysicBoxCreator pbc)
	{
		if (pbc != null)
		{
			bindPhysicBox(pbc.onCreatePhysicBox(this));
		}
	}

	/**
	 * 设置当从父容器移除时所响应的事件
	 * 
	 * @param event
	 *            事件
	 */
	public void setOnRemoveFromViewEvt(C2D_Event_RemovedFromView event)
	{
		m_removedEvt = event;
	}

	/**
	 * 绑定的碰撞矩形
	 * 
	 * @return
	 */
	public C2D_RectF getBoundingRect()
	{
		return null;
	}

	/**
	 * 设置是否激活热区
	 * 
	 * @param active
	 *            激活状态
	 */
	public void setUse_HotRegion(boolean active)
	{
		if (active != m_useHotRegion)
		{
			m_useHotRegion = active;
			if (!m_useHotRegion)
			{
				m_hotRegion = null;
			}
		}
	}

	/**
	 * 查看是否激活热区
	 * 
	 * @return 是否激活
	 */
	public boolean getUse_HotRegion()
	{
		return m_useHotRegion;
	}
}
