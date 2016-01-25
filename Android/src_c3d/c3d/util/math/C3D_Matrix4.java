package c3d.util.math;

import c2d.lang.util.debug.C2D_Debug;


/**
 * 4x4矩阵类
 * @author AndrewFan
 *
 */
public class C3D_Matrix4
{
	public float data[]= new float[16]; // 数值
	public static final int M00 = 0;// 0;
	public static final int M01 = 4;// 1;
	public static final int M02 = 8;// 2;
	public static final int M03 = 12;// 3;
	public static final int M10 = 1;// 4;
	public static final int M11 = 5;// 5;
	public static final int M12 = 9;// 6;
	public static final int M13 = 13;// 7;
	public static final int M20 = 2;// 8;
	public static final int M21 = 6;// 9;
	public static final int M22 = 10;// 10;
	public static final int M23 = 14;// 11;
	public static final int M30 = 3;// 12;
	public static final int M31 = 7;// 13;
	public static final int M32 = 11;// 14;
	public static final int M33 = 15;// 15;
	private static final C3D_Vector3 vector3Temp = new C3D_Vector3();// 临时三维向量
	private static C3D_Matrix4 matrix4Temp = new C3D_Matrix4();// 临时四维矩阵
	private static float f4_Temp0[] = new float[4];// 临时浮点数组
	private static C3D_Matrix4 matrix4Identify=new C3D_Matrix4();//初始矩阵
	static
	{
		matrix4Identify.data[M00] = 1;
		matrix4Identify.data[M01] = 0;
		matrix4Identify.data[M02] = 0;
		matrix4Identify.data[M03] = 0;
		matrix4Identify.data[M10] = 0;
		matrix4Identify.data[M11] = 1;
		matrix4Identify.data[M12] = 0;
		matrix4Identify.data[M13] = 0;
		matrix4Identify.data[M20] = 0;
		matrix4Identify.data[M21] = 0;
		matrix4Identify.data[M22] = 1;
		matrix4Identify.data[M23] = 0;
		matrix4Identify.data[M30] = 0;
		matrix4Identify.data[M31] = 0;
		matrix4Identify.data[M32] = 0;
		matrix4Identify.data[M33] = 1;
	}
	/**
	 * 构造函数
	 * 
	 * @param dataInit
	 */
	public C3D_Matrix4(float dataInit[])
	{
		System.arraycopy(dataInit, 0, data, 0, 16);
	}

	public C3D_Matrix4()
	{
		data[M00] = 1;
		data[M11] = 1;
		data[M22] = 1;
		data[M33] = 1;
	}

	public C3D_Matrix4(C3D_Matrix4 t)
	{
		this(t.data);
	}

	/**
	 * 矩阵重置(变成单位矩阵)
	 */
	public C3D_Matrix4 identity()
	{
		System.arraycopy(matrix4Identify.data, 0, data, 0, 16);
		return this;
	}

	/**
	 * 从4*4矩阵设置数值
	 * 
	 * @param matrix
	 */
	public void setValue(C3D_Matrix4 matrix)
	{
		System.arraycopy(matrix.data, 0, data, 0, 16);
	}

	// /**
	// * 从3*3矩阵设置数值
	// *
	// * @param matrix
	// */
	// public void setValue(Matrix3 matrix)
	// {
	// if (matrix == null)
	// {
	// return;
	// }
	// this.identity();
	// data[M00] = matrix.data[Matrix3.M00];
	// data[M01] = matrix.data[Matrix3.M01];
	// data[M03] = matrix.data[Matrix3.M02];
	// data[M10] = matrix.data[Matrix3.M10];
	// data[M11] = matrix.data[Matrix3.M11];
	// data[M13] = matrix.data[Matrix3.M12];
	// data[M20] = matrix.data[Matrix3.M20];
	// data[M21] = matrix.data[Matrix3.M21];
	// data[M33] = matrix.data[Matrix3.M22];
	// }


