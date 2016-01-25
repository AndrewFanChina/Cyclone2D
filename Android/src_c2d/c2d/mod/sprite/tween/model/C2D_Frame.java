package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;

/// <summary>
/// 帧单元
/// </summary>
public class C2D_Frame extends C2D_Object
{
    public int timeBegin;//帧起始时间(在时间轴上所在的位置)
    public int timeLast=1;//帧时间长度(在时间轴上持续间隔数，只有第一帧含有内容，后面的帧都是第一帧内容的延续)
    public int frameType = MFrameType_NUL;//帧类型(只可能是关键帧)
    public static final int MFrameType_NUL=0;//帧类型-不存在的帧
    public static final int MFrameType_KEY=1;//帧类型-关键帧
    public static final int MFrameType_MID=2;//帧类型-中间帧
    public boolean hasMotion = false;//是否含有补间动画
    protected C2D_TimeLine parent;     //父结构索引
    public C2D_FrameUnit[] sonList;
	public int id=0;
    public C2D_Frame()
    {
    }
    public C2D_Frame(C2D_TimeLine parenT)
    {
        parent = parenT;
    }
    //获取紧邻的下一个帧单元
    public C2D_Frame GetNext()
    {
        C2D_TimeLine timeLine = GetParent();
        if (timeLine == null||id+1<0||id+1>=timeLine.sonList.length)
        {
            return null;
        }
        return timeLine.sonList[id + 1];
    }
    //获取紧邻的前一个帧单元
    public C2D_Frame GetBefore()
    {
        C2D_TimeLine timeLine = GetParent();
        if (timeLine == null||id-1<0||id-1>=timeLine.sonList.length)
        {
            return null;
        }
        return timeLine.sonList[id - 1];
    }
    //检查是否是正常的补间帧
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
     * 获取父单元句柄
     * @return 父单元句柄
     */
    public C2D_TimeLine GetParent()
    {
        return (C2D_TimeLine)parent;
    }
	/**
	 * 获取子单元
	 * @return 子单元
	 */
	public C2D_FrameUnit GetSon(int id)
	{
		return sonList[id];
	}
	/**
	 * 获取子单元数目
	 * @return 子单元数目
	 */
	public int Count()
	{
		return sonList.length;
	}
	/**
	 * 释放资源
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