package c3d;

import c2d.lang.math.C2D_Math;
import c3d.util.math.C3D_Vector2;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex2;
import c3d.util.math.C3D_Vertex3;

public abstract class C3D_Navigator extends C3D_ObjModel
{
	public C3D_Vector2 direction = null;// ����ʸ��
	public int AIPath = 50;// AI·���ٷֱ�
	public int currentRoadID = 0;// ��ǰ·��ID
	public int currentLoop = 0;// Ȧ��
	public float directionSize = 0;// ����ʸ����С
	public float speed = 0; // �ٶȴ�С
	public float acc = 0; // ���ٶȴ�С
	public float speedMax = 1 << 13; // �ٶȷ�ֵ
	public float grade = 0;// ��ǰ�¶ȴ�С
	protected boolean passedNode = false;// �Ƿ�Խ��·��
	private static final float collideAdjustAngle = (float)C2D_Math.PI / 30;// ��ײʱ�Ƕ�У��
	protected float height = 0;// �������ĸ߶�
	// ���þ�̬��
	protected static C3D_Vector3 vector3Temp0 = new C3D_Vector3();
	protected static C3D_Vector3 vector3Temp1 = new C3D_Vector3();

	protected static C3D_Vertex3 vertex3Temp0 = new C3D_Vertex3();
	protected static C3D_Vertex3 vertex3Temp1 = new C3D_Vertex3();

	protected static C3D_Vertex3 vPreCenter = new C3D_Vertex3();
	protected static C3D_Vertex3 vPreLeft = new C3D_Vertex3();
	protected static C3D_Vertex3 vPreRight = new C3D_Vertex3();

	protected static C3D_Vertex3 vCurrentLeft = new C3D_Vertex3();
	protected static C3D_Vertex3 vCurrentRight = new C3D_Vertex3();
	protected static C3D_Vertex3 vCurrentCenter = new C3D_Vertex3();

	protected static C3D_Vertex3 vNextLeft = new C3D_Vertex3();
	protected static C3D_Vertex3 vNextRight = new C3D_Vertex3();
	protected static C3D_Vertex3 vNextCenter = new C3D_Vertex3();

	protected static C3D_Vector2 Vector2Temp0 = new C3D_Vector2();
	protected static C3D_Vector2 Vector2Temp1 = new C3D_Vector2();
	protected static C3D_Vector2 Vector2Temp2 = new C3D_Vector2();

	protected C3D_World m_world;

	protected C3D_Navigator(C3D_Vertex3 pointT)
	{
		super(MT_TYPE_CAR, pointT);
		direction = new C3D_Vector2();
	}

