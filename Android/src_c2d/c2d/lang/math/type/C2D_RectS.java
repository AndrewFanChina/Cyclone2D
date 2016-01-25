package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * 整型矩形类
 * @author AndrewFan
 *
 */
public class C2D_RectS
{
	/**
	 * x坐标
	 */
	public short m_x;
	/**
	 * y坐标
	 */
	public short m_y;
	/**
	 * 宽度
	 */
	public short m_width;
	/**
	 * 高度
	 */
	public short m_height;
	
	public C2D_RectS()
	{
	}
	
	public C2D_RectS(short x,short y,short w,short h)
	{
		setValue(x,y,w,h);
	}
	public C2D_RectS(int x,int y,int w,int h)
	{
		setValue((short)x,(short)y,(short)w,(short)h);
	}
	/**
	 * 设置矩形的数值跟指定的矩形相同
	 * @param rectR1 指定的矩形
	 */
	public void setValue(C2D_RectS rectR1)
	{
		m_x=rectR1.m_x;
		m_y=rectR1.m_y;
		m_width=rectR1.m_width;
		m_height=rectR1.m_height;
	}
	/**
	 * 设置矩形的数值跟指定的矩形相同
	 * @param rectR1 指定的矩形
	 */
	public void setValue(C2D_RectI rectR1)
	{
		m_x=(short)rectR1.m_x;
		m_y=(short)rectR1.m_y;
		m_width=(short)rectR1.m_width;
		m_height=(short)rectR1.m_height;
	}
	/**
	 * 设置矩形的数值
	 * @param x 坐标X
	 * @param y 坐标Y
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setValue(short x,short y,short width,short height)
	{
		m_x=x;
		m_y=y;
		m_width=width;
		m_height=height;
	}
	/**
	 * 设置矩形的数值
	 * @param x 坐标X
	 * @param y 坐标Y
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setValue(int x,int y,int width,int height)
	{
		m_x=(short)x;
		m_y=(short)y;
		m_width=(short)width;
		m_height=(short)height;
	}
	/**
	 * 设置矩形的位置
	 * @param x 坐标X
	 * @param y 坐标Y
	 */
	public void setPos(short x,short y)
	{
		m_x=x;
		m_y=y;
	}
	/**
	 * 设置矩形的尺寸
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setSize(short width,short height)
	{
		m_width=width;
		m_height=height;
	}
	/**
	 * 设置矩形的尺寸
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setSize(int width,int height)
	{
		m_width=(short)width;
		m_height=(short)height;
	}
	/**
	 * 判断是否跟另外一个矩形相碰
	 * @param another 另外一个矩形
	 * @return 是否相碰
	 */
	public boolean crossWith(C2D_RectS another)
	{
		if(another==null)
		{
			return false;
		}
		if(another.m_x>m_x+m_width||
				another.m_x+another.m_width<m_x||
				another.m_y>m_y+m_height||
				another.m_y+another.m_height<m_y)
		{
			return false;
		}
		return true;
	}

	public C2D_RectS cloneSelf()
	{
		// TODO Auto-generated method stub
		return new C2D_RectS(m_x,m_y,m_width,m_height);
	}
	/**
	 * 计算当前矩形和指定矩形的交叉值，并作为当前矩形结果
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setCross(int x, int y, int w, int h)
	{
		int xL=C2D_Math.max(x, m_x);
		int yT=C2D_Math.max(y, m_y);
		int xR=C2D_Math.min(x+w, m_x+m_width);
		int yB=C2D_Math.min(y+h, m_y+m_height);
		m_x=(short)xL;
		m_y=(short)yT;
		m_width=(short)(xR-xL);
		if(m_width<0)
		{
			m_width=0;
		}
		m_height=(short)(yB-yT);
		if(m_height<0)
		{
			m_height=0;
		}
	}
	/**
	 * 计算当前矩形和指定矩形的交叉值，并作为当前矩形结果
	 * @param rect 交叉矩形
	 */
	public void setCross(C2D_RectS rect)
	{
		if(rect==null)
		{
			return;
		}
		int xL=C2D_Math.max(rect.m_x, m_x);
		int yT=C2D_Math.max(rect.m_y, m_y);
		int xR=C2D_Math.min(rect.m_x+rect.m_width, m_x+m_width);
		int yB=C2D_Math.min(rect.m_y+rect.m_height, m_y+m_height);
		m_x=(short)xL;
		m_y=(short)yT;
		m_width=(short)(xR-xL);
		if(m_width<0)
		{
			m_width=0;
		}
		m_height=(short)(yB-yT);
		if(m_height<0)
		{
			m_height=0;
		}
	}
}
