package c2d.plat.font;

import android.graphics.Typeface;
import c2d.lang.obj.C2D_Object;

/**
 * C2DFontFace ������ۣ�����һ��C2DFontLib�����������㲻��Ҫ�Լ��ͷ�����
 * ���ͷ�C2DFontLibʱ��������������C2DFontFace�����Զ��ͷš�
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
