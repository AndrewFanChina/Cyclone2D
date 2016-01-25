package c2d.plat.audio;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.lang.util.C2D_Misc;
import c2d.mod.C2D_Consts;

public class C2D_TrackManager
{
	/** 当前同时播放的声音数 */
	static int LoadedAll = 0;
	/** 所有播放器 */
	private Hashtable m_trackGroups = new Hashtable();
	/**
	 * 停止声音
	 */
	void stopAll()
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				group.stopAll();
			}
		}
	}
	/**
	 * 停止指定的声音播放
	 * @param audio 声音名称
	 */
	void stopAudio(String audio)
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null&&group.getName().equals(audio))
			{
				group.stopAll();
				break;
			}
		}
	}
	/**
	 * 关闭指定的声音播放
	 * @param audio 声音名称
	 */
	void closeAudio(String audio)
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null&&group.getName().equals(audio))
			{
				group.closeAll();
				break;
			}
		}
	}
	/**
	 * 暂停声音
	 */
	void pauseAll()
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				group.pauseAll();
			}
		}
	}
	/**
	 * 恢复所有声音播放
	 */
	void resumeAll()
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				group.resumeAll();
			}
		}
	}

	/**
	 * 关闭所有声音
	 */
	void closeAll()
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				group.closeAll();
			}
		}
		m_trackGroups.clear();
	}

	/**
	 * 设置统一的声音大小
	 * @param volume 声音大小
	 */
	void setVolumeAll(int volume)
	{
		Enumeration er = m_trackGroups.elements();
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				group.setVolumeAll(volume);
			}
		}
	}

	/**
	 * 播放声音
	 * 
	 * @param trackName
	 *            音频文件名称
	 * @param loop 音频播放循环
	 * @return 是否成功播放
	 */

	boolean playTrack(String trackName, int loop)
	{
		int id = trackName.lastIndexOf('.');
		if (C2D_Consts.STR_SOUND_POSTFIX != null && id >= 0)
		{
			trackName = C2D_Misc.replace(trackName, trackName.substring(id, trackName.length()),
					C2D_Consts.STR_SOUND_POSTFIX);
		}
		C2D_TrackGroup group = null;
		//如果音轨组已经存在，则直接使用，不存在将创建音轨组
		if (m_trackGroups.containsKey(trackName))
		{
			group = (C2D_TrackGroup) m_trackGroups.get(trackName);
		}
		else
		{
			group=new C2D_TrackGroup(this,trackName);
			m_trackGroups.put(trackName, group);
		}
		//获取音轨
		C2D_Track track = group.getIdleTrack();
		if(track==null)
		{
			return false;
		}
		track.setLoop(loop);
		boolean result=track.play();
		return result;
	}

	/**
	 * 获取当前全部已经加载的音轨数
	 * 
	 * @return 当前全部正在播放的音轨数
	 */
	public static int TotalLoaded()
	{
		return LoadedAll;
	}
	/**
	 * 尝试关闭那些缓冲状态的音轨，以获得更多的音轨资源
	 */
	public void tryCloseBuffered()
	{
		Enumeration er = m_trackGroups.elements();
		int closedNum=0;
		C2D_TrackGroup gMaxTrack=null;
		int playinNum=0;
		while (er.hasMoreElements())
		{
			C2D_TrackGroup group = (C2D_TrackGroup) er.nextElement();
			if(group!=null)
			{
				if(group.tryCloseBuffered())
				{
					closedNum++;
				}
				else
				{
					int num = group.getPlayingNum();
					if(playinNum<num)
					{
						gMaxTrack = group;
						playinNum=num;
					}
				}
			}
		}
		//如果没有关闭任何一个缓冲，为了解决问题，将强制关闭早期播放的音轨
		if(closedNum==0&&gMaxTrack!=null)
		{
			gMaxTrack.tryClosePrePlaying();
		}
	}
	/**
	 * 查看某个音频是否有某些音轨正在播放
	 * @param trackName
	 * @return 是否正在播放
	 */
	public boolean isMusicPlaying(String trackName)
	{
		int id = trackName.lastIndexOf('.');
		if (C2D_Consts.STR_SOUND_POSTFIX != null && id >= 0)
		{
			trackName = C2D_Misc.replace(trackName, trackName.substring(id, trackName.length()),
					C2D_Consts.STR_SOUND_POSTFIX);
		}
		if (!m_trackGroups.containsKey(trackName))
		{
			return false;
		}
		C2D_TrackGroup group = (C2D_TrackGroup) m_trackGroups.get(trackName);
		int trackNum = group.getPlayingNum();
		return trackNum>0;
	}
}
