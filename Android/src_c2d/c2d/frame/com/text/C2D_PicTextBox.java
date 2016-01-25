package c2d.frame.com.text;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * ͼƬ�ı�����ؼ� ����Ҫָ��һ�����ö��󣬰�����ͼƬ�ı�����ʽ�� ���������������������ʾ���ı�����setText����������ʾ���ı���
 * �ı��п��Լ��뻻�з�'\n'ʵ�ֻ��С�
 * 
 * @author AndrewFan
 */
public class C2D_PicTextBox extends C2D_Paragraph
{
	/** ��ǰͼƬ�ı������ö��� */
	private C2D_PTC m_config;
	/**
	 * �洢ÿ���ı���ID���棬�Զ����㣬 -1��ʾ�����ö�����û���ҵ�ӳ���ַ�
	 */
	private short[][] m_textRows;

	/**
	 * ���캯����ָ�����ö���ҳ���Ⱥ͸߶�
	 * 
	 * @param config
	 *            ���ö���
	 * @param limitW
	 *            ҳ��Լ�����
	 * @param limitH
	 *            ҳ��Լ���߶�
	 */
	public C2D_PicTextBox(C2D_PTC config, int limitW, int limitH)
	{
		m_config = config;
		m_limitW = limitW;
		m_limitH = limitH;
	}

	/**
	 * ���캯����ָ�����ö��󣬴˹��캯����ʹ���Զ�ҳ���С
	 * 
	 * @param config
	 *            ���ö���
	 */
	public C2D_PicTextBox(C2D_PTC config)
	{
		m_config = config;
	}

	/**
	 * �������ö��󣬵�������ö����޸�֮�������Ҫ�ؼ�����ˢ�£� ��Ҫ�������������
	 * 
	 * @param config
	 *            ���ö���
	 */
	public void setConfig(C2D_PTC config)
	{
		m_config = config;
		refreshCharBuffer();
		layoutChanged();
	}

	/**
	 * ��ȡ���ö���
	 * 
	 * @return ���ö���
	 */
	public C2D_PTC getConfig()
	{
		return m_config;
	}

	/**
	 * ���ƽڵ�
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (!m_visible || !m_inCamera || stage == null)
		{
			return;
		}
		drawTexts(g);
		paintFocus(g, m_PageWidth, m_PageHeight);
	}

	/** ���ڼ����ı�����Ķ�̬���� */
	private static C2D_Array bufferArray = new C2D_Array();
	private static short bufferRow[] = new short[256];

	/**
	 * ˢ���ı�����
	 */
	protected void refreshCharBuffer()
	{
		if (m_text == null || m_config == null)
		{
			return;
		}
		String charTable = m_config.getChars();
		if (charTable == null)
		{
			return;
		}
		int textLen = m_text.length();

		// �����ַ�������ռ�ݳߴ�
		bufferArray.removeAllElements();
		int widthT = 0;
		int widthMax = 0;
		int heightMax = m_config.m_charH;
		int rowLen = 0;
		boolean nextRow = false;
		boolean expWidth = false;
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
				short id = (short) charTable.indexOf(c);
				bufferRow[rowLen] = id;
				rowLen++;
				widthT += m_config.getCharW(id);
				if (widthT + m_config.m_gapX + m_config.getCharW(id) + (m_config.m_gapX > 0 ? 0 : m_config.m_gapX) > m_limitW)
				{
					nextRow = true;
					expWidth = true;
				}
				else
				{
					widthT += m_config.m_gapX;
					expWidth = false;
				}
			}
			// ���д���
			if (nextRow || i + 1 >= textLen)
			{
				// �洢��ǰ��
				short[] rowI = new short[rowLen];
				System.arraycopy(bufferRow, 0, rowI, 0, rowLen);
				bufferArray.addElement(rowI);
				// �л�����һ��
				rowLen = 0;
				if (widthT > widthMax)
				{
					widthMax = widthT;
				}
				widthT = 0;
				if (i < textLen - 1)
				{
					heightMax += m_config.m_gapY + m_config.m_charH;
				}
				nextRow = false;
			}
		}
		if (widthT > widthMax)
		{
			widthMax = widthT;
		}
		// �洢��������
		m_textRows = new short[bufferArray.size()][];
		for (int i = 0; i < m_textRows.length; i++)
		{
			m_textRows[i] = (short[]) bufferArray.elementAt(i);
		}
		bufferArray.removeAllElements();
		// ˢ�³ߴ�
		m_PageWidth = widthMax;
		m_PageHeight = heightMax;
		// ��������ʾ
		if (m_PageHeight > m_limitH)
		{
			m_pageRow = (int)(m_limitH + m_config.m_gapY) / (m_config.m_gapY + m_config.m_charH);
			m_PageHeight = m_pageRow * (m_config.m_gapY + m_config.m_charH) - m_config.m_gapY;
		}
		else
		{
			m_pageRow = m_textRows.length;
		}
		m_totalRow = m_textRows.length;
		m_currentScroll = 0;
	}

	/**
	 * �����ı�
	 * 
	 * @param visibleRects
	 *            �ɼ�����
	 * @param g
	 */
	private void drawTexts(C2D_Graphics g)
	{
		if (m_config == null)
		{
			return;
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
			for (int i = m_currentScroll; i < m_currentScroll + m_pageRow; i++)
			{
				for (int j = 0; j < m_textRows[i].length; j++)
				{
					short id = m_textRows[i][j];
					if (id >= 0)// ��ͨ�ַ�
					{
						int gapX = m_config.m_gapX;
						if (j == 0)
						{
							gapX = 0;
						}
						C2D_ImageClip charT = m_config.getChar(id);
						if (charT != null)
						{
							charT.draw(x + gapX, y);
						}
					}
					x += m_config.getCharW(id) + m_config.m_gapX;
				}
				x = xBegin;
				y += m_config.m_charH + m_config.m_gapY;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		m_config = null;
		m_textRows = null;
	}
}
