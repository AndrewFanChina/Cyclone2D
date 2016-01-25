package game.tutorial.c2d;

import game.core.UserConsts_example;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Keys;
import c2d.mod.C2D_FrameManager;
import c2d.mod.script.C2D_ScriptExcutor;
import c2d.mod.script.C2D_ScriptFunctionHandler;
import c2d.mod.script.C2D_Thread;
import c2d.mod.script.C2D_VM;
import c2d.mod.sprite.C2D_Sprite;

/**
 * 精灵
 * 
 * @author AndrewFan
 * 
 */
public class L04_01_Sprites extends C2D_SceneUtil implements UserConsts_example, C2D_Keys, C2D_ScriptFunctionHandler
{
	C2D_FrameManager fm = null;
	C2D_ScriptExcutor excuter = null;

	public String getBvrNodeName()
	{
		return "L04_01_Sprites";
	}

	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		setBGColor(0xFF);
		if (fm == null)
		{
			fm = new C2D_FrameManager(C2D_NAME_example, true);
		}
		C2D_Sprite sp = new C2D_Sprite(fm, SpriteFolder_bullets, Sprite_bullets_bullet1);
		addChild(sp, 1);
		sp.setAutoPlay(C2D_Sprite.AUTOPLAY_FRAME);
		sp.setToParentCenter();

		// 脚本的执行
		excuter = fm.m_SptM.createScriptExcutor();
		excuter.setFunctionHandler(this);
		C2D_Thread thread1 = excuter.loadThread("lv1");
		C2D_VM.C2DS_RunScript(thread1);
		//设置返回键退出
		setBackKey(key_back);
	}

	public boolean handleFunction(C2D_Thread currentThread, int functionID)
	{
		String s;
		int p1, p2, p3;
//		switch (functionID)
//		{
//		case SCRIPT_FUN_PrintString:
//			s = currentThread.getParamAsString(0);
//			System.out.println(s);
//			break;
//		case SCRIPT_FUN_CreateSprite:
//			p1 = currentThread.getParamAsInt(2);
//			p2 = currentThread.getParamAsInt(1);
//			p3 = currentThread.getParamAsInt(0);
//			C2D_Sprite sp = new C2D_Sprite(fm, (short) p1);
//			addChild(sp, 20);
//			sp.setPosTo(p2, p3);
//			break;
//		}
		return false;
	}
}
