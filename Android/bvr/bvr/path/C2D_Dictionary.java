package bvr.path;

import java.util.Hashtable;

public class C2D_Dictionary extends C2D_Serilizable
{
	public Hashtable m_dictionary = new Hashtable();
	public String m_version = "v1.0.0";
	/**
	 * 构建行为字典
	 * @param definer 行为字典创建者
	 */
	public C2D_Dictionary(C2D_DictionDefiner definer)
	{
		if (definer != null)
		{
			m_version = definer.getDicVersion();
			C2D_BvrNode nodes[] = definer.getDicNodes();
			if (nodes != null)
			{
				for (int i = 0; i < nodes.length; i++)
				{
					C2D_BvrNode nodeI = nodes[i];
					if (nodeI != null)
					{
						String nodeName=nodeI.getBvrNodeName();
						if(nodeName!=null)
						{
							Integer id = new Integer(i);
							m_dictionary.put(id,nodeName);
						}
					}
				}
			}
		}
	}
	/**
	 * 获取指定行为节点的ID
	 * @param node 行为节点
	 * @return 节点ID
	 */
	public int getNodeID(C2D_BvrNode node)
	{
		if(node==null)
		{
			return -1;
		}
		Object o = m_dictionary.get(node);
		if(o==null || !(o instanceof Integer))
		{
			return -1;
		}
		return ((Integer)o).intValue();
	}
	public String serilizedOut()
	{
		int len=m_dictionary.size();
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < len; i++)
		{
			Integer id = new Integer(i);
			String nodeName = (String)m_dictionary.get(id);
			sb.append(nodeName);
			sb.append(",");
		}
		return sb.toString();
	}
	
}
