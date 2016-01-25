package c2d.plat.audio;

import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

public class C2D_TrackGroup extends C2D_Object
{
	/** 文件名 */
	private String m_name;
	/** 路径和文件名 */
	private String m_fullName;
	/** 同名音轨的当前播放数 */
	int m_loadedCount;
	/** 音轨集合 */
	private C2D_Array m_trackList;
	/** 管理器 */
	C2D_TrackManager m_trackManager;
	
	public C2D_TrackGroup(C2D_TrackManager manager, String name)
	{
		m_trackManager=manager;
		m_name=name;
		m_fullName=C2D_Consts.STR_RES + C2D_Consts.STR_SOUNDS +m_name;
		m_trackList=new C2D_Array();
	}
	/**
	 * 停止声音
	 */
	void stopAll()
	{
		for (int i = 0; i < m_trackList.size(); i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null )
			{
				track.stop();
			}
		}
	}
	/**
	 * 暂停声音
	 */
	void pauseAll()
	{
		for (int i = 0; i < m_trackList.size(); i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null )
			{
				track.pause();
			}
		}
	}
	/**
	 * 恢复所有声音播放
	 */
	void resumeAll()
	{
		for (int i = 0; i < m_trackList.size(); i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null)
			{
				track.resume();
			}
		}
	}
	/**
	 * 关闭所有声音
	 */
	void closeAll()
	{
		int size=m_trackList.size();
		C2D_Debug.logDebug("try close audio group["+size+"]");
		for (int i = 0; i <size ; i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null)
			{
				C2D_Debug.logDebug("try close audio:"+i);
				track.close();
			}
		}
	}
	/**
	 * 设置统一的声音大小
	 * @param volume 声音大小
	 */
	void setVolumeAll(int volume)
	{
		for (int i = 0; i < m_trackList.size(); i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null)
			{
				track.setVolume(volume);
			}
		}
	}
	/**
	 * 获取空闲的同名音轨，如果当前同名音轨超过了最大允许数目，则忽略此次播放。
	 * @return 同名的音轨
	 */
	public C2D_Track getIdleTrack()
	{
		//鉴别是否已经超过最大音轨限制
		if (C2D_TrackManager.TotalLoaded() >= C2D_Audio.Max_Track)
		{
			m_trackManager.tryCloseBuffered();
		}
		//如果仍然音轨数不足，则返回空
		if (C2D_TrackManager.TotalLoaded() >= C2D_Audio.Max_Track)
		{
			return null;
		}
		//寻找可以重新启动播放的音轨
		if(m_trackList==null)
		{
			m_trackList=new C2D_Array();
		}
		else
		{
			for (int i = 0; i < m_trackList.size(); i++)
			{
				C2D_Track trackI=(C2D_Track)m_trackList.elementAt(i);
				if (trackI != null && trackI.isIdle())
				{
					return trackI;
				}
			}	
		}
		//没有空闲音轨，则创建一个新的音轨
		C2D_Track t = new C2D_Track(this);
		m_trackList.addElement(t);
		return t;
	}
	/**
	 * 获取正在播放的同名音轨数
	 * @return 同名的音轨
	 */
	public int getPlayingNum()
	{
		if(m_trackList==null)
		{
			return 0;
		}
		int num=0;
		for (int i = 0; i < m_trackList.size(); i++)
		{
			C2D_Track trackI=(C2D_Track)m_trackList.elementAt(i);
			if (trackI != null && trackI.isPlaying())
			{
				num++;
			}
		}	
		return num;
	}
	/**
	 * 获取音源路径和文件名
	 * @return 音源路径和文件名
	 */
	public String getFullName()
	{
		return m_fullName;
	}
	/**
	 * 获取音源文件名
	 * @return 音源文件名
	 */
	public String getName()
	{
		return m_name;
	}
	public void onRelease()
	{
		if(m_trackList!=null)
		{
			for (int i = 0; i < m_trackList.size(); i++)
			{
				C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
				if (track != null)
				{
					track.doRelease();
				}
			}
			m_trackList=null;
		}
		m_name=null;
		m_trackManager=null;
	}
	/**
	 * 尝试关闭那些缓冲状态的音轨，以获得更多的音轨资源
	 * @return 是否有被关闭的缓冲
	 */
	public boolean tryCloseBuffered()
	{
		int size=m_trackList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Track track=(C2D_Track)m_trackList.elementAt(i);
			if (track != null)
			{
				if(track.tryCloseBuffered())
				{
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 尝试关闭早期播放的音轨，以获得更多的音轨资源
	 * @return 是否有被关闭的早期播放的音轨
	 */
	public void tryClosePrePlaying()
	{
		int size=m_trackList.size();
		C2D_Debug.logDebug("尝试关闭早期播放音轨，目前已经占用的音轨数->"+C2D_TrackManager.LoadedAll);
		//寻找最先播放的音轨
		C2D_Track track=null;
		long beginTime=0;
		for (int i = 0; i < size; i++)
		{
			C2D_Track trackI=(C2D_Track)m_trackList.elementAt(i);
			if (trackI != null&&trackI.isPlaying())
			{
				if(beginTime ==0 || trackI.m_playBegin< beginTime)
				{
					beginTime=trackI.m_playBegin;
					track = trackI;
				}
			}
		}
		if(track!=null)
		{
			C2D_Debug.logDebug("强制结束音轨："+getFullName());
			track.close();
		}
	}
}
