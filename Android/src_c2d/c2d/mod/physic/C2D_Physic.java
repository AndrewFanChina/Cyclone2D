package c2d.mod.physic;

import c2d.lang.math.type.C2D_SizeF;

public interface C2D_Physic
{
	/** 重力加速度,Y方向加速度 */
	public static float M_G = 3.4f;
	/** 世界大小，规定了宽度(X方向尺寸)和高度(地面坐标)*/
	public static C2D_SizeF WordSize=new C2D_SizeF(2000, 320);
}
