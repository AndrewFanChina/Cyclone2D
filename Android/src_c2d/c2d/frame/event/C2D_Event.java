package c2d.frame.event;

import c2d.lang.obj.C2D_Object;


/** 
 * C2D事件，须实现onDoEvent(C2D_Stage)方法。
 * 事件发生时，系统会传入有效的C2D_Stage对象
 * @author AndrewFan
 *
 */
public abstract class C2D_Event extends C2D_Object
{

	public void onRelease()
	{
	}
	
}
