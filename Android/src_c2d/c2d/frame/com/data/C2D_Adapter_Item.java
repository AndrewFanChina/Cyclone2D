package c2d.frame.com.data;

import c2d.frame.base.C2D_Widget;

public interface C2D_Adapter_Item
{
	public String[] onCreateItems();
	public void onItemClicked(C2D_Widget item,String caption,int id);
}
