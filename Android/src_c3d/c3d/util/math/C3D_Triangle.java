package c3d.util.math;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class C3D_Triangle
{
	public C3D_Vertex3 pointA, pointB, pointC;
	public int color;
	private static C3D_Vertex3 vectorTemp1 = new C3D_Vertex3();
	private static C3D_Vertex3 vectorTemp2 = new C3D_Vertex3();
	private static C3D_Vertex3 vectorTemp3 = new C3D_Vertex3();

	public C3D_Triangle()
	{
		pointA = new C3D_Vertex3();
		pointB = new C3D_Vertex3();
		pointC = new C3D_Vertex3();
	}

	public C3D_Vertex3 getElement(int id)
	{
		switch (id)
		{
		case 0:
			return pointA;
		case 1:
			return pointB;
		case 2:
			return pointC;
		}
		return null;
	}

	public void setValueA(long ax, long ay, long az)
	{
		pointA.x = ax;
		pointA.y = ay;
		pointA.z = az;
	}

	public void setValueA(C3D_Vertex3 vertex)
	{
		pointA.x = vertex.x;
		pointA.y = vertex.y;
		pointA.z = vertex.z;
	}

	public void setValueB(long ax, long ay, long az)
	{
		pointB.x = ax;
		pointB.y = ay;
		pointB.z = az;
	}

	public void setValueB(C3D_Vertex3 vertex)
	{
		pointB.x = vertex.x;
		pointB.y = vertex.y;
		pointB.z = vertex.z;
	}

	public void setValueC(long ax, long ay, long az)
	{
		pointC.x = ax;
		pointC.y = ay;
		pointC.z = az;
	}

	public void setValueC(C3D_Vertex3 vertex)
	{
		pointC.x = vertex.x;
		pointC.y = vertex.y;
		pointC.z = vertex.z;
	}

	public void setColor(int colorN)
	{
		color = colorN;
	}

	public void setValue(C3D_Vertex3 vertexA, C3D_Vertex3 vertexB, C3D_Vertex3 vertexC)
	{
		pointA.x = vertexA.x;
		pointA.y = vertexA.y;
		pointA.z = vertexA.z;
		pointB.x = vertexB.x;
		pointB.y = vertexB.y;
		pointB.z = vertexB.z;
		pointC.x = vertexC.x;
		pointC.y = vertexC.y;
		pointC.z = vertexC.z;
	}

	// 获得三点构成的法线
	public C3D_Vertex3 getNormal()
	{
		vectorTemp1.setValue(pointB);
		vectorTemp1.subtract(pointA);
		vectorTemp2.setValue(pointC);
		vectorTemp2.subtract(pointA);
		vectorTemp3.setValue(vectorTemp1.crossProduct(vectorTemp2));
		return vectorTemp3;
	}

	// 释放资源
	public void releaseRes()
	{
		pointA = null;
		pointB = null;
		pointC = null;
	}
}
