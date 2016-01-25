package c2d.plat.font;

import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * �����ַ�
 * 
 * @author AndrewFan
 * 
 */
public class C2D_FontChar extends C2D_Object
{
	/**
	 * ���ַ��ĺ����ƽ�����
	 */
	public float advanceX;
	/**
	 * ��������ַ�����߽����
	 */
	public float bearingX;
	/**
	 * ��������ַ����ϱ߽����
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
