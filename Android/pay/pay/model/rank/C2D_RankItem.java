package pay.model.rank;

import c2d.util.math.C2D_Math;
import c2d.util.misc.C2D_MiscUtil;
/**
 * 排行版条目
 * 注意：SQ渠道目前没有设置userName，而是用了userID替代，也即这两个值是一样的。
 * @author Andrew
 *
 */
public class C2D_RankItem
{
	/** 用户ID */
	public String m_userID;
	/** 用户名 */
	public String m_userName;
	/** 位于排行榜的名次，如果不存在，将为"-1" */
	public String m_rankNumber="-1";
	/** 进行排行的分数，如果不存在，将为"0" */
	public String m_score="0";
	/**
	 * 设置排行榜位置
	 * @param number
	 */
	public void setRankNumber(String number)
	{
		m_rankNumber = ""+C2D_Math.stringToInt(number);
	}
	/**
	 * 获取存在于排行榜中的排名ID
	 * @return 排名ID
	 */
	public int getRankNumer()
	{
		int id=C2D_Math.stringToInt(m_rankNumber);
		return id;
	}
	/**
	 * 设置分数
	 * @param score
	 */
	public void setScore(String score)
	{
		int scoreI = C2D_Math.stringToInt(score);
		if(scoreI<0)
		{
			scoreI=0;
		}
		m_score = ""+scoreI;
	}
	/**
	 * 显示详细信息
	 */
	public void logDetail()
	{
		String infor="id:"+m_userID;
		if(m_userID!=null && m_userID.length()<14)
		{
			infor+="\t";
		}
		infor+="\tname:"+m_userName;
		if(m_userName!=null && m_userName.length()<12)
		{
			infor+="\t";
		}
		infor+="\tnumber:"+m_rankNumber+"\tscore:"+m_score;
		C2D_MiscUtil.log(infor);
	}
	/**
	 * 获得整形的名次数值
	 * @return 名次
	 */
	public int getRankNum()
	{
		return C2D_Math.stringToInt(m_rankNumber);
	}
}
