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
	 * 设置开始时间
	 * @param date_start 开始时间
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
	 * 获得开始时间
	 * @return 时间
	 */
	public Date getDate_end()
	{
		return m_date_end;
	}
	/**
	 * 设置结束时间
	 * @param date_end 结束时间
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
	 * 获得公告标题
	 * @return 公告标题
	 */
	public String getTitle()
	{
		return m_title;
	}
	/**
	 * 设置公告标题
	 * @param title 公告标题
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
	 * 获得公告内容
	 * @return 公告内容
	 */
	public String getContent()
	{
		return m_content;
	}
	/**
	 * 设置公告内容
	 * @param content 公告内容
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
	 * 显示详细信息
	 */
	public void logDetail()
	{
		String infor="开始时间:"+getTime(m_date_start)+"\n";
		infor+="结束时间:"+getTime(m_date_end)+"\n";
		infor+="公告标题:"+m_title+"\n";
		infor+="公告内容:"+m_content+"";
		C2D_MiscUtil.log(infor);
	}
	/**
	 * 按照格式获取日期字符串
	 * @param date 日期
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
