package c2d.frame.com.view;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.util.debug.C2D_Debug;

public class C2D_FloatInforView extends C2D_View
{
	/** ������ʾ���ı��� */
	private C2D_TextBox m_infor;
	/** ��ʾ���ı���ɫ */
	public int m_txtColor;
	/** ��ʾ�ı�����ʼY����ٷֱ� */
	private float m_startYP=30;
	/** ��ʾ�ı��Ľ���Y����ٷֱ� */
	private float m_endYP=10;
	/** ��ʾ�ı��ƶ����ٶ� */
	private float m_speedY=-1;
	/** ��ʾ�ı��Ľ���Y���� */
	private float m_endY;
	
	public C2D_FloatInforView(int txtColor)
	{
		m_txtColor=txtColor;
	}
	/**
	 * ��ʼ��֮ǰ����Ҫ��ӵ�������
	 */
	public void initFloatView()
	{
		if(m_infor==null && m_parentNode!=null)
		{
			this.setSize(m_parentNode.getWidth(),m_parentNode.getHeight());
			m_infor = new C2D_TextBox();
			addChild(m_infor,1);
			m_infor.setColor(m_txtColor);
			m_infor.setVisible(false);
			m_infor.setToParentCenter();
			m_infor.Events_Update().add(new C2D_Event_Update()
			{
				protected boolean doEvent(C2D_Widget carrier)
				{
					if (m_infor.getVisible())
					{
						m_infor.setPosBy(0, m_speedY);
						if (m_infor.getY() < m_endY)
						{
							m_infor.setVisible(false);
						}
					}
					return false;
				}
			});
			m_endY=m_endYP*getHeight()/100;
		}
	}
	public void showInofr(String infor)
	{
		showInofr(infor,m_startYP,m_endYP);
	}
	public void showInofr(String infor,float ySP,float yEP)
	{
		m_startYP=ySP;
		m_endYP=yEP;
		m_endY=m_endYP*getHeight()/100;
		if (m_infor != null)
		{
			m_infor.setVisible(true);
			m_infor.setText(infor);
			m_infor.setPosTo(getWidth()/2, m_startYP*getHeight()/100);
		}
		C2D_Debug.log(infor);
	}
}
