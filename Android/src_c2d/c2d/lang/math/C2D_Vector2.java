package c2d.lang.math;

/**
 * ����2D������
 * 
 * @author AndrewFan
 * 
 */
public class C2D_Vector2
{
	public int x, y;

	public C2D_Vector2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public C2D_Vector2()
	{
		x = y = 0;
	}

	public C2D_Vector2(C2D_Vector2 v)
	{
		this(v.x, v.y);
	}

	/**
	 * ������ֵ
	 * 
	 * @param x
	 * @param y
	 */
	public void setValue(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * ������ֵ
	 * 
	 * @param v
	 *            ��������
	 */
	public void setValue(C2D_Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * �ڻ�����
	 * 
	 * @return int �ڻ�
	 */
	public int innerProduct(C2D_Vector2 v)
	{
		return (x * v.x + y * v.y);
	}

	/**
	 * �ڻ�����
	 * 
	 * @param v1
	 *            ����1
	 * @param v2
	 *            ����2
	 * @return �����������ڻ�
	 */
	public static int innerProduct(C2D_Vector2 v1, C2D_Vector2 v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * �������
	 * 
	 * @param v
	 *            Vector2
	 * @return int ������������ά������ʵ������һ������
	 */
	public int outerProduct(C2D_Vector2 v)
	{
		return (x * v.y - v.x * y);
	}

	/**
	 * �������
	 * 
	 * @param v1
	 *            ��ά����1
	 * @param v2
	 *            ��ά����2
	 * @return ����������������������ά������ʵ������һ������
	 */
	public static int outerProduct(C2D_Vector2 v1, C2D_Vector2 v2)
	{
		int t = (v1.x * v2.y - v2.x * v1.y);
		return t;
	}

	/**
	 * ���ȼ���
	 * 
	 * @return int ����
	 */
	public float dis()
	{
		float t = x * x + y * y;
		t = C2D_Math.sqrt(t);
		return t;
	}

	/**
	 * ���ȵ�ƽ��
	 * 
	 * @return int ���ȵ�ƽ��
	 */
	public float dis2()
	{
		int t = (x * x + y * y);
		return t;
	}

	/**
	 * ��������һ������ľ����ƽ��
	 * 
	 * @return int ����ƽ��
	 */
	public int sqrDisTo(C2D_Vector2 v)
	{
		int t = (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
		return t;
	}

	/**
	 * ����
	 */
	public void zero()
	{
		x = 0;
		y = 0;
	}
}
