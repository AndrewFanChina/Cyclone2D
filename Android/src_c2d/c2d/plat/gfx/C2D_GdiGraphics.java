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
 * 画笔类C2D_GdiGraphics
 * 
 * C2D_GdiImage: GDI图片类，内部使用，负责以多种形式创建位图并维护位图占用的内存。
 * C2D_GProto: 原始画笔类，内部使用，提供针对内部画笔的封装，是C2D_GdiGraphics的父类。
 * C2D_GdiGraphics: GDI画笔类，内部使用，负责针对GDI图片类的修改，即提供在其上各种形式的绘制功能。
 * C2D_Image: 图片类，提供用户使用，负责包装GDI图片类，提供给用户多种形式的创建接口。
 * C2D_Graphics: 画笔类，提供用户使用，负责将C2D_Image以多种形式绘制到屏幕。
 */
public class C2D_GdiGraphics
{
	android.graphics.Canvas gInner = null;
	private static android.graphics.Paint comPaint = new android.graphics.Paint();
	private static android.graphics.Rect comRect = new android.graphics.Rect();
	private static android.graphics.RectF comRectF = new android.graphics.RectF();
	private static android.graphics.Matrix comMatrix = new android.graphics.Matrix();

	/** 绘图时使用的翻转标志--无翻转 */
	public static final byte TRANS_NONE = 0;

	/** 绘图时使用的翻转标志--垂直镜像 (相当于先旋转180度再水平镜像) */
	public static final byte TRANS_VERTICAL = 1;

	/** 绘图时使用的翻转标志--水平镜像 */
	public static final byte TRANS_HORIZENTAL = 2;

	/** 绘图时使用的翻转标志--顺时针旋旋转180度 */
	public static final byte TRANS_ROTATE180 = 3;

	/** 绘图时使用的翻转标志--右上角对折左下角 (相当于先顺时针旋转270度再水平镜像) */
	public static final byte TRANS_FOLD_RT2LB = 4;

	/** 绘图时使用的翻转标志--顺时针旋转90度 */
	public static final byte TRANS_ROTATE90 = 5;

	/** 绘图时使用的翻转标志--顺时针旋转270度 */
	public static final byte TRANS_ROTATE270 = 6;

	/** 绘图时使用的翻转标志--左上角对折右下角 (相当于先旋转90度再水平镜像) */
	public static final byte TRANS_FOLD_LT2RB = 7;

	/** 绘图时使用的翻转标志的转换数组 */
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
	 * 构造函数，一般来说，你不需要构造自己的C2DGraphics
	 */
	public C2D_GdiGraphics()
	{
	}

	/**
	 * 设置内部画笔，一般来说，你不应该使用这个方法
	 * 
	 */
	public void setInner(android.graphics.Canvas g)
	{
		gInner = g;
	}

	/**
	 * 获取内部画笔，一般来说，你不应该使用这个方法
	 * 
	 * @return 内部画笔
	 */

	public android.graphics.Canvas getInner()
	{
		return gInner;
	}

	/**
	 * 从ARGB颜色获取正确的系统颜色，对于J2me来说，这个方法返回相同的结果，
	 * 对于Android来说，因为其将Alpha为0指定为完全透明，J2me的普通绘制则
	 * 不能显示Alpha信息，因此为了跨平台性，这个函数在用于Android时，会将
	 * Alpha为0，RGB不为0的颜色转换气Alpha为FF，这样J2me上的没设置Alpha
	 * 的RGB颜色可以不会被认为是完全透明色，如果在Android上，需要设置完全 透明色，只要注意RGB任意设置不为0就可以。
	 * 
	 * @param color
	 *            指定的ARGB颜色
	 * @return 经过处理的系统颜色
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
	 * 获取叠加的翻转操作结果，即基于一个翻转标标记，叠加另一个翻转标记
	 * 
	 * @param transFrom
	 *            基于的翻转操作
	 * @param transPlus
	 *            叠加的翻转操作
	 * @return 复合之后的翻转操作
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
	 * 对画笔设置可以显示的区域，这个函数完全重设当前的显示区域到指定的值。
	 * 
	 * @param x
	 *            显示区域x坐标
	 * @param y
	 *            显示区域Y坐标
	 * @param w
	 *            显示区域宽度
	 * @param h
	 *            显示区域高度
	 */
	public final void setClip(int x, int y, int w, int h)
	{
			gInner.clipRect(x, y, x + w, y + h, android.graphics.Region.Op.REPLACE);
	}

