package game.tutorial.c2d;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.data.unit_type.C2D_UT_PicBox;
import c2d.frame.com.data.unit_type.C2D_UT_PicTxt;
import c2d.frame.com.data.unit_type.C2D_UV_Int;
import c2d.frame.com.data.unit_type.C2D_UV_String;
import c2d.frame.com.data.unit_type.C2D_UnitType;
import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.frame.com.list.C2D_List;
import c2d.frame.com.list.C2D_ListStyle;
import c2d.frame.com.list.scroll.C2D_Scrollable;
import c2d.frame.com.text.C2D_PTC_DynW;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.event.C2D_Event_Scroll;
import c2d.frame.ext.scene.C2D_FpsScene;
import c2d.lang.math.type.C2D_PointI;
import c2d.mod.C2D_Consts;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

public class L05_04_ListScrollEvents extends C2D_FpsScene
{
	public L05_04_ListScrollEvents()
	{
	}
	public void onAddedToStage()
	{
		setBGImage("wodejiangpin.jpg");
//		createFPS(false);
		//设置列表的格式
		C2D_UnitType[] format = new C2D_UnitType[]
		{
			new C2D_UT_PicBox(5, 0, 0),
			new C2D_UT_PicTxt(70, 90, C2D_Consts.BOTTOM)
		};
		//创建列表
		C2D_List list=new C2D_List(format,C2D_TextFont.getDefaultFont());
		//设置列表的图片字体
		C2D_PTC_DynW config=new C2D_PTC_DynW();
		config.loadFromPTLib("prize");
		list.setPicTxtConfig(config);
		//设置列表的图标集合
//		list.setIconList(iconList);
		//设置列表的条目背景图
		list.setStyle(new C2D_ListStyle(new C2D_ImageClip("guangbiao.png"),
				new C2D_PointI(50, 50), C2D_Consts.HCENTER|C2D_Consts.VCENTER),null);
		//添加内容条目
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(0),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(1),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(2),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(3),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(4),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(0),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(1),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(2),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(3),new C2D_UV_String("1个")});
		list.addItem(new C2D_UnitValue[]{new C2D_UV_Int(4),new C2D_UV_String("1个")});
		addChild(list);
		list.setPosTo(80, 54);
		list.setSize(470, 320);
		list.setItemHeight(64);
		list.setItemGap(0);
		list.setFocusable(true);
		list.setScrollType(C2D_Scrollable.Scrool_Loop);
		this.setFocusedWidget(list);
		//添加滚动显示框
		C2D_PTC_DynW itemShowConfig=new C2D_PTC_DynW();
		itemShowConfig.loadFromPTLib("prizeNum");
		final C2D_PicTextBox itemShow=new C2D_PicTextBox(itemShowConfig);
		addChild(itemShow);
		itemShow.setPosTo(320, 35);
//		itemShow.setAutoSize(true);
		itemShow.setAnchor(C2D_Consts.HVCENTER);
		itemShow.setZOrder(10);
		itemShow.setText((list.getCurrentScroll()+1)+ "/"+list.getTotalRow());
		list.Events_Scroll().add(new C2D_Event_Scroll()
		{
			protected boolean doEvent(C2D_Widget carrier, int itemIndex, boolean scroll)
			{
				C2D_List myList = (C2D_List)carrier;
				if(scroll)
				{
				itemShow.setText( (itemIndex+1)+ "/"+myList.getTotalRow());
				}
				return false;
			}
		});
		orderChildren();
	}
	protected void onPaint(C2D_Graphics g)
	{
		super.onPaint(g);
		for (int i = 0; i < 10; i++)
		{
			g.drawRect(i*24, i*12, 400, 400, 0xFF);
		}
	}
	
}
