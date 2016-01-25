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
	/** 绘图时使用的翻转标志--无翻转 */
	public static final byte TRANS_NONE = 0;

	/** 绘图时使用的翻转标志--垂直镜像 (相当于先旋转180度再水平镜像) */
	public static final byte TRANS_VERTICAL = 1;

	/** 绘图时使用的翻转标志--水平镜像 */
	public static final byte TRANS_HORIZENTAL = 2;

	/** 绘图时使用的翻转标志--顺时针旋转180度 */
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

	// OpenGL参数
	public static final int FLag_Dither = C2D_Graphics.GL_DITHER;// 颜色抖动
	/**
	 * 当前画布大小
	 */
	private C2D_RectS m_canvasSize = new C2D_RectS();

	/**
	 * 构造函数，一般来说，你不需要构造自己的C2DGraphics
	 */
	public C2D_Graphics()
	{
	}

	/**
	 * 设置当前颜色(完全不透明)
	 * 
	 * @param color
	 *            颜色数值，会被转换成RRGGBB形式
	 */
	void setRGBColor(int color)
	{
		setColor(color & 0xFFFFFF);
	}

	/**
	 * 设置当前颜色(由RGB和制定的alpha来合成颜色)
	 * 
	 * @param color
	 *            RRGGBB形式的颜色数值
	 * @param alpha
	 *            AA形式的半透明数值
	 */
	void setColor(int color, int alpha)
	{
		setColor((color & 0xFFFFFF) | ((alpha & 0xFF) << 24));
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
			C2D_Debug.log("error in gettransform");
			return -1;
		}
		return TRANS_ARRAY[transFrom][transPlus];
	}

	/**
	 * 对画笔设置可以显示的区域，这个函数完全重设当前的显示区域到指定的值。
	 * 
	 * @param visibleRect
	 *            显示区域
	 */
	public final void setClip(C2D_RectF visibleRect)
	{
		setClip(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
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
		clipRect(visibleRect.m_x, visibleRect.m_y, visibleRect.m_width, visibleRect.m_height);
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
	 * 绘制一个填充的矩形,内部会进行颜色设置
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
	public final void fillRect(float x, float y, float width, float height, int color)
	{
		fillRect(x, y, width, height, color, 0, null);
	}

	/**
	 * 绘制一个填充的矩形,内部会进行颜色设置
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
	public final void fillRect(float x, float y, float width, float height, int color, int anchorPoint)
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
	public final void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight, int color)
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
	public final void fillRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight, int color, int anchorPoint)
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
	public final void drawRect(float x, float y, float width, float height, int color)
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
	public final void drawRect(float x, float y, float width, float height, int color, int anchorPoint)
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
	 * 绘制一个填充的三角形,内部会进行颜色设置 内部会自动辨别可以显示的区域，并使用setClip进行设置
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
		clipRect(m_canvasSize);
		setColor(color);
		fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * 绘制一个填充的三角形,内部会进行颜色设置 不会自动设置可以显示的区域,需要你自己手动设置
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

		setColor(color);
		fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
		C2D_Canvas.DrawCall++;
	}

	/**
	 * 绘制线段,内部会进行颜色设置 不会自动设置可以显示的区域,需要你自己手动设置
	 * 
	 * @param visibleRect
	 *            限制显示区域
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

	private C2D_ImageClip m_drawClip = new C2D_ImageClip();

	/**
	 * 使用指定颜色清除屏幕
	 * 
	 * @param color
	 *            AARRGGBB形式的颜色数值
	 */
	public void clearScreen(int color)
	{
		float r = ((color >> 16) & 0xFF) / 255.0f;
		float g = ((color >> 8) & 0xFF) / 255.0f;
		float b = ((color >> 0) & 0xFF) / 255.0f;
		float a = ((color >> 24) & 0xFF) / 255.0f;
		C2D_Graphics.glClearColor(r, g, b, a);
		// 清除颜色和深度为缓冲
		C2D_Graphics.glClear(C2D_Graphics.GL_COLOR_BUFFER_BIT | C2D_Graphics.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * 获得过渡中间颜色
	 * 
	 * @param colorFrom
	 *            起始颜色
	 * @param colorDest
	 *            目标颜色
	 * @param percent
	 *            当前百分比[0.0f,1.0f]
	 * @return the 过渡中间色
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
	 * 设置OpenGL参数
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
	 * 设置点描绘时点的大小
	 * 
	 * @param size
	 */
	public void setPointSize(float size)
	{
		C2D_Graphics.glPointSize(size);
	}

	/**
	 * 设置线描绘时线的宽度
	 * 
	 * @param width
	 */
	public void setLineWidth(float width)
	{
		C2D_Graphics.glLineWidth(width);
	}

	// 矢量图形绘制-----------------------------------------------------------------------
	// /**
	// * 绘制圆形边框，中心对齐
	// * @param x 中心X坐标
	// * @param y 中心Y坐标
	// * @param width 横向直径
	// * @param height纵向直径
	// */
	// public void drawCircle(int x, int y, int width, int height)
	// {
	// drawCircleBase(C2D_Graphics.GL_LINE_LOOP,x, y, width, height);
	// }
	// /**
	// * 绘制圆形填充，中心对齐
	// * @param x 中心X坐标
	// * @param y 中心Y坐标
	// * @param width 横向直径
	// * @param height纵向直径
	// */
	// public void fillCircle(int x, int y, int width, int height)
	// {
	// drawCircleBase(C2D_Graphics.GL_TRIANGLE_FAN,x, y, width, height);
	// }
	//
	// /**
	// * 绘制圆形辅助参数
	// */
	// private static FloatBuffer circleBuffer;//绘制圆形的辅助缓冲
	// private static int CIRCLE_PointCount=64;//标准圆形的顶点个数
	// private static float[] circleVertices= new
	// float[CIRCLE_PointCount*2];//标准圆形的顶点
	// private static final float circleR=200.0f;//标准圆形的直径
	// /**
	// * 绘制圆形
	// * @param mode 填充模式
	// * @param x 中心X坐标
	// * @param y 中心Y坐标
	// * @param width 横向直径
	// * @param height纵向直径
	// */
	// private static void drawCircleBase(int mode,int x, int y, int width, int
	// height)
	// {
	// if(circleBuffer==null)
	// {
	// //初始化圆形数据
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
	// //设置圆形顶点数据
	// ByteBuffer vbb = ByteBuffer.allocateDirect(circleVertices.length * 4);
	// vbb.order(ByteOrder.nativeOrder());
	// circleBuffer = vbb.asFloatBuffer();
	// circleBuffer.put(circleVertices);
	// circleBuffer.position(0);
	// }
	//
	// C2D_Graphics.glPushMatrix();
	// //矩阵变换
	// commonMatrix1.setToTranslate(x, y,0.0f);
	// commonMatrix2.setToScale(width/(circleR*2), height/(circleR*2),1.0f);
	// commonMatrix1.multiply(commonMatrix2);
	// C2D_Graphics.glLoadMatrixf(commonMatrix1.data, 0);
	// //开始会中圆形
	// C2D_Graphics.glEnableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
	// C2D_Graphics.glVertexPointer(2, C2D_Graphics.GL_FLOAT, 0, circleBuffer);
	// C2D_Graphics.glDrawArrays(mode, 0, CIRCLE_PointCount);
	// C2D_Graphics.glDisableClientState(C2D_Graphics.GL_VERTEX_ARRAY);
	//
	// C2D_Graphics.glPopMatrix();
	// }

	// 贴图绘制-----------------------------------------------------------------------
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
	 * 根据布局的坐标，尺寸，翻转，锚点等信息，计算最终呈现的矩形，并存放在resultRect中返回
	 * 
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param anchor
	 *            锚点
	 * @param trans
	 *            翻转
	 * @param resultRect
	 *            存放计算结果
	 * @return 是否成功取得
	 */
	public static boolean computeLayoutRect(float x, float y, float width, float height, int anchor, byte trans, C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		// 计算实际尺寸
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

	private static C2D_PointF pointFTemp = new C2D_PointF();

	/**
	 * 针对指定的矩形区域型应用锚点，之后返回其新的坐标点
	 * 
	 * @param rect
	 *            矩形区域
	 * @param anchor
	 *            锚点
	 * @return 其新的坐标点
	 */
	public static C2D_PointF applyAnchor(C2D_RectF rect, int anchor)
	{
		float x_offset_clip = 0;
		float y_offset_clip = 0;
		// 横向偏移
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
		// 纵向偏移
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
	 * 获取当前画布大小
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
		// 处理翻转
		int wReal = _w;
		int hReal = _h;
		if ((transFlag & TRANS_FOLD_RT2LB) != 0)
		{
			wReal = _h;
			hReal = _w;
		}
		int destXI = (int) x;
		int destYI = (int) y;
		// 设置可视区域
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
	 * 绘制矩形辅助参数
	 */
	private static float[] rectVertices = new float[12];// 三角形顶点缓冲
	private static FloatBuffer rectVerticesO;

	/**
	 * 绘制矩形
	 * 
	 * @param mode
	 *            填充模式
	 * @param x
	 *            矩形左上角X坐标
	 * @param y
	 *            矩形左上角Y坐标
	 * @param z
	 *            矩形深度Z坐标
	 * @param w
	 *            矩形宽度
	 * @param h
	 *            矩形高度
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
		// 开始绘制
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
	 * 绘制三角形辅助参数
	 */
	private static float[] trianVertices = new float[6];// 三角形顶点缓冲
	private static FloatBuffer triangleVerticesO;

	/**
	 * 绘制三角形
	 * 
	 * @param mode
	 *            填充模式
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
		// 开始绘制
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
	 * 绘制线段辅助参数
	 */
	private static FloatBuffer lineVerticesBuf = null;// 线段顶点缓冲
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
		// 开始绘制
		setTextureState(TexState_SHAPE);
		C2D_Graphics.glVertexPointer(2, C2D_Graphics.GL_FLOAT, 0, lineVerticesBuf);
		C2D_Graphics.glDrawArrays(C2D_Graphics.GL_LINE_LOOP, 0, 2);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * 贴图开关，如果打开则会开启顶点数组状态，贴图坐标数组状态，开启贴图
	 */
	public static int TexState_CLOSE = -1;// 表示关闭一切
	public static int TexState_TEXTURE = 0;// 表示绘制贴图
	public static int TexState_SHAPE = 1;// 表示绘制形状
	private static int texState_current = TexState_CLOSE;// 当前状态

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

	// FIME。。可以优化
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
