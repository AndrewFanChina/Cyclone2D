package c3d.util.math;

import java.io.DataInputStream;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 3D向量
 * 
 * @author AndrewFan
 * 
 */
public class C3D_Vector3
{
	private static C3D_Matrix4 martix4Temp = new C3D_Matrix4();// 临时矩阵
	private static C3D_Vector3 vector3Temp = new C3D_Vector3();// 临时矢量
	public float x, y, z;

	// 构造函数
	public C3D_Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public C3D_Vector3()
	{
		x = y = z = 0;
	}

	public C3D_Vector3(C3D_Vector3 v)
	{
		this(v.x, v.y, v.z);
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
		float t = x * x + y * y + z * z;
		t = (float) Math.sqrt(t);
		return t;
	}

	/**
	 * 长度的平方
	 * 
	 * @return float
	 */
	public float size2()
	{
		float t = (x * x + y * y + z * z);
		return t;
	}

	/**
	 * 赋值
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setValue(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * 赋值
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setValue(double x, double y, double z)
	{
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
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
	}

	/**
	 * 赋值
	 * 
	 * @param v
	 * @return 自身
	 */
	public C3D_Vector3 setValue(C3D_Vector4 v)
	{
		if (v.w != 0)
		{
			this.x = v.x / v.w;
			this.y = v.y / v.w;
			this.z = v.z / v.w;
		}
		else
		{
			this.x = v.x;
			this.y = v.y;
			this.z = v.z;
		}
		return this;
	}

	/**
	 * 获取数值，如果buffer长度>=4，则第四个单元被覆盖为1
	 * 
	 * @param buffer
	 *            将要设置的数值
	 */
	public void getValue(float buffer[])
	{
		if (buffer == null || buffer.length < 3)
		{
			return;
		}
		buffer[0] = x;
		buffer[1] = y;
		buffer[2] = z;
		if (buffer.length >= 4)
		{
			buffer[3] = 1;
		}
	}

	/**
	 * 设置数值，如果buffer长度>=4，且第四个单元不为1，那么在设置时会除以第四个单元
	 * 
	 * @param buffer
	 *            将要设置的数值
	 */
	public void setValue(float buffer[])
	{
		if (buffer == null || buffer.length < 3)
		{
			return;
		}
		if (buffer.length >= 4 && buffer[3] != 1)
		{
			x = buffer[0] / buffer[3];
			y = buffer[1] / buffer[3];
			z = buffer[2] / buffer[3];
		}
		else
		{
			x = buffer[0];
			y = buffer[1];
			z = buffer[2];
		}
	}

	/**
	 * 矢量的叉积
	 * 
	 * @param a
	 *            第1个矢量
	 * @param b
	 *            第2个矢量
	 * @return Vector3 两个矢量叉乘产生的新矢量
	 */
	public static C3D_Vector3 crossProduct(C3D_Vector3 a, C3D_Vector3 b)
	{
		C3D_Vector3 c = new C3D_Vector3();
		c.x = (a.y * b.z - b.y * a.z);
		c.y = (a.z * b.x - b.z * a.x);
		c.z = (a.x * b.y - b.x * a.y);
		return c;
	}

	/**
	 * 矢量的叉积
	 * 
	 * @param b
	 *            矢量b
	 * @return Vector3 当前矢量与矢量b两个矢量叉乘产生的新矢量
	 */
	public C3D_Vector3 crossProduct(C3D_Vector3 b)
	{
		C3D_Vector3 c = new C3D_Vector3();
		c.x = (this.y * b.z - b.y * this.z);
		c.y = (this.z * b.x - b.z * this.x);
		c.z = (this.x * b.y - b.x * this.y);
		return c;
	}

	/**
	 * 矢量点积
	 * 
	 * @param a
	 *            矢量a
	 * @return float 当前矢量与矢量a两个矢量点乘的结果
	 */
	public float innerProduct(C3D_Vector3 a)
	{
		float t = (this.x * a.x + this.y * a.y + this.z * a.z);
		return t;
	}

	/**
	 * 矢量点积
	 * 
	 * @param x
	 *            指定矢量X坐标
	 * @param y
	 *            指定矢量Y坐标
	 * @param z
	 *            指定矢量Z坐标
	 * @return float 当前矢量与指定矢量两个矢量点乘的结果
	 */
	public float innerProduct(float x, float y, float z)
	{
		float t = (this.x * x + this.y * y + this.z * z);
		return t;
	}

