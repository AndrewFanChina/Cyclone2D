package c2d.frame.ext.scene;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.math.type.C2D_SizeI;
import c2d.mod.C2D_Consts;

/**
 * 带两个按钮（确定和返回），一个提示文本框风格的提醒场景。 1、由于对话框不全屏，屏幕底层是纯色。
 * 2、对话框的背景是一个九片图。由m_bgView控制,它的大小是由用户设置。 3、对话框的内容存放在m_totalView中，它的大小和背景相同。
 * 4、对话框内容中存放了两个按钮，分别是确定和返回，并且已经预置了事件调用。 5、对话框内容中含有一个文本框，用于显示说明文字。
 * 6、这个方法中的按钮事件需要子类实现。
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_AlertScene extends C2D_SceneUtil
{
	/** 背景视图 */
	public C2D_View w_bgView;
	/** 总体视图 */
	public C2D_ViewUtil w_totalView;
	/** 说明文字控件 */
	public C2D_TextBox w_introTxt;
	/** 确定按钮 */
	public C2D_View w_btnOk;
	/** 返回按钮 */
	public C2D_View w_btnBack;
	//--------------------配置信息---------------------------
	/** 默认界面大小 */
	protected C2D_SizeI m_viewSize = new C2D_SizeI(320, 240);
	/** 默认界面大小 */
	protected C2D_SizeI m_textSize = new C2D_SizeI(300, 160);

	public C2D_AlertScene(C2D_SizeI viewSize, C2D_SizeI textSize)
	{
		m_viewSize.setValue(viewSize);
		m_textSize.setValue(textSize);
	}

	// 按钮按下事件
	protected C2D_Event_Button btn_event = new C2D_Event_Button()
	{
		protected boolean doEvent(C2D_Widget carrier, int btnState)
		{
			if (carrier == null)
			{
				return false;
			}
			String strName = carrier.getName();
			if (strName != null)
			{
				if (strName.equals("ok"))
				{
					onBtnOkPressed();
				}
				else if (strName.equals("back"))// 返回前一画面
				{
					onBtnBackPressed();
				}
			}
			return false;
		}
	};
	protected C2D_Event_Update m_backKeyEvent = new C2D_Event_Update()
	{
		protected boolean doEvent(C2D_Widget carrier)
		{
			//返回键响应
			if (getStageAt().isKeyBackOrRSoft())
			{
				getStageAt().releaseKeys();
				onKeyBackPressed();
				return true;
			}
			return false;
		}
	};

	protected void onAddedToStage()
	{
		super.onAddedToStage();
		if (!isEmpty())
		{
			return;
		}
		//背景颜色
		setBGColor(0);
		//背景容器【z:0】
		w_bgView = onCreateBgView(m_viewSize.m_width, m_viewSize.m_height);
		//整体视图【z:10】
		w_totalView = new C2D_ViewUtil();
		w_totalView.setSize(w_bgView.getWidth(), w_bgView.getHeight());
		addChild(w_totalView, 10);
		w_totalView.setToParentCenter();
		// 在整体视图添加文字说明【tz:10】
		w_introTxt = new C2D_TextBox();
		w_introTxt.setLimitWidth(m_textSize.m_width);
		w_totalView.addChild(w_introTxt, 10);
		w_introTxt.setPosPer(50, 40);
		w_introTxt.setAnchor(C2D_Consts.HVCENTER);
		w_introTxt.setColor(0xaf360a);
		// 在整体视图添加确定按钮【tz:20】
		w_btnOk = onCreateBackButton();
		w_totalView.addChild(w_btnOk, 20);
		w_btnOk.setPosPer(70, 80);
		w_btnOk.setName("ok");
		// 在整体视图添加返回按钮【tz:30】
		w_btnBack = onCreateOkButton();
		w_totalView.addChild(w_btnBack, 30);
		w_btnBack.setPosPer(30, 80);
		w_btnBack.setName("back");
		// 键盘事件
		Events_Update().add(m_backKeyEvent);
		// 默认设置焦点为确定
		getSceneAt().setFocusedWidget(w_btnOk);
		// 自定义配置
		doSelfConfig();
	}

	protected abstract void doSelfConfig();

	protected abstract C2D_View onCreateOkButton();
	
	protected abstract C2D_View onCreateBackButton();

	protected abstract C2D_View onCreateBgView(int width, int height);

	protected abstract void onBtnBackPressed();

	protected abstract void onBtnOkPressed();
	
	protected void onKeyBackPressed()
	{
		close();
	}

	public void onRelease()
	{
		super.onRelease();
		w_bgView = null;
		w_totalView = null;
		w_introTxt = null;
		w_btnOk = null;
		w_btnBack = null;
	}
}
