package c2d.frame.ext.scene;

import c2d.frame.com.data.unit_type.C2D_UT_SysTxt;
import c2d.frame.com.data.unit_type.C2D_UV_String;
import c2d.frame.com.data.unit_type.C2D_UnitType;
import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.frame.com.list.C2D_List;
import c2d.frame.com.list.scroll.C2D_ScrollView;
import c2d.frame.event.C2D_Event_ClickItem;
import c2d.mod.C2D_Consts;
import c2d.plat.font.C2D_TextFont;

public abstract class C2D_ListScene extends C2D_FpsScene
{
	String m_items[];
	protected C2D_List m_list;
	public C2D_ListScene(String items[])
	{
		m_items = items;
	}
	/**
	 * 初始化列表项
	 * @param zOrder 列表的zOrder
	 */
	protected void initList(int zOrder)
	{
		if(m_list!=null)
		{
			return;
		}
		m_list=new C2D_List(new C2D_UnitType[]{new C2D_UT_SysTxt(50, 50, C2D_Consts.HVCENTER)},null);
		m_list.setAutoHeight(true);
		C2D_TextFont.getDefaultFont().loadChars(m_items);
		//从指定的id开始，加入各个条目
		if (m_items != null)
		{
			for (int i = 0; i < m_items.length; i++)
			{
				m_list.addItem(new C2D_UnitValue[]{new C2D_UV_String(m_items[i])});
			}
		}
		addChild(m_list,zOrder);
		m_list.setSize(400, 600);
		m_list.setToParentCenter();
		m_list.Events_ClickItem().clear();
		m_list.Events_ClickItem().add(new C2D_Event_ClickItem()
		{
			protected boolean doEvent(C2D_ScrollView scrollView, int itemID)
			{
				if(onItemClicked(itemID))
				{
					return true;	
				}
				return false;
			}
		});
		this.setFocusedWidget(m_list);   
	}

	public abstract boolean onItemClicked(int itemID);
	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_list = null;
	}
}
