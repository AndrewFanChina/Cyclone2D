package pay.model.notice;

import java.util.Calendar;
import java.util.Date;

import c2d.util.misc.C2D_MiscUtil;

public class C2D_NoticeItem
{
	public Date m_date_start=new Date();
	public Date m_date_end=new Date();
	public String m_title;
	public String m_content;
	public Date getDate_start()
	{
		return m_date_start;
	}
	/**
	 * ���ÿ�ʼʱ��
	 * @param date_start ��ʼʱ��
	 */
	public void setDate_start(Date date_start)
	{
		if(date_start==null)
		{
			return;
		}
		this.m_date_start.setTime(date_start.getTime());
	}
	/**
	 * ��ÿ�ʼʱ��
	 * @return ʱ��
	 */
	public Date getDate_end()
	{
		return m_date_end;
	}
	/**
	 * ���ý���ʱ��
	 * @param date_end ����ʱ��
	 */
	public void setDate_end(Date date_end)
	{
		if(date_end==null)
		{
			return;
		}
		this.m_date_end.setTime(date_end.getTime());
	}
	/**
	 * ��ù������
	 * @return �������
	 */
	public String getTitle()
	{
		return m_title;
	}
	/**
	 * ���ù������
	 * @param title �������
	 */
	public void setTitle(String title)
	{
		if(title==null)
		{
			return;
		}
		this.m_title = title;
	}
	/**
	 * ��ù�������
	 * @return ��������
	 */
	public String getContent()
	{
		return m_content;
	}
	/**
	 * ���ù�������
	 * @param content ��������
	 */
	public void setContent(String content)
	{
		if(content==null)
		{
			return;
		}
		this.m_content = content;
	}
	/**
	 * ��ʾ��ϸ��Ϣ
	 */
	public void logDetail()
	{
		String infor="��ʼʱ��:"+getTime(m_date_start)+"\n";
		infor+="����ʱ��:"+getTime(m_date_end)+"\n";
		infor+="�������:"+m_title+"\n";
		infor+="��������:"+m_content+"";
		C2D_MiscUtil.log(infor);
	}
	/**
	 * ���ո�ʽ��ȡ�����ַ���
	 * @param date ����
	 * @return
	 */
	private String getTime(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String s=c.get(Calendar.YEAR)
				+"/"+(c.get(Calendar.MONTH)+1)
				+"/"+c.get(Calendar.DATE)
				+" "+c.get(Calendar.HOUR_OF_DAY)
				+":"+c.get(Calendar.MINUTE)
				+":"+c.get(Calendar.SECOND);
		return s;
	}
}
