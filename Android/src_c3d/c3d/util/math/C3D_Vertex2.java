package c3d.util.math;

import c2d.lang.math.C2D_Math;

/**
 * <p>
 * C3D_Vertex2��
 * </p>
 * 
 * <p>
 * Description:����2ά������࣬����X��Y2������ֵ
 * </p>
 * 
 * @author Andrew Fan
 * @version 1.0
 */
public class C3D_Vertex2
{

	public float x, y;

	public C3D_Vertex2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public C3D_Vertex2()
	{
		x = y = 0;
	}

	public C3D_Vertex2(C3D_Vertex2 v)
	{
		this(v.x, v.y);
	}

	public void setValue(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public void setValue(C3D_Vertex2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * innerProduct �ڻ�����
	 * 
	 * @return int
	 */
	public float innerProduct(C3D_Vertex2 v)
	{
		return (x * v.x + y * v.y);
	}

	public static float innerProduct(C3D_Vertex2 v1, C3D_Vertex2 v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * outerProduct �������
	 * 
	 * @param v
	 *            Vector3D
	 * @return Vector3D
	 */
	public float outerProduct(C3D_Vertex2 v)
	{
		return (x * v.y - v.x * y);
	}

	public static float outerProduct(C3D_Vertex2 v1, C3D_Vertex2 v2)
	{
		float t = (v1.x * v2.y - v2.x * v1.y);
		return t;
	}

	// ���ȼ���
	public float size()
	{
		float t = x * x + y * y;
		t = C2D_Math.sqrt(t);
		return t;
	}

	// ���ȵ�ƽ��(����ֵ��������32λ)
	public float size2()
	{
		float t = (x * x + y * y);
		return t;
	}

	public void print()
	{
		System.out.println("Vertex2 = (" + x + "," + y + ")");
	}

	public void print(String s)
	{
		System.out.println(s);
		System.out.println("  (x,y,z)=(" + x + "," + y + ")");
	}
}
