package c2d.frame.com.data.unit_type;
/**
 * �ӿؼ����Ͳ����࣬���ڶ����ӿռ佫λ�ڵ�����λ�ã�ê�����Ϣ
 * @author AndrewFan
 *
 */
public abstract class C2D_UnitType
{
	public static final int PB=100;//���ʻ�ֵ
	/** �ؼ�x����λ����������x����ٷֱ� */
	public int m_x;
	/** �ؼ�y����λ����������y����ٷֱ� */
	public int m_y;
	/** �ؼ�z���� */
	public int m_z;
	/** �ؼ���ê�� */
	public int m_anchor;
	/**
	 * ���x����ٷֱ�����
	 * @return �ٷֱ�����
	 */
	public int getX()
	{
		return m_x;
	}
	/**
	 * ����x����ٷֱ�����
	 * @param x x����ٷֱ�����
	 */
	public void setX(int x)
	{
		this.m_x = x;
	}
	/**
	 * ���y����ٷֱ�����
	 * @return �ٷֱ�����
	 */
	public int getY()
	{
		return m_y;
	}
	/**
	 * ����y����ٷֱ�����
	 * @param y y����ٷֱ�����
	 */
	public void setY(int y)
	{
		this.m_y = y;
	}
	/**
	 * ���z���
	 * @return z���
	 */
	public int getZ()
	{
		return m_z;
	}
	/**
	 * ����z���
	 * @param z z���
	 */
	public void setZ(int z)
	{
		this.m_z = z;
	}
	/**
	 * ���ê��
	 * @return ê��
	 */
	public int getAnchor()
	{
		return m_anchor;
	}
	/**
	 * ����ê��
	 * @param anchor ê��
	 */
	public void setAnchor(int anchor)
	{
		this.m_anchor = anchor;
	}
	/**
	 * ���ò���
	 * @param x x����ٷֱ�����
	 * @param y y����ٷֱ�����
	 * @param anchor ê��
	 */
	public void setParams( int x,int y,int anchor)
	{
		m_x=x;
		m_y=y;
		m_anchor=anchor;
	}
}