	/**
	 * 矢量点积
	 * 
	 * @param a
	 *            指定矢量a
	 * @param b
	 *            指定矢量b
	 * @return 指定矢量a与指定矢量b两个矢量点乘的结果
	 */
	public static float innerProduct(C3D_Vector3 a, C3D_Vector3 b)
	{
		float t = (a.x * b.x + a.y * b.y + a.z * b.z);
		return t;
	}

	/**
	 * 矢量与标量相乘(缩放)
	 * 
	 * @param t
	 */
	public void scale(float t)
	{
		this.x = (this.x * t);
		this.y = (this.y * t);
		this.z = (this.z * t);
	}

	/**
	 * 向量加法
	 * 
	 * @param v
	 */
	public void add(C3D_Vector3 v)
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	/**
	 * 向量减法
	 * 
	 * @param v
	 * @return 结果向量
	 */
	public C3D_Vector3 subtract(C3D_Vector3 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	/**
	 * 向量减法
	 * 
	 * @param vSub
	 *            需要减去的向量
	 * @param vResult
	 *            用于结果存放的向量对象
	 * @return 结果向量
	 */
	public C3D_Vector3 subtract(C3D_Vector3 vSub, C3D_Vector3 vResult)
	{
		vResult.x = x - vSub.x;
		vResult.y = y - vSub.y;
		vResult.z = z - vSub.z;
		return vResult;
	}

	/**
	 * 围绕X轴旋转
	 * 
	 * @param theta
	 *            弧度
	 */
	public void rotateX(float theta)
	{
		vector3Temp.setValue(1, 0, 0);
		rotate(vector3Temp, theta);
	}

	/**
	 * 围绕Y轴旋转
	 * 
	 * @param theta
	 *            弧度
	 */
	public void rotateY(float theta)
	{
		vector3Temp.setValue(0, 1, 0);
		rotate(vector3Temp, theta);
	}

	/**
	 * 围绕Z轴旋转
	 * 
	 * @param theta
	 *            弧度
	 */
	public void rotateZ(float theta)
	{
		vector3Temp.setValue(0, 0, 1);
		rotate(vector3Temp, theta);
	}

	/**
	 * 围绕任意轴旋转
	 * 
	 * @param theta
	 *            弧度
	 */
	public void rotate(C3D_Vector3 v, float theta)
	{
		martix4Temp.setToRotate(v, theta);
		multiplyBy(martix4Temp);
	}

	/**
	 * 4x4矩阵和本向量的乘积(结果会化成w=1)
	 * 
	 * @param m
	 *            矩阵
	 */
	public void multiplyBy(C3D_Matrix4 m)
	{
		float wT = 1.0f / (m.data[C3D_Matrix4.M30] * x + m.data[C3D_Matrix4.M31] * y + m.data[C3D_Matrix4.M32] * z + m.data[C3D_Matrix4.M33]);
		float xN = (m.data[C3D_Matrix4.M00] * x + m.data[C3D_Matrix4.M01] * y + m.data[C3D_Matrix4.M02] * z + m.data[C3D_Matrix4.M03])
				* wT;
		float yN = (m.data[C3D_Matrix4.M10] * x + m.data[C3D_Matrix4.M11] * y + m.data[C3D_Matrix4.M12] * z + m.data[C3D_Matrix4.M13])
				* wT;
		float zN = (m.data[C3D_Matrix4.M20] * x + m.data[C3D_Matrix4.M21] * y + m.data[C3D_Matrix4.M22] * z + m.data[C3D_Matrix4.M23])
				* wT;
		x = xN;
		y = yN;
		z = zN;
	}

	/**
	 * 3x3矩阵与向本量m的乘积(无视W)
	 * 
	 * @param m
	 *            矩阵
	 */
	public void multiplyBy(C3D_Matrix3 m)
	{
		float xN = (m.data[0] * x + m.data[1] * y + m.data[2] * z);
		float yN = (m.data[3] * x + m.data[4] * y + m.data[5] * z);
		float zN = (m.data[6] * x + m.data[7] * y + m.data[8] * z);
		x = xN;
		y = yN;
		z = zN;
	}

	//从流读取
	public void readObject(DataInputStream dis) throws Exception
	{
		x = C3D_IOUtil.readFloat(0, dis);
		y = C3D_IOUtil.readFloat(0, dis);
		z = C3D_IOUtil.readFloat(0, dis);
	}

	// 打印信息
	public void show(String s)
	{
		C2D_Debug.log("--*v* " + s + "  (x,y,z)=(" + x + "," + y + "," + z + ")");
	}
}
