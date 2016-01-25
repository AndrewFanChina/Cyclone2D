package c3d.util.math;

import c2d.lang.util.debug.C2D_Debug;


/**
 * 3x3������
 * @author AndrewFan
 *
 */
public class C3D_Matrix3
{
	private static C3D_Matrix3 m3Temp=new C3D_Matrix3();
	float data[];
	public static final int M00 = 0;
	public static final int M01 = 3;
	public static final int M02 = 6;
	public static final int M10 = 1;
	public static final int M11 = 4;
	public static final int M12 = 7;
	public static final int M20 = 2;
	public static final int M21 = 5;
	public static final int M22 = 8;

	/**
	 * ���캯��
	 */
	public C3D_Matrix3()
	{
		this.data = new float[9];
		identity();
	}

	public C3D_Matrix3(float b[])
	{
		this.data = new float[9];
		for (int i = 0; i < 9; i++)
		{
			this.data[i] = b[i];
		}
	}

	public C3D_Matrix3(C3D_Matrix3 t)
	{
		this(t.data);
	}

	/**
	 * ��������(��ɵ�λ����)
	 */
	public void identity()
	{
		for (int i = 0; i < 9; i++)
		{
			this.data[i] = 0;
		}
		this.data[M00] = this.data[M11] = this.data[M22] = 1;
	}

	/**
	 * ������ֵ
	 * 
	 * @param matrix
	 *            ָ������
	 */
	public void setValue(C3D_Matrix3 matrix)
	{
		if (matrix == null)
		{
			return;
		}
		for (int i = 0; i < data.length; i++)
		{
			this.data[i] = matrix.data[i];
		}
	}

	/**
	 * ������ֵ
	 * 
	 * @return ��ȡ��ֵ
	 */
	public float[] getValue()
	{
		return data;
	}

	/**
	 * 3x3��������ʽ������ʽ���ļ���
	 * 
	 * @return float ����ʽ��ֵ
	 */
	public float det()
	{
		float t = data[M00] * data[M11] * data[M22] + data[M01] * data[M12] * data[M20] + data[M02] * data[M21] * data[M10] - data[M20] * data[M11] * data[M02]
				- data[M12] * data[M21] * data[M00] - data[M22] * data[M10] * data[M01];
		return t;
	}
	/**
	 * 3x3��������(��ǰ�������M)
	 * 
	 * @param ma
	 *            ����m
	 */
	public void multiply(C3D_Matrix3 ma)
	{
		multiply(ma,this);
	}
	/**
	 * 3x3��������(��ǰ�������ma)�������������mb
	 * @param ma ��˾���
	 * @param mb �������
	 */
	public void multiply(C3D_Matrix3 ma,C3D_Matrix3 mb)
	{
		float v00 = data[M00] * ma.data[M00] + data[M10] * ma.data[M01] + data[M20] * ma.data[M02];
		float v01 = data[M00] * ma.data[M10] + data[M10] * ma.data[M11] + data[M20] * ma.data[M12];
		float v02 = data[M00] * ma.data[M20] + data[M10] * ma.data[M21] + data[M20] * ma.data[M22];

		float v10 = data[M01] * ma.data[M00] + data[M11] * ma.data[M01] + data[M21] * ma.data[M02];
		float v11 = data[M01] * ma.data[M10] + data[M11] * ma.data[M11] + data[M21] * ma.data[M12];
		float v12 = data[M01] * ma.data[M20] + data[M11] * ma.data[M21] + data[M21] * ma.data[M22];

		float v20 = data[M02] * ma.data[M00] + data[M12] * ma.data[M01] + data[M22] * ma.data[M02];
		float v21 = data[M02] * ma.data[M10] + data[M12] * ma.data[M11] + data[M22] * ma.data[M12];
		float v22 = data[M02] * ma.data[M20] + data[M12] * ma.data[M21] + data[M22] * ma.data[M22];

		mb.data[M00] = v00;
		mb.data[M01] = v01;
		mb.data[M02] = v02;
		mb.data[M10] = v10;
		mb.data[M11] = v11;
		mb.data[M12] = v12;
		mb.data[M20] = v20;
		mb.data[M21] = v21;
		mb.data[M22] = v22;
	}

