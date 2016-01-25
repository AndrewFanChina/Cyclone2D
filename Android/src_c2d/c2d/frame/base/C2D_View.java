package c2d.frame.base;

import c2d.frame.com.list.C2D_ListStyle;
import c2d.lang.math.C2D_SortableArray;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_SizeI;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 视图类，视图可以是某个设备（屏幕或者内存换冲图）上的一块不透明的区域。 它可以拥有自己的背景色，背景图片，以及视图内的多个子视图或者控件，绘图
 * 的步骤是，先绘制背景色，后绘制背景图片，再绘制子视图和子控件集，它们的 绘制顺序根据自身的z深度进行排序。
 * 视图的限制区域将会根据自身的坐标和尺寸自动计算更新。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_View extends C2D_Widget
{
	/** 子树列表 */
	public C2D_SortableArray m_nodeList = new C2D_SortableArray();
	/** 视图宽度. */
	protected float m_width;
	/** 视图高度. */
	protected float m_height;
	/** 背景色，如果不设置，将没有背景色 **/
	protected C2D_Color m_bgColor;
	/** 背景图片切片，如果不设置，将没有背景图片 **/
	protected C2D_ImageClip m_bgImgClip;
	/** 背景图片，如果不设置，将没有背景图片 **/
	private C2D_Image m_bgImg;
	/** 背景图片的坐标 */
	protected C2D_PointI m_bgImgPos = new C2D_PointI();
	/** 设置焦点图片在子节点的下方显示 */
	protected boolean m_focusImgBack = false;
	/** 背景图片的锚点 */
	protected int m_bgImgAnchor;
	/**
	 * 是否围住焦点，如果当前容器的某个节点拥有焦点，自动切换焦点时，无法离开当前视图
	 */
	protected boolean m_besiege;

	/**
	 * 默认使用局部重绘视图类型构造视图
	 */
	public C2D_View()
	{
	}

	/**
	 * 添加新的子节点，使用子节点现有的z排序，如果新添加的子节点是视图， 默认会将此视图的大小自动设置成当前视图的大小。
	 * 另外需要注意添加的顺序，尽量进行自顶至下的顺序添加，这样会获得正确的位置，空间，排序等信息。
	 * 
	 * @param child
	 *            子节点
	 * @return 返回是否插入成功
	 */
	public boolean addChild(C2D_Widget child)
	{
		if (child == null)
		{
			return false;
		}
		if (child instanceof C2D_View)
		{
			C2D_View c = (C2D_View) child;
			if (c.getWidth() == 0 || c.getHeight() == 0)
			{
				((C2D_View) (child)).setSize(m_width, m_height);
			}
		}
		m_nodeList.addElement(child);
		child.m_parentNode = this;
		m_needOrder = true;
		child.layoutChanged();
		child.accountScene();
		child.accountStage();
		return true;
	}

	/**
	 * 获取宽度
	 * 
	 * @return 控件宽度
	 */
	public float getWidth()
	{
		return m_width;
	}

	/**
	 * 获取控件的高度
	 * 
	 * @return 高度
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * 判断当前视图是否包含指定的控件，不包含自己
	 * 
	 * @param widget
	 *            指定的控件
	 * @return 是否包含
	 */
	public boolean contains(C2D_Widget widget)
	{
		if (widget == null)
		{
			return false;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget child = (C2D_Widget) m_nodeList.elementAt(i);
			if (widget.equals(child))
			{
				return true;
			}
			if (child instanceof C2D_View)
			{
				if (((C2D_View) child).contains(widget))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前场景的焦点控件是否处于本视图的子节点中（包括自己）
	 * 
	 * @return 是否包含当前光标控件
	 */
	public boolean containsFocus()
	{
		C2D_Scene scene = getSceneAt();
		if (scene == null)
		{
			return false;
		}
		C2D_Widget focus = scene.getFoucusedWidget();
		if (focus == null)
		{
			return false;
		}
		if (focus.equals(this) || contains(focus))
		{
			return true;
		}
		return false;
	}

	/**
	 * 设置大小，即宽度与高度
	 * 
	 * @param width
	 *            新设置的宽度
	 * @param height
	 *            新设置的高度度
	 * @return 返回自身
	 */
	public C2D_View setSize(float width, float height)
	{
		if (width > 0 && height > 0 && (width != m_width || height != m_height))
		{
			m_width = width;
			m_height = height;
			layoutChanged();
			setUpdatePos();
		}
		refreshHotRegion(true);
		return this;
	}

	/**
	 * 设置大小，即宽度与高度
	 * 
	 * @param size
	 *            新设置的尺寸
	 * @return 返回是否成功设置
	 */
	public C2D_View setSize(C2D_SizeI size)
	{
		if (size == null)
		{
			return null;
		}
		return setSize(size.m_width, size.m_height);
	}

	private static final C2D_Color readOnlyColor = new C2D_Color();

	/**
	 * 获取背景颜色，注意获取的对象是公共对象，对其修改不会对视图产生影响。 如果需要修改视图的背景色，应该使用setBGColor
	 * 
	 * @return 背景颜色
	 */
	public C2D_Color getBGColor()
	{
		if (m_bgColor != null)
		{
			readOnlyColor.setColor(m_bgColor);
			return readOnlyColor;
		}
		return null;
	}

	/**
	 * 设置背景颜色，可以传入null，表示没有背景颜色
	 * 
	 * @param bgColor
	 *            背景颜色
	 */
	public void setBGColor(C2D_Color bgColor)
	{
		if ((m_bgColor == bgColor) || (m_bgColor != null && m_bgColor.equals(bgColor)))
		{
			return;
		}
		m_bgColor = bgColor;
		layoutChanged();
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param bgColor
	 *            背景颜色
	 */
	public void setBGColor(int bgColor)
	{
		setBGColor(new C2D_Color(bgColor));
	}

	/**
	 * 获取背景图片
	 * 
	 * @return 背景图片
	 */
	public C2D_ImageClip getBGImage()
	{
		return m_bgImgClip;
	}

	/**
	 * 设置背景图片切片。可以传入null，代表没有背景图片。默认转移参数归属权
	 * 
	 * @param bgImageClip
	 *            背景图片切片
	 */
	public void setBGImage(C2D_ImageClip bgImageClip)
	{
		setBGImage(bgImageClip, true);
	}

	/**
	 * 设置背景图片切片。可以传入null，代表没有背景图片
	 * 
	 * @param bgImageClip
	 *            背景图片
	 * @param changeOwner
	 *            是否转移归bgImageClip属权
	 */
	public void setBGImage(C2D_ImageClip bgImageClip, boolean changeOwner)
	{
		releaseImageClip();
		releaseBgImage();
		if (bgImageClip != null)
		{
			m_bgImgClip = bgImageClip;
			if (changeOwner)
			{
				m_bgImgClip.transHadler(this);
			}
		}
	}

	/**
	 * 设置背景图片。可以传入null，代表没有背景图片
	 * 
	 * @param bgImage
	 *            背景图片
	 */
	public C2D_View setBGImage(C2D_Image bgImage)
	{
		releaseBgImage();
		releaseImageClip();
		if (bgImage != null)
		{
			m_bgImg = bgImage;
			m_bgImgClip = new C2D_ImageClip(m_bgImg);
			m_bgImgClip.transHadler(this);
		}
		layoutChanged();
		return this;
	}

	/**
	 * 设置背景图片。可以传入null，代表没有背景图片
	 * 
	 * @param imgName
	 *            背景图片名称，默认位于imgs_other目录
	 */
	public void setBGImage(String imgName)
	{
		setBGImage(C2D_Image.createImage(imgName));
	}

	/**
	 * 设置列表项背景图片。可以传入null，代表没有背景图片
	 * 
	 * @param bgClip
	 *            背景图片切片
	 * @param x
	 *            相对向前控件的x坐标
	 * @param y
	 *            相对向前控件的y坐标
	 * @param anchor
	 *            锚点
	 */
	public void setBGImage(C2D_ImageClip bgClip, int x, int y, int anchor)
	{
		if (!(m_bgImgClip != null && m_bgImgClip == bgClip))
		{
			releaseImageClip();
		}
		m_bgImgClip = bgClip;
		m_bgImgPos.setValue(x, y);
		m_bgImgAnchor = anchor;
		layoutChanged();
	}

	/**
	 * 设置列表项背景图片。可以传入null，代表没有背景图片
	 * 
	 * @param bgClip
	 *            背景图片切片
	 * @param x
	 *            相对向前控件的x坐标
	 * @param y
	 *            相对向前控件的y坐标
	 */
	public void setBGImage(C2D_ImageClip bgClip, int x, int y)
	{
		setBGImage(bgClip, x, y, 0);
	}

	/**
	 * 设置背景图片相对坐标
	 * 
	 * @param x
	 *            相对x做坐标
	 * @param y
	 *            相对Y做坐标
	 */
	public void setBGImagePos(int x, int y)
	{
		m_bgImgPos.setValue(x, y);
	}

	/**
	 * 设置跟背景图片相同的大小，如果背景图片存在的话
	 */
	public void setSizeOfBGImg()
	{
		if (m_bgImgClip != null)
		{
			this.setSize(m_bgImgClip.getContentW(), m_bgImgClip.getContentH());
			layoutChanged();
		}
	}

	/**
	 * 设置焦点图片在子节点的下方显示
	 * 
	 * @param back
	 *            是否在
	 */
	public void setFocusImgBack(boolean back)
	{
		if (m_focusImgBack != back)
		{
			m_focusImgBack = back;
			layoutChanged();
		}
	}

	/**
	 * 检查是否设置了焦点图片在子节点的下方显示
	 * 
	 * @return 是否设置
	 */
	public boolean getFocusImgBack()
	{
		return m_focusImgBack;
	}

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
	 */
	protected void onPaint(C2D_Graphics g)
	{
		if (g == null || !m_visible || m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		// 得到左上角坐标
		getLeftTop(point_com1);
		int pX = (int) point_com1.m_x;
		int pY = (int) point_com1.m_y;

		if (m_bgColor != null)
		{
			g.fillRect(pX, pY, m_width, m_height, m_bgColor.getColor(), 0);
		}
		if (m_bgImgClip != null)
		{
			m_bgImgClip.draw(pX + m_bgImgPos.m_x, pY + m_bgImgPos.m_y);
		}
		if (m_focusImgBack)
		{
			paintFocus(g, m_width, m_height);
		}
		paintSonList(g);
		if (!m_focusImgBack)
		{
			paintFocus(g, m_width, m_height);
		}

	}

	/**
	 * 绘制子节点
	 * 
	 * @param g
	 */
	protected void paintSonList(C2D_Graphics g)
	{
		paintSonList(g, m_nodeList);

	}

	/**
	 * 绘制指定容器中的子节点
	 * 
	 * @param g
	 * @param list
	 */
	protected void paintSonList(C2D_Graphics g, C2D_SortableArray list)
	{
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Widget w = ((C2D_Widget) list.m_datas[i]);
			w.onPaint(g);
		}
	}

	/**
	 * 获取相对布局矩形，即基于当前的坐标，尺寸，锚点，翻转等信息， 计算出相对于其父节点所占据的矩形区域。将信息存放在传入的
	 * 矩形参数，并返回是否成功取得。
	 * 
	 * @param resultRect
	 *            用于结果存放的矩形对象
	 * @return 是否成功取得
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * 获取控件的左上角的世界坐标，对于控件来说，是其绝对坐标，对于视图来说，是其左上角绝对坐标 。
	 * 注意如果在其父类层级中含有缓冲视图(BufferedView)的话，这个坐标将变成相对于最接近自身的缓冲视图
	 */
	public boolean getLeftTop(C2D_PointF value)
	{
		if (value != null)
		{
			C2D_PointF newPos = C2D_GdiGraphics.applyAnchor(m_xToTop, m_yToTop, m_width, m_height, m_anchor);
			value.setValue(newPos);
			return true;
		}
		return false;
	}

	/**
	 * 从子节点的角度获取当前控件的左上角坐标绝对坐标，对于控件来说， 是绝对坐标，对于视图来说，是其左上角绝对坐标
	 */
	public boolean getPosByChild(C2D_PointF value)
	{
		return getLeftTop(value);
	}

	private static final C2D_PointF point_com = new C2D_PointF();

	/**
	 * 从子节点的角度获取当前控件的占据区域，包括左上角绝对坐标和视图大小
	 * 
	 * @param value
	 *            用于返回值存储
	 * @return 是否成功获得
	 */
	public boolean getRectByChild(C2D_RectF value)
	{
		if (value == null)
		{
			return false;
		}
		getPosByChild(point_com);
		value.setValue(point_com.m_x, point_com.m_y, m_width, m_height);
		return true;
	}

	/**
	 * 更新绝对坐标，子单元也要跟着一起刷新
	 */
	public void refreshPos()
	{
		float xOld = m_xToTop;
		float yOld = m_yToTop;
		super.refreshPos();
		if (xOld != m_xToTop || yOld != m_yToTop)
		{
			m_needUpdateLT = true;
		}
	}

	/**
	 * 添加新的子节点，并为新节点指定z排序，将根据这个z排序值来进行节点间排序，z值越小，进入屏幕越深。
	 * 如果checkRepeat此项为真，则禁止重复插入作为子节点,注意：检测重复将使操作变得低效
	 * 另外需要注意添加的顺序，尽量进行自顶至下的顺序添加，这样会获得正确的位置，空间，排序等信息。
	 * 
	 * @param child
	 *            子节点
	 * @param zOrder
	 *            z排序值
	 * @param checkRepeat
	 *            是否检测重复
	 * @return 返回是否插入成功
	 */
	public boolean addChild(C2D_Widget child, int zOrder, boolean checkRepeat)
	{
		if (child == null)
		{
			return false;
		}
		child.setZOrder(zOrder);
		if (checkRepeat)
		{
			if (m_nodeList.contains(child))
			{
				return false;
			}
		}
		addChild(child);
		return true;
	}

	/**
	 * . 添加新的子节点，并为新节点指定z排序，将根据这个z排序值来进行节点间排序,z值越小，进入屏幕越深；
	 * 不检测重复.另外需要注意添加的顺序，尽量进行自顶至下的顺序添加，这样会获得正确的位置，空间，排序等信息。
	 * 
	 * @param child
	 *            子节点
	 * @param zOrder
	 *            z排序值
	 * @return 返回是否插入成功
	 */
	public boolean addChild(C2D_Widget child, int zOrder)
	{
		if (child == null)
		{
			return false;
		}
		child.setZOrder(zOrder);
		return addChild(child);
	}

	/**
	 * 获取指定下标的子节点
	 * 
	 * @param childID
	 *            子节点下标
	 * @return 子节点
	 */
	public C2D_Widget getChildAt(int childID)
	{
		if (childID < 0 || childID >= m_nodeList.m_length)
		{
			return null;
		}
		return (C2D_Widget) m_nodeList.m_datas[childID];
	}

	/**
	 * 获取指定的子节点
	 * 
	 * @param flag
	 *            子节点标志
	 * @return 子节点
	 */
	public C2D_Widget getChildByFlag(int flag)
	{
		if (m_nodeList == null)
		{
			return null;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (son != null && son.getFlag() == flag)
			{
				return son;
			}
		}
		return null;
	}

	/**
	 * 获取指定的子节点
	 * 
	 * @param flag
	 *            子节点标志
	 * @return 子节点
	 */
	public C2D_Widget getChildByFlag(String flag)
	{
		if (flag == null)
		{
			return null;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (flag.equals(son.getStrFlag()))
			{
				return son;
			}
		}
		return null;
	}

	/**
	 * 返回直接子节点的个数
	 * 
	 * @return 直接子节点的个数
	 */
	public int getChildCount()
	{
		return m_nodeList.m_length;
	}

	/**
	 * 查看当前视图是否为空的，即不含任何子节点
	 * 
	 * @return 是否为空
	 */
	public boolean isEmpty()
	{
		return m_nodeList.m_length == 0;
	}

	/**
	 * 删除指定的子节点
	 * 
	 * @param child
	 *            子节点
	 * @return 返回是否删除成功
	 */
	public boolean removeChild(C2D_Widget child)
	{
		if (child == null)
		{
			return false;
		}
		if (m_nodeList.remove(child))
		{
			layoutChanged();
			if (child.m_removedEvt != null)
			{
				child.m_removedEvt.onRemovedFromView(child);
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除指定下标的子节点
	 * 
	 * @param childID
	 *            子节点下标
	 * @return 返回是否删除成功
	 */
	public boolean removeChildAt(int childID)
	{
		C2D_Widget child = getChildAt(childID);
		if(child!=null)
		{
			if (m_nodeList.removeElementAt(childID))
			{
				layoutChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * 清除所有子节点
	 */
	public void removeAllChild()
	{
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			removeChildAt(0);
		}
	}

	/**
	 * 排序所有的子节点，如果已经排序，则不会再次操作
	 */
	public void orderChildren()
	{
		if (!m_needOrder)
		{
			return;
		}
		m_nodeList.quickSort2();
		m_needOrder = false;
		layoutChanged();
	}

	/**
	 * 自动更新整个树，检测需要调整的内容，并执行调整。 比如需要重新排序等。你不应该使用这个方法。
	 */
	protected void onAutoUpdate()
	{
		super.onAutoUpdate();
		// 刷新子树Z排序
		if (m_needOrder)
		{
			m_nodeList.quickSort2();
			m_needOrder = false;
		}
		// 更新子单元
		onAutoUpdateSonList();
		// 更新完毕处理
		onAutoUpdateOther();

	}

	/**
	 * 自动更新子树
	 */
	protected void onAutoUpdateSonList()
	{
		autoUpdateList(m_nodeList);
	}

	/**
	 * 自动更新指定的容器中的组件
	 */
	protected void autoUpdateList(C2D_SortableArray list)
	{
		C2D_Widget childI;
		// 更新子树
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			childI = (C2D_Widget) list.m_datas[i];
			if (childI != null)
			{
				childI.onAutoUpdate();
			}
		}
	}

	/**
	 * 更新完毕处理
	 */
	public void onAutoUpdateOther()
	{
	}

	/**
	 * 遍历所有子节点，执行更新
	 * 
	 * @param stage
	 *            当前场景
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		updateSelf(stage);
		updateList(stage, m_nodeList);
	}

	/**
	 * 更新指定的容器中的组件
	 */
	protected void updateList(C2D_Stage stage, C2D_SortableArray list)
	{
		// 处理子节点的更新事件
		if (list != null)
		{
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				if (list != null)
				{
					C2D_Widget wI = (C2D_Widget) list.m_datas[i];
					if (wI != null)
					{
						wI.onUpdate(stage);
					}
				}
				else
				{
					break;
				}
			}
		}
	}

	/**
	 * 设置需要重置位置，所有子树叶需要跟着重置位置。
	 */
	protected void setUpdatePos()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
		setUpdateSonListPos();
	}

	/**
	 * 通知子树列表更新
	 */
	protected void setUpdateSonListPos()
	{
		setUpdateListPos(m_nodeList);
	}

	/**
	 * 通知指定的容器范围内的对象需要更新位置
	 * 
	 * @param list
	 */
	protected void setUpdateListPos(C2D_SortableArray list)
	{
		if (list == null)
		{
			return;
		}
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Widget childI = (C2D_Widget) list.m_datas[i];
			if (childI != null)
			{
				childI.setUpdatePos();
			}
		}
	}

	/**
	 * 设置是否围住焦点，不让其自动离开当前视图
	 * 
	 * @param besiege
	 *            是否围住焦点
	 */
	public void setBesiege(boolean besiege)
	{
		m_besiege = besiege;
	}

	/**
	 * 检测当前视图是否围困焦点
	 * 
	 * @return 是否围困焦点
	 */
	public boolean getBesiege()
	{
		return m_besiege;
	}

	/**
	 * 卸载背景图片，如果拥有的话
	 */
	private void releaseBgImage()
	{
		if (m_bgImg != null)
		{
			m_bgImg.doRelease(this);
			m_bgImg = null;
		}
	}

	/**
	 * 卸载切片，如果拥有的话
	 */
	private void releaseImageClip()
	{
		if (m_bgImgClip != null)
		{
			m_bgImgClip.doRelease(this);
			m_bgImgClip = null;
		}
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		releaseContent();
	}
	/**
	 * 卸载内容，包括子树和本容器中的背景
	 */
	protected void releaseContent()
	{
		if (m_nodeList != null)
		{
			int size=m_nodeList.size();
			for (int i = 0; i < size; i++)
			{
				((C2D_Widget) m_nodeList.elementAt(i)).doRelease();
			}
			m_nodeList.clear();
		}
		m_bgColor = null;
		releaseBgImage();
		releaseImageClip();
	}

	/**
	 * 完成改变布局，需要向舞台汇报脏矩形，如果本身是透明的， 即没有背景色和背景图，那么会将汇报的过程交给子树
	 */
	public void layoutChanged()
	{
		super.layoutChanged();
		setUpdatePos();
	}

	/**
	 * 设置是否可见.
	 * 
	 * @param visibleNew
	 *            是否可见 TODO...多层会产生问题
	 */
	public void setVisible(boolean visibleNew)
	{
		super.setVisible(visibleNew);
		for (int i = 0; i < m_nodeList.size(); i++)
		{
			((C2D_Widget) m_nodeList.elementAt(i)).m_needUpdateVisible = true;
		}
	}

	/**
	 * 处理由子节点提交的内容变化
	 */
	protected void noticedByChild()
	{
		noticeParent();
	}
	/**
	 * 根据背景图片配置，设置当前的背景图片
	 * @param bgImgCfg
	 */
	public void setBgImgCfg(C2D_ListStyle bgImgCfg)
	{
		C2D_ImageClip bgClip=null;
		int x=0;
		int y=0;
		int imgAnchor=0;
		if (bgImgCfg != null)
		{
			bgClip=bgImgCfg.m_imgClip;

			imgAnchor= bgImgCfg.m_imgAnchor;
			if(bgImgCfg.m_imgPos!=null)
			{
				x=bgImgCfg.m_imgPos.m_x;
				y=bgImgCfg.m_imgPos.m_y;
			}
			m_bgColor = bgImgCfg.m_bgColor;
		}
		setBGImage(bgClip, (int) (x * getWidth() / 100),(int) (y * getHeight() / 100),imgAnchor );
	}
}
