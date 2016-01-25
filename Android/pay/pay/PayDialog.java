package pay;

import game.core.Def;
import game.core.UserConsts_Tetris_zh;
import c2d.graphics.C2D_Image;
import c2d.structs.C2D_Consts;
import c2d.widget.ui.C2D_Dialog;
import c2d.widget.ui.C2D_SpriteBtn;
import c2d.widget.ui.C2D_Stage;
import c2d.widget.ui.C2D_Widget;
import c2d.widget.ui.event.C2D_Event_Button;

public class PayDialog extends C2D_Dialog implements UserConsts_Tetris_zh
{
	PayLauncher m_launcher;
	public PayDialog(PayLauncher launcher)
	{
		m_launcher =launcher;
	}
	//按钮
	C2D_SpriteBtn m_btns[];
	public void onAddedToStage()
	{
		this.setBGImage("payBg"+Def.Lan+".png");
		this.setSizeOfBGImg();
		setPosTo(C2D_Stage.User_Size.getWidth()/2, C2D_Stage.User_Size.getHeight()/2);
		setAnchor(C2D_Consts.HCENTER|C2D_Consts.VCENTER);
		C2D_Event_Button event=new C2D_Event_Button()
		{
			protected boolean doEvent(C2D_Widget carrier, int btnState)
			{
				if(btnState==C2D_SpriteBtn.Btn_PressBegin)
				{
					if (Def.Audio_Effect)
					{
						Def.playSE("button.ogg");
					}	
				}
				if(btnState==C2D_SpriteBtn.Btn_ReleaseEnd)
				{
					switch(carrier.getID())
					{
					case 0://确定
						m_launcher.pay();
						break;
					case 1://取消
						closeDialog();
						break;
					}
				}
				return false;
			}
		};
		//按钮
		m_btns=new C2D_SpriteBtn[2];
		int x=getWidth()/2;
		//确定
		m_btns[0]=new C2D_SpriteBtn(Def.getC2DManager(), SpriteFolder_pay, Sprite_pay_ok);
		m_btns[0].setPosTo(x, 285);
		m_btns[0].setHotRegion(-90, -30, 180, 60);
		m_btns[0].Events_Button().add(event);
		m_btns[0].setZOrder(0);
		m_btns[0].setID4State(2, 3, 0, 1);
		addChild(m_btns[0]);
		//取消
		m_btns[1]=new C2D_SpriteBtn(Def.getC2DManager(), SpriteFolder_pay, Sprite_pay_cancle);
		m_btns[1].setPosTo(x, 360);
		m_btns[1].setHotRegion(-90, -30, 180, 60);
		m_btns[1].Events_Button().add(event);
		m_btns[1].setZOrder(1);
		m_btns[1].setID4State(2, 3, 0, 1);
		addChild(m_btns[1]);
	
	}

	public void onRemovedFromStage()
	{
		this.setBGImage((C2D_Image)null);
		releaseRes();
		m_btns=null;
	}
}
