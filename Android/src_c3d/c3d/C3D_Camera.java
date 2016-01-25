package c3d;

import c2d.lang.math.C2D_Math;
import c3d.util.math.C3D_Matrix4;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vector4;
import c3d.util.math.C3D_Vertex3;
import c3d.util.math.C3D_Vertex4;

/**
 * <p>
 * Title:Camera类
 * </p>
 * 
 * <p>
 * Description:照相机类
 * </p>
 * 
 * @author Andrew Fan
 * @version 1.0
 */
public class C3D_Camera
{
	public C3D_Matrix4 viewMatrix; // 照相机视角变换矩阵
	public C3D_Matrix4 viewMatrixRS;// 照相机视角变换矩阵的逆矩阵
	public C3D_Matrix4 projectMatrix;// 透视投影矩阵
	public float right, top; // 屏幕右上角坐标
	public float left, bottom; // 屏幕左下角坐标
	public C3D_Vertex3 eyePosition; // 视点
	public C3D_Vector3 upDirection; // 从视点看到的屏幕的垂直方向
	public C3D_Vector3 toDirection; // 从屏幕到视点的方向
	public C3D_Vector3 rightDirection; // 从视点看到的屏幕的右面方向
	public boolean parallel = false; // 是否平行投影的开关
	public static float near; // 焦点长度，也是近平面距离
	public C3D_Vertex3 nearPoint = new C3D_Vertex3();// 近屏幕的中心点
	public C3D_Vector3 directVector = new C3D_Vector3();// 摄像机的方向
	// private Vector3 directionL=new Vector3(); //视锥体左边缘
	// private Vector3 directionR=new Vector3(); //视锥体左边缘
	// private Vector3 directionU=new Vector3(); //视锥体左边缘
	// private Vector3 directionD=new Vector3(); //视锥体左边缘
	public static double far = 20;// 远平面距离
	public static double farSqr = far * far;// 远平面距离的平方
	public float pA;// 透视投影辅助参数A
	public float pB;// 透视投影辅助参数B
	public float fieldOfView; // X轴方向的视角(弧度),屏幕的纵横率设定为1
	public final static int LEFT_VIEWVOLUME = 0x1; // viewvolume的左面
	public final static int RIGHT_VIEWVOLUME = 0x2; // viewvolume的右面
	public final static int BOTTOM_VIEWVOLUME = 0x4;// viewvolume的下面
	public final static int TOP_VIEWVOLUME = 0x8; // viewvolume的上面
	public final static int NEAR_VIEWVOLUME = 0x10; // viewvolume的后面
	public final static int FAR_VIEWVOLUME = 0x20; // viewvolume的前面

	// 临时顶点
	private static C3D_Vector4 Vertex4Temp0 = new C3D_Vector4();
	private static C3D_Vector4 Vertex4Temp1 = new C3D_Vector4();
	private static C3D_Vector3 Vector3Temp0 = new C3D_Vector3();
	// 临时矩阵
	private static C3D_Matrix4 matrix4Temp_0 = new C3D_Matrix4();

	// 构造函数
	// 缺省X轴为rightDirection,
	// Y轴upDirection，Z轴为toDirection
	// 通常两个轴确定的话，剩下的轴方向用右手法则求出
	// 视点缺省为原点(0,0,0)
	// 屏幕的缺省位置为Z = -1.0
	public C3D_Camera()
	{
		eyePosition = new C3D_Vertex3(0, 0, 0);
		upDirection = new C3D_Vector3(0, 1, 0);
		toDirection = new C3D_Vector3(0, 0, 1);
		rightDirection = new C3D_Vector3(1, 0, 0);
		viewMatrix = new C3D_Matrix4();
		projectMatrix = new C3D_Matrix4();
		parallel = false;
		setFieldOfView((float) (C2D_Math.PI / 2));
		setFocalLength(1);
		setViewMatrix();
		setProjectMatrix();
	}

	// 设置视野
	public static void setViewFar(float farNew)
	{
		far = farNew;
		farSqr = far * far;
	}

	// 复位照相机
	public void reset()
	{
		eyePosition.x = eyePosition.y = eyePosition.z = 0;
		upDirection.x = upDirection.z = 0;
		upDirection.y = 1;
		toDirection.x = toDirection.y = 0;
		toDirection.z = 1;
		rightDirection.x = 1;
		rightDirection.y = rightDirection.z = 0;
		viewMatrix.identity();
		setViewMatrix();
	}

