package c2d.plat.audio;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import c2d.lang.app.C2D_App;
import c2d.lang.math.C2D_Math;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.C2D_System;
import c2d.lang.util.debug.C2D_Debug;

class C2D_Track extends C2D_Object implements OnCompletionListener
{
	protected static final int ST_IDLE = 0;
	protected static final int ST_INITED = ST_IDLE + 1;
	protected static final int ST_PREPARED = ST_INITED + 1;
	protected static final int ST_STARTED = ST_PREPARED + 1;
	protected static final int ST_PLAYEND = ST_STARTED + 1;
	protected static final int ST_STOPPED = ST_PLAYEND + 1;
	protected static final int ST_PAUSED = ST_STOPPED + 1;
	protected static final int ST_RELASED = ST_PAUSED + 1;
	protected static final int ST_ERROR = ST_RELASED + 1;
	/** 状态名称集合 */
	private static final String ST_Names[]=new String[]
	{
		"IDLE","INITED","PREPARED","STARTED",
		"PLAYEND","STOPPED","PAUSED","RELASED",
		"ERROR"
	};
	/**
	 * 获取指定播放状态的名称
	 * @param state
	 * @return
	 */
	protected static final String getStateName(int state)
	{
		if(state>=0&&state<ST_Names.length)
		{
			return ST_Names[state];
		}
		return null;
	}
	/** 当前声音循环 */
	private int m_loop = 0;
	/** 当前播放器 */
	private MediaPlayer m_player;
	/** 位于的音轨组 */
	private C2D_TrackGroup m_group;
	/** 播放状态 */
	private int m_state = ST_IDLE;
	/** 资源是否已经加载 */
	private boolean m_isLoaded = false;
	/** 音轨上一次开始播放的时间 */
	long m_playBegin;

	public C2D_Track(C2D_TrackGroup group)
	{
		m_group = group;
	}

	/**
	 * 进行播放
	 * 
	 * @return 是否成功播放
	 */
	boolean play()
	{
		if (m_group == null)
		{
			return false;
		}
		// 获取当前的音频状态
		if (m_player != null)
		{
			if (m_state == ST_STARTED)
			{
				return false;
			}
			if (m_state == ST_RELASED)
			{
				m_player = null;
			}
		}
		// 创建新的播放器
		if (m_player == null)
		{
			try
			{
				C2D_Debug.log("create sound:" + m_group.getName());
				android.content.res.AssetManager am = C2D_App.getApp().getAssets();
				android.content.res.AssetFileDescriptor fd = am.openFd(m_group.getName());
				m_player = new MediaPlayer();
				m_player.reset();
				setState(ST_IDLE);
				m_player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
				setState(ST_INITED);
				fd.close();
				m_player.setOnCompletionListener(this);
			}
			catch (Exception ex)
			{
				close();
				C2D_Debug.logErr("[load sound error:" + m_group.getFullName() + "]"+ ex.getMessage());
				ex.printStackTrace();
			}
		}
		// 加载
		if (m_state == ST_INITED || m_state == ST_STOPPED)
		{
			try
			{
				m_player.prepare();
				setLoaded(true);
				setState(ST_PREPARED);
			}
			catch (Exception e)
			{
				C2D_Debug.logDebug("--> exception:"+e.getMessage());
				e.printStackTrace();
			}
		}
		// 播放
		if (m_state == ST_PREPARED || m_state == ST_PAUSED || m_state == ST_PLAYEND)
		{
			try
			{
				setVolume(C2D_Audio.getVolume());
				m_playBegin=System.currentTimeMillis();
				m_player.start();
//				C2D_Debug.logC2D("playing audio --> " + m_group.getName());
				setState(ST_STARTED);
			}
			catch (Exception e)
			{
				C2D_Debug.logC2D("play sound error:" + e.getMessage());
				e.printStackTrace();
				close();
			}
		}
		return m_state == ST_STARTED;
	}

	/**
	 * 是否处于空闲状态，可以启动播放
	 * 
	 * @return 是否空闲
	 */
	public boolean isIdle()
	{
		if (m_player == null || (m_state != ST_STARTED))
		{
			return true;
		}
		return false;
	}

	/**
	 * 是否处于播放状态
	 * 
	 * @return 是否正在播放
	 */
	public boolean isPlaying()
	{
		if (m_player != null && (m_state == ST_STARTED))
		{
			return true;
		}
		return false;
	}

