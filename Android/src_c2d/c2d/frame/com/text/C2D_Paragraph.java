package c2d.frame.com.text;

import c2d.frame.com.list.scroll.C2D_ScrollWidget;
import c2d.frame.event.C2D_EventPool_ChangeStrValue;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_GdiGraphics;

public abstract class C2D_Paragraph extends C2D_ScrollWidget
{
	/** ��ʾ���ı� */
	protected String m_text;
	/** ҳ���Լ����� */
	protected float m_limitW = 2048;
	/** ҳ���Լ���߶� */
	protected float m_limitH = 2048;
	/** ҳ��Ŀ�� */
	protected float m_PageWidth;
	/** ҳ��ĸ߶� */
	protected float m_PageHeight;
	/** �ı����򱳾���ɫ */
	protected C2D_Color m_textBgColor;
	/** �¼���-�ı����� */
	protected C2D_EventPool_ChangeStrValue m_Events_value;
	/**
	 * ��ȡ�ı�
	 * 
	 * @return �ı�
	 */
	public String getText()
	{
		return m_text;
	}

	/**
	 * �����ı�
	 * 
	 * @param text
	 */
	public C2D_Paragraph setText(String text)
	{
		if (m_text == text || (m_text != null && m_text.equals(text)))
		{
			return this;
		}

		m_text = text;
		if (m_Events_value != null)
		{
			m_Events_value.onCalled(text);
		}

		refreshCharBuffer();

		return this;
	}

	/**
	 * ����ҳ����
	 * 
	 * @return ҳ��Ŀ��
	 */
	public float getPageWidth()
	{
		return m_PageWidth;
	}

	/**
	 * ����ҳ��߶�
	 * 
	 * @return ҳ��Ŀ�߶�
	 */
	public float getPageHeight()
	{
		return m_PageHeight;
	}

	/**
	 * ��ȡ�ı����򱳾���ɫ
	 * 
	 * @return �ı����򱳾���ɫ
	 */
	public C2D_Color getTxtBgColor()
	{
		return m_textBgColor;
	}

	/**
	 * �����ı����򱳾���ɫ
	 * 
	 * @param color
	 *            �ı����򱳾���ɫ
	 */
	public void setTxtBgColor(C2D_Color color)
	{

		m_textBgColor = color;

	}

	/**
	 * ����ҳ���Լ���ߴ�
	 * 
	 * @param limitW
	 *            ҳ��Լ�����
	 * @param limitH
	 *            ҳ��Լ���߶�
	 */
	public void setLimitSize(float limitW, float limitH)
	{
		m_limitW = limitW;
		m_limitH = limitH;
		refreshCharBuffer();
	}

	/**
	 * ����ҳ���Լ���ߴ�
	 * 
	 * @param limitW
	 *            ҳ��Լ�����
	 */
	public void setLimitWidth(float limitW)
	{
		m_limitW = limitW;
		refreshCharBuffer();
	}

	/**
	 * ����ҳ���Լ���ߴ�
	 * 
	 * @param limitH
	 *            ҳ��Լ���߶�
	 */
	public void setLimitHeight(int limitH)
	{
		m_limitH = limitH;
		refreshCharBuffer();
	}

	protected abstract void refreshCharBuffer();

	/**
	 * ��ȡ��Բ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ�� �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ����
	 * ���β������������Ƿ�ɹ�ȡ�á�
	 * 
	 * @param resultRect
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_PageWidth, m_PageHeight, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * ���ȫ�����Թ������������ı�ֻ�ܽ������й���
	 * 
	 * @return ȫ��ȫ������
	 */
	public int getTotalScroll()
	{
		if (m_pageRow >= m_totalRow)
		{
			return 0;
		}
		return m_totalRow - m_pageRow;
	}
	/**
	 * �ı���ֵ�¼���
	 * 
	 * @return �¼���
	 */
	public C2D_EventPool_ChangeStrValue Events_ChangeValue()
	{
		if (m_Events_value == null)
		{
			m_Events_value = new C2D_EventPool_ChangeStrValue(this);
		}
		return m_Events_value;
	}
	protected float getWidth()
	{
		return m_PageWidth;
	}
	protected float getHeight()
	{
		return m_PageHeight;
	}
	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		m_text = null;
		m_textBgColor = null;
	}
}
