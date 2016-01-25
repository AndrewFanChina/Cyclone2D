package pay.model.rank;

import c2d.util.misc.C2D_MiscUtil;

public class C2D_RankList
{
	/** 排行单元列表 */
	public C2D_RankItem m_rankList[];
	/** 用户排行单元 */
	public C2D_RankItem m_userRank;
	/**
	 * 显示详细信息
	 */
	public void logDetail()
	{
		if(m_userRank!=null)
		{
			C2D_MiscUtil.log("[用户排行信息]");
			m_userRank.logDetail();
		}
		if(m_rankList!=null)
		{
			C2D_MiscUtil.log("[排行帮单信息]");
			for (int i = 0; i < m_rankList.length; i++)
			{
				C2D_RankItem item = m_rankList[i];
				if(item!=null)
				{
					item.logDetail();
				}
				else
				{
					C2D_MiscUtil.log("<Warning>","[空条目]");
				}
			}
		}
	}
}
