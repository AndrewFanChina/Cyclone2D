package c2d.plat.font;

import android.graphics.Typeface;
import c2d.lang.obj.C2D_Object;

/**
 * C2DFontFace 字体外观，基于一个C2DFontLib创建产生。你不需要自己释放它。
 * 在释放C2DFontLib时，基于它创建的C2DFontFace都会自动释放。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_FontFace extends C2D_Object
{
	Typeface m_face;

	C2D_FontFace(Typeface face)
	{
		m_face = face;
	}

	@Override
	public void onRelease()
	{
		m_face=null;
	}
}
