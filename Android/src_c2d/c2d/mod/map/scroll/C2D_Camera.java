package c2d.mod.map.scroll;

import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_SizeI;

public class C2D_Camera
{
	/** 相机中心点位置 */
	private C2D_PointF m_pos = new C2D_PointF();
	/** 相机中心点位置(整数形式) */
	private C2D_PointF m_posLink = new C2D_PointF();
	/** 相机尺寸 */
	private C2D_SizeI m_size = new C2D_SizeI();
	/** 卷轴地图句柄 */
	private C2D_ScrollMap m_map;

	/** 最大震动幅度 */
	private float m_shakeMax = 0;
	/** 当前震动幅度 */
	private float m_shakeCur = 0;
	/** 震动衰减 */
	private float m_decay = 0;
	/** 衰减后剩余值 */
	private float m_decayLeft = 0;

	public C2D_Camera(C2D_ScrollMap scrollMap)
	{
		m_map = scrollMap;
	}

	/**
	 * 设置到指定的位置
	 * 
	 * @param x
	 *            坐标x
	 * @param y
	 *            坐标y
	 */
	public void setToPos(int x, int y)
	{
		m_pos.setValue(x, y);
		m_posLink.setValue(x, y);
	}

	/**
	 * 获取当前所在的位置
	 * 
	 * @return 所在的位置
	 */
	public C2D_PointF getPos()
	{
		return m_pos;
	}

	/**
	 * 获取当前所在的位置
	 * 
	 * @return 所在的位置
	 */
	public C2D_PointF getPosLink()
	{
		return m_posLink;
	}

	/**
	 * 设置摄像机镜头的大小，并不是地图世界大小
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void setSize(int width, int height)
	{
		m_size.setValue(width, height);
	}

	/**
	 * 获取当前摄像区域大小
	 * 
	 * @return 摄像区域大小
	 */
	public C2D_SizeI getSize()
	{
		return m_size;
	}

	/**
	 * 跟随到某个位置
	 * 
	 * @param x
	 *            目标坐标x
	 * @param y
	 *            目标坐标y
	 * @param world
	 *            世界尺寸
	 */
	public void tracePos(int x, int y)
	{
		tracePos(x, y, 50);
	}

	/**
	 * 跟随到某个位置
	 * 
	 * @param x
	 *            目标坐标x
	 * @param y
	 *            目标坐标y
	 * @param percent
	 *            跟随速度，改变当前距离的百分比
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
	 * 开始震动
	 * 
	 * @param shake
	 *            振幅
	 * @param decrease
	 *            衰减
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
	 * 跟随值
	 * 
	 * @param dest
	 *            目标值
	 * @param cur
	 *            当前值
	 * @param speedPct
	 *            跟随速度，改变当前距离的百分比
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
	 * 限制值[最小值,最大值]
	 * 
	 * @param min
	 *            目标最小值
	 * @param max
	 *            目标最大值
	 * @param cur
	 *            当前值
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
