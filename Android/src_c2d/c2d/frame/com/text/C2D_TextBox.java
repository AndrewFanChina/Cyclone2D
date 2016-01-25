package c2d.frame.com.text;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_SizeI;
import c2d.mod.C2D_Consts;
import c2d.plat.font.C2D_FontChar;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_Graphics;

/**
 * 文本段落控件，你需要在构造的时候指定一种字体，如果不指定， 将使用默认的字体特征-系统字型、简单风格、中等大小。你
 * 还需要指定显示的颜色和文本，控件将自动计算呈现的尺寸。 文本中可以加入换行符'\n'实现换行。
 * 
 * @author AndrewFan
 */
public class C2D_TextBox extends C2D_Paragraph
{
	/** 文本的字体 */
	private C2D_TextFont m_font;
	/** 文本颜色 */
	private int m_color;
	/** 文本边框颜色 */
	private C2D_Color m_borderColor;//【暂未实现】
	/** 存储每行文本的缓存，自动计算 */
	private String[] m_textRows;
	/** 内容边距 */
	private C2D_SizeI m_margin;
	/** 行距 */
	private int m_rowGap;

	/**
	 * 构造自动大小的多行文本框。
	 */
	public C2D_TextBox()
	{
		setDefaultFont();
	}
	/**
	 * 构造指定大小的文本框 使用默认的字体特征，系统字型，简单风格，中等大小。
	 * @param limitSize 限制大小
	 */
	public C2D_TextBox(C2D_SizeI limitSize)
	{
		setDefaultFont();
		if(limitSize!=null)
		{
			m_limitW = limitSize.m_width;
			m_limitH = limitSize.m_height;
		}
	}
	/**
	 * 构造指定大小的文本框 使用默认的字体特征，系统字型，简单风格，中等大小。
	 * 
	 * @param limitW
	 *            页面约束宽度
	 * @param limitH
	 *            页面约束高度
	 */
	public C2D_TextBox(int limitW, int limitH)
	{
		setDefaultFont();
		m_limitW = limitW;
		m_limitH = limitH;
	}

	/**
	 * 构造以单行显示的文本框，即使用默认字体以及默认字体的行高作为页面高度。 使用默认的字体特征，系统字型，简单风格，中等大小。
	 * 将采用指定的行宽rowWidth作为页面宽度。默认字体的高度作为页面高度 如果后续修改字体，需要重新设置大小。
	 * 
	 * @param limitW
	 *            约束行宽
	 */
	public C2D_TextBox(int limitW)
	{
		setDefaultFont();
		m_limitW = limitW;
		m_limitH = m_font.getFontHeight();
	}

	/**
	 * 构造以单行显示的文本框，即使用指定字体以及字体的行高作为页面高度。 使用默认的字体特征，系统字型，简单风格，中等大小。
	 * 将采用指定的行宽rowWidth作为页面宽度。使用字体的高度作为页面高度 如果后续修改字体，需要重新设置大小。
	 * 
	 * @param limitW
	 *            约束行宽
	 * @param font
	 *            指定的字体
	 */
	public C2D_TextBox(int limitW, C2D_TextFont font)
	{
		setFont(font);
		m_limitW = limitW;
		m_limitH = m_font.getFontHeight();
	}

	/**
	 * 构造指定大小的文本框， 使用默认的字体特征，系统字型，简单风格，中等大小。指定文本内容
	 * 
	 * @param text
	 *            文本内容
	 * @param limitW
	 *            页面约束宽度
	 * @param limitH
	 *            页面约束高度
	 */
	public C2D_TextBox(String text, int limitW, int limitH)
	{
		this(limitW, limitH);
		setText(text);
	}

	/**
	 * 获取文本颜色
	 * 
	 * @return 文本颜色
	 */
	public int getColor()
	{
		return m_color;
	}

	/**
	 * 设置文本颜色
	 * 
	 * @param color
	 *            文本颜色
	 */
	public void setColor(int color)
	{
		this.m_color = color;
		layoutChanged();
	}

