package c2d.lang.math.type;

/**
 * ���������
 * @author AndrewFan
 *
 */
public class C2D_RectF
{
	/**
	 * x����
	 */
	public float m_x;
	/**
	 * y����
	 */
	public float m_y;
	/**
	 * ���
	 */
	public float m_width;
	/**
	 * �߶�
	 */
	public float m_height;
	
	public C2D_RectF()
	{
	}
	public C2D_RectF(float x,float y,float w,float h)
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
	public void setValue(float x,float y,float width,float height)
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
	public void setValue(C2D_RectF rect)
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
	public void setPos(float x,float y)
	{
		m_x=x;
		m_y=y;
	}
	/**
	 * ���þ��εĳߴ�
	 * @param width ���
	 * @param height �߶�
	 */
	public void setSize(float width,float height)
	{
		m_width=width;
		m_height=height;
	}
	/**
	 * �ж��Ƿ������һ����������
	 * @param another ����һ������
	 * @return �Ƿ�����
	 */
	public boolean crossWith(C2D_RectF another)
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
}
