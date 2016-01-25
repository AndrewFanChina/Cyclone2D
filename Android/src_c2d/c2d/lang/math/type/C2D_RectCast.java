package c2d.lang.math.type;
/**
 * 引擎内部用于优化切块的数据类
 * @author AndrewFan
 *
 */
public class C2D_RectCast
{
	public short m_allClips[];//源数据
	public int m_dataID;//源数据ID
	public int m_x,m_y,m_w,m_h;
	public int m_transFlags;
	public C2D_RectCast(short all_clips[],int id)
	{
		m_allClips=all_clips;
		m_dataID=id;
		m_x=all_clips[m_dataID+1];
		m_y=all_clips[m_dataID+2];
		m_w=all_clips[m_dataID+3];
		m_h=all_clips[m_dataID+4];
	}
	public void mark(int transFlag)
	{
		m_transFlags|=1<<transFlag;
	}

}