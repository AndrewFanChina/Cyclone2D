package game.core;

import c2d.lang.app.C2D_Canvas;
import c2d.lang.app.C2D_Device;


public class Define
{
	// *****************************环境初始化*************************************
	public static void initEviroment(C2D_Canvas canvas)
	{
		// -----按键参数--------------------------------------------------------
		// #expand C2D_Device.setKeyValue(C2D_Device.key_up, %key.UP%);
		C2D_Device.setKeyValue(C2D_Device.key_up, 19);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_right, %key.RIGHT%);
		C2D_Device.setKeyValue(C2D_Device.key_right, 22);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_down, %key.DOWN%);
		C2D_Device.setKeyValue(C2D_Device.key_down, 20);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_left, %key.LEFT%);
		C2D_Device.setKeyValue(C2D_Device.key_left, 21);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_enter, %key.ENTER%);
		C2D_Device.setKeyValue(C2D_Device.key_enter, 66);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_LSoft, %key.SL%);
		C2D_Device.setKeyValue(C2D_Device.key_LSoft, -21);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_RSoft, %key.SR%);
		C2D_Device.setKeyValue(C2D_Device.key_RSoft, -22);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_pound, %key.POUND%);
		C2D_Device.setKeyValue(C2D_Device.key_pound, 18);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_star, %key.STAR%);
		C2D_Device.setKeyValue(C2D_Device.key_star, 17);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num0, %key.NUM0%);
		C2D_Device.setKeyValue(C2D_Device.key_num0, 7);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num1, %key.NUM1%);
		C2D_Device.setKeyValue(C2D_Device.key_num1, 8);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num2, %key.NUM2%);
		C2D_Device.setKeyValue(C2D_Device.key_num2, 9);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num3, %key.NUM3%);
		C2D_Device.setKeyValue(C2D_Device.key_num3, 10);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num4, %key.NUM4%);
		C2D_Device.setKeyValue(C2D_Device.key_num4, 11);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num5, %key.NUM5%);
		C2D_Device.setKeyValue(C2D_Device.key_num5, 12);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num6, %key.NUM6%);
		C2D_Device.setKeyValue(C2D_Device.key_num6, 13);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num7, %key.NUM7%);
		C2D_Device.setKeyValue(C2D_Device.key_num7, 14);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num8, %key.NUM8%);
		C2D_Device.setKeyValue(C2D_Device.key_num8, 15);
		// #expand C2D_Device.setKeyValue(C2D_Device.key_num9, %key.NUM9%);
		C2D_Device.setKeyValue(C2D_Device.key_num9, 16);
	}

}
