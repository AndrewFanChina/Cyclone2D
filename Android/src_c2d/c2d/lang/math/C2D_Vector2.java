package c2d.lang.math;

/**
 * 整型2D向量类
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
	 * 设置数值
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
	 * 设置数值
	 * 
	 * @param v
	 *            其它向量
	 */
	public void setValue(C2D_Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * 内积计算
	 * 
	 * @return int 内积
	 */
	public int innerProduct(C2D_Vector2 v)
	{
		return (x * v.x + y * v.y);
	}

	/**
	 * 内积计算
	 * 
	 * @param v1
	 *            向量1
	 * @param v2
	 *            向量2
	 * @return 两个向量的内积
	 */
	public static int innerProduct(C2D_Vector2 v1, C2D_Vector2 v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * 外积计算
	 * 
	 * @param v
	 *            Vector2
	 * @return int 外积结果，从三维来看，实际上是一个向量
	 */
	public int outerProduct(C2D_Vector2 v)
	{
		return (x * v.y - v.x * y);
	}

	/**
	 * 外积计算
	 * 
	 * @param v1
	 *            二维向量1
	 * @param v2
	 *            二维向量2
	 * @return 两个向量的外积结果，从三维来看，实际上是一个向量
	 */
	public static int outerProduct(C2D_Vector2 v1, C2D_Vector2 v2)
	{
		int t = (v1.x * v2.y - v2.x * v1.y);
		return t;
	}

	/**
	 * 长度计算
	 * 
	 * @return int 长度
	 */
	public float dis()
	{
		float t = x * x + y * y;
		t = C2D_Math.sqrt(t);
		return t;
	}

	/**
	 * 长度的平方
	 * 
	 * @return int 长度的平方
	 */
	public float dis2()
	{
		int t = (x * x + y * y);
		return t;
	}

	/**
	 * 到达另外一个顶点的距离的平方
	 * 
	 * @return int 距离平方
	 */
	public int sqrDisTo(C2D_Vector2 v)
	{
		int t = (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
		return t;
	}

	/**
	 * 归零
	 */
	public void zero()
	{
		x = 0;
		y = 0;
	}
}
