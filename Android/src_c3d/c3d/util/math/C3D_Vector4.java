package c3d.util.math;

/**
 * <p>
 * Title:Vector4 ��
 * </p>
 * 
 * <p>
 * Description:����4ά�������࣬����X��Y��Z��W 4������ֵ
 * </p>
 * 
 * @author Andrew Fan
 * @version 1.0
 */
public class C3D_Vector4
{
	private static C3D_Matrix4 martix4Temp = new C3D_Matrix4();// ��ʱ����
	private static C3D_Vector4 vector4Temp = new C3D_Vector4();// ��ʱʸ��
	public float x, y, z, w;

	public C3D_Vector4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public C3D_Vector4(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}

	public C3D_Vector4()
	{
		x = y = z = w = 0;
	}

	public C3D_Vector4(C3D_Vector4 v)
	{
		this(v.x, v.y, v.z, v.w);
	}

	/**
	 * �ж��Ƿ�Ϊ������
	 * 
	 * @return boolean
	 */
	public boolean isZeroVector()
	{
		if (Math.abs(x) == 0 && Math.abs(y) == 0 && Math.abs(z) == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * ���ȼ���
	 * 
	 * @return float ����
	 */
	public float size()
	{
		if (w == 0)
		{
			return 0;
		}
		float t = x * x + y * y + z * z;
		t = (float) Math.sqrt(t / (w * w));
		return t;
	}

	/**
	 * ���ȵ�ƽ��
	 * 
	 * @return float
	 */
	public float size2()
	{
		float t = (x * x + y * y + z * z) / (w * w);
		return t;
	}

	/**
	 * ��ֵ
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void setValue(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * ��ֵ
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void setValue(double x, double y, double z, double w)
	{
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.w = (float) w;
	}
	/**
	 * ��ֵ
	 * 
	 * @param v
	 */
	public void setValue(C3D_Vector3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = 1;
	}
	/**
	 * ��ֵ
	 * 
	 * @param v
	 */
	public void setValue(C3D_Vector4 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	// //���滯
	// public Vector4 normalize() throws Exception
	// {
	// float t = x*x + y*y + z*z + w*w;
	// t = Maths.sqrt(t);
	// if (Math.abs(t)==0)
	// {
	// throw new Exception();
	// }
	// Vector4 u = new Vector4();
	// u.x = x / t;
	// u.y = y / t;
	// u.z = z / t;
	// u.w = w / t;
	// return u;
	// }

	// ��˼���
	public float innerProduct(C3D_Vector4 a, C3D_Vector4 b)
	{
		float t = (a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w);
		return t;
	}

	/**
	 * ��ȡ��ֵ
	 * 
	 * @param buffer
	 *            ��Ҫ���õ���ֵ
	 */
	public void getValue(float buffer[])
	{
		if (buffer == null || buffer.length < 4)
		{
			return;
		}
		buffer[0] = x;
		buffer[1] = y;
		buffer[2] = z;
		buffer[3] = w;
	}

	/**
	 * ������ֵ�����buffer����>=4���ҵ��ĸ���Ԫ��Ϊ1����ô������ʱ����Ե��ĸ���Ԫ
	 * 
	 * @param buffer
	 *            ��Ҫ���õ���ֵ
	 */
	public void setValue(float buffer[])
	{
		if (buffer == null || buffer.length < 4 || buffer[3] == 0)
		{
			return;
		}
		x = buffer[0] / buffer[3];
		y = buffer[1] / buffer[3];
		z = buffer[2] / buffer[3];
		w = 1;
	}

	// ��ӡ
	public void print()
	{
		System.out.println("Vector4 = (" + x + "," + y + "," + z + "," + w + ")");
	}

	public void print(String s)
	{
		System.out.println(s);
		System.out.println("  (x,y,z,w)=(" + x + "," + y + "," + z + "," + w + ")");
	}
}
