package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
/**
 * 时间轴单元类
 * @author Andrew
 *
 */
public class C2D_TimeLine extends C2D_Object
{
	public C2D_Frame[] sonList;
	protected C2D_TimeLineHolder parent;
    public C2D_TimeLine(C2D_TimeLineHolder parenT)
    {
        parent = parenT;
    }

    /**
     * 获得指定时间位置的帧
     * @param timePos 
     * @return 指定时间位置的帧
     */
    public C2D_Frame getFrameByX(float timePos)
    {
        C2D_Frame f = null;
        for (int i = 0; i < sonList.length;i++)
        {
            C2D_Frame fI = sonList[i];
            if (fI.timeBegin <= timePos && fI.timeBegin + fI.timeLast > timePos)
            {
                f = fI;
                break;
            }
        }
        return f;
    }
    /**
     * 获得指定时间位置的帧类型
     * @param timePos 
     * @return 指定时间位置的帧类型
     */
    public int getFrameTypeByX(int timePos)
    {
    	int type = C2D_Frame.MFrameType_NUL;
        for (int i = 0; i < Count(); i++)
        {
            C2D_Frame fI = sonList[i];
            if (fI.timeBegin == timePos)
            {
                type = fI.frameType;
                break;
            }
            else if (fI.timeBegin < timePos && fI.timeBegin + fI.timeLast > timePos)
            {
                type =C2D_Frame.MFrameType_MID;
                break;
            }
        }
        return type;
    }
    /**
     * 获得指定时间位置的前端帧
     * @param timePos
     * @return  指定时间位置的前端帧
     */
    public C2D_Frame getFrontFrameByX(int timePos)
    {
        C2D_Frame f = null;
        for (int i = 0; i < Count(); i++)
        {
            C2D_Frame fI = sonList[i];
            if (fI.timeBegin > timePos)
            {
                break;
            }
            else
            {
                f = fI;
            }
        }
        return f;
    }
  
    /**
     * 获取时间轴长度(即全部持续的间隔数)
     * @return 时间轴长度
     */
    public int getTimeSpan()
    {
        if (Count() > 0)
        {
            return sonList[Count() - 1].timeBegin + sonList[Count() - 1].timeLast;
        }
        return 0;
    }
	/**
	 * 从输入流读取数据
	 * @param dataIn DataInputStream 输入流
	 * @throws Exception 
	 */
	public void readObject(DataInputStream dataIn) throws Exception
	{
		short number=0;
		number = C2D_IOUtil.readShort(number, dataIn);
		sonList =new C2D_Frame[number];
		for (int i = 0; i < number; i++)
		{
			C2D_Frame lement = new C2D_Frame(this);
			lement.readObject(dataIn);
			lement.id=i;
			sonList[i]=lement;
			lement.transHadler(this);
		}
		//初始化帧单元信息
		for (int i = 0; i < number; i++)
		{
			C2D_Frame element = sonList[i];
			for(int j=0;j<element.Count();j++)
			{
				element.sonList[j].setTransform(element.timeBegin);
			}
		}
	}
    /**
     * 获取父单元句柄
     * @return 父单元句柄
     */
    public C2D_TimeLineHolder GetParent()
    {
        return (C2D_TimeLineHolder)parent;
    }
	/**
	 * 获取子单元
	 * @return 子单元
	 */
	public C2D_Frame GetSon(int id)
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
