package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * ����������
 * @author AndrewFan
 *
 */
public class C2D_RegionI
{
	/**
	 * ��߳ߴ�
	 */
	public int m_l;
	/**
	 * �ϱ߳ߴ�
	 */
	public int m_t;
	/**
	 * �ұ߳ߴ�
	 */
	public int m_r;
	/**
	 * �±߳ߴ�
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
	 * ���þ��ε���ֵ
	 * @param l ��
	 * @param t ��
	 * @param r ��
	 * @param b ��
	 */
	public void setValue(int l,int t,int r,int b)
	{
		m_l=l;
		m_t=t;
		m_r=r;
		m_b=b;
	}
	/**
	 * ���þ��ε���ֵ��ָ���ľ�����ͬ
	 * @param rect ָ���ľ���
	 */
	public void setValue(C2D_RegionI rect)
	{
		m_l=rect.m_l;
		m_t=rect.m_t;
		m_r=rect.m_r;
		m_b=rect.m_b;
	}
	/**
	 * ��������ˮƽ�ߴ�
	 * @param l ��
	 * @param r ��
	 */
	public void setSizeX(int l,int r)
	{
		m_l=l;
		m_r=r;
	}
	/**
	 * ��������ֱ�ߴ�
	 * @param t ��
	 * @param b ��
	 */
	public void setSizeY(int t,int b)
	{
		m_t=t;
		m_b=b;
	}
	/**
	 * ����x��y����ƫ��
	 * @param x xƫ��
	 * @param y yƫ��
	 */
	public void addOffset(int x,int y)
	{
		m_l+=x;
		m_r+=x;
		m_t+=y;
		m_b+=y;
	}
	/**
	 * �ж��Ƿ������һ����������
	 * @param another ����һ������
	 * @return �Ƿ�����
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
	/** �������򻺳� */
	private static C2D_RegionI m_crossedRegion=new C2D_RegionI();
	/**
	 * ���������һ������Ľ�������
	 * @param another ����һ������
	 * @return �������򣬲������򷵻�null
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
