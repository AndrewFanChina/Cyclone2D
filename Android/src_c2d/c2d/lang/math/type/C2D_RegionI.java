package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * 整型区域类
 * @author AndrewFan
 *
 */
public class C2D_RegionI
{
	/**
	 * 左边尺寸
	 */
	public int m_l;
	/**
	 * 上边尺寸
	 */
	public int m_t;
	/**
	 * 右边尺寸
	 */
	public int m_r;
	/**
	 * 下边尺寸
	 */
	public int m_b;

	public C2D_RegionI()
	{
	}
	public C2D_RegionI(int l,int t,int r,int b)
	{
		setValue(l,t,r,b);
	}
	
	/**
	 * 设置矩形的数值
	 * @param l 左
	 * @param t 上
	 * @param r 右
	 * @param b 下
	 */
	public void setValue(int l,int t,int r,int b)
	{
		m_l=l;
		m_t=t;
		m_r=r;
		m_b=b;
	}
	/**
	 * 设置矩形的数值跟指定的矩形相同
	 * @param rect 指定的矩形
	 */
	public void setValue(C2D_RegionI rect)
	{
		m_l=rect.m_l;
		m_t=rect.m_t;
		m_r=rect.m_r;
		m_b=rect.m_b;
	}
	/**
	 * 设置区域水平尺寸
	 * @param l 左
	 * @param r 右
	 */
	public void setSizeX(int l,int r)
	{
		m_l=l;
		m_r=r;
	}
	/**
	 * 设置区域垂直尺寸
	 * @param t 上
	 * @param b 下
	 */
	public void setSizeY(int t,int b)
	{
		m_t=t;
		m_b=b;
	}
	/**
	 * 增加x和y方向偏移
	 * @param x x偏移
	 * @param y y偏移
	 */
	public void addOffset(int x,int y)
	{
		m_l+=x;
		m_r+=x;
		m_t+=y;
		m_b+=y;
	}
	/**
	 * 判断是否跟另外一个区域相碰
	 * @param another 另外一个区域
	 * @return 是否相碰
	 */
	public boolean crossWith(C2D_RegionI another)
	{
		if(another==null)
		{
			return false;
		}
		if(another.m_l>m_r||
				another.m_r<m_l||
				another.m_t>m_b||
				another.m_b<m_t)
		{
			return false;
		}
		return true;
	}
	/** 交叠区域缓冲 */
	private static C2D_RegionI m_crossedRegion=new C2D_RegionI();
	/**
	 * 计算跟另外一个区域的交叠区域
	 * @param another 另外一个区域
	 * @return 交叠区域，不交叠则返回null
	 */
	public C2D_RegionI crossedRegion(C2D_RegionI another)
	{
		if(another==null)
		{
			return null;
		}
		if(another.m_l>m_r||
			another.m_r<m_l||
			another.m_t>m_b||
			another.m_b<m_t)
		{
			return null;
		}
		m_crossedRegion.m_l=C2D_Math.max(another.m_l, m_l);
		m_crossedRegion.m_r=C2D_Math.min(another.m_r, m_r);
		m_crossedRegion.m_t=C2D_Math.max(another.m_t, m_t);
		m_crossedRegion.m_b=C2D_Math.min(another.m_b, m_b);
		return m_crossedRegion;
	}
}
