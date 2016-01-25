package c3d.util.math;

import c2d.lang.math.C2D_Math;

/**
 * 2D������
 * 
 * @author AndrewFan
 * 
 */
public class C3D_Vector2
{
	public float x, y;
	private static C3D_Vector3 vector3Temp = new C3D_Vector3();// ��ʱ��ά����

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
	 * ������ֵ
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
	 * ������ֵ
	 * 
	 * @param v
	 *            ��������
	 */
	public void setValue(C3D_Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * �ڻ�����
	 * 
	 * @return int
	 */
	public float innerProduct(C3D_Vector2 v)
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
	public static float innerProduct(C3D_Vector2 v1, C3D_Vector2 v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * �������
	 * 
	 * @param v
	 *            Vector2
	 * @return float ������������ά������ʵ������һ������
	 */
	public float outerProduct(C3D_Vector2 v)
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
	public static float outerProduct(C3D_Vector2 v1, C3D_Vector2 v2)
	{
		float t = (v1.x * v2.y - v2.x * v1.y);
		return t;
	}

	/**
	 * ���ȼ���
	 * 
	 * @return float ����
	 */
	public float dis()
	{
		float t = x * x + y * y;
		t = (float) Math.sqrt(t);
		return t;
	}

	/**
	 * ���ȵ�ƽ��
	 * 
	 * @return float ���ȵ�ƽ��
	 */
	public float dis2()
	{
		float t = (x * x + y * y);
		return t;
	}

	/**
	 * ��������һ������ľ����ƽ��
	 * 
	 * @return float ����ƽ��
	 */
	public float sqrDisTo(C3D_Vector2 v)
	{
		float t = (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
		return t;
	}

	/**
	 * ��תָ������(����ά����Χ��Z��)
	 * 
	 * @param theta
	 *            ����
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
	 * ����
	 */
	public void zero()
	{
		x = 0;
		y = 0;
	}
}