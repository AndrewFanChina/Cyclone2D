package c2d.plat.gfx;

import c2d.lang.app.C2D_Canvas;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * ������C2D_GdiGraphics
 * 
 * C2D_GdiImage: GDIͼƬ�࣬�ڲ�ʹ�ã������Զ�����ʽ����λͼ��ά��λͼռ�õ��ڴ档
 * C2D_GProto: ԭʼ�����࣬�ڲ�ʹ�ã��ṩ����ڲ����ʵķ�װ����C2D_GdiGraphics�ĸ��ࡣ
 * C2D_GdiGraphics: GDI�����࣬�ڲ�ʹ�ã��������GDIͼƬ����޸ģ����ṩ�����ϸ�����ʽ�Ļ��ƹ��ܡ�
 * C2D_Image: ͼƬ�࣬�ṩ�û�ʹ�ã������װGDIͼƬ�࣬�ṩ���û�������ʽ�Ĵ����ӿڡ�
 * C2D_Graphics: �����࣬�ṩ�û�ʹ�ã�����C2D_Image�Զ�����ʽ���Ƶ���Ļ��
 */
public class C2D_GdiGraphics
{
	android.graphics.Canvas gInner = null;
	private static android.graphics.Paint comPaint = new android.graphics.Paint();
	private static android.graphics.Rect comRect = new android.graphics.Rect();
	private static android.graphics.RectF comRectF = new android.graphics.RectF();
	private static android.graphics.Matrix comMatrix = new android.graphics.Matrix();

	/** ��ͼʱʹ�õķ�ת��־--�޷�ת */
	public static final byte TRANS_NONE = 0;

	/** ��ͼʱʹ�õķ�ת��־--��ֱ���� (�൱������ת180����ˮƽ����) */
	public static final byte TRANS_VERTICAL = 1;

	/** ��ͼʱʹ�õķ�ת��־--ˮƽ���� */
	public static final byte TRANS_HORIZENTAL = 2;

	/** ��ͼʱʹ�õķ�ת��־--˳ʱ������ת180�� */
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
	private static final byte[][] TRANS_ARRAY =
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

	private static C2D_PointI comPoint = new C2D_PointI();

	/**
	 * ���캯����һ����˵���㲻��Ҫ�����Լ���C2DGraphics
	 */
	public C2D_GdiGraphics()
	{
	}

	/**
	 * �����ڲ����ʣ�һ����˵���㲻Ӧ��ʹ���������
	 * 
	 */
	public void setInner(android.graphics.Canvas g)
	{
		gInner = g;
	}

	/**
	 * ��ȡ�ڲ����ʣ�һ����˵���㲻Ӧ��ʹ���������
	 * 
	 * @return �ڲ�����
	 */

	public android.graphics.Canvas getInner()
	{
		return gInner;
	}

