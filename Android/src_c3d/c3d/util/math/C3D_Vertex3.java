package c3d.util.math;


/**
 * 3D向量
 * @author AndrewFan
 *
 */
public class C3D_Vertex3 extends C3D_Vector3
{
	// 构造函数
	public C3D_Vertex3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public C3D_Vertex3()
	{
		x = y = z = 0;
	}

	public C3D_Vertex3(C3D_Vector3 v)
	{
		this(v.x, v.y, v.z);
	}
}
