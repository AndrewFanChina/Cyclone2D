package c3d;

import c3d.util.math.C3D_Matrix4;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex3;

/**
 * 3D模型类
 * 
 * @author Andrew
 * 
 */
public class C3D_Model
{
	public static final byte MT_TYPE_N = 0; // 非通用模型
	public static final byte MT_TYPE_C = MT_TYPE_N + 1;// 通用模型
	public static final byte MT_TYPE_R = MT_TYPE_C + 1;// 路面模型
	public static final byte MT_TYPE_WI = MT_TYPE_R + 1;// 路面内边缘模型
	public static final byte MT_TYPE_WO = MT_TYPE_WI + 1;// 路面外边缘模型
	public static final byte MT_TYPE_O = MT_TYPE_WO + 1;// 对象模型(一个顶点)
	public static final byte MT_TYPE_CAR = MT_TYPE_O + 1;// 赛车对象模型(一个顶点)
	public C3D_Vertex3 bottomCenter;// 位于世界中的底面坐标(对于通用模型，在投影时需要将模型先移动到此位置)
	public C3D_Vertex3 boundingCenter;// 该物体绑定球位于世界中的坐标
	public C3D_ModelData modelData;// 模型数据
	public byte modelType;// 模型类型
	public long maxR = 0;// 最大物体的包围球半径
	public C3D_Matrix4 transMartrix = null;
	public byte stagePropID = -1;// 场景道具ID
	public byte playerPropID = -1;// 玩家道具ID
	public short propModelActorID = -1;// 道具模型ActorID
	public boolean isEnable = true;// 是否有效
	public boolean isVisible = true;// 是否可见
	public int timeVisible = 0;// 可见倒计时
	public static int TIME_REFRESH = 200;
	public boolean alwaysVisible = false;// 是否不受视野范围约束
	public short followRoadID = 0;// 跟随路面ID
	public static int zoomPercents[] = new int[]
	{ 100, 90, 80, 70, 60, 50, 40, 30 };
	// public static int ObjM_MostFar=1024*4;//对象模型能被看到的最远距离
	public static int ObjM_offY = 15;// 对象模型显示偏移

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
		if (modelType == MT_TYPE_N)// 非通用模型的中心计算
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
		if (modelType == MT_TYPE_C)// 通用模型的绑定球中心计算
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

	// 建立变形矩阵
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

	// 道具被玩家获得
	public void got()
	{
		this.isVisible = false;
		this.timeVisible = TIME_REFRESH;
	}

	// 释放资源
	public void releaseRes()
	{
		bottomCenter = null;
		boundingCenter = null;
		modelData = null;
		transMartrix = null;
	}
}
