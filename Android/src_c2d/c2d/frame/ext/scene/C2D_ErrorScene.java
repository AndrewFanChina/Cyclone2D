package c2d.frame.ext.scene;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Button;
import c2d.lang.app.C2D_App;
import c2d.mod.C2D_Consts;
/**
 * 错误页面，用于程序发生错误时，跳转并显示错误信息的页面。
 * @author AndrewFan
 *
 */
public class C2D_ErrorScene extends C2D_MotionItemScene
{
	C2D_TextBox showBox;
	protected void onAddedToStage()
	{
		super.onAddedToStage();
		setBGColor(0xFF);
		showBox=addTxtBox("", getWidth()/2, getHeight()*2/5, 0, 0xFFFFFF);
		showBox.setLimitWidth(getWidth()-20);
		
		C2D_TextBox tb = addTxtBox("按确定键退出", getWidth()/2, getHeight()-2, 0, 0xFFFF00);
		tb.setAnchor(C2D_Consts.HCENTER|C2D_Consts.BOTTOM);
		tb.Events_Button().add(new C2D_Event_Button()
		{
			protected boolean doEvent(C2D_Widget carrier, int btnState)
			{
				if(btnState==C2D_Consts.Btn_PressBegin)
				{
					C2D_App.ShutDown();
					return true;
				}
				return false;
			}
		});
		tb.setFocusable(true);
		getSceneAt().setFocusedWidget(tb);
	}
	/**
	 * 显示错误信息
	 * @param errorString 错误信息
	 */
	public void showErrorInfor(String errorString)
	{
		if(showBox!=null&&errorString!=null)
		{
			showBox.setText(errorString);
		}
	}
	protected void onRemovedFromStage()
	{
		onRelease();
	}

}
