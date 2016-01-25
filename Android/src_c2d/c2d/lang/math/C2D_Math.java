package c2d.lang.math;

import java.util.Random;

import c2d.lang.math.type.C2D_SizeI;
import c2d.lang.util.debug.C2D_Debug;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex2;
import c3d.util.math.C3D_Vertex3;

/**
 * 数学类
 */
public class C2D_Math
{
	public static final double PI = 3.141592653589793;
	public static final double E = 2.718281828459045;
	private static C3D_Vertex3 vFL_Cross = new C3D_Vertex3();
	private static C3D_Vertex2 vBufT0 = new C3D_Vertex2();
	private static C3D_Vertex2 vBufT1 = new C3D_Vertex2();
	private static C3D_Vertex2 vBufT2 = new C3D_Vertex2();
	private static C3D_Vertex2 vBufT3 = new C3D_Vertex2();

	// 弧度到度数转换
	public static float radiansToDegrees(float radians)
	{
		return (float) (180.0f * radians / PI);
	}

	// 度数到弧度转换
	public static float degreesToRadians(float degrees)
	{
		return (float) (PI * degrees / 180);
	}

	/**
	 * 浮点数的快速开平方函数.
	 * 
	 * @param value
	 *            输入数值
	 * @return 平方根
	 */
	public static float sqrt(float value)
	{
		// float f1= intToFloat(0x1FBCF800 + (floatToInt(value) >> 1));
		// float f2= intToFloat(0x5f3759df - (floatToInt(value) >> 1));
		// return 0.5f*(f1 + (value * f2));
		return (float) sqrt((double) value);
	}

	/**
	 * 浮点数的快速开平方函数.这里内部会转成单精度浮点，因此注意会丢失精度
	 * 
	 * @param value
	 *            输入数值
	 * @return 平方根
	 */
	public static double sqrt(double value)
	{
		return Math.sqrt(value);
	}

