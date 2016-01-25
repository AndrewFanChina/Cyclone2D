package c2d.frame.com.text;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_SizeI;
import c2d.mod.C2D_Consts;
import c2d.plat.font.C2D_FontChar;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_Graphics;

/**
 * �ı�����ؼ�������Ҫ�ڹ����ʱ��ָ��һ�����壬�����ָ���� ��ʹ��Ĭ�ϵ���������-ϵͳ���͡��򵥷���еȴ�С����
 * ����Ҫָ����ʾ����ɫ���ı����ؼ����Զ�������ֵĳߴ硣 �ı��п��Լ��뻻�з�'\n'ʵ�ֻ��С�
 * 
 * @author AndrewFan
 */
public class C2D_TextBox extends C2D_Paragraph
{
	/** �ı������� */
	private C2D_TextFont m_font;
	/** �ı���ɫ */
	private int m_color;
	/** �ı��߿���ɫ */
	private C2D_Color m_borderColor;//����δʵ�֡�
	/** �洢ÿ���ı��Ļ��棬�Զ����� */
	private String[] m_textRows;
	/** ���ݱ߾� */
	private C2D_SizeI m_margin;
	/** �о� */
	private int m_rowGap;

	/**
	 * �����Զ���С�Ķ����ı���
	 */
	public C2D_TextBox()
	{
		setDefaultFont();
	}
	/**
	 * ����ָ����С���ı��� ʹ��Ĭ�ϵ�����������ϵͳ���ͣ��򵥷���еȴ�С��
	 * @param limitSize ���ƴ�С
	 */
	public C2D_TextBox(C2D_SizeI limitSize)
	{
		setDefaultFont();
		if(limitSize!=null)
		{
			m_limitW = limitSize.m_width;
			m_limitH = limitSize.m_height;
		}
	}
	/**
	 * ����ָ����С���ı��� ʹ��Ĭ�ϵ�����������ϵͳ���ͣ��򵥷���еȴ�С��
	 * 
	 * @param limitW
	 *            ҳ��Լ�����
	 * @param limitH
	 *            ҳ��Լ���߶�
	 */
	public C2D_TextBox(int limitW, int limitH)
	{
		setDefaultFont();
		m_limitW = limitW;
		m_limitH = limitH;
	}

	/**
	 * �����Ե�����ʾ���ı��򣬼�ʹ��Ĭ�������Լ�Ĭ��������и���Ϊҳ��߶ȡ� ʹ��Ĭ�ϵ�����������ϵͳ���ͣ��򵥷���еȴ�С��
	 * ������ָ�����п�rowWidth��Ϊҳ���ȡ�Ĭ������ĸ߶���Ϊҳ��߶� ��������޸����壬��Ҫ�������ô�С��
	 * 
	 * @param limitW
	 *            Լ���п�
	 */
	public C2D_TextBox(int limitW)
	{
		setDefaultFont();
		m_limitW = limitW;
		m_limitH = m_font.getFontHeight();
	}

	/**
	 * �����Ե�����ʾ���ı��򣬼�ʹ��ָ�������Լ�������и���Ϊҳ��߶ȡ� ʹ��Ĭ�ϵ�����������ϵͳ���ͣ��򵥷���еȴ�С��
	 * ������ָ�����п�rowWidth��Ϊҳ���ȡ�ʹ������ĸ߶���Ϊҳ��߶� ��������޸����壬��Ҫ�������ô�С��
	 * 
	 * @param limitW
	 *            Լ���п�
	 * @param font
	 *            ָ��������
	 */
	public C2D_TextBox(int limitW, C2D_TextFont font)
	{
		setFont(font);
		m_limitW = limitW;
		m_limitH = m_font.getFontHeight();
	}

	/**
	 * ����ָ����С���ı��� ʹ��Ĭ�ϵ�����������ϵͳ���ͣ��򵥷���еȴ�С��ָ���ı�����
	 * 
	 * @param text
	 *            �ı�����
	 * @param limitW
	 *            ҳ��Լ�����
	 * @param limitH
	 *            ҳ��Լ���߶�
	 */
	public C2D_TextBox(String text, int limitW, int limitH)
	{
		this(limitW, limitH);
		setText(text);
	}

	/**
	 * ��ȡ�ı���ɫ
	 * 
	 * @return �ı���ɫ
	 */
	public int getColor()
	{
		return m_color;
	}

