package c2d.lang.math.type;

import c2d.lang.math.C2D_Array;

/**
 * ����һ��C3D_PointF�����࣬��һ�����鱣��ָ��������C3D_PointF����
 * ÿ�ζ����Դӱ���������������һ��C3D_PointF���󣬵���������ĩβʱ����ѭ����
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
