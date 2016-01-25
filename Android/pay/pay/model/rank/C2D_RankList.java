package pay.model.rank;

import c2d.util.misc.C2D_MiscUtil;

public class C2D_RankList
{
	/** ���е�Ԫ�б� */
	public C2D_RankItem m_rankList[];
	/** �û����е�Ԫ */
	public C2D_RankItem m_userRank;
	/**
	 * ��ʾ��ϸ��Ϣ
	 */
	public void logDetail()
	{
		if(m_userRank!=null)
		{
			C2D_MiscUtil.log("[�û�������Ϣ]");
			m_userRank.logDetail();
		}
		if(m_rankList!=null)
		{
			C2D_MiscUtil.log("[���аﵥ��Ϣ]");
			for (int i = 0; i < m_rankList.length; i++)
			{
				C2D_RankItem item = m_rankList[i];
				if(item!=null)
				{
					item.logDetail();
				}
				else
				{
					C2D_MiscUtil.log("<Warning>","[����Ŀ]");
				}
			}
		}
	}
}
