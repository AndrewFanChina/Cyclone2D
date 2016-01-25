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
	 * 设备物理尺寸（像素），由系统获取，不可以更改。你基本上可以不用这个值。
	 */
	protected static final C2D_SizeI Device_Size = new C2D_SizeI();
	/** 新的单按键 */
	private int singleKeyNew = C2D_Device.key_empty;

	/** 当前单按键 */
	private int singleKeyCurrent = C2D_Device.key_empty;

	/** 上次按键的原始键值 */
	public int LastKeyOrgCode = -1;
	/**
	 * 是否需要重新刷新整个界
	 */
	public boolean m_needRepaint = true;
	/**
	 * 手势数据
	 */
	private C2D_TouchData m_newTouchData = new C2D_TouchData();
	public C2D_TouchData m_curTouchData = new C2D_TouchData();
	/** 用于计算一帧类绘图次数 */
	public static int DrawCall = 0;
	private static int DrawCallLast = 0;
	private static boolean showPaintInfor = false;
	/**
	 * 循环的起始、结束和逝去时间
	 */
	private long m_timeBegin, m_timeLast, m_timePassed;
	/**
	 * FPS统计值，计算用
	 */
	private int fpsSum = 0;
	/**
	 * 当前的FPS
	 */
	protected int m_fps = 0;
	/**
	 * 希望达到的FPS
	 */
	protected int m_fpsWish = 16;
	/**
	 * 希望达到的FPS阈值下限
	 */
	private static final int m_fpsWish_min = 8;
	/**
	 * 希望达到的FPS阈值上限
	 */
	private static final int m_fpsWish_max = 20;
	/**
	 * 为了达到期望的FPS，针对循环周期所作增减变化的间隔
	 */
	private int m_updateRateInc = 2;
	/**
	 * fps计算间隔
	 */
	private static long m_fpsSpan = 1000;
	/**
	 * 1秒
	 */
	private static final long Second_1 = 1000;
	/**
	 * 10秒
	 */
	private static final long Second_10 = 10000;
	/**
	 * 循环周期
	 */
	private int m_updateRate = 15;
	/**
	 * 每个循环周期需要等待绘图完毕
	 */
	private String m_name = "Canvas" + this.hashCode();
	/** 循环状态 */
	public static final int LOOP_NotStarted = 0;// 还没有启动
	public static final int LOOP_Started = 1; // 已经启动
	public static final int LOOP_Resumed = 2; // 从暂停中恢复
	public static final int LOOP_Inited = 3; // 完成初始化
	public static final int LOOP_GoPause = 4; // 进入暂停
	public static final int LOOP_Paused = 5; // 暂停中
	public static final int LOOP_GoToEnd = 6; // 标记为结束，即将结束
	public static final int LOOP_AtEnd = 7; // 已经结束
	/** 当前的循环状态 */
	protected int m_loopState = LOOP_NotStarted;

	/** 是否正在循环 */

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
	 * 构造函数
	 */
	public C2D_Canvas()
	{
		super(C2D_App.getApp());
		initHolder();
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * 当此Canvas作为启动画布，启动应用时发生，在这个函数内初始化你的资源
	 */
	protected abstract void onEnter_C2D();

	/**
	 * onUpdate 更新事件，子类须实现，由系统调用.
	 * 
	 */
	protected abstract void onUpdate_C2D();

	/**
	 * onPaint 重绘事件，子类须实现，由系统调用.
	 * 
	 */
	protected abstract void onPaint_C2D(C2D_Graphics g);

	/**
	 * 在当前的Canvas被激活，并且发生外部中断事件时被调用.
	 */
	protected abstract void onPause_C2D();

	/**
	 * 在当前的Canvas被激活，并且发生外部中断事件后恢复时被调用.
	 */
	protected abstract void onResume_C2D();

	/**
	 * 在当前的应用被关闭时被调用.
	 */
	protected abstract void onClose_C2D();

	/**
	 * onChange 画布变化事件
	 * 
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	protected abstract void onChangeView_C2D(int w, int h);

	/**
	 * 暂停，系统函数
	 */
	@Override
	public final void onPause()
	{
		loopThread.onPause();
	}

	/**
	 * 恢复，系统函数
	 */
	@Override
	public final void onResume()
	{
		loopThread.onResume();
	}

	/**
	 * 更新系统按键和触点事件，一般在游戏循环的开始调用一次，以保证本次循环中按键信息不再被系统所改变。.
	 */
	private void updateKeyAndToch()
	{
		// 更新单按键值
		singleKeyCurrent = singleKeyNew;
		if (m_curTouchData != null && m_newTouchData != null)
		{
			m_curTouchData.updateFrom(m_newTouchData);
		}
	}

	/**
	 * getSingleKey 返回单一按键，当多个按键同时存在时， 返回最后一个被按下的按键。
	 * 
	 * @return int 返回数值
	 */
	public int getSingleKey()
	{
		return singleKeyCurrent;
	}

	/**
	 * 如果按下了右软键或者返回键
	 * 
	 * @return
	 */
	public boolean isKeyBackOrRSoft()
	{
		return singleKeyCurrent == C2D_Device.key_back || singleKeyCurrent == C2D_Device.key_back1 || singleKeyCurrent == C2D_Device.key_back2 || singleKeyCurrent == C2D_Device.key_back3 || singleKeyCurrent == C2D_Device.key_back4 || singleKeyCurrent == C2D_Device.key_RSoft;
	}

	/**
	 * 如果是返回按键，即按下了右软键,返回键或者0键
	 * 
	 * @return 是否按下了返回按键
	 */
	public boolean isKeyCancle()
	{
		return singleKeyCurrent == C2D_Device.key_back || singleKeyCurrent == C2D_Device.key_back1 || singleKeyCurrent == C2D_Device.key_back2 || singleKeyCurrent == C2D_Device.key_back3 || singleKeyCurrent == C2D_Device.key_back4 || singleKeyCurrent == C2D_Device.key_num0 || singleKeyCurrent == C2D_Device.key_RSoft;
	}

	/**
	 * 是否正有有效按键输入。即能被识别的按键
	 * 
	 * @return 是否正有有效按键输入
	 */
	public boolean isValidKeyPressed()
	{
		return singleKeyCurrent >= C2D_Device.key_up && singleKeyCurrent < C2D_Device.key_other;
	}

	/**
	 * 释放所有按键信息.
	 */
	public void releaseKeys()
	{
		singleKeyNew = C2D_Device.key_empty;
		singleKeyCurrent = C2D_Device.key_empty;
	}

	/**
	 * 释放触点信息，直到接收到新的触点按下事件
	 */
	public void releaseTouchPoints()
	{
		if (m_curTouchData != null)
		{
			m_curTouchData.setInvalid(true);
		}
	}
	/**
	 * 释放输入的按键和触屏信息
	 */
	public void releaseInput()
	{
		releaseKeys();
		releaseTouchPoints();
	}

	/**
	 * 进入Canvas发生，系统函数
	 */
	public final void onEnterBySys()
	{
		setLoopState(LOOP_NotStarted);
		startLoop();
	}

	/**
	 * 启动循环线程
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
	 * 停止循环线程，随后会调用onClose_C2D()方法。
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

	// ===================按键与触屏操作处理===================

	/**
	 * 按键按下事件，系统函数
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
	 * 按键弹起事件，系统函数
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
	 * 是否允许系统自动释放按键，针对模拟器不足的情况可以使用此功能
	 * 
	 * @param allow
	 *            是否允许
	 */
	public void allowReleaseKeyBySys(boolean allow)
	{
		m_allowReleaseKeyBySys = allow;
	}

	/**
	 * 转换Android键值
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
	 * 触屏事件处理，系统函数
	 * 
	 * @param event
	 *            事件
	 * @return 是否不再转发此事件
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

	// ===================测试信息===================
	/**
	 * 是否允许显示测试信息
	 * 
	 * @param enable
	 */
	public static void ShowPaintInfor(boolean enable)
	{
		showPaintInfor = enable;
	}

	// ===================绘图处理===================
	private class EglHelper
	{
		public EglHelper()
		{

		}

		/**
		 * 初始化 EGL,根据给定的配置表
		 * 
		 * @param 配置表
		 */
		public void start(int[] configSpec)
		{
			/*
			 * 获得EGL上下文实例
			 */
			mEgl = (EGL10) EGLContext.getEGL();

			/*
			 * 获得默认的显示设备
			 */
			mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

			/*
			 * 现在我们可以为显示设备初始化EGL上下文
			 */
			int[] version = new int[2];
			mEgl.eglInitialize(mEglDisplay, version);

			EGLConfig[] configs = new EGLConfig[1];
			int[] num_config = new int[1];
			mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, num_config);
			mEglConfig = configs[0];

			/*
			 * 创建OpenGL上下文. 必须且只能执行一次, 一个OpenGL上下文会消耗大量资源
			 */
			mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig, EGL10.EGL_NO_CONTEXT, null);

			mEglSurface = null;
		}

		/*
		 * 创建一个 OpenGL表面
		 */
		public GL createSurface(SurfaceHolder holder)
		{
			/*
			 * 当窗口大小改变,我们需要创建一个新的表面
			 */
			if (mEglSurface != null)
			{
				/*
				 * 解除绑定并销毁旧的EGL表面, 如果它存在的话
				 */
				mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
			}

			/*
			 * 创建一个新的EGL表面，它将被用来渲染
			 */
			mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig, holder, null);

			/*
			 * 当我们发出GL命令之前，我们需要确定上下文本是绑定到了EGL表面
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
		 * 显示当前的render surface
		 * 
		 * @return false 如果context丢失
		 */
		public boolean swap()
		{
			mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);

			/*
			 * 始终会检查EGL_CONTEXT_LOST, 它意味着上下文和相关的数据丢失
			 * （例如因为设备转入休眠状态).我们需要让线程休眠直到它获得新的surface
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

	// ===================主循环处理===================
	/**
	 * 获取当前的运行时间，单位为毫秒
	 * 
	 * @return 运行时间
	 */
	public int getTimePassed()
	{
		return (int) m_timePassed;
	}

	/**
	 * 重置逝去时间，有时因为需要去除长时间加载的影响
	 */
	public void resetTimePassed()
	{
		m_timePassed = 0;
	}

	/**
	 * 获取当前帧率
	 * 
	 * @return 帧率
	 */
	public int getFps()
	{
		return m_fps;
	}

	/**
	 * 获取每个循环预期的运行周期，单位为毫秒
	 * 
	 * @return 运行时间
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
		 * 循环开始
		 */
		private void loop_Begin()
		{
			// 过去的时间计算
			m_timeBegin = System.currentTimeMillis();
			if (m_timeLast > 0)
			{
				m_timePassed = m_timeBegin - m_timeLast;
				// 屏蔽界面暂停引起的时间消耗
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
			// fps计算
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

		// 进行期望FPS调整
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
		 * 循环结束
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
		 * 主线程 当android框架启动第2个activity实例，新的实例的onCreate()
		 * 方法可能会先于第一个实例的onDestroy()执行，semaphore将保证 同时只有一个实例能访问EGL。
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
					 * 更新同步状态
					 */
					synchronized (this)
					{
						// 如果收到暂停的消息
						if (m_loopState == LOOP_GoPause)
						{
							// 销毁OpenGL Surface
							if (m_EglHelper != null)
							{
								m_EglHelper.destroySurface();
							}
							onPause_C2D();
							setLoopState(LOOP_Paused);
						}
						// 等待恢复
						if (needToWait())
						{
							while (needToWait())
							{
								wait();
							}
						}
						// 如果在暂停过程中结束
						if (m_loopState >= LOOP_GoToEnd)
						{
							break;
						}
					}
					// 重建OpenGL表面
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
					// 处理用户事件
					if (m_loopState == LOOP_Started || m_loopState == LOOP_Paused)
					{
						// 处理用户事件-进入
						if (m_loopState == LOOP_Started)
						{
							onEnter_C2D();
						}
						// 处理用户事件-恢复
						if (m_loopState == LOOP_Paused)
						{
							onResume_C2D();
						}
						setLoopState(LOOP_Inited);
					}
					// 处理用户事件-改变大小
					int w = Device_Size.m_width, h = Device_Size.m_height;
					if (m_sizeChanged && m_loopState == LOOP_Inited)
					{
						setupGL();
						setGLView(w, h);
						onChangeView_C2D(w, h);
						m_sizeChanged = false;
					}

					// 更新和绘图
					if (w > 0 && h > 0 && m_loopState == LOOP_Inited)
					{
						loop_Begin();
						// 按键更新
						updateKeyAndToch();
						// 更新世界
						onUpdate_C2D();
						// 清除屏幕
						C2D_Graphics.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
						// 清除颜色和深度为缓冲
						C2D_Graphics.glClear(C2D_Graphics.GL_COLOR_BUFFER_BIT | C2D_Graphics.GL_DEPTH_BUFFER_BIT);
						// 重置模型视图矩阵
						C2D_Graphics.glLoadIdentity();
						onPaint_C2D(m_g);
						m_EglHelper.swap();
						loopEnd();
					}
				}
				// 即将关闭
				setLoopState(LOOP_AtEnd);
				/*
				 * 清除EGL资源
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
	 * 设置OpenGL环境参数
	 */
	private void setupGL()
	{
		C2D_Debug.log("@---------------------setupGL");
		// 清除屏幕
		C2D_Graphics.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// 着色模式，默认不需要Smooth
		C2D_Graphics.glShadeModel(C2D_Graphics.GL_FLAT);
		// 初始化深度
		C2D_Graphics.glClearDepthf(0.0f);
		// 不允许深度测试
		C2D_Graphics.glDisable(C2D_Graphics.GL_DEPTH_TEST);
		// 设置深度范围
		// C2D_Graphics.glDepthRangef(-100.0f, 0.0f);
		// 指定深度比较的方法
		// C2D_Graphics.glDepthFunc(C2D_Graphics.GL_LEQUAL);
		// 不允许抖动
		C2D_Graphics.glDisable(C2D_Graphics.GL_DITHER);
		// 不允许光照
		C2D_Graphics.glDisable(C2D_Graphics.GL_LIGHTING);
		// 最好的透视计算
		C2D_Graphics.glHint(C2D_Graphics.GL_PERSPECTIVE_CORRECTION_HINT, C2D_Graphics.GL_NICEST);
	}

	/**
	 * 设置OpenGL视口
	 * 
	 * @param width
	 * @param height
	 */
	private void setGLView(int width, int height)
	{
		C2D_Debug.log("@---------------------setGLView:" + width + "x" + height);
		// 投影变换
		C2D_Graphics.glMatrixMode(C2D_Graphics.GL_PROJECTION);
		// 重置投影矩阵
		C2D_Graphics.glLoadIdentity();
		// 视口变换
		C2D_Graphics.glViewport(0, 0, width, height);
		// 设置透视投影矩阵
		// GLU.gluPerspective(gl10, 45.0f, (float) width / (float) height, 0.1f,
		// 1000.0f);
		// 设置正投影
		// C2D_Graphics.glOrthof(-VIEW_WIDTH/2.0f, VIEW_WIDTH/2.0f,
		// -VIEW_HEIGHT/2.0f,
		// VIEW_HEIGHT/2.0f, -1, 1);
		C2D_Graphics.glOrthof(0, (float) Device_Size.m_width, 0, (float) Device_Size.m_height, -100, 100);

		// 映射坐标系(视口方向朝向+Z轴，只有位于Z轴-100,100深度的物体才会出现，
		// 并且坐标系变成左上角为原点，向右为+X，向下为+Y的坐标系)
		C2D_Graphics.glTranslatef(0, Device_Size.m_height, 0);
		C2D_Graphics.glScalef(1.0f * Device_Size.m_width / User_Size.m_width, -1.0f * Device_Size.m_height / User_Size.m_height, 1.0f);
		// C2D_Graphics.glRotatef(180, 1, 0, 0);
		// C2D_Graphics.glTranslatef(0, 0, -10.0f);
		// 模型变换
		C2D_Graphics.glMatrixMode(C2D_Graphics.GL_MODELVIEW);
		// 重置模型矩阵
		C2D_Graphics.glLoadIdentity();
		// 设置Alpha混合模式
		/*
		 * Alpha混合跟几个因素有关系：混合因子、源色、目标色、当前OpenGL颜色、光照。
		 * 目前我们只讨论无光照的情况，目前的引擎中还没有涉及光照的内容，因此暂不考虑。 总体混色方程是这样：(Rs Sr + Rd Dr, Gs
		 * Sg + Gd Dg, Bs Sb + Bd Db, As Sa + Ad Da)
		 * 混合因子，就是混色方程中每个乘项的后面一项，他们表示了XX色的参与百分比，也就是alpha值
		 * 源色，就是我们即将绘制到屏幕上的颜色，比如某块贴图中的某部分颜色。 目标色，就是屏幕上已经存在的颜色，也可以称之为背景色。
		 * 当前OpenGL颜色，就是使用函数glColor4f或者同类函数设置的当前OpenGL填充颜色。
		 * 
		 * 一、我们看在没有当前OpenGL颜色影响情况下的alpha混合。也就是在进行贴图绘制时，
		 * 总是保证当前的opengl颜色是完全不透明白色，即调用了glColor4f(1.0f,1.0f,1.0f,1.0f);
		 * 
		 * Rs Sr + Rd Dr的意义，就是源红色*源红色因子+目标红色*目标红色因子，其它类推
		 * 其中混合因子的大小是由函数glBlendFunc确定的，比如glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)，
		 * 则，它确定了源红色因子=[源色Alpha]，源绿色因子=[源色Alpha]，源蓝色因子=[源色Alpha]，
		 * 目标红色因子=1-源色Alpha，目标绿色因子=1-源色Alpha，目标蓝色因子=1-源色Alpha。
		 * 
		 * 这里的[源色Alpha]，只所以加上括号，是因为它实际上并不是真正的源色Alpha，也即不是真正的贴图某个像素点处的Alpha值。
		 * 而是经过运算的。运算的方法是AsSa，也就是，这也就是As Sa + Ad Da的意义。
		 * 这个如果单纯看起来，只是计算了一个复合Alpha值。如果等到一切颜色计算完毕，计算这个复合Alpha值还有什么意义？
		 * 因此，实际上，这个多项式并不会一起计算，而是被拆开在计算前面的颜色过程中发生作用。也就是计算[源色Alpha]， 和[目标色Alpha]。
		 * 
		 * 假如我们使用方程glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)，那么就指定了Sa=真实源色Alpha 那么源红色因子 = [源色Alpha] = AsSa
		 * = 真实源色Alpha*真实源色Alpha
		 * 也指定了Da=1-真实源色Alpha，那么目标红色因子=AdDa=1.0*(1-真实源色Alpha)
		 * 因为对于目标色来说，已经不存在透明度的概念，所以皆为1.0，这也是下面的目标色和最终颜色都没有写alpha值的原因
		 * 
		 * 例子1，源色是（0,148,205，0.5），目标色是（255,255,255），那么最终颜色是（127,164,179） 127 = 0
		 * *0.5*0.5 + 255*1.0*(1-0.5); 164 = 148*0.5*0.5 + 255*1.0*(1-0.5); 179
		 * = 205*0.5*0.5 + 255*1.0*(1-0.5);
		 * 
		 * 例子2，源色是（0,148,205，0.4），目标色是（255,255,255），那么最终颜色是（153,177,186） 153 = 0
		 * *0.4*0.4 + 255*1.0*(1-0.4); 177 = 148*0.4*0.4 + 255*1.0*(1-0.4); 186
		 * = 205*0.4*0.4 + 255*1.0*(1-0.4);
		 * 
		 * 再假如我们使用方程glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)，那么就指定了Sa=1.0
		 * 那么源红色因子 = [源色Alpha] = AsSa = 真实源色Alpha*1.0
		 * 同样也指定了Da=1-真实源色Alpha，那么目标红色因子=AdDa=1.0*(1-真实源色Alpha)
		 * 
		 * 例子1，源色是（0,148,205，0.5），目标色是（255,255,255），那么最终颜色是（127,201,230） 127 = 0
		 * *0.5*1.0 + 255*1.0*(1-0.5); 201 = 148*0.5*1.0 + 255*1.0*(1-0.5); 230
		 * = 205*0.5*1.0 + 255*1.0*(1-0.5);
		 * 
		 * 例子2，源色是（0,148,205，0.4），目标色是（255,255,255），那么最终颜色是（153,212,235） 153 = 0
		 * *0.4*1.0 + 255*1.0*(1-0.4); 212 = 148*0.4*1.0 + 255*1.0*(1-0.4); 235
		 * = 205*0.4*1.0 + 255*1.0*(1-0.4);
		 * 
		 * 当使用其他的glBlendFunc方程参数时，与之类似。实际上Photoshop中的半透明正是采用 glBlendFunc(GL_ONE,
		 * GL_ONE_MINUS_SRC_ALPHA)这种方式叠加的，也是我们正常概念中的半透明混色。
		 * 而不是glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)，如果采用后者。我们会看到，在一块
		 * 纯白背景上面，绘制50%半透明，却发现图像变灰了的结果，因为这种方式，会降低画面的亮度，绘画 叠加的越多，就会降低越多。
		 * 
		 * 二、接下来，我们看glColor4f或者其他函数，设置的openGL当前颜色如何影响我们的混色结果。
		 * 假如我们使用方程glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)，
		 * 那么就指定了As=真实源色Alpha，Ad=1-真实源色Alpha
		 * 实际上当前OpenGL颜色相当于一个滤波器，我们使用小数来表示更容易看出其意义。
		 * 假设当前，我们设置glColor4f(1.0f,0.5f,0.5f,0.3f);
		 * 那么相当于即在混色之前，就针对源色添加了一层过滤，我们假定使用(Cr,Cg,Cb,Ca)来表示
		 * 
		 * 例子1，源色是（0,148,205，0.5），目标色是（255,255,255），那么最终颜色是（217,223,225)
		 * 如何得到最终值，我们来进行计算。现在已经确定As=0.5，Ad = 1.0
		 * 首先，计算Sa，根据GL_SRC_ALPHA，Sa=As=0.5，再因为过滤是针对RGBA所有项的，因此现在的源色
		 * Alpha也会被过滤值所影响。现在的Ca=0.3，因此Sa=0.15。也即，当前情况下Sa=As*Ca=0.5*0.3=0.15。
		 * 根据GL_ONE_MINUS_SRC_ALPHA，现在的Da=1.0-Sa=0.85，
		 * 
		 * 现在计算公式发生了一些变化： 比如红色计算， Rf = Rs*Sr + Rd*Dr;现在变成了 Rf = Rs*Sr*Cr +
		 * Rd*Dr;其中Cr表示过滤因子。 再机上使用方程glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA)， Sr = As*Sa; Dr = Ad*Da; 由此，我们可以得出： Rf
		 * =Rs*As*Sa*Cr+Rd* Ad*Da;
		 * Rs表示源红色分量，As表示源Alpha分量，Rd表示目标红色分量，Ad表示目标Alpha分量，这些都是定值。
		 * Sa和Da已经如前进行计算，Cr也由于glColor4f(1.0f,0.5f,0.5f,0.3f);给出。因此，红色如此计算:
		 * 
		 * 217 = 0 *0.5*0.15*1.0 + 255*1.0*0.85; 223 = 148*0.5*0.15*0.5 +
		 * 255*1.0*0.85; 225 = 205*0.5*0.15*0.5 + 255*1.0*0.85;
		 * 
		 * 因此，总结来说，如果在贴图时，如果想这个滤波不能起到作用，我们应该设置当前OpenGL颜色为纯白色。
		 * 这样，Sa=As*1.0f，Sa不会受到影响，各个颜色分量在计算时，也会因为混合因子乘以1.0而保持因子原值。
		 * 而如果，此时我们要绘制一个白色贴图，则可以让滤波起作用，使用不同的GL颜色，获得最终不同颜色的贴图。
		 */
		// 允许颜色混合
		C2D_Graphics.glEnable(C2D_Graphics.GL_BLEND);
		// 设置颜色混合模式
		C2D_Graphics.glBlendFunc(C2D_Graphics.GL_SRC_ALPHA, C2D_Graphics.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * 获取OpenGL的配置 系统将根据返回的配置表来设置OpenGL绘图模式， 默认使用高效的配置，不使用深度缓存，使用565而非888颜色模式，
	 * 使用EGL1.0版本，当某些机器不支持565格式，可以去掉开始的三行。 int[] configSpec = {
	 * EGL10.EGL_RED_SIZE, 5, EGL10.EGL_GREEN_SIZE, 6, EGL10.EGL_BLUE_SIZE, 5,
	 * EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
	 * 如果需要这样做的话，应该重写这个方法，并按照以上描述返回一个数组。
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
	 * 返回当前屏幕的SurfaceHolder
	 * 
	 * @return SurfaceHolder
	 */
	public SurfaceHolder getSurfaceHolder()
	{
		return mHolder;
	}

	// ===================系统响应===================
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
	 * 系统函数
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
	 * 系统函数
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
	 * 系统函数
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

	// ===================平台特有成员变量===================
	private static Semaphore sEglSemaphore = new Semaphore(1);
	private boolean m_sizeChanged;
	private SurfaceHolder mHolder;
	private GLThread loopThread;
}
