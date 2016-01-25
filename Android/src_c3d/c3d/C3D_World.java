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
 * 3D������
 * 
 * @author AndrewFan
 * 
 */
public class C3D_World extends C2D_View
{
	public static C3D_Camera activeCamera = null; // ��ǰ�Ļ���
	protected Vector vModelDatas = new Vector(); // ����ģ�͵�ԭ������
	protected static C3D_LinkedModels vModels = new C3D_LinkedModels();// ��ǰ�����е�����ģ��
	protected static Vector vAlwaysShowModels = new Vector();// һֱ�ɼ���ģ��
	public static int nbViewRoadChunk = 20;// ���Կ�����·����
	public static int nbViewObjectChunk = 15;// ���Կ��������·����
	public C3D_RoadModel road = null;
	private static int BUFF_STEP = 8;// ���嵥Ԫ���ݳ���[x1��y2��x2��y2��x3��y3��Z��color],һ����ɫ��ʾ�����Σ�0xAAAAAA��ʾ·�νڵ�
	private static int BUFFER_SIZE = 1500;
	private static int projectBuffer[];// ͶӰ����
	private static C2D_SortableArrayL projectBufferOrder;// 64λͶӰ��������[32λ��Z��32λID]
	public static int buffElementNB = 0;// ʹ�õĻ��嵥Ԫ��Ŀ
	public static int buffDeep = 0;// ʹ�õĻ������ݳ���
	public static int modelDeep = 0;// ���Ƶ�ģ����
	public static int cutFace = 0;// ����������
	public static int roadColorA = 0x555555;// ·����ɫA
	public static int roadColorB = 0x555555;// ·����ɫB
	public static int roadColorIA = 0x555555;// �ڱ�����ɫA
	public static int roadColorIB = 0x555555;// �ڱ�����ɫB
	public static int roadColorOA = 0x555555;// �������ɫA
	public static int roadColorOB = 0x555555;// �������ɫB
	public static int roadColorC = 0xFFFFFF;// ·�泵����ɫ
	// ������Χ
	protected static int horizonY = 200;// ��ƽ�߸߶�
	protected static int viewH, viewMax, viewGapX, viewGapY;
	// �����ϵĵ��߷���
	public static final byte prop_star = 0;// ����
	public static final byte prop_box = 1;// ����
	public static final byte prop_accelerate = 2;// ������
	public static final byte prop_lawn = 3;// ��ƺ
	public static final byte prop_pool = 4;// ˮ��
	public static final byte prop_star_b = 5;// ��������
	public static final byte prop_others = 6;// ����װ�ε���
	// ��ʱ����
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
	// ·�����
	private C3D_Vertex3 cameraPosOrg = new C3D_Vertex3();// ��ǰ���λ��(δ����𶯲���)
	public C3D_Vertex3 currentCameraPos = new C3D_Vertex3();// ��ǰ���λ��(����𶯲���)
	public C3D_Vector3 roadNavigate = new C3D_Vector3();// ·�泯��
	// �������
	public static float angleHorizon = 0;// �����ˮƽת��
	protected static float cameraHeight = 256;// ����߶�
	public long cameraDisToCar = 163840;// ���������֮��ı�׼����(XZƽ��)
	protected float cameraAngleEle = (float) -C2D_Math.PI / 20;// ���������
	protected static long shakeOffsetX = 0;// ���X����ƫ��
	protected static long shakeOffsetY = 0;// ���Y����ƫ��
	protected static long shakeOffsetZ = 0;// ���Z����ƫ��
	public int roadMosetHigh = 0; // ��ߵ�·��
	public static boolean showRoadEdge = true;
	C3D_Triangle tempTriangle;
	/** ����ĵ����� */
	public static C3D_Navigator m_followNg = null;
	// ·��Զ��ͶӰ����
	public static int roadFarX = 0;
	public static int roadFarY = 0;
	private static boolean isRoadFarFind = false;// �Ƿ��Ѿ��ҵ�·��Զ��

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
	 * ���ô�С���������߶�
	 * 
	 * @param width
	 *            �����õĿ��
	 * @param height
	 *            �����õĸ߶ȶ�
	 * @return �����Ƿ�ɹ�����
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
	 * ����ָ���ĵ�ͼ������������Ӿ�����
	 * 
	 * @param mapID
	 *            ��ͼ���
	 */
	public void loadWorld(int mapID)
	{
		// ���س�������
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
			modelTroopCount = C2D_IOUtil.readShort(modelTroopCount, dis);// ģ��Ⱥ����
			for (int i = 0; i < modelTroopCount; i++)
			{
				byteData = C2D_IOUtil.readByte(byteData, dis);// ģ��Ⱥ����
				switch (byteData)
				{
				case C3D_Model.MT_TYPE_N:
					C3D_ModelData dataN = new C3D_ModelData();
					dataN.readObject(dis);
					vModelDatas.addElement(dataN);
					C3D_Model modelN = new C3D_Model(C3D_Model.MT_TYPE_N, dataN);
					modelN.alwaysVisible = C2D_IOUtil.readBoolean(modelN.alwaysVisible, dis);// ��ȡ������Ϣ
					modelN.followRoadID = C2D_IOUtil.readShort(modelN.followRoadID, dis);// ��ȡ����ID
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
					cModeLen = C2D_IOUtil.readShort(cModeLen, dis);// ͨ��ģ�ͳ���
					propID = C2D_IOUtil.readShort(propID, dis);// ����ID
					propActorID = C2D_IOUtil.readShort(propActorID, dis);// ����ActorID
					C3D_ModelData dataC = null;
					for (int j = 0; j < cModeLen; j++)
					{
						C3D_Vertex3 vertex3 = new C3D_Vertex3();// ����������Ϣ
						vertex3.readObject(dis);
						int angle = C2D_IOUtil.readInt(0, dis);
						C3D_Vector3 vector3 = new C3D_Vector3();// ����scal��Ϣ
						vector3.readObject(dis);
						short followRoadID = 0;
						followRoadID = C2D_IOUtil.readShort(followRoadID, dis);// ��ȡ����ID
						// �����Ƿ��Ѿ�������ͬ���ֵ�ͨ��ģ������
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

	// ���زο�ģ��ԭ������
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
					// ���س�������
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

	/** @todo ----------��ʼ�������е��������---------- */
	public void initWorld()
	{
		if (projectBuffer == null)
		{
			projectBuffer = new int[BUFFER_SIZE * BUFF_STEP]; // ͶӰ����
		}
		if (projectBufferOrder == null)
		{
			projectBufferOrder = new C2D_SortableArrayL(BUFFER_SIZE); // 64λͶӰ��������[32λ��Z��32λID]
		}
		if (tempTriangle == null)
		{
			tempTriangle = new C3D_Triangle();
		}
		// ����·���·���Ե����
		if (road != null)
		{
			road.createRoad();
		}
		// ���������Ϣ
		C3D_ModelNode nodeI = vModels.headModel;
		do
		{
			nodeI.model.checkCenter();
			nodeI = nodeI.getNext();
		}
		while (!nodeI.equals(vModels.headModel));
		// ���õ�ǰ��ͶӰ����
		resetProjectBuffer();
		// ��ʼ�����----------------------------------------------------------
		// ��ʼ�������Ϣ
		activeCamera = new C3D_Camera();
		// ��ʼ·��
		C3D_LinkedModels.modelStart = null;
		C3D_LinkedModels.modelEnd = null;
		// ��ʼ��·�泯��
		setRoadNavigate(0);
	}

	// ��ʼ��·�浼��
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
			// ��ʼ��λ��
			cameraPosOrg.setValue(Vertex3Temp2.x, Vertex3Temp2.y, Vertex3Temp2.z);
			cameraPosOrg.y = Vertex3Temp2.y + cameraHeight + shakeOffsetX;
			currentCameraPos.setValue(cameraPosOrg);
			// ���������ʼλ�ú�ת��
			activeCamera.reset();
			activeCamera.rotateX(cameraAngleEle);
			activeCamera.rotateY((float) (angleHorizon - C2D_Math.PI / 2));
			this.activeCamera.setEyePosition(currentCameraPos.x, currentCameraPos.y, currentCameraPos.z);
		}
		// �������е�����
		checkObjectInCamera();
	}

	// ��鴦��������е�����
	public void checkObjectInCamera()
	{
		vModels.searchNextNode(currentRoadID + 1, (currentRoadID + nbViewObjectChunk) % road.gerNodeCount());
	}

	public static void setHY(int horizonYT)
	{
		horizonY = horizonYT;// ��ƽ�߸߶�
		int w = C2D_Stage.User_Size.m_width;
		int h = C2D_Stage.User_Size.m_height;
		viewH = w;
		viewMax = C2D_Math.max(w, h);
		viewGapX = viewMax - (w >> 1);
		viewGapY = viewMax - (h >> 1) + (horizonY - (h >> 1));
	}

	/** @todo ----------------------��������------------------------------- */
	public static int currentRoadID = 0;

	public void setFollowNavigator(C3D_Navigator navigator)
	{
		m_followNg = navigator;
	}

	/** @todo ----------------------������������------------------------------- */
	public void updateWorld()
	{
		// ��������
		if (this.m_followNg != null)
		{
			if (currentRoadID != m_followNg.currentRoadID)
			{
				currentRoadID = m_followNg.currentRoadID;
				// ����·�泯��
				road.getRoadCenter(m_followNg.currentRoadID, Vertex3Temp0);
				road.getRoadCenter(m_followNg.currentRoadID + 1, Vertex3Temp1);
				roadNavigate.setValue(Vertex3Temp1.x - Vertex3Temp0.x, Vertex3Temp1.y - Vertex3Temp0.y, Vertex3Temp1.z
						- Vertex3Temp0.z);
				// ����������
				checkObjectInCamera();
			}
		}
		// �������-----------------------------------------------------
		// У��ˮƽ�ӽ�
		// �ӽ�1-ʹ��·�淽�򵼺�
		road.getRoadCenter(m_followNg.currentRoadID, Vertex3Temp0);
		road.getRoadCenter(m_followNg.currentRoadID + 2, Vertex3Temp2);
		Vertex3Temp2.subtract(Vertex3Temp0, Vertex3Temp1);
		// �ӽ�2-ʹ���������򵼺�
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
		// �����복֮��ľ���(XZƽ��)
		m_followNg.bottomCenter.subtract(cameraPosOrg, Vertex3Temp0);
		float size = C2D_Math.sqrt(Vertex3Temp0.x * Vertex3Temp0.x + Vertex3Temp0.z * Vertex3Temp0.z);
		if (size > cameraDisToCar)// ����У��
		{
			cameraPosOrg.x += (size - cameraDisToCar) * Vertex3Temp0.x / (2 * size);
			cameraPosOrg.z += (size - cameraDisToCar) * Vertex3Temp0.z / (2 * size);
		}
		// β��У��
		cameraPosOrg.x += (cameraDestX - cameraPosOrg.x) / 4;
		cameraPosOrg.z += (cameraDestZ - cameraPosOrg.z) / 4;
		// �߶�����
		cameraPosOrg.y = m_followNg.bottomCenter.y + cameraHeight;
		// �𶯵���
		currentCameraPos.setValue(cameraPosOrg.x + shakeOffsetX, cameraPosOrg.y + shakeOffsetY, cameraPosOrg.z
				+ shakeOffsetZ);
		// ��������߶�
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

	// ���������Ϣ
	public void adjustCamera()
	{
		activeCamera.reset();
		activeCamera.rotateX(cameraAngleEle);
		activeCamera.rotateY((float) C2D_Math.standirdAngle(angleHorizon - C2D_Math.PI / 2));
		this.activeCamera.setEyePosition(currentCameraPos.x, currentCameraPos.y, currentCameraPos.z);
	}

	/** @todo -----------------ͶӰ��������----------------- */
	/**
	 * ͶӰ��������
	 * 
	 * @param onlyRoad
	 *            ���ֻ��·�棬��û��·�����
	 */
	public void projectWorld(boolean onlyRoad)
	{
		if (activeCamera == null)
		{
			return;
		}
		resetProjectBuffer();
		// ͶӰ��������
		if (!onlyRoad)
		{
			C3D_ModelNode modeI = C3D_LinkedModels.modelStart;
			while (modeI != null && !modeI.equals(C3D_LinkedModels.modelEnd))
			{
				projectModel(modeI.model);
				modeI = modeI.getNext();
			}
			// FIXME ����Ӧ�ò���Ҫ
			for (int i = 0; i < vAlwaysShowModels.size(); i++)
			{
				C3D_Model model = (C3D_Model) vAlwaysShowModels.elementAt(i);
				projectModel(model);
			}
		}
		// ͶӰ·��
		if (road != null)
		{
			roadMosetHigh = C2D_Stage.User_Size.m_width;
			isRoadFarFind = false;
			projectRoad(road, 0);// m_followNg.currentRoadID
		}
	}

	// ���õ�ǰ��ͶӰ����
	public void resetProjectBuffer()
	{
		buffDeep = 0;
		buffElementNB = 0;
		modelDeep = 0;
		cutFace = 0;
		viewObjModelCount = 0;
	}

	/** @todo --ͶӰͨ�úͷ�ͨ��ģ��(���ܴ���·��ģ�ͺͶ���ģ��)-- */
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
			// ģ�Ͱ�Χ�����--------------------------------------------------------------
			float maxR = model.maxR;// ���İ�Χ��뾶
			float xmax = activeCamera.right;
			float xmin = activeCamera.left;
			float ymax = activeCamera.top;
			float ymin = activeCamera.bottom;
			float z0 = -activeCamera.near;
			C3D_Vertex3 centerInWorld = model.boundingCenter;
			C3D_Vector3 centerInCamera = activeCamera.getCameraPosition(centerInWorld);
			// ��������Ƿ����Ҳ�����֮��
			C3D_Vector3 v = Vector3Temp0;
			v.setValue(-z0, 0, xmax); // ��������Ҳ෢����������������ת90�Ȼ�õ��Ҳ�����ķ���
			float t = v.innerProduct(centerInCamera); // ���������������Ҳ����淨���ϵı�������v.size2()����ͶӰ,���������ĵ��Ҳ���������v.size2()��
			// ������ĺ��Ҳ�����֮��ľ���
			// ƽ��ax+by+cz+d=0��(x0,y0,z0)֮��ľ����ƽ��h^2��
			// h^2 = |a*x0+b*y0+c*z0+d|^2/(a*a+b*b+c*c)
			double u = t * t - maxR * maxR * v.size2(); // ��t*t/v.size2()>p.rƽ�����㵽��ľ�����ڰ뾶
			if (t >= 0 && u > 0)
			{
				return; // ���Ҳ�����֮��
			}
			// ��������Ƿ����������֮����?
			v.setValue(z0, 0, -xmin);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // ���������֮��
			}
			// ��������Ƿ����ϲ�����֮����
			v.setValue(0, -z0, ymax);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // ���ϲ�����֮��
			}
			// ��������Ƿ����²�����֮����
			v.setValue(0, z0, -ymin);
			t = v.innerProduct(centerInCamera);
			u = t * t - maxR * maxR * v.size2();
			if (t >= 0 && u > 0)
			{
				return; // ���²�����֮��
			}
		}
		// ��ģ�����ݿ�ʼ��ģ----------------------------------------------------------
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
				// ͶӰ������
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

