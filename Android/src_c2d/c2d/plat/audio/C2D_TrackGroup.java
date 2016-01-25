package c2d.plat.audio;

import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

public class C2D_TrackGroup extends C2D_Object
{
	/** �ļ��� */
	private String m_name;
	/** ·�����ļ��� */
	private String m_fullName;
	/** ͬ������ĵ�ǰ������ */
	int m_loadedCount;
	/** ���켯�� */
	private C2D_Array m_trackList;
	/** ������ */
	C2D_TrackManager m_trackManager;
	
	public C2D_TrackGroup(C2D_TrackManager manager, String name)
	{
		m_trackManager=manager;
		m_name=name;
		m_fullName=C2D_Consts.STR_RES + C2D_Consts.STR_SOUNDS +m_name;
		m_trackList=new C2D_Array();
	}
	/**
	 * ֹͣ����
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
	 * ��ͣ����
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
	 * �ָ�������������
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
	 * �ر���������
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
	 * ����ͳһ��������С
	 * @param volume ������С
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
	 * ��ȡ���е�ͬ�����죬�����ǰͬ�����쳬�������������Ŀ������Դ˴β��š�
	 * @return ͬ��������
	 */
	public C2D_Track getIdleTrack()
	{
		//�����Ƿ��Ѿ����������������
		if (C2D_TrackManager.TotalLoaded() >= C2D_Audio.Max_Track)
		{
			m_trackManager.tryCloseBuffered();
		}
		//�����Ȼ���������㣬�򷵻ؿ�
		if (C2D_TrackManager.TotalLoaded() >= C2D_Audio.Max_Track)
		{
			return null;
		}
		//Ѱ�ҿ��������������ŵ�����
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
		//û�п������죬�򴴽�һ���µ�����
		C2D_Track t = new C2D_Track(this);
		m_trackList.addElement(t);
		return t;
	}
	/**
	 * ��ȡ���ڲ��ŵ�ͬ��������
	 * @return ͬ��������
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
	 * ��ȡ��Դ·�����ļ���
	 * @return ��Դ·�����ļ���
	 */
	public String getFullName()
	{
		return m_fullName;
	}
	/**
	 * ��ȡ��Դ�ļ���
	 * @return ��Դ�ļ���
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
	 * ���Թر���Щ����״̬�����죬�Ի�ø����������Դ
	 * @return �Ƿ��б��رյĻ���
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
	 * ���Թر����ڲ��ŵ����죬�Ի�ø����������Դ
	 * @return �Ƿ��б��رյ����ڲ��ŵ�����
	 */
	public void tryClosePrePlaying()
	{
		int size=m_trackList.size();
		C2D_Debug.logDebug("���Թر����ڲ������죬Ŀǰ�Ѿ�ռ�õ�������->"+C2D_TrackManager.LoadedAll);
		//Ѱ�����Ȳ��ŵ�����
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
			C2D_Debug.logDebug("ǿ�ƽ������죺"+getFullName());
			track.close();
		}
	}
}
