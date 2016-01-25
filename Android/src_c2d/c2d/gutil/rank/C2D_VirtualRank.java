package c2d.gutil.rank;

import c2d.lang.math.C2D_SortableArray;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;

public class C2D_VirtualRank extends C2D_Object
{
	/** �û������û�ID�������ַ����� */
	public static int M_CharLen = 12;
	/** �û����е�Ԫ */
	private C2D_VRankItem m_userRank;
	/** ���е�Ԫ�б� */
	private C2D_SortableArray m_list = new C2D_SortableArray();

	/**
	 * �����Ŀ
	 * 
	 * @param item
	 *            ��Ŀ
	 */
	public void addItem(C2D_VRankItem item)
	{
		if (item != null && !m_list.contains(item))
		{
			m_list.addElement(item);
		}
	}

	/**
	 * �����Ŀ
	 * 
	 * @param item
	 *            ��Ŀ
	 */
	public void addItem(String userID, String userName, int score)
	{
		C2D_VRankItem vri = new C2D_VRankItem(userID, userName, score);
		m_list.addElement(vri);
		vri.transHadler(this);
	}

	/**
	 * �������У����շ����ĴӴ�С��˳��
	 */
	public void resort()
	{
		m_list.quickSort2();
		m_list.reverse();
		// ���¸�������
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
	 * �����Ŀ
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
	 * ��ʾ��ϸ��Ϣ
	 */
	public void logDetail()
	{
		if (m_userRank != null)
		{
			C2D_Debug.log("[�û�������Ϣ]");
			m_userRank.logDetail();
		}
		if (m_list != null)
		{
			C2D_Debug.log("[���аﵥ��Ϣ]");
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
					C2D_Debug.logWarn("[����Ŀ]");
				}
			}
		}
	}

	/**
	 * ִ�������Ϣ�Ĳ������ء� �����û��Լ��������Ϣ̫�����Ż�ִ�нضϺ����أ��������������� ����������ң���Ȼ��������أ����̫�������нض�
	 */
	public void doMask()
	{
		if (m_userRank != null)
		{
			m_userRank.doMask(M_CharLen, false);
		}
		if (m_list != null)
		{
			C2D_Debug.log("[���аﵥ��Ϣ]");
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
	 * ��ȡ�û�����
	 * 
	 * @return
	 */
	public C2D_VRankItem getUserRankItem()
	{
		return m_userRank;
	}

	/**
	 * �����û�������Ŀ����ת������Ȩ��������
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
	 * ��ȡ��ǰ���а����Ŀ��
	 * 
	 * @return ��Ŀ��
	 */
	public int size()
	{
		return m_list.size();
	}

	/**
	 * ��ȡָ��id��������Ŀ
	 * 
	 * @param id
	 *            ��Ŀid
	 * @return ��Ŀ
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
	 * �鿴ָ��id��������Ŀ�Ƿ��ǵ�ǰ�û�����
	 * 
	 * @param id
	 *            ��Ŀid
	 * @return �Ƿ��ǵ�ǰ�û�����
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
	 * �������򣬴Ӵ�С
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
	 * �������ڵ�˳�����¼�¼����ID
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