	/**
	 * 两个4×4矩阵的乘积，并将结果赋给当前矩阵（当前矩阵左乘M。）
	 * 
	 * @param m
	 * @return 自身矩阵
	 */
	public C3D_Matrix4 multiply(C3D_Matrix4 m)
	{
		multiply(this.data, m.data, this.data);
		return this;
	}

	/**
	 * 两个4×4矩阵的乘积（当前矩阵左乘M，并将结果赋值给传入的矩阵。）
	 * 
	 * @param m
	 * @param res
	 */
	public void multiply(C3D_Matrix4 m, C3D_Matrix4 res)
	{
		multiply(this.data, m.data, res.data);
	}

	/**
	 * 两个4×4矩阵的乘积（m1*m2->m3）
	 * 
	 * @param m
	 * @param res
	 */
	//#if(Platform=="Android")
	private static native void multiply(float m1[], float m2[], float m3[]);
	//#else
//@	private static void multiply(float m1[], float m2[], float m3[])
//@	{
//@		matrix4_mul(m1, m2, m3);
//@	}
	//#endif
	// 转置矩阵索引
	private static final int[] TR = { 0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15 };

	/**
	 * 4x4矩阵转置（矩阵的行和列互换。）
	 */
	public C3D_Matrix4 transpose()
	{
		C3D_Matrix4 m = new C3D_Matrix4();
		for (int i = 0; i < 16; i++)
		{
			m.data[i] = data[TR[i]];
		}
		return m;
	}

	/**
	 * 逆反矩阵，将结果赋值给自己
	 */
	public boolean inverse()
	{
		return inverse(this.data, this.data);
	}

	/**
	 * 逆反矩阵，将结果赋值给指定结果
	 */
	public boolean inverse(C3D_Matrix4 res)
	{
		return inverse(this.data, res.data);
	}

	/**
	 * 逆反矩阵（inverse(m1)->m2）
	 * 
	 * @param m
	 * @param res
	 */
	//#if(Platform=="Android")
	private static native boolean inverse(float m1[], float m2[]);
	//#else
//@	private static boolean inverse(float m1[], float m2[])
//@	{
//@		return matrix4_inv(m1,m2);
//@	}
	//#endif
	/**
	 * 矩阵行列式（行列式）的计算
	 * 
	 * @return 矩阵行列式
	 */
	public float det()
	{
		return det(data);
	}
	//#if(Platform=="Android")
	private static native float det(float mat[]);
	//#else
//@	private static float det(float mat[])
//@	{
//@		return matrix4_det(mat);
//@	}
	//#endif
	/**
	 * 左乘（即后叠加操作）一个围绕任意轴的旋转矩阵
	 * 
	 * @param v
	 * @param theta
	 *            弧度
	 */
	public void postRotate(C3D_Vector3 v, float theta)
	{
		matrix4Temp.setToRotate(v, theta);
		matrix4Temp.multiply(this, this);
	}

	/**
	 * 左乘（即后叠加操作）一个围绕Z轴的旋转矩阵
	 * 
	 * @param theta
	 *            弧度
	 */
	public void postRotate(float theta)
	{
		matrix4Temp.setToRotate(theta);
		matrix4Temp.multiply(this, this);
	}
	/**
	 * 左乘（即后叠加操作）一个Z==0平面的位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 */
	public void postTranslate(float offX, float offY)
	{
		postTranslate(offX,offY,0.0f);
	}
	/**
	 * 左乘（即后叠加操作）一个位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 * @param offZ
	 */
	public void postTranslate(float offX, float offY, float offZ)
	{
		matrix4Temp.setToTranslate(offX, offY, offZ);
		matrix4Temp.multiply(this, this);
	}
	/**
	 * 左乘（即后叠加操作）一个XY方向的缩放矩阵(Z缩放==1)
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void postScale(float scaleX, float scaleY)
	{
		postScale(scaleX,scaleY,1.0f);
	}
	/**
	 * 左乘（即后叠加操作）一个缩放矩阵
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 */
	public void postScale(float scaleX, float scaleY, float scaleZ)
	{
		matrix4Temp.setToScale(scaleX, scaleY, scaleZ);
		matrix4Temp.multiply(this, this);
	}