	/**
	 * 3x3�������������
	 * 
	 * @return �����
	 * @throws Exception
	 */
	public C3D_Matrix3 inverse() throws Exception
	{
		float t = this.det();
		if (t == 0)
		{
			throw new Exception();
		}
		float[] b = new float[9];
		b[M00] = (data[M11] * data[M22] - data[M12] * data[M21]) / t;
		b[M01] = -(data[M01] * data[M22] - data[M21] * data[M02]) / t;
		b[M02] = (data[M01] * data[M12] - data[M02] * data[M11]) / t;
		b[M10] = -(data[M10] * data[M22] - data[M12] * data[M20]) / t;
		b[M11] = (data[M00] * data[M22] - data[M02] * data[M20]) / t;
		b[M12] = -(data[M00] * data[M12] - data[M02] * data[M10]) / t;
		b[M20] = (data[M10] * data[M21] - data[M11] * data[M20]) / t;
		b[M21] = -(data[M00] * data[M21] - data[M01] * data[M20]) / t;
		b[M22] = (data[M00] * data[M11] - data[M01] * data[M10]) / t;
		C3D_Matrix3 m = new C3D_Matrix3(b);
		return (m);
	}
	/**
	 * ��ˣ�������Ӳ�����һ����תָ�� ���ȵľ���
	 * 
	 * @param theta  ����
	 */

	public void postRotate(float theta)
	{
		m3Temp.setToRotate(theta);
		m3Temp.multiply(this,this);
	}

	/**
	 * ��ˣ�������Ӳ�����һ��λ�ƾ���
	 * 
	 * @param offX
	 * @param offY
	 */
	public void postTranslate(float offX, float offY)
	{
		m3Temp.setToTranslate(offX,offY);
		m3Temp.multiply(this,this);
	}

	/**
	 * ��ˣ�������Ӳ�����һ�����ž���
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void postScale(float scaleX, float scaleY)
	{
		m3Temp.setToScale(scaleX,scaleY);
		m3Temp.multiply(this,this);
	}
	/**
	 * ��ˣ�������Ӳ�����һ�����о���
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void postWarp(float warpX, float warpY)
	{
		m3Temp.setToWarp(warpX, warpY);
		m3Temp.multiply(this,this);
	}
	/**
	 * �ҳˣ����ȵ��Ӳ�����һ����תָ�� ���ȵľ���
	 * 
	 * @param theta  ����
	 */

	public void preRotate(float theta)
	{
		m3Temp.setToRotate(theta);
		multiply(m3Temp);
	}

	/**
	 * �ҳˣ����ȵ��Ӳ�����һ��λ�ƾ���
	 * 
	 * @param offX
	 * @param offY
	 */
	public void preTranslate(float offX, float offY)
	{
		m3Temp.setToTranslate(offX,offY);
		multiply(m3Temp);
	}

	/**
	 * �ҳˣ����ȵ��Ӳ�����һ�����ž���
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void preScale(float scaleX, float scaleY)
	{
		m3Temp.setToScale(scaleX,scaleY);
		multiply(m3Temp);
	}
	/**
	 * �ҳˣ����ȵ��Ӳ�����һ�����о���
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void preWarp(float warpX, float warpY)
	{
		m3Temp.setToWarp(warpX, warpY);
		multiply(m3Temp);
	}
	/**
	 * ���ó���תָ�� ���ȵľ���
	 * 
	 * @param theta  ����
	 */

	public void setToRotate(float theta)
	{
		float s = (float) Math.sin(theta);
		float c = (float) Math.cos(theta);
		this.identity();
		data[M00] = c;
		data[M01] = -s;
		data[M10] = s;
		data[M11] = c;
	}

	/**
	 * ���ó�λ�ƾ���
	 * 
	 * @param offX
	 * @param offY
	 */
	public void setToTranslate(float offX, float offY)
	{
		this.identity();
		data[M02] = offX;
		data[M12] = offY;
	}

	/**
	 * ���ó����ž���
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void setToScale(float scaleX, float scaleY)
	{
		this.identity();
		data[M00] = scaleX;
		data[M11] = scaleY;
	}
	/**
	 * ���óɴ��о���
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void setToWarp(float warpX, float warpY)
	{
		this.identity();
		data[M01] = warpX;
		data[M10] = warpY;
	}
//	/**
//	 * ת����4*4����
//	 * @return
//	 */
//	public Matrix4 toMatrix4()
//	{
//		m4Temp.setValue(this);
//		return m4Temp;
//	}
	public void show(String txt)
	{
		C2D_Debug.log("--"+txt+" --[");
		for (int i = 0; i < data.length; i++)
		{
			C2D_Debug.logChunk(data[i]+",");
		}
		C2D_Debug.log("]");
	}
	/**
	 * �ͷ���Դ
	 */
	public void releaseRes()
	{
		if (data != null)
		{
			data = null;
		}
	}
}
