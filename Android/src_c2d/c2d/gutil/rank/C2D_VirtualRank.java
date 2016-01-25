package c2d.gutil.rank;

import c2d.lang.math.C2D_SortableArray;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;

public class C2D_VirtualRank extends C2D_Object
{
	/** 用户名和用户ID保留的字符长度 */
	public static int M_CharLen = 12;
	/** 用户排行单元 */
	private C2D_VRankItem m_userRank;
	/** 排行单元列表 */
	private C2D_SortableArray m_list = new C2D_SortableArray();

	/**
	 * 添加条目
	 * 
	 * @param item
	 *            条目
	 */
	public void addItem(C2D_VRankItem item)
	{
		if (item != null && !m_list.contains(item))
		{
			m_list.addElement(item);
		}
	}

	/**
	 * 添加条目
	 * 
	 * @param item
	 *            条目
	 */
	public void addItem(String userID, String userName, int score)
	{
		C2D_VRankItem vri = new C2D_VRankItem(userID, userName, score);
		m_list.addElement(vri);
		vri.transHadler(this);
	}

	/**
	 * 重新排列，按照分数的从大到小的顺序
	 */
	public void resort()
	{
		m_list.quickSort2();
		m_list.reverse();
		// 重新赋予名次
		int size = m_list.size();
		for (int i = 0; i < size; i++)
		{
			C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
			if (item != null)
			{
				item.setRankNumber(i + 1);
			}
		}
	}

	/**
	 * 清空条目
	 */
	public void clear()
	{
		if (m_list != null)
		{
			if (m_userRank != null)
			{
				m_list.remove(m_userRank);
			}
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
				if (item != null)
				{
					item.doRelease(this);
				}
			}
			m_list.clear();
		}
	}

	/**
	 * 显示详细信息
	 */
	public void logDetail()
	{
		if (m_userRank != null)
		{
			C2D_Debug.log("[用户排行信息]");
			m_userRank.logDetail();
		}
		if (m_list != null)
		{
			C2D_Debug.log("[排行帮单信息]");
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
				if (item != null)
				{
					item.logDetail();
				}
				else
				{
					C2D_Debug.logWarn("[空条目]");
				}
			}
		}
	}

	/**
	 * 执行玩家信息的部分隐藏。 对于用户自己，如果信息太长，才会执行截断和隐藏，否则完整保留， 对于其它玩家，必然会进行隐藏，如果太长会先行截断
	 */
	public void doMask()
	{
		if (m_userRank != null)
		{
			m_userRank.doMask(M_CharLen, false);
		}
		if (m_list != null)
		{
			C2D_Debug.log("[排行帮单信息]");
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
				if (item != null)
				{
					item.doMask(M_CharLen, true);
				}
			}
		}
	}

	public void onRelease()
	{
		if (m_list != null)
		{
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
				if (item != null)
				{
					item.doRelease(this);
				}
			}
			m_list.clear();
			m_list = null;
		}
		if (m_userRank != null)
		{
			m_userRank.doRelease(this);
			m_userRank = null;
		}
	}

	/**
	 * 获取用户排行
	 * 
	 * @return
	 */
	public C2D_VRankItem getUserRankItem()
	{
		return m_userRank;
	}

	/**
	 * 设置用户排行条目，并转移所有权给本对象
	 * 
	 * @param userRank
	 */
	public void setUserRankItem(C2D_VRankItem userRank)
	{
		if (m_userRank != null)
		{
			if (!m_userRank.equals(userRank))
			{
				m_userRank.doRelease(this);
			}
			m_userRank = null;
		}
		m_userRank = userRank;
		userRank.transHadler(this);
	}

	/**
	 * 获取当前排行榜的条目数
	 * 
	 * @return 条目数
	 */
	public int size()
	{
		return m_list.size();
	}

	/**
	 * 获取指定id的排行条目
	 * 
	 * @param id
	 *            条目id
	 * @return 条目
	 */
	public C2D_VRankItem getRankItem(int id)
	{
		if (id < 0 || id >= m_list.size())
		{
			return null;
		}
		return (C2D_VRankItem) m_list.elementAt(id);
	}

	/**
	 * 查看指定id的排行条目是否是当前用户排行
	 * 
	 * @param id
	 *            条目id
	 * @return 是否是当前用户排行
	 */
	public boolean isUserRankItem(int id)
	{
		C2D_VRankItem vi = getRankItem(id);
		if (vi == null)
		{
			return false;
		}
		return vi.equals(m_userRank);
	}

	/**
	 * 进行排序，从大到小
	 */
	public void doSort()
	{
		if (m_list != null)
		{
			m_list.quickSort2();
			m_list.reverse();
			resetRandID();
		}
	}

	/**
	 * 按照现在的顺序，重新记录排行ID
	 */
	private void resetRandID()
	{
		if (m_list != null)
		{
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				C2D_VRankItem item = (C2D_VRankItem) m_list.elementAt(i);
				if (item != null)
				{
					item.setRankNumber(1 + i);
				}
			}
		}
	}
}