	/**
	 * 绝对值.
	 * 
	 * @param number
	 *            数值
	 * @return 绝对值
	 */
	public static long abs(long number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * 绝对值.
	 * 
	 * @param number
	 *            数值
	 * @return 绝对值
	 */
	public static int abs(int number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * 绝对值.
	 * 
	 * @param number
	 *            数值
	 * @return 绝对值
	 */
	public static float abs(float number)
	{
		if (number < 0)
			number = -number;
		return number;
	}
	/**
	 * 绝对值.
	 * 
	 * @param number
	 *            数值
	 * @return 绝对值
	 */
	public static double abs(double number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * max 求较大值.
	 * 
	 * @param num1
	 *            long 数值1
	 * @param num2
	 *            long 数值2
	 * @return long 较大数值
	 */
	public static long max(long num1, long num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max 求较大值.
	 * 
	 * @param num1
	 *            数值1
	 * @param num2
	 *            数值2
	 * @return 较大数值
	 */
	public static int max(int num1, int num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max 求较大值.
	 * 
	 * @param num1
	 *            数值1
	 * @param num2
	 *            数值2
	 * @return 较大数值
	 */
	public static float max(float num1, float num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max 求较小值.
	 * 
	 * @param num1
	 *            数值1
	 * @param num2
	 *            数值2
	 * @return 较小数值
	 */
	public static long min(long num1, long num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * min 求较小值.
	 * 
	 * @param num1
	 *            数值1
	 * @param num2
	 *            数值2
	 * @return 较小数值
	 */
	public static int min(int num1, int num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * min 求较小值.
	 * 
	 * @param num1
	 *            数值1
	 * @param num2
	 *            数值2
	 * @return 较小数值
	 */
	public static float min(float num1, float num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * @todo 求直线和平面的交点 L0，L1 直线端点 Fn平面的法向量 Fp平面上的任意一个点 返回 交点坐标
	 * */
	public static C3D_Vertex3 getCross(C3D_Vertex3 L0, C3D_Vertex3 L1, C3D_Vector3 Fn, C3D_Vertex3 Fp)
	{
		float x0 = L0.x;
		float y0 = L0.y;
		float z0 = L0.z;
		float x1 = L1.x;
		float y1 = L1.y;
		float z1 = L1.z;
		float x3 = Fp.x;
		float y3 = Fp.y;
		float z3 = Fp.z;
		float x4 = Fn.x;
		float y4 = Fn.y;
		float z4 = Fn.z;
		float x, y, z, temp;
		try
		{
			if (x1 == x0)
			{
				x = x0;
				if (y1 == y0)
				{
					temp = z4;
					if (temp == 0)
						return null;

					y = y0;
					z = z3 - ((x0 - x3) * x4 + (y0 - y3) * y4) / temp;
				}
				else
				{
					temp = (y4 * (y1 - y0) + z4 * (z1 - z0));
					if (temp == 0)
						return null;

					y = (y0 * z4 * (z1 - z0) + (y1 - y0) * (y3 * y4 - x4 * (x0 - x3) - z4 * (z0 - z3))) / temp;
					z = (y - y0) * (z1 - z0) / (y1 - y0) + z0;
				}
			}
			else
			{
				temp = (x4 * (x1 - x0) + y4 * (y1 - y0) + z4 * (z1 - z0));
				if (temp == 0)
					return null;

				x = (x0 * (y4 * (y1 - y0) + z4 * (z1 - z0)) + (x1 - x0) * (x3 * x4 - y4 * (y0 - y3) - z4 * (z0 - z3))) / temp;
				y = (x - x0) * (y1 - y0) / (x1 - x0) + y0;
				z = (x - x0) * (z1 - z0) / (x1 - x0) + z0;
			}
			vFL_Cross.setValue(x, y, z);
			return vFL_Cross;
		}
		catch (ArithmeticException e)
		{
			C2D_Debug.logErr("count error in getCross:" + e.getMessage());
		}
		return null;
	}

	// 判断网格构成是否顺时针方向(0->1->2,1->2->0,2->0->1为顺时针，其余为逆时针)
	/**
	 * Checks if is deasil.
	 * 
	 * @param n0
	 *            the n0
	 * @param n1
	 *            the n1
	 * @param n2
	 *            the n2
	 * @return true, if is deasil
	 */
	public static boolean isDeasil(int n0, int n1, int n2)
	{
		boolean res = false;
		if ((n0 == 0 && n1 == 1 && n2 == 2) || (n0 == 1 && n1 == 2 && n2 == 0) || (n0 == 2 && n1 == 1 && n2 == 0))
		{
			res = true;
		}
		return res;
	}

	/**
	 * 将角度调整到[0,2PI)区间
	 * 
	 * @param θ
	 *            the θ
	 * @return the float
	 */
	public static float standirdDegree(float θ)
	{
		// 调整 angle 到标准区间[0,360)
		if (θ < 0)
		{
			θ = 360 - θ;
			θ %= 360;
			θ = 360 - θ;
		}
		else
		{
			θ %= 360;
		}
		return θ;
	}

	/**
	 * 将弧度调整到[0,2PI)区间
	 * 
	 * @param θ
	 *            the θ
	 * @return the long
	 */
	public static double standirdAngle(double θ)
	{
		// 调整 angle 到标准区间[0,360)
		if (θ < 0)
		{
			θ = PI * 2 - θ;
			θ %= PI * 2;
			θ = PI * 2 - θ;
		}
		else
		{
			θ %= PI * 2;
		}
		return θ;
	}

	/**
	 * 将弧度调整到[-PI,PI)区间
	 * 
	 * @param θ
	 *            the θ
	 * @return the long
	 */
	public static double standirdAngle2(double θ)
	{
		// 调整 angle 到标准区间[-PI,PI)
		if (θ < -PI)
		{
			θ += ((-PI - θ) / (PI * 2) + 1) * PI * 2;
		}
		else if (θ >= PI)
		{
			θ -= ((θ - PI) / (PI * 2) + 1) * PI * 2;
		}
		return θ;
	}

	/**
	 * 在180度以内比较弧度A和B
	 * 
	 * @param θA
	 *            the θ a
	 * @param θB
	 *            the θ b
	 * @return true, if successful
	 */
	// TRUE A左边|FALSE A右边
	public static boolean left_Right(double θA, double θB)
	{
		θA = standirdAngle(θA);
		θB = standirdAngle(θB);
		if (C2D_Math.abs(θA - θB) < PI)
		{
			return θA > θB;
		}
		else
		{
			return θA < θB;
		}
	}

	/**
	 * 在180度以内计算角度A和B的夹角,返回值是正值
	 * 
	 * @param θA
	 *            the θ a
	 * @param θB
	 *            the θ b
	 * @return the long
	 */
	public static double clipAngle(double θA, double θB)
	{
		θA = standirdAngle(θA);
		θB = standirdAngle(θB);
		double abs = C2D_Math.abs(θA - θB);
		if (abs < PI)
		{
			return abs;
		}
		else
		{
			return PI * 2 - abs;
		}
	}

	/**
	 * 在180度以内计算弧度A和B的夹角,返回值左正右负
	 * 
	 * @param θA
	 *            the θ a
	 * @param θB
	 *            the θ b
	 * @return the long
	 */
	public static double gapAngle(double θA, double θB)
	{
		if (left_Right(θA, θB))
		{
			return clipAngle(θA, θB);
		}
		else
		{
			return -clipAngle(θA, θB);
		}
	}

	/** @todo 计算两条2维线段的焦点(P0,P1)(P2,P3)是笛卡尔坐标系中的坐标，非屏幕坐标 */
	public static C3D_Vertex2 getCross_V2D(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean whetherCross)
	{
		if (whetherCross)
		{
			// 判断有交点
			vBufT0.setValue(x3 - x0, y3 - y0);
			vBufT1.setValue(x2 - x0, y2 - y0);
			vBufT2.setValue(x1 - x0, y1 - y0);
			if (getCode(vBufT2.outerProduct(vBufT1)) * getCode(vBufT2.outerProduct(vBufT0)) >= 0)
			{
				return null;
			}
			vBufT0.setValue(x0 - x2, y0 - y2);
			vBufT1.setValue(x1 - x2, y1 - y2);
			vBufT2.setValue(x3 - x2, y3 - y2);
			if (getCode(vBufT2.outerProduct(vBufT1)) * getCode(vBufT2.outerProduct(vBufT0)) >= 0)
			{
				return null;
			}
		}
		// 求出交点
		float xDown = (x3 - x2) * (y1 - y0) - (y3 - y2) * (x1 - x0);
		if (xDown == 0)
		{
			// System.out.println("xDown is 0");
			return null;
		}
		float x = ((x1 - x0) * (x3 * y2 - x3 * y0 + x2 * y0 - x2 * y3) + x0 * (y1 - y0) * (x3 - x2)) / xDown;
		float yDown = (y3 - y2) * (x1 - x0) - (x3 - x2) * (y1 - y0);
		if (yDown == 0)
		{
			// System.out.println("yDown is 0");
			return null;
		}
		float y = ((y1 - y0) * (y3 * x2 - y3 * x0 + y2 * x0 - y2 * x3) + y0 * (x1 - x0) * (y3 - y2)) / yDown;
		vBufT3.setValue(x, y);
		return vBufT3;
	}

	// 判断P0在P1、P2、P3组成的三角形内，传入笛卡尔坐标
	public static boolean inTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3)
	{
		// 判断P1P0向量方向在P1P2和P1P3之间，即P0点处于P1P2和P1P3的发射角内
		vBufT0.setValue(x0 - x1, y0 - y1);
		vBufT2.setValue(x2 - x1, y2 - y1);
		vBufT3.setValue(x3 - x1, y3 - y1);
		if (vBufT0.outerProduct(vBufT2) * vBufT0.outerProduct(vBufT3) >= 0)
		{
			// System.out.println("not in triangle");
			return false;
		}
		// 判断P2P0向量方向在P2P1和P2P3之间，即P0点处于P2P1和P2P3的发射角内
		vBufT0.setValue(x0 - x2, y0 - y2);
		vBufT2.setValue(x1 - x2, y1 - y2);
		vBufT3.setValue(x3 - x2, y3 - y2);
		if (vBufT0.outerProduct(vBufT2) * vBufT0.outerProduct(vBufT3) >= 0)
		{
			// System.out.println("not in triangle");
			return false;
		}
		// System.out.println("in triangle");
		return true;
	}

	// 正弦函数
	public static double sin(double θ)
	{
		return Math.sin(θ);
	}

	// 余弦函数
	public static double cos(double θ)
	{
		return Math.cos(θ);
	}

	/**
	 * 在180度以内计算角度A和B的夹角,返回值左正右负
	 * 
	 * @param θA
	 *            the θ a
	 * @param θB
	 *            the θ b
	 * @return the long
	 */
	public static float gapAngleDegree(float θA, float θB)
	{
		return radiansToDegrees((float) gapAngle(degreesToRadians(θA), degreesToRadians(θB)));
	}

	/**
	 * 计算N次幂
	 * 
	 * @param x
	 *            the x
	 * @param n
	 *            the n
	 * @return the long
	 */
	public static float pow(float x, int n)
	{
		if (n == 0)
			return 1;
		if (n == 1)
			return x;
		if (n % 2 == 0)// 是否 偶数，
		{
			return pow(x * x, n / 2);
		}
		else
		{
			return pow(x * x, (n - 1) / 2) * x;
		}
	}

	/**
	 * 计算N次幂
	 * 
	 * @param x
	 *            the x
	 * @param n
	 *            the n
	 * @return the double
	 */
	public static double pow(double x, double n)
	{
		if (n == 0)
			return 1;
		if (n == 1)
			return x;
		if (n % 2 == 0)// 是否 偶数，
		{
			return pow(x * x, n / 2);
		}
		else
		{
			return pow(x * x, (n - 1) / 2) * x;
		}
	}

	// 线段交叉测试
	// static
	// {
	// System.out.println(linCoss1(0, 2, 3, 2, 1, -2, 1, 2));
	// System.out.println(linCoss(-1, 2, 1, 3, -1, 1, 1, 4));
	// System.out.println(linCoss(-1, 2, 1, 3, -1, 3, 1, 2));
	// System.out.println(linCoss(-1, 2, 1, 3, 2, 0, 2, 9));
	// }
	private static float TA[] = new float[4];
	private static float TB[] = new float[4];

	/**
	 * 判断两条线段是否相交(0,1)与(2,3),如果一个点在另外一条线段上也算作相交
	 * 
	 * @param x0
	 *            the x0
	 * @param y0
	 *            the y0
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @return 是否发生碰撞
	 */
	public static boolean linCoss(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3)
	{
		TB[0] = (x1 - x0) * (y3 - y0) - (x3 - x0) * (y1 - y0);
		if (TB[0] == 0)
		{
			if (inRegionClose(x3, x0, x1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		TB[1] = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
		if (TB[1] == 0)
		{
			if (inRegionClose(x2, x0, x1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		TB[2] = (x3 - x2) * (y0 - y2) - (x0 - x2) * (y3 - y2);
		if (TB[2] == 0)
		{
			if (inRegionClose(x0, x2, x3))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		TB[3] = (x3 - x2) * (y1 - y2) - (x1 - x2) * (y3 - y2);
		if (TB[3] == 0)
		{
			if (inRegionClose(x1, x2, x3))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		for (int i = 0; i < TA.length; i++)
		{
			if (TB[i] > 0)
			{
				TA[i] = 1;
			}
			else if (TB[i] < 0)
			{
				TA[i] = -1;
			}
			else
			{
				TA[i] = 0;
			}
		}
		if (TA[0] * TA[1] < 0 && TA[2] * TA[3] < 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断两条线段是否相交(0,1)与(2,3),如果点1在线段23上也算作相交，其余共点不算
	 * 
	 * @param x0
	 *            the x0
	 * @param y0
	 *            the y0
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @return 是否发生碰撞
	 */
	public static boolean linCoss1(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3)
	{
		TA[0] = (x1 - x0) * (y3 - y0) - (x3 - x0) * (y1 - y0);
		TA[1] = (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
		TA[2] = (x3 - x2) * (y0 - y2) - (x0 - x2) * (y3 - y2);
		TA[3] = (x3 - x2) * (y1 - y2) - (x1 - x2) * (y3 - y2);
		if (TA[4] == 0)
		{
			if (inRegionClose(x1, x2, x3))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		for (int i = 0; i < TA.length; i++)
		{
			if (TA[i] > 0)
			{
				TA[i] = 1;
			}
			else if (TA[i] < 0)
			{
				TA[i] = -1;
			}
			else
			{
				TA[i] = 0;
			}
		}
		if (TA[0] * TA[1] < 0 && TA[2] * TA[3] < 0)
		{
			return true;
		}
		return false;
	}

	// 获得正负号
	/**
	 * Gets the code.
	 * 
	 * @param number
	 *            the number
	 * @return the code
	 */
	private static float getCode(float number)
	{
		if (number > 0)
		{
			number = 1;
		}
		else if (number < 0)
		{
			number = -1;
		}
		else
		{
			number = 0;
		}
		return number;
	}

	/**
	 * 判断有向线段(2,3)在(0,1)的正向(逆时针)还是负向
	 * 
	 * @param x0
	 *            the x0
	 * @param y0
	 *            the y0
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @return true, if successful
	 */
	public static boolean leftOrRight(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3)
	{
		float xa = (x1 - x0);
		float ya = (y1 - y0);
		float xb = (x3 - x2);
		float yb = (y3 - y2);
		return xa * yb - xb * ya > 0;
	}

	// --------------------------------数学函数--------------------------------
	//
	/**
	 * 数值是否位于某个开区间
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return true, if successful
	 */
	public static final boolean inRegion(int num, int num0, int num1)
	{
		return Math.min(num0, num1) < num && Math.max(num0, num1) > num;
	}

	/**
	 * 数值是否位于某个闭区间
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return true, if successful
	 */
	public static final boolean inRegionClose(int num, int num0, int num1)
	{
		return Math.min(num0, num1) <= num && Math.max(num0, num1) >= num;
	}

	/**
	 * 数值是否位于某个开区间
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return true, if successful
	 */
	public static final boolean inRegion(float num, float num0, float num1)
	{
		return min(num0, num1) < num && max(num0, num1) > num;
	}

	/**
	 * 数值是否位于某个闭区间
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return true, if successful
	 */
	public static final boolean inRegionClose(float num, float num0, float num1)
	{
		return min(num0, num1) <= num && max(num0, num1) >= num;
	}

	/**
	 * 碰撞函数
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param w1
	 *            the w1
	 * @param h1
	 *            the h1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param w2
	 *            the w2
	 * @param h2
	 *            the h2
	 * @return true, if successful
	 */
	public static final boolean collideWith(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
	{
		if (x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断两个矩形是否碰撞
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param w1
	 *            the w1
	 * @param h1
	 *            the h1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param w2
	 *            the w2
	 * @param h2
	 *            the h2
	 * @return true, if successful
	 */
	public static final boolean collideWith(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2)
	{
		if (x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断两矩形是否碰撞快速算法 x1,y1 - 矩形A左上角坐标 x2,y2 - 矩形A右下角坐标 x3,y3 - 矩形B左上角坐标 x4,y4 -
	 * 矩形B右下角坐标.
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @param x4
	 *            the x4
	 * @param y4
	 *            the y4
	 * @return true, if is rect crossing
	 */
	public static final boolean isRectCrossing(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		if (x1 >= x4)
			return false;
		if (x2 <= x3)
			return false;
		if (y1 >= y4)
			return false;
		if (y2 <= y3)
			return false;
		return true;
	}

	/**
	 * 判断两矩形是否碰撞快速算法 x1,y1 - 矩形A左上角坐标 x2,y2 - 矩形A右下角坐标 x3,y3 - 矩形B左上角坐标 x4,y4 -
	 * 矩形B右下角坐标.
	 * 
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @param x4
	 *            the x4
	 * @param y4
	 *            the y4
	 * @return true, if is rect crossing
	 */
	public static final boolean isRectCrossing(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
	{
		if (x1 >= x4)
			return false;
		if (x2 <= x3)
			return false;
		if (y1 >= y4)
			return false;
		if (y2 <= y3)
			return false;
		return true;
	}

	/**
	 * 将数值控制在某个闭区间value = [min,max]
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return the int
	 */
	public static final int limitNumber(int num, int num0, int num1)
	{
		if (num < Math.min(num0, num1))
		{
			num = Math.min(num0, num1);
		}
		if (num > Math.max(num0, num1))
		{
			num = Math.max(num0, num1);
		}
		return num;
	}

	/**
	 * 将数值控制在某个闭区间value = [min,max]
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return the long
	 */
	public static final long limitNumber(long num, long num0, long num1)
	{
		if (num < Math.min(num0, num1))
		{
			num = Math.min(num0, num1);
		}
		if (num > Math.max(num0, num1))
		{
			num = Math.max(num0, num1);
		}
		return num;
	}
	/**
	 * 将数值控制在某个闭区间value = [min,max]
	 * 
	 * @param num
	 *            the num
	 * @param num0
	 *            the num0
	 * @param num1
	 *            the num1
	 * @return the int
	 */
	public static final float limitNumber(float num, float num0, float num1)
	{
		if (num < Math.min(num0, num1))
		{
			num = Math.min(num0, num1);
		}
		if (num > Math.max(num0, num1))
		{
			num = Math.max(num0, num1);
		}
		return num;
	}

	public static final double limitNumber(double num, double num0, double num1)
	{
		if (num < Math.min(num0, num1))
		{
			num = Math.min(num0, num1);
		}
		if (num > Math.max(num0, num1))
		{
			num = Math.max(num0, num1);
		}
		return num;
	}

	// 获取向量的角度[0-360)，x,y为笛卡尔坐标系中的坐标(向右+X向上+Y)
	public static float getVectorDegree(float x, float y)
	{
		double s = x * x + y * y;
		if (s == 0)
		{
			return 0.0f;
		}
		s = Math.sqrt(s);
		double sin = y / s;
		sin = limitNumber((double) sin, -1.0d, 1.0d);
		double value = actSin(sin);
		if (x > 0 && y == 0)
		{
			value = 0;
		}
		else if (x > 0 && y > 0)
		{

		}
		else if (x == 0 && y > 0)
		{
			value = PI / 2;
		}
		else if (x < 0 && y > 0)
		{
			value = PI - value;
		}
		else if (x < 0 && y == 0)
		{
			value = PI;
		}
		else if (x < 0 && y < 0)
		{
			value = PI - value;
		}
		else if (x == 0 && y < 0)
		{
			value = PI * 3 / 2;
		}
		else if (x > 0 && y < 0)
		{
			value = 2 * PI + value;
		}
		return radiansToDegrees((float) (value % (2 * PI)));
	}
	/** 随机数--------------------------------------- */
	private static final Random myRandom = new Random();

	/**
	 * 获得随机数[0,srcNum)
	 * 
	 * @param srcNum
	 *            the src num
	 * @return the random
	 */
	public static final int getRandom(int srcNum)
	{
		if (srcNum <= 0)
		{
			return 0;
		}
		return Math.abs(myRandom.nextInt()) % srcNum;
	}

	/**
	 * 获得随机数[srcNum1,srcNum2)
	 * 
	 * @param srcNum1
	 *            the src num1
	 * @param srcNum2
	 *            the src num2
	 * @return the random
	 */
	public static final int getRandom(int srcNum1, int srcNum2)
	{
		if (srcNum1 >= srcNum2)
		{
			return srcNum2;
		}
		return srcNum1 + (Math.abs(myRandom.nextInt()) % (srcNum2 - srcNum1));
	}

	/**
	 * 获得随机数[srcNum1,srcNum2)
	 * 
	 * @param srcNum1
	 *            the src num1
	 * @param srcNum2
	 *            the src num2
	 * @return the random
	 */
	public static final float getRandom(float srcNum1, float srcNum2)
	{
		if (srcNum1 >= srcNum2)
		{
			return srcNum2;
		}
		float addF = srcNum2 - srcNum1;
		int addI = (int) addF;
		float afterDot = addF - addI;
		int afterDotI = (int) afterDot * 1024;
		float res = srcNum1 + getRandom(0, addI) + getRandom(0, afterDotI) / 1024.0f;
		return res;
	}

	/**
	 * 获得随机数[srcNum1,srcNum2)
	 * 
	 * @param srcNum1
	 *            the src num1
	 * @param srcNum2
	 *            the src num2
	 * @return the random
	 */
	public static final double getRandom(double srcNum1, double srcNum2)
	{
		return getRandom((float) srcNum1, (float) srcNum2);
	}

	/**
	 * 获得无重复的随机数数组[srcNum1,srcNum2]
	 * 
	 * @param srcNum1
	 *            the src num1
	 * @param srcNum2
	 *            the src num2
	 * @param count
	 *            the count
	 * @return the no repeated random
	 */
	public static final int[] getNoRepeatedRandom(int srcNum1, int srcNum2, int count)
	{
		int maxCount = Math.abs(srcNum1 - srcNum2) + 1;
		int minNumber = Math.min(srcNum1, srcNum2);
		if (count > maxCount)
		{
			return null;
		}
		int datas[] = new int[count];
		// 建立随机数组
		for (int i = 0; i < datas.length; i++)
		{
			int index = getRandom(maxCount - i);
			// 寻找随机索引
			for (int j = 0; j < maxCount; j++)
			{
				boolean used = false;
				// 测试重复
				for (int k = 0; k < i; k++)
				{
					int data = j + minNumber;
					if (data == datas[k])
					{
						used = true;
						break;
					}
				}
				if (!used)
				{
					if (index == 0)
					{ // 找到
						index = j;
						break;
					}
					else
					{
						index--;
					}
				}
			}
			// 处理
			datas[i] = index + minNumber;
		}
		return datas;
	}

	/**
	 * 生成随机数数组
	 * 
	 * @param number
	 *            随机数个数
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return 随机数数组
	 */
	public static int[] getRandom(int number, int min, int max)
	{
		if (number <= 0 || min > max)
		{
			return null;
		}
		int ranData[] = new int[number];
		int temp;
		for (int i = 0; i < ranData.length; i++)
		{
			temp = myRandom.nextInt();
			if (temp < 0)
			{
				temp = -temp;
			}
			ranData[i] = min + temp % (max - min);
		}
		return ranData;
	}

	/**
	 * 判断[x,y]是否在区域[_x,_y,_w,_h]
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param _x
	 *            the _x
	 * @param _y
	 *            the _y
	 * @param _w
	 *            the _w
	 * @param _h
	 *            the _h
	 * @return true, if successful
	 */
	public static final boolean inRegion(float x, float y, float _x, float _y, float _w, float _h)
	{
		if (x >= _x && x <= _x + _w && y >= _y && y <= _y + _h)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断[x,y]是否在区域[_x,_y,_w,_h]
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param _x
	 *            the _x
	 * @param _y
	 *            the _y
	 * @param _w
	 *            the _w
	 * @param _h
	 *            the _h
	 * @return true, if successful
	 */
	public static final boolean inRegion(double x, double y, double _x, double _y, double _w, double _h)
	{
		if (x >= _x && x <= _x + _w && y >= _y && y <= _y + _h)
		{
			return true;
		}
		return false;
	}

	/**
	 * 获取随机的颜色
	 * 
	 * @return 随机的颜色
	 */
	public static int getRandomColor()
	{
		return getRandomColor(0xFF);
	}

	/**
	 * 获取基于指定原色阈值的随机颜色，比如0xCC，将从0xCC中随机出三原色。
	 * 
	 * @param colorUnit
	 *            原色阈值
	 * @return 随机颜色
	 */
	public static int getRandomColor(int colorUnit)
	{
		if (colorUnit < 0)
		{
			colorUnit = -colorUnit;
		}
		colorUnit = colorUnit % 256;
		return 0xFF<<24|(getRandom(colorUnit) << 16) | (getRandom(colorUnit) << 8) | (getRandom(colorUnit) << 0);
	}

	/**
	 * 判断[x,y]是否在区域[_x,_y,_w,_h]
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param _x
	 *            the _x
	 * @param _y
	 *            the _y
	 * @param _w
	 *            the _w
	 * @param _h
	 *            the _h
	 * @return true, if successful
	 */
	public static final boolean inRegion(int x, int y, int _x, int _y, int _w, int _h)
	{
		if (x >= _x && x <= _x + _w && y >= _y && y <= _y + _h)
		{
			return true;
		}
		return false;
	}

	private static int arrayIndex[] = null;

	/**
	 * 冒泡法从大到小排序数组,大数优先排序数组，返回对应项在所有项中的排位。如[30,20,40]返回[1,2,0]
	 * 
	 * @param arry
	 *            the arry
	 */
	public static final void orderNumbers(int arry[])
	{
		orderNumbers(arry, arry.length);
	}

	/**
	 * 冒泡法从大到小排序数组,大数优先排序数组，返回对应项在所有项中的排位。 如[30,20,40]返回[1,2,0],只排序前N项
	 * 
	 * @param arry
	 *            即将被排序的数组
	 * @param nbItem
	 *            前多少项
	 */
	public static final void orderNumbers(int arry[], int nbItem)
	{
		if (arrayIndex == null || arrayIndex.length != arry.length)
		{
			arrayIndex = new int[arry.length];
		}
		for (int i = 0; i < nbItem; i++)
		{
			arrayIndex[i] = i;
		}
		int t;
		for (int i = 0; i < nbItem - 1; i++)
		{
			for (int j = i + 1; j < nbItem; j++)
			{
				if (arry[j] > arry[i])
				{
					t = arry[j];
					arry[j] = arry[i];
					arry[i] = t;
					t = arrayIndex[j];
					arrayIndex[j] = arrayIndex[i];
					arrayIndex[i] = t;
				}
			}
		}
		for (int i = 0; i < nbItem; i++)
		{
			arry[arrayIndex[i]] = i;
		}
	}

	/**
	 * [插入法]按照数组某一单元从大到小排序数组
	 * 
	 * @param array
	 *            被排序的数组
	 * @param index
	 *            指定被比较的单元ID
	 */
	public static final void orderArrayMax(int array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] > array[j][index])
				{
					// 进行迁移
					int temp[] = array[i];
					for (int m = i; m > j; m--)
					{
						array[m] = array[m - 1];
					}
					array[j] = temp;
					break;
				}
			}
		}
	}

	/**
	 * [插入法]按照数组某一单元从大到小排序数组
	 * 
	 * @param array
	 *            被排序的数组
	 * @param index
	 *            指定被比较的单元ID
	 */
	public static final void orderArrayMax(long array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] > array[j][index])
				{
					// 进行迁移
					long temp[] = array[i];
					for (int m = i; m > j; m--)
					{
						array[m] = array[m - 1];
					}
					array[j] = temp;
					break;
				}
			}
		}
	}

	/**
	 * [插入法]按照数组某一单元从小到大排序数组
	 * 
	 * @param array
	 *            被排序的数组
	 * @param index
	 *            指定被比较的单元ID
	 */
	public static final void orderArrayMin(int array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] < array[j][index])
				{
					// 进行迁移
					int temp[] = array[i];
					for (int m = i; m > j; m--)
					{
						array[m] = array[m - 1];
					}
					array[j] = temp;
					break;
				}
			}
		}
	}

	/**
	 * [插入法]按照数组某一单元从小到大排序数组
	 * 
	 * @param array
	 *            被排序的数组
	 * @param index
	 *            指定被比较的单元ID
	 */
	public static final void orderArrayMin(long array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] < array[j][index])
				{
					// 进行迁移
					long temp[] = array[i];
					for (int m = i; m > j; m--)
					{
						array[m] = array[m - 1];
					}
					array[j] = temp;
					break;
				}
			}
		}
	}

	/**
	 * [快速排序]按照单元数值从小到大排序数组
	 * 
	 * @param array
	 *            将被排序的数组
	 * @param start
	 *            开始排序的数组下标
	 * @param end
	 *            结束排序的数组下标
	 */
	private static final void orderArrayMin(long array[], int start, int end)
	{
		long middle, strTemp;
		int i = start;
		int j = end;
		middle = array[(start + end) / 2];
		do
		{
			while (i < end && array[i] < middle)
			{
				i++;
			}
			while (j > start && array[j] > middle)
			{
				j--;
			}
			if (i <= j)
			{
				strTemp = array[i];
				array[i] = array[j];
				array[j] = strTemp;
				i++;
				j--;
			}
		}
		while (i < j);// 如果两边扫描的下标交错，完成一次排序
		if (start < j)
		{
			orderArrayMin(array, start, j); // 递归调用
		}
		if (end > i)
		{
			orderArrayMin(array, i, end); // 递归调用
		}
	}

	/**
	 * [插入法]依次比较二维数组的第二维的每个数值，从小到大排序数组
	 * 
	 * @param array
	 *            将被排序的数组
	 * @param start
	 *            开始排序的数组下标
	 * @param end
	 *            结束排序的数组下标
	 */
	public static final void orderArrayMinByAll(int array[][], int start, int end)
	{
		if (array == null || start < 0 || start >= end || end >= array.length)
		{
			return;
		}
		start = limitNumber(start, 0, array.length - 1);
		end = limitNumber(end, 0, array.length - 1);
		int iC, temp[];
		int iValueCout = array[0].length;
		for (int iN = start + 1; iN <= end; iN++)
		{
			for (iC = start; iC < iN; iC++)
			{
				boolean needContinue = false;
				boolean needSwitch = false;
				for (int iV = 0; iV < iValueCout; iV++)
				{
					if (array[iC][iV] < array[iN][iV])
					{
						needContinue = true;
						break;
					}
					else if (array[iC][iV] == array[iN][iV])
					{
						continue;
					}
					else
					{
						needSwitch = true;
						break;
					}
				}
				if (needContinue)
				{
					continue;
				}
				if (needSwitch)
				{
					break;
				}
			}
			// 转移位置
			if (iN != iC)
			{
				temp = array[iN];
				for (int i = iN; i > iC; i--)
				{
					array[i] = array[i - 1];
				}
				array[iC] = temp;
			}
		}
	}

	/**
	 * 浮点型转转成4字节数据，用整型存放
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int floatToIntBits(float value)
	{
		return Float.floatToIntBits(value);
	}

	/**
	 * 将以整型存放的4直接数据转换成浮点型
	 * 
	 * @param value
	 *            the value
	 * @return the float
	 */
	public static float intBitsToFloat(int value)
	{
		return Float.intBitsToFloat(value);
	}

	/**
	 * 浮点型转换成字符
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String floatToString(float value)
	{
		return value + "";
	}

	/**
	 * 字符串转换成浮点型
	 * 
	 * @param value
	 *            the value
	 * @return the float
	 */
	public static float stringToFloat(String value)
	{
		float res = -1;
		try
		{
			res = Float.parseFloat(value);
		}
		catch (Exception e)
		{
		}
		return res;
	}

	/**
	 * 对有序数组进行二分查找
	 * 
	 * @param array
	 *            被查找的数组
	 * @param searchKey
	 *            查找的值
	 * @return 找到数值对应的下标
	 */
	public static int find(int[] array, int searchKey)
	{
		int lowerBound = 0;
		int upperBound = array.length - 1;
		int curIn;
		while (true)
		{
			curIn = (lowerBound + upperBound) / 2;
			C2D_Debug.log("access:[" + curIn + "]");
			if (array[curIn] == searchKey)
			{
				return curIn;
			}
			else if (lowerBound > upperBound)
			{
				return -1;
			}
			else
			{
				if (searchKey > array[curIn])
				{
					lowerBound = curIn + 1;
				}
				else
				{
					upperBound = curIn - 1;
				}
			}
		}
	}

	/**
	 * 整形转换成字符串
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String intToString(int value)
	{
		return value + "";
	}

	/**
	 * 将字符串转换成整形
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int stringToInt(String value)
	{
		int res = -1;
		if (value != null)
		{
			try
			{
				res = Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
		}
		return res;
	}

	/**
	 * 将字符串转换成整形
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static long stringToLong(String value)
	{
		long res = -1;
		if (value != null)
		{
			try
			{
				res = Long.parseLong(value);
			}
			catch (Exception e)
			{
			}
		}
		return res;
	}
	/**
	 * 获取指定大于等于浮点数的最小整数
	 * 
	 * @param value
	 *            浮点数
	 * @return 最小整数
	 */
	public static int ceil(float value)
	{
		int valueI = (int) value;
		if (valueI != value)
		{
			return valueI + 1;
		}
		return valueI;
	}

	/**
	 * 获取指定大于等于浮点数的最小整数
	 * 
	 * @param value
	 *            浮点数值
	 * @return 最小整数
	 */
	public static long ceil(double value)
	{
		long valueI = (long) value;
		if (valueI != value)
		{
			return valueI + 1;
		}
		return valueI;
	}

	/**
	 * 获取指定小于等于浮点数的最大整数
	 * 
	 * @param value
	 *            浮点数
	 * @return 最大整数
	 */
	public static int floor(float value)
	{
		int valueI = (int) value;
		return valueI;
	}

	/**
	 * 获取指定小于等于浮点数的最大整数
	 * 
	 * @param value
	 *            最小整数
	 * @return 最大整数
	 */
	public static long floor(double value)
	{
		long valueI = (long) value;
		return valueI;
	}

	/**
	 * 计算指定值以2为底的最接近的对数
	 * 
	 * @param value
	 * @return 对数结果， 返回-1表示传入的参数不合法
	 */
	public static int log(int value)
	{
		if (value < 2)
		{
			return -1;
		}
		int x = 2;
		int xMax = 0;
		do
		{
			xMax++;
			x <<= 1;
		}
		while (x <= value);
		return xMax;
	}

	/**
	 * 计算2的N次幂
	 * 
	 * @param n
	 *            幂次数 须>=0
	 * @return n次幂值
	 */
	public static int pow2(int n)
	{
		if (n <= 0)
		{
			return 0;
		}
		int value = 1;
		while (n > 0)
		{
			value <<= 1;
			n--;
		}
		return value;
	}

	// 获取大于等于指定值的2的倍数的数值
	public static int getMultipleOfTwo(int number)
	{
		int numberNew = 2;
		while (numberNew < number)
		{
			numberNew *= 2;
		}
		return numberNew;
	}

	// 获取大于等于指定值的2的倍数的尺寸
	public static C2D_SizeI getMultipleOfTwo(C2D_SizeI size)
	{
		size.m_width = getMultipleOfTwo(size.m_width);
		size.m_height = getMultipleOfTwo(size.m_height);
		return size;
	}

	/**
	 * 将像素数据从整形数组转换成字节数组
	 * 
	 * @param src
	 *            整形数组
	 * @return 字节数组
	 */
	public static byte[] pixelInt2Byte(int[] src)
	{
		int srcLength = src.length;
		byte[] dst = new byte[srcLength << 2];
		for (int i = 0; i < srcLength; i++)
		{
			int x = src[i];
			int j = i << 2;
			dst[j++] = (byte) ((x >>> 16) & 0xff);
			dst[j++] = (byte) ((x >>> 8) & 0xff);
			dst[j++] = (byte) ((x >>> 0) & 0xff);
			dst[j++] = (byte) ((x >>> 24) & 0xff);
		}
		return dst;
	}

	/**
	 * 正切函数
	 * 
	 * @param radian
	 *            弧度为单位的角度,这个函数的值域为－π/2～π/2
	 * @return 该角度所对应的反正切
	 */
	public static double tan(double radian)
	{
		double retval = Math.tan(radian);
		return retval;
	}

	/**
	 * 反正切函数
	 * 
	 * @param radian
	 *            弧度为单位的角度,这个函数的值域为－π/2～π/2
	 * @return 该角度所对应的反正切
	 */
	public static double actTan(double radian)
	{
		double retval = Math.atan(radian);
		return retval;
	}

	/**
	 * 根据向量进行反正切计算 正切值为y/x，因此该函数求的是y/x所对应的角，这个函数的值域为－π～π
	 * 
	 * @param x
	 *            向量x坐标
	 * @param y
	 *            向量y坐标
	 * @return 该角度所对应的反正切
	 */
	public static double actTan(double x, double y)
	{
		if (y == 0.0d && x == 0.0d)
			return 0.0d;
		if (x > 0.0d)
			return actTan(y / x);
		if (x < 0.0F)
		{
			if (y < 0.0d)
			{
				return (-(PI - actTan(y / x)));
			}
			else
			{
				return (PI - actTan(-y / x));
			}
		}
		return y >= 0.0F ? PI / 2 : PI / 2;

	}

	/**
	 * 根据向量进行反正切计算
	 * 
	 * @param x
	 *            矢量x坐标
	 * @param y
	 *            矢量y坐标
	 * @return the float
	 */
	public static float actTan(float x, float y)
	{
		float x1 = x;
		float y1 = y;
		double res = actTan((double) x1, (double) y1);
		return (float) (res);
	}

	/**
	 * 反正弦(弧度单位)
	 * 
	 * @param radian
	 *            弧度为单位的角度
	 * @return 该角度所对应的反正弦
	 */
	public static double actSin(double radian)
	{
		double retval = Math.asin(radian);
		return retval;
	}

	/**
	 * 反余弦.(弧度单位)
	 * 
	 * @param radian
	 *            弧度为单位的角度
	 * @return 该角度所对应的反余弦
	 */
	public static double actCos(double radian)
	{
		double retval = Math.acos(radian);
		return retval;
	}

	/**
	 * 获取一个数值value针对其周期cycle的周期性起点，取值范围在(-cycle,0]
	 * 
	 * @param value
	 *            将被折算的数值
	 * @param cycle
	 *            周期
	 * @return 折算后的周期性起点
	 */
	public static float getCycleBegin(float value, float cycle)
	{
		if (cycle < 0)
		{
			cycle = -cycle;
		}
		if (cycle == 0)
		{
			cycle = Integer.MAX_VALUE;
		}
		if (value < 0)
		{
			value += (((int) ((-value) / cycle))) * cycle;
		}
		else if (value >= cycle)
		{
			value -= (((int) ((value + cycle - 1) / cycle))) * cycle;
		}
		return value;
	}

	/**
	 * 获得过渡颜色
	 * 
	 * @param colorFrom
	 *            指定的起始颜色
	 * @param colorDest
	 *            指定的目标颜色
	 * @param percent
	 *            过渡的百分比（分子）
	 * @param percentBase
	 *            过渡的百分比的基数（分母）
	 * @return 过渡颜色
	 */
	public static final int getInterimColor(int colorFrom, int colorDest, int percent, int percentBase)
	{
		int newColor = colorFrom;
		if (percentBase != 0)
		{
			int R = ((colorFrom >> 16) & 0xFF) + (((colorDest >> 16) & 0xFF) - ((colorFrom >> 16) & 0xFF)) * percent / percentBase;
			int G = ((colorFrom >> 8) & 0xFF) + (((colorDest >> 8) & 0xFF) - ((colorFrom >> 8) & 0xFF)) * percent / percentBase;
			int B = ((colorFrom) & 0xFF) + (((colorDest) & 0xFF) - ((colorFrom) & 0xFF)) * percent / percentBase;
			newColor = (colorFrom & 0xFF000000) | (R << 16) | (G << 8) | (B);
		}
		return newColor;
	}
}
