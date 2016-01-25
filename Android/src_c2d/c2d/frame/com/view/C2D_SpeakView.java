package c2d.frame.com.view;


import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.com.text.C2D_TextBox;
import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.script.C2D_ScriptExcutor;
import c2d.mod.script.C2D_Thread;
import c2d.plat.font.C2D_TextFont;
/**
 * �Ի�����ͼ
 * @author AndrewFan
 *
 */
public class C2D_SpeakView extends C2D_ViewUtil
{
	private C2D_TextBox m_speakTxt;
	private C2D_ViewUtil m_viewTop;
	private C2D_ViewUtil m_viewBottom;
	private C2D_View m_bfv;
	//û����ʾ�ı�ʱ����������һ��ʱ���ж�ضԻ�����
	int m_TxtTick;
	float m_TxtTickMax=10;
	//�Ƿ�������ʾ�Ի�
	boolean m_shown=false;
	/**
	 * ��ʼ��״̬��ͼ
	 * 
	 * @param hH
	 *            ���µ����ز��ָ߶�
	 */
	public void init(float hH)
	{
		removeAllChild();
		if(m_parentNode==null)
		{
			return;
		}
		float pW=m_parentNode.getWidth();
		float pH=m_parentNode.getHeight();
		setSize(pW, pH);
		m_TxtTickMax=hH;
		//������ͼ[z:1]
		m_viewTop = addView(0, 0, 1, pW, hH, 0);
		m_viewTop.setBGColor(0x0);
		//���ε�ɫ[vt:z:1]
		m_viewTop.addRectBox(0, hH-1, 1, pW, 1, 0, 0x666666);
		
		//�ײ���ͼ[z:2]
		m_viewBottom = addView(0, pH-hH, 2, pW, hH, 0);
		m_viewBottom.setBGColor(0x0);
		//���ε�ɫ[vb:z:1]
		m_viewBottom.addRectBox(0, 0, 1, pW, 1, 0, 0x666666);
		
		setVisible(false);
		
	}
	//�����ʾ����
	private void initBfv()
	{
		if(m_bfv==null)
		{
			float w=getWidth()-8;
			int h= C2D_TextFont.getDefaultFont().getFontHeight()*2;
			//������ͼ[vb:z:3]
			m_bfv=new C2D_View();
			m_bfv.setSize(w,h);
			m_bfv.setBGColor(0x0);
			m_viewBottom.addChild(m_bfv,3);
			m_bfv.setPosTo(4, 4);
			//���ֿ�[bfv:z:1]
			m_speakTxt = new C2D_TextBox();
			m_speakTxt.setColor(0xFFFFFF);
			m_speakTxt.setLimitSize(w, h);
			m_bfv.addChild(m_speakTxt,1);
		}
	}
	/**
	 * ж�ضԻ�����
	 */
	private void releaseBfv()
	{
		if(m_bfv!=null)
		{
			removeChild(m_bfv);
			m_bfv.doRelease();
			m_bfv=null;
			m_speakTxt=null;
		}
	}
	public void endSpeak()
	{
		
	}
	public void doSpeak(String text)
	{
		initBfv();
		if (m_speakTxt != null)
		{
			m_speakTxt.setText(text);
			C2D_Debug.logDebug("�Ի�:"+text);
			if(m_TxtTick<1)
			{
				m_TxtTick=1;
			}
			m_shown=true;
			this.setVisible(true);
		}
		getStageAt().releaseKeys();
	}
	/**
	 * �Ի����߼�
	 * @param speakScript �Ի��ű�ִ����
	 * @return �����Ƿ������Ҫ��ʾ�ĶԻ���
	 */
	public boolean logic(C2D_ScriptExcutor speakScript)
	{
		C2D_Stage stage = getStageAt();
		if(stage==null)
		{
			return false;
		}
		if (m_shown)
		{
			//�Ի�������Ч
			if(m_TxtTick<m_TxtTickMax)
			{
				float change=(m_TxtTickMax-m_TxtTick)/2;
				change=C2D_Math.limitNumber(change, 1, m_TxtTickMax-m_TxtTick);
				m_TxtTick+=change;
				m_viewTop.setYTo(m_TxtTick-m_TxtTickMax);
				m_viewBottom.setYTo(getHeight()-m_TxtTick);
				getStageAt().releaseKeys();
			}
			//�жϵ�ǰ�Ի�����
			if (stage.isValidKeyPressed())
			{
				m_shown=false;
				stage.releaseKeys();
				if(speakScript!=null)
				{
					C2D_Thread t = speakScript.getCurrentThread();
					if(t!=null )
					{
						t.setPauseFrame(0);
					}
				}
				return false;
			}
		}
		else
		{
			if(m_TxtTick>0)
			{
				int change=(m_TxtTick)/2;
				change=C2D_Math.limitNumber(change, 1, m_TxtTick);
				m_TxtTick-=change;
				m_viewTop.setYTo(m_TxtTick-m_TxtTickMax);
				m_viewBottom.setYTo(getHeight()-m_TxtTick);
				if(m_TxtTick==0)
				{
					releaseBfv();
					setVisible(false);
				}
			}
		}
		return m_shown;
	}
	
	/**
	 * ����Ƿ���������ʾ�ĶԻ�
	 * @return �Ƿ�������ʾ�Ի�
	 */
	public boolean isSpeaking()
	{
		return m_shown;
	}
}
