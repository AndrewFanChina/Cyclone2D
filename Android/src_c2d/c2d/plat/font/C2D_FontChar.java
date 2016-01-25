package c2d.plat.font;

import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 字体字符
 * 
 * @author AndrewFan
 * 
 */
public class C2D_FontChar extends C2D_Object
{
	/**
	 * 此字符的横向推进距离
	 */
	public float advanceX;
	/**
	 * 基点与此字符的左边界距离
	 */
	public float bearingX;
	/**
	 * 基点与此字符的上边界距离
	 */
	public float bearingY;
	public C2D_Image charImage;
	public C2D_ImageClip charClip;

	C2D_FontChar()
	{
	}

	@Override
	public void onRelease()
	{
		if (charClip != null)
		{
			charClip.doRelease(this);
			charClip = null;
		}
		if (charImage != null)
		{
			charImage.doRelease(this);
			charImage = null;
		}
	}
}
