package c3d;

import c3d.util.math.C3D_Matrix4;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex3;

/**
 * 3Dģ����
 * 
 * @author Andrew
 * 
 */
public class C3D_Model
{
	public static final byte MT_TYPE_N = 0; // ��ͨ��ģ��
	public static final byte MT_TYPE_C = MT_TYPE_N + 1;// ͨ��ģ��
	public static final byte MT_TYPE_R = MT_TYPE_C + 1;// ·��ģ��
	public static final byte MT_TYPE_WI = MT_TYPE_R + 1;// ·���ڱ�Եģ��
	public static final byte MT_TYPE_WO = MT_TYPE_WI + 1;// ·�����Եģ��
	public static final byte MT_TYPE_O = MT_TYPE_WO + 1;// ����ģ��(һ������)
	public static final byte MT_TYPE_CAR = MT_TYPE_O + 1;// ��������ģ��(һ������)
	public C3D_Vertex3 bottomCenter;// λ�������еĵ�������(����ͨ��ģ�ͣ���ͶӰʱ��Ҫ��ģ�����ƶ�����λ��)
	public C3D_Vertex3 boundingCenter;// ���������λ�������е�����
	public C3D_ModelData modelData;// ģ������
	public byte modelType;// ģ������
	public long maxR = 0;// �������İ�Χ��뾶
	public C3D_Matrix4 transMartrix = null;
	public byte stagePropID = -1;// ��������ID
	public byte playerPropID = -1;// ��ҵ���ID
	public short propModelActorID = -1;// ����ģ��ActorID
	public boolean isEnable = true;// �Ƿ���Ч
	public boolean isVisible = true;// �Ƿ�ɼ�
	public int timeVisible = 0;// �ɼ�����ʱ
	public static int TIME_REFRESH = 200;
	public boolean alwaysVisible = false;// �Ƿ�����Ұ��ΧԼ��
	public short followRoadID = 0;// ����·��ID
	public static int zoomPercents[] = new int[]
	{ 100, 90, 80, 70, 60, 50, 40, 30 };
	// public static int ObjM_MostFar=1024*4;//����ģ���ܱ���������Զ����
	public static int ObjM_offY = 15;// ����ģ����ʾƫ��

	public C3D_Model(byte modelTypeT, C3D_ModelData modelDataT)
	{
		modelType = modelTypeT;
		modelData = modelDataT;
	}

	public void checkCenter()
	{
		if (modelData == null)
		{
			return;
		}
		if (modelType == MT_TYPE_N)// ��ͨ��ģ�͵����ļ���
		{
			if (bottomCenter == null)
			{
				bottomCenter = new C3D_Vertex3();
				bottomCenter.setValue(modelData.getModelBottomCenter());
			}
			if (boundingCenter == null)
			{
				boundingCenter = new C3D_Vertex3();
				boundingCenter.setValue(modelData.getModelCenter());
			}
			maxR = modelData.getModelBoudingR();
		}
		if (modelType == MT_TYPE_C)// ͨ��ģ�͵İ������ļ���
		{
			if (boundingCenter == null)
			{
				boundingCenter = new C3D_Vertex3();
				boundingCenter.setValue(modelData.getModelCenter());
				boundingCenter.setValue(bottomCenter.x, bottomCenter.y + boundingCenter.y, bottomCenter.z);
			}
			maxR = modelData.getModelBoudingR();
		}
	}

	// �������ξ���
	public void createTransMartrix(int angle, C3D_Vector3 vScale)
	{
		C3D_Matrix4 trans = new C3D_Matrix4().setToTranslate(bottomCenter.x, bottomCenter.y, bottomCenter.z);
		C3D_Matrix4 rotate = new C3D_Matrix4().setToRotate(new C3D_Vector3(0, 1024, 0), angle);
		C3D_Matrix4 scale = new C3D_Matrix4().setToScale(vScale.x, vScale.y, vScale.z);
		C3D_Matrix4 complex = new C3D_Matrix4();
		complex.setValue(scale.multiply(rotate));
		complex.setValue(complex.multiply(trans));
		transMartrix = complex;
	}

	// ���߱���һ��
	public void got()
	{
		this.isVisible = false;
		this.timeVisible = TIME_REFRESH;
	}

	// �ͷ���Դ
	public void releaseRes()
	{
		bottomCenter = null;
		boundingCenter = null;
		modelData = null;
		transMartrix = null;
	}
}
