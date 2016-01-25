package c2d.plat.audio;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 声音类 
 * 对J2me来说在IPTV机顶盒上注意格式. 中兴机顶盒采用8KHz采样率、8bit采样深度、单声道、Wav格式
 * 华为机顶盒采用8KHz采样率、16bit采样深度、单声道、Wav格式。
 * 对Android来说，可以使用mp3、ogg等常用音效。
 */
public class C2D_Audio
{
	private static int m_volume = 50; // 当前声音大小
	/** 最多允许同时播放的声音数，在一些J2me机顶盒上要进行设置，比如华数机顶盒只允许播放一个声音 */
	public static int Max_Track = 30;
	/** 音轨管理器*/
	private static C2D_TrackManager m_tackManager;
	/**
	 * 检查并播放指定的声音，如果有指定的音频正在播放，则放弃此次播放，否则启动播放
	 * 
	 * @param name
	 *            音频文件名
	 * @param loop
	 *            播放循环次数，-1代表无限循环，0非法，>1表示循环次数
	 */
	public static final void checkPlayAudio(String name, int loop)
	{
		if(!isAudioPlaying(name))
		{
			playAudio(name, loop);
		}
	}
	/**
	 * 播放指定的声音
	 * 
	 * @param name
	 *            音频文件名
	 * @param loop
	 *            播放循环次数，-1代表无限循环，0非法，>1表示循环次数
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
	 * 播放指定的声音
	 * @param name
	 *            音频文件名
	 * @return 
	 *           是否正在播放音乐
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
	 * 暂停声音播放,使用这个方法时，声音将会停止，但是不会释放资源。
	 * 所有的音轨将被标记为暂停状态，除非调用resumeAudio，这些
	 * 音轨不会再次被启用。如果此时播放了的与已暂停音轨同名的声音，
	 * 那么将额外启用新的音轨来播放，而保持暂停的音轨不变。等恢复播
	 * 放相同的声音时，会"争取"从断点继续播放，得益于缓存的资源，提
	 * 升播放速度。J2me可能不会从断点播放。
	 */
	public static final void pauseAudio()
	{
		if(m_tackManager!=null)
		{
			m_tackManager.pauseAll();
		}
	}
	/**
	 * 停止声音播放,使用这个方法时，声音将会停止，但是不会释放资源，
	 * 等再次播放相同的声音时，会重头继续播放，但是会得益于缓存的资源，
	 * 提升播放速度。
	 */
	public static final void stopAudio()
	{
		if(m_tackManager!=null)
		{
			m_tackManager.stopAll();
		}
	}
	/**
	 * 停止指定声音的播放,实际上是一组声音，因为此时可能有某个同名音效同时存在若干音轨上。
	 * 使用这个方法时，声音将会停止，但是不会释放资源，等再次播放相同的声音时，会重头继续播放，
	 * 但是会得益于缓存的资源，提升播放速度。
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
	 * 关闭指定声音的播放,实际上是一组声音，因为此时可能有某个同名音效同时存在若干音轨上。
	 * 使用这个方法时，声音将会关闭。
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
	 * 恢复那些之前被暂停的声音，努力从上次暂停的断点继续播放
	 * （J2me可能不能做到从断点播放）
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
	 * 关闭声音播放,使用这个方法时，所有声音停止播放，音轨资源被释放，
	 * 当再次播放声音时，将需要重新载入资源，创建音轨
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
	 * 设置统一的声音音量
	 * 
	 * @param volume
	 *            新的声音大小
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
	 * 获取当前声音大小
	 * 
	 * @return 声音大小
	 */
	public static final int getVolume()
	{
		return m_volume;
	}


}