	/**
	 * 左乘（即后叠加操作）一个Z==0平面错切矩阵
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void postWarp(float warpX, float warpY)
	{
		matrix4Temp.setToWarp(warpX, warpY);
		matrix4Temp.multiply(this, this);
	}

	/**
	 * 右乘（即先叠加操作）一个围绕任意轴的旋转矩阵
	 * 
	 * @param v
	 * @param theta
	 *            弧度
	 */
	public void preRotate(C3D_Vector3 v, float theta)
	{
		matrix4Temp.setToRotate(v, theta);
		multiply(matrix4Temp);
	}

	/**
	 * 右乘（即先叠加操作）一个围绕Z轴的旋转矩阵
	 * 
	 * @param theta
	 *            弧度
	 */
	public void preRotate(float theta)
	{
		matrix4Temp.setToRotate(theta);
		multiply(matrix4Temp);
	}
	/**
	 * 右乘（即先叠加操作）一个Z==0平面位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 */
	public void preTranslate(float offX, float offY)
	{
		preTranslate(offX,offY,0.0f);
	}
	/**
	 * 右乘（即先叠加操作）一个位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 * @param offZ
	 */
	public void preTranslate(float offX, float offY, float offZ)
	{
		matrix4Temp.setToTranslate(offX, offY, offZ);
		multiply(matrix4Temp);
	}
	/**
	 * 右乘（即先叠加操作）一个XY方向的缩放矩阵(Z缩放==1)
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void preScale(float scaleX, float scaleY)
	{
		preScale(scaleX, scaleY, 1.0f);
	}
	/**
	 * 右乘（即先叠加操作）一个缩放矩阵
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 */
	public void preScale(float scaleX, float scaleY, float scaleZ)
	{
		matrix4Temp.setToScale(scaleX, scaleY, scaleZ);
		multiply(matrix4Temp);
	}

	/**
	 * 右乘（即先叠加操作）一个Z==0平面错切矩阵
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void preWarp(float warpX, float warpY)
	{
		matrix4Temp.setToWarp(warpX, warpY);
		multiply(matrix4Temp);
	}

	/**
	 * 设置成围绕任意轴的旋转矩阵
	 * 
	 * @param v
	 * @param theta
	 *            弧度
	 * @return 当前矩阵
	 */
	public C3D_Matrix4 setToRotate(C3D_Vector3 v, float theta)
	{
		float size2 = v.size2();
		float size = v.size();
		float s = (float) Math.sin(theta);
		float c = (float) Math.cos(theta);
		float t = (1) - c;
		data[M00] = t * v.x * v.x / size2 + c;
		data[M01] = t * v.x * v.y / size2 - (v.z * s / size);
		data[M02] = t * v.z * v.x / size2 + (v.y * s / size);
		data[M03]=0;
		data[M10] = t * v.x * v.y / size2 + (v.z * s / size);
		data[M11] = t * v.y * v.y / size2 + c;
		data[M12] = t * v.y * v.z / size2 - (v.x * s / size);
		data[M13]=0;
		data[M20] = t * v.z * v.x / size2 - (v.y * s / size);
		data[M21] = t * v.y * v.z / size2 + (v.x * s / size);
		data[M22] = t * v.z * v.z / size2 + c;
		data[M23]=0;
		data[M30]=0;
		data[M31]=0;
		data[M32]=0;
		data[M33]=1;
		return this;
	}

	/**
	 * 设置成围绕Z轴的旋转矩阵
	 * 
	 * @param theta
	 *            弧度
	 */
	public void setToRotate(float theta)
	{
		vector3Temp.setValue(0, 0, 1);
		setToRotate(vector3Temp, theta);
	}

	/**
	 * 设置成Z=0平面的位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 */
	public void setToTranslate(float offX, float offY)
	{
		System.arraycopy(matrix4Identify.data, 0, data, 0, 16);
		data[M03] = offX;
		data[M13] = offY;
		data[M23] = 0.0f;
	}

