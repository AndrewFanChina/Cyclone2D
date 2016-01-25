package c2d.lang.math.type;

/**
 * 浮点矩形类
 * @author AndrewFan
 *
 */
public class C2D_RectF
{
	/**
	 * x坐标
	 */
	public float m_x;
	/**
	 * y坐标
	 */
	public float m_y;
	/**
	 * 宽度
	 */
	public float m_width;
	/**
	 * 高度
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
	 * 设置矩形的数值
	 * @param x 坐标X
	 * @param y 坐标Y
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setValue(float x,float y,float width,float height)
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
	public void setValue(C2D_RectF rect)
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
	public void setPos(float x,float y)
	{
		m_x=x;
		m_y=y;
	}
	/**
	 * 设置矩形的尺寸
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setSize(float width,float height)
	{
		m_width=width;
		m_height=height;
	}
	/**
	 * 判断是否跟另外一个矩形相碰
	 * @param another 另外一个矩形
	 * @return 是否相碰
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
