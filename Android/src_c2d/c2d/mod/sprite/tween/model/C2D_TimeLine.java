package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
/**
 * ʱ���ᵥԪ��
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
     * ���ָ��ʱ��λ�õ�֡
     * @param timePos 
     * @return ָ��ʱ��λ�õ�֡
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
     * ���ָ��ʱ��λ�õ�֡����
     * @param timePos 
     * @return ָ��ʱ��λ�õ�֡����
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
     * ���ָ��ʱ��λ�õ�ǰ��֡
     * @param timePos
     * @return  ָ��ʱ��λ�õ�ǰ��֡
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
     * ��ȡʱ���᳤��(��ȫ�������ļ����)
     * @return ʱ���᳤��
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
	 * ����������ȡ����
	 * @param dataIn DataInputStream ������
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
		//��ʼ��֡��Ԫ��Ϣ
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
     * ��ȡ����Ԫ���
     * @return ����Ԫ���
     */
    public C2D_TimeLineHolder GetParent()
    {
        return (C2D_TimeLineHolder)parent;
    }
	/**
	 * ��ȡ�ӵ�Ԫ
	 * @return �ӵ�Ԫ
	 */
	public C2D_Frame GetSon(int id)
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
