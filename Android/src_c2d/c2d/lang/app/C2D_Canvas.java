package c2d.lang.app;

import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import c2d.lang.math.type.C2D_SizeI;
import c2d.lang.math.type.C2D_TouchData;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Graphics;

public abstract class C2D_Canvas extends GLSurfaceView implements SurfaceHolder.Callback,C2D_Consts
{
	/*
	 * �豸����ߴ磨���أ�����ϵͳ��ȡ�������Ը��ġ�������Ͽ��Բ������ֵ��
	 */
	protected static final C2D_SizeI Device_Size = new C2D_SizeI();
	/** �µĵ����� */
	private int singleKeyNew = C2D_Device.key_empty;

	/** ��ǰ������ */
	private int singleKeyCurrent = C2D_Device.key_empty;

	/** �ϴΰ�����ԭʼ��ֵ */
	public int LastKeyOrgCode = -1;
	/**
	 * �Ƿ���Ҫ����ˢ��������
	 */
	public boolean m_needRepaint = true;
	/**
	 * ��������
	 */
	private C2D_TouchData m_newTouchData = new C2D_TouchData();
	public C2D_TouchData m_curTouchData = new C2D_TouchData();
	/** ���ڼ���һ֡���ͼ���� */
	public static int DrawCall = 0;
	private static int DrawCallLast = 0;
	private static boolean showPaintInfor = false;
	/**
	 * ѭ������ʼ����������ȥʱ��
	 */
	private long m_timeBegin, m_timeLast, m_timePassed;
	/**
	 * FPSͳ��ֵ��������
	 */
	private int fpsSum = 0;
	/**
	 * ��ǰ��FPS
	 */
	protected int m_fps = 0;
	/**
	 * ϣ���ﵽ��FPS
	 */
	protected int m_fpsWish = 16;
	/**
	 * ϣ���ﵽ��FPS��ֵ����
	 */
	private static final int m_fpsWish_min = 8;
	/**
	 * ϣ���ﵽ��FPS��ֵ����
	 */
	private static final int m_fpsWish_max = 20;
	/**
	 * Ϊ�˴ﵽ������FPS�����ѭ���������������仯�ļ��
	 */
	private int m_updateRateInc = 2;
	/**
	 * fps������
	 */
	private static long m_fpsSpan = 1000;
	/**
	 * 1��
	 */
	private static final long Second_1 = 1000;
	/**
	 * 10��
	 */
	private static final long Second_10 = 10000;
	/**
	 * ѭ������
	 */
	private int m_updateRate = 15;
	/**
	 * ÿ��ѭ��������Ҫ�ȴ���ͼ���
	 */
	private String m_name = "Canvas" + this.hashCode();
	/** ѭ��״̬ */
	public static final int LOOP_NotStarted = 0;// ��û������
	public static final int LOOP_Started = 1; // �Ѿ�����
	public static final int LOOP_Resumed = 2; // ����ͣ�лָ�
	public static final int LOOP_Inited = 3; // ��ɳ�ʼ��
	public static final int LOOP_GoPause = 4; // ������ͣ
	public static final int LOOP_Paused = 5; // ��ͣ��
	public static final int LOOP_GoToEnd = 6; // ���Ϊ��������������
	public static final int LOOP_AtEnd = 7; // �Ѿ�����
	/** ��ǰ��ѭ��״̬ */
	protected int m_loopState = LOOP_NotStarted;

	/** �Ƿ�����ѭ�� */

	public boolean isRunning()
	{
		return m_loopState == LOOP_Started || m_loopState == LOOP_Started || m_loopState == LOOP_Resumed;
	}

	protected synchronized void setLoopState(int state)
	{
		m_loopState = state;
		C2D_Debug.logC2D(m_name + " loop state:" + m_loopState);
	}

	/**
	 * ���캯��
	 */
	public C2D_Canvas()
	{
		super(C2D_App.getApp());
		initHolder();
	}

	/**
	 * ��������
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * ����Canvas��Ϊ��������������Ӧ��ʱ����������������ڳ�ʼ�������Դ
	 */
	protected abstract void onEnter_C2D();

	/**
	 * onUpdate �����¼���������ʵ�֣���ϵͳ����.
	 * 
	 */
	protected abstract void onUpdate_C2D();

	/**
	 * onPaint �ػ��¼���������ʵ�֣���ϵͳ����.
	 * 
	 */
	protected abstract void onPaint_C2D(C2D_Graphics g);

	/**
	 * �ڵ�ǰ��Canvas��������ҷ����ⲿ�ж��¼�ʱ������.
	 */
	protected abstract void onPause_C2D();

	/**
	 * �ڵ�ǰ��Canvas��������ҷ����ⲿ�ж��¼���ָ�ʱ������.
	 */
	protected abstract void onResume_C2D();

	/**
	 * �ڵ�ǰ��Ӧ�ñ��ر�ʱ������.
	 */
	protected abstract void onClose_C2D();