	/** @todo -----------------ͶӰ������------------------- */
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
		// �������δӲο�ģ������ϵת������������ϵ
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
			// �������δ���������ϵת�����������ϵ
			activeCamera.getCameraPositionL(triangle.pointA);
			activeCamera.getCameraPositionL(triangle.pointB);
			activeCamera.getCameraPositionL(triangle.pointC);
		}
		// ���������������
		vn.setValue(triangle.getNormal());
		Vertex3Temp0.setValue((triangle.pointA.x + triangle.pointB.x + triangle.pointC.x) / 3, (triangle.pointA.y
				+ triangle.pointB.y + triangle.pointC.y) / 3,
				(triangle.pointA.z + triangle.pointB.z + triangle.pointC.z) / 3);
		if (vn.innerProduct(Vertex3Temp0) >= 0)
		{
			cutFace++;
			return;
		}
		// ͶӰ�任
		activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
		activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
		activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
		// CVV���п�ʼ------------------------------------------
		// �õ���������������������
		codeA = activeCamera.areaCode(Vertex4Temp1);
		codeB = activeCamera.areaCode(Vertex4Temp2);
		codeC = activeCamera.areaCode(Vertex4Temp3);
		// �ж�����
		if ((codeA | codeB | codeC) == 0)// ��ȫ���ܵ����
		{
			allowCountRoadHigh2 = true;
			addTriangleToBuffer(triangle.color, modelType);// getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			allowCountRoadHigh2 = false;
		}
		else if ((codeA & codeB & codeC) != 0)// ��ȫ�ܾ������
		{
			return;
		}
		else
		// ��ƽ��ķָ�ü�...
		{
			// ����ÿһ��������-------------------
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
					// ��¼
					indexOut[nbBehind] = j;
					nbBehind++;
				}
			}
			// �������--------------------------
			if (nbFront == 3)// 3��ȫ��λ�����������
			{
				addTriangleToBuffer(triangle.color, modelType);// getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
			else if (nbFront == 2)// ��2��λ������
			{
				V_A.setValue(triangle.getElement(indexIn[0]));
				V_B.setValue(triangle.getElement(indexIn[1]));
				V_C.setValue(triangle.getElement(indexOut[0]));
				// ��һ��������
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
				// ͶӰ�任
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
				// �ڶ���������
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
				// ͶӰ�任
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
			else if (nbFront == 1)// ��1��λ������
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
				// ͶӰ�任
				activeCamera.getProjectPositionL(Vertex4Temp1, triangle.pointA);
				activeCamera.getProjectPositionL(Vertex4Temp2, triangle.pointB);
				activeCamera.getProjectPositionL(Vertex4Temp3, triangle.pointC);
				addTriangleToBuffer(triangle.color, modelType);// ,getMinZ(triangle.pointA,triangle.pointB,triangle.pointC)
			}
		}
	}

	// ���������ָ������ɫͶӰ������
	private static C3D_Triangle tempTriangleTemp1 = new C3D_Triangle();

	private void projectTriangle(C3D_Vertex3 pointA, C3D_Vertex3 pointB, C3D_Vertex3 pointC, int color)
	{
		tempTriangleTemp1.pointA.setValue(pointA);
		tempTriangleTemp1.pointB.setValue(pointB);
		tempTriangleTemp1.pointC.setValue(pointC);
		tempTriangleTemp1.setColor(color);
		this.projectTriangle(tempTriangleTemp1, null);
	}

	// �������������ӵ�����㣬�����ؾ���ƽ��
	private float getMinZ(C3D_Vertex3 pointA, C3D_Vertex3 pointB, C3D_Vertex3 pointC)
	{
		float zSqrA = pointA.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zSqrB = pointB.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zSqrC = pointC.subtract(activeCamera.eyePosition, Vertex3Temp1).size();
		float zMin = C2D_Math.min(C2D_Math.min(zSqrA, zSqrB), zSqrC);
		return zMin;
	}

	/** @todo --------------��ͶӰ�������������ӵ�ͶӰ����---------------- */
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
		projectBuffer[buffDeep++] = type;// ������Ϣ
		projectBuffer[buffDeep++] = color;// �����color������Ǵ�·���Ƿ���Ҫ��ʾ
		long S_16 = 65536;
		// ��z�任����ֵ����������,�������[-1,1]��չ��[0,65536*2]
		long z = S_16
				- (long) C2D_Math.max(
						C2D_Math.max((Vertex4Temp1.z * S_16) / (Vertex4Temp1.w), (Vertex4Temp2.z * S_16)
								/ (Vertex4Temp2.w)), (Vertex4Temp3.z * S_16) / (Vertex4Temp3.w));
		// if(z<0||z>1<<17)
		// {
		// System.out.println("------------------------ error z"+z);
		// }
		projectBufferOrder.m_datas[buffElementNB] = (z << 32) | buffElementNB;
		// ˳���ҳ���ߵ�·��,��������������α���ɼ�
		if (type == C3D_Model.MT_TYPE_R)
		{
			int high = C2D_Math.min(C2D_Math.min(projectBuffer[buffDeep - 7], projectBuffer[buffDeep - 5]),
					projectBuffer[buffDeep - 3]);// >>8
			if (high < roadMosetHigh && allowCountRoadHigh1 && allowCountRoadHigh2)
			{
				roadMosetHigh = high;
			}
			// ����·��Զ������
			if (!isRoadFarFind)
			{
				roadFarX = (projectBuffer[buffDeep - 6] + projectBuffer[buffDeep - 4]) >> 1;
				roadFarY = (projectBuffer[buffDeep - 7] + projectBuffer[buffDeep - 5]) >> 1;
				isRoadFarFind = true;
			}
		}
		buffElementNB++;
	}

	/** @todo --------------��ͶӰ���ĵ����ӵ�ͶӰ����---------------- */
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
		projectBuffer[buffDeep++] = modelType;// ������Ϣ
		projectBuffer[buffDeep++] = objID;// ��Ŷ���ID
		// ��z�任����ֵ����������,�������[-1,1]��չ��[0,65536*2]
		long S_16 = 65536;
		long z = S_16 - (long) ((Vertex4Temp1.z * S_16) / (Vertex4Temp1.w));
		// if(z<0||z>1<<17)
		// {
		// System.out.println("------------------------ error z"+z);
		// }
		projectBufferOrder.m_datas[buffElementNB] = (z << 32) | buffElementNB;
		buffElementNB++;
	}

	/** @todo ----------------�������----------------- */
	private static int carAdjustOrder[] = new int[6];// ����У������

	public void orderBuffer()
	{
		int orderID = buffElementNB;
		// �ȶ���ά�����·���minZ��������(��С����),�������ٽ�����Z�ص��жϺ����򣬻�ø���ȷ�Ľ�������ǻή��Ч�ʡ�
		projectBufferOrder.quickSort3();
		// �����źõ�˳����ʹ�ö���ģ��������ʾ��Ȩֵ��������ģ�Ϳ����ڵ����ڵ�·��ģ��.....
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
				if (passRoad > 0 || (i == orderID - 1 && passRoad > 0))// �ҵ��˻��ס�����·��
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

	/** @todo ----------------ͶӰ·��ģ��----------------- */
	public void projectRoad(C3D_RoadModel model, int roadNodeID)
	{
		if (model == null)
		{
			return;
		}
		// ���ҵ���ԶͶӰ·��
		int farID = roadNodeID + nbViewRoadChunk;// ���ID
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
	public static double roadWidthShiftI = 0.5;// �����Ŀ��
	public static double roadWidthShiftO = 1.0;// �����Ŀ��
	public static double roadWidthShiftC = 0.125;// �����Ŀ��
	private static boolean allowCountRoadHigh1 = false;// ����ͳ��·��߶�����1
	private static boolean allowCountRoadHigh2 = false;// ����ͳ��·��߶�����2

	protected void projectRoadNode(int roadID)
	{
		int color = 0;
		color = roadID % 2 == 0 ? roadColorA : roadColorB;
		// ���·��ڵ����������----------------------------
		road.getRoadPoint(roadID, Vertex3Temp1, Vertex3Temp3);// Զ���ҵ�
		road.getRoadPoint(roadID - 1, Vertex3Temp4, Vertex3Temp6);// �����ҵ�
		allowCountRoadHigh1 = false;
		// ��ʾ·����----------------------------------------
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
		// ��ʾ·���ĳ�������-------------------------------------
		boolean showRoadCenter = roadID % 2 == 0;
		if (showRoadCenter)
		{
			road.getRoadCenter(roadID, Vertex3Temp7);// Զ���ĵ�
			road.getRoadCenter(roadID - 1, Vertex3Temp8);// �����ĵ�
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
		// ��ʾ·��------------------------------------------
		allowCountRoadHigh1 = true;
		tempTriangle.setColor(color);
		if (showRoadCenter)
		{
			// ��չ����
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

	/** @todo ----------------ͶӰ����ģ��----------------- */
	private static C3D_ObjModel viewModels[] = new C3D_ObjModel[50];// ���Կ����Ķ���ģ��
	private static int viewObjModelCount = 0;// ���Կ�����ģ����

	public void projectObjectModel(C3D_ObjModel model)
	{
		if (model == null || (model.modelType != C3D_ObjModel.MT_TYPE_O && model.modelType != C3D_ObjModel.MT_TYPE_CAR))
		{
			return;
		}
		int codeA = 0;
		// �������Ӳο�ģ������ϵת������������ϵ
		Vertex3Temp0.setValue(model.bottomCenter);
		activeCamera.getCameraPositionL(Vertex3Temp0);
		// ���������������
		if (Vertex3Temp0.z >= -this.activeCamera.near)
		{
			cutFace++;
			return;
		}
		// ͶӰ�任
		activeCamera.getProjectPositionL(Vertex4Temp1, Vertex3Temp0);
		// CVV���п�ʼ------------------------------------------
		// �õ���������������������
		codeA = activeCamera.areaCode(Vertex4Temp1);
		// �ж�����
		if (codeA != 0)// ��ȫ���ܵ����
		{
			return;
		}
		viewModels[viewObjModelCount] = model;
		addPointToBuffer(viewObjModelCount, (int) Vertex3Temp0.z, model.modelType);
		viewObjModelCount++;
	}

	// ����·�κ�·��֮��ļн�(������16λ�Ļ���)
	public double getRoadGapAngle(int raodCurrent, int roadCompare)
	{
		if (road == null)
		{
			return -1;
		}
		road.getRoadCenter(raodCurrent, Vertex3Temp0);// ��ǰ·������
		road.getRoadCenter(raodCurrent + 1, Vertex3Temp1);// ��ǰ·����һ������
		Vertex3Temp1.subtract(Vertex3Temp0, Vertex3Temp2);// ��ǰ·������->��ǰ·����һ������
		road.getRoadCenter(roadCompare, Vertex3Temp3);// �Ƚ�·������
		road.getRoadCenter(roadCompare + 1, Vertex3Temp4);// �Ƚ�·����һ������
		Vertex3Temp4.subtract(Vertex3Temp3, Vertex3Temp5);// �Ƚ�·������->�Ƚ�·����һ������
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

	/** @todo ----------------������ά�Ӿ�----------------- */
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
//@		// ���������е�����
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
//@			if (type <= C3D_Model.MT_TYPE_WO)// ��������
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
//@			else if (type == C3D_ObjModel.MT_TYPE_O || type == C3D_ObjModel.MT_TYPE_CAR)// ����ģ��,��ʱ��colorΪID
//@			{
//@				isTriangle = false;
//@				C3D_ObjModel model = viewModels[color];
//@				model.display(c2d_g, projectBuffer[idStart], projectBuffer[idStart + 1], projectBuffer[idStart + 5]);
//@			}
//@		}
		// #endif
	}

	// �ͷ���Դ
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
