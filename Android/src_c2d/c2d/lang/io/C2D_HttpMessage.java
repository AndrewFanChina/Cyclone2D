package c2d.lang.io;

import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;

public class C2D_HttpMessage extends C2D_Object
{
	/** 请求类型 */
	public int m_requestCode = -1;
	/** 二进制形式消息内容，代表了所有内容 */
	public byte m_binContent[];
	/** 字符串形式消息内容，代表了所有内容 */
	public String m_strContent;
	/** 消息内容部分1-成功标志 */
	public boolean m_success = false;
	/** 消息内容部分2-结果描述 */
	public String m_descrip;
	/** 异常信息，网络连接发生错误时返回的异常信息 */
	public String m_expInf;
	/** 默认的编码列表 */
	protected static String[] EncodsDefault = new String[]
	{ "utf-8", "GBK" };

	public C2D_HttpMessage(int requestCode, byte binData[])
	{
		m_requestCode = requestCode;
		m_binContent = binData;
	}

	public C2D_HttpMessage(int requestCode, String content)
	{
		m_requestCode = requestCode;
		m_strContent = content;
	}

	public C2D_HttpMessage(int requestCode, byte binData[], String exp)
	{
		m_requestCode = requestCode;
		m_binContent = binData;
		m_expInf = exp;
	}

	protected C2D_HttpMessage(C2D_HttpMessage anathor)
	{
		if (anathor != null)
		{
			m_requestCode = anathor.m_requestCode;
			m_strContent = anathor.m_strContent;
			m_success = anathor.m_success;
			m_binContent = anathor.m_binContent;
		}
	}

	protected C2D_HttpMessage()
	{

	}

	/**
	 * 解码数据，返回消息内容，不进行内容分析
	 * 
	 * @param encodeList
	 *            编码列表
	 * @return
	 */
	public String decodeMsg(String encodeList[])
	{
		if (m_binContent != null)
		{
			m_strContent = C2D_Encoder.decodeString(m_binContent, encodeList);
			if (m_strContent != null)
			{
				C2D_Debug.log("[Infor] Got message:" + m_strContent);
			}
		}
		return m_strContent;
	}

	/**
	 * 解码数据，返回消息内容，不进行内容分析，默认以UTF-8编码进行解码
	 * 
	 * @return
	 */
	public String decodeMsg()
	{
		return decodeMsg(EncodsDefault);
	}

	/**
	 * 设置结果描述
	 * 
	 * @param descrip
	 *            结果描述
	 */
	public void setDescrip(String descrip)
	{
		this.m_descrip = descrip;
	}

	/**
	 * 获取结果描述
	 * 
	 * @return 结果描述
	 */
	public String getDescrip()
	{
		return m_descrip;
	}

	/**
	 * 获取异常信息
	 * 
	 * @return
	 */
	public String getExpInf()
	{
		return m_expInf;
	}

	/**
	 * 设置异常信息
	 * 
	 * @param exp
	 */
	public void setExpInf(String exp)
	{
		this.m_expInf = exp;
	}

	/**
	 * 显示消息
	 */
	public void logout()
	{
		if (m_strContent != null)
		{
			C2D_Debug.logC2D("[GotMsg] request=" + m_requestCode + "," + "content:" + m_strContent);
		}
		else
		{
			C2D_Debug.logC2D("[GotMsg] request=" + m_requestCode + "," + "bin data len:"
					+ (m_binContent != null ? m_binContent.length : 0));
		}
	}

	public void onRelease()
	{
		m_binContent = null;
		m_strContent = null;
		m_descrip = null;
		m_expInf = null;
	}
}