	// 设置视角矩阵
	private void setViewMatrix()
	{
		C3D_Matrix4 m = new C3D_Matrix4();
		m.data[0] = rightDirection.x;
		m.data[1] = upDirection.x;
		m.data[2] = toDirection.x;
		m.data[3] = eyePosition.x;
		m.data[4] = rightDirection.y;
		m.data[5] = upDirection.y;
		m.data[6] = toDirection.y;
		m.data[7] = eyePosition.y;
		m.data[8] = rightDirection.z;
		m.data[9] = upDirection.z;
		m.data[10] = toDirection.z;
		m.data[11] = eyePosition.z;
		viewMatrix.setValue(m);
		setViewMatrixInverse();
	}

	private void setViewMatrixInverse()
	{
		viewMatrix.inverse(viewMatrixRS);
	}

	// 设置投影变换矩阵
	private void setProjectMatrix()
	{
		pA = (float) (-((far + near) / (far - near)));
		pB = (float) (-2 * far * near / (far - near));
		projectMatrix.data[0] = (float) ((2 * near) / (right - left));
		projectMatrix.data[1] = 0;
		projectMatrix.data[2] = ((right + left)) / (right - left);
		projectMatrix.data[3] = 0;
		projectMatrix.data[4] = 0;
		projectMatrix.data[5] = (float) ((2 * near) / (top - bottom));
		projectMatrix.data[6] = ((top + bottom)) / (top - bottom);
		projectMatrix.data[7] = 0;
		projectMatrix.data[8] = 0;
		projectMatrix.data[9] = 0;
		projectMatrix.data[10] = pA;
		projectMatrix.data[11] = pB;
		projectMatrix.data[12] = 0;
		projectMatrix.data[13] = 0;
		projectMatrix.data[14] = (-1);
		projectMatrix.data[15] = 0;
	}

	// 屏幕的计算(内部使用)
	private void setScreenDetail()
	{
		right = (float) (near * C2D_Math.tan(fieldOfView / 2));
		top = right;
		left = -right;
		bottom = -top;
	}

	// 返回屏幕的横向和纵向的照相机空间的大小
	public double getScreenX()
	{
		return (right - left);
	}

	public double getScreenY()
	{
		return (top - bottom);
	}

	// 区域代码判断
	public int areaCode(C3D_Vertex4 v)
	{
		float x = v.x;
		float y = v.y;
		float z = v.z;
		float w = v.w;
		int code = 0;
		if (x + w < 0)
		{
			code |= LEFT_VIEWVOLUME;
		}
		else if (x - w > 0)
		{
			code |= RIGHT_VIEWVOLUME;
		}
		if (y + w < 0)
		{
			code |= BOTTOM_VIEWVOLUME;
		}
		else if (y - w > 0)
		{
			code |= TOP_VIEWVOLUME;
		}
		if (z + w < 0)
		{
			code |= NEAR_VIEWVOLUME;
		}
		else if (z - w > 0)
		{
			code |= FAR_VIEWVOLUME;
		}
		return code;
	}

	// 从照相机坐标转换到世界坐标
	public void getWorldPositionL(C3D_Vector3 v)
	{
		Vertex4Temp0.setValue(v);
		viewMatrix.mapVector(Vertex4Temp0, Vertex4Temp1);
		v.setValue(Vertex4Temp1);
	}

	public C3D_Vector3 getWorldPosition(C3D_Vector3 v)
	{
		Vertex4Temp0.setValue(v);
		viewMatrix.mapVector(Vertex4Temp0, Vertex4Temp1);
		return new C3D_Vector3().setValue(Vertex4Temp1);
	}

	// 从世界坐标转换到照相机坐标
	public void getCameraPositionL(C3D_Vertex3 v)
	{
		Vertex4Temp0.setValue(v);
		viewMatrixRS.mapVector(Vertex4Temp0, Vertex4Temp1);
		v.setValue(Vertex4Temp1);
	}

	public C3D_Vector3 getCameraPosition(C3D_Vertex3 v)
	{
		Vertex4Temp0.setValue(v);
		viewMatrixRS.mapVector(Vertex4Temp0, Vertex4Temp1);
		return new C3D_Vertex3().setValue(Vertex4Temp1);
	}

	// 从照相机坐标投影到CVV,注意投影之后的Z被约束在[-1,1]的空间中,不在此区域标明不在CVV
	public void getProjectPositionL(C3D_Vertex4 v4Save, C3D_Vertex3 v)
	{
		Vertex4Temp0.setValue(v);
		projectMatrix.mapVector(v4Save, Vertex4Temp1);
		v4Save.setValue(Vertex4Temp1);
	}

	// 设置视点
	public void setEyePosition(C3D_Vertex3 eyePosition)
	{
		this.eyePosition.x = eyePosition.x;
		this.eyePosition.y = eyePosition.y;
		this.eyePosition.z = eyePosition.z;
		setViewMatrix();
	}

