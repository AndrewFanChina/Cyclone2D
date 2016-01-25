package c2d.frame.util;

import c2d.frame.com.list.C2D_ListStyle;
import c2d.lang.obj.C2D_ObjectHandler;
import c2d.mod.sprite.C2D_Sprite;


public class C2D_WidgetUtil implements C2D_ObjectHandler
{
	/*--------------------- ���峣�õ�UI��� ---------------------*/
	//��ѡ��״̬���б�ѡ��
	public static C2D_ListStyle listStyle_com=new C2D_ListStyle(0x230102, 0x781905);
	//ѡ��״̬���б�ѡ��
	public static C2D_ListStyle listStyle_fox=new C2D_ListStyle(0x3D2222, 0xFFFFFF);
	
	//����
	private static C2D_WidgetUtil INST=new C2D_WidgetUtil();
	private C2D_WidgetUtil()
	{
	}
	/**
	 * �ڳ����ʼ���ڼ����
	 */
	public static void Init()
	{
		listStyle_com.transHadler(INST);
		listStyle_fox.transHadler(INST);
	}
	/**
	 * ֻ�����˳���������ʱ����
	 */
	public static void DoRelease()
	{
		listStyle_com.doRelease(INST);
		listStyle_fox.doRelease(INST);
	}
	
	public static void releaseWidget(C2D_Sprite widget)
	{
		if(widget!=null)
		{
			widget.doRelease();
			widget=null;
		}
	}
	public static void releaseWidget(C2D_Sprite []widgets)
	{
		if(widgets!=null)
		{
			for (int i = 0; i < widgets.length; i++)
			{
				if(widgets[i]!=null)
				{
					widgets[i].doRelease();
					widgets[i]=null;
				}
			}
			widgets=null;
		}
	}
}
