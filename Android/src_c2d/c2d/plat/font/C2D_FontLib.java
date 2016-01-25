package c2d.plat.font;

import android.graphics.Typeface;
import c2d.lang.app.C2D_App;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;

/**
 * C2DFontLib 字体库，这个类用于从外部字库文件（*.ttf等）加载一个字库对象。一个字库对象，
 * 拥有了针对此外部字库文件的主要信息的描述，可以从它获得一个或者多个字体外观对象C2DFontFace
 * ，一个字库应该至少包含一个外观，多个外观可能是斜体、粗体等等，你可以通过C2DFontLib的
 * getFaceCount来查看有多少个外观。当加载字库时，会自动加载字库中所有存在的外观到当前的
 * c2dFontFaces数组中，你可以通过方法getFace(int id)来获取外观，或者通过getFace(C2DFontStyle style)
 * 来自动获取相应风格的字体外观。一般来说，windows下面的ttf文件只会包含一个字体外观，同一个字库的 多个外观会以相同前缀的多个文件的形式存放。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_FontLib extends C2D_Object
{

	// 字体风格
	public static final int STYLE_NORMAL = 0;
	public static final int STYLE_BOLD = 1;
	public static final int STYLE_ITALIC = 2;
	public static final int STYLE_BOLD_ITALIC = 4;

	private Typeface m_font = null;

	private C2D_FontLib(Typeface font)
	{
		m_font = font;
	}

	/**
	 * 从外部文件创建一个字体库，如果使用默认字体库，只要传入null
	 * 
	 * @param file_Name
	 * @return 没有目标字体时，会返回默认字体
	 */
	public static C2D_FontLib loadFromFile(String file_Name)
	{
		Typeface font = null;
		if (file_Name != null)
		{
			try
			{
				font = Typeface.createFromAsset(C2D_App.getApp().getAssets(), C2D_Consts.STR_RES + C2D_Consts.STR_Fonts + file_Name);				
			}
			catch(Exception e)
			{
				
			}
		}
		if (font == null)
		{
			font = Typeface.DEFAULT;
		}
		if (font == null)
		{
			return null;
		}
		C2D_FontLib fontLib = new C2D_FontLib(font);
		return fontLib;
	}

	/**
	 * 从系统字体创建一个字体库，如果使用默认字体库，只要传入null
	 * 
	 * @param family_Name
	 * @return 没有目标字体时，会返回默认字体
	 */
	public static C2D_FontLib loadFromFamily(String family_Name)
	{
		Typeface font = null;
		if (family_Name != null)
		{
			font = Typeface.create(family_Name, STYLE_NORMAL);
		}
		if (font == null)
		{
			font = Typeface.DEFAULT;
		}
		if (font == null)
		{
			return null;
		}
		C2D_FontLib fontLib = new C2D_FontLib(font);
		return fontLib;
	}

	// 自动获取相应风格的字体外观，不存在则返回NULL
	public C2D_FontFace createFace(int style)
	{
		if (style != STYLE_NORMAL && style != STYLE_BOLD && style != STYLE_ITALIC && style != STYLE_BOLD_ITALIC)
		{
			return null;
		}
		Typeface face = Typeface.create(m_font, style);
		if (face != null)
		{
			return new C2D_FontFace(face);
		}
		return null;
	}

	@Override
	public void onRelease()
	{
		m_font=null;
	}

}