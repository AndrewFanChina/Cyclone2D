package c2d.mod.physic;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;

public class C2D_Recycler
{
	public void checkRecycle(C2D_Widget widget)
	{
		//ªÿ ’
		if(widget!=null)
		{
			if(!widget.m_inCamera||!widget.getVisible())
			{
				C2D_View view = widget.getParentNode();
				if(view!=null)
				{
					view.removeChild(widget);
				}
				return ;
			}
		}
	
	}
}
