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
	 * �򼯺�����һ����Ŀ
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
	 * �����±�id�����Ӧ����Ŀ
	 * @param id ��Ŀ�±�ID
	 * @return ��Ŀ
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
	 * ���ؼ�¼����
	 * @return ��¼����
	 */
	public int size()
	{
		return m_notices.size();
	}
	/**
	 * ��ʾ��¼����
	 */
	public void logDetail()
	{
		C2D_MiscUtil.log("[Infor]","------����"+m_notices.size()+"������-----");
		for(int i=0;i<m_notices.size();i++)
		{
			C2D_NoticeItem item =  (C2D_NoticeItem)m_notices.elementAt(i);
			item.logDetail();
		}
		C2D_MiscUtil.log("[Infor]","-------------------");
	}
	/**
	 * ��ȡ��ǰ���й����У���ĳ������֮��Ĺ���
	 * @param date ����
	 * @return ���漯��
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