	public void setEyePosition(float x, float y, float z)
	{
		eyePosition.x = x;
		eyePosition.y = y;
		eyePosition.z = z;
		setViewMatrix();
	}

	public C3D_Vertex3 getEyePosition()
	{
		return eyePosition;
	}

	// 视角设定
	public void setFieldOfView(float fov)
	{
		if (fov <= 0 || fov >= C2D_Math.PI)
		{
			throw new Error("view error");
		}
		fieldOfView = fov;
		setScreenDetail();
		// float tan=Maths.tan(fov/2);
		// directionL.setValue(-tan,0,(-1));
		// directionR.setValue(tan,0,(-1));
		// directionU.setValue(0,tan,(-1));
		// directionD.setValue(0,-tan,(-1));
	}

	public double getFieldOfView()
	{
		return fieldOfView;
	}

	// 焦点距离
	public void setFocalLength(float focalLength)
	{
		if (focalLength <= 0)
		{
			throw new Error("焦点距离错误");
		}
		this.near = focalLength;
		nearPoint.setValue(0, 0, -near);
		directVector.setValue(0, 0, -1);
		setScreenDetail();
	}

	public double getFocalLength()
	{
		return near;
	}

	// 投影方法设定
	public void setParallel(boolean on)
	{
		parallel = on;
	}

	public boolean getParallel()
	{
		return parallel;
	}

	// 照相机坐标的旋转(围绕世界中心的指定轴)
	public void rotate(C3D_Vector3 axis, float theta)
	{
		theta = (float) C2D_Math.standirdAngle2(theta);
		matrix4Temp_0.identity().setToRotate(axis, theta);
		viewMatrix.multiply(matrix4Temp_0);
		rightDirection.x = viewMatrix.data[0];
		rightDirection.y = viewMatrix.data[4];
		rightDirection.z = viewMatrix.data[8];
		upDirection.x = viewMatrix.data[1];
		upDirection.y = viewMatrix.data[5];
		upDirection.z = viewMatrix.data[9];
		toDirection.x = viewMatrix.data[2];
		toDirection.y = viewMatrix.data[6];
		toDirection.z = viewMatrix.data[10];
		eyePosition.x = viewMatrix.data[3];
		eyePosition.y = viewMatrix.data[7];
		eyePosition.z = viewMatrix.data[11];
		setViewMatrixInverse();
	}

	public void rotate(float x, float y, float z, float theta)
	{
		C3D_Vector3 v = new C3D_Vector3(x, y, z);
		rotate(v, theta);
	}

	// 围绕视点进行X轴旋转
	public void rotateX(float theta)
	{
		// 记录视点位置
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// 平移到世界中心
		this.setEyePosition(0, 0, 0);
		// 旋转
		rotate(1024, 0, 0, theta);
		// 还原到视点位置
		this.setEyePosition(eX, eY, eZ);
	}

	// 围绕视点进行Y轴旋转
	public void rotateY(float theta)
	{
		// 记录视点位置
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// 平移到世界中心
		this.setEyePosition(0, 0, 0);
		// 旋转
		rotate(0, 1024, 0, theta);
		// 还原到视点位置
		this.setEyePosition(eX, eY, eZ);
	}

	// 围绕视点进行X轴旋转
	public void rotateZ(float theta)
	{
		// 记录视点位置
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// 平移到世界中心
		this.setEyePosition(0, 0, 0);
		// 旋转
		rotate(0, 0, 1024, theta);
		// 还原到视点位置
		this.setEyePosition(eX, eY, eZ);
	}

	public void forward(float step)
	{
		float sqr = C2D_Math.sqrt(toDirection.x * toDirection.x + toDirection.y * toDirection.y + toDirection.z * toDirection.z);
		if (sqr == 0)
		{
			return;
		}
		float stepX = -(toDirection.x) * step / sqr;
		float stepY = -(toDirection.y) * step / sqr;
		float stepZ = -(toDirection.z) * step / sqr;
		this.setEyePosition(eyePosition.x + stepX, eyePosition.y + stepY, eyePosition.z + stepZ);
	}

	// 释放资源
	public void releaseRes()
	{
		if (viewMatrix != null)
		{
			viewMatrix.releaseRes();
			viewMatrix = null;
		}
		if (viewMatrixRS != null)
		{
			viewMatrixRS.releaseRes();
			viewMatrixRS = null;
		}
		if (projectMatrix != null)
		{
			projectMatrix.releaseRes();
			projectMatrix = null;
		}
		eyePosition = null;
		upDirection = null;
		toDirection = null;
		rightDirection = null;
		nearPoint = null;
		directVector = null;
	}

}
