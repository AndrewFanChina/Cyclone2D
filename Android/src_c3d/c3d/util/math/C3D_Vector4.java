package c3d.util.math;

/**
 * <p>
 * Title:Vector4 类
 * </p>
 * 
 * <p>
 * Description:保存4维向量的类，包含X、Y、Z、W 4个坐标值
 * </p>
 * 
 * @author Andrew Fan
 * @version 1.0
 */
public class C3D_Vector4
{
	private static C3D_Matrix4 martix4Temp = new C3D_Matrix4();// 临时矩阵
	private static C3D_Vector4 vector4Temp = new C3D_Vector4();// 临时矢量
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
	 * 判断是否为零向量
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
	 * 长度计算
	 * 
	 * @return float 长度
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
	 * 长度的平方
	 * 
	 * @return float
	 */
	public float size2()
	{
		float t = (x * x + y * y + z * z) / (w * w);
		return t;
	}

	/**
	 * 赋值
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
	 * 赋值
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
	 * 赋值
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
	 * 赋值
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

	// //正规化
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

	// 点乘计算
	public float innerProduct(C3D_Vector4 a, C3D_Vector4 b)
	{
		float t = (a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w);
		return t;
	}

	/**
	 * 获取数值
	 * 
	 * @param buffer
	 *            将要设置的数值
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
	 * 设置数值，如果buffer长度>=4，且第四个单元不为1，那么在设置时会除以第四个单元
	 * 
	 * @param buffer
	 *            将要设置的数值
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

	// 打印
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
