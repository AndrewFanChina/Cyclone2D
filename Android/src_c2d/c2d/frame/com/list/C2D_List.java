package c2d.frame.com.list;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.data.unit_type.C2D_UT_PicBox;
import c2d.frame.com.data.unit_type.C2D_UT_PicTxt;
import c2d.frame.com.data.unit_type.C2D_UT_Sprite;
import c2d.frame.com.data.unit_type.C2D_UT_SysTxt;
import c2d.frame.com.data.unit_type.C2D_UV_Int;
import c2d.frame.com.data.unit_type.C2D_UV_Int2;
import c2d.frame.com.data.unit_type.C2D_UV_String;
import c2d.frame.com.data.unit_type.C2D_UnitType;
import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.frame.com.list.scroll.C2D_ScrollView;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.util.C2D_WidgetUtil;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;
import c2d.plat.gfx.C2D_ImgList;

/**
 * 列表类 列表默认可以拥有焦点。它是一个可以配置其内容的多行多页显示控件。 里面的行之间具有相同的行距，设定了列表的总体尺寸、行高、行距之后，
 * 显示的行数会自动计算出来，它将自动响应按键的滚动事件，包括在列表 单元之间的滚动，以及按页面滚动。你可以设置其循环滚动类型。如果是
 * 按页面滚动，那么当光标处于边界时，可以自动脱离列表。你可以注册滚 动事件以响应滚动操作。 列表内容是你预先设定的，你必须传入一个格式数组，列表将根据指定的
 * 格式组建内部组件，当进行滚动时，替换的实际上是内部组件里的数值， 而不是替换内部组件本身。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_List extends C2D_ScrollView
{
	/** 条目的格式 */
	private C2D_UnitType m_itemFormat[];
	/** 图标集合 */
	private C2D_ImgList m_iconList;
	/** C2D框架结构管理器 */
	private C2D_FrameManager m_fm;
	/** 图片字 */
	private C2D_PTC m_PTConfig;
	/** 条目数据列表 */
	private C2D_Array m_itemValues = new C2D_Array();
	/** 列表项背景图片配置 */
	protected C2D_ListStyle m_itemStyle = C2D_WidgetUtil.listStyle_com;
	/** 列表项背景图片配置 */
	protected C2D_ListStyle m_focusStyle = C2D_WidgetUtil.listStyle_fox;
	/** 条目高度 */
	protected int m_itemHeight = 10;
	/** 条目之间的间隔 */
	protected int m_itemGap = 0;
	/** 文本中的字符集 */
	protected C2D_TextFont m_font;
	/** 设置自动高度 */
	protected boolean m_autoHeight;

	/**
	 * 构建列表，需要制定每行内容的格式
	 * 
	 * @param itemFormat
	 *            每行内容的格式
	 */
	public C2D_List(C2D_UnitType itemFormat[])
	{
		this(itemFormat, C2D_TextFont.getDefaultFont());
	}

	/**
	 * 构建列表，需要制定每行内容的格式
	 * 
	 * @param itemFormat
	 *            每行内容的格式
	 * @param font
	 *            如果列表中有文字，需要制定字符集
	 */
	public C2D_List(C2D_UnitType itemFormat[], C2D_TextFont font)
	{
		m_itemFormat = itemFormat;
		m_focusable = true;
		if (m_itemFormat == null)
		{
			m_itemFormat = new C2D_UnitType[]
			{
				new C2D_UT_PicBox(50, 50, C2D_Consts.HVCENTER)
			};
		}
		m_font = font;
		if (m_font == null)
		{
			m_font = C2D_TextFont.getDefaultFont();
		}
		setItemHeight(m_font.getFontHeight());
		setUse_HotRegion(true);
	}

	/**
	 * 获取条目高度
	 * 
	 * @return 条目高度
	 */
	public int getItemHeight()
	{
		return m_itemHeight;
	}

	/**
	 * 设置条目高度
	 * 
	 * @param itemHeight
	 *            条目高度
	 * @return 是否成功设置
	 */
	public boolean setItemHeight(int itemHeight)
	{
		if (itemHeight <= 0)
		{
			return false;
		}
		this.m_itemHeight = itemHeight;
		rebuildRowInf();
		m_needRepack = true;
		return true;
	}

	/**
	 * 获取条目间的间隔
	 * 
	 * @return 条目间的间隔
	 */
	public int getItemGap()
	{
		return m_itemGap;
	}

	/**
	 * 设置自动高度，将根据当前的行高和行数占用的实际值来确定当前容器高度
	 * 
	 * @param autoHeight
	 *            是否允许自动高度
	 */
	public void setAutoHeight(boolean autoHeight)
	{
		if (m_autoHeight != autoHeight)
		{
			m_autoHeight = autoHeight;
			m_needRepack = true;
		}
	}

	/**
	 * 设置条目间的间隔
	 * 
	 * @param itemGap
	 *            条目间的间隔
	 * @return 是否成功设置
	 */
	public boolean setItemGap(int itemGap)
	{
		this.m_itemGap = itemGap;
		rebuildRowInf();
		m_needRepack = true;
		return true;
	}

	/**
	 * 获取列表项背景图片配置
	 * 
	 * @return 背景图片配置
	 */
	public C2D_ListStyle getItemBgImgCfg()
	{
		return m_itemStyle;
	}

	/**
	 * 设置列表项的风格
	 * 
	 * @param itemStyle
	 *            列表未选中时风格
	 * @param focusStyle
	 *            列表项选中时风格
	 */
	public void setStyle(String itemStyleImg, String focusStyleImg)
	{
		C2D_ListStyle itemStyle = new C2D_ListStyle(new C2D_ImageClip(itemStyleImg), new C2D_PointI(0, 0), 0);
		C2D_ListStyle focusStyle = new C2D_ListStyle(new C2D_ImageClip(focusStyleImg), new C2D_PointI(0, 0), 0);
		setStyle(itemStyle, focusStyle);
	}

	/**
	 * 设置列表项的风格
	 * 
	 * @param itemStyle
	 *            列表未选中时风格
	 * @param focusStyle
	 *            列表项选中时风格
	 */
	public void setStyle(C2D_ListStyle itemStyle, C2D_ListStyle focusStyle)
	{
		if (itemStyle != null)
		{
			if (m_itemStyle != null)
			{
				m_itemStyle.doRelease(this);
			}
			m_itemStyle = itemStyle;
		}
		if (focusStyle != null)
		{
			if (m_focusStyle != null)
			{
				m_focusStyle.doRelease(this);
			}
			m_focusStyle = focusStyle;
		}
		m_needRepack = true;
	}

	/**
	 * 设置大小，即宽度与高度
	 * 
	 * @param width
	 *            新设置的宽度
	 * @param height
	 *            新设置的高度度
	 * @return 返回是否成功设置
	 */
	public C2D_View setSize(int width, int height)
	{
		rebuildRowInf();
		m_needRepack = true;
		return super.setSize(width, height);
	}

	/**
	 * 获取图标列表
	 * 
	 * @return 图片控件列表
	 */
	public C2D_ImgList getIconList()
	{
		return m_iconList;
	}

	/**
	 * 设置图标列表
	 * 
	 * @param iconList
	 *            图片控件列表
	 */
	public void setIconList(C2D_ImgList iconList)
	{
		if(m_iconList!=null)
		{
			m_iconList.doRelease(this);
			m_iconList=null;
		}
		m_iconList = iconList;
		m_needRepack = true;
	}

	/**
	 * 设置C2D框架容器
	 * 
	 * @param fm
	 *            图片控件列表
	 */
	public void setFrameManager(C2D_FrameManager fm)
	{
		m_fm = fm;
		m_needRepack = true;
	}

	/**
	 * 添加条目，条目内容是须遵守预制格式的数值或者文本内容
	 * 
	 * @param itemValue
	 *            条目内容
	 * @return 是否成功添加
	 */
	public boolean addItem(C2D_UnitValue[] itemValue)
	{
		if (!checkValueFormat(itemValue))
		{
			C2D_Debug.logErr("列表数据格式错误");
			return false;
		}
		boolean notFull = (m_itemValues.size() < getPageRow());
		C2D_UnitValue[] itemValueClone = new C2D_UnitValue[itemValue.length];
		for (int i = 0; i < itemValueClone.length; i++)
		{
			itemValueClone[i] = itemValue[i].cloneSelf();
		}
		m_itemValues.addElement(itemValueClone);
		rebuildRowInf();
		setCurrentScroll(m_currentScroll);
		if (notFull)
		{
			m_needRepack = true;
		}
		return true;
	}

	/**
	 * 删除指定位置的条目
	 * 
	 * @param index
	 *            指定的位置
	 * @return 是否成功添加
	 */
	public boolean removeItemAt(int index)
	{
		if (index < 0 || index >= m_itemValues.size())
		{
			return false;
		}
		boolean inPage = (index >= m_firstRow && index <= m_firstRow + getPageRow());
		m_itemValues.removeElementAt(index);
		rebuildRowInf();
		setCurrentScroll(m_currentScroll);
		if (inPage)
		{
			m_needRepack = true;
		}
		return true;
	}

	/**
	 * 获取指定位置的条目内容
	 * 
	 * @param index
	 *            指定位置
	 * @return 条目内容
	 */
	public Object[] getItemAt(int index)
	{
		if (index < 0 || index >= m_itemValues.size())
		{
			return null;
		}
		return (Object[]) m_itemValues.elementAt(index);
	}
	/**
	 * 获取指定ID的条目控件，如果指定ID在当前的Scroll范围被呈现，则可以获得
	 * 相应的控件，否则返回null
	 * 
	 * @param index
	 *            指定ID
	 * @return 条目控件
	 */
	public C2D_ViewUtil getItemViewAt(int index)
	{
		if (index < m_firstRow|| index >= m_firstRow+getPageRow() || index >= m_itemValues.size())
		{
			return null;
		}
		return (C2D_ViewUtil)getChildAt(index-m_firstRow);
	}
	/**
	 * 删除所有条目
	 * 
	 * @return 是否成发生了变化
	 */
	public boolean removeAllItems()
	{
		if (m_itemValues.size() <= 0)
		{
			return false;
		}
		m_itemValues.removeAllElements();
		setCurrentScroll(0);
		m_needRepack = true;
		return true;
	}

	/**
	 * 检查数值内容是否与当前内容格式兼容
	 * 
	 * @param itemValue
	 *            数值内容数组
	 * @return 是否与当前内容格式兼容
	 */
	private boolean checkValueFormat(Object[] itemValue)
	{
		if (itemValue == null || itemValue.length != m_itemFormat.length)
		{
			return false;
		}
		for (int i = 0; i < itemValue.length; i++)
		{
			Object obj = itemValue[i];
			C2D_UnitType type = m_itemFormat[i];
			if (type instanceof C2D_UT_Sprite)
			{
				if (!(obj instanceof C2D_UV_Int))
				{
					return false;
				}
			}
			else if (type instanceof C2D_UT_PicBox || type instanceof C2D_UT_PicTxt || type instanceof C2D_UT_SysTxt)
			{
				if (!(obj instanceof C2D_UV_String))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 立刻重新组装界面，如果不需要重新 组装界面，则什么也不做
	 */
	public void repackLayoutNow()
	{
		if (!m_needRepack)
		{
			return;
		}
		repackLayout();
		m_needRepack = false;
	}

	/**
	 * 返回条目个数
	 * 
	 * @return 条目个数
	 */
	public int getCount()
	{
		return m_itemValues.size();
	}

	/**
	 * 获得当前图片字配置
	 * 
	 * @return 图片字配置
	 */
	public C2D_PTC getPicTxtConfig()
	{
		return m_PTConfig;
	}

	/**
	 * 设置图片字配置
	 * 
	 * @param PicTxtConfig
	 *            图片字配置
	 */
	public void setPicTxtConfig(C2D_PTC PicTxtConfig)
	{
		m_PTConfig = PicTxtConfig;
	}

	private C2D_Array m_releaseList=new C2D_Array();
	/**
	 * 重新组装界面
	 */
	private synchronized void repackLayout()
	{
		// 检查是否允许自动高度
		if (m_autoHeight)
		{
			int needRow = C2D_Math.min(getPageRow(), getTotalRow());
			int h = needRow * getItemHeight() + (needRow - 1) * getItemGap();
			if (h < 0)
			{
				h = 0;
			}
			setSize(m_width, h);
		}
		// 检查行数的不足和超出
		int wRow = m_nodeList.size();
		int pageRow = getPageRow();
		if (wRow < pageRow)
		{
			for (int i = 0; i < pageRow - wRow; i++)
			{
				C2D_ViewUtil itemView = new C2D_ViewUtil();
				super.addChild(itemView);
				// 添加行内容，使用默认的图标
				for (int j = 0; j < m_itemFormat.length; j++)
				{
					C2D_UnitType wt = m_itemFormat[j];
					C2D_Widget wi=null;
					if (wt instanceof C2D_UT_Sprite)
					{
						wi = getSprite(-1,-1);
					}
					else if (wt instanceof C2D_UT_PicBox)
					{
						wi = new C2D_PicBox((C2D_Image)null);
					}
					else if (wt instanceof C2D_UT_PicTxt)
					{
						wi = new C2D_PicTextBox(m_PTConfig);
					}
					else if (wt instanceof C2D_UT_SysTxt)
					{
						C2D_TextBox pTxt = new C2D_TextBox();
						if (m_font != null)
						{
							pTxt.setFont(m_font);
						}
						wi=pTxt;
					}
					if(wi!=null)
					{
						 itemView.addChild(wi);
						 wi.setZOrder(j);
						 wi.setFlag(j);
					}
				}
				itemView.setZOrder(super.getChildCount() * 5);
			}
		}
		else if (wRow > pageRow)
		{
			for (int i = 0; i < wRow - pageRow; i++)
			{
				int id = m_nodeList.size() - 1;
				C2D_Widget rw = getChildByFlag(id);
				if (rw != null)
				{
					rw.doRelease();
				}
				super.removeChildAt(id);
			}
		}
		m_releaseList.clear();
		// 刷新位置和间隔
		wRow = m_nodeList.size();
		for (int i = 0; i < wRow; i++)
		{
			C2D_ViewUtil itemView = (C2D_ViewUtil) super.getChildAt(i);
			itemView.setPosTo(0, i * (m_itemHeight + m_itemGap));
			itemView.setSize(m_width, m_itemHeight);
			itemView.setBgImgCfg(null);
			itemView.setZOrder(i * 5);
			int itemID = m_firstRow + i;
			if (itemID < getTotalRow())
			{
				// 设置正确的控件内容
				C2D_UnitValue[] itemValue = (C2D_UnitValue[]) m_itemValues.elementAt(itemID);
				// 刷新行内容信息
				for (int j = 0; j < m_itemFormat.length; j++)
				{
					C2D_Widget widget = itemView.getChildByFlag(j);
					C2D_UnitType wt = m_itemFormat[j];
					C2D_UnitValue wv = itemValue[j];
					try
					{
						if (wt instanceof C2D_UT_Sprite)
						{
							if (widget != null)
							{
								itemView.removeChild(widget);
								m_releaseList.addElement(widget);
								widget = null;
							}
							C2D_UV_Int2 v2=  (C2D_UV_Int2) wv;
							C2D_Sprite wSp = getSprite(v2.m_v1,v2.m_v2);
							if (wSp != null)
							{
								itemView.addChild(wSp);
								wSp.setZOrder(j);
								wSp.setFlag(j);
								widget = wSp;
								itemView.orderChildren();
							}
						}
						else if (widget instanceof C2D_PicBox)
						{
							C2D_PicBox wImg = (C2D_PicBox) widget;
							String name = ((C2D_UV_String) wv).m_value;
							wImg.setImage(getIcon(name));
						}
						else if (widget instanceof C2D_PicTextBox)
						{
							C2D_PicTextBox pImg = (C2D_PicTextBox) widget;
							pImg.setConfig(m_PTConfig);
							String stringValue = ((C2D_UV_String) wv).m_value;
							pImg.setText(stringValue);
						}
						else if (widget instanceof C2D_TextBox)
						{
							C2D_TextBox pTxt = (C2D_TextBox) widget;
							String stringValue = ((C2D_UV_String) wv).m_value;
							pTxt.setText(stringValue);
							if (itemID == m_currentScroll && m_focused)
							{
								if (m_focusStyle != null)
								{
									pTxt.setColor(m_focusStyle.m_txtColor);
								}
							}
							else
							{
								if (m_itemStyle != null)
								{
									pTxt.setColor(m_itemStyle.m_txtColor);
								}
							}
						}
						// 设置正确的行控件位置
						if (widget != null)
						{
							widget.setPosTo(wt.getX() * m_width / C2D_UnitType.PB, wt.getY() * m_itemHeight
									/ C2D_UnitType.PB);
							widget.setAnchor(wt.getAnchor());
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}
			}
		}
		//卸载即将卸载的对象
		int rLen=m_releaseList.size();
		for (int i = 0; i < rLen; i++)
		{
			C2D_Widget wi=(C2D_Widget)m_releaseList.elementAt(i);
			if(wi!=null)
			{
				wi.doRelease();
			}
		}
		m_releaseList.clear();
		// 刷新选项背景
		refreshItemChange();
		layoutChanged();
	}

	/**
	 * 刷新选项变化
	 */
	protected void refreshItemChange()
	{
		int pageRow = getPageRow();
		for (int i = 0; i < pageRow; i++)
		{
			int itemID = m_firstRow + i;
			C2D_ViewUtil itemView = (C2D_ViewUtil) super.getChildAt(i);
			if (itemView == null)
			{
				continue;
			}
			itemChange(itemID, itemView);
			itemView.setVisible(itemID < getTotalScroll());
		}
	}

	/**
	 * 根据条目获取相应应该显示的风格
	 * 
	 * @param itemID
	 *            条目ID
	 * @return 显示风格
	 */
	protected C2D_ListStyle getShowStyle(int itemID)
	{
		if (itemID == m_currentScroll && m_focused)
		{
			return m_focusStyle;
		}
		else
		{
			return m_itemStyle;
		}
	}

	protected void itemChange(int itemID, C2D_ViewUtil itemView)
	{
		// 改变背景颜色和背景图片
		C2D_ListStyle style = getShowStyle(itemID);
		itemView.setBgImgCfg(style);
		// 改变内部的文字颜色
		for (int j = 0; j < m_itemFormat.length; j++)
		{
			C2D_Widget widget = itemView.getChildAt(j);
			try
			{
				if (widget instanceof C2D_TextBox)
				{
					C2D_TextBox pTxt = (C2D_TextBox) widget;
					if (style != null)
					{
						pTxt.setColor(m_focusStyle.m_txtColor);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * 让当前控件拥有焦点，这个函数将自动将控件设置成可以拥有焦点。 将当前场景的焦点控件变成自身。你不应该手动调用这个函数。
	 * 
	 * @param another
	 *            原焦点
	 */
	protected void gotFocus(C2D_Widget another)
	{
		super.gotFocus(another);
		refreshItemChange();
		// 重新触发一次滚动事件
		callScrollEvent(m_currentScroll, true);
	}

	/**
	 * 失去焦点，这个函数将自动将控件设置成可以拥有焦点。 你不应该手动调用这个函数，那样的话，当前页面将失去拥有焦点的控件
	 * 
	 * @param another
	 *            目标焦点
	 */
	protected void lostFocus(C2D_Widget another)
	{
		super.lostFocus(another);
		refreshItemChange();
	}

	/**
	 * 根据ID返回图标对象
	 * 
	 * @param id
	 *            图标ID
	 * @return 图标对象爱你过
	 */
	private C2D_Image getIcon(String name)
	{
		if (m_iconList==null)
		{
			m_iconList=new C2D_ImgList();
			m_iconList.transHadler(this);
		}
		return m_iconList.getImage(name);
	}

	/**
	 * 根据folderId,spriteId返回精灵对象
	 * @param folderId
	 *            文件夹ID
	 * @param spriteId
	 *            精灵ID
	 * @return 精灵
	 */
	private C2D_Sprite getSprite(int folderId,int spriteId)
	{
		if (m_fm == null)
		{
			return null;
		}
		C2D_Sprite s = new C2D_Sprite(m_fm,(short)folderId, (short) spriteId);
		s.setAutoPlay(C2D_Sprite.AUTOPLAY_FRAME);
		return s;
	}

	/**
	 * 自动更新整个树，检测需要调整的内容，并执行调整。 比如需要重新排序等。你不应该使用这个方法。
	 */
	protected void onAutoUpdate()
	{
		if (m_needRepack)
		{
			repackLayout();
			m_needRepack = false;
		}
		super.onAutoUpdate();
	}

	/**
	 * List不允许自行添加控件，此方法这里被屏蔽效用
	 * 
	 * @param child
	 *            添加的子控件
	 * @return 是否添加成功
	 */
	public final boolean addChild(C2D_Widget child)
	{
		return false;
	}

	/**
	 * List不允许自行添加控件，此方法这里被屏蔽效用
	 * 
	 * @param child
	 *            添加的子控件
	 * @param zOrder
	 *            z排序值
	 * @return 是否添加成功
	 */
	public final boolean addChild(C2D_Widget child, int zOrder)
	{
		return false;
	}

	/**
	 * List不允许自行添加控件，此方法这里被屏蔽效用
	 * 
	 * @param child
	 *            子节点
	 * @param zOrder
	 *            z排序值
	 * @param checkRepeat
	 *            是否检测重复
	 * @return 返回是否插入成功
	 */
	public final boolean addChild(C2D_Widget child, int zOrder, boolean checkRepeat)
	{
		return false;
	}

	/**
	 * 删除指定的子节点,List不允许自行删除控件，此方法这里被屏蔽效用
	 * 
	 * @param child
	 *            子节点
	 * @return 返回是否删除成功
	 */
	public boolean removeChild(C2D_Widget child)
	{
		return false;
	}

	/**
	 * 删除指定下标的子节点,List不允许自行删除控件，此方法这里被屏蔽效用
	 * 
	 * @param childID
	 *            子节点下标
	 * @return 返回是否删除成功
	 */
	public boolean removeChildAt(int childID)
	{
		return false;
	}

	/**
	 * 清除所有子节点,List不允许自行删除控件，此方法这里被屏蔽效用
	 */
	public void removeAllChild()
	{
	}

	/**
	 * 获得全部行数
	 * 
	 * @return 全部全部行数
	 */
	public int getTotalRow()
	{
		return m_itemValues.size();
	}

	/**
	 * 获得页面显示行数
	 * 
	 * @return 页面显示行数
	 */
	public int getPageRow()
	{
		return (int)(m_height + m_itemGap) / (m_itemHeight + m_itemGap);
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_itemFormat = null;
		m_iconList = null;
		m_fm = null;
		m_PTConfig = null;
		if (m_itemValues != null)
		{
			m_itemValues.removeAllElements();
			m_itemValues = null;
		}
		if (m_itemStyle != null)
		{
			m_itemStyle.doRelease();
			m_itemStyle = null;
		}
		if (m_focusStyle != null)
		{
			m_focusStyle.doRelease();
			m_focusStyle = null;
		}
	}
}
