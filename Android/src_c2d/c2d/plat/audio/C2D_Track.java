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
	/** ״̬���Ƽ��� */
	private static final String ST_Names[]=new String[]
	{
		"IDLE","INITED","PREPARED","STARTED",
		"PLAYEND","STOPPED","PAUSED","RELASED",
		"ERROR"
	};
	/**
	 * ��ȡָ������״̬������
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
	/** ��ǰ����ѭ�� */
	private int m_loop = 0;
	/** ��ǰ������ */
	private MediaPlayer m_player;
	/** λ�ڵ������� */
	private C2D_TrackGroup m_group;
	/** ����״̬ */
	private int m_state = ST_IDLE;
	/** ��Դ�Ƿ��Ѿ����� */
	private boolean m_isLoaded = false;
	/** ������һ�ο�ʼ���ŵ�ʱ�� */
	long m_playBegin;

	public C2D_Track(C2D_TrackGroup group)
	{
		m_group = group;
	}

	/**
	 * ���в���
	 * 
	 * @return �Ƿ�ɹ�����
	 */
	boolean play()
	{
		if (m_group == null)
		{
			return false;
		}
		// ��ȡ��ǰ����Ƶ״̬
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
		// �����µĲ�����
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
		// ����
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
		// ����
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
	 * �Ƿ��ڿ���״̬��������������
	 * 
	 * @return �Ƿ����
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
	 * �Ƿ��ڲ���״̬
	 * 
	 * @return �Ƿ����ڲ���
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
	 * ֹͣ����
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
	 * ��ͣ����
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
	 * �ָ��������ţ�ֻ�д�����ͣ״̬�������ſ��Իָ����Ѿ�ֹͣ�Ĳ���ָ�
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
	 * �����Ƿ���Դ�Ѿ�����
	 * 
	 * @param isLoaded
	 *            �Ƿ��Ѿ�����
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
			C2D_Debug.logDebug("ע�����죬Ŀǰ�Ѿ�ռ�õ�������->"+C2D_TrackManager.LoadedAll);
		}
		else
		{
			C2D_TrackManager.LoadedAll--;
			if (m_group != null)
			{
				m_group.m_loadedCount--;
			}
			C2D_Debug.logDebug("ע�����죬Ŀǰ�Ѿ�ռ�õ�������->"+C2D_TrackManager.LoadedAll);
		}
		// C2D_MiscUtil.log("[infor]","playingTotal->"+C2D_TrackManager.LoadedAll);
	}

	/**
	 * ���õ�ǰ�Ĳ���״̬
	 * 
	 * @param state
	 *            ����״̬
	 */
	private void setState(int state)
	{
		m_state = state;
//		C2D_MiscUtil.logC2D("sound "+m_group.getName()+" state:"+getStateName(state));
	}

	/**
	 * ��ȡ����״̬�����Ƿ����ڲ���
	 * 
	 * @return ����״̬
	 */
	public int getState()
	{
		return m_state;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public MediaPlayer getPlayer()
	{
		return m_player;
	}

	/**
	 * ��ȡ��ǰ����ѭ��
	 * 
	 * @return ��ǰ����ѭ��
	 */
	public int getAudioLoop()
	{
		return m_loop;
	}

	/**
	 * ���õ�ǰ����ѭ��
	 * 
	 * @param ��ǰ����ѭ��
	 */
	void setLoop(int loop)
	{
		m_loop = loop;
	}

	/**
	 * ����ָ��������������������Ϊ��ǰ����
	 * 
	 * @param volumn
	 *            ������С
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
	 * �����Ž���
	 * @param player
	 */
	public void onCompletion(MediaPlayer player)
	{
		C2D_Debug.logC2D("onCompletion:"+player.getCurrentPosition()+",time:"+C2D_System.getTimeNow());
		onPlayerEnd(player);
	}
	/**
	 * �����Ž���
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
	 *  ���Թرջ���״̬�����죬�Ի�ø����������Դ
	 * @return �Ƿ�ر�
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