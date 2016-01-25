package c2d.lang.math.type;

import c2d.lang.math.C2D_Array;

/**
 * ����һ��C2D_PointI�����࣬��һ�����鱣��ָ��������C2D_PointI����
 * ÿ�ζ����Դӱ���������������һ��C2D_PointI���󣬵���������ĩβʱ����ѭ����
 * @author AndrewFan
 *
 */
public class C2D_PointIBuffer
{
	private C2D_Array m_buffer;
	private int m_id;
	public C2D_PointIBuffer(int bufSize)
	{
		m_buffer=new C2D_Array(bufSize);
		for (int i = 0; i < bufSize; i++)
		{
			m_buffer.addElement(new C2D_PointI());
		}
	}
	public C2D_PointI next()
	{
		C2D_PointI f = (C2D_PointI)m_buffer.elementAt(m_id);
		m_id++;
		if(m_id>=m_buffer.size())
		{
			m_id=0;
		}
		return f;
	}
}
