package pay.model.rank;

import c2d.util.math.C2D_Math;
import c2d.util.misc.C2D_MiscUtil;
/**
 * ���а���Ŀ
 * ע�⣺SQ����Ŀǰû������userName����������userID�����Ҳ��������ֵ��һ���ġ�
 * @author Andrew
 *
 */
public class C2D_RankItem
{
	/** �û�ID */
	public String m_userID;
	/** �û��� */
	public String m_userName;
	/** λ�����а�����Σ���������ڣ���Ϊ"-1" */
	public String m_rankNumber="-1";
	/** �������еķ�������������ڣ���Ϊ"0" */
	public String m_score="0";
	/**
	 * �������а�λ��
	 * @param number
	 */
	public void setRankNumber(String number)
	{
		m_rankNumber = ""+C2D_Math.stringToInt(number);
	}
	/**
	 * ��ȡ���������а��е�����ID
	 * @return ����ID
	 */
	public int getRankNumer()
	{
		int id=C2D_Math.stringToInt(m_rankNumber);
		return id;
	}
	/**
	 * ���÷���
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
	 * ��ʾ��ϸ��Ϣ
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
	 * ������ε�������ֵ
	 * @return ����
	 */
	public int getRankNum()
	{
		return C2D_Math.stringToInt(m_rankNumber);
	}
}
