package c2d.frame.com.data.unit_type;
/**
 * 子控件类型参数类，用于定义子空间将位于的坐标位置，锚点等信息
 * @author AndrewFan
 *
 */
public abstract class C2D_UnitType
{
	public static final int PB=100;//比率基值
	/** 控件x坐标位于其容器的x方向百分比 */
	public int m_x;
	/** 控件y坐标位于其容器的y方向百分比 */
	public int m_y;
	/** 控件z坐标 */
	public int m_z;
	/** 控件的锚点 */
	public int m_anchor;
	/**
	 * 获得x方向百分比坐标
	 * @return 百分比坐标
	 */
	public int getX()
	{
		return m_x;
	}
	/**
	 * 设置x方向百分比坐标
	 * @param x x方向百分比坐标
	 */
	public void setX(int x)
	{
		this.m_x = x;
	}
	/**
	 * 获得y方向百分比坐标
	 * @return 百分比坐标
	 */
	public int getY()
	{
		return m_y;
	}
	/**
	 * 设置y方向百分比坐标
	 * @param y y方向百分比坐标
	 */
	public void setY(int y)
	{
		this.m_y = y;
	}
	/**
	 * 获得z深度
	 * @return z深度
	 */
	public int getZ()
	{
		return m_z;
	}
	/**
	 * 设置z深度
	 * @param z z深度
	 */
	public void setZ(int z)
	{
		this.m_z = z;
	}
	/**
	 * 获得锚点
	 * @return 锚点
	 */
	public int getAnchor()
	{
		return m_anchor;
	}
	/**
	 * 设置锚点
	 * @param anchor 锚点
	 */
	public void setAnchor(int anchor)
	{
		this.m_anchor = anchor;
	}
	/**
	 * 设置参数
	 * @param x x方向百分比坐标
	 * @param y y方向百分比坐标
	 * @param anchor 锚点
	 */
	public void setParams( int x,int y,int anchor)
	{
		m_x=x;
		m_y=y;
		m_anchor=anchor;
	}
}