	/**
	 * onChange �����仯�¼�
	 * 
	 * @param w
	 *            ���
	 * @param h
	 *            �߶�
	 */
	protected abstract void onChangeView_C2D(int w, int h);

	/**
	 * ��ͣ��ϵͳ����
	 */
	@Override
	public final void onPause()
	{
		loopThread.onPause();
	}

	/**
	 * �ָ���ϵͳ����
	 */
	@Override
	public final void onResume()
	{
		loopThread.onResume();
	}

	/**
	 * ����ϵͳ�����ʹ����¼���һ������Ϸѭ���Ŀ�ʼ����һ�Σ��Ա�֤����ѭ���а�����Ϣ���ٱ�ϵͳ���ı䡣.
	 */
	private void updateKeyAndToch()
	{
		// ���µ�����ֵ
		singleKeyCurrent = singleKeyNew;
		if (m_curTouchData != null && m_newTouchData != null)
		{
			m_curTouchData.updateFrom(m_newTouchData);
		}
	}

	/**
	 * getSingleKey ���ص�һ���������������ͬʱ����ʱ�� �������һ�������µİ�����
	 * 
	 * @return int ������ֵ
	 */
	public int getSingleKey()
	{
		return singleKeyCurrent;
	}

	/**
	 * �����������������߷��ؼ�
	 * 
	 * @return
	 */
	public boolean isKeyBackOrRSoft()
	{
		return singleKeyCurrent == C2D_Device.key_back || singleKeyCurrent == C2D_Device.key_back1 || singleKeyCurrent == C2D_Device.key_back2 || singleKeyCurrent == C2D_Device.key_back3 || singleKeyCurrent == C2D_Device.key_back4 || singleKeyCurrent == C2D_Device.key_RSoft;
	}

	/**
	 * ����Ƿ��ذ������������������,���ؼ�����0��
	 * 
	 * @return �Ƿ����˷��ذ���
	 */
	public boolean isKeyCancle()
	{
		return singleKeyCurrent == C2D_Device.key_back || singleKeyCurrent == C2D_Device.key_back1 || singleKeyCurrent == C2D_Device.key_back2 || singleKeyCurrent == C2D_Device.key_back3 || singleKeyCurrent == C2D_Device.key_back4 || singleKeyCurrent == C2D_Device.key_num0 || singleKeyCurrent == C2D_Device.key_RSoft;
	}

	/**
	 * �Ƿ�������Ч�������롣���ܱ�ʶ��İ���
	 * 
	 * @return �Ƿ�������Ч��������
	 */
	public boolean isValidKeyPressed()
	{
		return singleKeyCurrent >= C2D_Device.key_up && singleKeyCurrent < C2D_Device.key_other;
	}

	/**
	 * �ͷ����а�����Ϣ.
	 */
	public void releaseKeys()
	{
		singleKeyNew = C2D_Device.key_empty;
		singleKeyCurrent = C2D_Device.key_empty;
	}

	/**
	 * �ͷŴ�����Ϣ��ֱ�����յ��µĴ��㰴���¼�
	 */
	public void releaseTouchPoints()
	{
		if (m_curTouchData != null)
		{
			m_curTouchData.setInvalid(true);
		}
	}
	/**
	 * �ͷ�����İ����ʹ�����Ϣ
	 */
	public void releaseInput()
	{
		releaseKeys();
		releaseTouchPoints();
	}

	/**
	 * ����Canvas������ϵͳ����
	 */
	public final void onEnterBySys()
	{
		setLoopState(LOOP_NotStarted);
		startLoop();
	}

	/**
	 * ����ѭ���߳�
	 */
	private final void startLoop()
	{
		if (m_loopState != LOOP_NotStarted)
		{
			return;
		}
		loopThread = new GLThread();
		setLoopState(LOOP_Started);
		loopThread.start();
	}

	/**
	 * ֹͣѭ���̣߳��������onClose_C2D()������
	 */
	public void stopLoop()
	{
		if (m_loopState >= LOOP_Started && m_loopState < LOOP_AtEnd)
		{
			setLoopState(LOOP_GoToEnd);
		}
		else
		{
			setLoopState(LOOP_AtEnd);
		}
	}

	// ===================�����봥����������===================

	/**
	 * ���������¼���ϵͳ����
	 * 
	 * @param keyCode
	 */
	public final boolean onKeyDown_C2D(int keyCode)
	{
		LastKeyOrgCode = keyCode;
		int key = getAndroidKey(keyCode);
		if (key != -1)
		{
			C2D_Debug.logDebug("Got Vitrual Key:" + key);
			singleKeyNew = key;
			return true;
		}
		else
		{
			C2D_Debug.logDebug("@Unknown KeyCode:" + keyCode);
		}
		return false;
	}