	/**
	 * 设置成位移矩阵
	 * 
	 * @param offX
	 * @param offY
	 * @param offZ
	 * @return 自身
	 */
	public C3D_Matrix4 setToTranslate(float offX, float offY, float offZ)
	{
		System.arraycopy(matrix4Identify.data, 0, data, 0, 16);
		data[M03] = offX;
		data[M13] = offY;
		data[M23] = offZ;
		return this;
	}
	/**
	 * 设置成Z==0平面的缩放矩阵
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void setToScale(float scaleX, float scaleY)
	{
		setToScale(scaleX, scaleY,0.0f);
	}
	/**
	 * 设置成缩放矩阵
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param scaleZ
	 * @return 自身
	 */
	public C3D_Matrix4 setToScale(float scaleX, float scaleY, float scaleZ)
	{
		data[M00] = scaleX;
		this.data[M01] = 0;
		this.data[M02] = 0;
		this.data[M03] = 0;
		this.data[M10] = 0;
		data[M11] = scaleY;
		this.data[M12] = 0;
		this.data[M13] = 0;
		this.data[M20] = 0;
		this.data[M21] = 0;
		data[M22] = scaleZ;
		this.data[M23] = 0;
		this.data[M30] = 0;
		this.data[M31] = 0;
		this.data[M32] = 0;
		this.data[M33] = 1;
		return this;
	}

	/**
	 * 设置成Z==0平面错切矩阵
	 * 
	 * @param warpX
	 * @param warpY
	 */
	public void setToWarp(float warpX, float warpY)
	{
		this.data[M00] = 1;
		data[M01] = warpX;
		this.data[M02] = 0;
		this.data[M03] = 0;
		data[M10] = warpY;
		this.data[M11] = 1;
		this.data[M12] = 0;
		this.data[M13] = 0;
		this.data[M20] = 0;
		this.data[M21] = 0;
		this.data[M22] = 1;
		this.data[M23] = 0;
		this.data[M30] = 0;
		this.data[M31] = 0;
		this.data[M32] = 0;
		this.data[M33] = 1;


	}

	/**
	 * 释放资源
	 */
	public void releaseRes()
	{
		if (data != null)
		{
			data = null;
		}
	}

	/**
	 * 将矩阵应用到矢量(左乘矢量)，并将结果存放回此矢量
	 * 
	 * @param vector
	 * @结果矢量
	 */
	public C3D_Vector3 mapVector(C3D_Vector3 vector)
	{
		mapVector(vector, vector);
		return vector;
	}

	/**
	 * 将矩阵应用到矢量A(左乘矢量A)，并将结果存放回矢量B
	 * 
	 * @param vectorA
	 *            矢量A
	 * @param vectorB
	 *            矢量B
	 */
	public void mapVector(C3D_Vector3 vectorA, C3D_Vector3 vectorB)
	{
		vectorA.getValue(f4_Temp0);
		mapVector(this.data, f4_Temp0, f4_Temp0);
		vectorB.setValue(f4_Temp0);
	}

	/**
	 * 将矩阵应用到矢量(左乘矢量)，并将结果存放回此矢量
	 * 
	 * @param vector
	 * @结果矢量
	 */
	public C3D_Vector4 mapVector(C3D_Vector4 vector)
	{
		mapVector(vector, vector);
		return vector;
	}

