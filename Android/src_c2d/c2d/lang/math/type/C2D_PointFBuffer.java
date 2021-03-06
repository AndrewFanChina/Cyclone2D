package c2d.lang.math.type;

import c2d.lang.math.C2D_Array;

/**
 * 构造一个C3D_PointF缓冲类，由一个数组保存指定个数的C3D_PointF对象，
 * 每次都可以从本对象获得数组中下一个C3D_PointF对象，当到达数组末尾时进行循环。
 * @author AndrewFan
 *
 */
public class C2D_PointFBuffer
{
	private C2D_Array m_buffer;
	private int m_id;
	public C2D_PointFBuffer(int bufSize)
	{
		m_buffer=new C2D_Array(bufSize);
		for (int i = 0; i < bufSize; i++)
		{
			m_buffer.addElement(new C2D_PointF());
		}
	}
	public C2D_PointF next()
	{
		C2D_PointF f = (C2D_PointF)m_buffer.elementAt(m_id);
		m_id++;
		if(m_id>=m_buffer.size())
		{
			m_id=0;
		}
		return f;
	}
}
