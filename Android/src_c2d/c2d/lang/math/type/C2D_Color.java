package c2d.lang.math.type;


public class C2D_Color
{
	/** 整个颜色 */
	private int m_color;
	/** 每个分量 */
	private int m_a, m_r, m_g, m_b;

	/**
	 * 构造函数，默认颜色为0xFF000000
	 */
	public C2D_Color()
	{
		setColor(0xFF000000);
	}

	/**
	 * 构造函数，指定rgb分量，alpha设定为0xFF
	 */
	public C2D_Color(int rgb)
	{
		setColor(0xFF000000 | rgb);
	}

	/**
	 * 构造函数，默认Alpha分量为0xFF，指定的r、g、b
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public C2D_Color(int r, int g, int b)
	{
		m_a = 0xFF;
		m_r = r & 0xFF;
		m_g = g & 0xFF;
		m_b = b & 0xFF;
		updateWhole();
	}

	/**
	 * 构造函数，指定的a、r、g、b
	 * 
	 * @param a
	 * @param r
	 * @param g
	 * @param b
	 */
	public C2D_Color(int a, int r, int g, int b)
	{
		m_a = a & 0xFF;
		m_r = r & 0xFF;
		m_g = g & 0xFF;
		m_b = b & 0xFF;
		updateWhole();
	}

	/**
	 * 创建新的RGB颜色
	 * 
	 * @param color
	 *            RGB颜色数值
	 * @return 颜色对象
	 */
	public static C2D_Color makeRGB(int color)
	{
		return new C2D_Color(color);
	}

	/**
	 * 创建新的ARGB颜色
	 * 
	 * @param color
	 *            ARGB颜色数值
	 * @return 颜色对象
	 */
	public static C2D_Color makeARGB(int color)
	{
		C2D_Color c = new C2D_Color();
		c.m_color = color;
		c.updateComponet();
		return c;
	}

	/**
	 * 获取颜色成ARGB型整数数值
	 * 
	 * @return 颜色ARGB型整数数值
	 */
	public int getColor()
	{
		return m_color;
	}

	/**
	 * 设置颜色
	 * 
	 * @param color
	 */
	public void setColor(int color)
	{
		this.m_color = color;
		updateComponet();
	}

	/**
	 * 设置颜色
	 * 
	 * @param color
	 */
	public void setColor(C2D_Color color)
	{
		if (color != null)
		{
			this.m_color = color.getColor();
			updateComponet();
		}
	}

	/**
	 * 获取Alpha分量
	 * 
	 * @return Alpha分量
	 */
	public int getA()
	{
		return m_a;
	}

	/**
	 * 设置Alpha分量
	 * 
	 * @param a
	 *            Alpha分量
	 */
	public void setA(int a)
	{
		this.m_a = a & 0xFF;
		updateWhole();
	}

	/**
	 * 获取Red分量
	 * 
	 * @return Red分量
	 */
	public int getR()
	{
		return m_r;
	}

	/**
	 * 设置Red分量
	 * 
	 * @param r
	 *            Red分量
	 */
	public void setR(int r)
	{
		this.m_r = r & 0xFF;
		updateWhole();
	}

	/**
	 * 获取Green分量
	 * 
	 * @return Green分量
	 */
	public int getG()
	{
		return m_g;
	}

	/**
	 * 设置Green分量
	 * 
	 * @param g
	 *            Green分量
	 */
	public void setG(int g)
	{
		this.m_g = g & 0xFF;
		updateWhole();
	}

	/**
	 * 获取Blue分量
	 * 
	 * @return Blue分量
	 */
	public int getB()
	{
		return m_b;
	}

	/**
	 * 设置Blue分量
	 * 
	 * @param b
	 *            Blue分量
	 */
	public void setB(int b)
	{
		this.m_b = b & 0xFF;
		updateWhole();
	}

	/**
	 * 根据当前的整体颜色更新颜色分量
	 */
	private void updateComponet()
	{
		m_a = (m_color >> 24) & 0xFF;
		m_r = (m_color >> 16) & 0xFF;
		m_g = (m_color >> 8) & 0xFF;
		m_b = (m_color >> 0) & 0xFF;
	}

	/**
	 * 根据当前的颜色分量更新整体颜色
	 */
	private void updateWhole()
	{
		m_color = ((m_a) << 24) | ((m_r) << 16) | ((m_g) << 8) | ((m_b) << 0);
	}

	/**
	 * 判断是否跟别的颜色相等
	 * 
	 * @return 是否相等
	 */
	public boolean equals(C2D_Color color)
	{
		if (color == null)
		{
			return false;
		}
		return (color.m_color == m_color);
	}

	private static float forZoom = 1;

	/**
	 * 缩放指定颜色的RGB分量，Alpha分量不变
	 * 
	 * @param color
	 *            指定颜色
	 * @param zoom
	 *            缩放倍率
	 * @return 结果颜色
	 */
	public static int Zoom_Color(int color, float zoom)
	{
		if (zoom != 0)
		{
			int a = (color >> 24) & 0xFF;
			int r = (color >> 16) & 0xFF;
			r = (int)(r*forZoom);
			if (r < 0)
			{
				r = 0;
			}
			if (r > 255)
			{
				r = 255;
			}
			int g = (color >> 8) & 0xFF;
			g =  (int)(g*forZoom);
			if (g < 0)
			{
				g = 0;
			}
			if (g > 255)
			{
				g = 255;
			}
			int b = (color >> 0) & 0xFF;
			b =  (int)(b*forZoom);
			if (b < 0)
			{
				b = 0;
			}
			if (b > 255)
			{
				b = 255;
			}
			color = ((a) << 24) | ((r) << 16) | ((g) << 8) | ((b) << 0);
		}
		return color;
	}

	/**
	 * 缩放指定颜色的Alpha分量，RGB分量不变
	 * 
	 * @param color
	 *            指定颜色
	 * @param zoom
	 *            缩放倍率
	 * @return 结果颜色
	 */
	public static int Zoom_Alpha(int color, float zoom)
	{
		int a = (color >> 24) & 0xFF;
		a =  (int)(a*forZoom);
		if (a < 0)
		{
			a = 0;
		}
		if (a > 255)
		{
			a = 255;
		}
		color = (a << 24) | (color * 0x00FFFFFF);
		return color;
	}
}
