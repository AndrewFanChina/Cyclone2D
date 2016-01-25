package c2d.plat.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES11;
import c2d.lang.app.C2D_Canvas;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.plat.font.C2D_TextFont;
import c3d.util.math.C3D_Matrix4;

public class C2D_Graphics extends GLES11
{
	/** ��ͼʱʹ�õķ�ת��־--�޷�ת */
	public static final byte TRANS_NONE = 0;

	/** ��ͼʱʹ�õķ�ת��־--��ֱ���� (�൱������ת180����ˮƽ����) */
	public static final byte TRANS_VERTICAL = 1;

	/** ��ͼʱʹ�õķ�ת��־--ˮƽ���� */
	public static final byte TRANS_HORIZENTAL = 2;

	/** ��ͼʱʹ�õķ�ת��־--˳ʱ����ת180�� */
	public static final byte TRANS_ROTATE180 = 3;

	/** ��ͼʱʹ�õķ�ת��־--���ϽǶ������½� (�൱����˳ʱ����ת270����ˮƽ����) */
	public static final byte TRANS_FOLD_RT2LB = 4;

	/** ��ͼʱʹ�õķ�ת��־--˳ʱ����ת90�� */
	public static final byte TRANS_ROTATE90 = 5;

	/** ��ͼʱʹ�õķ�ת��־--˳ʱ����ת270�� */
	public static final byte TRANS_ROTATE270 = 6;

	/** ��ͼʱʹ�õķ�ת��־--���ϽǶ������½� (�൱������ת90����ˮƽ����) */
	public static final byte TRANS_FOLD_LT2RB = 7;

	/** ��ͼʱʹ�õķ�ת��־��ת������ */
	public static final byte[][] TRANS_ARRAY =
	{
	{ TRANS_NONE, TRANS_VERTICAL, TRANS_HORIZENTAL, TRANS_ROTATE180, TRANS_FOLD_RT2LB, TRANS_ROTATE90, TRANS_ROTATE270, TRANS_FOLD_LT2RB },// TRANS_NONE
			{ TRANS_VERTICAL, TRANS_NONE, TRANS_ROTATE180, TRANS_HORIZENTAL, TRANS_ROTATE90, TRANS_FOLD_RT2LB, TRANS_FOLD_LT2RB, TRANS_ROTATE270 },// TRANS_MIRROR_ROT180
			{ TRANS_HORIZENTAL, TRANS_ROTATE180, TRANS_NONE, TRANS_VERTICAL, TRANS_ROTATE270, TRANS_FOLD_LT2RB, TRANS_FOLD_RT2LB, TRANS_ROTATE90 },// TRANS_MIRROR
			{ TRANS_ROTATE180, TRANS_HORIZENTAL, TRANS_VERTICAL, TRANS_NONE, TRANS_FOLD_LT2RB, TRANS_ROTATE270, TRANS_ROTATE90, TRANS_FOLD_RT2LB },// TRANS_ROT180
			{ TRANS_FOLD_RT2LB, TRANS_ROTATE270, TRANS_ROTATE90, TRANS_FOLD_LT2RB, TRANS_NONE, TRANS_HORIZENTAL, TRANS_VERTICAL, TRANS_ROTATE180 },// TRANS_MIRROR_ROT270
			{ TRANS_ROTATE90, TRANS_FOLD_LT2RB, TRANS_FOLD_RT2LB, TRANS_ROTATE270, TRANS_VERTICAL, TRANS_ROTATE180, TRANS_NONE, TRANS_HORIZENTAL },// TRANS_ROT90
			{ TRANS_ROTATE270, TRANS_FOLD_RT2LB, TRANS_FOLD_LT2RB, TRANS_ROTATE90, TRANS_HORIZENTAL, TRANS_NONE, TRANS_ROTATE180, TRANS_VERTICAL },// TRANS_ROT270
			{ TRANS_FOLD_LT2RB, TRANS_ROTATE90, TRANS_ROTATE270, TRANS_FOLD_RT2LB, TRANS_ROTATE180, TRANS_VERTICAL, TRANS_HORIZENTAL, TRANS_NONE }, // TRANS_MIRROR_ROT90
	};
	private static C2D_PointF comPoint = new C2D_PointF();

	// OpenGL����
	public static final int FLag_Dither = C2D_Graphics.GL_DITHER;// ��ɫ����
	/**
	 * ��ǰ������С
	 */
	private C2D_RectS m_canvasSize = new C2D_RectS();

	/**
	 * ���캯����һ����˵���㲻��Ҫ�����Լ���C2DGraphics
	 */
	public C2D_Graphics()
	{
	}

	/**
	 * ���õ�ǰ��ɫ(��ȫ��͸��)
	 * 
	 * @param color
	 *            ��ɫ��ֵ���ᱻת����RRGGBB��ʽ
	 */
	void setRGBColor(int color)
	{
		setColor(color & 0xFFFFFF);
	}

	/**
	 * ���õ�ǰ��ɫ(��RGB���ƶ���alpha���ϳ���ɫ)
	 * 
	 * @param color
	 *            RRGGBB��ʽ����ɫ��ֵ
	 * @param alpha
	 *            AA��ʽ�İ�͸����ֵ
	 */
	void setColor(int color, int alpha)
	{
		setColor((color & 0xFFFFFF) | ((alpha & 0xFF) << 24));
	}

