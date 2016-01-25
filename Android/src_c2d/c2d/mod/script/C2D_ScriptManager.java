package c2d.mod.script;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;

/**
 * �ű�������
 */
public class C2D_ScriptManager extends C2D_Object
{
	// ---- CSE�ļ�ͷ -----------------------------------------------------------
	/** ��Ϊִ���ļ�����֤��. */
	static final String CSE_ID_STRING = "CSE10";

	/**  ���汾��. */
	static final short VERSION_MAJOR = 1;

	/** ���汾��. */
	static final short VERSION_MINOR = 0;

	/** ȫ�����α���. */
	private int[] globleInts;

	/** ȫ���ַ�����. */
	private String[] globleStrs;

	/** �ű�ȫ��API������Ϣ. */
	byte[] g_HostsLen;

//	/** �ű��ļ������б�. */
//	String[] fileNameList;

	/** �ű������б�. */
	private Hashtable scriptDataList;// C2D_ScriptData[] 

	/** �ű�ִ�����б�. */
	private C2D_Array scriptExcutorList = new C2D_Array();
	// �����
	/** C2D������. */
	private C2D_FrameManager c2dManager;

	/**
	 * ���캯��.
	 * 
	 * @param c2dManagerT C2D������
	 */
	public C2D_ScriptManager(C2D_FrameManager c2dManagerT)
	{
		c2dManager = c2dManagerT;
		initBase();
	}