	/**
	 * ��ARGB��ɫ��ȡ��ȷ��ϵͳ��ɫ������J2me��˵���������������ͬ�Ľ����
	 * ����Android��˵����Ϊ�佫AlphaΪ0ָ��Ϊ��ȫ͸����J2me����ͨ������
	 * ������ʾAlpha��Ϣ�����Ϊ�˿�ƽ̨�ԣ��������������Androidʱ���Ὣ
	 * AlphaΪ0��RGB��Ϊ0����ɫת����AlphaΪFF������J2me�ϵ�û����Alpha
	 * ��RGB��ɫ���Բ��ᱻ��Ϊ����ȫ͸��ɫ�������Android�ϣ���Ҫ������ȫ ͸��ɫ��ֻҪע��RGB�������ò�Ϊ0�Ϳ��ԡ�
	 * 
	 * @param color
	 *            ָ����ARGB��ɫ
	 * @return ���������ϵͳ��ɫ
	 */
	public static final int getColorARGB(int color)
	{
		if ((color & 0xFF000000) == 0 && (color & 0x00FFFFFF) != 0)
		{
			color |= 0xFF000000;
		}
		return color;
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
			C2D_Debug.logErr("error in gettransform");
			return -1;
		}
		return TRANS_ARRAY[transFrom][transPlus];
	}

	/**
	 * �Ի������ÿ�����ʾ���������������ȫ���赱ǰ����ʾ����ָ����ֵ��
	 * 
	 * @param x
	 *            ��ʾ����x����
	 * @param y
	 *            ��ʾ����Y����
	 * @param w
	 *            ��ʾ������
	 * @param h
	 *            ��ʾ����߶�
	 */
	public final void setClip(int x, int y, int w, int h)
	{
			gInner.clipRect(x, y, x + w, y + h, android.graphics.Region.Op.REPLACE);
	}

	/**
	 * �Ի������ÿ�����ʾ���������������ȫ���赱ǰ����ʾ����ָ����ֵ��
	 * 
	 * @param visibleRect
	 *            ��ʾ����
	 */
	public final void setClip(C2D_RectS visibleRect)
	{
			gInner.clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_x + visibleRect.m_width, visibleRect.m_y + visibleRect.m_height, android.graphics.Region.Op.REPLACE);
	}
	
	/**
	 * �Ի������ø�С�Ľ���������ʾ�������������������ǰ����ʾ���� ֻ�������С��ʾ������setClip��ͬ��setClip����ȫ����
	 * ��ǰ����ʾ����ָ����ֵ����clipRect������ڵ�ǰ����ʾ���� �������µ���ʾ���ơ�
	 * 
	 * @param x
	 *            ����������ʾ����x����
	 * @param y
	 *            ����������ʾ����y����
	 * @param w
	 *            ����������ʾ������
	 * @param h
	 *            ����������ʾ����߶�
	 */
	public final void clipRect(int x, int y, int w, int h)
	{
		gInner.clipRect(x, y, x + w, y + h, android.graphics.Region.Op.INTERSECT);
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
			gInner.clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_x + visibleRect.m_width, visibleRect.m_y + visibleRect.m_height);
	}

	/**
	 * ����ARGB��ɫ���ڲ������getColorARGB������ת����ϵͳ��ɫ
	 * 
	 * @param color
	 *            ���õ���ɫ
	 */
	public final void setARGBColor(int color)
	{
		comPaint.setColor(getColorARGB(color));
	}

	/**
	 * ���ð�͸����[0-255]
	 * @param alpha
	 */
	public final void setAlpha(int alpha)
	{
		comPaint.setAlpha(alpha);
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
	public final void fillArc(int x, int y, int width, int height, int startAngle, int sweepAngle, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawArc(comRectF, startAngle, sweepAngle, true, comPaint);

		C2D_Canvas.DrawCall++;
	}

	/**
	 * �����������,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRect
	 *            ����������ʾ����
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
	 */
	public final void fillArc(C2D_Array visibleRect, int x, int y, int width, int height, int startAngle, int sweepAngle, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawArc(comRectF, startAngle, sweepAngle, true, comPaint);

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
	public final void drawArc(int x, int y, int width, int height, int startAngle, int sweepAngle, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawArc(comRectF, startAngle, sweepAngle, true, comPaint);
		
		C2D_Canvas.DrawCall++;
	}

	/**
	 * �������α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRect
	 *            ����������ʾ����
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
	 */
	public final void drawArc(C2D_Array visibleRect, int x, int y, int width, int height, int startAngle, int sweepAngle, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawArc(comRectF, startAngle, sweepAngle, true, comPaint);

		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ�����ľ���,�ڲ������setARGBColor������ɫ����
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
	public final void fillRect(int x, int y, int width, int height, int color)
	{
		fillRect(x, y, width, height, color, 0, null);
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
	 */
	public final void fillRect(int x, int y, int width, int height, int color, int anchorPoint)
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
	public final void fillRect(int x, int y, int width, int height, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRect.left = comPoint.m_x;
		comRect.right = comPoint.m_x + width;
		comRect.top = comPoint.m_y;
		comRect.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawRect(comRect, comPaint);

		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ�����ľ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRects
	 *            C2D_Array ����������ʾ���򼯺�
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
	public final void fillRect(C2D_Array visibleRects, int x, int y, int width, int height, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRects, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRect.left = comPoint.m_x;
		comRect.right = comPoint.m_x + width;
		comRect.top = comPoint.m_y;
		comRect.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawRect(comRect, comPaint);
		
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
	public final void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color)
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
	public final void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint)
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
	public final void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawRoundRect(comRectF, arcWidth / 2, arcHeight / 2, comPaint);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ������Բ�Ǿ���,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRect
	 *            ����������ʾ�����б�
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
	public final void fillRoundRect(C2D_Array visibleRect, int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = comPoint.m_x;
		comRectF.right = comPoint.m_x + width;
		comRectF.top = comPoint.m_y;
		comRectF.bottom = comPoint.m_y + height;
		comPaint.setStyle(android.graphics.Paint.Style.FILL);
		gInner.drawRoundRect(comRectF, arcWidth / 2, arcHeight / 2, comPaint);
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
	public final void drawRect(int x, int y, int width, int height, int color)
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
	public final void drawRect(int x, int y, int width, int height, int color, int anchorPoint)
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
	public final void drawRect(int x, int y, int width, int height, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRect.left = comPoint.m_x;
		comRect.right = comPoint.m_x + width - 1;
		comRect.top = y;
		comRect.bottom = comPoint.m_y + height - 1;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawRect(comRect, comPaint);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ�����α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRects
	 *            C2D_Array ����������ʾ���򼯺�
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
	public final void drawRect(C2D_Array visibleRects, int x, int y, int width, int height, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRects, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRect.left = comPoint.m_x;
		comRect.right = comPoint.m_x + width - 1;
		comRect.top = y;
		comRect.bottom = comPoint.m_y + height - 1;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawRect(comRect, comPaint);

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
	public final void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint, C2D_RectS visibleRect)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRect, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = x;
		comRectF.right = x + width;
		comRectF.top = y;
		comRectF.bottom = y + height;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawRoundRect(comRectF, arcWidth / 2, arcHeight / 2, comPaint);
		C2D_Canvas.DrawCall++;

	}

	/**
	 * ����һ��Բ�Ǿ��α߿�,�ڲ������setARGBColor������ɫ����
	 * 
	 * @param visibleRects
	 *            ����������ʾ�����б�
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
	 */
	public final void drawRoundRect(C2D_Array visibleRects, int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint)
	{
		if (!applyClip(x, y, width, height, anchorPoint, visibleRects, comPoint))
		{
			return;
		}
		setARGBColor(color);
		comRectF.left = x;
		comRectF.right = x + width;
		comRectF.top = y;
		comRectF.bottom = y + height;
		comPaint.setStyle(android.graphics.Paint.Style.STROKE);
		gInner.drawRoundRect(comRectF, arcWidth / 2, arcHeight / 2, comPaint);
		C2D_Canvas.DrawCall++;

	}

		private static final float triangleBufs[] = new float[6];

	/**
	 * ����һ������������,�ڲ������setARGBColor������ɫ���� �ڲ����Զ���������ʾ�����򣬲�ʹ��setClip��������
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
		setARGBColor(color);
		triangleBufs[0] = x1;
		triangleBufs[1] = y1;
		triangleBufs[2] = x2;
		triangleBufs[3] = y2;
		triangleBufs[4] = x3;
		triangleBufs[5] = y3;
		gInner.drawVertices(android.graphics.Canvas.VertexMode.TRIANGLE_FAN, 3, triangleBufs, 0, null, 0, null, 0, null, 0, 0, comPaint);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * ����һ������������,�ڲ������setARGBColor������ɫ���� �����Զ����ÿ�����ʾ������,��Ҫ���Լ��ֶ�����
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

		setARGBColor(color);
		triangleBufs[0] = x1;
		triangleBufs[1] = y1;
		triangleBufs[2] = x2;
		triangleBufs[3] = y2;
		triangleBufs[4] = x3;
		triangleBufs[5] = y3;
		gInner.drawVertices(android.graphics.Canvas.VertexMode.TRIANGLE_FAN, 3, triangleBufs, 0, null, 0, null, 0, null, 0, 0, comPaint);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * �����߶�,�ڲ������setARGBColor������ɫ���� �ڲ����Զ���������ʾ�����򣬲�ʹ��setClip��������
	 * 
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
	public final void drawLineClip(int x1, int y1, int x2, int y2, int color)
	{
		setARGBColor(color);
		int xL = Math.min(x1, x2);
		int xR = Math.max(x1, x2);
		int yT = Math.min(y1, y2);
		int yB = Math.max(y1, y2);
		setClip(xL, yT, xR - xL + 1, yB - yT + 1);

		gInner.drawLine(x1, y1, x2, y2, comPaint);
		C2D_Canvas.DrawCall++;

	}

	/**
	 * �����߶�,�ڲ������setARGBColor������ɫ���� �����Զ����ÿ�����ʾ������,��Ҫ���Լ��ֶ�����
	 * 
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
	public final void drawLine(int x1, int y1, int x2, int y2, int color)
	{
		setARGBColor(color);
		// #if(Platform!="Android")
//@		gInner.drawLine(x1, y1, x2, y2);
		// #else
				gInner.drawLine(x1, y1, x2, y2, comPaint);
		// #endif
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

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�ã��൱�ڽ�ĳһ��ͼƬ����������� ��ָ����Ŀ��λ�á��������û��ָ�����ƿ������򣬽���������ʾ��û��ָ����ת
	 * ��־��������з�ת��û��ָ��ê�㣬�൱��ê�������ϽǶ��롣û��ָ����ʾ���� ����ζ�Ż�������ͼƬ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY)
	{
		if (image == null)
		{
			return;
		}
		drawImage(image, destX, destY, null, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ�����Ƶ���Ŀ��λ���Լ�ê�㣬�൱�ڽ�ĳһ��ͼƬ���������ê��
	 * ���Ƶ�ָ����Ŀ��λ�á��������û��ָ�����ƿ������򣬽���������ʾ��û��ָ����ת��־�� ������з�ת��û��ָ����ʾ������ζ�Ż�������ͼƬ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int anchorPoint)
	{
		if (image == null)
		{
			return;
		}
		drawImage(image, destX, destY, null, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ���Լ�ê��ͷ�ת��Ϣ���൱�ڽ�ĳһ��ͼƬ����
	 * ���ꡢê��ͷ�ת��Ϣ���Ƶ�ָ����Ŀ��λ�á��������û��ָ�����ƿ������򣬽���������ʾ�� û��ָ����ʾ������ζ�Ż�������ͼƬ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int anchorPoint, byte transform)
	{
		if (image == null)
		{
			return;
		}
		drawImage(image, destX, destY, null, anchorPoint, transform, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ���Լ�ê��ͷ�ת��Ϣ���൱�ڽ�ĳһ��ͼƬ����
	 * ���ꡢê��ͷ�ת��Ϣ���Ƶ�ָ����Ŀ��λ�á��������ûָ�������ƿ������򣬽���ֲ���ʾ�� û��ָ����ʾ������ζ�Ż�������ͼƬ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 * @param visibleRect
	 *            ��ͼʱ���ƵĿ��Ӿ�������
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int anchorPoint, byte transform, C2D_RectS visibleRect)
	{
		if (image == null)
		{
			return;
		}
		drawImage(image, destX, destY, null, anchorPoint, transform, visibleRect);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ�����൱�ڴ�ĳһ��ͼƬ�ϣ�
	 * �ü���һ�����򣬸���������Ƶ�ָ����Ŀ��λ�á��������û��ָ�����ƿ������򣬽�������
	 * ��ʾ��û��ָ����ת��־��������з�ת��û��ָ��ê�㣬�൱��ê�������ϽǶ��롣
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showRect
	 *            ��ȡԴͼ�ϵ���ʾ����
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect)
	{
		drawImage(image, destX, destY, showRect, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ�����Լ�ê����Ϣ��
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê�㣬���Ƶ�ָ����Ŀ��λ�á���
	 * ������û��ָ�����ƿ������򣬽���������ʾ��û��ָ����ת��־��������з�ת��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showRect
	 *            ��ȡԴͼ�ϵ���ʾ����
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint)
	{
		drawImage(image, destX, destY, showRect, anchorPoint, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ����ê����Ϣ���Լ���ת��־��
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê��ͷ�ת�����Ƶ�ָ����Ŀ��λ�á��� ������û��ָ�����ƿ������򣬽���������ʾ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showRect
	 *            ��ȡԴͼ�ϵ���ʾ����
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint, byte transform)
	{
		drawImage(image, destX, destY, showRect, anchorPoint, transform, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ����ê����Ϣ����ת��־���Լ����ƿ�������
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê��ͷ�ת�����Ƶ�ָ����Ŀ��λ�á����û��ָ������ �����������������ʾ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showRect
	 *            ��ȡԴͼ�ϵ���ʾ����
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 * @param visibleRect
	 *            ��ͼʱ���ƵĿ��Ӿ�������
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint, byte transform, C2D_RectS visibleRect)
	{
		// �����ȷ����ʾ����
		int showX, showY, showW, showH;
		if (showRect != null && showRect.m_width > 0 && showRect.m_height > 0)
		{
			showX = showRect.m_x;
			showY = showRect.m_y;
			showW = showRect.m_width;
			showH = showRect.m_height;
		}
		else
		{
			showX = 0;
			showY = 0;
			showW = image.getWidth();
			showH = image.getHeight();
		}
		drawImage(image, destX, destY, showX, showY, showW, showH, anchorPoint, transform, visibleRect);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á��Լ�ͼƬ�ϵ���ʾ���� �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������꣬���Ƶ�ָ����Ŀ��λ�á�
	 * �˺���û��ָ�����ƿ������򣬻�������ʾ�������з�ת�������Ͻ���Ϊê�㡣
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showX
	 *            Դͼ�ϵ���ʾ����X����
	 * @param showY
	 *            Դͼ�ϵ���ʾ����Y����
	 * @param showW
	 *            Դͼ�ϵ���ʾ������W
	 * @param showH
	 *            Դͼ�ϵ���ʾ����߶�H
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ�����Լ�ê����Ϣ��
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê�㣬���Ƶ�ָ����Ŀ��λ�á� �˺���û��ָ�����ƿ������򣬻�������ʾ�������з�ת��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showX
	 *            Դͼ�ϵ���ʾ����X����
	 * @param showY
	 *            Դͼ�ϵ���ʾ����Y����
	 * @param showW
	 *            Դͼ�ϵ���ʾ������W
	 * @param showH
	 *            Դͼ�ϵ���ʾ����߶�H
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, anchorPoint, TRANS_NONE, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ����ê����Ϣ���Լ���ת��־��
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê��ͷ�ת�����Ƶ�ָ����Ŀ��λ�á� �˺���û��ָ�����ƿ������򣬻�������ʾ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showX
	 *            Դͼ�ϵ���ʾ����X����
	 * @param showY
	 *            Դͼ�ϵ���ʾ����Y����
	 * @param showW
	 *            Դͼ�ϵ���ʾ������W
	 * @param showH
	 *            Դͼ�ϵ���ʾ����߶�H
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, anchorPoint, transform, null);
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ����ê����Ϣ����ת��־���Լ����ƿ�������
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê��ͷ�ת�����Ƶ�ָ����Ŀ��λ�á����û��ָ������ �����������������ʾ��
	 * 
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showX
	 *            Դͼ�ϵ���ʾ����X����
	 * @param showY
	 *            Դͼ�ϵ���ʾ����Y����
	 * @param showW
	 *            Դͼ�ϵ���ʾ������W
	 * @param showH
	 *            Դͼ�ϵ���ʾ����߶�H
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 * @param visibleRect
	 *            ��ͼʱ���ƵĿ��Ӿ�������
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform, C2D_RectS visibleRect)
	{
		if (image == null)
		{
			return;
		}
		// ����ת
		int wReal = showW;
		int hReal = showH;
		if ((transform & TRANS_FOLD_RT2LB) != 0)
		{
			wReal = showH;
			hReal = showW;
		}
		int destXI = (int) destX;
		int destYI = (int) destY;
		// #if Debug_DrawRegionError
		// @ try
		// @ {
		// #endif
		// ���ÿ�������
		if (anchorPoint !=  C2D_Consts.DEF)
		{
			if ((anchorPoint &  C2D_Consts.HCENTER) != 0)
			{
				destXI -= wReal >> 1;
			}
			else if ((anchorPoint & C2D_Consts.RIGHT) != 0)
			{
				destXI -= wReal;
			}
			else
			{
				anchorPoint |= C2D_Consts.LEFT;
			}

			if ((anchorPoint & C2D_Consts.VCENTER) != 0)
			{
				destYI -= hReal >> 1;
			}
			else if ((anchorPoint & C2D_Consts.BOTTOM) != 0)
			{
				destYI -= hReal;
			}
			else
			{
				anchorPoint |= C2D_Consts.TOP;
			}
		}
		setClip(destXI, destYI, wReal, hReal);
		// ���ý�ֹ��ʾ����
		if (visibleRect != null)
		{
			if (destXI + wReal <= visibleRect.m_x || destXI >= visibleRect.m_x + visibleRect.m_width || destYI + hReal <= visibleRect.m_y || destYI >= visibleRect.m_y + visibleRect.m_height)
			{
				return;
			}
			clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
		}
		// ��ʼ�滭
		if (transform == TRANS_NONE)
		{
			drawImage_Android(image, destXI - showX, destYI - showY);
		}
		else
		{
			drawRegion_Android(image, showX, showY, showW, showH, transform, destXI, destYI, 0);
		}
		C2D_Canvas.DrawCall++;
		// #if Debug_DrawRegionError
		// @ }
		// @ catch (Exception e)
		// @ {
		// @ C2D_Debug.log("drawImage excetion****imgWH:" +
		// @ // image.getWidth()
		// @ + "," + image.getHeight() + "    x:" + destXI + ",y:" + destYI
		// @ + ",_x:" + clipX + ",_y:" + clipY + ",_w:" + showW + ",_h:" + showH
		// @ + ",transform:" + transform);
		// @ e.printStackTrace();
		// @ }
		// #endif
	}

	/**
	 * ����ͼƬ��Ƭ
	 * 
	 * @param imgClip
	 *            ͼƬ��Ƭ
	 * @param x
	 *            Ŀ��λ��X����
	 * @param y
	 *            Ŀ��λ��Y����
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRect
	 *            ��������
	 */
	public final void drawImageClip(C2D_GdiImageClip imgClip, int x, int y, int anchorPoint, C2D_RectS visibleRect)
	{
		if (imgClip == null)
		{
			return;
		}
		drawImage(imgClip.getImage(), x, y, imgClip.getContentRect(), anchorPoint, imgClip.getTransform(), visibleRect);
	}

	/**
	 * ����ͼƬ��Ƭ
	 * 
	 * @param imgClip
	 *            ͼƬ��Ƭ
	 * @param x
	 *            Ŀ��λ��X����
	 * @param y
	 *            Ŀ��λ��Y����
	 * @param anchorPoint
	 *            ê��
	 * @param visibleRects
	 *            �������򼯺�
	 */
	public final void drawImageClip(C2D_GdiImageClip imgClip, int x, int y, int anchorPoint, C2D_Array visibleRects)
	{
		if (imgClip == null)
		{
			return;
		}
		C2D_RectS c = imgClip.getContentRect();
		if (c == null)
		{
			return;
		}
		drawImage(visibleRects, imgClip.getImage(), x, y, c.m_x, c.m_y, c.m_width, c.m_height, anchorPoint, imgClip.getTransform());
	}

	/**
	 * ����ͼƬ��ָ��ͼƬ�����Ƶ���Ŀ��λ�á�ͼƬ�ϵ���ʾ����ê����Ϣ����ת��־���Լ����ƿ�������
	 * �൱�ڴ�ĳһ��ͼƬ�ϣ��ü���һ�����򣬸������ꡢê��ͷ�ת�����Ƶ�ָ����Ŀ��λ�á����û��ָ������ �����������������ʾ��
	 * 
	 * @param visibleRects
	 *            ��ͼʱ���ƵĿ��Ӿ������򼯺�
	 * @param image
	 *            �����Ƶ�ͼƬ
	 * @param destX
	 *            ���Ƶ�Ŀ��λ�õ�����X
	 * @param destY
	 *            ���Ƶ�Ŀ��λ�õ�����Y
	 * @param showX
	 *            Դͼ�ϵ���ʾ����X����
	 * @param showY
	 *            Դͼ�ϵ���ʾ����Y����
	 * @param showW
	 *            Դͼ�ϵ���ʾ������W
	 * @param showH
	 *            Դͼ�ϵ���ʾ����߶�H
	 * @param anchorPoint
	 *            ê�㣬�μ�C2DGraphgics��ANCHORϵ�г���
	 * @param transform
	 *            ��ת��־���μ�C2DGraphgics��TRANSϵ�г���
	 */
	public final void drawImage(C2D_Array visibleRects, C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform)
	{
		if (image == null)
		{
			return;
		}
		// ����ת
		int wReal = showW;
		int hReal = showH;
		if ((transform & TRANS_FOLD_RT2LB) != 0)
		{
			wReal = showH;
			hReal = showW;
		}
		int destXI = (int) destX;
		int destYI = (int) destY;
		// #if Debug_DrawRegionError
		// @ try
		// @ {
		// #endif
		// ���ÿ�������
		if (anchorPoint != C2D_Consts.DEF)
		{
			if ((anchorPoint & C2D_Consts.HCENTER) != 0)
			{
				destXI -= wReal >> 1;
			}
			else if ((anchorPoint & C2D_Consts.RIGHT) != 0)
			{
				destXI -= wReal;
			}
			else
			{
				anchorPoint |= C2D_Consts.LEFT;
			}

			if ((anchorPoint & C2D_Consts.VCENTER) != 0)
			{
				destYI -= hReal >> 1;
			}
			else if ((anchorPoint & C2D_Consts.BOTTOM) != 0)
			{
				destYI -= hReal;
			}
			else
			{
				anchorPoint |= C2D_Consts.TOP;
			}
		}
		setClip(destXI, destYI, wReal, hReal);
		// ���ý�ֹ��ʾ����
		if (visibleRects != null)
		{
			for (int i = 0; i < visibleRects.size(); i++)
			{
				C2D_RectS visibleRect = (C2D_RectS) visibleRects.elementAt(i);
				if (destXI + wReal <= visibleRect.m_x || destXI >= visibleRect.m_x + visibleRect.m_width || destYI + hReal <= visibleRect.m_y || destYI >= visibleRect.m_y + visibleRect.m_height)
				{
					return;
				}
				clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
			}
		}
		// ��ʼ�滭
		if (transform == TRANS_NONE)
		{
			drawImage_Android(image, destXI - showX, destYI - showY);
		}
		else
		{
			drawRegion_Android(image, showX, showY, showW, showH, transform, destXI, destYI, 0);
		}
		C2D_Canvas.DrawCall++;
		// #if Debug_DrawRegionError
		// @ }
		// @ catch (Exception e)
		// @ {
		// @ C2D_Debug.log("drawImage excetion****imgWH:" +
		// @ // image.getWidth()
		// @ + "," + image.getHeight() + "    x:" + destXI + ",y:" + destYI
		// @ + ",_x:" + clipX + ",_y:" + clipY + ",_w:" + showW + ",_h:" + showH
		// @ + ",transform:" + transform);
		// @ e.printStackTrace();
		// @ }
		// #endif
	}

		public void drawImage_Android(C2D_GdiImage image, float x, float y)
		{
			gInner.drawBitmap(image.getInner(), x, y, comPaint);
		}
		/**
		 * ����ͼƬ��һ���ֵ�Ŀ��λ�ã������뷭ת��ê����
		 * @param image ��Ҫ���Ƶ�ͼƬ
		 * @param _x Դ�п�λ��X����
		 * @param _y Դ�п�λ��Y����
		 * @param _w Դ�п���
		 * @param _h Դ�п�߶�
		 * @param transform ��ת��Ϣ
		 * @param x Ŀ��λ��X����
		 * @param y Ŀ��λ��Y����
		 * @param anchor ê����
		 */
		public void drawRegion_Android(C2D_GdiImage image, int _x, int _y, int _w, int _h, int transform, int x, int y, int anchor)
		{
			comMatrix.reset();
			switch (transform)
			{
			case TRANS_VERTICAL:
				comMatrix.postScale(1, -1, _x, _y);
				comMatrix.postTranslate(x - _x, y - _y + _h);
				break;
			case TRANS_HORIZENTAL:
				comMatrix.postScale(-1, 1, _x, _y);
				comMatrix.postTranslate(x - _x + _w, y - _y);
				break;
			case TRANS_ROTATE180:
				comMatrix.postRotate(180, _x, _y);
				comMatrix.postTranslate(x - _x + _w, y - _y + _h);
				break;
			case TRANS_FOLD_RT2LB:
				comMatrix.postScale(-1, 1, _x, _y);
				comMatrix.postTranslate(_w, 0);
				comMatrix.postRotate(270, _x, _y);
				comMatrix.postTranslate(x - _x, y - _y + _w);
				break;
			case TRANS_ROTATE90:
				comMatrix.postRotate(90, _x, _y);
				comMatrix.postTranslate(x - _x + _h, y - _y);
				break;
			case TRANS_ROTATE270:
				comMatrix.postRotate(270, _x, _y);
				comMatrix.postTranslate(x - _x, y - _y + _w);
				break;
			case TRANS_FOLD_LT2RB:
				comMatrix.postScale(-1, 1, _x, _y);
				comMatrix.postTranslate(_w, 0);
				comMatrix.postRotate(90, _x, _y);
				comMatrix.postTranslate(x - _x + _h, y - _y);
				break;
			}
			gInner.drawBitmap(image.getInner(), comMatrix, comPaint);
		}

		/**
		 * ���ƿ������ŵ�ͼƬ
		 * @param image Դͼ
		 * @param x  Ŀ��x����
		 * @param y  Ŀ��y����
		 * @param _x Դ�п�x����
		 * @param _y Դ�п�y����
		 * @param _w Դ�п���
		 * @param _h Դ�п�߶�
		 * @param transform ��ת��Ϣ
		 * @param xScale x��������
		 * @param yScale y��������
		 */
		public final void drawScaleImage_Android(C2D_GdiImage image, int x, int y, int _x, int _y, int _w, int _h, int transform, float xScale, float yScale)
		{
			if (image == null)
			{
				return;
			}
			int wReal = (int) (_w * xScale);
			int hReal = (int) (_h * yScale);
			if ((transform & TRANS_FOLD_RT2LB) != 0)
			{
				wReal = (int) (_h * xScale);
				hReal = (int) (_w * yScale);
			}
			try
			{
				setClip(x, y, wReal, hReal);
				// setClip(g,0, 0, 480, 800);
				comMatrix.reset();
				switch (transform)
				{
				case TRANS_NONE:
					comMatrix.postScale(xScale, yScale, _x, _y);
					comMatrix.postTranslate(x - _x, y - _y);
					break;
				case TRANS_VERTICAL:
					comMatrix.postScale(xScale, -yScale, _x, _y);
					comMatrix.postTranslate(x - _x, y + -_y + _h * yScale);
					break;
				case TRANS_HORIZENTAL:
					comMatrix.postScale(-xScale, yScale, _x, _y);
					comMatrix.postTranslate(x - _x + _w * xScale, y - _y);
					break;
				case TRANS_ROTATE180:
					comMatrix.postScale(xScale, yScale, _x, _y);
					comMatrix.postRotate(180, _x, _y);
					comMatrix.postTranslate(x - _x + _w * xScale, y - _y + _h * yScale);
					break;
				case TRANS_FOLD_RT2LB:
					comMatrix.postScale(-yScale, xScale, _x, _y);
					comMatrix.postRotate(270, _x, _y);
					comMatrix.postTranslate(x - _x, y - _y);
					break;
				case TRANS_ROTATE90:
					comMatrix.postScale(yScale, xScale, _x, _y);
					comMatrix.postRotate(90, _x, _y);
					comMatrix.postTranslate(x - _x + _h * xScale, y - _y);
					break;
				case TRANS_ROTATE270:
					comMatrix.postScale(yScale, xScale, _x, _y);
					comMatrix.postRotate(270, _x, _y);
					comMatrix.postTranslate(x - _x, y - _y + _w * yScale);
					break;
				case TRANS_FOLD_LT2RB:
					comMatrix.postScale(-yScale, xScale, _x, _y);
					comMatrix.postRotate(90, _x, _y);
					comMatrix.postTranslate(x - _x + _h * xScale, y - _y + _w * yScale);
					break;
				}
				gInner.drawBitmap(image.getInner(), comMatrix, comPaint);
			}
			catch (Exception e)
			{
				C2D_Debug.log("draw excetion****imgWH:" + image.getWidth() + "," + image.getHeight() + "    x:" + x + ",y:" + y + ",_x:" + _x + ",_y:" + _y + ",_w:" + _w + ",_h:" + _h + ",transform:" + transform);
				e.printStackTrace();
			}
		}
	
		/**
		 * ���û��Ʊ��
		 * 
		 * @param flag
		 *            ���Ʊ��
		 */
		public static void setGraphicsFlag(int flag)
		{
			comPaint.setFlags(flag);
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
	public boolean applyClip(int x, int y, int width, int height, int anchorPoint, C2D_RectS visibleRect, C2D_PointI destP)
	{
		if (anchorPoint != C2D_Consts.DEF)
		{
			if ((anchorPoint & C2D_Consts.HCENTER) != 0)
			{
				x -= width >> 1;
			}
			else if ((anchorPoint & C2D_Consts.RIGHT) != 0)
			{
				x -= width;
			}
			if ((anchorPoint & C2D_Consts.VCENTER) != 0)
			{
				y -= height >> 1;
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
		return true;
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
	 * @param visibleRects
	 *            ������������
	 * @param destP
	 *            ת�����Ŀ��λ��
	 * @return �Ƿ���Ҫ����
	 */
	public boolean applyClip(int x, int y, int width, int height, int anchorPoint, C2D_Array visibleRects, C2D_PointI destP)
	{
		if (anchorPoint != C2D_Consts.DEF)
		{
			if ((anchorPoint & C2D_Consts.HCENTER) != 0)
			{
				x -= width >> 1;
			}
			else if ((anchorPoint & C2D_Consts.RIGHT) != 0)
			{
				x -= width;
			}
			if ((anchorPoint & C2D_Consts.VCENTER) != 0)
			{
				y -= height >> 1;
			}
			else if ((anchorPoint & C2D_Consts.BOTTOM) != 0)
			{
				y -= height;
			}
		}
		destP.m_x = x;
		destP.m_y = y;
		setClip(x, y, width, height);
		for (int i = 0; i < visibleRects.size(); i++)
		{
			C2D_RectS visibleRect = (C2D_RectS) visibleRects.elementAt(i);
			if (visibleRect != null)
			{
				if (x + width <= visibleRect.m_x || x >= visibleRect.m_x + visibleRect.m_width || y + height <= visibleRect.m_y || y >= visibleRect.m_y + visibleRect.m_height || visibleRect.m_width <= 0 || visibleRect.m_height <= 0)
				{
					return false;
				}
				clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
			}
		}
		return true;
	}

	/**
	 * ���ݲ��ֵ����꣬�ߴ磬��ת��ê�����Ϣ���������ճ��ֵľ��Σ��������resultRect�з���
	 * 
	 * @param x X����
	 * @param y Y����
	 * @param width ���
	 * @param height �߶�
	 * @param anchor ê��
	 * @param trans ��ת
	 * @param resultRect ��ż�����
	 * @return �Ƿ�ɹ�ȡ��
	 */
	public static boolean computeLayoutRect(float x, float y, float width, float height, int anchor, int trans, C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		// ����ʵ�ʳߴ�
		float wReal, hReal;
		if ((trans & C2D_GdiGraphics.TRANS_FOLD_RT2LB) != 0)
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
	private static C2D_PointF pointFTemp=new C2D_PointF();
	private static C2D_RectF rectFTemp=new C2D_RectF();
	/**
	 * ���ָ���ľ���������Ӧ��ê�㣬֮�󷵻����µ������
	 * @param x ��������x����
	 * @param y ��������y����
	 * @param w ����������w
	 * @param h ����������h
	 * @param anchor ê��
	 * @return ���µ������
	 */
	public static C2D_PointF applyAnchor(float x,float y,float w,float h,int anchor)
	{
		rectFTemp.setValue(x, y, w, h);
		return applyAnchor(rectFTemp,anchor);
	}
	/**
	 * ���ָ���ľ���������Ӧ��ê�㣬֮�󷵻����µ������
	 * @param rect     ��������
	 * @param anchor ê��
	 * @return ���µ������
	 */
	public static C2D_PointF applyAnchor(C2D_RectF rect,int anchor)
	{
		float x_offset_clip = 0;
		float y_offset_clip = 0;
		// ����ƫ��
		if ((C2D_Consts.LEFT & anchor) != 0) {
			x_offset_clip = 0;
		} else if ((C2D_Consts.HCENTER & anchor) != 0) {
			x_offset_clip = -rect.m_width / 2;
		} else if ((C2D_Consts.RIGHT & anchor) != 0) {
			x_offset_clip = -rect.m_width;
		}
		// ����ƫ��
		if ((C2D_Consts.TOP & anchor) != 0) {
			y_offset_clip = 0;
		} else if ((C2D_Consts.VCENTER & anchor) != 0) {
			y_offset_clip = -rect.m_height / 2;
		} else if ((C2D_Consts.BOTTOM & anchor) != 0) {
			y_offset_clip = -rect.m_height;
		}
		pointFTemp.setValue(rect.m_x+x_offset_clip, rect.m_y+y_offset_clip);
		return pointFTemp;
	}
}