	/**
	 * 对画笔设置可以显示的区域，这个函数完全重设当前的显示区域到指定的值。
	 * 
	 * @param visibleRect
	 *            显示区域
	 */
	public final void setClip(C2D_RectS visibleRect)
	{
			gInner.clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_x + visibleRect.m_width, visibleRect.m_y + visibleRect.m_height, android.graphics.Region.Op.REPLACE);
	}
	
	/**
	 * 对画笔设置更小的交叉限制显示区域，这个函数不能扩大当前的显示区域， 只会更加缩小显示区域。与setClip不同，setClip会完全重设
	 * 当前的显示区域到指定的值。而clipRect将会基于当前的显示区域 ，增加新的显示限制。
	 * 
	 * @param x
	 *            交叉限制显示区域x坐标
	 * @param y
	 *            交叉限制显示区域y坐标
	 * @param w
	 *            交叉限制显示区域宽度
	 * @param h
	 *            交叉限制显示区域高度
	 */
	public final void clipRect(int x, int y, int w, int h)
	{
		gInner.clipRect(x, y, x + w, y + h, android.graphics.Region.Op.INTERSECT);
	}

	/**
	 * 对画笔设置更小的交叉限制显示区域，这个函数不能扩大当前的显示区域， 只会更加缩小显示区域。与setClip不同，setClip会完全重设
	 * 当前的显示区域到指定的值。而clipRect将会基于当前的显示区域 ，增加新的显示限制。
	 * 
	 * @param visibleRect
	 *            交叉限制显示区域
	 */
	public final void clipRect(C2D_RectS visibleRect)
	{
			gInner.clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_x + visibleRect.m_width, visibleRect.m_y + visibleRect.m_height);
	}

	/**
	 * 设置ARGB颜色，内部会调用getColorARGB方法，转换成系统颜色
	 * 
	 * @param color
	 *            设置的颜色
	 */
	public final void setARGBColor(int color)
	{
		comPaint.setColor(getColorARGB(color));
	}

	/**
	 * 设置半透明度[0-255]
	 * @param alpha
	 */
	public final void setAlpha(int alpha)
	{
		comPaint.setAlpha(alpha);
	}

	/**
	 * 绘制扇形填充,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            横向直径
	 * @param height
	 *            纵向直径
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            扇形开口角度
	 * @param color
	 *            ARGB颜色
	 * @param anchorPoint
	 *            锚点坐标
	 * @param visibleRect
	 *            限制显示区域
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
	 * 绘制扇形填充,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRect
	 *            交叉限制显示区域
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            横向直径
	 * @param height
	 *            纵向直径
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            扇形开口角度
	 * @param color
	 *            ARGB颜色
	 * @param anchorPoint
	 *            锚点坐标
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
	 * 绘制扇形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            横向直径
	 * @param height
	 *            纵向直径
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            扇形开口角度
	 * @param color
	 *            ARGB颜色
	 * @param anchorPoint
	 *            锚点坐标
	 * @param visibleRect
	 *            限制显示区域
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
	 * 绘制扇形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRect
	 *            交叉限制显示区域
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            横向直径
	 * @param height
	 *            纵向直径
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            扇形开口角度
	 * @param color
	 *            ARGB颜色
	 * @param anchorPoint
	 *            锚点坐标
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
	 * 绘制一个填充的矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 */
	public final void fillRect(int x, int y, int width, int height, int color)
	{
		fillRect(x, y, width, height, color, 0, null);
	}

	/**
	 * 绘制一个填充的矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 */
	public final void fillRect(int x, int y, int width, int height, int color, int anchorPoint)
	{
		fillRect(x, y, width, height, color, anchorPoint, null);
	}

	/**
	 * 绘制一个填充的矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRect
	 *            RectS 交叉限制显示区域
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
	 * 绘制一个填充的矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRects
	 *            C2D_Array 交叉限制显示区域集合
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
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
	 * 绘制一个填充的圆角矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 */
	public final void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color)
	{
		fillRoundRect(x, y, width, height, arcWidth, arcHeight, color, 0, null);
	}

	/**
	 * 绘制一个填充的圆角矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 */
	public final void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color, int anchorPoint)
	{
		fillRoundRect(x, y, width, height, arcWidth, arcHeight, color, anchorPoint, null);
	}

	/**
	 * 绘制一个填充的圆角矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRect
	 *            交叉限制显示区域
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
	 * 绘制一个填充的圆角矩形,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRect
	 *            交叉限制显示区域列表
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
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
	 * 绘制一个矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 */
	public final void drawRect(int x, int y, int width, int height, int color)
	{
		drawRect(x, y, width, height, color, 0, null);
	}

	/**
	 * 绘制一个矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 */
	public final void drawRect(int x, int y, int width, int height, int color, int anchorPoint)
	{
		drawRect(x, y, width, height, color, anchorPoint, null);
	}

	/**
	 * 绘制一个矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRect
	 *            RectS 交叉限制显示区域
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
	 * 绘制一个矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRects
	 *            C2D_Array 交叉限制显示区域集合
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点
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
	 * 绘制一个圆角矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 */
	public final void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, int color)
	{
		drawRoundRect(x, y, width, height, arcWidth, arcHeight, color, 0, null);
	}

	/**
	 * 绘制一个圆角矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点坐标
	 * @param visibleRect
	 *            交叉限制显示区域
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
	 * 绘制一个圆角矩形边框,内部会调用setARGBColor进行颜色设置
	 * 
	 * @param visibleRects
	 *            交叉限制显示区域列表
	 * @param x
	 *            左上角X坐标
	 * @param y
	 *            左上角Y坐标
	 * @param width
	 *            圆角矩形的宽度
	 * @param height
	 *            圆角矩形的高度
	 * @param arcWidth
	 *            圆角的宽度
	 * @param arcHeight
	 *            圆角的宽度
	 * @param color
	 *            颜色
	 * @param anchorPoint
	 *            锚点坐标
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
	 * 绘制一个填充的三角形,内部会调用setARGBColor进行颜色设置 内部会自动辨别可以显示的区域，并使用setClip进行设置
	 * 
	 * @param x1
	 *            第1个顶点X坐标
	 * @param y1
	 *            第1个顶点Y坐标
	 * @param x2
	 *            第2个顶点X坐标
	 * @param y2
	 *            第2个顶点Y坐标
	 * @param x3
	 *            第3个顶点X坐标
	 * @param y3
	 *            第3个顶点Y坐标
	 * @param color
	 *            颜色
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
	 * 绘制一个填充的三角形,内部会调用setARGBColor进行颜色设置 不会自动设置可以显示的区域,需要你自己手动设置
	 * 
	 * @param x1
	 *            第1个顶点X坐标
	 * @param y1
	 *            第1个顶点Y坐标
	 * @param x2
	 *            第2个顶点X坐标
	 * @param y2
	 *            第2个顶点Y坐标
	 * @param x3
	 *            第3个顶点X坐标
	 * @param y3
	 *            第3个顶点Y坐标
	 * @param color
	 *            颜色
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
	 * 绘制线段,内部会调用setARGBColor进行颜色设置 内部会自动辨别可以显示的区域，并使用setClip进行设置
	 * 
	 * @param x1
	 *            第1个顶点X坐标
	 * @param y1
	 *            第1个顶点Y坐标
	 * @param x2
	 *            第2个顶点X坐标
	 * @param y2
	 *            第2个顶点Y坐标
	 * @param color
	 *            颜色
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
	 * 绘制线段,内部会调用setARGBColor进行颜色设置 不会自动设置可以显示的区域,需要你自己手动设置
	 * 
	 * @param x1
	 *            第1个顶点X坐标
	 * @param y1
	 *            第1个顶点Y坐标
	 * @param x2
	 *            第2个顶点X坐标
	 * @param y2
	 *            第2个顶点Y坐标
	 * @param color
	 *            颜色
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

	/**
	 * 绘制图片，指定图片、绘制到的目标位置，相当于将某一张图片根据坐标绘制 到指定的目标位置。这个函数没有指定限制可视区域，将会完整显示。没有指定翻转
	 * 标志，不会进行翻转。没有指定锚点，相当于锚点以左上角对齐。没有指定显示区域 ，意味着绘制整个图片。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
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
	 * 绘制图片，指定绘制到的目标位置以及锚点，相当于将某一张图片根据坐标和锚点
	 * 绘制到指定的目标位置。这个函数没有指定限制可视区域，将会完整显示。没有指定翻转标志， 不会进行翻转。没有指定显示区域，意味着绘制整个图片。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
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
	 * 绘制图片，指定图片、绘制到的目标位置以及锚点和翻转信息，相当于将某一张图片根据
	 * 坐标、锚点和翻转信息绘制到指定的目标位置。这个函数没有指定限制可视区域，将会完整显示。 没有指定显示区域，意味着绘制整个图片。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
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
	 * 绘制图片，指定图片、绘制到的目标位置以及锚点和翻转信息，相当于将某一张图片根据
	 * 坐标、锚点和翻转信息绘制到指定的目标位置。这个函数没指定了限制可视区域，将会局部显示。 没有指定显示区域，意味着绘制整个图片。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 * @param visibleRect
	 *            绘图时限制的可视矩形区域
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
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域，相当于从某一张图片上，
	 * 裁剪出一块区域，根据坐标绘制到指定的目标位置。这个函数没有指定限制可视区域，将会完整
	 * 显示。没有指定翻转标志，不会进行翻转。没有指定锚点，相当于锚点以左上角对齐。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showRect
	 *            截取源图上的显示区域
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect)
	{
		drawImage(image, destX, destY, showRect, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、以及锚点信息，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点，绘制到指定的目标位置。这
	 * 个函数没有指定限制可视区域，将会完整显示。没有指定翻转标志，不会进行翻转。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showRect
	 *            截取源图上的显示区域
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint)
	{
		drawImage(image, destX, destY, showRect, anchorPoint, TRANS_NONE, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、锚点信息、以及翻转标志，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点和翻转，绘制到指定的目标位置。这 个函数没有指定限制可视区域，将会完整显示。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showRect
	 *            截取源图上的显示区域
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint, byte transform)
	{
		drawImage(image, destX, destY, showRect, anchorPoint, transform, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、锚点信息、翻转标志、以及限制可视区域，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点和翻转，绘制到指定的目标位置。如果没有指定限制 可视区域，则会完整显示。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showRect
	 *            截取源图上的显示区域
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 * @param visibleRect
	 *            绘图时限制的可视矩形区域
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, C2D_RectS showRect, int anchorPoint, byte transform, C2D_RectS visibleRect)
	{
		// 获得正确的显示区域
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
	 * 绘制图片，指定图片、绘制到的目标位置、以及图片上的显示区域， 相当于从某一张图片上，裁剪出一块区域，根据坐标，绘制到指定的目标位置。
	 * 此函数没有指定限制可视区域，会完整显示，不进行翻转，以左上角作为锚点。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showX
	 *            源图上的显示区域X坐标
	 * @param showY
	 *            源图上的显示区域Y坐标
	 * @param showW
	 *            源图上的显示区域宽度W
	 * @param showH
	 *            源图上的显示区域高度H
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, C2D_Consts.DEF, TRANS_NONE, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、以及锚点信息，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点，绘制到指定的目标位置。 此函数没有指定限制可视区域，会完整显示，不进行翻转。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showX
	 *            源图上的显示区域X坐标
	 * @param showY
	 *            源图上的显示区域Y坐标
	 * @param showW
	 *            源图上的显示区域宽度W
	 * @param showH
	 *            源图上的显示区域高度H
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, anchorPoint, TRANS_NONE, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、锚点信息、以及翻转标志，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点和翻转，绘制到指定的目标位置。 此函数没有指定限制可视区域，会完整显示。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showX
	 *            源图上的显示区域X坐标
	 * @param showY
	 *            源图上的显示区域Y坐标
	 * @param showW
	 *            源图上的显示区域宽度W
	 * @param showH
	 *            源图上的显示区域高度H
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform)
	{
		drawImage(image, destX, destY, showX, showY, showW, showH, anchorPoint, transform, null);
	}

	/**
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、锚点信息、翻转标志、以及限制可视区域，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点和翻转，绘制到指定的目标位置。如果没有指定限制 可视区域，则会完整显示。
	 * 
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showX
	 *            源图上的显示区域X坐标
	 * @param showY
	 *            源图上的显示区域Y坐标
	 * @param showW
	 *            源图上的显示区域宽度W
	 * @param showH
	 *            源图上的显示区域高度H
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 * @param visibleRect
	 *            绘图时限制的可视矩形区域
	 */
	public final void drawImage(C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform, C2D_RectS visibleRect)
	{
		if (image == null)
		{
			return;
		}
		// 处理翻转
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
		// 设置可视区域
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
		// 设置禁止显示区域
		if (visibleRect != null)
		{
			if (destXI + wReal <= visibleRect.m_x || destXI >= visibleRect.m_x + visibleRect.m_width || destYI + hReal <= visibleRect.m_y || destYI >= visibleRect.m_y + visibleRect.m_height)
			{
				return;
			}
			clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
		}
		// 开始绘画
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
	 * 绘制图片切片
	 * 
	 * @param imgClip
	 *            图片切片
	 * @param x
	 *            目标位置X坐标
	 * @param y
	 *            目标位置Y坐标
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRect
	 *            可视区域
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
	 * 绘制图片切片
	 * 
	 * @param imgClip
	 *            图片切片
	 * @param x
	 *            目标位置X坐标
	 * @param y
	 *            目标位置Y坐标
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRects
	 *            可视区域集合
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
	 * 绘制图片，指定图片、绘制到的目标位置、图片上的显示区域、锚点信息、翻转标志、以及限制可视区域，
	 * 相当于从某一张图片上，裁剪出一块区域，根据坐标、锚点和翻转，绘制到指定的目标位置。如果没有指定限制 可视区域，则会完整显示。
	 * 
	 * @param visibleRects
	 *            绘图时限制的可视矩形区域集合
	 * @param image
	 *            被绘制的图片
	 * @param destX
	 *            绘制到目标位置的坐标X
	 * @param destY
	 *            绘制到目标位置的坐标Y
	 * @param showX
	 *            源图上的显示区域X坐标
	 * @param showY
	 *            源图上的显示区域Y坐标
	 * @param showW
	 *            源图上的显示区域宽度W
	 * @param showH
	 *            源图上的显示区域高度H
	 * @param anchorPoint
	 *            锚点，参见C2DGraphgics的ANCHOR系列常量
	 * @param transform
	 *            翻转标志，参见C2DGraphgics的TRANS系列常量
	 */
	public final void drawImage(C2D_Array visibleRects, C2D_GdiImage image, float destX, float destY, int showX, int showY, int showW, int showH, int anchorPoint, byte transform)
	{
		if (image == null)
		{
			return;
		}
		// 处理翻转
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
		// 设置可视区域
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
		// 设置禁止显示区域
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
		// 开始绘画
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
		 * 绘制图片的一部分到目标位置，并加入翻转和锚点标记
		 * @param image 将要绘制的图片
		 * @param _x 源切块位置X坐标
		 * @param _y 源切块位置Y坐标
		 * @param _w 源切块宽度
		 * @param _h 源切块高度
		 * @param transform 翻转信息
		 * @param x 目标位置X坐标
		 * @param y 目标位置Y坐标
		 * @param anchor 锚点标记
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
		 * 绘制可以缩放的图片
		 * @param image 源图
		 * @param x  目标x坐标
		 * @param y  目标y坐标
		 * @param _x 源切块x坐标
		 * @param _y 源切块y坐标
		 * @param _w 源切块宽度
		 * @param _h 源切块高度
		 * @param transform 翻转信息
		 * @param xScale x方向缩放
		 * @param yScale y方向缩放
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
		 * 设置绘制标记
		 * 
		 * @param flag
		 *            绘制标记
		 */
		public static void setGraphicsFlag(int flag)
		{
			comPaint.setFlags(flag);
		}
	

	/**
	 * 针对目标应用锚点和可视区域
	 * 
	 * @param x
	 *            目标坐标x
	 * @param y
	 *            目标坐标y
	 * @param width
	 *            目标区域宽度
	 * @param height
	 *            目标区域高度
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRect
	 *            可视区域
	 * @param destP
	 *            转换后的目标位置
	 * @return 是否还需要绘制
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
	 * 针对目标应用锚点和可视区域
	 * 
	 * @param x
	 *            目标坐标x
	 * @param y
	 *            目标坐标y
	 * @param width
	 *            目标区域宽度
	 * @param height
	 *            目标区域高度
	 * @param anchorPoint
	 *            锚点
	 * @param visibleRects
	 *            可是区域数组
	 * @param destP
	 *            转换后的目标位置
	 * @return 是否还需要绘制
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
	 * 根据布局的坐标，尺寸，翻转，锚点等信息，计算最终呈现的矩形，并存放在resultRect中返回
	 * 
	 * @param x X坐标
	 * @param y Y坐标
	 * @param width 宽度
	 * @param height 高度
	 * @param anchor 锚点
	 * @param trans 翻转
	 * @param resultRect 存放计算结果
	 * @return 是否成功取得
	 */
	public static boolean computeLayoutRect(float x, float y, float width, float height, int anchor, int trans, C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		// 计算实际尺寸
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
		// 计算实际坐标
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
		// 存储数据
		resultRect.m_x = xReal;
		resultRect.m_y = yReal;
		resultRect.m_width = wReal;
		resultRect.m_height = hReal;
		return true;
	}

	/**
	 * 从一张图片（源图）中截取一个矩形区域，以指定的翻转标记， 绘制到另外一张图片（目标图）的指定位置中。源图和目标图
	 * 均是以整形数组形式保存的像素数据数组。
	 * 
	 * @param srcImg
	 *            源图
	 * @param _x
	 *            从源图中截取的矩形区域的X坐标
	 * @param _y
	 *            从源图中截取的矩形区域的Y坐标
	 * @param _w
	 *            从源图中截取的矩形区域的宽度W
	 * @param _h
	 *            从源图中截取的矩形区域的高度H
	 * @param imgW_s
	 *            源图的宽度
	 * @param imgH_s
	 *            源图的高度
	 * @param destImg
	 *            目标图
	 * @param x_d
	 *            绘制到目标位置X坐标
	 * @param y_d
	 *            绘制到目标位置Y坐标
	 * @param imgW_d
	 *            目标图宽度
	 * @param imgH_d
	 *            目标图高度
	 * @param transFlag
	 *            翻转标志
	 * @return 是否成功绘制
	 */
	public static boolean drawBitmap(int[] srcImg, int _x, int _y, int _w, int _h, int imgW_s, int imgH_s, int[] destImg, int x_d, int y_d, int imgW_d, int imgH_d, int transFlag)
	{
		if (srcImg == null || destImg == null || srcImg.length < imgW_s * imgH_s || destImg.length < imgW_d * imgH_d)
		{
			return false;
		}
		// 剪切源矩形
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
		// 计算目标矩形尺寸
		int w_d = _w;
		int h_d = _h;
		if ((int) transFlag >= (int) TRANS_FOLD_RT2LB)
		{
			w_d = _h;
			h_d = _w;
		}
		// 计算目标矩形位置
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
		// 开始绘制
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
	 * 针对指定的矩形区域型应用锚点，之后返回其新的坐标点
	 * @param x 矩形区域x坐标
	 * @param y 矩形区域y坐标
	 * @param w 矩形区域宽度w
	 * @param h 矩形区域宽度h
	 * @param anchor 锚点
	 * @return 其新的坐标点
	 */
	public static C2D_PointF applyAnchor(float x,float y,float w,float h,int anchor)
	{
		rectFTemp.setValue(x, y, w, h);
		return applyAnchor(rectFTemp,anchor);
	}
	/**
	 * 针对指定的矩形区域型应用锚点，之后返回其新的坐标点
	 * @param rect     矩形区域
	 * @param anchor 锚点
	 * @return 其新的坐标点
	 */
	public static C2D_PointF applyAnchor(C2D_RectF rect,int anchor)
	{
		float x_offset_clip = 0;
		float y_offset_clip = 0;
		// 横向偏移
		if ((C2D_Consts.LEFT & anchor) != 0) {
			x_offset_clip = 0;
		} else if ((C2D_Consts.HCENTER & anchor) != 0) {
			x_offset_clip = -rect.m_width / 2;
		} else if ((C2D_Consts.RIGHT & anchor) != 0) {
			x_offset_clip = -rect.m_width;
		}
		// 纵向偏移
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
