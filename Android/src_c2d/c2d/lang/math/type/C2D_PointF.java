package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * �����Ͷ�ά����
 * @author AndrewFan
 *
 */
public class C2D_PointF
{
	public float m_x, m_y;

	private static C2D_PointFBuffer m_tempBufs = new C2D_PointFBuffer(40);
	public static C2D_PointF zero()
	{
		return new C2D_PointF(0, 0);
	}

	public C2D_PointF()
	{
		this(0, 0);
	}

	public static C2D_PointF make(int x, int y)
	{
		return new C2D_PointF(x, y);
	}

	public static C2D_PointF make(float x, float y)
	{
		return new C2D_PointF(x, y);
	}

	public C2D_PointF(int x, int y)
	{
		m_x=x;
		m_y=y;
	}

	public C2D_PointF(float x, float y)
	{
		m_x = x;
		m_y = y;
	}

	public C2D_PointF setValue(int x, int y)
	{
		m_x=x;
		m_y=y;
		return this;
	}

	public C2D_PointF setValue(float x, float y)
	{
		m_x = x;
		m_y = y;
		return this;
	}

	public C2D_PointF setValue(C2D_PointF point)
	{
		if (point != null)
		{
			m_x = point.m_x;
			m_y = point.m_y;
		}
		return this;
	}
	public C2D_PointF setToBuffer(float x, float y)
	{
		C2D_PointF next = m_tempBufs.next();
		next.m_x=x;
		next.m_y=y;
		return next;
	}
	/**
	 * �����Ƿ���ָ��������Χ��
	 * 
	 * @param rect
	 *            ָ��������
	 * @return �Ƿ���ָ��������Χ��
	 */
	public boolean inRegion(C2D_RectI rect)
	{
		if (rect == null)
		{
			return false;
		}
		return m_x >= rect.m_x && m_x <= rect.m_x + rect.m_width && m_y >= rect.m_y && m_y <= rect.m_y + rect.m_height;
	}

	/**
	 * �����Ƿ���ָ��������Χ��
	 * 
	 * @param rect
	 *            ָ��������
	 * @return �Ƿ���ָ��������Χ��
	 */
	public boolean inRegion(C2D_RectF rect)
	{
		if (rect == null)
		{
			return false;
		}
		return m_x >= rect.m_x && m_x <= rect.m_x + rect.m_width && m_y >= rect.m_y && m_y <= rect.m_y + rect.m_height;
	}

	public String toString()
	{
		return "(" + m_x + ", " + m_y + ")";
	}

	/**
	 * ��������һ������ľ����ƽ��
	 * 
	 * @return float ����ƽ��
	 */
	public float lengthSQTo(C2D_PointF v)
	{
		float t = (m_x - v.m_x) * (m_x - v.m_x) + (m_y - v.m_y) * (m_y - v.m_y);
		return t;
	}

	public static boolean equalToPoint(C2D_PointF p1, C2D_PointF p2)
	{
		return p1.m_x == p2.m_x && p1.m_y == p2.m_y;
	}

	/**
	 * ȡ��
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Neg()
	{
		return setToBuffer(-m_x, -m_y);
	}

	/**
	 * ������������ۼ�ֵ
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Add(final C2D_PointF v)
	{
		return setToBuffer(m_x+v.m_x, m_y+v.m_y);
	}

	/**
	 * �������
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Sub(final C2D_PointF v)
	{
		return setToBuffer(m_x-v.m_x, m_y-v.m_y);
	}

	/**
	 * �˷�����
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Mult(final float s)
	{
		return setToBuffer(m_x * s, m_y * s);
	}

	/**
	 * �����е�
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Midpoint(final C2D_PointF v2)
	{
		return Add(v2).Mult(0.5f);
	}

	/**
	 * ������
	 * 
	 * @return float
	 */
	public float Dot(final C2D_PointF v2)
	{
		return m_x * v2.m_x + m_y * v2.m_y;
	}

	/**
	 * ������
	 * 
	 * @return float
	 */
	public float Cross(final C2D_PointF v2)
	{
		return m_x * v2.m_y - m_y * v2.m_x;
	}

	/**
	 * ��ʱ����ת90��
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Perp()
	{
		return setToBuffer(-m_y, m_x);
	}

	/**
	 * ˳ʱ����ת90��
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF RPerp()
	{
		return setToBuffer(m_y, -m_x);
	}

	/**
	 * ����v1��v2�ϵ�ͶӰ
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Project(final C2D_PointF v2)
	{
		return v2.Mult(this.Dot(v2) / v2.Dot(v2));
	}

	/**
	 * ��ת�������㣨����
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Rotate(final C2D_PointF v2)
	{
		return setToBuffer(m_x * v2.m_x - m_y * v2.m_y, m_x * v2.m_y + m_y * v2.m_x);
	}

	/**
	 * ������ת�������㣨����
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Unrotate(final C2D_PointF v2)
	{
		return setToBuffer(m_x * v2.m_x + m_y * v2.m_y, m_y * v2.m_x - m_x * v2.m_y);
	}

	/**
	 * ����ʸ�����ȵ�ƽ��
	 * 
	 * @return float
	 */
	public float LengthSQ()
	{
		return this.Dot(this);
	}

	/**
	 * ����ʸ������
	 * 
	 * @return float
	 */
	public float Length(final C2D_PointF v)
	{
		return (float) Math.sqrt(LengthSQ());
	}

	/**
	 * ������������
	 * 
	 * @return float
	 */
	public float Distance(final C2D_PointF v2)
	{
		return Length(Sub(v2));
	}

	/**
	 * ��ʸ����λ��������ɳ���Ϊ1��ʸ��
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF Normalize()
	{
		return this.Mult(1.0f / Length(this));
	}

	/**
	 * ������ֵת���ɵ�λʸ��
	 * 
	 * @return C2D_PointF
	 */
	public C2D_PointF ForAngle(final float a)
	{
		return setToBuffer((float) Math.cos(a), (float) Math.sin(a));
	}

	/**
	 * ��ʸ��ת���ɻ���ֵ
	 * 
	 * @return float
	 */
	public float ToAngle()
	{
		return (float) C2D_Math.actTan(m_x, m_y);
	}

}
