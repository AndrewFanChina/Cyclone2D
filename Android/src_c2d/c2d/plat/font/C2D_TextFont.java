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
 * C2D_TextFont 字符集，它是一个关于某个字体的指定了外观和字号大小的字符容器，
 * 你可以申请加载某些字符，加载时，会自动从字体外观中获取字符的所有信息， 并且生成相应的字符贴图。只有加载过的字符才可以使用--被用作计算尺寸或者当做
 * 贴图绘制到屏幕。字号一旦设定，它的大小是不允许改变的，这样才会产生一系列同样 大小的字符贴图。如果希望基于相同的字体外观产生不同字号的字符集，那么需要另外
 * 创建一个字符集对象。 更精确的尺寸控制、渲染到缓冲以提升效率...to be down
 * 
 * @author AndrewFan
 */
public class C2D_TextFont extends C2D_Object
{
	private int m_fontSize; // 字号
	private HashMap<Character, C2D_FontChar> charSet = new HashMap<Character, C2D_FontChar>(); // 字符集
	C2D_FontFace m_fontFace; // 字体外观
	private Paint m_paintInner;
	private int m_fontHeight;// 文本高度
	private int m_baseLine; // 基线位置
	private int m_rowHeight;// 行高(包括文本和间隔)

	/** 默认字体大小 */
	public static int DEF_SIZE = 32;
	private static C2D_TextFont m_defFont = null;
	private static C2D_FontLib m_defFontLib = C2D_FontLib.loadFromFamily(null);

	/**
	 * 构建字符集
	 * 
	 * @param fontFace
	 *            字符外观
	 * @param font_size
	 *            字符大小
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
	 * 获取默认的字体
	 * 
	 * @return 默认字体
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
	 * 设置默认字体
	 * 
	 * @param defaultFont
	 *            默认字体
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
	 * 绘制描边字符串(这个函数会绘制9次字符串，效率很低).
	 * 
	 * @param s
	 *            String 文字
	 * @param x
	 *            int x坐标
	 * @param y
	 *            int y坐标
	 * @param fColor
	 *            int 前景色
	 * @param bColor
	 *            int 后景色
	 * @param anchor
	 *            int 锚点
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
	 * 绘制带右下角阴影的字符串(这个函数会绘制2次字符串).
	 * 
	 * @param s
	 *            String 文字
	 * @param x
	 *            int x坐标
	 * @param y
	 *            int y坐标
	 * @param fColor
	 *            int 前景色
	 * @param bColor
	 *            int 后景色
	 * @param anchor
	 *            int 锚点
	 */
	public void drawShadeString(String s, int x, int y, int fColor, int bColor, int anchor)
	{
		drawString(s, x + 1, y + 1, bColor, anchor);
		drawString(s, x, y, fColor, anchor);
	}

	private static C2D_RectF rectForDS = new C2D_RectF();

	/**
	 * 绘制字符串.
	 * 
	 * @param s
	 *            String 文字
	 * @param x
	 *            int x坐标
	 * @param y
	 *            int y坐标
	 * @param color
	 *            int 文字颜色
	 * @param anchor
	 *            byte 锚点
	 */
	public void drawString(String s, int x, int y, int color, int anchor)
	{
		C2D_GdiGraphics.computeLayoutRect(x, y, stringWidth(s), getFontHeight(), anchor, C2D_GdiGraphics.TRANS_NONE, rectForDS);
		x = (int) rectForDS.m_x;
		y = (int) rectForDS.m_y;
		paintString(s, x, y, color, 0, 0);
	}

	/**
	 *  按照水平居左，垂直以基线对齐的方式，绘制指定的字符串到指定坐标，
	 *  会根据换行符进行换行，xOffset,yOffset是水平和垂直附加间隔
	 * @param s
	 *            String 绘制的字符
	 * @param x
	 *            int 左上角X坐标
	 * @param y
	 *            int 左上角Y坐标
	 * @param color
	 *            int 绘制颜色
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
	 * 测量字符串的宽度
	 * 
	 * @param text
	 *            需要测量的字符串
	 * @return 字符宽度
	 */
	public int stringWidth(String text)
	{
		return (int) Math.ceil(m_paintInner.measureText(text));
	}
	/**
	 * 获得字符宽度，根据当前字体来衡量.
	 * 
	 * @param c
	 *            String 字符
	 * @return int 返回宽度
	 */
	public int charWidth(char c)
	{
		return stringWidth(c+"");
	}
	/**
	 * 从某个字体外观装载字符，返回是否成功加载
	 * @param str 要装载的字符串数组
	 * @return 是否装载成功
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
	 * 从某个字体外观装载字符，返回是否成功加载  【需要整体优化，将所有文字放到一张图片上】
	 * @param str 要装载的字符串
	 * @return 是否装载成功
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
		// 开始加载
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
			/* 生成贴图 */
			C2D_GdiImage gdiImage = C2D_GdiImage.createImage(width, height);
			Canvas canvas = gdiImage.getGraphics().getInner();
			canvas.drawText(String.valueOf(charI), -bearingX, -bearingY, m_paintInner);
			C2D_Image charImage = C2D_Image.createImage(gdiImage, true);
			// 存储贴图
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
	 * 获得字体高度
	 * 
	 * @return 字体高度
	 */
	public int getFontHeight()
	{
		return m_fontHeight;
	}

	/**
	 * 获得行高
	 * 
	 * @return 行高
	 */
	public int getRowHeight()
	{
		return m_rowHeight;
	}

	/**
	 * 获取指定字符的对应字体字符
	 * 
	 * @param charValue
	 *            指定的字符
	 * @return 指定字符的对应字体字符
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
	 * 获取指定字符的贴图
	 * 
	 * @param charValue
	 *            指定的字符
	 * @return 指定字符的贴图
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
	 * 获取指定字符的贴图切块
	 * 
	 * @param charValue
	 *            指定的字符
	 * @return 指定字符的贴图切块
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
	 * 分割字符串.将指定的字符串按照某一分割字符来分割成若干行字符串， 一行超过指定的宽度也会被分割。
	 * 
	 * @param str
	 *            String 待分割的字符串
	 * @param separ
	 *            char 分割字符，一般使用'|'
	 * @param width
	 *            int 界面能显示的宽度，如果不限制宽度，可以输入<=0的任意数字
	 * @return String[] 分割好的字符串数组
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
			// 换行
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
	 * 分割字符串.
	 * 
	 * @param str
	 *            String 待分割的字符串
	 * @param separ
	 *            char 分割字符，一般使用'|'
	 * @return String[] 分割好的字符串数组
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
			// 换行
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
