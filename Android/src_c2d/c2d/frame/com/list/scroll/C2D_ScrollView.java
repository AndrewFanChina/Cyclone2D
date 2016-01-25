package c2d.frame.com.list.scroll;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_EventPool_ClickItem;
import c2d.frame.event.C2D_EventPool_Scroll;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_TouchData;
import c2d.lang.math.type.C2D_PointF;

public abstract class C2D_ScrollView extends C2D_View  implements C2D_Scrollable
{
	/** ��ǰ������ڵ���ID */
	protected int m_currentScroll;
	/** ��ǰ������ܵ���ĵ���С��ID */
	protected int m_minScroll=0;
	/** ��ǰ������ܵ���ĵ������ID */
	protected int m_maxScroll=65536;
	/** ������ʽ��Ĭ�ϲ�ȡ��Ŀ����**/
	protected int m_scrollType=C2D_Scrollable.Scrool_Loop;
	/** ҳ���һ�����ڵ���ID*/
	protected int m_firstRow;
	/** �Ƿ���Ҫ������װUI */
	protected boolean m_needRepack=true;
	/** �¼���-����*/
	protected C2D_EventPool_Scroll m_Events_Sroll;
	/** ��ť�¼���*/
	protected C2D_EventPool_ClickItem m_Events_ClickItem;
	/**
	 * ������ͼ���͹�����ͼ
	 * @param partRedraw �Ƿ���þֲ���ͼ
	 */
	public C2D_ScrollView()
	{
	}
	/**
	 * ��ǰ����һ�У�����б����ҳ�������������βʱ����й���л�
	 * @return �Ƿ�ִ��Ҫ������������İ���
	 */
	public boolean scrollFront()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_currentScroll+1;
		if(nextScroll>m_maxScroll)
		{
			return true;
		}
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
			if(nextScroll>=m_firstRow+pageRow||nextScroll>=total)
			{
				C2D_Scene scene= getSceneAt();
				C2D_Stage stage=getStageAt();
				if(scene!=null&&stage!=null)
				{
					if(scene.moveFocus(this, stage.getSingleKey()))
					{
						stage.releaseKeys();
					}
				}
			}
			else
			{
				m_currentScroll = nextScroll;
			}
			refreshItemChange();
			break;
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll>=total)
			{
				return false;
			}
			m_currentScroll = nextScroll;
			if(m_currentScroll>=m_firstRow+pageRow)
			{
				m_firstRow=C2D_Math.min(m_currentScroll, total-pageRow);
				m_needRepack=true;
			}
			else
			{
				refreshItemChange();
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll>=m_firstRow+pageRow||nextScroll>=total)
			{
				m_needRepack=true;
				if(nextScroll>=total)
				{
					m_currentScroll=0;
					m_firstRow=0;
				}
				else
				{
					m_currentScroll=nextScroll;
					m_firstRow=m_currentScroll-pageRow+1;
				}
			}
			else
			{
				m_currentScroll=nextScroll;
				refreshItemChange();
			}
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * ��ع���һ�У�����б����ҳ�������������βʱ����й���л�
	 * @return �Ƿ�ִ��Ҫ������������İ���
	 */
	public boolean scrollBack()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_currentScroll-1;
		if(nextScroll<m_minScroll)
		{
			return true;
		}
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
			if(nextScroll<m_firstRow)
			{
				C2D_Scene scene= getSceneAt();
				C2D_Stage stage=getStageAt();
				if(scene!=null&&stage!=null)
				{
					if(scene.moveFocus(this, stage.getSingleKey()))
					{
						stage.releaseKeys();
					}
				}
			}
			else
			{
				m_currentScroll = nextScroll;
			}
			refreshItemChange();
			break;
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll<0)
			{
				return false;
			}
			m_currentScroll = nextScroll;
			if(m_currentScroll<m_firstRow)
			{
				m_firstRow=m_currentScroll;
				m_needRepack=true;
			}
			else
			{
				refreshItemChange();
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll<m_firstRow)
			{
				m_needRepack=true;
				if(nextScroll<0)
				{
					m_currentScroll=total-1;
					m_firstRow=m_currentScroll-(pageRow-1);
					if(m_firstRow<0)
					{
						m_firstRow=0;
					}
				}
				else
				{
					m_currentScroll=nextScroll;
					m_firstRow=nextScroll;
				}
			}
			else
			{
				m_currentScroll=nextScroll;
				refreshItemChange();
			}
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * ��ǰ����һҳ
	 * @return �Ƿ�ִ���˹���
	 */
	public boolean scrollFrontByPage()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_firstRow+pageRow;
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll<total)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
				m_needRepack=true;
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll<total)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
			}
			else
			{
				m_currentScroll = 0;
				m_firstRow = 0;
			}
			m_needRepack=true;
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * ��ع���һҳ
	 * @return �Ƿ�ִ���˹���
	 */
	public boolean scrollBackByPage()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_firstRow-pageRow;
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll>=0)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
				m_needRepack=true;
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll>=0)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
			}
			else
			{
				m_firstRow= ((total-1)/pageRow)*pageRow;
				m_currentScroll = m_firstRow;
			}
			m_needRepack=true;
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	
	/**
	 * ��õ�ǰ���λ�ã�����ѡ����ID
	 * @return ��ǰ���λ��
	 */
	public int getCurrentScroll()
	{
		return m_currentScroll;
	}
	/**
	 * ��õ�ǰ���λ�ã�����ѡ���е���������Ӧ��Y����
	 * @return Y���꣬�����������
	 */
	public float getCurrentScrollTopY()
	{
		int itemID=m_currentScroll-m_firstRow;
		if(itemID>=0)
		{
			C2D_Widget child = getChildAt(itemID);
			if(child!=null)
			{
				return child.getYToTop();
			}
		}
		return 0;
	}
	/**
	 * ���ָ��topY�����Ӧ����ĿID����������ڣ��򷵻�-1
	 * @return ��ĿID
	 */
	public int getItemIDAtY(float Y)
	{
		int ih = getItemHeight()+getItemGap();
		if(ih<=0)
		{
			return -1;
		}
		C2D_PointF lt =getLeftTop();
		if(lt==null)
		{
			return -1;
		}
		int idT=((int)(Y-lt.m_y))/ih;
		int pageReach=C2D_Math.min(getTotalRow()-1, m_firstRow+getPageRow()-1);
		if(m_firstRow+idT<=pageReach)
		{
			return m_firstRow+idT;
		}
		return -1;
	}
	/**
	 * ���ù���λ��
	 */
	public void resetScroll()
	{
		int orgScroll = m_currentScroll;
		m_currentScroll=0;
		m_firstRow=0;
		m_needRepack=true;
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
		}
	}
	/**
	 * ���õ���ǰѡ�е���
	 * @param value ��ǰѡ�е���
	 */
	public void setCurrentScroll(int value)
	{
		if(value>=0&&value<getTotalRow())
		{
			int orgScroll = m_currentScroll;
			m_currentScroll = value;
			rebuildRowInf();
			m_needRepack=true;
			if(orgScroll!=m_currentScroll)
			{
				callScrollEvent(orgScroll,false);
				callScrollEvent(m_currentScroll,true);
			}
		}
	}
	/**
	 * ���ȫ�����Թ������������б����ȫ��Ŀ����
	 * @return ȫ��ȫ������
	 */
	public int getTotalScroll()
	{
		return getTotalRow();
	}
	/**
	 * ���������¼�����ϵͳ���ã��㲻��ʹ���������
	 * @param itemIndex ���������¼�����ĿID
	 * @param scrollIn true������룬�������ø�����false����ʧȥ����
	 */
	protected void callScrollEvent(int itemIndex,boolean scrollIn)
	{
		if(m_Events_Sroll!=null)
		{
			m_Events_Sroll.onCalled(itemIndex,scrollIn);
		}
	}
	/**
	 * ���½�������Ϣ
	 */
	protected void rebuildRowInf()
	{
		int totalRow=getTotalRow();
		//��������ҳ����ʾ������Լ��
		int m_pageRow=getPageRow();
		if(m_pageRow>totalRow)
		{
			m_pageRow = totalRow;
		}
		//�������Ե�ǰҳ�����е�Լ��
		if(totalRow<=m_pageRow)
		{
			m_firstRow=0;
		}
		else
		{
			m_firstRow=C2D_Math.limitNumber(
					m_firstRow, 0, totalRow-m_pageRow);
		}
		//�������Ե�ǰ����е�Լ��
		if(totalRow>0)
		{
			m_currentScroll = C2D_Math.limitNumber(m_currentScroll,
				0, totalRow-1);
		}
		else
		{
			m_currentScroll=-1;
		}
		//ҳ�����и������е�Ǩ��
		if(totalRow>0)
		{
			if(m_currentScroll<m_firstRow)
			{
				m_firstRow=m_currentScroll;
			}
			if(m_currentScroll>=m_firstRow+m_pageRow)
			{
				m_firstRow=m_currentScroll;
				m_firstRow=C2D_Math.limitNumber(
						m_firstRow, 0, totalRow-m_pageRow);
			}
		}
	}
	/**
	 * ��ù����¼���
	 * @return �����¼���
	 */
	public C2D_EventPool_Scroll Events_Scroll()
	{
		if(m_Events_Sroll==null)
		{
			m_Events_Sroll=new C2D_EventPool_Scroll(this);
		}
		return m_Events_Sroll;
	}
	/**
	 * �����Ŀ����¼���
	 * @return �¼���
	 */
	public C2D_EventPool_ClickItem Events_ClickItem()
	{
		if(m_Events_ClickItem==null)
		{
			m_Events_ClickItem=new C2D_EventPool_ClickItem(this);
		}
		return m_Events_ClickItem;
	}
	/**
	 * ����ʱˢ�±仯����Ŀ
	 */
	protected abstract void refreshItemChange();
	/**
	 * ���ù�������
	 * @param scrollType ��������
	 */
	public void setScrollType(int scrollType)
	{
		m_scrollType = scrollType;
	}
	/**
	 * ��ù�������
	 * @return ��������
	 */
	public int getScrollType()
	{
		return m_currentScroll;
	}
	/**
	 * ��Ӧ�ڿؼ��ϲ����������󰴼���ʵ�ֽ�����ƶ�.
	 * �˷�������ϵͳ���ã���Ӧ�����ⲿ�ֶ����á�
	 * @param stage ���ڳ���
	 */
	protected void processNavigations(C2D_Stage stage)
	{
		if(!m_focused)
		{
			return;
		}
		C2D_Scene scene = getSceneAt();
		if(scene==null)
		{
			return;
		}
		int keyCode=stage.getSingleKey();
		//�����¼���Ӧ
		if(keyCode>=C2D_Device.key_up&&keyCode<=C2D_Device.key_left)
		{
			switch(keyCode)
			{
				case C2D_Device.key_down:
					if(scrollFront())
					{
						stage.releaseKeys();
					}
					break;
				case C2D_Device.key_up:
					if(scrollBack())
					{
					stage.releaseKeys();
					}
					break;
			}
		}
		//�������ҷ���Ľ����¼�
		super.processNavigations(stage);
	}
	/**
	 * ��������İ�ť��Ӧ�� �˷�������ϵͳ���ã���Ӧ�����ⲿ�ֶ����á�
	 * 
	 * @param stage
	 *            ���ڳ���
	 */
	protected void processBtnCalls(C2D_Stage stage)
	{
		if (!m_visible)
		{
			return;
		}
		int keyCode=stage.getSingleKey();
		//����¼���Ӧ
		if(m_currentScroll>=0&&m_currentScroll<getTotalRow())
		{
			if (keyCode >= C2D_Device.key_up && keyCode < C2D_Device.key_other)
			{
				if (m_Events_KeyPress != null)
				{
					m_Events_KeyPress.onCalled(keyCode);
				}
			}
			//����������Ŀ���
			if (m_focused&&keyCode == C2D_Device.key_enter && !anyKeyUsed(keyCode))
			{
				if(m_Events_ClickItem!=null)
				{
					if(m_Events_ClickItem.onCalled(m_currentScroll))
					{
						stage.releaseKeys();
					}
				}
			}
			//����������Ŀ���
			else if (stage != null&&m_focusable)
			{
				// ������������
				C2D_TouchData td = stage.m_curTouchData;
				if(td!=null&&td.m_touchCount>0)
				{
					if(Plat_Tochable && m_focusable)
					{
						C2D_PointF pt= getTouchPoint();
						if(pt!=null)
						{
							int id= getItemIDAtY(pt.m_y);
							if(id>=0 && m_Events_ClickItem!=null)
							{
								if(m_Events_ClickItem.onCalled(id))
								{
									stage.releaseTouchPoints();
								}
							}	
						}
					}
				}
	
			}

		}
	}
	public void setLimitScroll(int min,int max)
	{
		m_minScroll=C2D_Math.max(0, min);
		m_maxScroll=C2D_Math.min(getTotalRow()-1, max);
	}
	/**
	 * ���ȫ������
	 * @return ȫ������
	 */
	public abstract int getTotalRow();
	/**
	 * ���ҳ����ʾ����
	 * @return ҳ����ʾ����
	 */
	public abstract int getPageRow();
	/**
	 * ��ȡ��Ŀ�߶�
	 * 
	 * @return ��Ŀ�߶�
	 */
	public abstract int getItemHeight();
	/**
	 * ��ȡ��Ŀ��ļ��
	 * 
	 * @return ��Ŀ��ļ��
	 */
	public abstract int getItemGap();
	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		if(m_Events_Sroll!=null)
		{
			m_Events_Sroll.doRelease();
			m_Events_Sroll=null;
		}
		if(m_Events_ClickItem!=null)
		{
			m_Events_ClickItem.doRelease();
			m_Events_ClickItem=null;
		}
	}
}