	/**
	 * 将矩阵应用到矢量A(左乘矢量A)，并将结果存放回矢量B
	 * 
	 * @param vectorA
	 *            矢量A
	 * @param vectorB
	 *            矢量B
	 */
	public void mapVector(C3D_Vector4 vectorA, C3D_Vector4 vectorB)
	{
		vectorA.getValue(f4_Temp0);
		mapVector(this.data, f4_Temp0, f4_Temp0);
		vectorB.setValue(f4_Temp0);
	}	
	/**
	 * 将矩阵m1应用到矢量A(左乘矢量A)，并将结果存放回矢量B
	 * 
	 * @param m1
	 *            矩阵m1
	 * @param va
	 *            矢量A
	 * @param vb
	 *            矢量B
	 */
	//#if(Platform=="Android")
	private static native void mapVector(float m1[], float va[], float vb[]);
	//#else
//@	private static void mapVector(float m1[], float va[], float vb[])
//@	{
//@		matrix4_mapvector(m1,va,vb);
//@	}
	//#endif
	/**
	 * 显示此矩阵内容
	 * 
	 * @param txt
	 */
	public void show(String txt)
	{
		C2D_Debug.logChunk("--" + txt + " --[");
		for (int i = 0; i < data.length; i++)
		{
			C2D_Debug.logChunk(data[i] + ",");
		}
		C2D_Debug.log("]");
	}
	private static float[] tmp4 =new float[4];
	private static void matrix4_mapvector(float[] matrix, float[] va, float[] vb) {
		
		tmp4[0] = matrix[M00] * va[0] + matrix[M01] * va[1] + matrix[M02] * va[2] + matrix[M03] * va[3];
		tmp4[1] = matrix[M10] * va[0] + matrix[M11] * va[1] + matrix[M12] * va[2] + matrix[M13] * va[3];
		tmp4[2] = matrix[M20] * va[0] + matrix[M21] * va[1] + matrix[M22] * va[2] + matrix[M23] * va[3];
		tmp4[3] = matrix[M30] * va[0] + matrix[M31] * va[1] + matrix[M32] * va[2] + matrix[M33] * va[3];
		System.arraycopy(tmp4, 0, vb, 0, 4);
	}
	private static float[] tmp16 =new float[16];
	static void matrix4_mul(float[] mata, float[] matb, float[] matc) {
		tmp16[M00] = mata[M00] * matb[M00] + mata[M01] * matb[M10] + mata[M02] * matb[M20] + mata[M03] * matb[M30];
		tmp16[M01] = mata[M00] * matb[M01] + mata[M01] * matb[M11] + mata[M02] * matb[M21] + mata[M03] * matb[M31];
		tmp16[M02] = mata[M00] * matb[M02] + mata[M01] * matb[M12] + mata[M02] * matb[M22] + mata[M03] * matb[M32];
		tmp16[M03] = mata[M00] * matb[M03] + mata[M01] * matb[M13] + mata[M02] * matb[M23] + mata[M03] * matb[M33];
		tmp16[M10] = mata[M10] * matb[M00] + mata[M11] * matb[M10] + mata[M12] * matb[M20] + mata[M13] * matb[M30];
		tmp16[M11] = mata[M10] * matb[M01] + mata[M11] * matb[M11] + mata[M12] * matb[M21] + mata[M13] * matb[M31];
		tmp16[M12] = mata[M10] * matb[M02] + mata[M11] * matb[M12] + mata[M12] * matb[M22] + mata[M13] * matb[M32];
		tmp16[M13] = mata[M10] * matb[M03] + mata[M11] * matb[M13] + mata[M12] * matb[M23] + mata[M13] * matb[M33];
		tmp16[M20] = mata[M20] * matb[M00] + mata[M21] * matb[M10] + mata[M22] * matb[M20] + mata[M23] * matb[M30];
		tmp16[M21] = mata[M20] * matb[M01] + mata[M21] * matb[M11] + mata[M22] * matb[M21] + mata[M23] * matb[M31];
		tmp16[M22] = mata[M20] * matb[M02] + mata[M21] * matb[M12] + mata[M22] * matb[M22] + mata[M23] * matb[M32];
		tmp16[M23] = mata[M20] * matb[M03] + mata[M21] * matb[M13] + mata[M22] * matb[M23] + mata[M23] * matb[M33];
		tmp16[M30] = mata[M30] * matb[M00] + mata[M31] * matb[M10] + mata[M32] * matb[M20] + mata[M33] * matb[M30];
		tmp16[M31] = mata[M30] * matb[M01] + mata[M31] * matb[M11] + mata[M32] * matb[M21] + mata[M33] * matb[M31];
		tmp16[M32] = mata[M30] * matb[M02] + mata[M31] * matb[M12] + mata[M32] * matb[M22] + mata[M33] * matb[M32];
		tmp16[M33] = mata[M30] * matb[M03] + mata[M31] * matb[M13] + mata[M32] * matb[M23] + mata[M33] * matb[M33];
		System.arraycopy(tmp16, 0, matc, 0, 16);
	}
	static float matrix4_det(float[] val) {
		return val[M30] * val[M21] * val[M12] * val[M03] - val[M20] * val[M31] * val[M12] * val[M03] - val[M30] * val[M11]
				* val[M22] * val[M03] + val[M10] * val[M31] * val[M22] * val[M03] + val[M20] * val[M11] * val[M32] * val[M03] - val[M10]
				* val[M21] * val[M32] * val[M03] - val[M30] * val[M21] * val[M02] * val[M13] + val[M20] * val[M31] * val[M02] * val[M13]
				+ val[M30] * val[M01] * val[M22] * val[M13] - val[M00] * val[M31] * val[M22] * val[M13] - val[M20] * val[M01] * val[M32]
				* val[M13] + val[M00] * val[M21] * val[M32] * val[M13] + val[M30] * val[M11] * val[M02] * val[M23] - val[M10] * val[M31]
				* val[M02] * val[M23] - val[M30] * val[M01] * val[M12] * val[M23] + val[M00] * val[M31] * val[M12] * val[M23] + val[M10]
				* val[M01] * val[M32] * val[M23] - val[M00] * val[M11] * val[M32] * val[M23] - val[M20] * val[M11] * val[M02] * val[M33]
				+ val[M10] * val[M21] * val[M02] * val[M33] + val[M20] * val[M01] * val[M12] * val[M33] - val[M00] * val[M21] * val[M12]
				* val[M33] - val[M10] * val[M01] * val[M22] * val[M33] + val[M00] * val[M11] * val[M22] * val[M33];
	}

