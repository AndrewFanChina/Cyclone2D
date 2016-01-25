package c2d.frame.com.text;

import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 图片文本的配置类 你需要指定一个图片C2D_Image和一个映射字符串，指定从这图片上绘制的
 * 起点坐标(charX,charY)，指定单个字符的宽度(CharW)和整体高度(CharH)，
 * 显示字符之间的间隔(Gap)，最少显示的位数(LeastNum)。这些参数可以单独指定，也 可以使用setParameters一起设置。
 * 注意与图片数字不同，这里的字符换的映射图可以来自多行，当一行不足以
 * 包含所有的文本时，读取到图片的尾部，将从下一行(textX,textY+=CharH)继续 读取。
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_PTC extends C2D_Object
{
	/** 用于绘制的图片 */
	protected C2D_Image m_image;
	/** 映射字符串 */
	protected String m_charTable;
	/** 字符序列位于图片中的X坐标 */
	protected int m_TextX;
	/** 字符序列位于图片中的Y坐标 */
	protected int m_TextY;
	/** 单个字符高度 */
	public int m_charH;
	/** 字符之间的横向间隔 */
	public int m_gapX;
	/** 字符之间的纵向间隔 */
	protected int m_gapY;
	/** 图片文本每个字符的位置,长度 = 映射字符串长度*2,(x,y) */
	protected short m_charPos[];
	/** 图片字切片表 */
	protected C2D_ImageClip m_chars[];

	/**
	 * 获取字符序列位于图片中的X坐标
	 * 
	 * @return X坐标
	 */
	public int getTextX()
	{
		return m_TextX;
	}

	/**
	 * 获取字符序列位于图片中的Y坐标
	 * 
	 * @return Y坐标
	 */
	public int getTextY()
	{
		return m_TextY;
	}

	/**
	 * 获取单个字符宽度
	 * 
	 * @return 字符高度
	 */
	public int getCharH()
	{
		return m_charH;
	}

	/**
	 * 获取字符之间的横向间隔
	 * 
	 * @return 间隔
	 */
	public int getGapX()
	{
		return m_gapX;
	}

	/**
	 * 获取字符之间的纵向间隔
	 * 
	 * @return 间隔
	 */
	public int getGapY()
	{
		return m_gapY;
	}

	/**
	 * 设置字符之间的纵向间隔
	 * 
	 * @param gapX
	 *            字符之间的横向间隔
	 */
	public void setGapX(int gapX)
	{
		m_gapX = gapX;
	}

	/**
	 * 设置字符之间的纵向间隔
	 * 
	 * @param gapY
	 *            字符之间的纵向间隔
	 */
	public void setGapY(int gapY)
	{
		m_gapY = gapY;
	}

	/**
	 * 获取当前的映射字符串
	 * 
	 * @return 映射字符串
	 */
	public String getChars()
	{
		return m_charTable;
	}

	/**
	 * 获取映射字符串长度
	 * 
	 * @return 映射字符串长度
	 */
	public int getCharCount()
	{
		if (m_charTable == null)
		{
			return 0;
		}
		return m_charTable.length();
	}

	/**
	 * 获得对应字符的宽度
	 * 
	 * @param id
	 *            字符ID
	 * @return 宽度
	 */
	public abstract int getCharW(int id);

	/**
	 * 获得最大的字符宽度
	 * 
	 * @return 最大的字符宽度
	 */
	public abstract int getCharWMax();

	/**
	 * 获得对应字符所在的位置X坐标
	 * 
	 * @param id
	 *            字符ID
	 * @return 所在的位置X坐标
	 */
	public abstract int getCharX(int id);

	/**
	 * 获得对应字符所在的位置Y坐标
	 * 
	 * @param id
	 *            字符ID
	 * @return 所在的位置Y坐标
	 */
	public abstract int getCharY(int id);

	/**
	 * 获得用于绘制字符的图片
	 * 
	 * @param id
	 *            字符图片id
	 * @return 字符图片
	 */
	public abstract C2D_Image getImage(int id);

	/**
	 * 更新每个字符所在的位置
	 */
	protected void updatePositions()
	{
		if (m_chars != null)
		{
			for (int i = 0; i < m_chars.length; i++)
			{
				if (m_chars[i] != null)
				{
					m_chars[i].doRelease(this);
					m_chars[i] = null;
				}
			}
			m_chars = null;
		}
		m_chars = new C2D_ImageClip[m_charTable.length()];
		short x = (short) m_TextX;
		short y = (short) m_TextY;
		for (int i = 0; i < m_chars.length; i++)
		{
			m_chars[i] = new C2D_ImageClip(m_image);
			m_chars[i].transHadler(this);
			m_chars[i].setContentRect(x, y, getCharW(i), m_charH);
			m_chars[i].setShowSize(getCharW(i), m_charH);
			x += getCharW(i);
			if (x + getCharW(i) - 1 >= m_image.pixelWidth())
			{
				x = (short) m_TextX;
				y += m_charH;
			}
		}
	}

	/**
	 * 获得单个字符的贴图切块
	 * 
	 * @param id
	 *            字符ID
	 * @return 单个字符
	 */
	public C2D_ImageClip getChar(int id)
	{
		if (m_chars != null && id >= 0 && id < m_chars.length)
		{
			return m_chars[id];
		}
		return null;
	}

	public void onRelease()
	{
		m_charTable = null;
		if (m_image != null)
		{
			m_image.doRelease();
			m_image = null;
		}

		if (m_chars != null)
		{
			for (int i = 0; i < m_chars.length; i++)
			{
				if (m_chars[i] != null)
				{
					m_chars[i].doRelease(this);
					m_chars[i] = null;
				}
			}
			m_chars = null;
		}

	}
}