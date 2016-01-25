package c2d.frame.com.data;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.data.unit_type.C2D_UnitType;
import c2d.frame.com.data.unit_type.C2D_UnitValue;

public interface C2D_Adapter_uiGroup
{
	public C2D_UnitType[] onDefineFormats();
	public C2D_UnitValue[][] onCreateItems();
	public void onItemClicked(C2D_Widget item,short spriteID,int widgetID);
}
