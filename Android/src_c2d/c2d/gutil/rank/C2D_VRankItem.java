package c2d.gutil.rank;

import c2d.lang.io.C2D_Serializable;
import c2d.lang.io.C2D_SerializeObject;
import c2d.lang.math.C2D_Order;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.C2D_Misc;
import c2d.lang.util.debug.C2D_Debug;

/**
 * ���а���Ŀ
 * 
 * @author Andrew
 * 
 */
public class C2D_VRankItem extends C2D_Object implements C2D_Order,C2D_SerializeObject
{
	/** �û�ID */
	public String m_userID;
	/** �û��ǳ� */
	public String m_nickName;
	/** λ�����а�����Σ���1��ʼ����������ڣ���Ϊ0 */
	public int m_rankNumber = 0;
	/** �������еķ�������������ڣ���Ϊ0 */
	public int m_score = 0;
	public C2D_VRankItem()
	{
		
	}
	public C2D_VRankItem(String userID,String userName,int score)
	{
		m_userID=userID;
		m_nickName=userName;
		setScore(score);
	}

	/**
	 * �������а�λ��
	 * 
	 * @param number
	 */
	public void setRankNumber(int number)
	{
		m_rankNumber = number;
	}

	/**
	 * ��ȡ���������а��е�����ID
	 * 
	 * @return ����ID
	 */
	public int getRankNumer()
	{
		return m_rankNumber;
	}

	/**
	 * ���÷���
	 * 
	 * @param score
	 */
	public void setScore(int score)
	{
		int scoreI = score;
		if (scoreI < 0)
		{
			scoreI = 0;
		}
		m_score = scoreI;
	}
	/**
	 * �����û��ǳ�
	 * 
	 * @param nickName
	 */
	public void setNickName(String nickName)
	{
		m_nickName = nickName;
	}
	/**
	 * ��ʾ��ϸ��Ϣ
	 */
	public void logDetail()
	{
		logDetail(null);
	}
	/**
	 * ��ʾ��ϸ��Ϣ
	 */
	public void logDetail(String infor)
	{
		if(infor==null)
		{
			infor="";
		}
		infor += "\tuserID:" + m_userID;
		infor += "\tuserName:" + m_nickName;
		infor += "\trankNumber:" + m_rankNumber ;
		infor += "\tscore:" + m_score;
		C2D_Debug.log(infor);
	}

	/**
	 * ��ý������еķ���
	 * 
	 * @return ����
	 */
	public int getRankScore()
	{
		return m_score;
	}

	/**
	 * �������ݵ����ش���
	 * 
	 * @param maxLen
	 *            ���ı�������
	 * @param needCtMask
	 *            �Ƿ���Ҫ�ж�����
	 */
	public void doMask(int maxLen, boolean needCtMask)
	{
		m_userID = mask(m_userID, maxLen, needCtMask);
		m_nickName = mask(m_nickName, maxLen, needCtMask);
	}

	/**
	 * ִ�����ش���
	 * 
	 * @param content
	 *            Ҫ���������
	 * @param maxLen
	 *            ��󳤶�
	 * @param needMask
	 *            �Ƿ���Ҫ�ж�����
	 * @return ����������
	 */
	private String mask(String content, int maxLen, boolean needMask)
	{
		if (content == null)
		{
			return content;
		}
		int len = content.length();
		if (len > maxLen)
		{
			needMask = true;
			int cut = len - maxLen;
			int begin = (len - cut) >> 1;
			int end = len - begin;
			content = content.substring(0, begin) + content.substring(end, len);
		}
		if (needMask)
		{
			content = C2D_Misc.confuseString(content, '*');
		}
		return content;
	}

	/**
	 * ��ȡ�û��ǳƣ���������ڣ��򷵻��û��˺�
	 * 
	 * @return �û����ƻ����û��˺�
	 */
	public String getNickName()
	{
		if (m_nickName != null)
		{
			return m_nickName;
		}
		return m_userID;
	}

	public int getOrderValue(int orderType)
	{
		return m_score;
	}

	public void onRelease()
	{
		m_nickName = null;
	}

	public void readObject(C2D_Serializable record)
	{
		if (record == null)
		{
			return;
		}
		m_userID = record.readString();
		m_nickName = record.readString();
		m_rankNumber = record.readInt();
		m_score = record.readInt();
	}

	public void writeObject(C2D_Serializable record)
	{
		if (record == null)
		{
			return;
		}
		record.writeString(m_userID);
		record.writeString(m_nickName);
		record.writeInt(m_rankNumber);
		record.writeInt(m_score);
	}

}
