package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.mod.sprite.tween.C2D_SpriteManager;

public class C2D_SpriteModel extends C2D_Object
{
	/** ������������������ */
	protected C2D_SpriteManager parent;
	
	/** �����б� */
	public C2D_Action[] sonList;
	
	/** ��ǰ��ɫʹ�õ���ͼƬ����. */
	private short imgsUsedIndexs[] = null;
	
	/** ����λ������. */
	private byte actionOffsets[] = null;
	
	/** ��ʹ�õĴ��������н�ɫ����ʱ���˴��������ӣ����˴�����0ʱ�����Ա�ж��*/
	private short usedTime = 0;
	/**
	 * ��ɫ���ݹ��캯��
	 */
	public C2D_SpriteModel(C2D_SpriteManager parentT)
	{
		parent=parentT;
	}
	/**
	 * ����������ȡ��ɫ����
	 * @param dataIn DataInputStream ������
	 * @throws Exception 
	 */
	public void readObject(DataInputStream dataIn) throws Exception
	{

		// ��ʼ��ȡ��ɫ��Ϣ
		byte byteData = 0;
		short shortData = 0;
		// ����ͼƬ������Ϣ
		short number = 0;
		number = C2D_IOUtil.readShort(number, dataIn);
		imgsUsedIndexs = new short[number];
		for (int i = 0; i < number; i++)
		{
			imgsUsedIndexs[i] = C2D_IOUtil.readShort(imgsUsedIndexs[i], dataIn);
		}
		// �����ɫ��Ϣ
		number = C2D_IOUtil.readShort(number, dataIn);
		sonList =new C2D_Action[number];
		for (int i = 0; i < number; i++)
		{
			C2D_Action action = new C2D_Action(this);
			action.readObject(dataIn);
			action.transHadler(this);
			sonList[i] = action;
		}
	}


	/**
	 * ��ȡָ��������ʱ���᳤�ȣ�Ҳ����֡��
	 *
	 * @param actionID int ָ���Ķ���ID
	 * @return int ��֡��
	 */
	public int getFrameCount(int actionID)
	{
		return sonList[actionID].getMaxFrameCount();
	}



	/**
	 * getUsedImgIDs ����ʹ�õ���ͼƬ����.
	 *
	 * @return short[]
	 */
	public short[] getUsedImgIDs()
	{
		return imgsUsedIndexs;
	}


	/**
	 * ���ض���֡��ָ������ķ�����ƫ��(��δʵ��)
	 */
	public byte getActionOffset(byte direction,int actionID, float playTime)
	{
		if (actionOffsets==null)
		{
			return 0;
		}
		if (actionID < 0 || actionID >= getActionCount())
		{
			return 0;
		}
		if (playTime < 0 || playTime >= getFrameCount(actionID))
		{
			return 0;
		}
		if(direction>parent.actionOffsetType)
		{
			return 0;
		}
//		int posID = actionFrames_pos[actionID * 2] + frameID * (parent.actionOffsetType);
//		return  actionOffsets[posID+direction];
		return 0;
	}
	/**
	 * ʹ������
	 */
	public void useData()
	{
		usedTime++;
	}

	/**
	 * unuseData �ͷ�����
	 *
	 * @return boolean
	 */
	public boolean unuseData()
	{
		if (usedTime > 0)
		{
			usedTime--;
		}
		if (usedTime <= 0)
		{
			doRelease();
			return true;
		}
		return false;
	}

	/**
	 * ����ʹ�ô���.
	 *
	 * @return int
	 */
	public int getUsedTime()
	{
		return usedTime;
	}
    /**
     * ��ȡ����Ԫ���
     * @return ����Ԫ���
     */
    public C2D_SpriteManager GetParent()
    {
        return parent;
    }
	/**
	 * ��ȡ�ӵ�Ԫ
	 * @return ��ȡ�ӵ�Ԫ
	 */
	public C2D_Action GetSon(int id)
	{
		return sonList[id];
	}
	/**
	 * ��ȡ�ӵ�Ԫ��Ŀ
	 * @return �ӵ�Ԫ��Ŀ
	 */
	public int getActionCount()
	{
		return sonList.length;
	}
	/**
	 * �ͷ���Դ
	 */
	public void onRelease()
	{
		parent=null;
		if(sonList!=null)
		{
			for(int i=0;i<sonList.length;i++)
			{
				sonList[i].doRelease(this);
				sonList[i]=null;
			}
			sonList=null;
		}
	}

}
