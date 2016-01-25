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
 * 脚本管理器
 */
public class C2D_ScriptManager extends C2D_Object
{
	// ---- CSE文件头 -----------------------------------------------------------
	/** 作为执行文件的验证码. */
	static final String CSE_ID_STRING = "CSE10";

	/**  主版本号. */
	static final short VERSION_MAJOR = 1;

	/** 负版本号. */
	static final short VERSION_MINOR = 0;

	/** 全局整形变量. */
	private int[] globleInts;

	/** 全局字符变量. */
	private String[] globleStrs;

	/** 脚本全局API函数信息. */
	byte[] g_HostsLen;

//	/** 脚本文件名称列表. */
//	String[] fileNameList;

	/** 脚本数据列表. */
	private Hashtable scriptDataList;// C2D_ScriptData[] 

	/** 脚本执行者列表. */
	private C2D_Array scriptExcutorList = new C2D_Array();
	// 父框架
	/** C2D管理器. */
	private C2D_FrameManager c2dManager;

	/**
	 * 构造函数.
	 * 
	 * @param c2dManagerT C2D管理器
	 */
	public C2D_ScriptManager(C2D_FrameManager c2dManagerT)
	{
		c2dManager = c2dManagerT;
		initBase();
	}

	// 初始化基础数据
	/**
	 * Inits the base.
	 */
	private void initBase()
	{
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.getResName() + C2D_Consts.STR_SCRIPT;
		DataInputStream dataIn = null;
		try
		{
			// 创建输入流
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			short len = 0;
			// 读取脚本全局变量信息
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
			// 读取脚本全局API函数信息
			len = C2D_IOUtil.readShort(len, dataIn);
			g_HostsLen = new byte[len];
			for (int i = 0; i < g_HostsLen.length; i++)
			{
				g_HostsLen[i] = C2D_IOUtil.readByte(g_HostsLen[i], dataIn);
			}
			// 读取脚本文件名称列表
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
	 * 更新脚本逻辑.
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
	 * 创建脚本执行者.
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
	 * 删除脚本执行者.
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
	 * 使用脚本数据
	 * 
	 * @param fileName 加载的脚本文件名称
	 * @return 脚本文件结构
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
	 * 注销一次对脚本数据的使用
	 * 
	 * @param fileName 脚本文件名
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
	 * 释放脚本数据
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
	 * 获取全局整形数值
	 * 
	 * @param id
	 *            数值ID
	 * @return 整形数值
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
	 * 设置全局整形数值
	 * 
	 * @param id
	 *            数值ID
	 * @param value
	 *            目标数值
	 * @return 是否设置成功
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
	 * 获取全局字符数值
	 * 
	 * @param id
	 *            字符ID
	 * @return 字符数值
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
	 * 设置全局字符数值
	 * 
	 * @param id
	 *            字符ID
	 * @param value
	 *            目标数值
	 * @return 是否设置成功
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
	 * 释放资源.
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