	// ��ʼ����������
	/**
	 * Inits the base.
	 */
	private void initBase()
	{
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.getResName() + C2D_Consts.STR_SCRIPT;
		DataInputStream dataIn = null;
		try
		{
			// ����������
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			short len = 0;
			// ��ȡ�ű�ȫ�ֱ�����Ϣ
			len = C2D_IOUtil.readShort(len, dataIn);
			globleInts = new int[len];
			for (int i = 0; i < globleInts.length; i++)
			{
				globleInts[i] = C2D_IOUtil.readInt(globleInts[i], dataIn);
			}
			len = C2D_IOUtil.readShort(len, dataIn);
			globleStrs = new String[len];
			for (int i = 0; i < globleStrs.length; i++)
			{
				globleStrs[i] = C2D_IOUtil.readString(globleStrs[i], dataIn);
			}
			// ��ȡ�ű�ȫ��API������Ϣ
			len = C2D_IOUtil.readShort(len, dataIn);
			g_HostsLen = new byte[len];
			for (int i = 0; i < g_HostsLen.length; i++)
			{
				g_HostsLen[i] = C2D_IOUtil.readByte(g_HostsLen[i], dataIn);
			}
			// ��ȡ�ű��ļ������б�
//			len = C2D_IOUtil.readShort(len, dataIn);
//			fileNameList = new String[len];
//			for (int i = 0; i < len; i++)
//			{
//				fileNameList[i] = C2D_IOUtil.readString(fileNameList[i], dataIn);
//			}
			scriptDataList = new Hashtable();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException ex)
				{
				}
				dataIn = null;
			}
		}
	}

	/**
	 * ���½ű��߼�.
	 */
	public void updateScripts()
	{
		for (int i = 0; i < scriptExcutorList.size(); i++)
		{
			C2D_ScriptExcutor excutor = (C2D_ScriptExcutor) scriptExcutorList.elementAt(i);
			excutor.updateThread();
		}
	}

	/**
	 * �����ű�ִ����.
	 * 
	 * @return ScriptExcutor
	 */
	public C2D_ScriptExcutor createScriptExcutor()
	{
		C2D_ScriptExcutor excutor = new C2D_ScriptExcutor(this);
		scriptExcutorList.addElement(excutor);
		return excutor;
	}

	/**
	 * ɾ���ű�ִ����.
	 * 
	 * @param excutor
	 *            ScriptExcutor
	 * @return boolean
	 */
	public boolean deleteScriptExcutor(C2D_ScriptExcutor excutor)
	{
		if (excutor == null || !scriptExcutorList.contains(excutor))
		{
			return false;
		}
		excutor.doRelease();
		scriptExcutorList.remove(excutor);
		return true;
	}

	/**
	 * ʹ�ýű�����
	 * 
	 * @param fileName ���صĽű��ļ�����
	 * @return �ű��ļ��ṹ
	 */
	public C2D_ScriptData useScriptData(String fileName)
	{
		if (fileName==null)
		{
			return null;
		}
		C2D_ScriptData scriptData=(C2D_ScriptData)scriptDataList.get(fileName);
		if (scriptData == null)
		{
			scriptData = new C2D_ScriptData(fileName);
			scriptData.loadRes();
			scriptDataList.put(fileName, scriptData);
		}
		scriptData.usedTime++;
		return scriptData;
	}

	/**
	 * ע��һ�ζԽű����ݵ�ʹ��
	 * 
	 * @param fileName �ű��ļ���
	 */
	public void unuseScriptData(String fileName)
	{
		if (fileName==null)
		{
			return;
		}
		C2D_ScriptData scriptData=(C2D_ScriptData)scriptDataList.get(fileName);
		if (scriptData==null)
		{
			return;
		}
		scriptData.usedTime--;
		if (scriptData.usedTime <= 0)
		{
			scriptData.doRelease();
			scriptDataList.remove(fileName);
		}
	}

	/**
	 * �ͷŽű�����
	 */
	public void releaseScripts()
	{
		if (scriptExcutorList != null)
		{
			for (int i = 0; i < scriptExcutorList.size(); i++)
			{
				C2D_ScriptExcutor excutor = (C2D_ScriptExcutor) scriptExcutorList.elementAt(i);
				excutor.doRelease();
			}
			scriptExcutorList.removeAllElements();
		}
	}

	/**
	 * ��ȡȫ��������ֵ
	 * 
	 * @param id
	 *            ��ֵID
	 * @return ������ֵ
	 */
	public int getGlobleInt(int id)
	{
		if (globleInts == null || id < 0 || id >= globleInts.length)
		{
			return -1;
		}
		return globleInts[id];
	}

	/**
	 * ����ȫ��������ֵ
	 * 
	 * @param id
	 *            ��ֵID
	 * @param value
	 *            Ŀ����ֵ
	 * @return �Ƿ����óɹ�
	 */
	public boolean setGlobleInt(int id, int value)
	{
		if (globleInts == null || id < 0 || id >= globleInts.length)
		{
			return false;
		}
		globleInts[id] = value;
		return true;
	}

	/**
	 * ��ȡȫ���ַ���ֵ
	 * 
	 * @param id
	 *            �ַ�ID
	 * @return �ַ���ֵ
	 */
	public String getGlobleStr(int id)
	{
		if (globleStrs == null || id < 0 || id >= globleStrs.length)
		{
			return null;
		}
		return globleStrs[id];
	}

	/**
	 * ����ȫ���ַ���ֵ
	 * 
	 * @param id
	 *            �ַ�ID
	 * @param value
	 *            Ŀ����ֵ
	 * @return �Ƿ����óɹ�
	 */
	public boolean setGlobleStr(int id, String value)
	{
		if (globleStrs == null || id < 0 || id >= globleStrs.length)
		{
			return false;
		}
		globleStrs[id] = value;
		return true;
	}

	/**
	 * �ͷ���Դ.
	 */
	public void onRelease()
	{
		releaseScripts();
		if (scriptDataList != null)
		{
			Enumeration enumer= scriptDataList.elements();
			while(enumer.hasMoreElements())
			{
				C2D_ScriptData scriptData=(C2D_ScriptData)enumer.nextElement();
				if(scriptData!=null)
				{
					scriptData.doRelease();
				}
			}
			scriptDataList.clear();
			scriptDataList = null;
		}
	}


}