	/**
	 * �����ı���ɫ
	 * 
	 * @param color
	 *            �ı���ɫ
	 */
	public void setColor(int color)
	{
		this.m_color = color;
		layoutChanged();
	}

	/**
	 * �����ı���ɫ
	 * 
	 * @param color
	 *            �ı���ɫ
	 */
	public void setColor(C2D_Color color)
	{
		if (color == null)
		{
			return;
		}

		this.m_color = color.getColor();
		layoutChanged();
	}

	/**
	 * �����ı��߿���ɫ
	 * 
	 * @param borderColor
	 *            �ı���ɫ
	 */
	public void setBorderColor(C2D_Color borderColor)
	{
		if (borderColor == null)
		{
			return;
		}

		m_borderColor = borderColor;
		layoutChanged();
	}
	/**
	 * �������ݱ߾�
	 * @param mx x����߾�
	 * @param my y����߾�
	 */
	public void setMargin(int mx, int my)
	{

		if (m_margin == null)
		{
			m_margin = new C2D_SizeI();
		}
		m_margin.setValue(mx, my);
		refreshCharBuffer();
		layoutChanged();
	}

	public void setRowGap(int rowGap)
	{
		if (m_rowGap == rowGap)
		{
			return;
		}

		m_rowGap = rowGap;
		refreshCharBuffer();
		layoutChanged();
	}
	/**
	 * ��õ�ǰʹ�õ����壬ע���޸ķ��ض��󲻻�Ӱ������ؼ��е����ݡ� �����Ҫ�޸ģ������ٴε���setFont(C2D_TextFont)
	 * 
	 * @return ��ǰʹ�õ�����
	 */
	public C2D_TextFont getFont()
	{
		return m_font;
	}

	/**
	 * ����������
	 * 
	 * @param font
	 *            ������
	 */
	public void setFont(C2D_TextFont font)
	{
		m_font = font;
		refreshCharBuffer();
		layoutChanged();
	}
	/**
	 * ����Ĭ������
	 */
	public void setDefaultFont()
	{
		C2D_TextFont defFont = null;
		defFont = C2D_TextFont.getDefaultFont();
		setFont(defFont);
	}
	/**
	 * ���ƽڵ�
	 * 
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (m_font == null || stage == null || !m_visible)
		{
			return;
		}
		drawTexts(g);
		paintFocus(g,m_PageWidth, m_PageHeight);
	}

	/**
	 * �����ı��ؼ������в���
	 * 
	 * @param font
	 *            �ı�����
	 * @param text
	 *            �ı�����
	 * @param color
	 *            �ı���ɫ
	 * @param anchor
	 *            ê��
	 */
	public void setParameters(C2D_TextFont font, String text, int color, int anchor)
	{
		m_font = font;
		m_text = text;
		if (m_text != null && m_font != null)
		{
			m_PageWidth = m_font.stringWidth(m_text);
			m_PageHeight = m_font.getFontHeight();
		}
		m_color = color;
		m_anchor = anchor;
		layoutChanged();
	}

	/** ���ڼ����ı�����Ķ�̬���� */
	private static C2D_Array bufferArray = new C2D_Array();

