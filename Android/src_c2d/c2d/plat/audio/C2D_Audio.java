package c2d.plat.audio;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ������ 
 * ��J2me��˵��IPTV��������ע���ʽ. ���˻����в���8KHz�����ʡ�8bit������ȡ���������Wav��ʽ
 * ��Ϊ�����в���8KHz�����ʡ�16bit������ȡ���������Wav��ʽ��
 * ��Android��˵������ʹ��mp3��ogg�ȳ�����Ч��
 */
public class C2D_Audio
{
	private static int m_volume = 50; // ��ǰ������С
	/** �������ͬʱ���ŵ�����������һЩJ2me��������Ҫ�������ã����绪��������ֻ������һ������ */
	public static int Max_Track = 30;
	/** ���������*/
	private static C2D_TrackManager m_tackManager;
	/**
	 * ��鲢����ָ���������������ָ������Ƶ���ڲ��ţ�������˴β��ţ�������������
	 * 
	 * @param name
	 *            ��Ƶ�ļ���
	 * @param loop
	 *            ����ѭ��������-1��������ѭ����0�Ƿ���>1��ʾѭ������
	 */
	public static final void checkPlayAudio(String name, int loop)
	{
		if(!isAudioPlaying(name))
		{
			playAudio(name, loop);
		}
	}
	/**
	 * ����ָ��������
	 * 
	 * @param name
	 *            ��Ƶ�ļ���
	 * @param loop
	 *            ����ѭ��������-1��������ѭ����0�Ƿ���>1��ʾѭ������
	 */
	public static final void playAudio(String name, int loop)
	{
		if (loop == 0)
		{
			return;
		}
		if(m_tackManager==null)
		{
			m_tackManager=new C2D_TrackManager();
		}
		if (name != null)
		{
			m_tackManager.playTrack( name, loop);
		}
	}
	/**
	 * ����ָ��������
	 * @param name
	 *            ��Ƶ�ļ���
	 * @return 
	 *           �Ƿ����ڲ�������
	 */
	public static final boolean isAudioPlaying(String name)
	{
		if(m_tackManager==null)
		{
			return false;
		}
		return m_tackManager.isMusicPlaying(name);
	}
	/**
	 * ��ͣ��������,ʹ���������ʱ����������ֹͣ�����ǲ����ͷ���Դ��
	 * ���е����콫�����Ϊ��ͣ״̬�����ǵ���resumeAudio����Щ
	 * ���첻���ٴα����á������ʱ�����˵�������ͣ����ͬ����������
	 * ��ô�����������µ����������ţ���������ͣ�����첻�䡣�Ȼָ���
	 * ����ͬ������ʱ����"��ȡ"�Ӷϵ�������ţ������ڻ������Դ����
	 * �������ٶȡ�J2me���ܲ���Ӷϵ㲥�š�
	 */
	public static final void pauseAudio()
	{
		if(m_tackManager!=null)
		{
			m_tackManager.pauseAll();
		}
	}
	/**
	 * ֹͣ��������,ʹ���������ʱ����������ֹͣ�����ǲ����ͷ���Դ��
	 * ���ٴβ�����ͬ������ʱ������ͷ�������ţ����ǻ�����ڻ������Դ��
	 * ���������ٶȡ�
	 */
	public static final void stopAudio()
	{
		if(m_tackManager!=null)
		{
			m_tackManager.stopAll();
		}
	}
	/**
	 * ָֹͣ�������Ĳ���,ʵ������һ����������Ϊ��ʱ������ĳ��ͬ����Чͬʱ�������������ϡ�
	 * ʹ���������ʱ����������ֹͣ�����ǲ����ͷ���Դ�����ٴβ�����ͬ������ʱ������ͷ�������ţ�
	 * ���ǻ�����ڻ������Դ�����������ٶȡ�
	 * @param audio
	 */
	public static final void stopAudio(String audio)
	{
		if(m_tackManager!=null)
		{
			m_tackManager.stopAudio(audio);
		}
	}
	/**
	 * �ر�ָ�������Ĳ���,ʵ������һ����������Ϊ��ʱ������ĳ��ͬ����Чͬʱ�������������ϡ�
	 * ʹ���������ʱ����������رա�
	 * @param audio
	 */
	public static final void closeAudio(String audio)
	{
		if(m_tackManager!=null)
		{
			m_tackManager.closeAudio(audio);
		}
	}
	/**
	 * �ָ���Щ֮ǰ����ͣ��������Ŭ�����ϴ���ͣ�Ķϵ��������
	 * ��J2me���ܲ��������Ӷϵ㲥�ţ�
	 * 
	 */
	public static final void resumeAudio()
	{
		if(m_tackManager!=null)
		{
			m_tackManager.resumeAll();
		}
	}

	/**
	 * �ر���������,ʹ���������ʱ����������ֹͣ���ţ�������Դ���ͷţ�
	 * ���ٴβ�������ʱ������Ҫ����������Դ����������
	 */
	public static final void closeAudio()
	{
		stopAudio();
		if(m_tackManager!=null)
		{
			m_tackManager.closeAll();
		}
	}

	/**
	 * ����ͳһ����������
	 * 
	 * @param volume
	 *            �µ�������С
	 */
	public static final void setVolume(int volume)
	{
		try
		{
			if (volume < 0)
			{
				volume = -volume;
			}
			if (volume > 100)
			{
				volume = 100;
			}
			m_volume = volume;
			if(m_tackManager!=null)
			{
				m_tackManager.setVolumeAll(m_volume);
			}
		}
		catch (Exception e)
		{
			C2D_Debug.log("setAudioLevel:"+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��ǰ������С
	 * 
	 * @return ������С
	 */
	public static final int getVolume()
	{
		return m_volume;
	}


}