	// �����߼��������Ƿ����Ե��������ײ-1 �����ײ 1�ұ���ײ
	public int logic()
	{
		C3D_RoadModel road = m_world.road;
		int collide = 0;
		passedNode = false;
		// ���²���
		directionSize = direction.dis();
		if (directionSize == 0)
		{
			System.out.println("error");
		}
		// Ԥ����һλ��
		nextPosition.x = bottomCenter.x + direction.x * speed / directionSize;
		nextPosition.y = -(bottomCenter.z - (direction.y * speed / directionSize));
		// �����Խ��·����
		road.getRoadPoint(currentRoadID + 1, vCurrentRight, vCurrentLeft); // ��õ�ǰ��·�����
		int segementPassed = 0;
		while (collideSegement(vCurrentLeft, vCurrentRight))
		{
			segementPassed++;
			road.getRoadPoint(currentRoadID + 1 + segementPassed, vCurrentRight, vCurrentLeft);
		}
		boolean collodeLeft = false;
		boolean collodeRight = false;
		// XZƽ�����ײ����
		for (int i = -2; i <= segementPassed + 2; i++)
		{
			road.getRoadPoint(currentRoadID + i, vCurrentRight, vCurrentLeft);
			road.getRoadPoint(currentRoadID + i + 1, vNextRight, vNextLeft);
			// ������ײ
			C3D_Vertex2 collidePos = collideEdge(vCurrentLeft, vNextLeft);
			if (collidePos != null)
			{
				collodeLeft = true;
				dealCollide(collidePos, vCurrentLeft, vNextLeft);
				collide = -1;
			}
			// ������ײ
			collidePos = collideEdge(vCurrentRight, vNextRight);
			if (collidePos != null)
			{
				collodeRight = true;
				dealCollide(collidePos, vCurrentRight, vNextRight);
				collide = 1;
			}
			if (collodeLeft || collodeRight)
			{
				if (collodeRight && !collodeLeft)
				{
					this.direction.rotate(collideAdjustAngle);
				}
				else if (collodeLeft && !collodeRight)
				{
					this.direction.rotate(-collideAdjustAngle);
				}
				dealCollideWall();
				break;
			}
		}
		// ����Ԥ��λ�ø���ڵ��ߵ���ײ
		road.getRoadPoint(currentRoadID, vCurrentRight, vCurrentLeft);
		C3D_Vertex2 collidePos = collideEdge(vCurrentLeft, vCurrentRight);
		if (collidePos != null)
		{
			dealCollide(collidePos, vCurrentLeft, vCurrentRight);
		}
		// ����������Խ��·����
		road.getRoadPoint(currentRoadID + 1, vCurrentRight, vCurrentLeft); // ��õ�ǰ��·�����
		segementPassed = 0;
		while (collideSegement(vCurrentLeft, vCurrentRight))
		{
			segementPassed++;
			road.getRoadPoint(currentRoadID + 1 + segementPassed, vCurrentRight, vCurrentLeft);
		}
		if (segementPassed > 0)
		{
			currentRoadID += segementPassed;
			if (currentRoadID >= road.gerNodeCount())
			{
				currentRoadID = 0;
				currentLoop++;
			}
			passedNode = true;
		}
		// ���õ���һ��λ��
		passedLen = C2D_Math.sqrt((bottomCenter.x - nextPosition.x) * (bottomCenter.x - nextPosition.x)
				+ (bottomCenter.z + nextPosition.y) * (bottomCenter.z + nextPosition.y));
		bottomCenter.x = nextPosition.x;
		bottomCenter.z = -nextPosition.y;
		// ���·��ڵ�
		getRoadPoint(road, currentRoadID);
		// ����߶�
		float x1 = bottomCenter.x - vPreCenter.x;
		float y1 = -(bottomCenter.z - vPreCenter.z);
		vCurrentCenter.subtract(vPreCenter,vector3Temp0);
		if (x1 * y1 != 0)
		{
			// �����������
			Vector2Temp1.setValue(vPreLeft.x - bottomCenter.x, -(vPreLeft.z - bottomCenter.z));
			Vector2Temp2.setValue(vPreRight.x - bottomCenter.x, -(vPreRight.z - bottomCenter.z));
			float s1 = C2D_Math.abs(Vector2Temp1.outerProduct(Vector2Temp2));
			Vector2Temp1.setValue(vCurrentLeft.x - bottomCenter.x, -(vCurrentLeft.z - bottomCenter.z));
			Vector2Temp2.setValue(vCurrentRight.x - bottomCenter.x, -(vCurrentRight.z - bottomCenter.z));
			float s2 = C2D_Math.abs(Vector2Temp1.outerProduct(Vector2Temp2));
			// �ó��߶�
			float yPre = C2D_Math.max(vPreLeft.y, vPreRight.y);
			float yCurrent = C2D_Math.max(vCurrentLeft.y, vCurrentRight.y);
			float h = yPre + (s1) * (yCurrent - yPre) / (s1 + s2);
			bottomCenter.y = h + height;
			// �ó��¶�
			if (passedNode)
			{
				Vector2Temp1.setValue(vCurrentCenter.x - vPreCenter.x, -(vCurrentCenter.z - vPreCenter.z));
				float sizeXZ = Vector2Temp1.dis();
				grade = ((yPre - yCurrent)) / sizeXZ;
			}
		}
		else
		{
			bottomCenter.y = vPreCenter.y + height;
		}
		return collide;
	}

