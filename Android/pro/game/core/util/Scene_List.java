package game.core.util;

import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_FloatInforView;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_ListScene;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;

public abstract class Scene_List extends C2D_ListScene implements C2D_Consts
{
	protected C2D_TextBox m_quitTxt;
	//浮动信息显示
	protected C2D_FloatInforView m_floatView;
	public Scene_List(String items[])
	{
		super(items);
	}

	protected void onAddedToStage()
	{
		super.onAddedToStage();
		this.setBGColor(0x230102);
		//两个用于装饰的矩形视图[z->1]
		addInnerView(10, 10, 1, 0xCD4300);
		C2D_ViewUtil v2 = addInnerView(12, 12, 2, 0x230102);
		//初始化列表[z->10],[z->11..]
		initList(10);
		//退出提示[v2:z->10]
		m_quitTxt = v2.addTxtBox("【返回】键退出", 0, v2.getHeight(), 10,LEFT|BOTTOM, 0xFFFFFFFF);
		//设置退出按键
		setBackKey(C2D_Device.key_back);
		//浮动框[z->2000]
		m_floatView = new C2D_FloatInforView(0xFFFFFF);
		addChild(m_floatView, 2000);
		m_floatView.initFloatView();
	}
}
