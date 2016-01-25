package c2d.lang.math.type;


public class C2D_Color
{
	/** ������ɫ */
	private int m_color;
	/** ÿ������ */
	private int m_a, m_r, m_g, m_b;

	/**
	 * ���캯����Ĭ����ɫΪ0xFF000000
	 */
	public C2D_Color()
	{
		setColor(0xFF000000);
	}

	/**
	 * ���캯����ָ��rgb������alpha�趨Ϊ0xFF
	 */
	public C2D_Color(int rgb)
	{
		setColor(0xFF000000 | rgb);
	}

	/**
	 * ���캯����Ĭ��Alpha����Ϊ0xFF��ָ����r��g��b
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
	 * ���캯����ָ����a��r��g��b
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
	 * �����µ�RGB��ɫ
	 * 
	 * @param color
	 *            RGB��ɫ��ֵ
	 * @return ��ɫ����
	 */
	public static C2D_Color makeRGB(int color)
	{
		return new C2D_Color(color);
	}

	/**
	 * �����µ�ARGB��ɫ
	 * 
	 * @param color
	 *            ARGB��ɫ��ֵ
	 * @return ��ɫ����
	 */
	public static C2D_Color makeARGB(int color)
	{
		C2D_Color c = new C2D_Color();
		c.m_color = color;
		c.updateComponet();
		return c;
	}

	/**
	 * ��ȡ��ɫ��ARGB��������ֵ
	 * 
	 * @return ��ɫARGB��������ֵ
	 */
	public int getColor()
	{
		return m_color;
	}

	/**
	 * ������ɫ
	 * 
	 * @param color
	 */
	public void setColor(int color)
	{
		this.m_color = color;
		updateComponet();
	}

	/**
	 * ������ɫ
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
	 * ��ȡAlpha����
	 * 
	 * @return Alpha����
	 */
	public int getA()
	{
		return m_a;
	}

	/**
	 * ����Alpha����
	 * 
	 * @param a
	 *            Alpha����
	 */
	public void setA(int a)
	{
		this.m_a = a & 0xFF;
		updateWhole();
	}

	/**
	 * ��ȡRed����
	 * 
	 * @return Red����
	 */
	public int getR()
	{
		return m_r;
	}

	/**
	 * ����Red����
	 * 
	 * @param r
	 *            Red����
	 */
	public void setR(int r)
	{
		this.m_r = r & 0xFF;
		updateWhole();
	}

	/**
	 * ��ȡGreen����
	 * 
	 * @return Green����
	 */
	public int getG()
	{
		return m_g;
	}

	/**
	 * ����Green����
	 * 
	 * @param g
	 *            Green����
	 */
	public void setG(int g)
	{
		this.m_g = g & 0xFF;
		updateWhole();
	}

	/**
	 * ��ȡBlue����
	 * 
	 * @return Blue����
	 */
	public int getB()
	{
		return m_b;
	}

	/**
	 * ����Blue����
	 * 
	 * @param b
	 *            Blue����
	 */
	public void setB(int b)
	{
		this.m_b = b & 0xFF;
		updateWhole();
	}

	/**
	 * ���ݵ�ǰ��������ɫ������ɫ����
	 */
	private void updateComponet()
	{
		m_a = (m_color >> 24) & 0xFF;
		m_r = (m_color >> 16) & 0xFF;
		m_g = (m_color >> 8) & 0xFF;
		m_b = (m_color >> 0) & 0xFF;
	}

	/**
	 * ���ݵ�ǰ����ɫ��������������ɫ
	 */
	private void updateWhole()
	{
		m_color = ((m_a) << 24) | ((m_r) << 16) | ((m_g) << 8) | ((m_b) << 0);
	}

	/**
	 * �ж��Ƿ�������ɫ���
	 * 
	 * @return �Ƿ����
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
	 * ����ָ����ɫ��RGB������Alpha��������
	 * 
	 * @param color
	 *            ָ����ɫ
	 * @param zoom
	 *            ���ű���
	 * @return �����ɫ
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
	 * ����ָ����ɫ��Alpha������RGB��������
	 * 
	 * @param color
	 *            ָ����ɫ
	 * @param zoom
	 *            ���ű���
	 * @return �����ɫ
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
