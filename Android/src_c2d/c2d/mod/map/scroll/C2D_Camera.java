package c2d.mod.map.scroll;

import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_SizeI;

public class C2D_Camera
{
	/** ������ĵ�λ�� */
	private C2D_PointF m_pos = new C2D_PointF();
	/** ������ĵ�λ��(������ʽ) */
	private C2D_PointF m_posLink = new C2D_PointF();
	/** ����ߴ� */
	private C2D_SizeI m_size = new C2D_SizeI();
	/** �����ͼ��� */
	private C2D_ScrollMap m_map;

	/** ����𶯷��� */
	private float m_shakeMax = 0;
	/** ��ǰ�𶯷��� */
	private float m_shakeCur = 0;
	/** ��˥�� */
	private float m_decay = 0;
	/** ˥����ʣ��ֵ */
	private float m_decayLeft = 0;

	public C2D_Camera(C2D_ScrollMap scrollMap)
	{
		m_map = scrollMap;
	}

	/**
	 * ���õ�ָ����λ��
	 * 
	 * @param x
	 *            ����x
	 * @param y
	 *            ����y
	 */
	public void setToPos(int x, int y)
	{
		m_pos.setValue(x, y);
		m_posLink.setValue(x, y);
	}

	/**
	 * ��ȡ��ǰ���ڵ�λ��
	 * 
	 * @return ���ڵ�λ��
	 */
	public C2D_PointF getPos()
	{
		return m_pos;
	}

	/**
	 * ��ȡ��ǰ���ڵ�λ��
	 * 
	 * @return ���ڵ�λ��
	 */
	public C2D_PointF getPosLink()
	{
		return m_posLink;
	}

	/**
	 * �����������ͷ�Ĵ�С�������ǵ�ͼ�����С
	 * 
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public void setSize(int width, int height)
	{
		m_size.setValue(width, height);
	}

	/**
	 * ��ȡ��ǰ���������С
	 * 
	 * @return ���������С
	 */
	public C2D_SizeI getSize()
	{
		return m_size;
	}

	/**
	 * ���浽ĳ��λ��
	 * 
	 * @param x
	 *            Ŀ������x
	 * @param y
	 *            Ŀ������y
	 * @param world
	 *            ����ߴ�
	 */
	public void tracePos(int x, int y)
	{
		tracePos(x, y, 50);
	}

	/**
	 * ���浽ĳ��λ��
	 * 
	 * @param x
	 *            Ŀ������x
	 * @param y
	 *            Ŀ������y
	 * @param percent
	 *            �����ٶȣ��ı䵱ǰ����İٷֱ�
	 */
	public void tracePos(int x, int y, int speedPct)
	{
		shakeLogic();
		traceValue(x, m_pos.m_x, speedPct);
		traceValue(y, m_pos.m_y, speedPct);
		int wHalf = m_size.m_width >> 1;
		int hHalf = m_size.m_height >> 1;
		limitValue(wHalf, m_map.getWidth() - wHalf, m_pos.m_x);
		limitValue(hHalf, m_map.getHeight() - hHalf, m_pos.m_y);
		m_posLink.setValue(m_pos.m_x, m_pos.m_y + m_shakeCur);
		m_map.setPosTo(wHalf - m_posLink.m_x, hHalf - m_posLink.m_y);
	}

	private void shakeLogic()
	{
		if (m_decay == 0)
		{
			return;
		}
		if (m_decayLeft < m_decay)
		{
			m_decayLeft = 1;
			m_decay = 0;
			return;
		}
		m_decayLeft *= (1 - m_decay);
		int flag = m_shakeCur > 0 ? -1 : 1;
		m_shakeCur = m_shakeMax * m_decayLeft * flag;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param shake
	 *            ���
	 * @param decrease
	 *            ˥��
	 */
	public void startShake(float shake, float decrease)
	{
		if (shake != 0 && decrease != 0)
		{
			m_shakeMax = (shake);
			m_shakeCur = (shake);
			m_decay = C2D_Math.abs(decrease);
			m_decayLeft = (1);
		}
	}

	/**
	 * ����ֵ
	 * 
	 * @param dest
	 *            Ŀ��ֵ
	 * @param cur
	 *            ��ǰֵ
	 * @param speedPct
	 *            �����ٶȣ��ı䵱ǰ����İٷֱ�
	 */
	private void traceValue(float dest, float cur, int speedPct)
	{
		speedPct = C2D_Math.limitNumber(speedPct, 1, 100);
		float t = (dest - cur) * speedPct / 100;
		if (C2D_Math.abs(t) < 1)
		{
			cur = dest;
		}
		else
		{
			cur += t;
		}
	}

	/**
	 * ����ֵ[��Сֵ,���ֵ]
	 * 
	 * @param min
	 *            Ŀ����Сֵ
	 * @param max
	 *            Ŀ�����ֵ
	 * @param cur
	 *            ��ǰֵ
	 */
	private void limitValue(float min, float max, float cur)
	{
		if (cur < min)
		{
			cur = min;
		}
		else if (cur > max)
		{
			cur = max;
		}
	}

	public float getLeft()
	{
		return m_posLink.m_x - m_size.m_width / 2;
	}

	public float getRight()
	{
		return m_posLink.m_x + m_size.m_width / 2;
	}

	public float getTop()
	{
		return m_posLink.m_y - m_size.m_height / 2;
	}

	public float getBottom()
	{
		return m_posLink.m_y + m_size.m_height / 2;
	}
}
