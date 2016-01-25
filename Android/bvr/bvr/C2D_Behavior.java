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
	/** ϵͳ������ȡ�� */
	C2D_AppParamRequster m_appParamRequster;
	/** ��Ϊ·���ֵ� */
	C2D_Dictionary m_pathDiction;
	/** ��Ϊ·��ͼ */
	C2D_BvrPath m_pathGraph;
	/** ������Ϣ */
	private String m_errMsg = null;

	public C2D_Behavior(C2D_AppParamRequster paramRequester, C2D_DictionDefiner pathDefiner)
	{
		m_appParamRequster = paramRequester;
		m_pathDiction = new C2D_Dictionary(pathDefiner);
		m_pathGraph = new C2D_BvrPath(m_pathDiction);
	}

	/**
	 * ��¼UIת��·��
	 * 
	 * @param bvrNode
	 *            ui�ڵ�
	 */
	public void logPath(C2D_BvrNode bvrNode)
	{
		if (m_pathGraph != null)
		{
			m_pathGraph.logPath(bvrNode);
		}
	}

	/**
	 * ���Խ����µ�·���ֵ䱣�浽������
	 * ������ʽ[?Action=LogDic&GameID=123465&DicVer=v1.0.0&Diction=dictionary]
	 * 
	 * @return ��������Ľ��
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
				m_errMsg = "error:�ֵ���󲻴���";
				break;
			}
			if (m_appParamRequster == null)
			{
				m_errMsg = "error:δ���ò���������";
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
				m_errMsg = "error:DicVer�����Ƿ�";
				break;
			}
			Diction = m_pathDiction.serilizedOut();
			if (Diction == null)
			{
				m_errMsg = "error:�ֵ����ɴ���";
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
	 * ���Խ���ǰ��·�����浽������ ������ʽ <br>
	 * [?Action=LogPath&GameID=123465&DicVer=v1.0.0 <br>
	 * &UserID=user000&Path=path&AreaCode=jiangsu]
	 * 
	 * @return ��������Ľ��
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
				m_errMsg = "error:�ֵ���󲻴���";
				break;
			}
			if (m_pathGraph == null)
			{
				m_errMsg = "error:·��ͼ���󲻴���";
				break;
			}
			if (m_appParamRequster == null)
			{
				m_errMsg = "error:δ���ò���������";
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
				m_errMsg = "error:DicVer�����Ƿ�";
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
				m_errMsg = "error:·��ͼ���ɴ���";
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
	 * ��ȡϵͳ����
	 * 
	 * @param paramName
	 *            ��������
	 * @return ����ֵ
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
			m_errMsg = "error:" + paramName + "�����Ƿ�";
		}
		return result;
	}
}