	/**
	 * 设置文本颜色
	 * 
	 * @param color
	 *            文本颜色
	 */
	public void setColor(C2D_Color color)
	{
		if (color == null)
		{
			return;
		}

		this.m_color = color.getColor();
		layoutChanged();
	}

	/**
	 * 设置文本边框颜色
	 * 
	 * @param borderColor
	 *            文本颜色
	 */
	public void setBorderColor(C2D_Color borderColor)
	{
		if (borderColor == null)
		{
			return;
		}

		m_borderColor = borderColor;
		layoutChanged();
	}
	/**
	 * 设置内容边距
	 * @param mx x方向边距
	 * @param my y方向边距
	 */
	public void setMargin(int mx, int my)
	{

		if (m_margin == null)
		{
			m_margin = new C2D_SizeI();
		}
		m_margin.setValue(mx, my);
		refreshCharBuffer();
		layoutChanged();
	}

	public void setRowGap(int rowGap)
	{
		if (m_rowGap == rowGap)
		{
			return;
		}

		m_rowGap = rowGap;
		refreshCharBuffer();
		layoutChanged();
	}
	/**
	 * 获得当前使用的字体，注意修改返回对象不会影响字体控件中的内容。 如果需要修改，可以再次调用setFont(C2D_TextFont)
	 * 
	 * @return 当前使用的字体
	 */
	public C2D_TextFont getFont()
	{
		return m_font;
	}

	/**
	 * 设置新字体
	 * 
	 * @param font
	 *            新字体
	 */
	public void setFont(C2D_TextFont font)
	{
		m_font = font;
		refreshCharBuffer();
		layoutChanged();
	}
	/**
	 * 设置默认字体
	 */
	public void setDefaultFont()
	{
		C2D_TextFont defFont = null;
		defFont = C2D_TextFont.getDefaultFont();
		setFont(defFont);
	}
	/**
	 * 绘制节点
	 * 
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (m_font == null || stage == null || !m_visible)
		{
			return;
		}
		drawTexts(g);
		paintFocus(g,m_PageWidth, m_PageHeight);
	}

	/**
	 * 设置文本控件的所有参数
	 * 
	 * @param font
	 *            文本字体
	 * @param text
	 *            文本内容
	 * @param color
	 *            文本颜色
	 * @param anchor
	 *            锚点
	 */
	public void setParameters(C2D_TextFont font, String text, int color, int anchor)
	{
		m_font = font;
		m_text = text;
		if (m_text != null && m_font != null)
		{
			m_PageWidth = m_font.stringWidth(m_text);
			m_PageHeight = m_font.getFontHeight();
		}
		m_color = color;
		m_anchor = anchor;
		layoutChanged();
	}

	/** 用于计算文本缓存的动态数组 */
	private static C2D_Array bufferArray = new C2D_Array();

