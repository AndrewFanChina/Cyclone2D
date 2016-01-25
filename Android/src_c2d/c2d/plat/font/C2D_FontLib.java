package c2d.plat.font;

import android.graphics.Typeface;
import c2d.lang.app.C2D_App;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;

/**
 * C2DFontLib ����⣬��������ڴ��ⲿ�ֿ��ļ���*.ttf�ȣ�����һ���ֿ����һ���ֿ����
 * ӵ������Դ��ⲿ�ֿ��ļ�����Ҫ��Ϣ�����������Դ������һ�����߶��������۶���C2DFontFace
 * ��һ���ֿ�Ӧ�����ٰ���һ����ۣ������ۿ�����б�塢����ȵȣ������ͨ��C2DFontLib��
 * getFaceCount���鿴�ж��ٸ���ۡ��������ֿ�ʱ�����Զ������ֿ������д��ڵ���۵���ǰ��
 * c2dFontFaces�����У������ͨ������getFace(int id)����ȡ��ۣ�����ͨ��getFace(C2DFontStyle style)
 * ���Զ���ȡ��Ӧ����������ۡ�һ����˵��windows�����ttf�ļ�ֻ�����һ��������ۣ�ͬһ���ֿ�� �����ۻ�����ͬǰ׺�Ķ���ļ�����ʽ��š�
 * 
 * @author AndrewFan
 * 
 */
public class C2D_FontLib extends C2D_Object
{

	// ������
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
	 * ���ⲿ�ļ�����һ������⣬���ʹ��Ĭ������⣬ֻҪ����null
	 * 
	 * @param file_Name
	 * @return û��Ŀ������ʱ���᷵��Ĭ������
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
	 * ��ϵͳ���崴��һ������⣬���ʹ��Ĭ������⣬ֻҪ����null
	 * 
	 * @param family_Name
	 * @return û��Ŀ������ʱ���᷵��Ĭ������
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

	// �Զ���ȡ��Ӧ����������ۣ��������򷵻�NULL
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