package pay.model.notice;

import java.util.Date;

import c2d.util.math.C2D_Array;
import c2d.util.misc.C2D_MiscUtil;

public class C2D_NoticeSet
{
	private C2D_Array m_notices=new C2D_Array();
	public C2D_NoticeSet()
	{
	}
	/**
	 * 向集合增加一条条目
	 * @param notice
	 */
	public void addItem(C2D_NoticeItem notice)
	{
		if(notice==null)
		{
			return;
		}
		if(m_notices.contains(notice))
		{
			return;
		}
		m_notices.addElement(notice);
	}
	/**
	 * 根据下标id获得相应的条目
	 * @param id 条目下标ID
	 * @return 条目
	 */
	public C2D_NoticeItem getItem(int id)
	{
		if(id<0||id>=m_notices.size())
		{
			return null;
		}
		C2D_NoticeItem item =  (C2D_NoticeItem)m_notices.elementAt(id);
		return item;
	}
	
	/**
	 * 返回记录条数
	 * @return 记录条数
	 */
	public int size()
	{
		return m_notices.size();
	}
	/**
	 * 显示记录内容
	 */
	public void logDetail()
	{
		C2D_MiscUtil.log("[Infor]","------共有"+m_notices.size()+"条公告-----");
		for(int i=0;i<m_notices.size();i++)
		{
			C2D_NoticeItem item =  (C2D_NoticeItem)m_notices.elementAt(i);
			item.logDetail();
		}
		C2D_MiscUtil.log("[Infor]","-------------------");
	}
	/**
	 * 获取当前所有公告中，在某个日期之后的公告
	 * @param date 日期
	 * @return 公告集合
	 */
	public C2D_NoticeSet afterDate(Date date)
	{
		C2D_NoticeSet set=new C2D_NoticeSet();
		for (int i = 0; i < m_notices.size(); i++)
		{
			C2D_NoticeItem nI = (C2D_NoticeItem)m_notices.elementAt(i);
			if(nI.getDate_end().getTime()>date.getTime())
			{
				set.addItem(nI);
			}
		}
		return set;
	}
}