	/**
	 * ���������¼���ϵͳ����
	 * 
	 * @param keyCode
	 */
	public final boolean onKeyUp_C2D(int keyCode)
	{
		int key = getAndroidKey(keyCode);
		if (key != -1 && m_allowReleaseKeyBySys)
		{
			C2D_Debug.logDebug("@LostKey:" + key);
			singleKeyNew = C2D_Device.key_empty;
			return true;
		}
		return false;
	}

	protected boolean m_allowReleaseKeyBySys = true;

	/**
	 * �Ƿ�����ϵͳ�Զ��ͷŰ��������ģ����������������ʹ�ô˹���
	 * 
	 * @param allow
	 *            �Ƿ�����
	 */
	public void allowReleaseKeyBySys(boolean allow)
	{
		m_allowReleaseKeyBySys = allow;
	}

	/**
	 * ת��Android��ֵ
	 */
	protected int getAndroidKey(int keyCode)
	{
		int codeCast = -1;
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_UP:
			codeCast = C2D_Device.key_up;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			codeCast = C2D_Device.key_right;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			codeCast = C2D_Device.key_down;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			codeCast = C2D_Device.key_left;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			codeCast = C2D_Device.key_enter;
			break;
		case KeyEvent.KEYCODE_BACK:
			codeCast = C2D_Device.key_back;
			break;
		}
		if (codeCast == -1)
		{
			codeCast = C2D_Device.getVirtualKey(keyCode);
		}
		return codeCast;
	}

	/**
	 * �����¼�����ϵͳ����
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ���ת�����¼�
	 */
	public final boolean onTouchEvent_C2D(MotionEvent event)
	{
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		m_newTouchData.m_lastAction = action;
//		C2D_Debug.logC2D(action+ " -- "+m_newTouchData.m_lastPointerID);
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < m_newTouchData.m_touchStates.length; i++)
			{
				m_newTouchData.m_touchStates[i] = false;
			}
			int pointCount = event.getPointerCount();
			m_newTouchData.m_touchCount = 0;
			float upX = event.getX();
			float upY = event.getY();
			for (int i = 0; i < pointCount; i++)
			{
				int id = event.getPointerId(i);
				try
				{
					float x = event.getX(id);
					float y = event.getY(id);
					if (!(action == MotionEvent.ACTION_UP && x == upX && y == upY))
					{
						x = x * User_Size.m_width / Device_Size.m_width;
						y = y * User_Size.m_height / Device_Size.m_height;
						m_newTouchData.m_touchPoints[id].m_x = x;
						m_newTouchData.m_touchPoints[id].m_y = y;
						m_newTouchData.m_touchStates[id] = true;
						m_newTouchData.m_touchCount++;
					}
				}
				catch (Exception e)
				{

				}
			}
			m_newTouchData.incNewDataTime();