	/**
	 * ��ȡ���ӵķ�ת���������������һ����ת���ǣ�������һ����ת���
	 * 
	 * @param transFrom
	 *            ���ڵķ�ת����
	 * @param transPlus
	 *            ���ӵķ�ת����
	 * @return ����֮��ķ�ת����
	 */
	public static final byte getMixedTrans(byte transFrom, byte transPlus)
	{
		if (transFrom < 0 || transFrom > TRANS_FOLD_LT2RB || transPlus < 0 || transPlus > TRANS_FOLD_LT2RB)
		{
			C2D_Debug.log("error in gettransform");
			return -1;
		}
		return TRANS_ARRAY[transFrom][transPlus];
	}

	/**
	 * �Ի������ÿ�����ʾ���������������ȫ���赱ǰ����ʾ����ָ����ֵ��
	 * 
	 * @param visibleRect
	 *            ��ʾ����
	 */
	public final void setClip(C2D_RectF visibleRect)
	{
		setClip(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
	}

	/**
	 * �Ի������ø�С�Ľ���������ʾ�������������������ǰ����ʾ���� ֻ�������С��ʾ������setClip��ͬ��setClip����ȫ����
	 * ��ǰ����ʾ����ָ����ֵ����clipRect������ڵ�ǰ����ʾ���� �������µ���ʾ���ơ�
	 * 
	 * @param visibleRect
	 *            ����������ʾ����
	 */
	public final void clipRect(C2D_RectS visibleRect)
	{
		clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
	}

	/**
	 * �����������,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ����ֱ��
	 * @param height
	 *            ����ֱ��
	 * @param startAngle
	 *            ��ʼ�Ƕ�
	 * @param sweepAngle
	 *            ���ο��ڽǶ�
	 * @param color
	 *            ARGB��ɫ
	 * @param anchorPoint
	 *            ê������
	 * @param visibleRect
	 *            ������ʾ����
	 */
	public final void fillArc(float x, float y, float width, float height, float startAngle, float sweepAngle, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		fillArc(comPoint.m_x, comPoint.m_y, width - 1, height - 1, startAngle, sweepAngle);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * �������α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ����ֱ��
	 * @param height
	 *            ����ֱ��
	 * @param startAngle
	 *            ��ʼ�Ƕ�
	 * @param sweepAngle
	 *            ���ο��ڽǶ�
	 * @param color
	 *            ARGB��ɫ
	 * @param anchorPoint
	 *            ê������
	 * @param visibleRect
	 *            ������ʾ����
	 */
	public final void drawArc(float x, float y, float width, float height, int startAngle, int sweepAngle, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		drawArc(comPoint.m_x, comPoint.m_y, width - 1, height - 1, startAngle, sweepAngle);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ�����ľ���,�ڲ��������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 */
	public final void fillRect(float x, float y, float width, float height, int color)
	{
		fillRect(x, y, width, height, color, 0, null);
	}

	/**
	 * ����һ�����ľ���,�ڲ��������ɫ����
	 * 
	 * @param x
	 *            X����
	 * @param y
	 *            Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 */
	public final void fillRect(float x, float y, float width, float height, int color, int anchorPoint)
	{
		fillRect(x, y, width, height, color, anchorPoint, null);
	}

	/**
	 * ����һ�����ľ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            X����
	 * @param y
	 *            Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRect
	 *            RectS ����������ʾ����
	 */
	public final void fillRect(float x, float y, float width, float height, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		fillRect(comPoint.m_x, comPoint.m_y, width, height);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ������Բ�Ǿ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            Բ�Ǿ��εĿ��
	 * @param height
	 *            Բ�Ǿ��εĸ߶�
	 * @param arcWidth
	 *            Բ�ǵĿ��
	 * @param arcHeight
	 *            Բ�ǵĿ��
	 * @param color
	 *            ��ɫ
	 */
	public final void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight, int color)
	{
		fillRoundRect(x, y, width, height, arcWidth, arcHeight, color, 0, null);
	}

	/**
	 * ����һ������Բ�Ǿ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            Բ�Ǿ��εĿ��
	 * @param height
	 *            Բ�Ǿ��εĸ߶�
	 * @param arcWidth
	 *            Բ�ǵĿ��
	 * @param arcHeight
	 *            Բ�ǵĿ��
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 */
	public final void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight, int color, int anchorPoint)
	{
		fillRoundRect(x, y, width, height, arcWidth, arcHeight, color, anchorPoint, null);
	}

	/**
	 * ����һ������Բ�Ǿ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            Բ�Ǿ��εĿ��
	 * @param height
	 *            Բ�Ǿ��εĸ߶�
	 * @param arcWidth
	 *            Բ�ǵĿ��
	 * @param arcHeight
	 *            Բ�ǵĿ��
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRect
	 *            ����������ʾ����
	 */
	public final void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		fillRoundRect(comPoint.m_x, comPoint.m_y, width - 1, height - 1, arcWidth, arcHeight);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ�����α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 */
	public final void drawRect(float x, float y, float width, float height, int color)
	{
		drawRect(x, y, width, height, color, 0, null);
	}

	/**
	 * ����һ�����α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 */
	public final void drawRect(float x, float y, float width, float height, int color, int anchorPoint)
	{
		drawRect(x, y, width, height, color, anchorPoint, null);
	}

	/**
	 * ����һ�����α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRect
	 *            RectS ����������ʾ����
	 */
	public final void drawRect(float x, float y, float width, float height, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		drawRect(comPoint.m_x, comPoint.m_y, width - 1, height - 1);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ��Բ�Ǿ��α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            Բ�Ǿ��εĿ��
	 * @param height
	 *            Բ�Ǿ��εĸ߶�
	 * @param arcWidth
	 *            Բ�ǵĿ��
	 * @param arcHeight
	 *            Բ�ǵĿ��
	 * @param color
	 *            ��ɫ
	 */
	public final void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color)
	{
		drawRoundRect(x, y, width, height, arcWidth, arcHeight, color, 0, null);
	}

	/**
	 * ����һ��Բ�Ǿ��α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param x
	 *            ���Ͻ�X����
	 * @param y
	 *            ���Ͻ�Y����
	 * @param width
	 *            Բ�Ǿ��εĿ��
	 * @param height
	 *            Բ�Ǿ��εĸ߶�
	 * @param arcWidth
	 *            Բ�ǵĿ��
	 * @param arcHeight
	 *            Բ�ǵĿ��
	 * @param color
	 *            ��ɫ
	 * @param anchorPoint
	 *            ê������
	 * @param visibleRect
	 *            ����������ʾ����
	 */
	public final void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint, C2D_RectF visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		drawRoundRect(comPoint.m_x, comPoint.m_y, width - 1, height - 1, arcWidth, arcHeight);
		C2D_Canvas.DrawCall++;

	}

	/**
	 * ����һ������������,�ڲ��������ɫ���� �ڲ����Զ���������ʾ�����򣬲�ʹ��setClip��������
	 * 
	 * @param x1
	 *            ��1������X����
	 * @param y1
	 *            ��1������Y����
	 * @param x2
	 *            ��2������X����
	 * @param y2
	 *            ��2������Y����
	 * @param x3
	 *            ��3������X����
	 * @param y3
	 *            ��3������Y����
	 * @param color
	 *            ��ɫ
	 */
	public final void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color)
	{

		int xL = Math.min(x1, x2);
		xL = Math.min(xL, x3);
		int xR = Math.max(x1, x2);
		xR = Math.max(xR, x3);
		int yT = Math.min(y1, y2);
		yT = Math.min(yT, y3);
		int yB = Math.max(y1, y2);
		yB = Math.max(yB, y3);
		setClip(xL, yT, xR - xL + 1, yB - yT + 1);
		clipRect(m_canvasSize);
		setColor(color);
		fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ������������,�ڲ��������ɫ���� �����Զ����ÿ�����ʾ������,��Ҫ���Լ��ֶ�����
	 * 
	 * @param x1
	 *            ��1������X����
	 * @param y1
	 *            ��1������Y����
	 * @param x2
	 *            ��2������X����
	 * @param y2
	 *            ��2������Y����
	 * @param x3
	 *            ��3������X����
	 * @param y3
	 *            ��3������Y����
	 * @param color
	 *            ��ɫ
	 */
	public final void fillTrianglePlain(int x1, int y1, int x2, int y2, int x3, int y3, int color)
	{

		setColor(color);
		fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * �����߶�,�ڲ��������ɫ���� �����Զ����ÿ�����ʾ������,��Ҫ���Լ��ֶ�����
	 * 
	 * @param visibleRect
	 *            ������ʾ����
	 * @param x1
	 *            ��1������X����
	 * @param y1
	 *            ��1������Y����
	 * @param x2
	 *            ��2������X����
	 * @param y2
	 *            ��2������Y����
	 * @param color
	 *            ��ɫ
	 */
	public final void drawLine(C2D_RectF visibleRect, int x1, int y1, int x2, int y2, int color)
	{
		int x = C2D_Math.min(x1, x2);
		int y = C2D_Math.min(y1, y2);
		int width = C2D_Math.abs(x2 - x1) + 1;
		int height = C2D_Math.abs(y2 - y1) + 1;
		if (!applyClip(x, y, width, height, 0, visibleRect, comPoint))
		{
			return;
		}
		setColor(color);
		drawLine(x1, y1, x2, y2);
		C2D_Canvas.DrawCall++;
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

	private C2D_ImageClip m_drawClip = new C2D_ImageClip();

	/**
	 * ʹ��ָ����ɫ�����Ļ
	 * 
	 * @param color
	 *            AARRGGBB��ʽ����ɫ��ֵ
	 */
	public void clearScreen(int color)
	{
		float r = ((color >> 16) & 0xFF) / 255.0f;
		float g = ((color >> 8) & 0xFF) / 255.0f;
		float b = ((color >> 0) & 0xFF) / 255.0f;
		float a = ((color >> 24) & 0xFF) / 255.0f;
		C2D_Graphics.glClearColor(r, g, b, a);
		// �����ɫ�����Ϊ����
		C2D_Graphics.glClear(C2D_Graphics.GL_COLOR_BUFFER_BIT | C2D_Graphics.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * ��ù����м���ɫ
	 * 
	 * @param colorFrom
	 *            ��ʼ��ɫ
	 * @param colorDest
	 *            Ŀ����ɫ
	 * @param percent
	 *            ��ǰ�ٷֱ�[0.0f,1.0f]
	 * @return the �����м�ɫ
	 */
	public final int getInterimColor(int colorFrom, int colorDest, float percent)
	{
		int newColor = colorFrom;
		int R = (int) (((colorFrom >> 16) & 0xFF) + (((colorDest >> 16) & 0xFF) - ((colorFrom >> 16) & 0xFF)) * percent);
		int G = (int) (((colorFrom >> 8) & 0xFF) + (((colorDest >> 8) & 0xFF) - ((colorFrom >> 8) & 0xFF)) * percent);
		int B = (int) (((colorFrom) & 0xFF) + (((colorDest) & 0xFF) - ((colorFrom) & 0xFF)) * percent);
		newColor = (colorFrom & 0xFF000000) | (R << 16) | (G << 8) | (B);
		return newColor;
	}

	/**
	 * ����OpenGL����
	 * 
	 * @param enable
	 */
	public void setFlag(int flag, boolean enable)
	{
		if (enable)
		{
			C2D_Graphics.glEnable(flag);
		}
		else
		{
			C2D_Graphics.glDisable(flag);
		}
	}

	/**
	 * ���õ����ʱ��Ĵ�С
	 * 
	 * @param size
	 */
	public void setPointSize(float size)
	{
		C2D_Graphics.glPointSize(size);
	}

	/**
	 * ���������ʱ�ߵĿ��
	 * 
	 * @param width
	 */
	public void setLineWidth(float width)
	{
		C2D_Graphics.glLineWidth(width);
	}

	// ʸ��ͼ�λ���-----------------------------------------------------------------------
	// /**
	// * ����Բ�α߿����Ķ���
	// * @param x ����X����
	// * @param y ����Y����
	// * @param width ����ֱ��
	// * @param height����ֱ��
	// */
	// public void drawCircle(int x, int y, int width, int height)
	// {
	// drawCircleBase(C2D_Graphics.GL_LINE_LOOP,x, y, width, height);
	// }
	// /**
	// * ����Բ����䣬���Ķ���
	// * @param x ����X����
	// * @param y ����Y����
	// * @param width ����ֱ��
	// * @param height����ֱ��
	// */
	// public void fillCircle(int x, int y, int width, int height)
	// {
	// drawCircleBase(C2D_Graphics.GL_TRIANGLE_FAN,x, y, width, height);
	// }
	//
	// /**
	// * ����Բ�θ�������
	// */
	// private static FloatBuffer circleBuffer;//����Բ�εĸ�������
	// private static int CIRCLE_PointCount=64;//��׼Բ�εĶ������
	// private static float[] circleVertices= new
	// float[CIRCLE_PointCount*2];//��׼Բ�εĶ���
	// private static final float circleR=200.0f;//��׼Բ�ε�ֱ��
	// /**
	// * ����Բ��
	// * @param mode ���ģʽ
	// * @param x ����X����
	// * @param y ����Y����
	// * @param width ����ֱ��
	// * @param height����ֱ��
	// */
	// private static void drawCircleBase(int mode,int x, int y, int width, int
	// height)
	// {
	// if(circleBuffer==null)
	// {
	// //��ʼ��Բ������
	// float angle=0;
	// float angleAdd=360.0f/CIRCLE_PointCount;
	// for (int i = 0; i < CIRCLE_PointCount; i ++)
	// {
	// circleVertices[i<<1] = (float) (Math.cos(Math.toRadians(angle)) *
	// circleR);
	// circleVertices[(i<<1)+1] = (float) (Math.sin(Math.toRadians(angle)) *
	// circleR);
	// angle+=angleAdd;
	// }
	// //����Բ�ζ�������
	// ByteBuffer vbb = ByteBuffer.allocateDirect(circleVertices.length * 4);
	// vbb.order(ByteOrder.nativeOrder());
	// circleBuffer = vbb.asFloatBuffer();
	// circleBuffer.put(circleVertices);
	// circleBuffer.position(0);
	// }
	//
	// C2D_Graphics.glPushMatrix();
	// //����任
	// commonMatrix1.setToTranslate(x, y,0.0f);
	// commonMatrix2.setToScale(width/(circleR*2), height/(circleR*2),1.0f);
	// commonMatrix1.multiply(commonMatrix2);
	// C2D_Graphics.glLoadMatrixf(commonMatrix1.data, 0);
	// //��ʼ����Բ��
	// C2D_Graphics.glEnableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
	// C2D_Graphics.glVertexPointer(2, C2D_Graphics.GL_FLOAT, 0, circleBuffer);
	// C2D_Graphics.glDrawArrays(mode, 0, CIRCLE_PointCount);
	// C2D_Graphics.glDisableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
	//
	// C2D_Graphics.glPopMatrix();
	// }

	// ��ͼ����-----------------------------------------------------------------------
	private static FloatBuffer matrixBuffer;

	void PushMatrix(float mtrixGLValue[])
	{
		if (mtrixGLValue == null)
		{
			return;
		}
		C2D_Graphics.glPushMatrix();
		if (matrixBuffer == null || matrixBuffer.array().length != mtrixGLValue.length)
		{
			ByteBuffer bbf = ByteBuffer.allocate(mtrixGLValue.length * 4);
			bbf = bbf.order(ByteOrder.nativeOrder());
			matrixBuffer = bbf.asFloatBuffer();
		}
		matrixBuffer.clear();
		matrixBuffer.put(mtrixGLValue);
		matrixBuffer.position(0);
		C2D_Graphics.glMultMatrixf(matrixBuffer);
	}

	void PushMatrix()
	{
		C2D_Graphics.glPushMatrix();
	}

	void PopMatrix()
	{
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * ���Ŀ��Ӧ��ê��Ϳ�������
	 * 
	 * @param x
	 *            Ŀ������x
	 * @param y
	 *            Ŀ������y
	 * @param width
	 *            Ŀ��������
	 * @param height
	 *            Ŀ������߶�
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRect
	 *            ��������
	 * @param destP
	 *            ת�����Ŀ��λ��
	 * @return �Ƿ���Ҫ����
	 */
	public boolean applyClip(float x, float y, float width, float height, int anchorPoint, C2D_RectF visibleRect, C2D_PointF destP)
	{
		if (anchorPoint != C2D_Consts.DEF)
		{
			if ((anchorPoint & C2D_Consts.HCENTER) != 0)
			{
				x -= width / 2;
			}
			else if ((anchorPoint & C2D_Consts.RIGHT) != 0)
			{
				x -= width;
			}
			if ((anchorPoint & C2D_Consts.VCENTER) != 0)
			{
				y -= height / 2;
			}
			else if ((anchorPoint & C2D_Consts.BOTTOM) != 0)
			{
				y -= height;
			}
		}
		destP.m_x = x;
		destP.m_y = y;
		setClip(x, y, width, height);
		if (visibleRect != null)
		{
			if (x + width <= visibleRect.m_x || x >= visibleRect.m_x + visibleRect.m_width || y + height <= visibleRect.m_y || y >= visibleRect.m_y + visibleRect.m_height || visibleRect.m_width <= 0 || visibleRect.m_height <= 0)
			{
				return false;
			}
			clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
		}
		clipRect(m_canvasSize);
		return true;
	}

	/**
	 * ���ݲ��ֵ����꣬�ߴ磬��ת��ê�����Ϣ���������ճ��ֵľ��Σ��������resultRect�з���
	 * 
	 * @param x
	 *            X����
	 * @param y
	 *            Y����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 * @param anchor
	 *            ê��
	 * @param trans
	 *            ��ת
	 * @param resultRect
	 *            ��ż�����
	 * @return �Ƿ�ɹ�ȡ��
	 */
	public static boolean computeLayoutRect(float x, float y, float width, float height, int anchor, byte trans, C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		// ����ʵ�ʳߴ�
		float wReal, hReal;
		if ((trans & C2D_Graphics.TRANS_FOLD_RT2LB) != 0)
		{
			wReal = height;
			hReal = width;
		}
		else
		{
			wReal = width;
			hReal = height;
		}
		// ����ʵ������
		float xReal = x;
		float yReal = y;
		if (anchor != C2D_Consts.DEF)
		{
			if ((anchor & C2D_Consts.HCENTER) != 0)
			{
				xReal -= wReal / 2;
			}
			else if ((anchor & C2D_Consts.RIGHT) != 0)
			{
				xReal -= wReal;
			}
			if ((anchor & C2D_Consts.VCENTER) != 0)
			{
				yReal -= hReal / 2;
			}
			else if ((anchor & C2D_Consts.BOTTOM) != 0)
			{
				yReal -= hReal;
			}
		}
		// �洢����
		resultRect.m_x = xReal;
		resultRect.m_y = yReal;
		resultRect.m_width = wReal;
		resultRect.m_height = hReal;
		return true;
	}

	/**
	 * ��һ��ͼƬ��Դͼ���н�ȡһ������������ָ���ķ�ת��ǣ� ���Ƶ�����һ��ͼƬ��Ŀ��ͼ����ָ��λ���С�Դͼ��Ŀ��ͼ
	 * ����������������ʽ����������������顣
	 * 
	 * @param srcImg
	 *            Դͼ
	 * @param _x
	 *            ��Դͼ�н�ȡ�ľ��������X����
	 * @param _y
	 *            ��Դͼ�н�ȡ�ľ��������Y����
	 * @param _w
	 *            ��Դͼ�н�ȡ�ľ�������Ŀ��W
	 * @param _h
	 *            ��Դͼ�н�ȡ�ľ�������ĸ߶�H
	 * @param imgW_s
	 *            Դͼ�Ŀ��
	 * @param imgH_s
	 *            Դͼ�ĸ߶�
	 * @param destImg
	 *            Ŀ��ͼ
	 * @param x_d
	 *            ���Ƶ�Ŀ��λ��X����
	 * @param y_d
	 *            ���Ƶ�Ŀ��λ��Y����
	 * @param imgW_d
	 *            Ŀ��ͼ���
	 * @param imgH_d
	 *            Ŀ��ͼ�߶�
	 * @param transFlag
	 *            ��ת��־
	 * @return �Ƿ�ɹ�����
	 */
	public static boolean drawBitmap(int[] srcImg, int _x, int _y, int _w, int _h, int imgW_s, int imgH_s, int[] destImg, int x_d, int y_d, int imgW_d, int imgH_d, int transFlag)
	{
		if (srcImg == null || destImg == null || srcImg.length < imgW_s * imgH_s || destImg.length < imgW_d * imgH_d)
		{
			return false;
		}
		// ����Դ����
		if (_x < 0 || _y < 0 || _x >= imgW_s || _y >= imgH_s)
		{
			return false;
		}
		if (_x + _w > imgW_s)
		{
			_w = imgW_s - _x;
		}
		if (_y + _h > imgH_s)
		{
			_h = imgH_s - _y;
		}
		if (_w <= 0 || _h <= 0)
		{
			return false;
		}
		// ����Ŀ����γߴ�
		int w_d = _w;
		int h_d = _h;
		if ((int) transFlag >= (int) TRANS_FOLD_RT2LB)
		{
			w_d = _h;
			h_d = _w;
		}
		// ����Ŀ�����λ��
		if (x_d < 0)
		{
			w_d += x_d;
			x_d = 0;
		}
		if (y_d < 0)
		{
			h_d += y_d;
			y_d = 0;
		}
		if (x_d + w_d > imgW_d)
		{
			w_d = imgW_d - x_d;
		}
		if (y_d + h_d > imgH_d)
		{
			h_d = imgH_d - y_d;
		}
		if (x_d >= imgW_d || y_d >= imgH_d || w_d <= 0 || h_d <= 0)
		{
			return false;
		}
		// ��ʼ����
		int iH, iW;
		int dataFrom;
		int dataDest;
		int copyLen;
		int scanLenS;
		int scanLenD;
		int dataFT;
		switch (transFlag)
		{
		case TRANS_NONE:
			dataFrom = _y * imgW_s + _x;
			dataDest = (y_d * imgW_d + x_d);
			copyLen = w_d;
			scanLenD = imgW_d;
			scanLenS = imgW_s;
			for (iH = 0; iH < h_d; iH++)
			{
				System.arraycopy(srcImg, dataFrom, destImg, dataDest, copyLen);
				dataFrom += scanLenS;
				dataDest += scanLenD;
			}
			break;
		case TRANS_VERTICAL:
			dataFrom = ((_y + h_d - 1) * imgW_s + _x);
			dataDest = (y_d * imgW_d + x_d);
			copyLen = w_d;
			scanLenD = imgW_d;
			scanLenS = imgW_s;
			for (iH = 0; iH < h_d; iH++)
			{
				System.arraycopy(srcImg, dataFrom, destImg, dataDest, copyLen);
				dataFrom -= scanLenS;
				dataDest += scanLenD;
			}
			break;
		case TRANS_HORIZENTAL:
			dataFrom = (_y * imgW_s + _x + imgW_s - 1);
			dataDest = (y_d * imgW_d + x_d);
			scanLenD = imgW_d - w_d;
			scanLenS = imgW_s + w_d;
			for (iH = 0; iH < h_d; iH++)
			{
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFrom];
					dataDest++;
					dataFrom--;
				}
				dataFrom += scanLenS;
				dataDest += scanLenD;
			}
			break;
		case TRANS_ROTATE180:
			dataFrom = ((_y + h_d - 1) * imgW_s + _x + w_d - 1);
			dataDest = (y_d * imgW_d + x_d);
			scanLenD = imgW_d - w_d;
			scanLenS = imgW_s - w_d;
			for (iH = 0; iH < h_d; iH++)
			{
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFrom];
					dataDest++;
					dataFrom--;
				}
				dataFrom -= scanLenS;
				dataDest += scanLenD;
			}
			break;
		case TRANS_FOLD_RT2LB:
			dataFrom = ((_y) * imgW_s + _x);
			dataDest = (y_d * imgW_d + x_d);
			scanLenD = imgW_d - w_d;
			scanLenS = imgW_s;
			for (iH = 0; iH < h_d; iH++)
			{
				dataFT = dataFrom + iH;
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFT];
					dataDest++;
					dataFT += scanLenS;
				}
				dataDest += scanLenD;
			}
			break;
		case TRANS_ROTATE90:
			dataFrom = ((_y + w_d - 1) * imgW_s + _x);
			dataDest = (y_d * imgW_d + x_d);
			scanLenS = imgW_s;
			scanLenD = imgW_d - w_d;
			for (iH = 0; iH < h_d; iH++)
			{
				dataFT = dataFrom + iH;
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFT];
					dataDest++;
					dataFT -= scanLenS;
				}
				dataDest += scanLenD;
			}
			break;
		case TRANS_ROTATE270:
			dataFrom = ((_y) * imgW_s + _x + _w - 1);
			dataDest = (y_d * imgW_d + x_d);
			scanLenS = imgW_s;
			scanLenD = imgW_d - w_d;
			for (iH = 0; iH < h_d; iH++)
			{
				dataFT = dataFrom - iH;
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFT];
					dataDest++;
					dataFT += scanLenS;
				}
				dataDest += scanLenD;
			}
			break;
		case TRANS_FOLD_LT2RB:
			dataFrom = ((_y + w_d - 1) * imgW_s + _x + h_d - 1);
			dataDest = (y_d * imgW_d + x_d);
			scanLenS = imgW_s;
			scanLenD = imgW_d - w_d;
			for (iH = 0; iH < h_d; iH++)
			{
				dataFT = dataFrom - iH;
				for (iW = 0; iW < w_d; iW++)
				{
					destImg[dataDest] = srcImg[dataFT];
					dataDest++;
					dataFT -= scanLenS;
				}
				dataDest += scanLenD;
			}
			break;
		}
		return true;
	}

	private static C2D_PointF pointFTemp = new C2D_PointF();

	/**
	 * ���ָ���ľ���������Ӧ��ê�㣬֮�󷵻����µ������
	 * 
	 * @param rect
	 *            ��������
	 * @param anchor
	 *            ê��
	 * @return ���µ������
	 */
	public static C2D_PointF applyAnchor(C2D_RectF rect, int anchor)
	{
		float x_offset_clip = 0;
		float y_offset_clip = 0;
		// ����ƫ��
		if ((C2D_Consts.LEFT & anchor) != 0)
		{
			x_offset_clip = 0;
		}
		else if ((C2D_Consts.HCENTER & anchor) != 0)
		{
			x_offset_clip = -(rect.m_width / 2);
		}
		else if ((C2D_Consts.RIGHT & anchor) != 0)
		{
			x_offset_clip = -rect.m_width;
		}
		// ����ƫ��
		if ((C2D_Consts.TOP & anchor) != 0)
		{
			y_offset_clip = 0;
		}
		else if ((C2D_Consts.VCENTER & anchor) != 0)
		{
			y_offset_clip = -(rect.m_height / 2);
		}
		else if ((C2D_Consts.BOTTOM & anchor) != 0)
		{
			y_offset_clip = -rect.m_height;
		}
		pointFTemp.m_x = rect.m_x + x_offset_clip;
		pointFTemp.m_y = rect.m_y + y_offset_clip;
		return pointFTemp;
	}

	/**
	 * ��ȡ��ǰ������С
	 * 
	 * @return
	 */
	public C2D_RectS getCanvasRect()
	{
		return m_canvasSize;
	}

	public void setClip(float x, float y, float w, float h)
	{
		// gInner.setClip(x, y, w, h);
	}

	public void clipRect(float x, float y, float w, float h)
	{
		// gInner.clipRect(x, y, w, h);
	}

	public void fillArc(float x, float y, float w, float h, float startAngle, float sweepAngle)
	{
		// gInner.fillArc(x, y, w, h, startAngle, sweepAngle);
	}

	public void setColor(int color)
	{
		float r = ((color >> 16) & 0xFF) / 255.0f;
		float g = ((color >> 8) & 0xFF) / 255.0f;
		float b = ((color >> 0) & 0xFF) / 255.0f;
		float a = ((color >> 24) & 0xFF) / 255.0f;
		glColor4f(r, g, b, a);
	}

	public void drawArc(float x, float y, float w, float h, float startAngle, float sweepAngle)
	{
		// gInner.drawArc(x, y, w, h, startAngle, sweepAngle);
	}

	public void fillRect(float x, float y, float w, float h)
	{
		drawRect_GL(GL_TRIANGLE_FAN, x, y, 0, w, h);
	}

	public void fillRoundRect(float x, float y, float w, float h, float arcW, float arcH)
	{
		// gInner.fillRoundRect(x, y, w, h, arcW, arcH);
	}

	public void drawRect(float x, float y, float w, float h)
	{
		drawRect_GL(C2D_Graphics.GL_LINE_LOOP, x, y, 0, w, h);
	}

	public void drawLine(float x1, float y1, float x2, float y2)
	{
		drawLineBase(x1, y1, x2, y2);
	}

	public void drawRoundRect(float x, float y, float w, float h, float arcW, float arcH)
	{
		// gInner.drawRoundRect(x, y, w, h, arcW, arcH);
	}

	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		drawTriangleBase(C2D_Graphics.GL_TRIANGLE_FAN, x1, y1, x2, y2, x3, y3);
	}

	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		drawTriangleBase(C2D_Graphics.GL_LINE_LOOP, x1, y1, x2, y2, x3, y3);
	}

	public void drawImage(C2D_Image img, float x, float y, int anchor)
	{
		drawRegion(img, 0, 0, img.bitmapWidth(), img.bitmapHeight(), (byte) 0, x, y, anchor);
	}

	public void drawRegion(C2D_Image img, int _x, int _y, int _w, int _h, byte transFlag, float x, float y, int anchor)
	{
		m_drawClip.setContentRect(img, _x, _y, _w, _h);
		m_drawClip.setShowSize(_w, _h);
		// ����ת
		int wReal = _w;
		int hReal = _h;
		if ((transFlag & TRANS_FOLD_RT2LB) != 0)
		{
			wReal = _h;
			hReal = _w;
		}
		int destXI = (int) x;
		int destYI = (int) y;
		// ���ÿ�������
		if (anchor != C2D_Consts.DEF)
		{
			if ((anchor & C2D_Consts.HCENTER) != 0)
			{
				destXI -= wReal >> 1;
			}
			else if ((anchor & C2D_Consts.RIGHT) != 0)
			{
				destXI -= wReal;
			}
			else
			{
				anchor |= C2D_Consts.LEFT;
			}

			if ((anchor & C2D_Consts.VCENTER) != 0)
			{
				destYI -= hReal >> 1;
			}
			else if ((anchor & C2D_Consts.BOTTOM) != 0)
			{
				destYI -= hReal;
			}
			else
			{
				anchor |= C2D_Consts.TOP;
			}
		}
		m_drawClip.draw(img, destXI, destYI, transFlag, 0xFFFFFFFF);
	}

	/**
	 * ���ƾ��θ�������
	 */
	private static float[] rectVertices = new float[12];// �����ζ��㻺��
	private static FloatBuffer rectVerticesO;

	/**
	 * ���ƾ���
	 * 
	 * @param mode
	 *            ���ģʽ
	 * @param x
	 *            �������Ͻ�X����
	 * @param y
	 *            �������Ͻ�Y����
	 * @param z
	 *            �������Z����
	 * @param w
	 *            ���ο��
	 * @param h
	 *            ���θ߶�
	 */
	private void drawRect_GL(int mode, float x, float y, float z, float w, float h)
	{
		rectVertices[0] = x;
		rectVertices[1] = y;
		rectVertices[2] = z;
		rectVertices[3] = x + w;
		rectVertices[4] = y;
		rectVertices[5] = z;
		rectVertices[6] = x + w;
		rectVertices[7] = y + h;
		rectVertices[8] = z;
		rectVertices[9] = x;
		rectVertices[10] = y + h;
		rectVertices[11] = z;
		C2D_Graphics.glPushMatrix();
		// ��ʼ����
		setTextureState(TexState_SHAPE);
		if (rectVerticesO == null)
		{
			ByteBuffer ibb = ByteBuffer.allocateDirect(rectVertices.length * 4);
			ibb.order(ByteOrder.nativeOrder());
			rectVerticesO = ibb.asFloatBuffer();
		}
		rectVerticesO.clear();
		rectVerticesO.put(rectVertices);
		rectVerticesO.position(0);
		C2D_Graphics.glVertexPointer(3, C2D_Graphics.GL_FLOAT, 0, rectVerticesO);
		C2D_Graphics.glDrawArrays(mode, 0, 4);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * ���������θ�������
	 */
	private static float[] trianVertices = new float[6];// �����ζ��㻺��
	private static FloatBuffer triangleVerticesO;

	/**
	 * ����������
	 * 
	 * @param mode
	 *            ���ģʽ
	 * @param x1
	 *            ��1������X����
	 * @param y1
	 *            ��1������Y����
	 * @param x2
	 *            ��2������X����
	 * @param y2
	 *            ��2������Y����
	 * @param x3
	 *            ��3������X����
	 * @param y3
	 *            ��3������Y����
	 */
	private void drawTriangleBase(int mode, float x1, float y1, float x2, float y2, float x3, float y3)
	{
		trianVertices[0] = x1;
		trianVertices[1] = y1;
		trianVertices[2] = x2;
		trianVertices[3] = y2;
		trianVertices[4] = x3;
		trianVertices[5] = y3;
		C2D_Graphics.glPushMatrix();
		// ��ʼ����
		setTextureState(TexState_SHAPE);
		if (triangleVerticesO == null)
		{
			ByteBuffer ibb = ByteBuffer.allocateDirect(trianVertices.length * 4);
			ibb.order(ByteOrder.nativeOrder());
			triangleVerticesO = ibb.asFloatBuffer();
		}
		triangleVerticesO.clear();
		triangleVerticesO.put(trianVertices);
		triangleVerticesO.position(0);
		C2D_Graphics.glVertexPointer(2, C2D_Graphics.GL_FLOAT, 0, triangleVerticesO);
		C2D_Graphics.glDrawArrays(mode, 0, 3);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * �����߶θ�������
	 */
	private static FloatBuffer lineVerticesBuf = null;// �߶ζ��㻺��
	private static float[] lineVertices = new float[4];

	private void drawLineBase(float xBegin, float yBegin, float xEnd, float yEnd)
	{
		lineVertices[0] = xBegin;
		lineVertices[1] = yBegin;
		lineVertices[2] = xBegin + xEnd;
		lineVertices[3] = yBegin + yEnd;
		if (lineVerticesBuf == null)
		{
			ByteBuffer ibb = ByteBuffer.allocateDirect(lineVertices.length * 4);
			ibb.order(ByteOrder.nativeOrder());
			lineVerticesBuf = ibb.asFloatBuffer();
		}
		lineVerticesBuf.clear();
		lineVerticesBuf.put(lineVertices);
		lineVerticesBuf.position(0);

		C2D_Graphics.glPushMatrix();
		// ��ʼ����
		setTextureState(TexState_SHAPE);
		C2D_Graphics.glVertexPointer(2, C2D_Graphics.GL_FLOAT, 0, lineVerticesBuf);
		C2D_Graphics.glDrawArrays(C2D_Graphics.GL_LINE_LOOP, 0, 2);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * ��ͼ���أ��������Ὺ����������״̬����ͼ��������״̬��������ͼ
	 */
	public static int TexState_CLOSE = -1;// ��ʾ�ر�һ��
	public static int TexState_TEXTURE = 0;// ��ʾ������ͼ
	public static int TexState_SHAPE = 1;// ��ʾ������״
	private static int texState_current = TexState_CLOSE;// ��ǰ״̬

	public static void setTextureState(int state)
	{
		if (state == texState_current)
		{
			return;
		}
		if (state == TexState_TEXTURE)
		{
			C2D_Graphics.glEnable(C2D_Graphics.GL_TEXTURE_2D);
			C2D_Graphics.glEnableClientState(C2D_Graphics.GL_TEXTURE_COORD_ARRAY);
			C2D_Graphics.glEnableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
		}
		else if (state == TexState_SHAPE)
		{
			C2D_Graphics.glDisable(C2D_Graphics.GL_TEXTURE_2D);
			C2D_Graphics.glDisableClientState(C2D_Graphics.GL_TEXTURE_COORD_ARRAY);
			C2D_Graphics.glEnableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, 0);
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		else
		{
			C2D_Graphics.glDisable(C2D_Graphics.GL_TEXTURE_2D);
			C2D_Graphics.glDisableClientState(C2D_Graphics.GL_TEXTURE_COORD_ARRAY);
			C2D_Graphics.glDisableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, 0);
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		texState_current = state;
	}

	public void release()
	{
		if (m_drawClip != null)
		{
			m_drawClip.doRelease();
			m_drawClip = null;
		}
	}

	// FIME���������Ż�
	void drawImage_bak(C2D_Image textrueImage, C2D_RectF srcRect, C2D_RectF destRect, C3D_Matrix4 matrix)
	{
		m_drawClip.setImage(textrueImage);
		m_drawClip.setContentRectAndSize((int) srcRect.m_x, (int) srcRect.m_y, (int) srcRect.m_width, (int) srcRect.m_height);
		m_drawClip.setShowSize(destRect.m_width, destRect.m_height);
		if (matrix != null)
		{
			PushMatrix(matrix.data);
		}
		m_drawClip.draw(destRect.m_x, destRect.m_y);
		if (matrix != null)
		{
			PopMatrix();
		}
	}
}
