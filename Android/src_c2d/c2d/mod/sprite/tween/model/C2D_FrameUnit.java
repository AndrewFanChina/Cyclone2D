package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.math.type.C2D_SizeF;
import c2d.lang.obj.C2D_Object;
import c3d.util.math.C3D_IOUtil;

public abstract class C2D_FrameUnit extends C2D_Object
{
	//帧单元类型
    public static final int UnitType_Bitmap=0;
    public static final int UnitType_MC=1;
    
    protected C2D_Frame parent;//父结构索引
    public int unitType = UnitType_Bitmap;
    public float posX = 0;//中心位置X坐标
    public float posY = 0;//中心位置Y坐标
    public float anchorX = 0.5f;//锚点X比率
    public float anchorY = 0.5f;//锚点Y比率
    public float rotateDegree=0.0f;//旋转角度(度数)
    public float scaleX = 1.0f;//水平方向缩放
    public float scaleY = 1.0f;//垂直方向缩放
    public float alpha = 1.0f;//透明度
//    public static Matrix4 mtrixGLCommon = new Matrix4();//GL公共绘图矩阵【先自身缩放、接着自身斜切、接着自身旋转、再整体平移】
    //加速运算存储
    public C2D_ValueTransform mTransform=new C2D_ValueTransform();//变形信息
    public float mParentAlpha = 1.0f;//父类透明度
    /**
     * 构造函数
     * @param parentT
     */
    public C2D_FrameUnit(C2D_Frame parentT)
    {
    	parent=parentT;
    }
    /**
     * 初始化变形信息
     * @param timePos
     */
    public abstract void setTransform(float timePos);
	/**
	 * 设置父单元句柄
	 */
	public void setParent(C2D_Frame parentT)
	{
		parent=parentT;
	}
    /**
     * 根据指定的时间轴位置，获取该单元当时的变形信息
     * @param timePos 位于时间轴上的位置
     * @param transform 用于存储结果的变形数值对象
     */
    public void getValueTransform(float timePos,C2D_ValueTransform transform)
    {
        if(transform==null)
        {
        	return;
        }
        C2D_Frame endFrame = parent.GetNext();
        if (!parent.hasMotion || timePos < parent.timeBegin || endFrame==null)
        {
            transform.setValue(this);
        }
        else
        {
        	C2D_FrameUnit headUnit = parent.GetSon(0);
        	C2D_FrameUnit endUnit = endFrame.GetSon(0);
            if (timePos >= endFrame.timeBegin)
            {
                transform.setValue(endUnit);
            }
            else
            {
                float transit = (timePos - parent.timeBegin) * 1.0f / (endFrame.timeBegin - parent.timeBegin);
                transform.rotateDegree = headUnit.rotateDegree + (endUnit.rotateDegree - headUnit.rotateDegree) * transit;
                transform.alpha = headUnit.alpha + (endUnit.alpha - headUnit.alpha) * transit;
                transform.scale.m_x = headUnit.scaleX + (endUnit.scaleX -headUnit.scaleX) * transit;
                transform.scale.m_y = headUnit.scaleY + (endUnit.scaleY - headUnit.scaleY) * transit;
                transform.anchor.m_x = headUnit.anchorX + (endUnit.anchorX - headUnit.anchorX) * transit;
                transform.anchor.m_y = headUnit.anchorY + (endUnit.anchorY - headUnit.anchorY) * transit;
                transform.pos.m_x = headUnit.posX + (endUnit.posX - headUnit.posX) * transit;
                transform.pos.m_y = headUnit.posY + (endUnit.posY - headUnit.posY) * transit;
            }
        }
    }
    /**
     * 设置当前单元的变形数值
     * @param transform 变形数值
     */
    public void setValueTransform(C2D_ValueTransform transform)
    {
        rotateDegree = transform.rotateDegree;
        alpha = transform.alpha;
        scaleX = transform.scale.m_x;
        scaleY = transform.scale.m_y;
        anchorX = transform.anchor.m_x;
        anchorY = transform.anchor.m_y;
        posX = transform.pos.m_x;
        posY = transform.pos.m_y;
    }
    /**
     * 获取帧单元的原始大小
     * @return 帧单元的原始大小
     */
    public abstract C2D_SizeF getSize();
    /**
     * 左下角为原点，使用OpenGL绘图，绘制时间轴上指定的一帧
     * @param xScreen  绘制到屏幕目标位置的X坐标
     * @param yScreen 绘制到屏幕目标位置的Y坐标
     * @param zoomScreen 绘制到屏幕时叠加的缩放比率
     * @param parentAlpha 父类的透明度
     * @param timePos 位于时间轴上的位置
     */
    public abstract void GlDisplay(float xScreen, float yScreen, float zoomScreen, float parentAlpha, float timePos);


    /**
     * 从流中读取资源
     * @param s 输入流
     * @throws Exception 
     */
    public void ReadObject(DataInputStream s) throws Exception
    {
        posX = C3D_IOUtil.readFloat(posX,s);
        posY = C3D_IOUtil.readFloat(posY,s);
        anchorX = C3D_IOUtil.readFloat(anchorX,s);
        anchorY = C3D_IOUtil.readFloat(anchorY,s);
        rotateDegree = C3D_IOUtil.readFloat(rotateDegree,s);
        scaleX = C3D_IOUtil.readFloat(scaleX,s);
        scaleY = C3D_IOUtil.readFloat(scaleY,s);
        alpha = C3D_IOUtil.readFloat(alpha,s);
    }
	/**
	 * 释放资源
	 */
	public void onRelease()
	{
		parent=null;
		if(mTransform!=null)
		{
			mTransform.doRelease(this);
			mTransform=null;
		}

	}
}
