package c2d.lang.io;

import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;

public class C2D_HttpMessage extends C2D_Object
{
	/** �������� */
	public int m_requestCode = -1;
	/** ��������ʽ��Ϣ���ݣ��������������� */
	public byte m_binContent[];
	/** �ַ�����ʽ��Ϣ���ݣ��������������� */
	public String m_strContent;
	/** ��Ϣ���ݲ���1-�ɹ���־ */
	public boolean m_success = false;
	/** ��Ϣ���ݲ���2-������� */
	public String m_descrip;
	/** �쳣��Ϣ���������ӷ�������ʱ���ص��쳣��Ϣ */
	public String m_expInf;
	/** Ĭ�ϵı����б� */
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
	 * �������ݣ�������Ϣ���ݣ����������ݷ���
	 * 
	 * @param encodeList
	 *            �����б�
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
	 * �������ݣ�������Ϣ���ݣ����������ݷ�����Ĭ����UTF-8������н���
	 * 
	 * @return
	 */
	public String decodeMsg()
	{
		return decodeMsg(EncodsDefault);
	}

	/**
	 * ���ý������
	 * 
	 * @param descrip
	 *            �������
	 */
	public void setDescrip(String descrip)
	{
		this.m_descrip = descrip;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return �������
	 */
	public String getDescrip()
	{
		return m_descrip;
	}

	/**
	 * ��ȡ�쳣��Ϣ
	 * 
	 * @return
	 */
	public String getExpInf()
	{
		return m_expInf;
	}

	/**
	 * �����쳣��Ϣ
	 * 
	 * @param exp
	 */
	public void setExpInf(String exp)
	{
		this.m_expInf = exp;
	}

	/**
	 * ��ʾ��Ϣ
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
