package c2d.mod.sprite.tween.model;

import c2d.lang.math.type.C2D_PointF;
import c2d.lang.obj.C2D_Object;

public class C2D_ValueTransform extends C2D_Object
{
    public float rotateDegree;
    public float alpha;
    public C2D_PointF scale=new C2D_PointF();
    public C2D_PointF anchor=new C2D_PointF();
    public C2D_PointF pos=new C2D_PointF();
    public void setValue(C2D_FrameUnit unit)
    {
        rotateDegree = unit.rotateDegree;
        alpha = unit.alpha;
        scale.m_x = unit.scaleX;
        scale.m_y = unit.scaleY;
        anchor.m_x = unit.anchorX;
        anchor.m_y = unit.anchorY;
        pos.m_x = unit.posX;
        pos.m_y = unit.posY;
    }
	public void onRelease()
	{
		scale=null;
		anchor=null;
		pos=null;
	}
}
