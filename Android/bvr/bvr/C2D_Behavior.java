package bvr;

import bvr.path.C2D_BvrNode;
import bvr.path.C2D_BvrPath;
import bvr.path.C2D_DictionDefiner;
import bvr.path.C2D_Dictionary;
import bvr.util.C2D_AppParamRequster;
import c2d.lang.io.C2D_HttpPackage;
import c2d.lang.io.C2D_HttpPkgReceiver;

public class C2D_Behavior
{
	/** 系统参数读取者 */
	C2D_AppParamRequster m_appParamRequster;
	/** 行为路径字典 */
	C2D_Dictionary m_pathDiction;
	/** 行为路径图 */
	C2D_BvrPath m_pathGraph;
	/** 错误消息 */
	private String m_errMsg = null;

	public C2D_Behavior(C2D_AppParamRequster paramRequester, C2D_DictionDefiner pathDefiner)
	{
		m_appParamRequster = paramRequester;
		m_pathDiction = new C2D_Dictionary(pathDefiner);
		m_pathGraph = new C2D_BvrPath(m_pathDiction);
	}

	/**
	 * 记录UI转移路径
	 * 
	 * @param bvrNode
	 *            ui节点
	 */
	public void logPath(C2D_BvrNode bvrNode)
	{
		if (m_pathGraph != null)
		{
			m_pathGraph.logPath(bvrNode);
		}
	}

	/**
	 * 尝试将最新的路径字典保存到服务器
	 * 参数格式[?Action=LogDic&GameID=123465&DicVer=v1.0.0&Diction=dictionary]
	 * 
	 * @return 保存操作的结果
	 */
	public void saveDicToServer(C2D_HttpPkgReceiver receiver)
	{
		m_errMsg = null;
		String BvrUrl = null;
		String GameID = null;
		String DicVer = null;
		String Diction = null;
		do
		{
			if (m_pathDiction == null)
			{
				m_errMsg = "error:字典对象不存在";
				break;
			}
			if (m_appParamRequster == null)
			{
				m_errMsg = "error:未设置参数请求者";
				break;
			}
			BvrUrl = checkParam("sns_bvr");
			if (m_errMsg != null)
			{
				break;
			}
			GameID = checkParam("sns_game_id");
			if (m_errMsg != null)
			{
				break;
			}
			DicVer = m_pathDiction.m_version;
			if (DicVer == null)
			{
				m_errMsg = "error:DicVer参数非法";
				break;
			}
			Diction = m_pathDiction.serilizedOut();
			if (Diction == null)
			{
				m_errMsg = "error:字典生成错误";
				break;
			}
		}
		while (false);
		if (m_errMsg != null)
		{
			receiver.receiveData(null, m_errMsg);
			return;
		}
		String BvrParams = "Action=LogDic&GameID=" + GameID + "&DicVer=" + DicVer + "&Diction=" + Diction;
		C2D_HttpPackage packager = new C2D_HttpPackage();
		packager.requestMsgAsynPost(BvrUrl, BvrParams, receiver);
	}

	/**
	 * 尝试将当前的路径保存到服务器 参数格式 <br>
	 * [?Action=LogPath&GameID=123465&DicVer=v1.0.0 <br>
	 * &UserID=user000&Path=path&AreaCode=jiangsu]
	 * 
	 * @return 保存操作的结果
	 */
	public void savePathToServer(C2D_HttpPkgReceiver receiver)
	{
		m_errMsg = null;
		String BvrUrl = null;
		String GameID = null;
		String DicVer = null;
		String UserID = null;
		String AreaCode = null;
		String Path = null;
		do
		{
			if (m_pathDiction == null)
			{
				m_errMsg = "error:字典对象不存在";
				break;
			}
			if (m_pathGraph == null)
			{
				m_errMsg = "error:路径图对象不存在";
				break;
			}
			if (m_appParamRequster == null)
			{
				m_errMsg = "error:未设置参数请求者";
				break;
			}
			BvrUrl = checkParam("sns_bvr");
			if (m_errMsg != null)
			{
				break;
			}
			GameID = checkParam("sns_game_id");
			if (m_errMsg != null)
			{
				break;
			}
			DicVer = m_pathDiction.m_version;
			if (DicVer == null)
			{
				m_errMsg = "error:DicVer参数非法";
				break;
			}
			UserID = checkParam("sns_user_id");
			if (m_errMsg != null)
			{
				break;
			}
			AreaCode = checkParam("sns_area_code");
			if (m_errMsg != null)
			{
				break;
			}
			Path = m_pathGraph.serilizedOut();
			if (Path == null)
			{
				m_errMsg = "error:路径图生成错误";
				break;
			}
		}
		while (false);
		if (m_errMsg != null)
		{
			receiver.receiveData(null, m_errMsg);
			return;
		}
		String BvrParams = "Action=LogPath&GameID=" + GameID + "&DicVer=" + DicVer + "&UserID=" + UserID + "&Path=" + Path + "&AreaCode=" + AreaCode;
		C2D_HttpPackage packager = new C2D_HttpPackage();
		packager.requestMsgAsynPost(BvrUrl, BvrParams, receiver);
	}

	/**
	 * 读取系统参数
	 * 
	 * @param paramName
	 *            参数名称
	 * @return 参数值
	 */
	private String checkParam(String paramName)
	{
		String result = null;
		if (m_appParamRequster != null)
		{
			result = m_appParamRequster.requestAppParam(paramName);
		}
		if (result == null)
		{
			m_errMsg = "error:" + paramName + "参数非法";
		}
		return result;
	}
}
