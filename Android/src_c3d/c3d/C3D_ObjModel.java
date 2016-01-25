package c3d;

import c2d.lang.math.C2D_Math;
import c2d.mod.sprite.C2D_Sprite;
import c2d.plat.gfx.C2D_Graphics;
import c3d.util.math.C3D_Vertex3;

public class C3D_ObjModel extends C3D_Model
{
	// actor对象
	public C2D_Sprite objActor = null;

	public C3D_ObjModel(byte modelTypeT, C3D_Vertex3 pointT)
	{
		super(modelTypeT, null);
		bottomCenter = pointT;
	}

	/**
	 * setActor 设置Actor对象
	 */
	public void setActor(C2D_Sprite objActorT)
	{
		objActor = objActorT;
	}


	public int lastY = -1;

	/**
	 * display 显示对象
	 * 
	 * @param g
	 *            Graphics
	 * @param x
	 *            int 2D屏幕坐标X
	 * @param y
	 *            int 2D屏幕坐标Y
	 * @param far
	 *            int 远近指数
	 */
	public void display(C2D_Graphics g, int x, int y, int far)
	{
		if (objActor == null)
		{
			return;
		}
		int region = (int) (((long)C3D_Camera.far) >> 8);
		int zoomCount = zoomPercents.length;
		int scaleID = (int) (Math.abs(far) * zoomCount / region);
		scaleID = C2D_Math.limitNumber(scaleID, 0, zoomCount - 1);
		// 场景无关道具的坐标补偿
		// if(stagePropID==AWorld.prop_others)
		// {
		// int roadID=this.followRoadID;
		// if(AWorld.currentRoadID-roadID>AWorld.nbViewObjectChunk*2)
		// {
		// roadID+=AWorld.road.gerNodeCount();
		// }
		// int roadDis = roadID-AWorld.currentRoadID;
		// percent=(AWorld.nbViewObjectChunk-roadDis)*10;
		// percent-=percent%10;
		// if(lastY>=0)
		// {
		// y=lastY;
		// }
		// if(lastY<0&&Math.abs(roadDis)<6)
		// {
		// lastY=y;
		// }
		// }
		//
		int percent = zoomPercents[scaleID];
		int off = 0;// ObjM_offY/(scaleID+1);
		if (playerPropID >= 0)
		{
			off = -ObjM_offY / (scaleID + 1);
		}
		// FIXME objActor.display(g,x,y+off,percent,scaleID);
	}

	// 释放资源
	public void releaseRes()
	{
		super.releaseRes();
		objActor = null;
	}
}
