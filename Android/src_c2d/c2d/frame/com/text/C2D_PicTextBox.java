package c2d.frame.com.text;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 图片文本段落控件 你需要指定一个配置对象，包含了图片文本的样式。 接下来你可以设置你想显示的文本，用setText函数更改显示的文本。
 * 文本中可以加入换行符'\n'实现换行。
 * 
 * @author AndrewFan
 */
public class C2D_PicTextBox extends C2D_Paragraph
{
	/** 当前图片文本的配置对象 */
	private C2D_PTC m_config;
	/**
	 * 存储每行文本的ID缓存，自动计算， -1表示在配置对象中没有找到映射字符
	 */
	private short[][] m_textRows;

	/**
	 * 构造函数，指定配置对象，页面宽度和高度
	 * 
	 * @param config
	 *            配置对象
	 * @param limitW
	 *            页面约束宽度
	 * @param limitH
	 *            页面约束高度
	 */
	public C2D_PicTextBox(C2D_PTC config, int limitW, int limitH)
	{
		m_config = config;
		m_limitW = limitW;
		m_limitH = limitH;
	}

	/**
	 * 构造函数，指定配置对象，此构造函数将使用自动页面大小
	 * 
	 * @param config
	 *            配置对象
	 */
	public C2D_PicTextBox(C2D_PTC config)
	{
		m_config = config;
	}

	/**
	 * 设置配置对象，当你对配置对象修改之后，如果需要控件立刻刷新， 需要调用这个方法。
	 * 
	 * @param config
	 *            配置对象
	 */
	public void setConfig(C2D_PTC config)
	{
		m_config = config;
		refreshCharBuffer();
		layoutChanged();
	}

	/**
	 * 获取配置对象
	 * 
	 * @return 配置对象
	 */
	public C2D_PTC getConfig()
	{
		return m_config;
	}

	/**
	 * 绘制节点
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (!m_visible || !m_inCamera || stage == null)
		{
			return;
		}
		drawTexts(g);
		paintFocus(g, m_PageWidth, m_PageHeight);
	}

	/** 用于计算文本缓存的动态数组 */
	private static C2D_Array bufferArray = new C2D_Array();
	private static short bufferRow[] = new short[256];

	/**
	 * 刷新文本缓存
	 */
	protected void refreshCharBuffer()
	{
		if (m_text == null || m_config == null)
		{
			return;
		}
		String charTable = m_config.getChars();
		if (charTable == null)
		{
			return;
		}
		int textLen = m_text.length();

		// 计算字符索引和占据尺寸
		bufferArray.removeAllElements();
		int widthT = 0;
		int widthMax = 0;
		int heightMax = m_config.m_charH;
		int rowLen = 0;
		boolean nextRow = false;
		boolean expWidth = false;
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
				short id = (short) charTable.indexOf(c);
				bufferRow[rowLen] = id;
				rowLen++;
				widthT += m_config.getCharW(id);
				if (widthT + m_config.m_gapX + m_config.getCharW(id) + (m_config.m_gapX > 0 ? 0 : m_config.m_gapX) > m_limitW)
				{
					nextRow = true;
					expWidth = true;
				}
				else
				{
					widthT += m_config.m_gapX;
					expWidth = false;
				}
			}
			// 换行处理
			if (nextRow || i + 1 >= textLen)
			{
				// 存储当前行
				short[] rowI = new short[rowLen];
				System.arraycopy(bufferRow, 0, rowI, 0, rowLen);
				bufferArray.addElement(rowI);
				// 切换到下一行
				rowLen = 0;
				if (widthT > widthMax)
				{
					widthMax = widthT;
				}
				widthT = 0;
				if (i < textLen - 1)
				{
					heightMax += m_config.m_gapY + m_config.m_charH;
				}
				nextRow = false;
			}
		}
		if (widthT > widthMax)
		{
			widthMax = widthT;
		}
		// 存储索引数组
		m_textRows = new short[bufferArray.size()][];
		for (int i = 0; i < m_textRows.length; i++)
		{
			m_textRows[i] = (short[]) bufferArray.elementAt(i);
		}
		bufferArray.removeAllElements();
		// 刷新尺寸
		m_PageWidth = widthMax;
		m_PageHeight = heightMax;
		// 计算能显示
		if (m_PageHeight > m_limitH)
		{
			m_pageRow = (int)(m_limitH + m_config.m_gapY) / (m_config.m_gapY + m_config.m_charH);
			m_PageHeight = m_pageRow * (m_config.m_gapY + m_config.m_charH) - m_config.m_gapY;
		}
		else
		{
			m_pageRow = m_textRows.length;
		}
		m_totalRow = m_textRows.length;
		m_currentScroll = 0;
	}

	/**
	 * 绘制文本
	 * 
	 * @param visibleRects
	 *            可见区域
	 * @param g
	 */
	private void drawTexts(C2D_Graphics g)
	{
		if (m_config == null)
		{
			return;
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
			for (int i = m_currentScroll; i < m_currentScroll + m_pageRow; i++)
			{
				for (int j = 0; j < m_textRows[i].length; j++)
				{
					short id = m_textRows[i][j];
					if (id >= 0)// 普通字符
					{
						int gapX = m_config.m_gapX;
						if (j == 0)
						{
							gapX = 0;
						}
						C2D_ImageClip charT = m_config.getChar(id);
						if (charT != null)
						{
							charT.draw(x + gapX, y);
						}
					}
					x += m_config.getCharW(id) + m_config.m_gapX;
				}
				x = xBegin;
				y += m_config.m_charH + m_config.m_gapY;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_config = null;
		m_textRows = null;
	}
}