	static boolean matrix4_inv(float[] val,float[] res) {
		float l_det = matrix4_det(val);
		if (l_det == 0) return false;
		tmp16[M00] = val[M12] * val[M23] * val[M31] - val[M13] * val[M22] * val[M31] + val[M13] * val[M21] * val[M32] - val[M11]
			* val[M23] * val[M32] - val[M12] * val[M21] * val[M33] + val[M11] * val[M22] * val[M33];
		tmp16[M01] = val[M03] * val[M22] * val[M31] - val[M02] * val[M23] * val[M31] - val[M03] * val[M21] * val[M32] + val[M01]
			* val[M23] * val[M32] + val[M02] * val[M21] * val[M33] - val[M01] * val[M22] * val[M33];
		tmp16[M02] = val[M02] * val[M13] * val[M31] - val[M03] * val[M12] * val[M31] + val[M03] * val[M11] * val[M32] - val[M01]
			* val[M13] * val[M32] - val[M02] * val[M11] * val[M33] + val[M01] * val[M12] * val[M33];
		tmp16[M03] = val[M03] * val[M12] * val[M21] - val[M02] * val[M13] * val[M21] - val[M03] * val[M11] * val[M22] + val[M01]
			* val[M13] * val[M22] + val[M02] * val[M11] * val[M23] - val[M01] * val[M12] * val[M23];
		tmp16[M10] = val[M13] * val[M22] * val[M30] - val[M12] * val[M23] * val[M30] - val[M13] * val[M20] * val[M32] + val[M10]
			* val[M23] * val[M32] + val[M12] * val[M20] * val[M33] - val[M10] * val[M22] * val[M33];
		tmp16[M11] = val[M02] * val[M23] * val[M30] - val[M03] * val[M22] * val[M30] + val[M03] * val[M20] * val[M32] - val[M00]
			* val[M23] * val[M32] - val[M02] * val[M20] * val[M33] + val[M00] * val[M22] * val[M33];
		tmp16[M12] = val[M03] * val[M12] * val[M30] - val[M02] * val[M13] * val[M30] - val[M03] * val[M10] * val[M32] + val[M00]
			* val[M13] * val[M32] + val[M02] * val[M10] * val[M33] - val[M00] * val[M12] * val[M33];
		tmp16[M13] = val[M02] * val[M13] * val[M20] - val[M03] * val[M12] * val[M20] + val[M03] * val[M10] * val[M22] - val[M00]
			* val[M13] * val[M22] - val[M02] * val[M10] * val[M23] + val[M00] * val[M12] * val[M23];
		tmp16[M20] = val[M11] * val[M23] * val[M30] - val[M13] * val[M21] * val[M30] + val[M13] * val[M20] * val[M31] - val[M10]
			* val[M23] * val[M31] - val[M11] * val[M20] * val[M33] + val[M10] * val[M21] * val[M33];
		tmp16[M21] = val[M03] * val[M21] * val[M30] - val[M01] * val[M23] * val[M30] - val[M03] * val[M20] * val[M31] + val[M00]
			* val[M23] * val[M31] + val[M01] * val[M20] * val[M33] - val[M00] * val[M21] * val[M33];
		tmp16[M22] = val[M01] * val[M13] * val[M30] - val[M03] * val[M11] * val[M30] + val[M03] * val[M10] * val[M31] - val[M00]
			* val[M13] * val[M31] - val[M01] * val[M10] * val[M33] + val[M00] * val[M11] * val[M33];
		tmp16[M23] = val[M03] * val[M11] * val[M20] - val[M01] * val[M13] * val[M20] - val[M03] * val[M10] * val[M21] + val[M00]
			* val[M13] * val[M21] + val[M01] * val[M10] * val[M23] - val[M00] * val[M11] * val[M23];
		tmp16[M30] = val[M12] * val[M21] * val[M30] - val[M11] * val[M22] * val[M30] - val[M12] * val[M20] * val[M31] + val[M10]
			* val[M22] * val[M31] + val[M11] * val[M20] * val[M32] - val[M10] * val[M21] * val[M32];
		tmp16[M31] = val[M01] * val[M22] * val[M30] - val[M02] * val[M21] * val[M30] + val[M02] * val[M20] * val[M31] - val[M00]
			* val[M22] * val[M31] - val[M01] * val[M20] * val[M32] + val[M00] * val[M21] * val[M32];
		tmp16[M32] = val[M02] * val[M11] * val[M30] - val[M01] * val[M12] * val[M30] - val[M02] * val[M10] * val[M31] + val[M00]
			* val[M12] * val[M31] + val[M01] * val[M10] * val[M32] - val[M00] * val[M11] * val[M32];
		tmp16[M33] = val[M01] * val[M12] * val[M20] - val[M02] * val[M11] * val[M20] + val[M02] * val[M10] * val[M21] - val[M00]
			* val[M12] * val[M21] - val[M01] * val[M10] * val[M22] + val[M00] * val[M11] * val[M22];

		float inv_det = 1.0f / l_det;
		res[M00] = tmp16[M00] * inv_det;
		res[M01] = tmp16[M01] * inv_det;
		res[M02] = tmp16[M02] * inv_det;
		res[M03] = tmp16[M03] * inv_det;
		res[M10] = tmp16[M10] * inv_det;
		res[M11] = tmp16[M11] * inv_det;
		res[M12] = tmp16[M12] * inv_det;
		res[M13] = tmp16[M13] * inv_det;
		res[M20] = tmp16[M20] * inv_det;
		res[M21] = tmp16[M21] * inv_det;
		res[M22] = tmp16[M22] * inv_det;
		res[M23] = tmp16[M23] * inv_det;
		res[M30] = tmp16[M30] * inv_det;
		res[M31] = tmp16[M31] * inv_det;
		res[M32] = tmp16[M32] * inv_det;
		res[M33] = tmp16[M33] * inv_det;
		return true;
	}
}
