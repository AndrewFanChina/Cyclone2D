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
 * �б��� �б�Ĭ�Ͽ���ӵ�н��㡣����һ���������������ݵĶ��ж�ҳ��ʾ�ؼ��� �������֮�������ͬ���о࣬�趨���б������ߴ硢�иߡ��о�֮��
 * ��ʾ���������Զ���������������Զ���Ӧ�����Ĺ����¼����������б� ��Ԫ֮��Ĺ������Լ���ҳ������������������ѭ���������͡������
 * ��ҳ���������ô����괦�ڱ߽�ʱ�������Զ������б������ע��� ���¼�����Ӧ���������� �б���������Ԥ���趨�ģ�����봫��һ����ʽ���飬�б�����ָ����
 * ��ʽ�齨�ڲ�����������й���ʱ���滻��ʵ�������ڲ���������ֵ�� �������滻�ڲ��������
 * 
 * @author AndrewFan
 * 
 */
public class C2D_List extends C2D_ScrollView
{
	/** ��Ŀ�ĸ�ʽ */
	private C2D_UnitType m_itemFormat[];
	/** ͼ�꼯�� */
	private C2D_ImgList m_iconList;
	/** C2D��ܽṹ������ */
	private C2D_FrameManager m_fm;
	/** ͼƬ�� */
	private C2D_PTC m_PTConfig;
	/** ��Ŀ�����б� */
	private C2D_Array m_itemValues = new C2D_Array();
	/** �б����ͼƬ���� */
	protected C2D_ListStyle m_itemStyle = C2D_WidgetUtil.listStyle_com;
	/** �б����ͼƬ���� */
	protected C2D_ListStyle m_focusStyle = C2D_WidgetUtil.listStyle_fox;
	/** ��Ŀ�߶� */
	protected int m_itemHeight = 10;
	/** ��Ŀ֮��ļ�� */
	protected int m_itemGap = 0;
	/** �ı��е��ַ��� */
	protected C2D_TextFont m_font;
	/** �����Զ��߶� */
	protected boolean m_autoHeight;

	/**
	 * �����б���Ҫ�ƶ�ÿ�����ݵĸ�ʽ
	 * 
	 * @param itemFormat
	 *            ÿ�����ݵĸ�ʽ
	 */
	public C2D_List(C2D_UnitType itemFormat[])
	{
		this(itemFormat, C2D_TextFont.getDefaultFont());
	}

	/**
	 * �����б���Ҫ�ƶ�ÿ�����ݵĸ�ʽ
	 * 
	 * @param itemFormat
	 *            ÿ�����ݵĸ�ʽ
	 * @param font
	 *            ����б��������֣���Ҫ�ƶ��ַ���
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
	 * ��ȡ��Ŀ�߶�
	 * 
	 * @return ��Ŀ�߶�
	 */
	public int getItemHeight()
	{
		return m_itemHeight;
	}

	/**
	 * ������Ŀ�߶�
	 * 
	 * @param itemHeight
	 *            ��Ŀ�߶�
	 * @return �Ƿ�ɹ�����
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
	 * ��ȡ��Ŀ��ļ��
	 * 
	 * @return ��Ŀ��ļ��
	 */
	public int getItemGap()
	{
		return m_itemGap;
	}

	/**
	 * �����Զ��߶ȣ������ݵ�ǰ���иߺ�����ռ�õ�ʵ��ֵ��ȷ����ǰ�����߶�
	 * 
	 * @param autoHeight
	 *            �Ƿ������Զ��߶�
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
	 * ������Ŀ��ļ��
	 * 
	 * @param itemGap
	 *            ��Ŀ��ļ��
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setItemGap(int itemGap)
	{
		this.m_itemGap = itemGap;
		rebuildRowInf();
		m_needRepack = true;
		return true;
	}

	/**
	 * ��ȡ�б����ͼƬ����
	 * 
	 * @return ����ͼƬ����
	 */
	public C2D_ListStyle getItemBgImgCfg()
	{
		return m_itemStyle;
	}

	/**
	 * �����б���ķ��
	 * 
	 * @param itemStyle
	 *            �б�δѡ��ʱ���
	 * @param focusStyle
	 *            �б���ѡ��ʱ���
	 */
	public void setStyle(String itemStyleImg, String focusStyleImg)
	{
		C2D_ListStyle itemStyle = new C2D_ListStyle(new C2D_ImageClip(itemStyleImg), new C2D_PointI(0, 0), 0);
		C2D_ListStyle focusStyle = new C2D_ListStyle(new C2D_ImageClip(focusStyleImg), new C2D_PointI(0, 0), 0);
		setStyle(itemStyle, focusStyle);
	}

