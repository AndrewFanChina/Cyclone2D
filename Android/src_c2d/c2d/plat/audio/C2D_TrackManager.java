package c2d.plat.audio;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.lang.util.C2D_Misc;
import c2d.mod.C2D_Consts;

public class C2D_TrackManager
{
	/** ��ǰͬʱ���ŵ������� */
	static int LoadedAll = 0;
	/** ���в����� */
	private Hashtable m_trackGroups = new Hashtable();
	/**
	 * ֹͣ����
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
	 * ָֹͣ������������
	 * @param audio ��������
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
	 * �ر�ָ������������
	 * @param audio ��������
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
	 * ��ͣ����
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
	 * �ָ�������������
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
	 * �ر���������
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
	 * ����ͳһ��������С
	 * @param volume ������С
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
	 * ��������
	 * 
	 * @param trackName
	 *            ��Ƶ�ļ�����
	 * @param loop ��Ƶ����ѭ��
	 * @return �Ƿ�ɹ�����
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
		//����������Ѿ����ڣ���ֱ��ʹ�ã������ڽ�����������
		if (m_trackGroups.containsKey(trackName))
		{
			group = (C2D_TrackGroup) m_trackGroups.get(trackName);
		}
		else
		{
			group=new C2D_TrackGroup(this,trackName);
			m_trackGroups.put(trackName, group);
		}
		//��ȡ����
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
	 * ��ȡ��ǰȫ���Ѿ����ص�������
	 * 
	 * @return ��ǰȫ�����ڲ��ŵ�������
	 */
	public static int TotalLoaded()
	{
		return LoadedAll;
	}
	/**
	 * ���Թر���Щ����״̬�����죬�Ի�ø����������Դ
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
		//���û�йر��κ�һ�����壬Ϊ�˽�����⣬��ǿ�ƹر����ڲ��ŵ�����
		if(closedNum==0&&gMaxTrack!=null)
		{
			gMaxTrack.tryClosePrePlaying();
		}
	}
	/**
	 * �鿴ĳ����Ƶ�Ƿ���ĳЩ�������ڲ���
	 * @param trackName
	 * @return �Ƿ����ڲ���
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
