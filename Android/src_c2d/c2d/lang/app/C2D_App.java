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
 * ��Ϸ���磺
 *  ��̨----- C2D_Stage������----- C2D_Scene�����ߣ���ͨ�ؼ���----C2D_Widget 
 * ��ͨ�ؼ�������
 * ����-----C2D_Sprite��ϵͳ�ı���----C2D_SysTextBox��ͼƬ�ı���----C2D_PicTextBox��
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_App extends android.app.Activity
{
	/*
	 * ===================���á���ȡӦ�ó�����==============================
	 */
	/** Ӧ�ó�����. */
	private static C2D_App m_app = null;
	/** ���� */
	private static C2D_Canvas m_canvas = null;
	private Handler m_handler;

	/**
	 * ��ȡӦ�ó�����.
	 * 
	 * @return C2D_APP ����Ӧ�ó�����
	 */
	public static C2D_App getApp()
	{
		return m_app;
	}
	/**
	 * ���캯��
	 */
	public C2D_App()
	{
	}

	/**
	 * �������������������ʱ��Ҫ������Դ�ĳ�ʼ��������ϵͳ���Զ�����
	 * canvas��onEnter_C2D����
	 */
	protected abstract C2D_Canvas onCreate_C2D();

	/**
	 * ����ͣ��Ӧ�ó��򣬼�Ӧ�ó���ת���̨
	 */
	protected abstract void onPauseApp_C2D();

	/**
	 * ���ָ�Ӧ�ó��򣬼�Ӧ�ó���ָ���ǰ̨
	 */
	protected abstract void onResumeApp_C2D();

	/**
	 * �������˳�Ӧ�ó���
	 */
	protected abstract void onCloseApp_C2D();

	/**
	 * ����Activity
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

	/** ����ȫ�� */
	protected void setFullScreen()
	{
		final Window win = getWindow();
		// No Statusbar
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// No Titlebar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// ===================�����봥����������===================

	/**
	 * ���������¼���ϵͳ����
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
	 * ���������¼���ϵͳ����
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
	 * �����¼�����ϵͳ����
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ���ת�����¼�
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

	/** ����� **/
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
	 * ���Ӧ�ó��򼴽��˳�
	 */
	public static void ShutDown()
	{
		DoClose();
	}

	/**
	 * �˳�Ӧ�ó���ע�����˳�ǰ�˳����������̣߳����ͷ�������Դ��.
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