	// ��ײ����
	private void dealCollide(C3D_Vertex2 collidePos, C3D_Vertex3 vCurrent, C3D_Vertex3 vNext)
	{
		// �ƶ�����ײλ�õ��90%
		float stepX = collidePos.x - bottomCenter.x;
		float stepY = collidePos.y + bottomCenter.z;
		float stepXGap = stepX / 10;
		float stepYGap = stepY / 10;
		boolean dontMove = true;
		if (Math.abs(stepXGap) > 256 && Math.abs(stepYGap) > 256)
		{
			nextPosition.x = bottomCenter.x + stepX - stepXGap;
			nextPosition.y = -bottomCenter.z + stepY - stepYGap;
			// �ٴ���֤�Ƿ�Խ��
			if (!C2D_Math.linCoss(bottomCenter.x, -bottomCenter.z, nextPosition.x, nextPosition.y, vCurrent.x,
					-vCurrent.z, vNext.x, -vNext.z))
			{
				dontMove = false;
			}
			else
			{
				System.out.println("���Է��õ㳬��");
			}
		}
		if (dontMove)
		{
			nextPosition.x = bottomCenter.x;
			nextPosition.y = -bottomCenter.z;
		}
		// debug

		// ���������������·�ߵ����߷���
		direction.setValue(vNext.x - vCurrent.x, -vNext.z + vCurrent.z);
		directionSize = direction.dis();
		if (directionSize == 0)
		{
			System.out.println("error");
		}
	}

	// ���÷���
	public void setDirection(C3D_Vector3 vDirection)
	{
		if (vDirection != null)
		{
			direction.setValue(vDirection.x, -vDirection.z);
			directionSize = direction.dis();
			if (directionSize == 0)
			{
				System.out.println("error");
			}

		}
	}

	// �����߼�
	protected static C3D_Vertex2 nextPosition = new C3D_Vertex2();
	protected static C3D_Vertex2 crossPoint = new C3D_Vertex2();
	public float passedLen = 0;// ��һ֡��ȥ��·�γ���

	// ���·����Ϣ
	protected void getRoadPoint(C3D_RoadModel road, int currentRoadID)
	{
		road.getRoadPoint(currentRoadID, vPreRight, vPreLeft);
		road.getRoadCenter(currentRoadID, vPreCenter);
		road.getRoadPoint(currentRoadID + 1, vCurrentRight, vCurrentLeft);
		road.getRoadCenter(currentRoadID + 1, vCurrentCenter);
		road.getRoadPoint(currentRoadID + 2, vNextRight, vNextLeft);
		road.getRoadCenter(currentRoadID + 2, vNextCenter);
	}

	// �����Ƿ���·�η�������ײ
	public boolean collideSegement(C3D_Vertex3 prePoint, C3D_Vertex3 nextPoint)
	{
		if (C2D_Math.linCoss1(bottomCenter.x, -bottomCenter.z, nextPosition.x, nextPosition.y, prePoint.x, -prePoint.z,
				nextPoint.x, -nextPoint.z))
		{
			return true;
		}
		return false;
	}

	// �����Ƿ���·�η�������ײ
	public C3D_Vertex2 collideEdge(C3D_Vertex3 prePoint, C3D_Vertex3 nextPoint)
	{
		if (C2D_Math.linCoss(bottomCenter.x, -bottomCenter.z, nextPosition.x, nextPosition.y, prePoint.x, -prePoint.z,
				nextPoint.x, -nextPoint.z))
		{
			return C2D_Math.getCross_V2D(bottomCenter.x, -bottomCenter.z, nextPosition.x, nextPosition.y, prePoint.x,
					-prePoint.z, nextPoint.x, -nextPoint.z, false);
		}
		return null;
	}

	// ��ײǽ��
	public abstract void dealCollideWall();

	// �ͷ���Դ
	public void releaseRes()
	{
		super.releaseRes();
		direction = null;
	}
}
