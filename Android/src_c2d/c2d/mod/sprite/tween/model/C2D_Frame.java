package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;

/// <summary>
/// ֡��Ԫ
/// </summary>
public class C2D_Frame extends C2D_Object
{
    public int timeBegin;//֡��ʼʱ��(��ʱ���������ڵ�λ��)
    public int timeLast=1;//֡ʱ�䳤��(��ʱ�����ϳ����������ֻ�е�һ֡�������ݣ������֡���ǵ�һ֡���ݵ�����)
    public int frameType = MFrameType_NUL;//֡����(ֻ�����ǹؼ�֡)
    public static final int MFrameType_NUL=0;//֡����-�����ڵ�֡
    public static final int MFrameType_KEY=1;//֡����-�ؼ�֡
    public static final int MFrameType_MID=2;//֡����-�м�֡
    public boolean hasMotion = false;//�Ƿ��в��䶯��
    protected C2D_TimeLine parent;     //���ṹ����
    public C2D_FrameUnit[] sonList;
	public int id=0;
    public C2D_Frame()
    {
    }
    public C2D_Frame(C2D_TimeLine parenT)
    {
        parent = parenT;
    }
    //��ȡ���ڵ���һ��֡��Ԫ
    public C2D_Frame GetNext()
    {
        C2D_TimeLine timeLine = GetParent();
        if (timeLine == null||id+1<0||id+1>=timeLine.sonList.length)
        {
            return null;
        }
        return timeLine.sonList[id + 1];
    }
    //��ȡ���ڵ�ǰһ��֡��Ԫ
    public C2D_Frame GetBefore()
    {
        C2D_TimeLine timeLine = GetParent();
        if (timeLine == null||id-1<0||id-1>=timeLine.sonList.length)
        {
            return null;
        }
        return timeLine.sonList[id - 1];
    }
    //����Ƿ��������Ĳ���֡
    public boolean IsNormalMotion()
    {
        if (!hasMotion)
        {
            return false;
        }
        if (this.Count() > 1)
        {
            return false;
        }
        C2D_Frame next = GetNext();
        if (next == null||next.Count() != 1)
        {
            return false;
        }
        return true;
    }
    public void readObject(DataInputStream s) throws Exception
    {
    	int intData=0;
        timeBegin=C2D_IOUtil.readInt(intData,s);
        timeLast=C2D_IOUtil.readInt(intData,s);
        frameType = C2D_IOUtil.readInt(intData,s);
        hasMotion=C2D_IOUtil.readBoolean(hasMotion,s);
        short len=0;
        len = C2D_IOUtil.readShort(len,s);
        sonList=new C2D_FrameUnit[len];
        for (short i = 0; i < len; i++)
        {
            C2D_FrameUnit elem=null;
            int type = C2D_IOUtil.readInt(intData,s);
            if (type == C2D_FrameUnit.UnitType_Bitmap)
            {
                elem = new C2D_FrameUnit_Bitmap(this);
            }
            else if (type == C2D_FrameUnit.UnitType_MC)
            {
//                elem = new MFrameUnit_MC();
            }
            elem.unitType = type;
            elem.parent=this;
            elem.ReadObject(s);
            elem.transHadler(this);
            sonList[i]=elem;
        }
    }
    /**
     * ��ȡ����Ԫ���
     * @return ����Ԫ���
     */
    public C2D_TimeLine GetParent()
    {
        return (C2D_TimeLine)parent;
    }
	/**
	 * ��ȡ�ӵ�Ԫ
	 * @return �ӵ�Ԫ
	 */
	public C2D_FrameUnit GetSon(int id)
	{
		return sonList[id];
	}
	/**
	 * ��ȡ�ӵ�Ԫ��Ŀ
	 * @return �ӵ�Ԫ��Ŀ
	 */
	public int Count()
	{
		return sonList.length;
	}
	/**
	 * �ͷ���Դ
	 */
	public void onRelease()
	{
		parent=null;
		for(int i=0;i<sonList.length;i++)
		{
			sonList[i].doRelease(this);
			sonList[i]=null;
		}
		sonList=null;
	}
}