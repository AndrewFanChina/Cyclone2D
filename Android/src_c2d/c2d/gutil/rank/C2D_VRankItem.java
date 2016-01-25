package c2d.gutil.rank;

import c2d.lang.io.C2D_Serializable;
import c2d.lang.io.C2D_SerializeObject;
import c2d.lang.math.C2D_Order;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.C2D_Misc;
import c2d.lang.util.debug.C2D_Debug;

/**
 * 排行版条目
 * 
 * @author Andrew
 * 
 */
public class C2D_VRankItem extends C2D_Object implements C2D_Order,C2D_SerializeObject
{
	/** 用户ID */
	public String m_userID;
	/** 用户昵称 */
	public String m_nickName;
	/** 位于排行榜的名次，从1开始，如果不存在，将为0 */
	public int m_rankNumber = 0;
	/** 进行排行的分数，如果不存在，将为0 */
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
	 * 设置排行榜位置
	 * 
	 * @param number
	 */
	public void setRankNumber(int number)
	{
		m_rankNumber = number;
	}

	/**
	 * 获取存在于排行榜中的排名ID
	 * 
	 * @return 排名ID
	 */
	public int getRankNumer()
	{
		return m_rankNumber;
	}

	/**
	 * 设置分数
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
	 * 设置用户昵称
	 * 
	 * @param nickName
	 */
	public void setNickName(String nickName)
	{
		m_nickName = nickName;
	}
	/**
	 * 显示详细信息
	 */
	public void logDetail()
	{
		logDetail(null);
	}
	/**
	 * 显示详细信息
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
	 * 获得进行排行的分数
	 * 
	 * @return 分数
	 */
	public int getRankScore()
	{
		return m_score;
	}

	/**
	 * 进行内容的隐藏处理
	 * 
	 * @param maxLen
	 *            最大的保留长度
	 * @param needCtMask
	 *            是否需要中段隐藏
	 */
	public void doMask(int maxLen, boolean needCtMask)
	{
		m_userID = mask(m_userID, maxLen, needCtMask);
		m_nickName = mask(m_nickName, maxLen, needCtMask);
	}

	/**
	 * 执行隐藏处理
	 * 
	 * @param content
	 *            要处理的内容
	 * @param maxLen
	 *            最大长度
	 * @param needMask
	 *            是否需要中段隐藏
	 * @return 处理后的内容
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
	 * 获取用户昵称，如果不存在，则返回用户账号
	 * 
	 * @return 用户名称或者用户账号
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