//			m_newTouchData.printInf("new touch");
			break;
		}
		if (action== MotionEvent.ACTION_DOWN&&m_newTouchData.m_touchCount==1)
		{
			m_curTouchData.setInvalid(false);
		}
		return true;
	}

	// ===================������Ϣ===================
	/**
	 * �Ƿ�������ʾ������Ϣ
	 * 
	 * @param enable
	 */
	public static void ShowPaintInfor(boolean enable)
	{
		showPaintInfor = enable;
	}

	// ===================��ͼ����===================
	private class EglHelper
	{
		public EglHelper()
		{

		}

		/**
		 * ��ʼ�� EGL,���ݸ��������ñ�
		 * 
		 * @param ���ñ�
		 */
		public void start(int[] configSpec)
		{
			/*
			 * ���EGL������ʵ��
			 */
			mEgl = (EGL10) EGLContext.getEGL();

			/*
			 * ���Ĭ�ϵ���ʾ�豸
			 */
			mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

			/*
			 * �������ǿ���Ϊ��ʾ�豸��ʼ��EGL������
			 */
			int[] version = new int[2];
			mEgl.eglInitialize(mEglDisplay, version);

			EGLConfig[] configs = new EGLConfig[1];
			int[] num_config = new int[1];
			mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, num_config);
			mEglConfig = configs[0];

			/*
			 * ����OpenGL������. ������ֻ��ִ��һ��, һ��OpenGL�����Ļ����Ĵ�����Դ
			 */
			mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig, EGL10.EGL_NO_CONTEXT, null);

			mEglSurface = null;
		}

		/*
		 * ����һ�� OpenGL����
		 */
		public GL createSurface(SurfaceHolder holder)
		{
			/*
			 * �����ڴ�С�ı�,������Ҫ����һ���µı���
			 */
			if (mEglSurface != null)
			{
				/*
				 * ����󶨲����پɵ�EGL����, ��������ڵĻ�
				 */
				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
			}

			/*
			 * ����һ���µ�EGL���棬������������Ⱦ
			 */
			mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig, holder, null);

			/*
			 * �����Ƿ���GL����֮ǰ��������Ҫȷ�������ı��ǰ󶨵���EGL����
			 */
			mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext);

			GL gl = mEglContext.getGL();
			return gl;
		}

		public void destroySurface()
		{
			if (mEglSurface != null)
			{
				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
				mEglSurface = null;
			}
		}

		/**
		 * ��ʾ��ǰ��render surface
		 * 
		 * @return false ���context��ʧ
		 */
		public boolean swap()
		{
			mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);

			/*
			 * ʼ�ջ���EGL_CONTEXT_LOST, ����ζ�������ĺ���ص����ݶ�ʧ
			 * ��������Ϊ�豸ת������״̬).������Ҫ���߳�����ֱ��������µ�surface
			 */
			return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
		}

		public void finish()
		{
			if (mEglSurface != null)
			{
				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
				mEglSurface = null;
			}
			if (mEglContext != null)
			{
				mEgl.eglDestroyContext(mEglDisplay, mEglContext);
				mEglContext = null;
			}
			if (mEglDisplay != null)
			{
				mEgl.eglTerminate(mEglDisplay);
				mEglDisplay = null;
			}
		}

		EGL10 mEgl;
		EGLDisplay mEglDisplay;
		EGLSurface mEglSurface;
		EGLConfig mEglConfig;
		EGLContext mEglContext;
	}

	// ===================��ѭ������===================
	/**
	 * ��ȡ��ǰ������ʱ�䣬��λΪ����
	 * 
	 * @return ����ʱ��
	 */
	public int getTimePassed()
	{
		return (int) m_timePassed;
	}

	/**
	 * ������ȥʱ�䣬��ʱ��Ϊ��Ҫȥ����ʱ����ص�Ӱ��
	 */
	public void resetTimePassed()
	{
		m_timePassed = 0;
	}

	/**
	 * ��ȡ��ǰ֡��
	 * 
	 * @return ֡��
	 */
	public int getFps()
	{
		return m_fps;
	}

	/**
	 * ��ȡÿ��ѭ��Ԥ�ڵ��������ڣ���λΪ����
	 * 
	 * @return ����ʱ��
	 */
	public int getRunRate()
	{
		return m_updateRate;
	}

	private class GLThread extends Thread
	{
		GLThread()
		{
			super();
			Device_Size.setValue(0, 0);
			setName("GLThread");
		}

		/**
		 * ѭ����ʼ
		 */
		private void loop_Begin()
		{
			// ��ȥ��ʱ�����
			m_timeBegin = System.currentTimeMillis();
			if (m_timeLast > 0)
			{
				m_timePassed = m_timeBegin - m_timeLast;
				// ���ν�����ͣ�����ʱ������
				if (m_timePassed > 1000)
				{
					m_timePassed = m_updateRate;
				}
			}
			m_timeLast = m_timeBegin;

			if (m_timePassed > Second_10)
			{
				m_timePassed = m_updateRate;
			}
			// fps����
			m_fpsSpan -= m_timePassed;
			if (m_fpsSpan < 0)
			{
				m_fpsSpan += Second_1;
				if (m_fpsSpan < 0)
				{
					m_fpsSpan = Second_1;
				}
				m_fps = fpsSum;
				fpsSum = 0;
				fpsAdjust();

			}
			else
			{
				fpsSum++;
			}
		}

		// ��������FPS����
		private void fpsAdjust()
		{
			if (m_fps > m_fpsWish)
			{
				m_updateRate += m_updateRateInc;
			}
			else if (m_fps < m_fpsWish)
			{
				if (m_updateRate > m_updateRateInc)
				{
					m_updateRate -= m_updateRateInc;
				}
			}
		}

		/**
		 * ѭ������
		 */
		private void loopEnd()
		{
			if (showPaintInfor)
			{
				if (DrawCallLast != DrawCall)
				{
					DrawCallLast = DrawCall;
					C2D_Debug.log("DrawCall:" + DrawCallLast);
				}
				DrawCall = 0;
			}
			try
			{
				long timeNow = System.currentTimeMillis();
				if (timeNow - m_timeBegin < m_updateRate)
				{
					Thread.sleep(m_updateRate - (timeNow - m_timeBegin));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/*
		 * ���߳� ��android���������2��activityʵ�����µ�ʵ����onCreate()
		 * �������ܻ����ڵ�һ��ʵ����onDestroy()ִ�У�semaphore����֤ ͬʱֻ��һ��ʵ���ܷ���EGL��
		 */
		public void run()
		{
			try
			{
				try
				{
					sEglSemaphore.acquire();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					return;
				}

				while (m_loopState < LOOP_GoToEnd)
				{
					/*
					 * ����ͬ��״̬
					 */
					synchronized (this)
					{
						// ����յ���ͣ����Ϣ
						if (m_loopState == LOOP_GoPause)
						{
							// ����OpenGL Surface
							if (m_EglHelper != null)
							{
								m_EglHelper.destroySurface();
							}
							onPause_C2D();
							setLoopState(LOOP_Paused);
						}
						// �ȴ��ָ�
						if (needToWait())
						{
							while (needToWait())
							{
								wait();
							}
						}
						// �������ͣ�����н���
						if (m_loopState >= LOOP_GoToEnd)
						{
							break;
						}
					}
					// �ؽ�OpenGL����
					if (m_loopState <= LOOP_Resumed || m_contextLost)
					{
						if (m_EglHelper == null)
						{
							m_EglHelper = new EglHelper();
							int[] configSpec = getConfigSpec();
							m_EglHelper.start(configSpec);
						}
						m_EglHelper.createSurface(mHolder);
						setupGL();
						m_contextLost = false;
					}
					// �����û��¼�
					if (m_loopState == LOOP_Started || m_loopState == LOOP_Paused)
					{
						// �����û��¼�-����
						if (m_loopState == LOOP_Started)
						{
							onEnter_C2D();
						}
						// �����û��¼�-�ָ�
						if (m_loopState == LOOP_Paused)
						{
							onResume_C2D();
						}
						setLoopState(LOOP_Inited);
					}
					// �����û��¼�-�ı��С
					int w = Device_Size.m_width, h = Device_Size.m_height;
					if (m_sizeChanged && m_loopState == LOOP_Inited)
					{
						setupGL();
						setGLView(w, h);
						onChangeView_C2D(w, h);
						m_sizeChanged = false;
					}

					// ���ºͻ�ͼ
					if (w > 0 && h > 0 && m_loopState == LOOP_Inited)
					{
						loop_Begin();
						// ��������
						updateKeyAndToch();
						// ��������
						onUpdate_C2D();
						// �����Ļ
						C2D_Graphics.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
						// �����ɫ�����Ϊ����
						C2D_Graphics.glClear(C2D_Graphics.GL_COLOR_BUFFER_BIT | C2D_Graphics.GL_DEPTH_BUFFER_BIT);
						// ����ģ����ͼ����
						C2D_Graphics.glLoadIdentity();
						onPaint_C2D(m_g);
						m_EglHelper.swap();
						loopEnd();
					}
				}
				// �����ر�
				setLoopState(LOOP_AtEnd);
				/*
				 * ���EGL��Դ
				 */
				m_EglHelper.finish();
				m_EglHelper=null;
				m_g.release();
				m_g=null;
				sEglSemaphore.release();
				sEglSemaphore = null;
				onClose_C2D();
				C2D_App.DoClose();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (sEglSemaphore != null)
				{
					sEglSemaphore.release();
					sEglSemaphore = null;
				}
			}
		}

		private C2D_Graphics m_g = new C2D_Graphics();

		private boolean needToWait()
		{
			return (m_loopState == LOOP_Paused || (!m_hasFocus) || (!m_hasSurface)) && (m_loopState < LOOP_GoToEnd);
		}

		public void surfaceCreated()
		{
			synchronized (this)
			{
				m_hasSurface = true;
				notify();
			}
		}

		public void surfaceDestroyed()
		{
			synchronized (this)
			{
				m_hasSurface = false;
				m_contextLost = true;
				notify();
			}
		}

		public void onPause()
		{
			synchronized (this)
			{
				setLoopState(LOOP_GoPause);
			}
		}

		public void onResume()
		{
			synchronized (this)
			{
				setLoopState(LOOP_Resumed);
				notify();
			}
		}

		public void onFocusChanged(boolean hasFocus)
		{
			synchronized (this)
			{
				m_hasFocus = hasFocus;
				if (m_hasFocus == true)
				{
					notify();
				}
			}
		}

		public void onResize(int w, int h)
		{
			synchronized (this)
			{
				m_sizeChanged = true;
				Device_Size.m_width = w;
				Device_Size.m_height = h;
			}
		}

		private boolean m_hasFocus;
		private boolean m_hasSurface;
		private boolean m_contextLost;
		private EglHelper m_EglHelper;
	}

	/**
	 * ����OpenGL��������
	 */
	private void setupGL()
	{
		C2D_Debug.log("@---------------------setupGL");
		// �����Ļ
		C2D_Graphics.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// ��ɫģʽ��Ĭ�ϲ���ҪSmooth
		C2D_Graphics.glShadeModel(C2D_Graphics.GL_FLAT);
		// ��ʼ�����
		C2D_Graphics.glClearDepthf(0.0f);
		// ��������Ȳ���
		C2D_Graphics.glDisable(C2D_Graphics.GL_DEPTH_TEST);
		// ������ȷ�Χ
		// C2D_Graphics.glDepthRangef(-100.0f, 0.0f);
		// ָ����ȱȽϵķ���
		// C2D_Graphics.glDepthFunc(C2D_Graphics.GL_LEQUAL);
		// ��������
		C2D_Graphics.glDisable(C2D_Graphics.GL_DITHER);
		// ���������
		C2D_Graphics.glDisable(C2D_Graphics.GL_LIGHTING);
		// ��õ�͸�Ӽ���
		C2D_Graphics.glHint(C2D_Graphics.GL_PERSPECTIVE_CORRECTION_HINT, C2D_Graphics.GL_NICEST);
	}

	/**
	 * ����OpenGL�ӿ�
	 * 
	 * @param width
	 * @param height
	 */
	private void setGLView(int width, int height)
	{
		C2D_Debug.log("@---------------------setGLView:" + width + "x" + height);
		// ͶӰ�任
		C2D_Graphics.glMatrixMode(C2D_Graphics.GL_PROJECTION);
		// ����ͶӰ����
		C2D_Graphics.glLoadIdentity();
		// �ӿڱ任
		C2D_Graphics.glViewport(0, 0, width, height);
		// ����͸��ͶӰ����
		// GLU.gluPerspective(gl10, 45.0f, (float) width / (float) height, 0.1f,
		// 1000.0f);
		// ������ͶӰ
		// C2D_Graphics.glOrthof(-VIEW_WIDTH/2.0f, VIEW_WIDTH/2.0f,
		// -VIEW_HEIGHT/2.0f,
		// VIEW_HEIGHT/2.0f, -1, 1);
		C2D_Graphics.glOrthof(0, (float) Device_Size.m_width, 0, (float) Device_Size.m_height, -100, 100);

		// ӳ������ϵ(�ӿڷ�����+Z�ᣬֻ��λ��Z��-100,100��ȵ�����Ż���֣�
		// ��������ϵ������Ͻ�Ϊԭ�㣬����Ϊ+X������Ϊ+Y������ϵ)
		C2D_Graphics.glTranslatef(0, Device_Size.m_height, 0);
		C2D_Graphics.glScalef(1.0f * Device_Size.m_width / User_Size.m_width, -1.0f * Device_Size.m_height / User_Size.m_height, 1.0f);
		// C2D_Graphics.glRotatef(180, 1, 0, 0);
		// C2D_Graphics.glTranslatef(0, 0, -10.0f);
		// ģ�ͱ任
		C2D_Graphics.glMatrixMode(C2D_Graphics.GL_MODELVIEW);
		// ����ģ�;���
		C2D_Graphics.glLoadIdentity();
		// ����Alpha���ģʽ
		/*
		 * Alpha��ϸ����������й�ϵ��������ӡ�Դɫ��Ŀ��ɫ����ǰOpenGL��ɫ�����ա�
		 * Ŀǰ����ֻ�����޹��յ������Ŀǰ�������л�û���漰���յ����ݣ�����ݲ����ǡ� �����ɫ������������(Rs Sr + Rd Dr, Gs
		 * Sg + Gd Dg, Bs Sb + Bd Db, As Sa + Ad Da)
		 * ������ӣ����ǻ�ɫ������ÿ������ĺ���һ����Ǳ�ʾ��XXɫ�Ĳ���ٷֱȣ�Ҳ����alphaֵ
		 * Դɫ���������Ǽ������Ƶ���Ļ�ϵ���ɫ������ĳ����ͼ�е�ĳ������ɫ�� Ŀ��ɫ��������Ļ���Ѿ����ڵ���ɫ��Ҳ���Գ�֮Ϊ����ɫ��
		 * ��ǰOpenGL��ɫ������ʹ�ú���glColor4f����ͬ�ຯ�����õĵ�ǰOpenGL�����ɫ��
		 * 
		 * һ�����ǿ���û�е�ǰOpenGL��ɫӰ������µ�alpha��ϡ�Ҳ�����ڽ�����ͼ����ʱ��
		 * ���Ǳ�֤��ǰ��opengl��ɫ����ȫ��͸����ɫ����������glColor4f(1.0f,1.0f,1.0f,1.0f);
		 * 
		 * Rs Sr + Rd Dr�����壬����Դ��ɫ*Դ��ɫ����+Ŀ���ɫ*Ŀ���ɫ���ӣ���������
		 * ���л�����ӵĴ�С���ɺ���glBlendFuncȷ���ģ�����glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)��
		 * ����ȷ����Դ��ɫ����=[ԴɫAlpha]��Դ��ɫ����=[ԴɫAlpha]��Դ��ɫ����=[ԴɫAlpha]��
		 * Ŀ���ɫ����=1-ԴɫAlpha��Ŀ����ɫ����=1-ԴɫAlpha��Ŀ����ɫ����=1-ԴɫAlpha��
		 * 
		 * �����[ԴɫAlpha]��ֻ���Լ������ţ�����Ϊ��ʵ���ϲ�����������ԴɫAlpha��Ҳ��������������ͼĳ�����ص㴦��Alphaֵ��
		 * ���Ǿ�������ġ�����ķ�����AsSa��Ҳ���ǣ���Ҳ����As Sa + Ad Da�����塣
		 * ������������������ֻ�Ǽ�����һ������Alphaֵ������ȵ�һ����ɫ������ϣ������������Alphaֵ����ʲô���壿
		 * ��ˣ�ʵ���ϣ��������ʽ������һ����㣬���Ǳ����ڼ���ǰ�����ɫ�����з������á�Ҳ���Ǽ���[ԴɫAlpha]�� ��[Ŀ��ɫAlpha]��
		 * 
		 * ��������ʹ�÷���glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)����ô��ָ����Sa=��ʵԴɫAlpha ��ôԴ��ɫ���� = [ԴɫAlpha] = AsSa
		 * = ��ʵԴɫAlpha*��ʵԴɫAlpha
		 * Ҳָ����Da=1-��ʵԴɫAlpha����ôĿ���ɫ����=AdDa=1.0*(1-��ʵԴɫAlpha)
		 * ��Ϊ����Ŀ��ɫ��˵���Ѿ�������͸���ȵĸ�����Խ�Ϊ1.0����Ҳ�������Ŀ��ɫ��������ɫ��û��дalphaֵ��ԭ��
		 * 
		 * ����1��Դɫ�ǣ�0,148,205��0.5����Ŀ��ɫ�ǣ�255,255,255������ô������ɫ�ǣ�127,164,179�� 127 = 0
		 * *0.5*0.5 + 255*1.0*(1-0.5); 164 = 148*0.5*0.5 + 255*1.0*(1-0.5); 179
		 * = 205*0.5*0.5 + 255*1.0*(1-0.5);
		 * 
		 * ����2��Դɫ�ǣ�0,148,205��0.4����Ŀ��ɫ�ǣ�255,255,255������ô������ɫ�ǣ�153,177,186�� 153 = 0
		 * *0.4*0.4 + 255*1.0*(1-0.4); 177 = 148*0.4*0.4 + 255*1.0*(1-0.4); 186
		 * = 205*0.4*0.4 + 255*1.0*(1-0.4);
		 * 
		 * �ټ�������ʹ�÷���glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)����ô��ָ����Sa=1.0
		 * ��ôԴ��ɫ���� = [ԴɫAlpha] = AsSa = ��ʵԴɫAlpha*1.0
		 * ͬ��Ҳָ����Da=1-��ʵԴɫAlpha����ôĿ���ɫ����=AdDa=1.0*(1-��ʵԴɫAlpha)
		 * 
		 * ����1��Դɫ�ǣ�0,148,205��0.5����Ŀ��ɫ�ǣ�255,255,255������ô������ɫ�ǣ�127,201,230�� 127 = 0
		 * *0.5*1.0 + 255*1.0*(1-0.5); 201 = 148*0.5*1.0 + 255*1.0*(1-0.5); 230
		 * = 205*0.5*1.0 + 255*1.0*(1-0.5);
		 * 
		 * ����2��Դɫ�ǣ�0,148,205��0.4����Ŀ��ɫ�ǣ�255,255,255������ô������ɫ�ǣ�153,212,235�� 153 = 0
		 * *0.4*1.0 + 255*1.0*(1-0.4); 212 = 148*0.4*1.0 + 255*1.0*(1-0.4); 235
		 * = 205*0.4*1.0 + 255*1.0*(1-0.4);
		 * 
		 * ��ʹ��������glBlendFunc���̲���ʱ����֮���ơ�ʵ����Photoshop�еİ�͸�����ǲ��� glBlendFunc(GL_ONE,
		 * GL_ONE_MINUS_SRC_ALPHA)���ַ�ʽ���ӵģ�Ҳ���������������еİ�͸����ɫ��
		 * ������glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)��������ú��ߡ����ǻῴ������һ��
		 * ���ױ������棬����50%��͸����ȴ����ͼ�����˵Ľ������Ϊ���ַ�ʽ���ή�ͻ�������ȣ��滭 ���ӵ�Խ�࣬�ͻή��Խ�ࡣ
		 * 
		 * ���������������ǿ�glColor4f�����������������õ�openGL��ǰ��ɫ���Ӱ�����ǵĻ�ɫ�����
		 * ��������ʹ�÷���glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)��
		 * ��ô��ָ����As=��ʵԴɫAlpha��Ad=1-��ʵԴɫAlpha
		 * ʵ���ϵ�ǰOpenGL��ɫ�൱��һ���˲���������ʹ��С������ʾ�����׿��������塣
		 * ���赱ǰ����������glColor4f(1.0f,0.5f,0.5f,0.3f);
		 * ��ô�൱�ڼ��ڻ�ɫ֮ǰ�������Դɫ�����һ����ˣ����Ǽٶ�ʹ��(Cr,Cg,Cb,Ca)����ʾ
		 * 
		 * ����1��Դɫ�ǣ�0,148,205��0.5����Ŀ��ɫ�ǣ�255,255,255������ô������ɫ�ǣ�217,223,225)
		 * ��εõ�����ֵ�����������м��㡣�����Ѿ�ȷ��As=0.5��Ad = 1.0
		 * ���ȣ�����Sa������GL_SRC_ALPHA��Sa=As=0.5������Ϊ���������RGBA������ģ�������ڵ�Դɫ
		 * AlphaҲ�ᱻ����ֵ��Ӱ�졣���ڵ�Ca=0.3�����Sa=0.15��Ҳ������ǰ�����Sa=As*Ca=0.5*0.3=0.15��
		 * ����GL_ONE_MINUS_SRC_ALPHA�����ڵ�Da=1.0-Sa=0.85��
		 * 
		 * ���ڼ��㹫ʽ������һЩ�仯�� �����ɫ���㣬 Rf = Rs*Sr + Rd*Dr;���ڱ���� Rf = Rs*Sr*Cr +
		 * Rd*Dr;����Cr��ʾ�������ӡ� �ٻ���ʹ�÷���glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)�� Sr = As*Sa; Dr = Ad*Da; �ɴˣ����ǿ��Եó��� Rf
		 * =Rs*As*Sa*Cr+Rd* Ad*Da;
		 * Rs��ʾԴ��ɫ������As��ʾԴAlpha������Rd��ʾĿ���ɫ������Ad��ʾĿ��Alpha��������Щ���Ƕ�ֵ��
		 * Sa��Da�Ѿ���ǰ���м��㣬CrҲ����glColor4f(1.0f,0.5f,0.5f,0.3f);��������ˣ���ɫ��˼���:
		 * 
		 * 217 = 0 *0.5*0.15*1.0 + 255*1.0*0.85; 223 = 148*0.5*0.15*0.5 +
		 * 255*1.0*0.85; 225 = 205*0.5*0.15*0.5 + 255*1.0*0.85;
		 * 
		 * ��ˣ��ܽ���˵���������ͼʱ�����������˲����������ã�����Ӧ�����õ�ǰOpenGL��ɫΪ����ɫ��
		 * ������Sa=As*1.0f��Sa�����ܵ�Ӱ�죬������ɫ�����ڼ���ʱ��Ҳ����Ϊ������ӳ���1.0����������ԭֵ��
		 * ���������ʱ����Ҫ����һ����ɫ��ͼ����������˲������ã�ʹ�ò�ͬ��GL��ɫ��������ղ�ͬ��ɫ����ͼ��
		 */
		// ������ɫ���
		C2D_Graphics.glEnable(C2D_Graphics.GL_BLEND);
		// ������ɫ���ģʽ
		C2D_Graphics.glBlendFunc(C2D_Graphics.GL_SRC_ALPHA, C2D_Graphics.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * ��ȡOpenGL������ ϵͳ�����ݷ��ص����ñ�������OpenGL��ͼģʽ�� Ĭ��ʹ�ø�Ч�����ã���ʹ����Ȼ��棬ʹ��565����888��ɫģʽ��
	 * ʹ��EGL1.0�汾����ĳЩ������֧��565��ʽ������ȥ����ʼ�����С� int[] configSpec = {
	 * EGL10.EGL_RED_SIZE, 5, EGL10.EGL_GREEN_SIZE, 6, EGL10.EGL_BLUE_SIZE, 5,
	 * EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
	 * �����Ҫ�������Ļ���Ӧ����д���������������������������һ�����顣
	 * */
	protected int[] getConfigSpec()
	{
		int[] configSpec =
		{
				// EGL10.EGL_RED_SIZE, 5,
				// EGL10.EGL_GREEN_SIZE, 6,
				// EGL10.EGL_BLUE_SIZE, 5,
				EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE, };

		return configSpec;
	}

	private void initHolder()
	{
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	/**
	 * ���ص�ǰ��Ļ��SurfaceHolder
	 * 
	 * @return SurfaceHolder
	 */
	public SurfaceHolder getSurfaceHolder()
	{
		return mHolder;
	}

	// ===================ϵͳ��Ӧ===================
	public final void surfaceCreated(SurfaceHolder holder)
	{
		if (loopThread != null)
		{
			loopThread.surfaceCreated();
		}
	}

	public final void surfaceDestroyed(SurfaceHolder holder)
	{
		if (loopThread != null)
		{
			loopThread.surfaceDestroyed();
		}
	}

	/**
	 * ϵͳ����
	 */
	@Override
	public final void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		if (loopThread != null)
		{
			loopThread.onResize(width, height);
		}
	}

	/**
	 * ϵͳ����
	 */
	@Override
	public final void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (loopThread != null)
		{
			loopThread.onFocusChanged(hasFocus);
		}
	}

	/**
	 * ϵͳ����
	 */
	@Override
	protected final void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		if (loopThread != null)
		{
			try
			{
				loopThread.join();
			}
			catch (InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}
			loopThread = null;
		}
	}

	// ===================ƽ̨���г�Ա����===================
	private static Semaphore sEglSemaphore = new Semaphore(1);
	private boolean m_sizeChanged;
	private SurfaceHolder mHolder;
	private GLThread loopThread;
}
