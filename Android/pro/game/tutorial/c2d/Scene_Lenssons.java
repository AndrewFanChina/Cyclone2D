package game.tutorial.c2d;

import game.core.util.Scene_List;
import c2d.frame.base.C2D_Scene;
import c2d.lang.math.C2D_Array;

public class Scene_Lenssons extends Scene_List
{
	// ��ǰ��Ŀ
	static String Items[] = new String[]
	{ 
		"L01_01����̨�ͳ�������", "L01_02�����غ�ж����Դ",
		"L01_03����ͼ����״�ṹ", "L01_04��������ͼ��ȱ��",
		"L02_01����õ�ͼƬ��", "L02_02��ͼƬ��ͼƬ�п�",
		"L02_03����������״�ؼ�", "L02_04��ʹ��ϵͳ�ı���", 
		"L02_05��ʹ��ͼƬ�ı���", "L02_06����ť�밴ť�¼�",
		"L03_01�������ĸ����¼�", "L03_02���ؼ��İ����¼�",
		"L03_03�������������", "L03_04�������л��¼�",
		"L03_05�����泡�����¼�",
		"L04_01�����鶯���Ŀ���", "L04_02�����鶯�����¼�",
		"L05_01����Ƭͼʹ�÷���", "L05_02����Ƭͼʹ�÷���",
		"L05_03���б��ʹ�÷���", "L05_04���б�������¼�",
		"L05_05���б������¼�",	 "L05_06�������ı������", 
		"L05_07�����鰴ť���¼�", "L05_08��ѡ���ʹ�÷���",
		"L06_01������ͼƬ���б�", "L06_02��ͼƬ�ı����ñ�",
		"L06_03����Դ�Ķ�Ȩ����",
		"L07_01�������ϵĶԻ���", "L07_02���ѺõĹ��ɳ���",
		"L07_03�������ı���Ͽ�", "L07_04��ѡ����Ͽ����¼�",
	};
	C2D_Array m_lessons;

	public Scene_Lenssons()
	{
		super(Items);
	}
	public void onAddedToStage()
	{
		super.onAddedToStage();
		if(m_lessons==null)
		{
			m_lessons = new C2D_Array();
			m_lessons.addElement(new L01_01_Scene());
			m_lessons.addElement(new L01_02_LoadAndUnload());
			m_lessons.addElement(new L01_03_View());
			m_lessons.addElement(new L01_04_BufferedView());
			m_lessons.addElement(new L02_01_PictrueBox());
			m_lessons.addElement(new L02_02_ImageAndClip());
			m_lessons.addElement(new L02_03_Shapes());
			m_lessons.addElement(new L02_04_TxtBox());
			m_lessons.addElement(new L02_05_PictureTxtBox());
			m_lessons.addElement(new L02_06_ButtomAndEvent());
			m_lessons.addElement(new L03_01_UpdateEvent());
			m_lessons.addElement(new L03_02_KeyPressEvent());
			m_lessons.addElement(new L03_03_Focus());
			m_lessons.addElement(new L03_04_ChangeFocusEvent());
			m_lessons.addElement(new L03_05_SceneEvent());
			m_lessons.addElement(new L04_01_Sprites());
		}
		
	}

	public boolean onItemClicked(int itemID)
	{
		openScene((C2D_Scene) m_lessons.elementAt(itemID));
		return true;
	}

	public void onRelease()
	{
		super.onRelease();
		if(m_lessons!=null)
		{
			int size=m_lessons.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Scene si=(C2D_Scene)m_lessons.elementAt(i);
				if(si!=null)
				{
					si.doRelease(this);
				}
			}
			m_lessons.clear();
			m_lessons=null;
		}
	}
	
}
