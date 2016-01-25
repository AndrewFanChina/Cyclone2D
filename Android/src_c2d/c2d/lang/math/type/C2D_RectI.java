package c2d.lang.math.type;

import c2d.lang.math.C2D_Math;

/**
 * ���;�����
 * @author AndrewFan
 *
 */
public class C2D_RectI
{
	/**
	 * x����
	 */
	public int m_x;
	/**
	 * y����
	 */
	public int m_y;
	/**
	 * ���
	 */
	public int m_width;
	/**
	 * �߶�
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
	 * ���þ��ε���ֵ
	 * @param x ����X
	 * @param y ����Y
	 * @param width ���
	 * @param height �߶�
	 */
	public void setValue(int x,int y,int width,int height)
	{
		m_x=x;
		m_y=y;
		m_width=width;
		m_height=height;
	}
	/**
	 * ���þ��ε���ֵ��ָ���ľ�����ͬ
	 * @param rect ָ���ľ���
	 */
	public void setValue(C2D_RectI rect)
	{
		m_x=rect.m_x;
		m_y=rect.m_y;
		m_width=rect.m_width;
		m_height=rect.m_height;
	}
	/**
	 * ���þ��ε�λ��
	 * @param x ����X
	 * @param y ����Y
	 */
	public void setPos(int x,int y)
	{
		m_x=x;
		m_y=y;
	}
	/**
	 * ���þ��εĳߴ�
	 * @param width ���
	 * @param height �߶�
	 */
	public void setSize(int width,int height)
	{
		m_width=width;
		m_height=height;
	}
	/**
	 * �ж��Ƿ������һ����������
	 * @param another ����һ������
	 * @return �Ƿ�����
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
	 * ���յ�ǰ���εĶ��㻮�ֳɹ��ɵ�9�����ޣ����ա�#��״̬���֣�
	 * �����ұߣ����ϵ��£����зֱ��趨Ϊ0-8�ı�š���������
	 * ��ȡָ���Ķ������ڵ����ޱ�š�
	 * @param x
	 * @param y
	 * @return ���ޱ��
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
	 * �����жϵ�ǰ���θ�ָ���߶ε���ײ
	 * @param x1 �˵�1��x����
	 * @param y1 �˵�1��y����
	 * @param x2 �˵�2��x����
	 * @param y2 �˵�2��y����
	 * @return �Ƿ�����ײ
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
	 * �жϵ�ǰ���θ�ָ���߶ε���ײ
	 * @param x1 �˵�1��x����
	 * @param y1 �˵�1��y����
	 * @param x2 �˵�2��x����
	 * @param y2 �˵�2��y����
	 * @return �Ƿ�����ײ
	 */
	public boolean crossWith(int x1,int y1,int x2,int y2)
	{

		//�ж�һ����ײ�����(ĳ�����ھ����ڲ�����λ�ھ���#״���ֵ�4������)
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
		//�ж�һ������ײ�����(����λ��ͬ������)
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
		//�жϸ������(���ζԽ��߸��߶ε���ײ)
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
	 * ���㵱ǰ���κ�ָ�����εĽ���ֵ������Ϊ��ǰ���ν��
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
	 * ���㵱ǰ���κ�ָ�����εĽ���ֵ������Ϊ��ǰ���ν��
	 * @param rect �������
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
