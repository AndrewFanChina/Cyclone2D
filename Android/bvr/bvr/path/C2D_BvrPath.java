package bvr.path;

import c2d.lang.math.C2D_ArrayI;

public class C2D_BvrPath extends C2D_Serilizable
{
	/** ��ǰ��Ϊ·������Ӧ����Ϊ�ֵ� */
	C2D_Dictionary m_diction;
	/** ��Ϊ·��ͼ */
	C2D_ArrayI m_path = new C2D_ArrayI();
	/** �ϴμ�¼ʱ�� */
	private long m_lastTime = -1;
	/**
	 * ������Ϊ·��
	 * 
	 * @param diction
	 *            ��Ϊ�ֵ�
	 */
	public C2D_BvrPath(C2D_Dictionary diction)
	{
		m_diction = diction;
	}

	/**
	 * ��¼��������Ϊ�ڵ�
	 * 
	 * @param node
	 *            ��Ϊ�ڵ�
	 */
	public void logPath(C2D_BvrNode node)
	{
		if (m_diction == null || node == null)
		{
			return;
		}
		if (node.beSkipped())
		{
			return;
		}
		int nodeID = m_diction.getNodeID(node);
		if (nodeID < 0)
		{
			return;
		}
		int logTime = 0;
		long currentTime = System.currentTimeMillis();
		if (m_lastTime > 0)
		{
			logTime = (int) (currentTime - m_lastTime);
		}
		m_lastTime = currentTime;
		m_path.addElement(nodeID);
		m_path.addElement(logTime);
	}
	public String serilizedOut()
	{
		int len=m_path.size();
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < len; i++)
		{
			int iID=m_path.elementAt(i++);
			int iTime=m_path.elementAt(i);
			if(iTime<0)
			{
				break;
			}
			sb.append(iID);
			sb.append("#");
			sb.append(iTime);
			sb.append(",");
		}
		return sb.toString();
	}

}
