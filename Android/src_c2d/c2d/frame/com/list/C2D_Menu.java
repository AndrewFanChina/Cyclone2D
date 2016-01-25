package c2d.frame.com.list;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.list.scroll.C2D_ScrollView;

/**
 * �˵��� �˵���һ������Ĺ�����ͼ��������ӵ��ӵ�Ԫ����Ϊ�˵�� ����Ѿ������˲˵���֮����еĹ����¼�������Ҫ���һ�� ���ú�������Ӧ�˵���ı仯��
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Menu extends C2D_ScrollView
{
	public C2D_Menu()
	{
		m_focusable = true;
	}

	protected void refreshItemChange()
	{
		int pageRow = getPageRow();
		for (int i = 0; i < pageRow; i++)
		{
			int itemID = m_firstRow + i;
			C2D_Widget item = getChildAt(i);
			if (item == null)
			{
				continue;
			}
			configItem(item, i, itemID == m_currentScroll);
		}
	}

	/**
	 * ������Ŀ�����˵�����仯ʱ������ô˺����� ����Ҫ�����ڲ�ʵ����Ŀ�������ı仯��
	 * 
	 * @param item
	 *            ��Ŀ���
	 * @param itemIndex
	 *            ��Ŀ�����Ӧ��ID
	 * @param selected
	 *            ��Ŀ�Ƿ�ѡ��
	 */
	public abstract void configItem(C2D_Widget item, int itemIndex, boolean selected);

	/**
	 * �õ�ǰ�ؼ�ӵ�н��㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 ����ǰ�����Ľ���ؼ���������㲻Ӧ���ֶ��������������
	 * @param another ԭ����
	 */
	protected void gotFocus(C2D_Widget another)
	{
		super.gotFocus(another);
		refreshItemChange();
		// ���´���һ�ι����¼�
		callScrollEvent(m_currentScroll, true);
	}

	/**
	 * ʧȥ���㣬����������Զ����ؼ����óɿ���ӵ�н��㡣 �㲻Ӧ���ֶ�������������������Ļ�����ǰҳ�潫ʧȥӵ�н���Ŀؼ�
	 * @param another Ŀ�꽹��
	 */
	protected void lostFocus(C2D_Widget another)
	{
		super.lostFocus(another);
		refreshItemChange();
	}

	public int getTotalRow()
	{
		return getChildCount();
	}

	public int getPageRow()
	{
		return getChildCount();
	}

}
