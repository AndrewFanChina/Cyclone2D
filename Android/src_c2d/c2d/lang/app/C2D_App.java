package c2d.lang.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.audio.C2D_Audio;

/**
 * 游戏世界：
 *  舞台----- C2D_Stage、场景----- C2D_Scene、道具（普通控件）----C2D_Widget 
 * 普通控件包括：
 * 精灵-----C2D_Sprite、系统文本框----C2D_SysTextBox、图片文本框----C2D_PicTextBox等
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_App extends android.app.Activity
{
	/*
	 * ===================设置、获取应用程序句柄==============================
	 */
	/** 应用程序句柄. */
	private static C2D_App m_app = null;
	/** 画布 */
	private static C2D_Canvas m_canvas = null;
	private Handler m_handler;

	/**
	 * 获取应用程序句柄.
	 * 
	 * @return C2D_APP 返回应用程序句柄
	 */
	public static C2D_App getApp()
	{
		return m_app;
	}
	/**
	 * 构造函数
	 */
	public C2D_App()
	{
	}

	/**
	 * 设置你的启动画布，此时不要进行资源的初始化，后续系统会自动调用
	 * canvas的onEnter_C2D方法
	 */
	protected abstract C2D_Canvas onCreate_C2D();

	/**
	 * 当暂停了应用程序，即应用程序转入后台
	 */
	protected abstract void onPauseApp_C2D();

	/**
	 * 当恢复应用程序，即应用程序恢复到前台
	 */
	protected abstract void onResumeApp_C2D();

	/**
	 * 当即将退出应用程序
	 */
	protected abstract void onCloseApp_C2D();

	/**
	 * 创建Activity
	 */
	@Override
	protected final void onCreate(android.os.Bundle savedInstanceContent)
	{
		super.onCreate(savedInstanceContent);
		C2D_Debug.log("@---------------------appOnCreate");
		setFullScreen();
		m_app = this;
		m_handler = new Handler();
		if (m_canvas == null)
		{
			m_canvas = onCreate_C2D();
		}
		if (m_canvas != null)
		{
			try
			{
				setContentView(m_canvas);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			m_canvas.onEnterBySys();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		C2D_Debug.log("@---------------------appOnStart");
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		C2D_Debug.log("@---------------------appOnRestart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		onResumeApp_C2D();
		C2D_Debug.log("@---------------------appOnResume");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		onPauseApp_C2D();
		C2D_Debug.log("@---------------------appOnPause");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		C2D_Debug.log("@---------------------appOnStop");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		C2D_Debug.log("@---------------------appOnDestroy");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		C2D_Debug.log("@---------------------appOnSaveInstanceState");
	}

	/** 设置全屏 */
	protected void setFullScreen()
	{
		final Window win = getWindow();
		// No Statusbar
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// No Titlebar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// ===================按键与触屏操作处理===================

	/**
	 * 按键按下事件，系统函数
	 */
	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (m_canvas != null)
		{
			return m_canvas.onKeyDown_C2D(keyCode);
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 按键弹起事件，系统函数
	 */
	public final boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (m_canvas != null)
		{
			return m_canvas.onKeyUp_C2D(keyCode);
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 触屏事件处理，系统函数
	 * 
	 * @param event
	 *            事件
	 * @return 是否不再转发此事件
	 */
	public boolean onTouchEvent(MotionEvent event)
	{
		if (m_canvas != null)
		{
			return m_canvas.onTouchEvent_C2D(event);
		}
		else
		{
			return super.onTouchEvent(event);
		}
	}

	/** 载入库 **/
	static
	{
		try
		{
			System.loadLibrary("C2D_Math");
		}
		catch (UnsatisfiedLinkError ule)
		{
			System.err.println("WARNING: Could not load library C2D_Core!");
		}
	}

	/**
	 * 标记应用程序即将退出
	 */
	public static void ShutDown()
	{
		DoClose();
	}

	/**
	 * 退出应用程序。注意在退出前退出所有其它线程，并释放所有资源。.
	 */
	static void DoClose()
	{
		C2D_Debug.logC2D( "@---------------------shutDown");
		C2D_App app=m_app;
		m_app = null;
		m_canvas = null;
		if(app!=null)
		{
			app.onCloseApp_C2D();
			C2D_Debug.logC2D("app closed");
		}
		C2D_Audio.closeAudio();
		C2D_Debug.logC2D("audio closed");
		c2d.lang.io.C2D_ServerLogger.close();
		C2D_Debug.logC2D("log server closed");
		System.exit(0);
	}

	public Handler getHandler()
	{
		return m_handler;
	}
}
