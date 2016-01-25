package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.type.C2D_SizeF;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_FrameUnit_Bitmap extends C2D_FrameUnit
{
	/**
	 * 构造函数
	 * @param parentT 父类引用
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
	 * 切块索引
	 */
    public C2D_SpriteClip clipElement = null;
    /**
     * 左下角为原点，使用OpenGL绘图，绘制时间轴上指定的一帧
     * @param xScreen  绘制到屏幕目标位置的X坐标
     * @param yScreen 绘制到屏幕目标位置的Y坐标
     * @param zoomScreen 绘制到屏幕时叠加的缩放比率
     * @param parentAlpha 父类的透明度
     * @param timePos 位于时间轴上的位置
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
     * 获得显示大小
     */
    public C2D_SizeF getSize()
    {
        return clipElement.getShowSize();
    }
    /**
     * 从流中读取资源
     * @param s 输入流
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
	 * 释放资源
	 */
	public void onRelease()
	{
		clipElement=null;
		super.onRelease();
	}
}
