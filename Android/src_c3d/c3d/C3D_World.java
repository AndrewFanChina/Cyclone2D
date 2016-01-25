package c3d;

import java.io.DataInputStream;
import java.util.Vector;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.C2D_SortableArrayL;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Graphics;
import c3d.util.math.C3D_Triangle;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex2;
import c3d.util.math.C3D_Vertex3;
import c3d.util.math.C3D_Vertex4;

/**
 * 3D世界类
 * 
 * @author AndrewFan
 * 
 */
public class C3D_World extends C2D_View
{
	public static C3D_Camera activeCamera = null; // 当前的活动相机
	protected Vector vModelDatas = new Vector(); // 所有模型的原型数据
	protected static C3D_LinkedModels vModels = new C3D_LinkedModels();// 当前世界中的所有模型
	protected static Vector vAlwaysShowModels = new Vector();// 一直可见的模型
	public static int nbViewRoadChunk = 20;// 可以看到的路面数
	public static int nbViewObjectChunk = 15;// 可以看到物体的路面数
	public C3D_RoadModel road = null;
	private static int BUFF_STEP = 8;// 缓冲单元数据长度[x1、y2、x2、y2、x3、y3、Z、color],一般颜色表示三角形，0xAAAAAA表示路段节点
	private static int BUFFER_SIZE = 1500;
	private static int projectBuffer[];// 投影缓冲
	private static C2D_SortableArrayL projectBufferOrder;// 64位投影缓冲排序[32位正Z、32位ID]
	public static int buffElementNB = 0;// 使用的缓冲单元数目
	public static int buffDeep = 0;// 使用的缓冲数据长度
	public static int modelDeep = 0;// 绘制的模型数
	public static int cutFace = 0;// 背面消除数
	public static int roadColorA = 0x555555;// 路段颜色A
	public static int roadColorB = 0x555555;// 路段颜色B
	public static int roadColorIA = 0x555555;// 内边沿颜色A
	public static int roadColorIB = 0x555555;// 内边沿颜色B
	public static int roadColorOA = 0x555555;// 外边沿颜色A
	public static int roadColorOB = 0x555555;// 外边沿颜色B
	public static int roadColorC = 0xFFFFFF;// 路面车道颜色
	// 场景范围
	protected static int horizonY = 200;// 地平线高度
	protected static int viewH, viewMax, viewGapX, viewGapY;
	// 赛道上的道具分类
	public static final byte prop_star = 0;// 星星
	public static final byte prop_box = 1;// 箱子
	public static final byte prop_accelerate = 2;// 加速器
	public static final byte prop_lawn = 3;// 草坪
	public static final byte prop_pool = 4;// 水洼
	public static final byte prop_star_b = 5;// 任务星星
	public static final byte prop_others = 6;// 其它装饰道具
	// 临时顶点
	public static C3D_Vertex4 Vertex4Temp0 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp1 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp2 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp3 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp4 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp5 = new C3D_Vertex4();
	public static C3D_Vertex4 Vertex4Temp6 = new C3D_Vertex4();
	public static C3D_Vertex3 Vertex3Temp0 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp1 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp2 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp3 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp4 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp5 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp6 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp7 = new C3D_Vertex3();
	public static C3D_Vertex3 Vertex3Temp8 = new C3D_Vertex3();
	private static C3D_Vector3 Vector3Temp0 = new C3D_Vector3();
	public static C3D_Vertex2 Vertex2Temp0 = new C3D_Vertex2();
	public static C3D_Vertex2 Vertex2Temp1 = new C3D_Vertex2();
	// 路面参数
	private C3D_Vertex3 cameraPosOrg = new C3D_Vertex3();// 当前相机位置(未添加震动参量)
	public C3D_Vertex3 currentCameraPos = new C3D_Vertex3();// 当前相机位置(添加震动参量)
	public C3D_Vector3 roadNavigate = new C3D_Vector3();// 路面朝向
	// 相机参数
	public static float angleHorizon = 0;// 摄像机水平转角
	protected static float cameraHeight = 256;// 相机高度
	public long cameraDisToCar = 163840;// 摄像机到车之间的标准距离(XZ平面)
	protected float cameraAngleEle = (float) -C2D_Math.PI / 20;// 摄像机仰角
	protected static long shakeOffsetX = 0;// 相机X向震动偏移
	protected static long shakeOffsetY = 0;// 相机Y向震动偏移
	protected static long shakeOffsetZ = 0;// 相机Z向震动偏移
	public int roadMosetHigh = 0; // 最高的路面
	public static boolean showRoadEdge = true;
	C3D_Triangle tempTriangle;
	/** 跟随的导航器 */
	public static C3D_Navigator m_followNg = null;
	// 路面远点投影坐标
	public static int roadFarX = 0;
	public static int roadFarY = 0;
	private static boolean isRoadFarFind = false;// 是否已经找到路面远点

	public C3D_World()
	{
	}

	protected void onUpdate(C2D_Stage stage)
	{
		super.onUpdate(stage);
	}

	protected void onAutoUpdate()
	{
		super.onAutoUpdate();
		this.projectWorld(false);
	}

	/**
	 * 设置大小，即宽度与高度
	 * 
	 * @param width
	 *            新设置的宽度
	 * @param height
	 *            新设置的高度度
	 * @return 返回是否成功设置
	 */
	public C2D_View setSize(int width, int height)
	{
		viewH = width;
		viewMax = C2D_Math.max(width, height);
		viewGapX = viewMax - (width >> 1);
		viewGapY = viewMax - (height >> 1) + (horizonY - (height >> 1));
		return super.setSize(width, height);
	}

