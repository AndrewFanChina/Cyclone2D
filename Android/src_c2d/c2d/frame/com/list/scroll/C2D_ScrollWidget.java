package c2d.frame.com.list.scroll;

import c2d.frame.base.C2D_Widget;


public abstract class C2D_ScrollWidget extends C2D_Widget implements C2D_Scrollable
{
	/** ��ǰ�����ڵ���ID */
	protected int m_currentScroll=-1;
	/** ҳ����ʾ��������ҳ����ʾ������*/
	protected int m_pageRow;	
	/** ȫ�����Թ��������� */
	protected int m_totalRow;
	/** �Ƿ�����ѭ������ ��Ĭ������**/
	protected boolean m_loopScroll=true;
	/**
	 * ��ǰ����
	 * @return �Ƿ�ִ���˹���
	 */
	public boolean scrollFront()
	{
		int total=getTotalScroll();
		if(total<=0)
		{
			return false;
		}
		int nextScroll = m_currentScroll+1;
		if(nextScroll>=total&&!m_loopScroll)
		{
			return false;
		}
		m_currentScroll=(nextScroll)%total;
		layoutChanged();
		return true;
	}
	/**
	 * ��ع���
	 * @return �Ƿ�ִ���˹���
	 */
	public boolean scrollBack()
	{
		int total=getTotalScroll();
		if(total<=0)
		{
			return false;
		}
		int nextScroll = m_currentScroll-1;
		if(nextScroll<0&&!m_loopScroll)
		{
			return false;
		}
		m_currentScroll=(nextScroll+total)%total;
		layoutChanged();
		return true;
	}
	/**
	 * ��õ�ǰ����λ��,��ȫ����������L������λ�ô���[0,L-1]����
	 * @return ��ǰ����λ��
	 */
	public int getCurrentScroll()
	{
		return m_currentScroll;
	}
	/**
	 * ���õ�ǰ����λ��
	 * @param scroll ����λ��
	 */
	public void setCurrentScroll(int scroll)
	{
		if(scroll<0||scroll>getTotalScroll())
		{
			return;
		}
		m_currentScroll=scroll;
		layoutChanged();
	}

	/**
	 * ���ù���λ��
	 */
	public void resetScroll()
	{
		m_currentScroll=0;
		layoutChanged();
	}
	/**
	 * ���ȫ������
	 * @return ȫ��ȫ������
	 */
	public int getTotalRow()
	{
		return m_totalRow;
	}
	/**
	 * �����Ƿ�����ѭ������ 
	 * @param loopScroll �Ƿ�����ѭ������
	 */
	public void setLoopScroll(boolean loopScroll)
	{
		m_loopScroll=loopScroll;
	}
	
}
