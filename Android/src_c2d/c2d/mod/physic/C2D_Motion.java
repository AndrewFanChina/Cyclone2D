package c2d.mod.physic;

import c2d.frame.base.C2D_Widget;
import c2d.lang.obj.C2D_Object;

public class C2D_Motion extends C2D_Object
{
	//�趨ƫ����
	private float m_XOffset;
	private float m_YOffset;
	//���ٶ�
	private float m_a;
	//�����˶�ʱ��
	private int m_timeTotal;
	//��ǰ�˶�ʱ��
	private int m_timeCurrent;
	//�ؼ�
	private C2D_Widget m_widget;
	//��ʼλ��
	private float m_XBegin;
	private float m_YBegin;
	//�Ƿ��˶����
	private boolean m_isOver;
	//��ǰ�˶�ƫ�����ܼ�
	private float m_XOffsetSum;
	private float m_YOffsetSum;
	
	public C2D_Motion(C2D_Widget widget,float offX,float offY,float a,int timeTotal)
	{
		m_widget=widget;
		startMotion(offX,offY,a,timeTotal);
	}
	public C2D_Motion(C2D_Widget widget,int offX,int offY,int a,int timeTotal)
	{
		startMotion(widget,offX,offY,a,timeTotal);
	}
	public void startMotion(C2D_Widget widget,int offX,int offY,int a,int timeTotal)
	{
		m_widget=widget;
		startMotion(offX,offY,a,timeTotal);
	}
	/**
	 * ��������
	 * @param offX
	 * @param offY
	 * @param a
	 * @param timeTotal
	 */
	public void startMotion(float offX,float offY,float a,int timeTotal)
	{
		m_a=(a);
		m_XOffset=(offX);
		m_YOffset=(offY);
		m_timeTotal=timeTotal;
		restart();
	}
	/**
	 * ���¿�ʼ�˶�
	 */
	public void restart()
	{
		if(m_widget!=null)
		{
			m_XBegin=m_widget.getX();
			m_YBegin=m_widget.getY();
		}
		m_XOffsetSum=0;
		m_YOffsetSum=0;
		m_timeCurrent=0;
		m_isOver=false;
	}
	/**
	 * ִ���ƶ����������Ƿ��ƶ����
	 * @param timePassed
	 * @return �Ƿ��ƶ����
	 */
	public boolean doMotion(int timePassed)
	{
		if(m_isOver)
		{
			return true;
		}
		if(m_widget==null||timePassed<=0)
		{
			return false;
		}

		if(m_timeCurrent+timePassed>=m_timeTotal)
		{
			m_widget.setPosTo(m_XOffset +m_XBegin, m_YOffset+m_YBegin);
			m_isOver=true;
		}
		else
		{
			float offsetX,offsetY;
			if(m_a==0)
			{
				offsetX=(m_XOffset*timePassed/m_timeTotal);
				offsetY=(m_YOffset*timePassed/m_timeTotal);

			}
			else
			{
				offsetX=acc_step(m_timeCurrent,m_timeTotal,timePassed,m_XOffset,m_a);
				offsetY=acc_step(m_timeCurrent,m_timeTotal,timePassed,m_YOffset,m_a);
			}
			m_XOffsetSum+=(offsetX);
			m_YOffsetSum+=(offsetY);
			m_widget.setPosTo(m_XOffsetSum+m_XBegin, m_YOffsetSum+m_YBegin);
			m_timeCurrent+=timePassed;
		}
		return m_isOver;
	}
	/**
	 * TODO ����һ�������˶���ָ�����ܾ���L��������ʱ��tt����ǰ��ʱ��tc��
	 * ������ȥ��ʱ��td�ͼ��ٶ�a�����㼴���ƶ��Ĳ���
	 * @param tc ��ǰʱ��
	 * @param tt ������ʱ��
	 * @param td ������ȥ��ʱ��
	 * @param L �ܾ���
	 * @param a ���ٶ�
	 * @return �ƶ�����
	 */
	public float acc_step(float tc,float tt,float td,float L,float a)
	{
		float ld=((L-a*tt*tt/2)/tt+a*tc)*td+a*td*td/2;
		return ld;
	}
	public boolean isOver()
	{
		return m_isOver;
	}
	public void onRelease()
	{
		m_widget=null;
	}
}
