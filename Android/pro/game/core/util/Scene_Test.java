package game.core.util;

import game.core.ProAssets;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;

public class Scene_Test extends C2D_SceneUtil
{
	protected C2D_TextBox m_quitTxt;
	protected void onAddedToStage()
	{		
		super.onAddedToStage();
		setBGColor(0x666666);
		//�˳���ʾ[z->100]
		m_quitTxt = addTxtBox("�����ء�������", 2, 2, 100,0, 0xFFFFFFFF);
		//�����˳�����
		setBackKey(C2D_Device.key_back);
	}
	protected void onRemovedFromStage()
	{
		super.onRemovedFromStage();
		ProAssets.releaseRes();
	}


}