	/**
	 * 根据指定的地图编号载入世界视景数据
	 * 
	 * @param mapID
	 *            地图编号
	 */
	public void loadWorld(int mapID)
	{
		// 加载场景数据
		vModelDatas.removeAllElements();
		vModels.releaseRes();
		String path = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + "level" + mapID + ".bin";
		DataInputStream dis = null;
		try
		{
			dis = C2D_IOUtil.getDataInputStream(path);
			short modelTroopCount = 0;
			byte byteData = 0;
			String name = null;
			short cModeLen = 0;
			short propID = -1;
			short propActorID = -1;
			modelTroopCount = C2D_IOUtil.readShort(modelTroopCount, dis);// 模型群数量
			for (int i = 0; i < modelTroopCount; i++)
			{
				byteData = C2D_IOUtil.readByte(byteData, dis);// 模型群类型
				switch (byteData)
				{
				case C3D_Model.MT_TYPE_N:
					C3D_ModelData dataN = new C3D_ModelData();
					dataN.readObject(dis);
					vModelDatas.addElement(dataN);
					C3D_Model modelN = new C3D_Model(C3D_Model.MT_TYPE_N, dataN);
					modelN.alwaysVisible = C2D_IOUtil.readBoolean(modelN.alwaysVisible, dis);// 读取可视信息
					modelN.followRoadID = C2D_IOUtil.readShort(modelN.followRoadID, dis);// 读取跟随ID
					if (!modelN.alwaysVisible)
					{
						vModels.addNode(modelN);
					}
					else
					{
						vAlwaysShowModels.addElement(modelN);
					}
					break;
				case C3D_Model.MT_TYPE_C:
					name = C2D_IOUtil.readString(name, dis);
					cModeLen = C2D_IOUtil.readShort(cModeLen, dis);// 通用模型长度
					propID = C2D_IOUtil.readShort(propID, dis);// 道具ID
					propActorID = C2D_IOUtil.readShort(propActorID, dis);// 道具ActorID
					C3D_ModelData dataC = null;
					for (int j = 0; j < cModeLen; j++)
					{
						C3D_Vertex3 vertex3 = new C3D_Vertex3();// 读入坐标信息
						vertex3.readObject(dis);
						int angle = C2D_IOUtil.readInt(0, dis);
						C3D_Vector3 vector3 = new C3D_Vector3();// 读入scal信息
						vector3.readObject(dis);
						short followRoadID = 0;
						followRoadID = C2D_IOUtil.readShort(followRoadID, dis);// 读取跟随ID
						// 查找是否已经存在相同名字的通用模型数据
						for (int m = 0; m < vModelDatas.size(); m++)
						{
							C3D_ModelData dataM = (C3D_ModelData) vModelDatas.elementAt(m);
							if (dataM.dataName != null && dataM.dataName.equals(name))
							{
								dataC = dataM;
								break;
							}
						}
						if (dataC == null)
						{
							dataC = new C3D_ModelData();
							dataC.dataName = name;
							vModelDatas.addElement(dataC);
						}
						if (propActorID < 0)
						{
							C3D_Model modeC = new C3D_Model(C3D_Model.MT_TYPE_C, dataC);
							modeC.bottomCenter = vertex3;
							modeC.createTransMartrix(angle, vector3);
							modeC.stagePropID = (byte) propID;
							modeC.propModelActorID = propActorID;
							modeC.followRoadID = followRoadID;
							vModels.addNode(modeC);
							dataC.needLoad = true;
						}
						else
						{
							C3D_ObjModel modeC = new C3D_ObjModel(C3D_Model.MT_TYPE_O, vertex3);
							modeC.stagePropID = (byte) propID;
							modeC.propModelActorID = propActorID;
							modeC.followRoadID = followRoadID;
							vModels.addNode(modeC);
						}
					}
					break;
				case C3D_Model.MT_TYPE_R:
					C3D_ModelData dataR = new C3D_ModelData();
					dataR.readObject(dis);
					vModelDatas.addElement(dataR);
					C3D_RoadModel modelR = new C3D_RoadModel(C3D_Model.MT_TYPE_R, dataR);
					// vModels.addElement(modelR);
					road = modelR;
					break;
				case C3D_Model.MT_TYPE_WI:
				case C3D_Model.MT_TYPE_WO:
					C3D_ModelData dataW = new C3D_ModelData();
					dataW.readObject(dis);
					vModelDatas.addElement(dataW);
					C3D_RoadModel modelW = new C3D_RoadModel(byteData, dataW);
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (dis != null)
				{
					dis.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		// start test
		// ModelNode node = vModels.headModel;
		// while(node!=null)
		// {
		// System.out.println("followRoadID:"+node.model.followRoadID);
		// node=node.getNext();
		// }
		// end test
		vModels.linkStartAndEnd();
		loadStandirdModels();
		initWorld();
	}

	// 加载参考模型原型数据
	private void loadStandirdModels()
	{
		DataInputStream dis = null;
		for (int m = 0; m < vModelDatas.size(); m++)
		{
			C3D_ModelData dataM = (C3D_ModelData) vModelDatas.elementAt(m);
			if (dataM.dataName != null && dataM.needLoad)
			{
				String path = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + dataM.dataName + ".bin";
				System.out.println("try to load :" + path);
				try
				{
					// 加载场景数据
					dis = C2D_IOUtil.getDataInputStream(path);
					dataM.readObject(dis);
				}
				catch (Exception e)
				{
					System.out.println("fail to load :" + path);
				}
				finally
				{
					try
					{
						if (dis != null)
						{
							dis.close();
						}
					}
					catch (Exception e)
					{
					}
				}
			}
			else
			{
			}
		}
	}

	/** @todo ----------初始化世界中的物体参数---------- */
	public void initWorld()
	{
		if (projectBuffer == null)
		{
			projectBuffer = new int[BUFFER_SIZE * BUFF_STEP]; // 投影缓冲
		}
		if (projectBufferOrder == null)
		{
			projectBufferOrder = new C2D_SortableArrayL(BUFFER_SIZE); // 64位投影缓冲排序[32位正Z、32位ID]
		}
		if (tempTriangle == null)
		{
			tempTriangle = new C3D_Triangle();
		}
		// 创建路面和路面边缘索引
		if (road != null)
		{
			road.createRoad();
		}
		// 检查中心信息
		C3D_ModelNode nodeI = vModels.headModel;
		do
		{
			nodeI.model.checkCenter();
			nodeI = nodeI.getNext();
		}
		while (!nodeI.equals(vModels.headModel));
		// 重置当前的投影缓冲
		resetProjectBuffer();
		// 初始化相机----------------------------------------------------------
		// 初始化相机信息
		activeCamera = new C3D_Camera();
		// 起始路面
		C3D_LinkedModels.modelStart = null;
		C3D_LinkedModels.modelEnd = null;
		// 初始化路面朝向
		setRoadNavigate(0);
	}

	// 初始化路面导航
	protected void setRoadNavigate(int roadID)
	{
		if (road != null)
		{
			currentRoadID = roadID;
			road.getRoadCenter(currentRoadID, Vertex3Temp0);
			road.getRoadCenter(currentRoadID + 1, Vertex3Temp1);
			road.getRoadCenter(currentRoadID - 3, Vertex3Temp2);
			roadNavigate.setValue(Vertex3Temp1.x - Vertex3Temp0.x, Vertex3Temp1.y - Vertex3Temp0.y, Vertex3Temp1.z
					- Vertex3Temp0.z);
			angleHorizon = (float) C2D_Math.standirdAngle(C2D_Math.actTan(-roadNavigate.z, roadNavigate.x));
			// 初始化位置
			cameraPosOrg.setValue(Vertex3Temp2.x, Vertex3Temp2.y, Vertex3Temp2.z);
			cameraPosOrg.y = Vertex3Temp2.y + cameraHeight + shakeOffsetX;
			currentCameraPos.setValue(cameraPosOrg);
			// 设置相机初始位置和转角
			activeCamera.reset();
			activeCamera.rotateX(cameraAngleEle);
			activeCamera.rotateY((float) (angleHorizon - C2D_Math.PI / 2));
			this.activeCamera.setEyePosition(currentCameraPos.x, currentCameraPos.y, currentCameraPos.z);
		}
		// 检查相机中的物体
		checkObjectInCamera();
	}

	// 检查处于照相机中的物体
	public void checkObjectInCamera()
	{
		vModels.searchNextNode(currentRoadID + 1, (currentRoadID + nbViewObjectChunk) % road.gerNodeCount());
	}

	public static void setHY(int horizonYT)
	{
		horizonY = horizonYT;// 地平线高度
		int w = C2D_Stage.User_Size.m_width;
		int h = C2D_Stage.User_Size.m_height;
		viewH = w;
		viewMax = C2D_Math.max(w, h);
		viewGapX = viewMax - (w >> 1);
		viewGapY = viewMax - (h >> 1) + (horizonY - (h >> 1));
	}

	/** @todo ----------------------设置赛车------------------------------- */
	public static int currentRoadID = 0;

	public void setFollowNavigator(C3D_Navigator navigator)
	{
		m_followNg = navigator;
	}

	/** @todo ----------------------更新整个世界------------------------------- */
	public void updateWorld()
	{
		// 导航更新
		if (this.m_followNg != null)
		{
			if (currentRoadID != m_followNg.currentRoadID)
			{
				currentRoadID = m_followNg.currentRoadID;
				// 更新路面朝向
				road.getRoadCenter(m_followNg.currentRoadID, Vertex3Temp0);
				road.getRoadCenter(m_followNg.currentRoadID + 1, Vertex3Temp1);
				roadNavigate.setValue(Vertex3Temp1.x - Vertex3Temp0.x, Vertex3Temp1.y - Vertex3Temp0.y, Vertex3Temp1.z
						- Vertex3Temp0.z);
				// 检查可视物体
				checkObjectInCamera();
			}
		}
		// 更新相机-----------------------------------------------------
		// 校正水平视角
		// 视角1-使用路面方向导航
		road.getRoadCenter(m_followNg.currentRoadID, Vertex3Temp0);
		road.getRoadCenter(m_followNg.currentRoadID + 2, Vertex3Temp2);
		Vertex3Temp2.subtract(Vertex3Temp0, Vertex3Temp1);
		// 视角2-使用赛车方向导航
		// Vertex3Temp1.setValue(followCar.bottomCenter.decT(activeCamera.eyePosition));
		double newCameraAngle = C2D_Math.standirdAngle(C2D_Math.actTan(-(Vertex3Temp1.z), Vertex3Temp1.x));
		// long
		// newCameraAngle=Maths.standirdAngle(Maths.actTan(-(followCar.position.z-currentCameraPos.z),followCar.position.x-currentCameraPos.x));
		double clipAngle = C2D_Math.clipAngle(angleHorizon, newCameraAngle);
		int num = 6;
		if (C2D_Math.abs(clipAngle) >= 10)
		{
			double c = clipAngle * num / C2D_Math.PI + 1;
			if (C2D_Math.left_Right(newCameraAngle, angleHorizon))
			{
				angleHorizon += clipAngle * c / (c + num - 1);
			}
			else
			{
				angleHorizon -= clipAngle * c / (c + num - 1);
			}
			angleHorizon = (float) C2D_Math.standirdAngle(angleHorizon);
		}
		double posAngle = C2D_Math.standirdAngle(angleHorizon + C2D_Math.PI);
		double cameraDestX = m_followNg.bottomCenter.x + (cameraDisToCar * C2D_Math.cos(posAngle));
		double cameraDestZ = m_followNg.bottomCenter.z - (cameraDisToCar * C2D_Math.sin(posAngle));
		// 更新与车之间的距离(XZ平面)
		m_followNg.bottomCenter.subtract(cameraPosOrg, Vertex3Temp0);
		float size = C2D_Math.sqrt(Vertex3Temp0.x * Vertex3Temp0.x + Vertex3Temp0.z * Vertex3Temp0.z);
		if (size > cameraDisToCar)// 距离校正
		{
			cameraPosOrg.x += (size - cameraDisToCar) * Vertex3Temp0.x / (2 * size);
			cameraPosOrg.z += (size - cameraDisToCar) * Vertex3Temp0.z / (2 * size);
		}
		// 尾随校正
		cameraPosOrg.x += (cameraDestX - cameraPosOrg.x) / 4;
		cameraPosOrg.z += (cameraDestZ - cameraPosOrg.z) / 4;
		// 高度设置
		cameraPosOrg.y = m_followNg.bottomCenter.y + cameraHeight;
		// 震动叠加
		currentCameraPos.setValue(cameraPosOrg.x + shakeOffsetX, cameraPosOrg.y + shakeOffsetY, cameraPosOrg.z
				+ shakeOffsetZ);
		// 计算相机高度
		// long destY=followCar.position.y+cameraHeight;
		// long gapY=destY-currentCameraPos.y;
		// long absY=Math.abs(gapY);
		// if(absY>65536)
		// {
		// currentCameraPos.y+=gapY;
		// }
		// else if(absY>256)
		// {
		// currentCameraPos.y+=gapY/2;
		// }
		adjustCamera();
	}

	// 重设相机信息
	public void adjustCamera()
	{
		activeCamera.reset();
		activeCamera.rotateX(cameraAngleEle);
		activeCamera.rotateY((float) C2D_Math.standirdAngle(angleHorizon - C2D_Math.PI / 2));
		this.activeCamera.setEyePosition(currentCameraPos.x, currentCameraPos.y, currentCameraPos.z);
	}

	/** @todo -----------------投影整个世界----------------- */
	/**
	 * 投影整个世界
	 * 
	 * @param onlyRoad
	 *            如果只有路面，将没有路面道具
	 */
	public void projectWorld(boolean onlyRoad)
	{
		if (activeCamera == null)
		{
			return;
		}
		resetProjectBuffer();
		// 投影整个世界
		if (!onlyRoad)
		{
			C3D_ModelNode modeI = C3D_LinkedModels.modelStart;
			while (modeI != null && !modeI.equals(C3D_LinkedModels.modelEnd))
			{
				projectModel(modeI.model);
				modeI = modeI.getNext();
			}
			// FIXME 这里应该不需要
			for (int i = 0; i < vAlwaysShowModels.size(); i++)
			{
				C3D_Model model = (C3D_Model) vAlwaysShowModels.elementAt(i);
				projectModel(model);
			}
		}
		// 投影路面
		if (road != null)
		{
			roadMosetHigh = C2D_Stage.User_Size.m_width;
			isRoadFarFind = false;
			projectRoad(road, 0);// m_followNg.currentRoadID
		}
	}

	// 重置当前的投影缓冲
	public void resetProjectBuffer()
	{
		buffDeep = 0;
		buffElementNB = 0;
		modelDeep = 0;
		cutFace = 0;
		viewObjModelCount = 0;
	}

	/** @todo --投影通用和非通用模型(不能处理路面模型和对象模型)-- */
	public void projectModel(C3D_Model model)
	{
		if (model == null || !model.isVisible || !model.isEnable)
		{
			return;
		}
		if (model.modelType == C3D_Model.MT_TYPE_O)
		{
			C3D_ObjModel objModel = (C3D_ObjModel) model;
			projectObjectModel(objModel);
			return;
		}
		if ((model.modelType != C3D_Model.MT_TYPE_C && model.modelType != C3D_Model.MT_TYPE_N)
				|| model.modelData == null)
		{
			return;
		}
		if (!model.alwaysVisible)
		{
			// 模型包围球裁切--------------------------------------------------------------
			float maxR = model.maxR;// 最大的包围球半径
			float xmax = activeCamera.right;
			float xmin = activeCamera.left;
			float ymax = activeCamera.top;
			float ymin = activeCamera.bottom;
			float z0 = -activeCamera.near;
			C3D_Vertex3 centerInWorld = model.boundingCenter;
			C3D_Vector3 centerInCamera = activeCamera.getCameraPosition(centerInWorld);
			// 球的中心是否在右裁切面之外
			C3D_Vector3 v = Vector3Temp0;
			v.setValue(-z0, 0, xmax); // 这是相机右侧发射向量经过向右旋转90度获得的右裁切面的法线
			float t = v.innerProduct(centerInCamera); // 这是球体中心在右裁切面法线上的被扩大了v.size2()倍的投影,即球体中心到右裁切面距离的v.size2()倍
			// 球的中心和右裁切面之间的距离
			// 平面ax+by+cz+d=0与(x0,y0,z0)之间的距离的平方h^2是
			// h^2 = |a*x0+b*y0+c*z0+d|^2/(a*a+b*b+c*c)
			double u = t * t - maxR * maxR * v.size2(); // 即t*t/v.size2()>p.r平方，点到面的距离大于半径
			if (t >= 0 && u > 0)
			{
				return; // 在右裁切面之外
			}
			// 球的中心是否在左裁切面之左外?
			v.setValue(z0, 0, -xmin);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // 在左裁切面之外
			}
			// 球的中心是否在上裁切面之上外
			v.setValue(0, -z0, ymax);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // 在上裁切面之外
			}
			// 球的中心是否在下裁切面之下外
			v.setValue(0, z0, -ymin);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // 在下裁切面之外
			}
		}
		// 从模型数据开始建模----------------------------------------------------------
		modelDeep++;
		C3D_ModelData modelData = model.modelData;
		int id = 0, idTemp = 0;
		int faceLen = 0;
		for (int i = 0; i < modelData.faceDataLens.length; i++)
		{
			int vID = 0;
			for (int j = 0; j < modelData.faceDataLens[i].length; j++)
			{
				faceLen = modelData.faceDataLens[i][j];
				// 投影三角形
				for (int k = 0; k <= faceLen - 3; k++)
				{
					id = modelData.faceDataList[i][vID] - 1;
					idTemp = id + id + id;
					tempTriangle.pointA.x = modelData.vertexList[idTemp++];
					tempTriangle.pointA.y = modelData.vertexList[idTemp++];
					tempTriangle.pointA.z = modelData.vertexList[idTemp];
					// tempTriangle.setValueA(modelData.vertexList[idTemp],modelData.vertexList[idTemp+1],modelData.vertexList[idTemp+2]);
					id = modelData.faceDataList[i][vID + 1 + k] - 1;
					idTemp = id + id + id;
					tempTriangle.pointB.x = modelData.vertexList[idTemp++];
					tempTriangle.pointB.y = modelData.vertexList[idTemp++];
					tempTriangle.pointB.z = modelData.vertexList[idTemp];
					// tempTriangle.setValueB(modelData.vertexList[idTemp],modelData.vertexList[idTemp+1],modelData.vertexList[idTemp+2]);
					id = modelData.faceDataList[i][vID + 2 + k] - 1;
					idTemp = id + id + id;
					tempTriangle.pointC.x = modelData.vertexList[idTemp++];
					tempTriangle.pointC.y = modelData.vertexList[idTemp++];
					tempTriangle.pointC.z = modelData.vertexList[idTemp];
					// tempTriangle.setValueC(modelData.vertexList[idTemp],modelData.vertexList[idTemp+1],modelData.vertexList[idTemp+2]);
					tempTriangle.color = modelData.faceColorList[i];
					// tempTriangle.setColor(modelData.faceColorList[i]);
					projectTriangle(tempTriangle, model);
				}
				vID += faceLen;
			}
		}
	}

	/** @todo -----------------投影三角形------------------- */
	private static C3D_Vector3 vn = new C3D_Vector3();
	private static int[] indexIn = new int[3];
	private static int[] indexOut = new int[3];
	private static C3D_Vertex3 V_A = new C3D_Vertex3();
	private static C3D_Vertex3 V_B = new C3D_Vertex3();
	private static C3D_Vertex3 V_C = new C3D_Vertex3();
	private static C3D_Vertex3 V_CS1 = new C3D_Vertex3();
	private static C3D_Vertex3 V_CS2 = new C3D_Vertex3();

	private void projectTriangle(C3D_Triangle triangle, C3D_Model model)
	{
		int codeA = 0;
		int codeB = 0;
		int codeC = 0;
		// 将三角形从参考模型坐标系转换到世界坐标系
		byte modelType = C3D_Model.MT_TYPE_N;
		if (model != null)
		{
			modelType = model.modelType;
			if (modelType == C3D_Model.MT_TYPE_C && model.transMartrix != null)
			{
				model.transMartrix.mapVector(triangle.pointA);
				model.transMartrix.mapVector(triangle.pointB);
				model.transMartrix.mapVector(triangle.pointC);
			}
			// 将三角形从世界坐标系转换到相机坐标系
			activeCamera.getCameraPositionL(triangle.pointA);
			activeCamera.getCameraPositionL(triangle.pointB);
			activeCamera.getCameraPositionL(triangle.pointC);
		}
		// 先做隐藏面的消除
		vn.setValue(triangle.getNormal());
		Vertex3Temp0.setValue((triangle.pointA.x + triangle.pointB.x + triangle.pointC.x) / 3, (triangle.pointA.y
				+ triangle.pointB.y + triangle.pointC.y) / 3,
				(triangle.pointA.z + triangle.pointB.z + triangle.pointC.z) / 3);
		if (vn.innerProduct(Vertex3Temp0) >= 0)
		{
			cutFace++;
			return;
		}
		// 投影变换
		activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
		activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
		activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
		// CVV裁切开始------------------------------------------
		// 得到三角形三个点的区域代码
		codeA = activeCamera.areaCode(Vertex4Temp1);
		codeB = activeCamera.areaCode(Vertex4Temp2);
		codeC = activeCamera.areaCode(Vertex4Temp3);
		// 判断区域
		if ((codeA | codeB | codeC) == 0)// 完全接受的情况
		{
			allowCountRoadHigh2 = true;
			addTriangleToBuffer(triangle.color, modelType);// getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			allowCountRoadHigh2 = false;
		}
		else if ((codeA & codeB & codeC) != 0)// 完全拒绝的情况
		{
			return;
		}
		else
		// 近平面的分割裁剪...
		{
			// 测试每一个三角形-------------------
			int nbFront = 0;
			int nbBehind = 0;
			for (int j = 0; j < 3; j++)
			{
				triangle.getNormal();
				C3D_Vertex3 vJ = triangle.getElement(j);
				vJ.subtract(activeCamera.nearPoint, vn);
				if (vn.innerProduct(activeCamera.directVector) > 0)
				{
					indexIn[nbFront] = j;
					nbFront++;
				}
				else
				{
					// 记录
					indexOut[nbBehind] = j;
					nbBehind++;
				}
			}
			// 分类裁切--------------------------
			if (nbFront == 3)// 3个全部位于摄像机正向
			{
				addTriangleToBuffer(triangle.color, modelType);// getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
			else if (nbFront == 2)// 有2个位于正向
			{
				V_A.setValue(triangle.getElement(indexIn[0]));
				V_B.setValue(triangle.getElement(indexIn[1]));
				V_C.setValue(triangle.getElement(indexOut[0]));
				// 第一个三角形
				C3D_Vertex3 V_Temp = C2D_Math.getCross(V_B, V_C, activeCamera.directVector, activeCamera.nearPoint);
				if (V_Temp == null)
				{
					return;
				}
				V_CS1.setValue(V_Temp);
				boolean deasil = C2D_Math.isDeasil(indexIn[0], indexIn[1], indexOut[0]);
				triangle.setValueA(V_A);
				if (deasil)
				{
					triangle.setValueB(V_B);
					triangle.setValueC(V_CS1);
				}
				else
				{
					triangle.setValueB(V_CS1);
					triangle.setValueC(V_B);
				}
				// 投影变换
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
				// 第二个三角形
				V_Temp = C2D_Math.getCross(V_A, V_C, activeCamera.directVector, activeCamera.nearPoint);
				if (V_Temp == null)
				{
					return;
				}
				V_CS2.setValue(V_Temp);
				triangle.setValueA(V_A);
				if (deasil)
				{
					triangle.setValueB(V_CS1);
					triangle.setValueC(V_CS2);
				}
				else
				{
					triangle.setValueB(V_CS2);
					triangle.setValueC(V_CS1);
				}
				// 投影变换
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
			else if (nbFront == 1)// 有1个位于正向
			{
				V_A.setValue(triangle.getElement(indexIn[0]));
				V_B.setValue(triangle.getElement(indexOut[0]));
				V_C.setValue(triangle.getElement(indexOut[1]));
				boolean deasil = C2D_Math.isDeasil(indexIn[0], indexOut[0], indexOut[1]);
				C3D_Vertex3 V_Temp = C2D_Math.getCross(V_A, V_B, activeCamera.directVector, activeCamera.nearPoint);
				if (V_Temp == null)
				{
					return;
				}
				V_CS1.setValue(V_Temp);
				V_Temp = C2D_Math.getCross(V_A, V_C, activeCamera.directVector, activeCamera.nearPoint);
				if (V_Temp == null)
				{
					return;
				}
				V_CS2.setValue(V_Temp);
				triangle.setValueA(V_A);
				if (deasil)
				{
					triangle.setValueB(V_CS1);
					triangle.setValueC(V_CS2);
				}
				else
				{
					triangle.setValueB(V_CS2);
					triangle.setValueC(V_CS1);
				}
				// 投影变换
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
		}
	}

	// 根据三点和指定的颜色投影三角形
	private static C3D_Triangle tempTriangleTemp1 = new C3D_Triangle();

	private void projectTriangle(C3D_Vertex3 pointA, C3D_Vertex3 pointB, C3D_Vertex3 pointC, int color)
	{
		tempTriangleTemp1.pointA.setValue(pointA);
		tempTriangleTemp1.pointB.setValue(pointB);
		tempTriangleTemp1.pointC.setValue(pointC);
		tempTriangleTemp1.setColor(color);
		this.projectTriangle(tempTriangleTemp1, null);
	}

	// 获得三点与相机视点最近点，并返回距离平方
	private float getMinZ(C3D_Vertex3 pointA, C3D_Vertex3 pointB, C3D_Vertex3 pointC)
	{
		float zSqrA = pointA.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zSqrB = pointB.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zSqrC = pointC.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zMin = C2D_Math.min(C2D_Math.min(zSqrA, zSqrB), zSqrC);
		return zMin;
	}

	/** @todo --------------将投影过的三角形增加到投影缓存---------------- */
	private void addTriangleToBuffer(int color, byte type)
	{
		if (buffDeep >= projectBuffer.length)
		{
			C2D_Debug.logErr("not enough project buffer");
			return;
		}
		projectBuffer[buffDeep++] = (int) ((Vertex4Temp1.x + Vertex4Temp1.w) * C3D_World.viewMax / Vertex4Temp1.w - C3D_World.viewGapX);
		projectBuffer[buffDeep++] = (int) (C3D_World.viewGapY + C3D_World.viewH - (Vertex4Temp1.y + Vertex4Temp1.w)
				* C3D_World.viewMax / Vertex4Temp1.w);
		projectBuffer[buffDeep++] = (int) ((Vertex4Temp2.x + Vertex4Temp2.w) * C3D_World.viewMax / Vertex4Temp2.w - C3D_World.viewGapX);
		projectBuffer[buffDeep++] = (int) (C3D_World.viewGapY + C3D_World.viewH - (Vertex4Temp2.y + Vertex4Temp2.w)
				* C3D_World.viewMax / Vertex4Temp2.w);
		projectBuffer[buffDeep++] = (int) ((Vertex4Temp3.x + Vertex4Temp3.w) * C3D_World.viewMax / Vertex4Temp3.w - C3D_World.viewGapX);
		projectBuffer[buffDeep++] = (int) (C3D_World.viewGapY + C3D_World.viewH - (Vertex4Temp3.y + Vertex4Temp3.w)
				* C3D_World.viewMax / Vertex4Temp3.w);
		projectBuffer[buffDeep++] = type;// 类型信息
		projectBuffer[buffDeep++] = color;// 这里的color用来标记此路段是否需要显示
		long S_16 = 65536;
		// 将z变换成正值存放在排序表,将区间从[-1,1]扩展到[0,65536*2]
		long z = S_16
				- (long) C2D_Math.max(
						C2D_Math.max((Vertex4Temp1.z * S_16) / (Vertex4Temp1.w), (Vertex4Temp2.z * S_16)
								/ (Vertex4Temp2.w)), (Vertex4Temp3.z * S_16) / (Vertex4Temp3.w));
		// if(z<0||z>1<<17)
		// {
		// System.out.println("------------------------ error z"+z);
		// }
		projectBufferOrder.m_datas[buffElementNB] = (z << 32) | buffElementNB;
		// 顺便找出最高的路面,条件是这个三角形必须可见
		if (type == C3D_Model.MT_TYPE_R)
		{
			int high = C2D_Math.min(C2D_Math.min(projectBuffer[buffDeep - 7], projectBuffer[buffDeep - 5]),
					projectBuffer[buffDeep - 3]);// >>8
			if (high < roadMosetHigh && allowCountRoadHigh1 && allowCountRoadHigh2)
			{
				roadMosetHigh = high;
			}
			// 查找路面远点坐标
			if (!isRoadFarFind)
			{
				roadFarX = (projectBuffer[buffDeep - 6] + projectBuffer[buffDeep - 4]) >> 1;
				roadFarY = (projectBuffer[buffDeep - 7] + projectBuffer[buffDeep - 5]) >> 1;
				isRoadFarFind = true;
			}
		}
		buffElementNB++;
	}

	/** @todo --------------将投影过的点增加到投影缓存---------------- */
	private void addPointToBuffer(int objID, long zReal, byte modelType)
	{
		if (buffDeep >= projectBuffer.length)
		{
			C2D_Debug.logErr("not enough project buffer");
			return;
		}
		projectBuffer[buffDeep++] = (int) ((Vertex4Temp1.x + Vertex4Temp1.w) * C3D_World.viewMax / Vertex4Temp1.w - C3D_World.viewGapX);
		projectBuffer[buffDeep++] = (int) (C3D_World.viewGapY + C3D_World.viewH - (Vertex4Temp1.y + Vertex4Temp1.w)
				* C3D_World.viewMax / Vertex4Temp1.w);
		buffDeep += 3;
		long far = (zReal >> 8);
		projectBuffer[buffDeep++] = (int) far;
		projectBuffer[buffDeep++] = modelType;// 类型信息
		projectBuffer[buffDeep++] = objID;// 存放对象ID
		// 将z变换成正值存放在排序表,将区间从[-1,1]扩展到[0,65536*2]
		long S_16 = 65536;
		long z = S_16 - (long) ((Vertex4Temp1.z * S_16) / (Vertex4Temp1.w));
		// if(z<0||z>1<<17)
		// {
		// System.out.println("------------------------ error z"+z);
		// }
		projectBufferOrder.m_datas[buffElementNB] = (z << 32) | buffElementNB;
		buffElementNB++;
	}

	/** @todo ----------------深度排序----------------- */
	private static int carAdjustOrder[] = new int[6];// 赛车校正缓冲

	public void orderBuffer()
	{
		int orderID = buffElementNB;
		// 先对三维景物和路面的minZ进行排序(从小到大),【可以再进行面Z重叠判断和排序，获得更精确的结果，但是会降低效率】
		projectBufferOrder.quickSort3();
		// 再在排好的顺序中使用对象模型优先显示的权值，即对象模型可以遮挡相邻的路面模型.....
		int id, type;
		boolean findObj = false;
		int passRoad = 0;
		int objStart = 0;
		int objEnd = 0;
		int nbObj = 0;
		for (int i = 0; i < orderID; i++)
		{
			id = (int) ((projectBufferOrder.m_datas[i] & 0xFFFFFFFF) << 3);
			type = projectBuffer[id + 6];
			if (findObj)
			{
				if (type == C3D_ObjModel.MT_TYPE_R)
				{
					passRoad++;
				}
				else if (type == C3D_ObjModel.MT_TYPE_CAR)
				{
					carAdjustOrder[nbObj] = i;
					nbObj++;
				}
				if (passRoad > 0 || (i == orderID - 1 && passRoad > 0))// 找到了会盖住对象的路面
				{
					for (int nb = nbObj - 1; nb >= 0; nb--)
					{
						objStart = carAdjustOrder[nb];
						objEnd = i - (nbObj - nb - 1);
						long dataStart = projectBufferOrder.m_datas[objStart];
						for (int j = objStart; j < objEnd; j++)
						{
							projectBufferOrder.m_datas[j] = projectBufferOrder.m_datas[j + 1];
						}
						projectBufferOrder.m_datas[objEnd] = dataStart;
					}
					findObj = false;
					passRoad = 0;
					nbObj = 0;
				}
			}
			else
			{
				if (type == C3D_ObjModel.MT_TYPE_CAR)
				{
					findObj = true;
					passRoad = 0;
					nbObj = 0;
					carAdjustOrder[nbObj] = i;
					nbObj++;
				}
			}
		}
	}

	/** @todo ----------------投影路面模型----------------- */
	public void projectRoad(C3D_RoadModel model, int roadNodeID)
	{
		if (model == null)
		{
			return;
		}
		// 先找到最远投影路段
		int farID = roadNodeID + nbViewRoadChunk;// 相对ID
		for (int i = farID; i >= roadNodeID - 1; i--)
		{
			projectRoadNode(i);
		}
	}

	private static C3D_Vertex3 Vertex3W_ILF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_ILN = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_OLF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_OLN = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_IRF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_IRN = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_ORF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_ORN = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_CRF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_CRN = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_CLF = new C3D_Vertex3();
	private static C3D_Vertex3 Vertex3W_CLN = new C3D_Vertex3();
	public static double roadWidthShiftI = 0.5;// 赛道的宽度
	public static double roadWidthShiftO = 1.0;// 赛道的宽度
	public static double roadWidthShiftC = 0.125;// 赛道的宽度
	private static boolean allowCountRoadHigh1 = false;// 允许统计路面高度限制1
	private static boolean allowCountRoadHigh2 = false;// 允许统计路面高度限制2

	protected void projectRoadNode(int roadID)
	{
		int color = 0;
		color = roadID % 2 == 0 ? roadColorA : roadColorB;
		// 获得路面节点的三个顶点----------------------------
		road.getRoadPoint(roadID, Vertex3Temp1, Vertex3Temp3);// 远左右点
		road.getRoadPoint(roadID - 1, Vertex3Temp4, Vertex3Temp6);// 近左右点
		allowCountRoadHigh1 = false;
		// 显示路边沿----------------------------------------
		Vertex3Temp1.subtract(Vertex3Temp3, Vertex3Temp2);
		double lenF = Vertex3Temp2.size();
		Vertex3W_IRF
				.setValue(Vertex3Temp1.x + (Vertex3Temp2.x * roadWidthShiftI) / lenF, Vertex3Temp1.y
						+ (Vertex3Temp2.y * roadWidthShiftI) / lenF, Vertex3Temp1.z
						+ (Vertex3Temp2.z * roadWidthShiftI) / lenF);
		Vertex3W_ORF
				.setValue(Vertex3Temp1.x + (Vertex3Temp2.x * roadWidthShiftO) / lenF, Vertex3Temp1.y
						+ (Vertex3Temp2.y * roadWidthShiftO) / lenF, Vertex3Temp1.z
						+ (Vertex3Temp2.z * roadWidthShiftO) / lenF);
		Vertex3W_ILF
				.setValue(Vertex3Temp3.x - (Vertex3Temp2.x * roadWidthShiftI) / lenF, Vertex3Temp3.y
						- (Vertex3Temp2.y * roadWidthShiftI) / lenF, Vertex3Temp3.z
						- (Vertex3Temp2.z * roadWidthShiftI) / lenF);
		Vertex3W_OLF
				.setValue(Vertex3Temp3.x - (Vertex3Temp2.x * roadWidthShiftO) / lenF, Vertex3Temp3.y
						- (Vertex3Temp2.y * roadWidthShiftO) / lenF, Vertex3Temp3.z
						- (Vertex3Temp2.z * roadWidthShiftO) / lenF);
		Vertex3Temp4.subtract(Vertex3Temp6, Vertex3Temp5);
		double lenN = Vertex3Temp5.size();
		Vertex3W_IRN
				.setValue(Vertex3Temp4.x + (Vertex3Temp5.x * roadWidthShiftI) / lenN, Vertex3Temp4.y
						+ (Vertex3Temp5.y * roadWidthShiftI) / lenN, Vertex3Temp4.z
						+ (Vertex3Temp5.z * roadWidthShiftI) / lenN);
		Vertex3W_ORN
				.setValue(Vertex3Temp4.x + (Vertex3Temp5.x * roadWidthShiftO) / lenN, Vertex3Temp4.y
						+ (Vertex3Temp5.y * roadWidthShiftO) / lenN, Vertex3Temp4.z
						+ (Vertex3Temp5.z * roadWidthShiftO) / lenN);
		Vertex3W_ILN
				.setValue(Vertex3Temp6.x - (Vertex3Temp5.x * roadWidthShiftI) / lenN, Vertex3Temp6.y
						- (Vertex3Temp5.y * roadWidthShiftI) / lenN, Vertex3Temp6.z
						- (Vertex3Temp5.z * roadWidthShiftI) / lenN);
		Vertex3W_OLN
				.setValue(Vertex3Temp6.x - (Vertex3Temp5.x * roadWidthShiftO) / lenN, Vertex3Temp6.y
						- (Vertex3Temp5.y * roadWidthShiftO) / lenN, Vertex3Temp6.z
						- (Vertex3Temp5.z * roadWidthShiftO) / lenN);
		int colorI, colorO;
		if (roadID % 2 == 0)
		{
			colorI = roadColorIA;
			colorO = roadColorOA;
		}
		else
		{
			colorI = roadColorIB;
			colorO = roadColorOB;
		}
		tempTriangle.setColor(colorI);
		tempTriangle.setValue(Vertex3Temp1, Vertex3Temp4, Vertex3W_IRF);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3Temp4, Vertex3W_IRN, Vertex3W_IRF);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3Temp6, Vertex3Temp3, Vertex3W_ILF);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3Temp6, Vertex3W_ILF, Vertex3W_ILN);
		projectTriangle(tempTriangle, road);
		tempTriangle.setColor(colorO);
		tempTriangle.setValue(Vertex3W_IRF, Vertex3W_IRN, Vertex3W_ORN);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3W_ORN, Vertex3W_ORF, Vertex3W_IRF);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3W_OLF, Vertex3W_OLN, Vertex3W_ILN);
		projectTriangle(tempTriangle, road);
		tempTriangle.setValue(Vertex3W_OLF, Vertex3W_ILN, Vertex3W_ILF);
		projectTriangle(tempTriangle, road);
		// 显示路中心车道线条-------------------------------------
		boolean showRoadCenter = roadID % 2 == 0;
		if (showRoadCenter)
		{
			road.getRoadCenter(roadID, Vertex3Temp7);// 远中心点
			road.getRoadCenter(roadID - 1, Vertex3Temp8);// 近中心点
			Vertex3W_CRF.setValue(Vertex3Temp7.x + (Vertex3Temp2.x * roadWidthShiftC) / lenF, Vertex3Temp7.y
					+ (Vertex3Temp2.y * roadWidthShiftC) / lenF, Vertex3Temp7.z + (Vertex3Temp2.z * roadWidthShiftC)
					/ lenF);
			Vertex3W_CLF.setValue(Vertex3Temp7.x - (Vertex3Temp2.x * roadWidthShiftC) / lenF, Vertex3Temp7.y
					- (Vertex3Temp2.y * roadWidthShiftC) / lenF, Vertex3Temp7.z - (Vertex3Temp2.z * roadWidthShiftC)
					/ lenF);
			Vertex3W_CRN.setValue(Vertex3Temp8.x + (Vertex3Temp5.x * roadWidthShiftC) / lenN, Vertex3Temp8.y
					+ (Vertex3Temp5.y * roadWidthShiftC) / lenN, Vertex3Temp8.z + (Vertex3Temp5.z * roadWidthShiftC)
					/ lenN);
			Vertex3W_CLN.setValue(Vertex3Temp8.x - (Vertex3Temp5.x * roadWidthShiftC) / lenN, Vertex3Temp8.y
					- (Vertex3Temp5.y * roadWidthShiftC) / lenN, Vertex3Temp8.z - (Vertex3Temp5.z * roadWidthShiftC)
					/ lenN);
			tempTriangle.setColor(roadColorC);
			tempTriangle.setValue(Vertex3W_CRF, Vertex3W_CLF, Vertex3W_CRN);
			projectTriangle(tempTriangle, road);
			tempTriangle.setValue(Vertex3W_CRN, Vertex3W_CLF, Vertex3W_CLN);
			projectTriangle(tempTriangle, road);
		}
		// 显示路段------------------------------------------
		allowCountRoadHigh1 = true;
		tempTriangle.setColor(color);
		if (showRoadCenter)
		{
			// 拓展长度
			float roadExt = 0.5f;
			Vertex3Temp3.subtract(Vertex3Temp6, Vertex3Temp2);
			float lenL = Vertex3Temp2.size();
			Vertex3Temp1.subtract(Vertex3Temp4, Vertex3Temp5);
			float lenR = Vertex3Temp5.size();
			Vertex3Temp3.setValue(Vertex3Temp3.x + (Vertex3Temp2.x * roadExt) / lenL, Vertex3Temp3.y
					+ (Vertex3Temp2.y * roadExt) / lenL, Vertex3Temp3.z + (Vertex3Temp2.z * roadExt) / lenL);
			Vertex3Temp6.setValue(Vertex3Temp6.x - (Vertex3Temp2.x * roadExt) / lenL, Vertex3Temp6.y
					- (Vertex3Temp2.y * roadExt) / lenL, Vertex3Temp6.z - (Vertex3Temp2.z * roadExt) / lenL);
			Vertex3Temp1.setValue(Vertex3Temp1.x + (Vertex3Temp5.x * roadExt) / lenR, Vertex3Temp1.y
					+ (Vertex3Temp5.y * roadExt) / lenR, Vertex3Temp1.z + (Vertex3Temp5.z * roadExt) / lenR);
			Vertex3Temp4.setValue(Vertex3Temp4.x - (Vertex3Temp5.x * roadExt) / lenR, Vertex3Temp4.y
					- (Vertex3Temp5.y * roadExt) / lenR, Vertex3Temp4.z - (Vertex3Temp5.z * roadExt) / lenR);
			Vertex3W_CLF.subtract(Vertex3W_CLN, Vertex3Temp2);
			lenL = Vertex3Temp2.size();
			Vertex3W_CRF.subtract(Vertex3W_CRN, Vertex3Temp5);
			lenR = Vertex3Temp5.size();
			Vertex3W_CLF.setValue(Vertex3W_CLF.x + (Vertex3Temp2.x * roadExt) / lenL, Vertex3W_CLF.y
					+ (Vertex3Temp2.y * roadExt) / lenL, Vertex3W_CLF.z + (Vertex3Temp2.z * roadExt) / lenL);
			Vertex3W_CLN.setValue(Vertex3W_CLN.x - (Vertex3Temp2.x * roadExt) / lenL, Vertex3W_CLN.y
					- (Vertex3Temp2.y * roadExt) / lenL, Vertex3W_CLN.z - (Vertex3Temp2.z * roadExt) / lenL);
			Vertex3W_CRF.setValue(Vertex3W_CRF.x + (Vertex3Temp5.x * roadExt) / lenR, Vertex3W_CRF.y
					+ (Vertex3Temp5.y * roadExt) / lenR, Vertex3W_CRF.z + (Vertex3Temp5.z * roadExt) / lenR);
			Vertex3W_CRN.setValue(Vertex3W_CRN.x - (Vertex3Temp5.x * roadExt) / lenR, Vertex3W_CRN.y
					- (Vertex3Temp5.y * roadExt) / lenR, Vertex3W_CRN.z - (Vertex3Temp5.z * roadExt) / lenR);
			tempTriangle.setValue(Vertex3W_CLN, Vertex3W_CLF, Vertex3Temp3);
			projectTriangle(tempTriangle, road);
			tempTriangle.setValue(Vertex3W_CLN, Vertex3Temp3, Vertex3Temp6);
			projectTriangle(tempTriangle, road);
			tempTriangle.setValue(Vertex3Temp4, Vertex3Temp1, Vertex3W_CRF);
			projectTriangle(tempTriangle, road);
			tempTriangle.setValue(Vertex3Temp4, Vertex3W_CRF, Vertex3W_CRN);
			projectTriangle(tempTriangle, road);
		}
		else
		{
			tempTriangle.setValue(Vertex3Temp4, Vertex3Temp1, Vertex3Temp3);
			projectTriangle(tempTriangle, road);
			tempTriangle.setValue(Vertex3Temp4, Vertex3Temp3, Vertex3Temp6);
			projectTriangle(tempTriangle, road);
		}
	}

	/** @todo ----------------投影对象模型----------------- */
	private static C3D_ObjModel viewModels[] = new C3D_ObjModel[50];// 可以看到的对象模型
	private static int viewObjModelCount = 0;// 可以看到的模型数

	public void projectObjectModel(C3D_ObjModel model)
	{
		if (model == null || (model.modelType != C3D_ObjModel.MT_TYPE_O && model.modelType != C3D_ObjModel.MT_TYPE_CAR))
		{
			return;
		}
		int codeA = 0;
		// 将对象点从参考模型坐标系转换到世界坐标系
		Vertex3Temp0.setValue(model.bottomCenter);
		activeCamera.getCameraPositionL(Vertex3Temp0);
		// 先做隐藏面的消除
		if (Vertex3Temp0.z >= -this.activeCamera.near)
		{
			cutFace++;
			return;
		}
		// 投影变换
		activeCamera.getProjectPositionL(Vertex4Temp1, Vertex3Temp0);
		// CVV裁切开始------------------------------------------
		// 得到三角形三个点的区域代码
		codeA = activeCamera.areaCode(Vertex4Temp1);
		// 判断区域
		if (codeA != 0)// 完全接受的情况
		{
			return;
		}
		viewModels[viewObjModelCount] = model;
		addPointToBuffer(viewObjModelCount, (int) Vertex3Temp0.z, model.modelType);
		viewObjModelCount++;
	}

	// 计算路段和路段之间的夹角(被左移16位的弧度)
	public double getRoadGapAngle(int raodCurrent, int roadCompare)
	{
		if (road == null)
		{
			return -1;
		}
		road.getRoadCenter(raodCurrent, Vertex3Temp0);// 当前路段中心
		road.getRoadCenter(raodCurrent + 1, Vertex3Temp1);// 当前路段下一个中心
		Vertex3Temp1.subtract(Vertex3Temp0, Vertex3Temp2);// 当前路段中心->当前路段下一个中心
		road.getRoadCenter(roadCompare, Vertex3Temp3);// 比较路段中心
		road.getRoadCenter(roadCompare + 1, Vertex3Temp4);// 比较路段下一个中心
		Vertex3Temp4.subtract(Vertex3Temp3, Vertex3Temp5);// 比较路段中心->比较路段下一个中心
		Vertex2Temp0.setValue(Vertex3Temp2.x, -Vertex3Temp2.z);
		Vertex2Temp1.setValue(Vertex3Temp5.x, -Vertex3Temp5.z);
		float size0 = Vertex2Temp0.size();
		float size1 = Vertex2Temp1.size();
		if (size0 != 0 && size1 != 0)
		{
			int sign = Vertex2Temp0.outerProduct(Vertex2Temp1) > 0 ? -1 : 1;
			float cos = (Vertex2Temp0.innerProduct(Vertex2Temp1)) / (size1 * size0);
			double angle = C2D_Math.abs(C2D_Math.actCos(cos));
			return angle * sign;
		}
		return -1;
	}

	/** @todo ----------------绘制三维视景----------------- */
	protected void onPaint(C2D_Graphics c2d_g)
	{
		if (c2d_g == null)
		{
			return;
		}
		// #if Platform == "J2me"
//@		javax.microedition.lcdui.Graphics g = c2d_g.getInner();
//@		if (g == null)
//@		{
//@			return;
//@		}
//@		// 绘制世界中的物体
//@		int count = buffElementNB;
//@		int idStart = 0;
//@		int color;
//@		int type;
//@		boolean isTriangle = false;
//@		int xL = 0, xR = 0, yT = 0, yB = 0;
//@		int viewW = C2D_Stage.User_Size.m_width;
//@		int viewH = C2D_Stage.User_Size.m_height;
//@		for (int i = 0; i < count; i++)
//@		{
//@			idStart = (int) ((projectBufferOrder.m_datas[i] & 0xFFFFFFFF) << 3);
//@			type = projectBuffer[idStart + 6];
//@			color = projectBuffer[idStart + 7];
//@			if (type <= C3D_Model.MT_TYPE_WO)// 三角形面
//@			{
//@				g.setColor(color);
//@				if (!isTriangle)
//@				{
//@					g.setClip(0, 0, viewW, viewH);
//@					isTriangle = true;
//@				}
//@				if (projectBuffer[idStart] < projectBuffer[idStart + 2])
//@				{
//@					xL = projectBuffer[idStart];
//@					xR = projectBuffer[idStart + 2];
//@				}
//@				else
//@				{
//@					xL = projectBuffer[idStart + 2];
//@					xR = projectBuffer[idStart];
//@				}
//@				if (xL > projectBuffer[idStart + 4])
//@				{
//@					xL = projectBuffer[idStart + 4];
//@				}
//@				if (xR < projectBuffer[idStart + 4])
//@				{
//@					xR = projectBuffer[idStart + 4];
//@				}
//@				if (projectBuffer[idStart + 1] < projectBuffer[idStart + 3])
//@				{
//@					yT = projectBuffer[idStart + 1];
//@					yB = projectBuffer[idStart + 3];
//@				}
//@				else
//@				{
//@					yT = projectBuffer[idStart + 3];
//@					yB = projectBuffer[idStart + 1];
//@				}
//@				if (yT > projectBuffer[idStart + 5])
//@				{
//@					yT = projectBuffer[idStart + 5];
//@				}
//@				if (yB < projectBuffer[idStart + 5])
//@				{
//@					yB = projectBuffer[idStart + 5];
//@				}
//@				// g.setClip(xL, yT, xR-xL+1, yB-yT+1);
//@				if (xR < 0 || xL > viewW || yB < 0 || yT > viewH)
//@				{
//@					continue;
//@				}
//@				g.fillTriangle(projectBuffer[idStart], projectBuffer[idStart + 1], projectBuffer[idStart + 2],
//@						projectBuffer[idStart + 3], projectBuffer[idStart + 4], projectBuffer[idStart + 5]);
//@			}
//@			else if (type == C3D_ObjModel.MT_TYPE_O || type == C3D_ObjModel.MT_TYPE_CAR)// 对象模型,此时的color为ID
//@			{
//@				isTriangle = false;
//@				C3D_ObjModel model = viewModels[color];
//@				model.display(c2d_g, projectBuffer[idStart], projectBuffer[idStart + 1], projectBuffer[idStart + 5]);
//@			}
//@		}
		// #endif
	}

	// 释放资源
	public void onRelease()
	{
		super.onRelease();
		if (activeCamera != null)
		{
			activeCamera.releaseRes();
			activeCamera = null;
		}
		if (vModelDatas != null)
		{
			for (int m = 0; m < vModelDatas.size(); m++)
			{
				C3D_ModelData dataM = (C3D_ModelData) vModelDatas.elementAt(m);
				dataM.releaseRes();
			}
			vModelDatas.removeAllElements();
			vModelDatas = null;
		}
		if (vModels != null)
		{
			vModels.releaseRes();
		}
		vAlwaysShowModels.removeAllElements();
		C3D_LinkedModels.modelStart = null;
		C3D_LinkedModels.modelEnd = null;
		road = null;
		projectBuffer = null;
		projectBufferOrder = null;
		currentCameraPos = null;
		cameraPosOrg = null;
		roadNavigate = null;
		m_followNg = null;
		if (tempTriangle != null)
		{
			tempTriangle.releaseRes();
			tempTriangle = null;
		}
		if (viewModels != null)
		{
			for (int i = 0; i < viewModels.length; i++)
			{
				viewModels[i] = null;
			}
		}
	}
}
