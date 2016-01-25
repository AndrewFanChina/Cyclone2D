package bvr.path;

import c2d.lang.math.C2D_ArrayI;

public class C2D_BvrPath extends C2D_Serilizable
{
	/** 当前行为路径所对应的行为字典 */
	C2D_Dictionary m_diction;
	/** 行为路径图 */
	C2D_ArrayI m_path = new C2D_ArrayI();
	/** 上次记录时间 */
	private long m_lastTime = -1;
	/**
	 * 构建行为路径
	 * 
	 * @param diction
	 *            行为字典
	 */
	public C2D_BvrPath(C2D_Dictionary diction)
	{
		m_diction = diction;
	}

	/**
	 * 记录发生的行为节点
	 * 
	 * @param node
	 *            行为节点
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
