package c2d.plat.font;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_GdiImage;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * C2D_TextFont �ַ���������һ������ĳ�������ָ������ۺ��ֺŴ�С���ַ�������
 * ������������ĳЩ�ַ�������ʱ�����Զ�����������л�ȡ�ַ���������Ϣ�� ����������Ӧ���ַ���ͼ��ֻ�м��ع����ַ��ſ���ʹ��--����������ߴ���ߵ���
 * ��ͼ���Ƶ���Ļ���ֺ�һ���趨�����Ĵ�С�ǲ�����ı�ģ������Ż����һϵ��ͬ�� ��С���ַ���ͼ�����ϣ��������ͬ��������۲�����ͬ�ֺŵ��ַ�������ô��Ҫ����
 * ����һ���ַ������� ����ȷ�ĳߴ���ơ���Ⱦ������������Ч��...to be down
 * 
 * @author AndrewFan
 */
public class C2D_TextFont extends C2D_Object
{
	private int m_fontSize; // �ֺ�
	private HashMap<Character, C2D_FontChar> charSet = new HashMap<Character, C2D_FontChar>(); // �ַ���
	C2D_FontFace m_fontFace; // �������
	private Paint m_paintInner;
	private int m_fontHeight;// �ı��߶�
	private int m_baseLine; // ����λ��
	private int m_rowHeight;// �и�(�����ı��ͼ��)

	/** Ĭ�������С */
	public static int DEF_SIZE = 32;
	private static C2D_TextFont m_defFont = null;
	private static C2D_FontLib m_defFontLib = C2D_FontLib.loadFromFamily(null);