	/**
	 * 刷新文本缓存
	 */
	protected void refreshCharBuffer()
	{
		if (m_font == null)
		{
			return;
		}
		int textLen = 0;
		if (m_text != null)
		{
			textLen = m_text.length();
			m_font.loadChars(m_text);
		}
		// 计算字符索引和占据尺寸
		bufferArray.removeAllElements();
		int mx = 0;
		int my = 0;
		if (m_margin != null)
		{
			mx = m_margin.m_width;
			my = m_margin.m_height;
		}
		float widthRow = mx;
		float widthMax = 0;
		int heightMax = m_font.getFontHeight()+m_rowGap;
		boolean nextRow = false;
		boolean expWidth = false;
		StringBuffer bufferRow = new StringBuffer();
		for (int i = 0; i < textLen; i++)
		{
			char c = m_text.charAt(i);
			if (c == '\n')
			{
				if (!expWidth)
				{
					nextRow = true;
				}
				expWidth = false;
			}
			else
			{
				bufferRow.append(c);
				widthRow = mx + m_font.stringWidth(bufferRow.toString());
				expWidth = false;
				if(i+1<textLen)
				{
					char cNext = m_text.charAt(i+1);
					int len=bufferRow.length();
					bufferRow.append(cNext);
					if ( mx + m_font.stringWidth(bufferRow.toString()) > m_limitW)
					{
						nextRow = true;
						expWidth = true;
					}
					bufferRow.setLength(len);
				}
	
			}
			// 换行处理
			if (nextRow || i + 1 >= textLen)
			{
				// 存储当前行
				bufferArray.addElement(bufferRow.toString());
				bufferRow.setLength(0);
				// 切换到下一行
				if (widthRow > widthMax)
				{
					widthMax = widthRow;
				}
				widthRow = mx;
				if (i < textLen - 1)
				{
					heightMax += m_font.getFontHeight()+m_rowGap;
				}
				nextRow = false;
			}
		}
		if (widthRow > widthMax)
		{
			widthMax = widthRow;
		}
		// 存储文本数组
		m_textRows = new String[bufferArray.size()];
		for (int i = 0; i < m_textRows.length; i++)
		{
			m_textRows[i] = (String) bufferArray.elementAt(i);
		}
		bufferArray.removeAllElements();
		// 刷新尺寸
		m_PageWidth = C2D_Math.ceil(widthMax);
		m_PageHeight = heightMax;
		// 计算能显示的行数和尺寸
		if (m_PageHeight > m_limitH && (m_font.getFontHeight() + m_rowGap > 0))
		{
			m_pageRow = (int)(m_limitH - my) / (m_font.getFontHeight() + m_rowGap);
			m_PageHeight = m_pageRow * m_font.getFontHeight();
		}
		else
		{
			m_pageRow = m_textRows.length;
		}
		m_totalRow = m_textRows.length;
		m_currentScroll = 0;
	}
	/**
	 * 获取当前字符串下标为id的字符的宽度
	 * @param id 字符下标
	 * @return 字符宽度
	 */
	public float getCharW(int id)
	{
		if(m_text==null|| id<0||id>=m_text.length())
		{
			return 0;
		}
		char c = m_text.charAt(id);
		C2D_FontChar clip = m_font.getFontChar(c);
		float widthW = 0;
		if (clip != null)
		{
			widthW = clip.advanceX;
		}
		return widthW;
	}

	/**
	 * 绘制文本
	 */
	private void drawTexts(C2D_Graphics g)
	{
		if (m_font == null)
		{
			return;
		}
		float mx = 0;
		float my = 0;
		if (m_margin != null)
		{
			mx = m_margin.m_width;
			my = m_margin.m_height;
		}
		try
		{
			float _w = m_PageWidth;
			float _h = m_PageHeight;
			float x_offset_clip = 0;
			float y_offset_clip = 0;
			// 横向偏移
			if ((C2D_Consts.LEFT & m_anchor) != 0)
			{
				x_offset_clip = 0;
			}
			else if ((C2D_Consts.HCENTER & m_anchor) != 0)
			{
				x_offset_clip = -_w / 2;
			}
			else if ((C2D_Consts.RIGHT & m_anchor) != 0)
			{
				x_offset_clip = -_w;
			}
			// 纵向偏移
			if ((C2D_Consts.TOP & m_anchor) != 0)
			{
				y_offset_clip = 0;
			}
			else if ((C2D_Consts.VCENTER & m_anchor) != 0)
			{
				y_offset_clip = -_h / 2;
			}
			else if ((C2D_Consts.BOTTOM & m_anchor) != 0)
			{
				y_offset_clip = -_h;
			}
			float xBegin = m_xToTop + x_offset_clip;
			float yBegin = m_yToTop + y_offset_clip;
			float x = xBegin;
			float y = yBegin;
			// 绘制文本区域背景
			if (m_textBgColor != null)
			{
				g.fillRect(x, y, m_PageWidth, m_PageHeight, m_textBgColor.getColor(), 0);
			}
			// 绘制文本
			for (int i = m_currentScroll; i < m_currentScroll + m_pageRow; i++)
			{
				m_font.paintString(m_textRows[i], x + mx, y + my, m_color, 0, 0);
				y += m_font.getRowHeight() + m_rowGap;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onRelease()
	{
		super.onRelease();
		m_font = null;
		m_textRows = null;
	}
}
