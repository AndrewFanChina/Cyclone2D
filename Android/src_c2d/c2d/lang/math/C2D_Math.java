package c2d.lang.math;

import java.util.Random;

import c2d.lang.math.type.C2D_SizeI;
import c2d.lang.util.debug.C2D_Debug;
import c3d.util.math.C3D_Vector3;
import c3d.util.math.C3D_Vertex2;
import c3d.util.math.C3D_Vertex3;

/**
 * ��ѧ��
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

	// ���ȵ�����ת��
	public static float radiansToDegrees(float radians)
	{
		return (float) (180.0f * radians / PI);
	}

	// ����������ת��
	public static float degreesToRadians(float degrees)
	{
		return (float) (PI * degrees / 180);
	}

	/**
	 * �������Ŀ��ٿ�ƽ������.
	 * 
	 * @param value
	 *            ������ֵ
	 * @return ƽ����
	 */
	public static float sqrt(float value)
	{
		// float f1= intToFloat(0x1FBCF800 + (floatToInt(value) >> 1));
		// float f2= intToFloat(0x5f3759df - (floatToInt(value) >> 1));
		// return 0.5f*(f1 + (value * f2));
		return (float) sqrt((double) value);
	}

	/**
	 * �������Ŀ��ٿ�ƽ������.�����ڲ���ת�ɵ����ȸ��㣬���ע��ᶪʧ����
	 * 
	 * @param value
	 *            ������ֵ
	 * @return ƽ����
	 */
	public static double sqrt(double value)
	{
		return Math.sqrt(value);
	}

	/**
	 * ����ֵ.
	 * 
	 * @param number
	 *            ��ֵ
	 * @return ����ֵ
	 */
	public static long abs(long number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * ����ֵ.
	 * 
	 * @param number
	 *            ��ֵ
	 * @return ����ֵ
	 */
	public static int abs(int number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * ����ֵ.
	 * 
	 * @param number
	 *            ��ֵ
	 * @return ����ֵ
	 */
	public static float abs(float number)
	{
		if (number < 0)
			number = -number;
		return number;
	}
	/**
	 * ����ֵ.
	 * 
	 * @param number
	 *            ��ֵ
	 * @return ����ֵ
	 */
	public static double abs(double number)
	{
		if (number < 0)
			number = -number;
		return number;
	}

	/**
	 * max ��ϴ�ֵ.
	 * 
	 * @param num1
	 *            long ��ֵ1
	 * @param num2
	 *            long ��ֵ2
	 * @return long �ϴ���ֵ
	 */
	public static long max(long num1, long num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max ��ϴ�ֵ.
	 * 
	 * @param num1
	 *            ��ֵ1
	 * @param num2
	 *            ��ֵ2
	 * @return �ϴ���ֵ
	 */
	public static int max(int num1, int num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max ��ϴ�ֵ.
	 * 
	 * @param num1
	 *            ��ֵ1
	 * @param num2
	 *            ��ֵ2
	 * @return �ϴ���ֵ
	 */
	public static float max(float num1, float num2)
	{
		return (num1 > num2) ? num1 : num2;
	}

	/**
	 * max ���Сֵ.
	 * 
	 * @param num1
	 *            ��ֵ1
	 * @param num2
	 *            ��ֵ2
	 * @return ��С��ֵ
	 */
	public static long min(long num1, long num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * min ���Сֵ.
	 * 
	 * @param num1
	 *            ��ֵ1
	 * @param num2
	 *            ��ֵ2
	 * @return ��С��ֵ
	 */
	public static int min(int num1, int num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * min ���Сֵ.
	 * 
	 * @param num1
	 *            ��ֵ1
	 * @param num2
	 *            ��ֵ2
	 * @return ��С��ֵ
	 */
	public static float min(float num1, float num2)
	{
		return (num1 < num2) ? num1 : num2;
	}

	/**
	 * @todo ��ֱ�ߺ�ƽ��Ľ��� L0��L1 ֱ�߶˵� Fnƽ��ķ����� Fpƽ���ϵ�����һ���� ���� ��������
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

	// �ж����񹹳��Ƿ�˳ʱ�뷽��(0->1->2,1->2->0,2->0->1Ϊ˳ʱ�룬����Ϊ��ʱ��)
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
	 * ���Ƕȵ�����[0,2PI)����
	 * 
	 * @param ��
	 *            the ��
	 * @return the float
	 */
	public static float standirdDegree(float ��)
	{
		// ���� angle ����׼����[0,360)
		if (�� < 0)
		{
			�� = 360 - ��;
			�� %= 360;
			�� = 360 - ��;
		}
		else
		{
			�� %= 360;
		}
		return ��;
	}

	/**
	 * �����ȵ�����[0,2PI)����
	 * 
	 * @param ��
	 *            the ��
	 * @return the long
	 */
	public static double standirdAngle(double ��)
	{
		// ���� angle ����׼����[0,360)
		if (�� < 0)
		{
			�� = PI * 2 - ��;
			�� %= PI * 2;
			�� = PI * 2 - ��;
		}
		else
		{
			�� %= PI * 2;
		}
		return ��;
	}

	/**
	 * �����ȵ�����[-PI,PI)����
	 * 
	 * @param ��
	 *            the ��
	 * @return the long
	 */
	public static double standirdAngle2(double ��)
	{
		// ���� angle ����׼����[-PI,PI)
		if (�� < -PI)
		{
			�� += ((-PI - ��) / (PI * 2) + 1) * PI * 2;
		}
		else if (�� >= PI)
		{
			�� -= ((�� - PI) / (PI * 2) + 1) * PI * 2;
		}
		return ��;
	}

	/**
	 * ��180�����ڱȽϻ���A��B
	 * 
	 * @param ��A
	 *            the �� a
	 * @param ��B
	 *            the �� b
	 * @return true, if successful
	 */
	// TRUE A���|FALSE A�ұ�
	public static boolean left_Right(double ��A, double ��B)
	{
		��A = standirdAngle(��A);
		��B = standirdAngle(��B);
		if (C2D_Math.abs(��A - ��B) < PI)
		{
			return ��A > ��B;
		}
		else
		{
			return ��A < ��B;
		}
	}

	/**
	 * ��180�����ڼ���Ƕ�A��B�ļн�,����ֵ����ֵ
	 * 
	 * @param ��A
	 *            the �� a
	 * @param ��B
	 *            the �� b
	 * @return the long
	 */
	public static double clipAngle(double ��A, double ��B)
	{
		��A = standirdAngle(��A);
		��B = standirdAngle(��B);
		double abs = C2D_Math.abs(��A - ��B);
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
	 * ��180�����ڼ��㻡��A��B�ļн�,����ֵ�����Ҹ�
	 * 
	 * @param ��A
	 *            the �� a
	 * @param ��B
	 *            the �� b
	 * @return the long
	 */
	public static double gapAngle(double ��A, double ��B)
	{
		if (left_Right(��A, ��B))
		{
			return clipAngle(��A, ��B);
		}
		else
		{
			return -clipAngle(��A, ��B);
		}
	}

	/** @todo ��������2ά�߶εĽ���(P0,P1)(P2,P3)�ǵѿ�������ϵ�е����꣬����Ļ���� */
	public static C3D_Vertex2 getCross_V2D(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean whetherCross)
	{
		if (whetherCross)
		{
			// �ж��н���
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
		// �������
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

	// �ж�P0��P1��P2��P3��ɵ��������ڣ�����ѿ�������
	public static boolean inTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3)
	{
		// �ж�P1P0����������P1P2��P1P3֮�䣬��P0�㴦��P1P2��P1P3�ķ������
		vBufT0.setValue(x0 - x1, y0 - y1);
		vBufT2.setValue(x2 - x1, y2 - y1);
		vBufT3.setValue(x3 - x1, y3 - y1);
		if (vBufT0.outerProduct(vBufT2) * vBufT0.outerProduct(vBufT3) >= 0)
		{
			// System.out.println("not in triangle");
			return false;
		}
		// �ж�P2P0����������P2P1��P2P3֮�䣬��P0�㴦��P2P1��P2P3�ķ������
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

	// ���Һ���
	public static double sin(double ��)
	{
		return Math.sin(��);
	}

	// ���Һ���
	public static double cos(double ��)
	{
		return Math.cos(��);
	}

	/**
	 * ��180�����ڼ���Ƕ�A��B�ļн�,����ֵ�����Ҹ�
	 * 
	 * @param ��A
	 *            the �� a
	 * @param ��B
	 *            the �� b
	 * @return the long
	 */
	public static float gapAngleDegree(float ��A, float ��B)
	{
		return radiansToDegrees((float) gapAngle(degreesToRadians(��A), degreesToRadians(��B)));
	}

	/**
	 * ����N����
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
		if (n % 2 == 0)// �Ƿ� ż����
		{
			return pow(x * x, n / 2);
		}
		else
		{
			return pow(x * x, (n - 1) / 2) * x;
		}
	}

	/**
	 * ����N����
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
		if (n % 2 == 0)// �Ƿ� ż����
		{
			return pow(x * x, n / 2);
		}
		else
		{
			return pow(x * x, (n - 1) / 2) * x;
		}
	}

	// �߶ν������
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
	 * �ж������߶��Ƿ��ཻ(0,1)��(2,3),���һ����������һ���߶���Ҳ�����ཻ
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
	 * @return �Ƿ�����ײ
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
	 * �ж������߶��Ƿ��ཻ(0,1)��(2,3),�����1���߶�23��Ҳ�����ཻ�����๲�㲻��
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
	 * @return �Ƿ�����ײ
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

	// ���������
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
	 * �ж������߶�(2,3)��(0,1)������(��ʱ��)���Ǹ���
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

	// --------------------------------��ѧ����--------------------------------
	//
	/**
	 * ��ֵ�Ƿ�λ��ĳ��������
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
	 * ��ֵ�Ƿ�λ��ĳ��������
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
	 * ��ֵ�Ƿ�λ��ĳ��������
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
	 * ��ֵ�Ƿ�λ��ĳ��������
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
	 * ��ײ����
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
	 * �ж����������Ƿ���ײ
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
	 * �ж��������Ƿ���ײ�����㷨 x1,y1 - ����A���Ͻ����� x2,y2 - ����A���½����� x3,y3 - ����B���Ͻ����� x4,y4 -
	 * ����B���½�����.
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
	 * �ж��������Ƿ���ײ�����㷨 x1,y1 - ����A���Ͻ����� x2,y2 - ����A���½����� x3,y3 - ����B���Ͻ����� x4,y4 -
	 * ����B���½�����.
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
	 * ����ֵ������ĳ��������value = [min,max]
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
	 * ����ֵ������ĳ��������value = [min,max]
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
	 * ����ֵ������ĳ��������value = [min,max]
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

	// ��ȡ�����ĽǶ�[0-360)��x,yΪ�ѿ�������ϵ�е�����(����+X����+Y)
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
	/** �����--------------------------------------- */
	private static final Random myRandom = new Random();

	/**
	 * ��������[0,srcNum)
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
	 * ��������[srcNum1,srcNum2)
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
	 * ��������[srcNum1,srcNum2)
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
	 * ��������[srcNum1,srcNum2)
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
	 * ������ظ������������[srcNum1,srcNum2]
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
		// �����������
		for (int i = 0; i < datas.length; i++)
		{
			int index = getRandom(maxCount - i);
			// Ѱ���������
			for (int j = 0; j < maxCount; j++)
			{
				boolean used = false;
				// �����ظ�
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
					{ // �ҵ�
						index = j;
						break;
					}
					else
					{
						index--;
					}
				}
			}
			// ����
			datas[i] = index + minNumber;
		}
		return datas;
	}

	/**
	 * �������������
	 * 
	 * @param number
	 *            ���������
	 * @param min
	 *            ��Сֵ
	 * @param max
	 *            ���ֵ
	 * @return ���������
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
	 * �ж�[x,y]�Ƿ�������[_x,_y,_w,_h]
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
	 * �ж�[x,y]�Ƿ�������[_x,_y,_w,_h]
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
	 * ��ȡ�������ɫ
	 * 
	 * @return �������ɫ
	 */
	public static int getRandomColor()
	{
		return getRandomColor(0xFF);
	}

	/**
	 * ��ȡ����ָ��ԭɫ��ֵ�������ɫ������0xCC������0xCC���������ԭɫ��
	 * 
	 * @param colorUnit
	 *            ԭɫ��ֵ
	 * @return �����ɫ
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
	 * �ж�[x,y]�Ƿ�������[_x,_y,_w,_h]
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
	 * ð�ݷ��Ӵ�С��������,���������������飬���ض�Ӧ�����������е���λ����[30,20,40]����[1,2,0]
	 * 
	 * @param arry
	 *            the arry
	 */
	public static final void orderNumbers(int arry[])
	{
		orderNumbers(arry, arry.length);
	}

	/**
	 * ð�ݷ��Ӵ�С��������,���������������飬���ض�Ӧ�����������е���λ�� ��[30,20,40]����[1,2,0],ֻ����ǰN��
	 * 
	 * @param arry
	 *            ���������������
	 * @param nbItem
	 *            ǰ������
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
	 * [���뷨]��������ĳһ��Ԫ�Ӵ�С��������
	 * 
	 * @param array
	 *            �����������
	 * @param index
	 *            ָ�����Ƚϵĵ�ԪID
	 */
	public static final void orderArrayMax(int array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] > array[j][index])
				{
					// ����Ǩ��
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
	 * [���뷨]��������ĳһ��Ԫ�Ӵ�С��������
	 * 
	 * @param array
	 *            �����������
	 * @param index
	 *            ָ�����Ƚϵĵ�ԪID
	 */
	public static final void orderArrayMax(long array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] > array[j][index])
				{
					// ����Ǩ��
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
	 * [���뷨]��������ĳһ��Ԫ��С������������
	 * 
	 * @param array
	 *            �����������
	 * @param index
	 *            ָ�����Ƚϵĵ�ԪID
	 */
	public static final void orderArrayMin(int array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] < array[j][index])
				{
					// ����Ǩ��
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
	 * [���뷨]��������ĳһ��Ԫ��С������������
	 * 
	 * @param array
	 *            �����������
	 * @param index
	 *            ָ�����Ƚϵĵ�ԪID
	 */
	public static final void orderArrayMin(long array[][], int index)
	{
		for (int i = 1; i < array.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (array[i][index] < array[j][index])
				{
					// ����Ǩ��
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
	 * [��������]���յ�Ԫ��ֵ��С������������
	 * 
	 * @param array
	 *            �������������
	 * @param start
	 *            ��ʼ����������±�
	 * @param end
	 *            ��������������±�
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
		while (i < j);// �������ɨ����±꽻�����һ������
		if (start < j)
		{
			orderArrayMin(array, start, j); // �ݹ����
		}
		if (end > i)
		{
			orderArrayMin(array, i, end); // �ݹ����
		}
	}

	/**
	 * [���뷨]���αȽ϶�ά����ĵڶ�ά��ÿ����ֵ����С������������
	 * 
	 * @param array
	 *            �������������
	 * @param start
	 *            ��ʼ����������±�
	 * @param end
	 *            ��������������±�
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
			// ת��λ��
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
	 * ������תת��4�ֽ����ݣ������ʹ��
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
	 * �������ʹ�ŵ�4ֱ������ת���ɸ�����
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
	 * ������ת�����ַ�
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
	 * �ַ���ת���ɸ�����
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
	 * ������������ж��ֲ���
	 * 
	 * @param array
	 *            �����ҵ�����
	 * @param searchKey
	 *            ���ҵ�ֵ
	 * @return �ҵ���ֵ��Ӧ���±�
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
	 * ����ת�����ַ���
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
	 * ���ַ���ת��������
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
	 * ���ַ���ת��������
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
	 * ��ȡָ�����ڵ��ڸ���������С����
	 * 
	 * @param value
	 *            ������
	 * @return ��С����
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
	 * ��ȡָ�����ڵ��ڸ���������С����
	 * 
	 * @param value
	 *            ������ֵ
	 * @return ��С����
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
	 * ��ȡָ��С�ڵ��ڸ��������������
	 * 
	 * @param value
	 *            ������
	 * @return �������
	 */
	public static int floor(float value)
	{
		int valueI = (int) value;
		return valueI;
	}

	/**
	 * ��ȡָ��С�ڵ��ڸ��������������
	 * 
	 * @param value
	 *            ��С����
	 * @return �������
	 */
	public static long floor(double value)
	{
		long valueI = (long) value;
		return valueI;
	}

	/**
	 * ����ָ��ֵ��2Ϊ�׵���ӽ��Ķ���
	 * 
	 * @param value
	 * @return ��������� ����-1��ʾ����Ĳ������Ϸ�
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
	 * ����2��N����
	 * 
	 * @param n
	 *            �ݴ��� ��>=0
	 * @return n����ֵ
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

	// ��ȡ���ڵ���ָ��ֵ��2�ı�������ֵ
	public static int getMultipleOfTwo(int number)
	{
		int numberNew = 2;
		while (numberNew < number)
		{
			numberNew *= 2;
		}
		return numberNew;
	}

	// ��ȡ���ڵ���ָ��ֵ��2�ı����ĳߴ�
	public static C2D_SizeI getMultipleOfTwo(C2D_SizeI size)
	{
		size.m_width = getMultipleOfTwo(size.m_width);
		size.m_height = getMultipleOfTwo(size.m_height);
		return size;
	}

	/**
	 * ���������ݴ���������ת�����ֽ�����
	 * 
	 * @param src
	 *            ��������
	 * @return �ֽ�����
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
	 * ���к���
	 * 
	 * @param radian
	 *            ����Ϊ��λ�ĽǶ�,���������ֵ��Ϊ����/2����/2
	 * @return �ýǶ�����Ӧ�ķ�����
	 */
	public static double tan(double radian)
	{
		double retval = Math.tan(radian);
		return retval;
	}

	/**
	 * �����к���
	 * 
	 * @param radian
	 *            ����Ϊ��λ�ĽǶ�,���������ֵ��Ϊ����/2����/2
	 * @return �ýǶ�����Ӧ�ķ�����
	 */
	public static double actTan(double radian)
	{
		double retval = Math.atan(radian);
		return retval;
	}

	/**
	 * �����������з����м��� ����ֵΪy/x����˸ú��������y/x����Ӧ�Ľǣ����������ֵ��Ϊ���С���
	 * 
	 * @param x
	 *            ����x����
	 * @param y
	 *            ����y����
	 * @return �ýǶ�����Ӧ�ķ�����
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
	 * �����������з����м���
	 * 
	 * @param x
	 *            ʸ��x����
	 * @param y
	 *            ʸ��y����
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
	 * ������(���ȵ�λ)
	 * 
	 * @param radian
	 *            ����Ϊ��λ�ĽǶ�
	 * @return �ýǶ�����Ӧ�ķ�����
	 */
	public static double actSin(double radian)
	{
		double retval = Math.asin(radian);
		return retval;
	}

	/**
	 * ������.(���ȵ�λ)
	 * 
	 * @param radian
	 *            ����Ϊ��λ�ĽǶ�
	 * @return �ýǶ�����Ӧ�ķ�����
	 */
	public static double actCos(double radian)
	{
		double retval = Math.acos(radian);
		return retval;
	}

	/**
	 * ��ȡһ����ֵvalue���������cycle����������㣬ȡֵ��Χ��(-cycle,0]
	 * 
	 * @param value
	 *            �����������ֵ
	 * @param cycle
	 *            ����
	 * @return ���������������
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
	 * ��ù�����ɫ
	 * 
	 * @param colorFrom
	 *            ָ������ʼ��ɫ
	 * @param colorDest
	 *            ָ����Ŀ����ɫ
	 * @param percent
	 *            ���ɵİٷֱȣ����ӣ�
	 * @param percentBase
	 *            ���ɵİٷֱȵĻ�������ĸ��
	 * @return ������ɫ
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