	/**
	 * �����ַ���
	 * 
	 * @param fontFace
	 *            �ַ����
	 * @param font_size
	 *            �ַ���С
	 */
	public C2D_TextFont(C2D_FontFace fontFace, int font_size)
	{
		m_fontFace = fontFace;
		m_fontSize = font_size;
		m_paintInner = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.LINEAR_TEXT_FLAG);//| Paint.DITHER_FLAG
		m_paintInner.setTextSize(m_fontSize);
		m_paintInner.setTypeface(m_fontFace.m_face);
		m_paintInner.setARGB(0xFF, 0xFF, 0xFF, 0xFF);
		FontMetrics fm = m_paintInner.getFontMetrics();
		m_fontHeight = (int) Math.ceil(fm.descent - fm.ascent);
		m_baseLine = (int) Math.ceil(-fm.top);
		m_rowHeight = (int) Math.ceil(fm.bottom - fm.top);
		loadChars(" ");
	}

	/**
	 * ��ȡĬ�ϵ�����
	 * 
	 * @return Ĭ������
	 */
	public static C2D_TextFont getDefaultFont()
	{
		if (m_defFont == null)
		{
			C2D_FontFace face = m_defFontLib.createFace(C2D_FontLib.STYLE_NORMAL);
			m_defFont = new C2D_TextFont(face, DEF_SIZE);
		}
		return m_defFont;
	}

	/**
	 * ����Ĭ������
	 * 
	 * @param defaultFont
	 *            Ĭ������
	 */
	public static void setDefaultFont(C2D_TextFont defaultFont)
	{
		if (m_defFont != null)
		{
			m_defFont.doRelease(m_defFont);
		}
		m_defFont = defaultFont;
	}

	/**
	 * ��������ַ���(������������9���ַ�����Ч�ʺܵ�).
	 * 
	 * @param s
	 *            String ����
	 * @param x
	 *            int x����
	 * @param y
	 *            int y����
	 * @param fColor
	 *            int ǰ��ɫ
	 * @param bColor
	 *            int ��ɫ
	 * @param anchor
	 *            int ê��
	 */
	public void drawStrokeString(String s, int x, int y, int fColor, int bColor, int anchor)
	{

		drawString(s, x - 1, y - 1, bColor, anchor);
		drawString(s, x, y - 1, bColor, anchor);
		drawString(s, x + 1, y - 1, bColor, anchor);
		drawString(s, x - 1, y, bColor, anchor);
		drawString(s, x + 1, y, bColor, anchor);
		drawString(s, x - 1, y + 1, bColor, anchor);
		drawString(s, x, y + 1, bColor, anchor);
		drawString(s, x + 1, y + 1, bColor, anchor);
		drawString(s, x, y, fColor, anchor);
	}

	/**
	 * ���ƴ����½���Ӱ���ַ���(������������2���ַ���).
	 * 
	 * @param s
	 *            String ����
	 * @param x
	 *            int x����
	 * @param y
	 *            int y����
	 * @param fColor
	 *            int ǰ��ɫ
	 * @param bColor
	 *            int ��ɫ
	 * @param anchor
	 *            int ê��
	 */
	public void drawShadeString(String s, int x, int y, int fColor, int bColor, int anchor)
	{
		drawString(s, x + 1, y + 1, bColor, anchor);
		drawString(s, x, y, fColor, anchor);
	}

	private static C2D_RectF rectForDS = new C2D_RectF();

	/**
	 * �����ַ���.
	 * 
	 * @param s
	 *            String ����
	 * @param x
	 *            int x����
	 * @param y
	 *            int y����
	 * @param color
	 *            int ������ɫ
	 * @param anchor
	 *            byte ê��
	 */
	public void drawString(String s, int x, int y, int color, int anchor)
	{
		C2D_GdiGraphics.computeLayoutRect(x, y, stringWidth(s), getFontHeight(), anchor, C2D_GdiGraphics.TRANS_NONE, rectForDS);
		x = (int) rectForDS.m_x;
		y = (int) rectForDS.m_y;
		paintString(s, x, y, color, 0, 0);
	}

	/**
	 *  ����ˮƽ���󣬴�ֱ�Ի��߶���ķ�ʽ������ָ�����ַ�����ָ�����꣬
	 *  ����ݻ��з����л��У�xOffset,yOffset��ˮƽ�ʹ�ֱ���Ӽ��
	 * @param s
	 *            String ���Ƶ��ַ�
	 * @param x
	 *            int ���Ͻ�X����
	 * @param y
	 *            int ���Ͻ�Y����
	 * @param color
	 *            int ������ɫ
	 * @param xOffset
	 * @param yOffset
	 */
	public void paintString(String s, float x, float y, int color, int xOffset, int yOffset)
	{
		float xStart = x;
		float yStart = y;
		C2D_FontChar fontChar = charSet.get(' ');
		for (int i = 0; i < s.length(); i++)
		{
			char key = s.charAt(i);
			if (key == '\n')
			{
				yStart += m_rowHeight + yOffset;
				xStart = x;
			}
			fontChar = charSet.get(key);
			if (fontChar == null)
			{
				continue;
			}
			C2D_ImageClip clip = fontChar.charClip;
			if (clip != null)
			{
				clip.draw(xStart + fontChar.bearingX, yStart + m_baseLine + fontChar.bearingY, 0, color);
				xStart += fontChar.advanceX + xOffset;
			}

		}
	}

	/**
	 * �����ַ����Ŀ��
	 * 
	 * @param text
	 *            ��Ҫ�������ַ���
	 * @return �ַ����
	 */
	public int stringWidth(String text)
	{
		return (int) Math.ceil(m_paintInner.measureText(text));
	}
	/**
	 * ����ַ���ȣ����ݵ�ǰ����������.
	 * 
	 * @param c
	 *            String �ַ�
	 * @return int ���ؿ��
	 */
	public int charWidth(char c)
	{
		return stringWidth(c+"");
	}
	/**
	 * ��ĳ���������װ���ַ��������Ƿ�ɹ�����
	 * @param str Ҫװ�ص��ַ�������
	 * @return �Ƿ�װ�سɹ�
	 */
	public boolean loadChars(String str[])
	{
		if(str==null)
		{
			return false;
		}
		for (int i = 0; i < str.length; i++)
		{
			loadChars(str[i]);
		}
		return true;
	}
	/**
	 * ��ĳ���������װ���ַ��������Ƿ�ɹ�����  ����Ҫ�����Ż������������ַŵ�һ��ͼƬ�ϡ�
	 * @param str Ҫװ�ص��ַ���
	 * @return �Ƿ�װ�سɹ�
	 */
	public boolean loadChars(String str)
	{
		if (m_fontFace == null)
		{
			return false;
		}
		int width = 0;
		int height = 0;
		float bearingX = 0;
		float bearingY = 0;
		float advanceX = 0;
		char text[] = new char[1];
		Rect bounds = new Rect();
		// ��ʼ����
		for (int i = 0; i < str.length(); i++)
		{
			char charI = str.charAt(i);
			if (charSet.containsKey(charI))
			{
				continue;
			}
			text[0] = charI;
			m_paintInner.getTextBounds(text, 0, 1, bounds);
			bearingX = bounds.left;
			bearingY = bounds.top;
			advanceX = (int) Math.ceil(m_paintInner.measureText(charI + ""));
			width = bounds.right - bounds.left;
			height = bounds.bottom - bounds.top;
			if (width == 0)
			{
				width = (int) advanceX;
			}
			if (height == 0)
			{
				height = (int) m_fontHeight;
			}
			/* ������ͼ */
			C2D_GdiImage gdiImage = C2D_GdiImage.createImage(width, height);
			Canvas canvas = gdiImage.getGraphics().getInner();
			canvas.drawText(String.valueOf(charI), -bearingX, -bearingY, m_paintInner);
			C2D_Image charImage = C2D_Image.createImage(gdiImage, true);
			// �洢��ͼ
			C2D_FontChar fontChar = new C2D_FontChar();
			fontChar.advanceX = advanceX;
			fontChar.bearingX = bearingX;
			fontChar.bearingY = bearingY;
			fontChar.charImage = charImage;
			fontChar.charClip = new C2D_ImageClip(charImage);
			fontChar.charClip.transHadler(this);
			charSet.put(charI, fontChar);
		}
		return true;
	}

	/**
	 * �������߶�
	 * 
	 * @return ����߶�
	 */
	public int getFontHeight()
	{
		return m_fontHeight;
	}

	/**
	 * ����и�
	 * 
	 * @return �и�
	 */
	public int getRowHeight()
	{
		return m_rowHeight;
	}

	/**
	 * ��ȡָ���ַ��Ķ�Ӧ�����ַ�
	 * 
	 * @param charValue
	 *            ָ�����ַ�
	 * @return ָ���ַ��Ķ�Ӧ�����ַ�
	 */
	public C2D_FontChar getFontChar(char charValue)
	{
		C2D_FontChar fontChar = charSet.get(charValue);
		if (fontChar == null)
		{
			return null;
		}
		return fontChar;
	}

	/**
	 * ��ȡָ���ַ�����ͼ
	 * 
	 * @param charValue
	 *            ָ�����ַ�
	 * @return ָ���ַ�����ͼ
	 */
	public C2D_Image getCharTextureImage(char charValue)
	{
		C2D_FontChar fontChar = charSet.get(charValue);
		if (fontChar == null)
		{
			return null;
		}
		return fontChar.charImage;
	}

	/**
	 * ��ȡָ���ַ�����ͼ�п�
	 * 
	 * @param charValue
	 *            ָ�����ַ�
	 * @return ָ���ַ�����ͼ�п�
	 */
	public C2D_ImageClip getCharTextureClip(char charValue)
	{
		C2D_FontChar fontChar = charSet.get(charValue);
		if (fontChar == null)
		{
			return null;
		}
		return fontChar.charClip;
	}

	private static C2D_Array strArray = new C2D_Array(0);

	/**
	 * �ָ��ַ���.��ָ�����ַ�������ĳһ�ָ��ַ����ָ���������ַ����� һ�г���ָ���Ŀ��Ҳ�ᱻ�ָ
	 * 
	 * @param str
	 *            String ���ָ���ַ���
	 * @param separ
	 *            char �ָ��ַ���һ��ʹ��'|'
	 * @param width
	 *            int ��������ʾ�Ŀ�ȣ���������ƿ�ȣ���������<=0����������
	 * @return String[] �ָ�õ��ַ�������
	 */
	public String[] splitString(String str, char separ, int width)
	{
		int curIndex = 0;
		int preIndex = 0;
		int curWidth = 0;
		int strCount = str.length();
		strArray.removeAllElements();
		do
		{
			if (curIndex >= strCount)
			{
				strArray.addElement(str.substring(preIndex, curIndex));
				break;
			}
			char c = str.charAt(curIndex);
			curWidth += charWidth(c);
			// ����
			if (c == separ)
			{
				strArray.addElement(str.substring(preIndex, curIndex));
				curWidth = 0;
				preIndex = ++curIndex;
			}
			else if (width > 0 && curWidth > width)
			{
				strArray.addElement(str.substring(preIndex, curIndex));
				curWidth = charWidth(c);
				preIndex = curIndex++;
			}
			else
			{
				curIndex++;
			}
		}
		while (true);
		int size = strArray.size();
		String[] divStr = new String[size];
		for (int i = 0; i < size; i++)
		{
			divStr[i] = (String) strArray.elementAt(i);
		}
		strArray.removeAllElements();
		return divStr;
	}

	/**
	 * �ָ��ַ���.
	 * 
	 * @param str
	 *            String ���ָ���ַ���
	 * @param separ
	 *            char �ָ��ַ���һ��ʹ��'|'
	 * @return String[] �ָ�õ��ַ�������
	 */
	public String[] splitString(String str, char separ)
	{
		int curIndex = 0;
		int preIndex = 0;
		int strCount = str.length();
		strArray.removeAllElements();
		do
		{
			if (curIndex >= strCount)
			{
				strArray.addElement(str.substring(preIndex, curIndex));
				break;
			}
			char c = str.charAt(curIndex);
			// ����
			if (c == separ)
			{
				strArray.addElement(str.substring(preIndex, curIndex));
				preIndex = ++curIndex;
			}
			else
			{
				curIndex++;
			}
		}
		while (true);
		int size = strArray.size();
		String[] divStr = new String[size];
		for (int i = 0; i < size; i++)
		{
			divStr[i] = (String) strArray.elementAt(i);
		}
		strArray.removeAllElements();
		return divStr;
	}
	public void onRelease()
	{
		Set<Character> chars = charSet.keySet();
		Iterator<Character> it = chars.iterator();
		while (it.hasNext())
		{
			Character c = it.next();
			C2D_FontChar font = charSet.get(c);
			if (font != null)
			{
				font.doRelease(this);
			}
		}
		charSet.clear();
	}
}
