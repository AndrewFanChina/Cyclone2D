package game.app;

import bvr.C2D_Behavior;
import bvr.path.C2D_BvrNode;
import bvr.path.C2D_DictionDefiner;
import bvr.util.C2D_AppParamRequster;
import c2d.frame.base.C2D_Stage;
import c2d.frame.util.C2D_SceneTracker;
import c2d.lang.app.C2D_App;
import c2d.lang.io.C2D_HttpPackage;
import c2d.lang.io.C2D_HttpPkgReceiver;
import c2d.lang.io.C2D_ServerLogger;
import c2d.lang.math.C2D_Array;
import c2d.lang.util.C2D_System;
import c2d.lang.util.debug.C2D_Debug;

public class MainStage extends C2D_Stage implements C2D_AppParamRequster, C2D_DictionDefiner
{
	public C2D_Behavior m_behavior;

	public MainStage()
	{
	}

	public Scene_Entry m_entryScene;

	protected void onEnter_C2D()
	{
//		//#if LogServer
//		String logurl = C2D_App.getApp().getAppProperty("sns_logurl");
//		C2D_ServerLogger.connect(logurl, "SNS_Test,MEM:" + Runtime.getRuntime().freeMemory());
//		//#endif
		C2D_SceneTracker.Enable();
		m_entryScene = new Scene_Entry();
		if (m_behavior == null)
		{
			m_behavior = new C2D_Behavior(this, this);
			m_behavior.saveDicToServer(new C2D_HttpPkgReceiver()
			{
				public void receiveData(byte[] pkgData, String err)
				{
					String res =  C2D_HttpPackage.DealResult(pkgData,err);
					C2D_Debug.logDebug("±£´æ×Öµä½á¹û:"+res);
				}
			});
		}
		C2D_SceneTracker.Disable();
		pushScene(m_entryScene);
	}

	protected void onClose_C2D()
	{
		m_entryScene = null;
	}


	public C2D_BvrNode[] getDicNodes()
	{
		C2D_BvrNode[] nodes = null;
		C2D_Array array = C2D_SceneTracker.GetTrackedScenes();
		if (array != null)
		{
			int size = array.size();
			nodes = new C2D_BvrNode[size];
			for (int i = 0; i < size; i++)
			{
				C2D_BvrNode nodeI = (C2D_BvrNode) array.elementAt(i);
				nodes[i] = nodeI;
			}
		}
		return nodes;
	}

	public String getDicVersion()
	{
		return "v1.0.0";
	}

	@Override
	public String requestAppParam(String paramName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onChangeView_C2D(int w, int h)
	{
	}
}
