package c3d.util.math;

import java.io.DataInputStream;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 3D����
 * 
 * @author AndrewFan
 * 
 */
public class C3D_Vector3
{
	private static C3D_Matrix4 martix4Temp = new C3D_Matrix4();// ��ʱ����
	private static C3D_Vector3 vector3Temp = new C3D_Vector3();// ��ʱʸ��
	public float x, y, z;

	// ���캯��
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
		float t = x * x + y * y + z * z;
		t = (float) Math.sqrt(t);
		return t;
	}

	/**
	 * ���ȵ�ƽ��
	 * 
	 * @return float
	 */
	public float size2()
	{
		float t = (x * x + y * y + z * z);
		return t;
	}

	/**
	 * ��ֵ
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
	 * ��ֵ
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
	 * ��ֵ
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
	 * ��ֵ
	 * 
	 * @param v
	 * @return ����
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
	 * ��ȡ��ֵ�����buffer����>=4������ĸ���Ԫ������Ϊ1
	 * 
	 * @param buffer
	 *            ��Ҫ���õ���ֵ
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
	 * ������ֵ�����buffer����>=4���ҵ��ĸ���Ԫ��Ϊ1����ô������ʱ����Ե��ĸ���Ԫ
	 * 
	 * @param buffer
	 *            ��Ҫ���õ���ֵ
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
	 * ʸ���Ĳ��
	 * 
	 * @param a
	 *            ��1��ʸ��
	 * @param b
	 *            ��2��ʸ��
	 * @return Vector3 ����ʸ����˲�������ʸ��
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
	 * ʸ���Ĳ��
	 * 
	 * @param b
	 *            ʸ��b
	 * @return Vector3 ��ǰʸ����ʸ��b����ʸ����˲�������ʸ��
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
	 * ʸ�����
	 * 
	 * @param a
	 *            ʸ��a
	 * @return float ��ǰʸ����ʸ��a����ʸ����˵Ľ��
	 */
	public float innerProduct(C3D_Vector3 a)
	{
		float t = (this.x * a.x + this.y * a.y + this.z * a.z);
		return t;
	}

	/**
	 * ʸ�����
	 * 
	 * @param x
	 *            ָ��ʸ��X����
	 * @param y
	 *            ָ��ʸ��Y����
	 * @param z
	 *            ָ��ʸ��Z����
	 * @return float ��ǰʸ����ָ��ʸ������ʸ����˵Ľ��
	 */
	public float innerProduct(float x, float y, float z)
	{
		float t = (this.x * x + this.y * y + this.z * z);
		return t;
	}

	/**
	 * ʸ�����
	 * 
	 * @param a
	 *            ָ��ʸ��a
	 * @param b
	 *            ָ��ʸ��b
	 * @return ָ��ʸ��a��ָ��ʸ��b����ʸ����˵Ľ��
	 */
	public static float innerProduct(C3D_Vector3 a, C3D_Vector3 b)
	{
		float t = (a.x * b.x + a.y * b.y + a.z * b.z);
		return t;
	}

	/**
	 * ʸ����������(����)
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
	 * �����ӷ�
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
	 * ��������
	 * 
	 * @param v
	 * @return �������
	 */
	public C3D_Vector3 subtract(C3D_Vector3 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	/**
	 * ��������
	 * 
	 * @param vSub
	 *            ��Ҫ��ȥ������
	 * @param vResult
	 *            ���ڽ����ŵ���������
	 * @return �������
	 */
	public C3D_Vector3 subtract(C3D_Vector3 vSub, C3D_Vector3 vResult)
	{
		vResult.x = x - vSub.x;
		vResult.y = y - vSub.y;
		vResult.z = z - vSub.z;
		return vResult;
	}

	/**
	 * Χ��X����ת
	 * 
	 * @param theta
	 *            ����
	 */
	public void rotateX(float theta)
	{
		vector3Temp.setValue(1, 0, 0);
		rotate(vector3Temp, theta);
	}

	/**
	 * Χ��Y����ת
	 * 
	 * @param theta
	 *            ����
	 */
	public void rotateY(float theta)
	{
		vector3Temp.setValue(0, 1, 0);
		rotate(vector3Temp, theta);
	}

	/**
	 * Χ��Z����ת
	 * 
	 * @param theta
	 *            ����
	 */
	public void rotateZ(float theta)
	{
		vector3Temp.setValue(0, 0, 1);
		rotate(vector3Temp, theta);
	}

	/**
	 * Χ����������ת
	 * 
	 * @param theta
	 *            ����
	 */
	public void rotate(C3D_Vector3 v, float theta)
	{
		martix4Temp.setToRotate(v, theta);
		multiplyBy(martix4Temp);
	}

	/**
	 * 4x4����ͱ������ĳ˻�(����ữ��w=1)
	 * 
	 * @param m
	 *            ����
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
	 * 3x3����������m�ĳ˻�(����W)
	 * 
	 * @param m
	 *            ����
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

	//������ȡ
	public void readObject(DataInputStream dis) throws Exception
	{
		x = C3D_IOUtil.readFloat(0, dis);
		y = C3D_IOUtil.readFloat(0, dis);
		z = C3D_IOUtil.readFloat(0, dis);
	}

	// ��ӡ��Ϣ
	public void show(String s)
	{
		C2D_Debug.log("--*v* " + s + "  (x,y,z)=(" + x + "," + y + "," + z + ")");
	}
}
