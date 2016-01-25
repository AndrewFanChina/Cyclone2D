package c2d.lang.math.type;


/**
 * ���Ͷ�ά����
 * @author AndrewFan
 *
 */
public class C2D_PointI {
    public int m_x, m_y;
	private static C2D_PointIBuffer m_tempBufs = new C2D_PointIBuffer(40);
    public static C2D_PointI zero() {
        return new C2D_PointI(0, 0);
    }

    public C2D_PointI() {
        this(0, 0);
    }

    public static C2D_PointI make(int x, int y) 
    {
        return new C2D_PointI(x, y);
    }

    public C2D_PointI(int x, int y) 
    {
        m_x = x;
        m_y = y;
    }
    public C2D_PointI setValue(int x, int y)
    {
        m_x = x;
        m_y = y;
        return this;
    }
    public C2D_PointI setValue(C2D_PointI point)
    {
    	if(point!=null)
    	{
	        m_x = point.m_x;
	        m_y = point.m_y;
    	}
    	return this;
    }
	public C2D_PointI setToBuffer(int x, int y)
	{
		C2D_PointI next = m_tempBufs.next();
		next.m_x=x;
		next.m_y=y;
		return next;
	}
    /**
     * �����Ƿ���ָ��������Χ��
     * @param rect ָ��������
     * @return �Ƿ���ָ��������Χ��
     */
    public boolean inRegion(C2D_RectI rect)
    {
    	if(rect==null)
    	{
    		return false;
    	}
		return m_x >= rect.m_x && m_x <= rect.m_x + rect.m_width && 
				m_y >= rect.m_y && m_y <= rect.m_y + rect.m_height;
    }
    /**
     * �����Ƿ���ָ��������Χ��
     * @param rect ָ��������
     * @return �Ƿ���ָ��������Χ��
     */
    public boolean inRegion(C2D_RectF rect)
    {
    	if(rect==null)
    	{
    		return false;
    	}
		return m_x >= rect.m_x && m_x <= rect.m_x + rect.m_width && 
				m_y >= rect.m_y && m_y <= rect.m_y + rect.m_height;
    }
    public String toString() 
    {
        return "(" + m_x + ", " + m_y + ")";
    }

    public static boolean equalToPoint(C2D_PointI p1, C2D_PointI p2) {
        return p1.m_x == p2.m_x && p1.m_y == p2.m_y;
    }


    /**
     * ȡ��
     *
     * @return C2D_PointI
     */
    public C2D_PointI Neg() {
        return setToBuffer(-m_x, -m_y);
    }

    /**
     * ������������ۼ�ֵ
     *
     * @return C2D_PointI
     */
	public C2D_PointI Add(final C2D_PointI v) {
        return setToBuffer(m_x + v.m_x, m_y + v.m_y);
    }

    /**
     * �������
     *
     * @return C2D_PointI
     */
    public C2D_PointI Sub(final C2D_PointI v2) {
        return setToBuffer(m_x - v2.m_x, m_y - v2.m_y);
    }


}