	/**
	 * 停止声音
	 */
	public void stop()
	{
		if (m_player != null && m_state == ST_STARTED)
		{
			try
			{
				m_loop = 0;
				m_player.stop();
				setLoaded(false);
				setState(ST_STOPPED);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			if (m_group != null)
			{
				C2D_Debug.logC2D("audio stopped:" + m_group.getFullName());
			}
		}
	}

	/**
	 * 暂停声音
	 */
	public void pause()
	{
		if (m_player != null && m_state == ST_STARTED)
		{
			try
			{
				m_player.pause();
				setState(ST_PAUSED);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			if (m_group != null)
			{
				C2D_Debug.logC2D("audio stopped:" + m_group.getFullName());
			}
		}
	}

	/**
	 * 恢复声音播放，只有处于暂停状态的声音才可以恢复，已经停止的不会恢复
	 */
	public void resume()
	{
		if (m_player != null && m_state == ST_PAUSED)
		{
			play();
		}
		if (m_group != null)
		{
			C2D_Debug.logC2D("audio started:" + m_group.getFullName());
		}
	}

	public void close()
	{
		if (m_player != null && m_state != ST_RELASED)
		{
			try
			{
				setLoaded(false);
				m_player.release();
				setState(ST_RELASED);
			}
			catch (Exception ex)
			{
				C2D_Debug.logErr("close audio:" + ex.getMessage());
				ex.printStackTrace();
			}
			if (m_group != null)
			{
				C2D_Debug.logDebug("audio closed:" + m_group.getFullName());
			}
			m_player = null;
		}
	}

	/**
	 * 设置是否资源已经加载
	 * 
	 * @param isLoaded
	 *            是否已经加载
	 */
	public void setLoaded(boolean isLoaded)
	{
		if (isLoaded == m_isLoaded)
		{
			return;
		}
		m_isLoaded = isLoaded;
		if (m_isLoaded)
		{
			C2D_TrackManager.LoadedAll++;
			if (m_group != null)
			{
				m_group.m_loadedCount++;
			}
			C2D_Debug.logDebug("注册音轨，目前已经占用的音轨数->"+C2D_TrackManager.LoadedAll);
		}
		else
		{
			C2D_TrackManager.LoadedAll--;
			if (m_group != null)
			{
				m_group.m_loadedCount--;
			}
			C2D_Debug.logDebug("注销音轨，目前已经占用的音轨数->"+C2D_TrackManager.LoadedAll);
		}
		// C2D_MiscUtil.log("[infor]","playingTotal->"+C2D_TrackManager.LoadedAll);
	}

	/**
	 * 设置当前的播放状态
	 * 
	 * @param state
	 *            播放状态
	 */
	private void setState(int state)
	{
		m_state = state;
//		C2D_MiscUtil.logC2D("sound "+m_group.getName()+" state:"+getStateName(state));
	}

	/**
	 * 获取播放状态，即是否正在播放
	 * 
	 * @return 播放状态
	 */
	public int getState()
	{
		return m_state;
	}

	/**
	 * 获取播放器
	 * 
	 * @return 播放器
	 */
	public MediaPlayer getPlayer()
	{
		return m_player;
	}

	/**
	 * 获取当前播放循环
	 * 
	 * @return 当前播放循环
	 */
	public int getAudioLoop()
	{
		return m_loop;
	}

	/**
	 * 设置当前播放循环
	 * 
	 * @param 当前播放循环
	 */
	void setLoop(int loop)
	{
		m_loop = loop;
	}

	/**
	 * 设置指定播放器的声音音量成为当前音量
	 * 
	 * @param volumn
	 *            声音大小
	 */
	void setVolume(int volumn)
	{
		if (m_player == null || m_state < ST_PREPARED)
		{
			return;
		}
		try
		{
			volumn = C2D_Math.limitNumber(volumn, 0, 100);
			float level = volumn / 100.0f;
			m_player.setVolume(level, level);
		}
		catch (Exception e)
		{
			C2D_Debug.log("setPlayerVolume:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 处理播放结束
	 * @param player
	 */
	public void onCompletion(MediaPlayer player)
	{
		C2D_Debug.logC2D("onCompletion:"+player.getCurrentPosition()+",time:"+C2D_System.getTimeNow());
		onPlayerEnd(player);
	}
	/**
	 * 处理播放结束
	 * @param player
	 */
	void onPlayerEnd(MediaPlayer player)
	{
		if (!m_isLoaded || m_state == ST_PAUSED)
		{
			return;
		}
		try
		{
			if (getAudioLoop() >= 1)
			{
				setLoop(getAudioLoop() - 1);
			}
			if (getAudioLoop() != 0)
			{
				player.seekTo(0);
				m_playBegin=System.currentTimeMillis();
				player.start();
				C2D_Debug.logC2D("restart " + m_group.getFullName());
			}
			else
			{
				setState(ST_PLAYEND);
			}
		}
		catch (Exception ex)
		{
			C2D_Debug.logErr("playerUpdate" + ex.getMessage());
			ex.printStackTrace();
		}
	}
	public void onRelease()
	{
		close();
		setState(ST_ERROR);
		m_player = null;
	}

	/**
	 *  尝试关闭缓冲状态的音轨，以获得更多的音轨资源
	 * @return 是否关闭
	 */
	public boolean tryCloseBuffered()
	{
		if (m_player != null  && m_state != ST_STARTED)
		{
			close();
			return true;
		}
		return false;
	}
}