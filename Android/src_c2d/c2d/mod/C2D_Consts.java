package c2d.mod;

import c2d.lang.math.type.C2D_SizeI;

/**
 * C2D框架常量定义
 * @author AndrewFan
 */
public interface C2D_Consts
{

	// 图层定义
	/** 图层定义---物理标记层. */
	public static final byte LEVEL_PHYSICAL = 0;
	
	/** 图层定义---底层地形层. */
	public static final byte LEVEL_TILE_BG = 1;
	
	/** 图层定义---融合地形层. */
	public static final byte LEVEL_TILE_SUR = 2;
	
	/** 图层定义---背景对象层. */
	public static final byte LEVEL_OBJ_BG = 3;
	
	/** 图层定义---前景对象层. */
	public static final byte LEVEL_OBJ_FG = 4;
	
	/** 图层定义---事件对象层. */
	public static final byte LEVEL_OBJ_EV = 5;

	// 锚点常量定义


	
	/** 锚点定义---垂直居下对齐. */
	public static final int BOTTOM = 32;
	
	/** 锚点定义---水平居中对齐. */
	public static final int HCENTER = 1;
	
	/** 锚点定义---水平居左对齐. */
	public static final int LEFT = 4;
	
	/** 锚点定义---水平居右对齐. */
	public static final int RIGHT = 8;
	
	/** 锚点定义---垂直居上对齐. */
	public static final int TOP = 16;
	
	/** 锚点定义---垂直居中对齐. */
	public static final int VCENTER = 2;

	/** 锚点定义---水平垂直居中对齐. */
	public static final int HVCENTER = HCENTER|VCENTER;
	
	/** 锚点定义---默认居左居上对齐. */
	public static final int DEF = 0;
	
	/** 资源文件夹顶级目录名 */
	public static final String STR_RES = "res/";
 	/** 图片后缀名PNG*/
 	public static final String STR_IMG_PNG = ".png";
 	/** 图片后缀名JPG*/
 	public static final String STR_IMG_JPG=".jpg";
	/**  图片映射表文件后缀名 */
 	public static final String STR_IMG_PMT = ".pmt";
 	/**  图片字文件后缀名 */
 	public static final String STR_PIC_FNT = ".picf";
 	/**  点阵字文件后缀名 */
 	public static final String STR_PNT_FNT = ".pntf";

	 /**  声音文件的强制后缀名 */
	public static final String STR_SOUND_POSTFIX = null; 

	/** 数据资源文件夹名称 */
	public static final String STR_C2D = "c2d/";
	
	/**  C2D图片资源文件夹前缀. */
	public static final String STR_IMGS_ = "imgs_";

	/** 字体资源文件夹名称 */
	public static final String STR_Fonts = "fnts/";
	
	/** 零散图片资源文件夹名称 */
	public static final String STR_OTHER_IMGS = "imgs_other/";
	
	/** 音乐资源文件夹名称 */
	public static final String STR_SOUNDS = "sounds/";
	
	/** 文本文件后缀名称 */
	public static final String STR_TXT = "_texts.bin";
	
	/** 属性文件后缀名称 */
	public static final String STR_PROP = "_prop.bin";
	
	/** 脚本文件后缀名称 */
	public static final String STR_SCRIPT = "_script.bin";
	
	/** 触发包文件中缀名称 */
	public static final String STR_C2DS = "_c2ds_"; 
	
	
	// 参数格式类型定义
	/** 整型常量参数 */
	public static final byte PARAM_INT = 0;
	
	/** 字符型常量参数 */
	public static final byte PARAM_STR = PARAM_INT + 1;
	
	/** 整型变量参数(参数为指向公共整型变量表中某个变量的ID) */
	public static final byte PARAM_INT_VAR = PARAM_STR + 1;
	
	/** 字符型变量参数(参数为指向公共字符变量表中某个变量的ID) */
	public static final byte PARAM_STR_VAR = PARAM_INT_VAR + 1;
	
	/** 整型ID常量参数 */
	public static final byte PARAM_INT_ID = (byte) (PARAM_STR_VAR + 1);
	
	/** 属性型参数 */
	public static final byte PARAM_PROP = (byte) (PARAM_INT_ID + 1);

	// 函数类型
	
	/** 触发函数 */
	public static final byte FUN_TRIGGER = 0;
	
	/** 环境函数 */
	public static final byte FUN_CONTEXT = FUN_TRIGGER + 1;
	
	/** 执行函数 */
	public static final byte FUN_EXECUTION = FUN_CONTEXT + 1;
	/**
	 * TAG
	 */
	public static final String tag="c2d_debug";
	
	
	/** 按钮状态-空状态 --0*/
	public static final int Btn_Float=0;
	/** 按钮状态-获得焦点--1 */
	public static final int Btn_Focused=Btn_Float+1;
	/** 按钮状态-开始按下，非按钮控件只响应这个状态--2 */
	public static final int Btn_PressBegin=Btn_Focused+1;
	/** 按钮状态-完成按下--3 */
	public static final int Btn_PressEnd=Btn_PressBegin+1;
	/** 按钮状态-开始释放 --4*/
	public static final int Btn_ReleaseBegin=Btn_PressEnd+1;
	/** 按钮状态-完成释放--5 */
	public static final int Btn_ReleaseEnd=Btn_ReleaseBegin+1;
	
	/** 是否含有触屏功能 */
	public static boolean Plat_Tochable=true;
	/*
	 * 用户定义尺寸，你自己定义尺寸单位，代表填充满一屏幕时所需的尺寸。
	 * 比如，你的游戏图片为60*30，你的游戏世界定义为120*90，那当此图片以世界左上角进行
	 * 绘制时，将在横向方向填充1/2屏幕，纵向填充1/3屏幕。而不管具体的手机屏幕大小是多少。
	 */
	public static final C2D_SizeI User_Size = new C2D_SizeI(480, 800);
}