	/**
	 * ˢ���ı�����
	 */
	protected void refreshCharBuffer()
	{
		if (m_font == null)
		{
			return;
		}
		int textLen = 0;
		if (m_text != null)
		{
			textLen = m_text.length();
			m_font.loadChars(m_text);
		}
		// �����ַ�������ռ�ݳߴ�
		bufferArray.removeAllElements();
		int mx = 0;
		int my = 0;
		if (m_margin != null)
		{
			mx = m_margin.m_width;
			my = m_margin.m_height;
		}
		float widthRow = mx;
		float widthMax = 0;
		int heightMax = m_font.getFontHeight()+m_rowGap;
		boolean nextRow = false;
		boolean expWidth = false;
		StringBuffer bufferRow = new StringBuffer();
		for (int i = 0; i < textLen; i++)
		{
			char c = m_text.charAt(i);
			if (c == '\n')
			{
				if (!expWidth)
				{
					nextRow = true;
				}
				expWidth = false;
			}
			else
			{
				bufferRow.append(c);
				widthRow = mx + m_font.stringWidth(bufferRow.toString());
				expWidth = false;
				if(i+1<textLen)
				{
					char cNext = m_text.charAt(i+1);
					int len=bufferRow.length();
					bufferRow.append(cNext);
					if ( mx + m_font.stringWidth(bufferRow.toString()) > m_limitW)
					{
						nextRow = true;
						expWidth = true;
					}
					bufferRow.setLength(len);
				}
	
			}
			// ���д���
			if (nextRow || i + 1 >= textLen)
			{
				// �洢��ǰ��
				bufferArray.addElement(bufferRow.toString());
				bufferRow.setLength(0);
				// �л�����һ��
				if (widthRow > widthMax)
				{
					widthMax = widthRow;
				}
				widthRow = mx;
				if (i < textLen - 1)
				{
					heightMax += m_font.getFontHeight()+m_rowGap;
				}
				nextRow = false;
			}
		}
		if (widthRow > widthMax)
		{
			widthMax = widthRow;
		}
		// �洢�ı�����
		m_textRows = new String[bufferArray.size()];
		for (int i = 0; i < m_textRows.length; i++)
		{
			m_textRows[i] = (String) bufferArray.elementAt(i);
		}
		bufferArray.removeAllElements();
		// ˢ�³ߴ�
		m_PageWidth = C2D_Math.ceil(widthMax);
		m_PageHeight = heightMax;
		// ��������ʾ�������ͳߴ�
		if (m_PageHeight > m_limitH && (m_font.getFontHeight() + m_rowGap > 0))
		{
			m_pageRow = (int)(m_limitH - my) / (m_font.getFontHeight() + m_rowGap);
			m_PageHeight = m_pageRow * m_font.getFontHeight();
		}
		else
		{
			m_pageRow = m_textRows.length;
		}
		m_totalRow = m_textRows.length;
		m_currentScroll = 0;
	}
	/**
	 * ��ȡ��ǰ�ַ����±�Ϊid���ַ��Ŀ��
	 * @param id �ַ��±�
	 * @return �ַ����
	 */
	public float getCharW(int id)
	{
		if(m_text==null|| id<0||id>=m_text.length())
		{
			return 0;
		}
		char c = m_text.charAt(id);
		C2D_FontChar clip = m_font.getFontChar(c);
		float widthW = 0;
		if (clip != null)
		{
			widthW = clip.advanceX;
		}
		return widthW;
	}

	/**
	 * �����ı�
	 */
	private void drawTexts(C2D_Graphics g)
	{
		if (m_font == null)
		{
			return;
		}
		float mx = 0;
		float my = 0;
		if (m_margin != null)
		{
			mx = m_margin.m_width;
			my = m_margin.m_height;
		}
		try
		{
			float _w = m_PageWidth;
			float _h = m_PageHeight;
			float x_offset_clip = 0;
			float y_offset_clip = 0;
			// ����ƫ��
			if ((C2D_Consts.LEFT & m_anchor) != 0)
			{
				x_offset_clip = 0;
			}
			else if ((C2D_Consts.HCENTER & m_anchor) != 0)
			{
				x_offset_clip = -_w / 2;
			}
			else if ((C2D_Consts.RIGHT & m_anchor) != 0)
			{
				x_offset_clip = -_w;
			}
			// ����ƫ��
			if ((C2D_Consts.TOP & m_anchor) != 0)
			{
				y_offset_clip = 0;
			}
			else if ((C2D_Consts.VCENTER & m_anchor) != 0)
			{
				y_offset_clip = -_h / 2;
			}
			else if ((C2D_Consts.BOTTOM & m_anchor) != 0)
			{
				y_offset_clip = -_h;
			}
			float xBegin = m_xToTop + x_offset_clip;
			float yBegin = m_yToTop + y_offset_clip;
			float x = xBegin;
			float y = yBegin;
			// �����ı����򱳾�
			if (m_textBgColor != null)
			{
				g.fillRect(x, y, m_PageWidth, m_PageHeight, m_textBgColor.getColor(), 0);
			}
			// �����ı�
			for (int i = m_currentScroll; i < m_currentScroll + m_pageRow; i++)
			{
				m_font.paintString(m_textRows[i], x + mx, y + my, m_color, 0, 0);
				y += m_font.getRowHeight() + m_rowGap;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onRelease()
	{
		super.onRelease();
		m_font = null;
		m_textRows = null;
	}
}
