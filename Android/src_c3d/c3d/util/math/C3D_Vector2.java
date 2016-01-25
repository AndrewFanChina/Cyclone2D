package c3d.util.math;

import c2d.lang.math.C2D_Math;

/**
 * 2D向量类
 * 
 * @author AndrewFan
 * 
 */
public class C3D_Vector2
{
	public float x, y;
	private static C3D_Vector3 vector3Temp = new C3D_Vector3();// 临时三维向量

	public C3D_Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public C3D_Vector2()
	{
		x = y = 0;
	}

	public C3D_Vector2(C3D_Vector2 v)
	{
		this(v.x, v.y);
	}

	/**
	 * 设置数值
	 * 
	 * @param x
	 * @param y
	 */
	public void setValue(float x, float y)
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
	public void setValue(C3D_Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * 内积计算
	 * 
	 * @return int
	 */
	public float innerProduct(C3D_Vector2 v)
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
	public static float innerProduct(C3D_Vector2 v1, C3D_Vector2 v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * 外积计算
	 * 
	 * @param v
	 *            Vector2
	 * @return float 外积结果，从三维来看，实际上是一个向量
	 */
	public float outerProduct(C3D_Vector2 v)
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
	public static float outerProduct(C3D_Vector2 v1, C3D_Vector2 v2)
	{
		float t = (v1.x * v2.y - v2.x * v1.y);
		return t;
	}

	/**
	 * 长度计算
	 * 
	 * @return float 长度
	 */
	public float dis()
	{
		float t = x * x + y * y;
		t = (float) Math.sqrt(t);
		return t;
	}

	/**
	 * 长度的平方
	 * 
	 * @return float 长度的平方
	 */
	public float dis2()
	{
		float t = (x * x + y * y);
		return t;
	}

	/**
	 * 到达另外一个顶点的距离的平方
	 * 
	 * @return float 距离平方
	 */
	public float sqrDisTo(C3D_Vector2 v)
	{
		float t = (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
		return t;
	}

	/**
	 * 旋转指定弧度(从三维来看围绕Z轴)
	 * 
	 * @param theta
	 *            弧度
	 */
	public void rotate(float theta)
	{
		vector3Temp.setValue(x, y, 0);
		vector3Temp.rotateZ(theta);
		this.setValue(vector3Temp.x, vector3Temp.y);
	}

	public void rotateDegree(float degree)
	{
		rotate(C2D_Math.degreesToRadians(degree));
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