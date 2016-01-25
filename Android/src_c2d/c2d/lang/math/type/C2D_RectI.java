package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * 整型矩形类
 * @author AndrewFan
 *
 */
public class C2D_RectI
{
	/**
	 * x坐标
	 */
	public int m_x;
	/**
	 * y坐标
	 */
	public int m_y;
	/**
	 * 宽度
	 */
	public int m_width;
	/**
	 * 高度
	 */
	public int m_height;

	public C2D_RectI()
	{
	}
	public C2D_RectI(int x,int y,int w,int h)
	{
		setValue(x,y,w,h);
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
		m_x=x;
		m_y=y;
		m_width=width;
		m_height=height;
	}
	/**
	 * 设置矩形的数值跟指定的矩形相同
	 * @param rect 指定的矩形
	 */
	public void setValue(C2D_RectI rect)
	{
		m_x=rect.m_x;
		m_y=rect.m_y;
		m_width=rect.m_width;
		m_height=rect.m_height;
	}
	/**
	 * 设置矩形的位置
	 * @param x 坐标X
	 * @param y 坐标Y
	 */
	public void setPos(int x,int y)
	{
		m_x=x;
		m_y=y;
	}
	/**
	 * 设置矩形的尺寸
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setSize(int width,int height)
	{
		m_width=width;
		m_height=height;
	}
	/**
	 * 判断是否跟另外一个矩形相碰
	 * @param another 另外一个矩形
	 * @return 是否相碰
	 */
	public boolean crossWith(C2D_RectI another)
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
	/**
	 * 按照当前矩形的顶点划分成构成的9个象限，按照“#”状态划分，
	 * 从左到右边，从上到下，逐行分别设定为0-8的编号。本函数将
	 * 获取指定的顶点所在的象限编号。
	 * @param x
	 * @param y
	 * @return 象限编号
	 */
	public int getQuadrant(int x,int y)
	{
		int r=m_x+m_width;
		int b=m_y+m_height;
		int q=0;
		if(x>=r)
		{
			q+=2;
		}
		else if(x>=m_x)
		{
			q+=1;
		}
		
		if(y>=b)
		{
			q+=6;
		}
		else if(y>=m_y)
		{
			q+=3;
		}

		return q;
	}
	/**
	 * 测试判断当前矩形跟指定线段的碰撞
	 * @param x1 端点1的x坐标
	 * @param y1 端点1的y坐标
	 * @param x2 端点2的x坐标
	 * @param y2 端点2的y坐标
	 * @return 是否发生碰撞
	 */
	public boolean testCrossWith(int x1,int y1,int x2,int y2)
	{
		boolean res = crossWith(x1,y1,x2,y2);
		System.out.print("rect["+m_x+","+m_y+","+m_width+","+m_height+"]");
		if(!res)
		{
			System.out.print(" not");
		}
		System.out.print(" cross with");
		System.out.println("line["+x1+","+y1+","+x2+","+y2+"]");
		return res;
	}
	/**
	 * 判断当前矩形跟指定线段的碰撞
	 * @param x1 端点1的x坐标
	 * @param y1 端点1的y坐标
	 * @param x2 端点2的x坐标
	 * @param y2 端点2的y坐标
	 * @return 是否发生碰撞
	 */
	public boolean crossWith(int x1,int y1,int x2,int y2)
	{

		//判断一定碰撞的情况(某顶点在矩形内部，即位于矩形#状划分的4号象限)
		int q1=getQuadrant(x1,y1);
		if(q1==4)
		{
			return true;
		}
		int q2=getQuadrant(x2,y2);
		if(q2==4)
		{
			return true;
		}
		//判断一定不碰撞的情况(两点位于同侧象限)
		if((q1<=2&&q2<=2)||(q1>=6&&q2>=6))
		{
			return false;
		}
		int m1=q1%3;
		int m2=q2%3;
		if((m1==0&&m2==0)||(m1==2&&m2==2))
		{
			return false;
		}
		//判断复杂情况(矩形对角线跟线段的碰撞)
		int r=m_x+m_width;
		int b=m_y+m_height;
		boolean crossed=C2D_Math.linCoss(m_x, m_y,r, b, x1, y1, x2, y2);
		if(crossed)
		{
			return crossed;
		}
		crossed=C2D_Math.linCoss(r, m_y,m_x, b, x1, y1, x2, y2);
		return crossed;
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
		m_x=xL;
		m_y=yT;
		m_width=xR-xL;
		if(m_width<0)
		{
			m_width=0;
		}
		m_height=yB-yT;
		if(m_height<0)
		{
			m_height=0;
		}
	}
	/**
	 * 计算当前矩形和指定矩形的交叉值，并作为当前矩形结果
	 * @param rect 交叉矩形
	 */
	public void setCross(C2D_RectI rect)
	{
		if(rect==null)
		{
			return;
		}
		int xL=C2D_Math.max(rect.m_x, m_x);
		int yT=C2D_Math.max(rect.m_y, m_y);
		int xR=C2D_Math.min(rect.m_x+rect.m_width, m_x+m_width);
		int yB=C2D_Math.min(rect.m_y+rect.m_height, m_y+m_height);
		m_x=xL;
		m_y=yT;
		m_width=xR-xL;
		if(m_width<0)
		{
			m_width=0;
		}
		m_height=yB-yT;
		if(m_height<0)
		{
			m_height=0;
		}
	}
}
