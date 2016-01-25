package c2d.mod;

import c2d.lang.math.type.C2D_SizeI;

/**
 * C2D��ܳ�������
 * @author AndrewFan
 */
public interface C2D_Consts
{

	// ͼ�㶨��
	/** ͼ�㶨��---�����ǲ�. */
	public static final byte LEVEL_PHYSICAL = 0;
	
	/** ͼ�㶨��---�ײ���β�. */
	public static final byte LEVEL_TILE_BG = 1;
	
	/** ͼ�㶨��---�ںϵ��β�. */
	public static final byte LEVEL_TILE_SUR = 2;
	
	/** ͼ�㶨��---���������. */
	public static final byte LEVEL_OBJ_BG = 3;
	
	/** ͼ�㶨��---ǰ�������. */
	public static final byte LEVEL_OBJ_FG = 4;
	
	/** ͼ�㶨��---�¼������. */
	public static final byte LEVEL_OBJ_EV = 5;

	// ê�㳣������


	
	/** ê�㶨��---��ֱ���¶���. */
	public static final int BOTTOM = 32;
	
	/** ê�㶨��---ˮƽ���ж���. */
	public static final int HCENTER = 1;
	
	/** ê�㶨��---ˮƽ�������. */
	public static final int LEFT = 4;
	
	/** ê�㶨��---ˮƽ���Ҷ���. */
	public static final int RIGHT = 8;
	
	/** ê�㶨��---��ֱ���϶���. */
	public static final int TOP = 16;
	
	/** ê�㶨��---��ֱ���ж���. */
	public static final int VCENTER = 2;

	/** ê�㶨��---ˮƽ��ֱ���ж���. */
	public static final int HVCENTER = HCENTER|VCENTER;
	
	/** ê�㶨��---Ĭ�Ͼ�����϶���. */
	public static final int DEF = 0;
	
	/** ��Դ�ļ��ж���Ŀ¼�� */
	public static final String STR_RES = "res/";
 	/** ͼƬ��׺��PNG*/
 	public static final String STR_IMG_PNG = ".png";
 	/** ͼƬ��׺��JPG*/
 	public static final String STR_IMG_JPG=".jpg";
	/**  ͼƬӳ����ļ���׺�� */
 	public static final String STR_IMG_PMT = ".pmt";
 	/**  ͼƬ���ļ���׺�� */
 	public static final String STR_PIC_FNT = ".picf";
 	/**  �������ļ���׺�� */
 	public static final String STR_PNT_FNT = ".pntf";

	 /**  �����ļ���ǿ�ƺ�׺�� */
	public static final String STR_SOUND_POSTFIX = null; 

	/** ������Դ�ļ������� */
	public static final String STR_C2D = "c2d/";
	
	/**  C2DͼƬ��Դ�ļ���ǰ׺. */
	public static final String STR_IMGS_ = "imgs_";

	/** ������Դ�ļ������� */
	public static final String STR_Fonts = "fnts/";
	
	/** ��ɢͼƬ��Դ�ļ������� */
	public static final String STR_OTHER_IMGS = "imgs_other/";
	
	/** ������Դ�ļ������� */
	public static final String STR_SOUNDS = "sounds/";
	
	/** �ı��ļ���׺���� */
	public static final String STR_TXT = "_texts.bin";
	
	/** �����ļ���׺���� */
	public static final String STR_PROP = "_prop.bin";
	
	/** �ű��ļ���׺���� */
	public static final String STR_SCRIPT = "_script.bin";
	
	/** �������ļ���׺���� */
	public static final String STR_C2DS = "_c2ds_"; 
	
	
	// ������ʽ���Ͷ���
	/** ���ͳ������� */
	public static final byte PARAM_INT = 0;
	
	/** �ַ��ͳ������� */
	public static final byte PARAM_STR = PARAM_INT + 1;
	
	/** ���ͱ�������(����Ϊָ�򹫹����ͱ�������ĳ��������ID) */
	public static final byte PARAM_INT_VAR = PARAM_STR + 1;
	
	/** �ַ��ͱ�������(����Ϊָ�򹫹��ַ���������ĳ��������ID) */
	public static final byte PARAM_STR_VAR = PARAM_INT_VAR + 1;
	
	/** ����ID�������� */
	public static final byte PARAM_INT_ID = (byte) (PARAM_STR_VAR + 1);
	
	/** �����Ͳ��� */
	public static final byte PARAM_PROP = (byte) (PARAM_INT_ID + 1);

	// ��������
	
	/** �������� */
	public static final byte FUN_TRIGGER = 0;
	
	/** �������� */
	public static final byte FUN_CONTEXT = FUN_TRIGGER + 1;
	
	/** ִ�к��� */
	public static final byte FUN_EXECUTION = FUN_CONTEXT + 1;
	/**
	 * TAG
	 */
	public static final String tag="c2d_debug";
	
	
	/** ��ť״̬-��״̬ --0*/
	public static final int Btn_Float=0;
	/** ��ť״̬-��ý���--1 */
	public static final int Btn_Focused=Btn_Float+1;
	/** ��ť״̬-��ʼ���£��ǰ�ť�ؼ�ֻ��Ӧ���״̬--2 */
	public static final int Btn_PressBegin=Btn_Focused+1;
	/** ��ť״̬-��ɰ���--3 */
	public static final int Btn_PressEnd=Btn_PressBegin+1;
	/** ��ť״̬-��ʼ�ͷ� --4*/
	public static final int Btn_ReleaseBegin=Btn_PressEnd+1;
	/** ��ť״̬-����ͷ�--5 */
	public static final int Btn_ReleaseEnd=Btn_ReleaseBegin+1;
	
	/** �Ƿ��д������� */
	public static boolean Plat_Tochable=true;
	/*
	 * �û�����ߴ磬���Լ�����ߴ絥λ�����������һ��Ļʱ����ĳߴ硣
	 * ���磬�����ϷͼƬΪ60*30�������Ϸ���綨��Ϊ120*90���ǵ���ͼƬ���������Ͻǽ���
	 * ����ʱ�����ں��������1/2��Ļ���������1/3��Ļ�������ܾ�����ֻ���Ļ��С�Ƕ��١�
	 */
	public static final C2D_SizeI User_Size = new C2D_SizeI(480, 800);
}
