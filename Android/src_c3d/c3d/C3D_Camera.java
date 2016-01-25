package c3d;

import c2d.lang.math.C2D_Math;
import c3d.util.math.C3D_Matrix4;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vector4;
import c3d.util.math.C3D_Vertex3;
import c3d.util.math.C3D_Vertex4;

/**
 * <p>
 * Title:Camera��
 * </p>
 * 
 * <p>
 * Description:�������
 * </p>
 * 
 * @author Andrew Fan
 * @version 1.0
 */
public class C3D_Camera
{
	public C3D_Matrix4 viewMatrix; // ������ӽǱ任����
	public C3D_Matrix4 viewMatrixRS;// ������ӽǱ任����������
	public C3D_Matrix4 projectMatrix;// ͸��ͶӰ����
	public float right, top; // ��Ļ���Ͻ�����
	public float left, bottom; // ��Ļ���½�����
	public C3D_Vertex3 eyePosition; // �ӵ�
	public C3D_Vector3 upDirection; // ���ӵ㿴������Ļ�Ĵ�ֱ����
	public C3D_Vector3 toDirection; // ����Ļ���ӵ�ķ���
	public C3D_Vector3 rightDirection; // ���ӵ㿴������Ļ�����淽��
	public boolean parallel = false; // �Ƿ�ƽ��ͶӰ�Ŀ���
	public static float near; // ���㳤�ȣ�Ҳ�ǽ�ƽ�����
	public C3D_Vertex3 nearPoint = new C3D_Vertex3();// ����Ļ�����ĵ�
	public C3D_Vector3 directVector = new C3D_Vector3();// ������ķ���
	// private Vector3 directionL=new Vector3(); //��׶�����Ե
	// private Vector3 directionR=new Vector3(); //��׶�����Ե
	// private Vector3 directionU=new Vector3(); //��׶�����Ե
	// private Vector3 directionD=new Vector3(); //��׶�����Ե
	public static double far = 20;// Զƽ�����
	public static double farSqr = far * far;// Զƽ������ƽ��
	public float pA;// ͸��ͶӰ��������A
	public float pB;// ͸��ͶӰ��������B
	public float fieldOfView; // X�᷽����ӽ�(����),��Ļ���ݺ����趨Ϊ1
	public final static int LEFT_VIEWVOLUME = 0x1; // viewvolume������
	public final static int RIGHT_VIEWVOLUME = 0x2; // viewvolume������
	public final static int BOTTOM_VIEWVOLUME = 0x4;// viewvolume������
	public final static int TOP_VIEWVOLUME = 0x8; // viewvolume������
	public final static int NEAR_VIEWVOLUME = 0x10; // viewvolume�ĺ���
	public final static int FAR_VIEWVOLUME = 0x20; // viewvolume��ǰ��

	// ��ʱ����
	private static C3D_Vector4 Vertex4Temp0 = new C3D_Vector4();
	private static C3D_Vector4 Vertex4Temp1 = new C3D_Vector4();
	private static C3D_Vector3 Vector3Temp0 = new C3D_Vector3();
	// ��ʱ����
	private static C3D_Matrix4 matrix4Temp_0 = new C3D_Matrix4();

	// ���캯��
	// ȱʡX��ΪrightDirection,
	// Y��upDirection��Z��ΪtoDirection
	// ͨ��������ȷ���Ļ���ʣ�µ��᷽�������ַ������
	// �ӵ�ȱʡΪԭ��(0,0,0)
	// ��Ļ��ȱʡλ��ΪZ = -1.0
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

	// ������Ұ
	public static void setViewFar(float farNew)
	{
		far = farNew;
		farSqr = far * far;
	}

	// ��λ�����
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

	// �����ӽǾ���
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

	// ����ͶӰ�任����
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

	// ��Ļ�ļ���(�ڲ�ʹ��)
	private void setScreenDetail()
	{
		right = (float) (near * C2D_Math.tan(fieldOfView / 2));
		top = right;
		left = -right;
		bottom = -top;
	}

	// ������Ļ�ĺ���������������ռ�Ĵ�С
	public double getScreenX()
	{
		return (right - left);
	}

	public double getScreenY()
	{
		return (top - bottom);
	}

	// ��������ж�
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

	// �����������ת������������
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

	// ����������ת�������������
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

	// �����������ͶӰ��CVV,ע��ͶӰ֮���Z��Լ����[-1,1]�Ŀռ���,���ڴ������������CVV
	public void getProjectPositionL(C3D_Vertex4 v4Save, C3D_Vertex3 v)
	{
		Vertex4Temp0.setValue(v);
		projectMatrix.mapVector(v4Save, Vertex4Temp1);
		v4Save.setValue(Vertex4Temp1);
	}

	// �����ӵ�
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

	// �ӽ��趨
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

	// �������
	public void setFocalLength(float focalLength)
	{
		if (focalLength <= 0)
		{
			throw new Error("����������");
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

	// ͶӰ�����趨
	public void setParallel(boolean on)
	{
		parallel = on;
	}

	public boolean getParallel()
	{
		return parallel;
	}

	// ������������ת(Χ���������ĵ�ָ����)
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

	// Χ���ӵ����X����ת
	public void rotateX(float theta)
	{
		// ��¼�ӵ�λ��
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// ƽ�Ƶ���������
		this.setEyePosition(0, 0, 0);
		// ��ת
		rotate(1024, 0, 0, theta);
		// ��ԭ���ӵ�λ��
		this.setEyePosition(eX, eY, eZ);
	}

	// Χ���ӵ����Y����ת
	public void rotateY(float theta)
	{
		// ��¼�ӵ�λ��
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// ƽ�Ƶ���������
		this.setEyePosition(0, 0, 0);
		// ��ת
		rotate(0, 1024, 0, theta);
		// ��ԭ���ӵ�λ��
		this.setEyePosition(eX, eY, eZ);
	}

	// Χ���ӵ����X����ת
	public void rotateZ(float theta)
	{
		// ��¼�ӵ�λ��
		float eX = eyePosition.x;
		float eY = eyePosition.y;
		float eZ = eyePosition.z;
		// ƽ�Ƶ���������
		this.setEyePosition(0, 0, 0);
		// ��ת
		rotate(0, 0, 1024, theta);
		// ��ԭ���ӵ�λ��
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

	// �ͷ���Դ
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