	/**
	 * �����б���ķ��
	 * 
	 * @param itemStyle
	 *            �б�δѡ��ʱ���
	 * @param focusStyle
	 *            �б���ѡ��ʱ���
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
	 * ���ô�С���������߶�
	 * 
	 * @param width
	 *            �����õĿ��
	 * @param height
	 *            �����õĸ߶ȶ�
	 * @return �����Ƿ�ɹ�����
	 */
	public C2D_View setSize(int width, int height)
	{
		rebuildRowInf();
		m_needRepack = true;
		return super.setSize(width, height);
	}

	/**
	 * ��ȡͼ���б�
	 * 
	 * @return ͼƬ�ؼ��б�
	 */
	public C2D_ImgList getIconList()
	{
		return m_iconList;
	}

	/**
	 * ����ͼ���б�
	 * 
	 * @param iconList
	 *            ͼƬ�ؼ��б�
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
	 * ����C2D�������
	 * 
	 * @param fm
	 *            ͼƬ�ؼ��б�
	 */
	public void setFrameManager(C2D_FrameManager fm)
	{
		m_fm = fm;
		m_needRepack = true;
	}

	/**
	 * �����Ŀ����Ŀ������������Ԥ�Ƹ�ʽ����ֵ�����ı�����
	 * 
	 * @param itemValue
	 *            ��Ŀ����
	 * @return �Ƿ�ɹ����
	 */
	public boolean addItem(C2D_UnitValue[] itemValue)
	{
		if (!checkValueFormat(itemValue))
		{
			C2D_Debug.logErr("�б����ݸ�ʽ����");
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
	 * ɾ��ָ��λ�õ���Ŀ
	 * 
	 * @param index
	 *            ָ����λ��
	 * @return �Ƿ�ɹ����
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
	 * ��ȡָ��λ�õ���Ŀ����
	 * 
	 * @param index
	 *            ָ��λ��
	 * @return ��Ŀ����
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
	 * ��ȡָ��ID����Ŀ�ؼ������ָ��ID�ڵ�ǰ��Scroll��Χ�����֣�����Ի��
	 * ��Ӧ�Ŀؼ������򷵻�null
	 * 
	 * @param index
	 *            ָ��ID
	 * @return ��Ŀ�ؼ�
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
	 * ɾ��������Ŀ
	 * 
	 * @return �Ƿ�ɷ����˱仯
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
	 * �����ֵ�����Ƿ��뵱ǰ���ݸ�ʽ����
	 * 
	 * @param itemValue
	 *            ��ֵ��������
	 * @return �Ƿ��뵱ǰ���ݸ�ʽ����
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
	 * ����������װ���棬�������Ҫ���� ��װ���棬��ʲôҲ����
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
	 * ������Ŀ����
	 * 
	 * @return ��Ŀ����
	 */
	public int getCount()
	{
		return m_itemValues.size();
	}

	/**
	 * ��õ�ǰͼƬ������
	 * 
	 * @return ͼƬ������
	 */
	public C2D_PTC getPicTxtConfig()
	{
		return m_PTConfig;
	}

	/**
	 * ����ͼƬ������
	 * 
	 * @param PicTxtConfig
	 *            ͼƬ������
	 */
	public void setPicTxtConfig(C2D_PTC PicTxtConfig)
	{
		m_PTConfig = PicTxtConfig;
	}

	private C2D_Array m_releaseList=new C2D_Array();
	/**
	 * ������װ����
	 */
	private synchronized void repackLayout()
	{
		// ����Ƿ������Զ��߶�
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
		// ��������Ĳ���ͳ���
		int wRow = m_nodeList.size();
		int pageRow = getPageRow();
		if (wRow < pageRow)
		{
			for (int i = 0; i < pageRow - wRow; i++)
			{
				C2D_ViewUtil itemView = new C2D_ViewUtil();
				super.addChild(itemView);
				// ��������ݣ�ʹ��Ĭ�ϵ�ͼ��
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
		// ˢ��λ�úͼ��
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
				// ������ȷ�Ŀؼ�����
				C2D_UnitValue[] itemValue = (C2D_UnitValue[]) m_itemValues.elementAt(itemID);
				// ˢ����������Ϣ
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
						// ������ȷ���пؼ�λ��
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
		//ж�ؼ���ж�صĶ���
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
		// ˢ��ѡ���
		refreshItemChange();
		layoutChanged();
	}

	/**
	 * ˢ��ѡ��仯
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
	 * ������Ŀ��ȡ��ӦӦ����ʾ�ķ��
	 * 
	 * @param itemID
	 *            ��ĿID
	 * @return ��ʾ���
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
		// �ı䱳����ɫ�ͱ���ͼƬ
		C2D_ListStyle style = getShowStyle(itemID);
		itemView.setBgImgCfg(style);
		// �ı��ڲ���������ɫ
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
	 * �õ�ǰ�ؼ�ӵ�н��㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 ����ǰ�����Ľ���ؼ���������㲻Ӧ���ֶ��������������
	 * 
	 * @param another
	 *            ԭ����
	 */
	protected void gotFocus(C2D_Widget another)
	{
		super.gotFocus(another);
		refreshItemChange();
		// ���´���һ�ι����¼�
		callScrollEvent(m_currentScroll, true);
	}

	/**
	 * ʧȥ���㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 �㲻Ӧ���ֶ�������������������Ļ�����ǰҳ�潫ʧȥӵ�н���Ŀؼ�
	 * 
	 * @param another
	 *            Ŀ�꽹��
	 */
	protected void lostFocus(C2D_Widget another)
	{
		super.lostFocus(another);
		refreshItemChange();
	}

	/**
	 * ����ID����ͼ�����
	 * 
	 * @param id
	 *            ͼ��ID
	 * @return ͼ��������
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
	 * ����folderId,spriteId���ؾ������
	 * @param folderId
	 *            �ļ���ID
	 * @param spriteId
	 *            ����ID
	 * @return ����
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
	 * �Զ������������������Ҫ���������ݣ���ִ�е����� ������Ҫ��������ȡ��㲻Ӧ��ʹ�����������
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
	 * List������������ӿؼ����˷������ﱻ����Ч��
	 * 
	 * @param child
	 *            ��ӵ��ӿؼ�
	 * @return �Ƿ���ӳɹ�
	 */
	public final boolean addChild(C2D_Widget child)
	{
		return false;
	}

	/**
	 * List������������ӿؼ����˷������ﱻ����Ч��
	 * 
	 * @param child
	 *            ��ӵ��ӿؼ�
	 * @param zOrder
	 *            z����ֵ
	 * @return �Ƿ���ӳɹ�
	 */
	public final boolean addChild(C2D_Widget child, int zOrder)
	{
		return false;
	}

	/**
	 * List������������ӿؼ����˷������ﱻ����Ч��
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @param zOrder
	 *            z����ֵ
	 * @param checkRepeat
	 *            �Ƿ����ظ�
	 * @return �����Ƿ����ɹ�
	 */
	public final boolean addChild(C2D_Widget child, int zOrder, boolean checkRepeat)
	{
		return false;
	}

	/**
	 * ɾ��ָ�����ӽڵ�,List����������ɾ���ؼ����˷������ﱻ����Ч��
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @return �����Ƿ�ɾ���ɹ�
	 */
	public boolean removeChild(C2D_Widget child)
	{
		return false;
	}

	/**
	 * ɾ��ָ���±���ӽڵ�,List����������ɾ���ؼ����˷������ﱻ����Ч��
	 * 
	 * @param childID
	 *            �ӽڵ��±�
	 * @return �����Ƿ�ɾ���ɹ�
	 */
	public boolean removeChildAt(int childID)
	{
		return false;
	}

	/**
	 * ��������ӽڵ�,List����������ɾ���ؼ����˷������ﱻ����Ч��
	 */
	public void removeAllChild()
	{
	}

	/**
	 * ���ȫ������
	 * 
	 * @return ȫ��ȫ������
	 */
	public int getTotalRow()
	{
		return m_itemValues.size();
	}

	/**
	 * ���ҳ����ʾ����
	 * 
	 * @return ҳ����ʾ����
	 */
	public int getPageRow()
	{
		return (int)(m_height + m_itemGap) / (m_itemHeight + m_itemGap);
	}

	/**
	 * ж����Դ
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
