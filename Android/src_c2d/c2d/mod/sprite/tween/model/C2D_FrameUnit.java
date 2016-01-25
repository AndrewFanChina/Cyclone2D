package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.math.type.C2D_SizeF;
import c2d.lang.obj.C2D_Object;
import c3d.util.math.C3D_IOUtil;

public abstract class C2D_FrameUnit extends C2D_Object
{
	//֡��Ԫ����
    public static final int UnitType_Bitmap=0;
    public static final int UnitType_MC=1;
    
    protected C2D_Frame parent;//���ṹ����
    public int unitType = UnitType_Bitmap;
    public float posX = 0;//����λ��X����
    public float posY = 0;//����λ��Y����
    public float anchorX = 0.5f;//ê��X����
    public float anchorY = 0.5f;//ê��Y����
    public float rotateDegree=0.0f;//��ת�Ƕ�(����)
    public float scaleX = 1.0f;//ˮƽ��������
    public float scaleY = 1.0f;//��ֱ��������
    public float alpha = 1.0f;//͸����
//    public static Matrix4 mtrixGLCommon = new Matrix4();//GL������ͼ�������������š���������б�С�����������ת��������ƽ�ơ�
    //��������洢
    public C2D_ValueTransform mTransform=new C2D_ValueTransform();//������Ϣ
    public float mParentAlpha = 1.0f;//����͸����
    /**
     * ���캯��
     * @param parentT
     */
    public C2D_FrameUnit(C2D_Frame parentT)
    {
    	parent=parentT;
    }
    /**
     * ��ʼ��������Ϣ
     * @param timePos
     */
    public abstract void setTransform(float timePos);
	/**
	 * ���ø���Ԫ���
	 */
	public void setParent(C2D_Frame parentT)
	{
		parent=parentT;
	}
    /**
     * ����ָ����ʱ����λ�ã���ȡ�õ�Ԫ��ʱ�ı�����Ϣ
     * @param timePos λ��ʱ�����ϵ�λ��
     * @param transform ���ڴ洢����ı�����ֵ����
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
     * ���õ�ǰ��Ԫ�ı�����ֵ
     * @param transform ������ֵ
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
     * ��ȡ֡��Ԫ��ԭʼ��С
     * @return ֡��Ԫ��ԭʼ��С
     */
    public abstract C2D_SizeF getSize();
    /**
     * ���½�Ϊԭ�㣬ʹ��OpenGL��ͼ������ʱ������ָ����һ֡
     * @param xScreen  ���Ƶ���ĻĿ��λ�õ�X����
     * @param yScreen ���Ƶ���ĻĿ��λ�õ�Y����
     * @param zoomScreen ���Ƶ���Ļʱ���ӵ����ű���
     * @param parentAlpha �����͸����
     * @param timePos λ��ʱ�����ϵ�λ��
     */
    public abstract void GlDisplay(float xScreen, float yScreen, float zoomScreen, float parentAlpha, float timePos);


    /**
     * �����ж�ȡ��Դ
     * @param s ������
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
	 * �ͷ���Դ
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
