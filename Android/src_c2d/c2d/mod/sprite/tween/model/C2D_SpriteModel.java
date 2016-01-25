package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.mod.sprite.tween.C2D_SpriteManager;

public class C2D_SpriteModel extends C2D_Object
{
	/** 动画管理器，父容器 */
	protected C2D_SpriteManager parent;
	
	/** 动作列表 */
	public C2D_Action[] sonList;
	
	/** 当前角色使用到的图片索引. */
	private short imgsUsedIndexs[] = null;
	
	/** 动作位移数据. */
	private byte actionOffsets[] = null;
	
	/** 被使用的次数，当有角色引用时，此次数会增加，当此次数是0时，可以被卸载*/
	private short usedTime = 0;
	/**
	 * 角色数据构造函数
	 */
	public C2D_SpriteModel(C2D_SpriteManager parentT)
	{
		parent=parentT;
	}
	/**
	 * 从输入流读取角色数据
	 * @param dataIn DataInputStream 输入流
	 * @throws Exception 
	 */
	public void readObject(DataInputStream dataIn) throws Exception
	{

		// 开始读取角色信息
		byte byteData = 0;
		short shortData = 0;
		// 读入图片索引信息
		short number = 0;
		number = C2D_IOUtil.readShort(number, dataIn);
		imgsUsedIndexs = new short[number];
		for (int i = 0; i < number; i++)
		{
			imgsUsedIndexs[i] = C2D_IOUtil.readShort(imgsUsedIndexs[i], dataIn);
		}
		// 读入角色信息
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
	 * 获取指定动作的时间轴长度，也即总帧数
	 *
	 * @param actionID int 指定的动作ID
	 * @return int 总帧数
	 */
	public int getFrameCount(int actionID)
	{
		return sonList[actionID].getMaxFrameCount();
	}



	/**
	 * getUsedImgIDs 返回使用到的图片索引.
	 *
	 * @return short[]
	 */
	public short[] getUsedImgIDs()
	{
		return imgsUsedIndexs;
	}


	/**
	 * 返回动作帧的指定方向的方向动作偏移(暂未实现)
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
	 * 使用数据
	 */
	public void useData()
	{
		usedTime++;
	}

	/**
	 * unuseData 释放数据
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
	 * 返回使用次数.
	 *
	 * @return int
	 */
	public int getUsedTime()
	{
		return usedTime;
	}
    /**
     * 获取父单元句柄
     * @return 父单元句柄
     */
    public C2D_SpriteManager GetParent()
    {
        return parent;
    }
	/**
	 * 获取子单元
	 * @return 获取子单元
	 */
	public C2D_Action GetSon(int id)
	{
		return sonList[id];
	}
	/**
	 * 获取子单元数目
	 * @return 子单元数目
	 */
	public int getActionCount()
	{
		return sonList.length;
	}
	/**
	 * 释放资源
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
