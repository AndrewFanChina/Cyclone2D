package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.type.C2D_SizeF;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_FrameUnit_Bitmap extends C2D_FrameUnit
{
	/**
	 * ���캯��
	 * @param parentT ��������
	 */
	public C2D_FrameUnit_Bitmap(C2D_Frame parentT)
	{
		super(parentT);
	}
	public void setTransform(float timePos)
	{
        getValueTransform(timePos,mTransform);
	}
	/**
	 * �п�����
	 */
    public C2D_SpriteClip clipElement = null;
    /**
     * ���½�Ϊԭ�㣬ʹ��OpenGL��ͼ������ʱ������ָ����һ֡
     * @param xScreen  ���Ƶ���ĻĿ��λ�õ�X����
     * @param yScreen ���Ƶ���ĻĿ��λ�õ�Y����
     * @param zoomScreen ���Ƶ���Ļʱ���ӵ����ű���
     * @param parentAlpha �����͸����
     * @param timePos λ��ʱ�����ϵ�λ��
     */
	public void GlDisplay(float xScreen, float yScreen, float zoomScreen, float parentAlpha, float timePos)
    {
        mParentAlpha=parentAlpha;
        C2D_Graphics.glPushMatrix();
    	if(zoomScreen!=1.0f)
    	{
    		C2D_Graphics.glScalef(zoomScreen, zoomScreen, 1.0f);
    	}
        if (scaleX != 0.0f && scaleY != 0.0f)
        {
            float newAlpha = mParentAlpha * mTransform.alpha;
            clipElement.drawWithTransform(1.0f,xScreen,yScreen,mTransform,newAlpha,0);
        }
        C2D_Graphics.glPopMatrix();
    }
 
    /**
     * �����ʾ��С
     */
    public C2D_SizeF getSize()
    {
        return clipElement.getShowSize();
    }
    /**
     * �����ж�ȡ��Դ
     * @param s ������
     * @throws Exception 
     */
	public void ReadObject(DataInputStream s) throws Exception
	{
        short id = 0;
        id = C2D_IOUtil.readShort(id,s);
        clipElement = parent.parent.parent.parent.parent.spriteClips[id];
        super.ReadObject(s);
		
	}
	/**
	 * �ͷ���Դ
	 */
	public void onRelease()
	{
		clipElement=null;
		super.onRelease();
	}
}
