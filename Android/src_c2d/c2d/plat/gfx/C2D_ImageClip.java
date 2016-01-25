package c2d.plat.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.math.type.C2D_SizeF;
import c2d.lang.obj.C2D_Object;
import c2d.mod.sprite.tween.model.C2D_ValueTransform;

/**
 * 图片切块类
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ImageClip extends C2D_Object
{
	protected float[] mVerticesBuffer = null; // 顶点数组
	protected FloatBuffer mVerticesBufferO = null;// 顶点数组
	protected int mVerticesBufferID = -1; // 顶点数组缓冲ID
	protected float[] mTextureBuffer; // 贴图坐标数组
	protected FloatBuffer mTextureBufferO; // 贴图坐标数组对象
	protected int mTextureBufferID = -1; // 贴图坐标数组缓冲ID
	// protected int mIndexBufferID=-1; // 索引数组缓冲ID
	protected int mNumOfIndices = -1; // 顶点个数
	private C2D_Image m_image;
	/** 图片 */
	/** 翻转标志 */
	protected byte m_transFlag = 0;

	/** 内容区域，位于图片上的矩形区域，只有存在矩形区域内的像素才会被显示出来 */
	private C2D_RectS m_contentRect = new C2D_RectS();
	/** 显示大小 */
	protected C2D_SizeF showSize = new C2D_SizeF();
	protected C2D_PointF anchor = new C2D_PointF(); // 锚点坐标
	// protected static Matrix4 matrixAnchorCommon=new Matrix4();//通用锚点矩阵
	// protected ShortBuffer mIndicesBuffer = null; // 顶点索引数组

	/**
	 * 建立一个空的贴图切片
	 */
	public C2D_ImageClip()
	{
	}

	/**
	 * 使用贴图已有贴图建立一个贴图切片，使用默认贴图坐标，即整张图片，使用默认尺寸
	 * 
	 * @param image
	 */
	public C2D_ImageClip(C2D_Image image)
	{
		setImage(image);
	}

	/**
	 * 创建一张图片的切片对象，切片将默认使用图片的完整大小，即使用(0,0,imgW,imgH)
	 * 
	 * @param fileName
	 *            图片文件名称，位于资源文件夹的默认图片目录
	 */
	public C2D_ImageClip(String fileName)
	{
		C2D_Image image = C2D_Image.createImage(fileName);
		if (image != null)
		{
			setImage(image);
			image.transHadler(this);
		}
	}

	/**
	 * 查看此切块是否无效，当它未指向任何贴图或者尺寸为0时，即失效
	 * 
	 * @return 此切块是否无效
	 */
	public boolean isInvalid()
	{
		return (m_image == null || m_contentRect.m_width == 0 || m_contentRect.m_height == 0);
	}

	/**
	 * 重新设置贴图，并指定是否自动重新设置大小
	 * 
	 * @param image
	 *            新的贴图,如果传入null，则本切块将失效
	 * @param resetSize
	 *            自动重新设定大小
	 */
	public void setImage(C2D_Image image, boolean resetSize)
	{
		if (m_image != null)
		{
			m_image.doRelease(this);
		}
		m_image = image;
		if (m_image != null)
		{
			if (resetSize)
			{
				int _cw = m_image.bitmapWidth();
				int _ch = m_image.bitmapHeight();
				setShowSize((float) _cw, (float) _ch);
				setContentRect(0, 0, _cw, _ch);
			}
		}
	}

	/**
	 * 重新设置贴图，会自动重新设置大小
	 * 
	 * @param image
	 */
	public void setImage(C2D_Image image)
	{
		setImage(image, true);
	}

	/**
	 * 设置贴图的裁剪区域，即切片在原GDI位图中的位置和尺寸 注意：切片区域设置是相对于原GDI位图的
	 * 
	 * @param rect
	 *            内容区域
	 */
	public void setContentRect(C2D_RectS rect)
	{
		if (rect != null)
		{
			setContentRect(rect.m_x, rect.m_y, rect.m_width, rect.m_height);
		}
	}
	/**
	 * 设置贴图的裁剪区域，即切片在原GDI位图中的位置和尺寸 注意：切片区域设置是相对于原GDI位图的
	 * 
	 * @param _cx
	 *            左上角X坐标
	 * @param _cy
	 *            左上角Y坐标
	 * @param _cw
	 *            切片宽度
	 * @param _ch
	 *            切片高度
	 * 
	 */
	public void setContentRect(int _cx, int _cy, int _cw, int _ch)
	{
		setContentRect(m_image,_cx, _cy, _cw, _ch);
	}
	/**
	 * 设置贴图的裁剪区域，即切片在原GDI位图中的位置和尺寸 注意：切片区域设置是相对于原GDI位图的
	 * 
	 * @param image 目标图片
	 * 
	 * @param _cx
	 *            左上角X坐标
	 * @param _cy
	 *            左上角Y坐标
	 * @param _cw
	 *            切片宽度
	 * @param _ch
	 *            切片高度
	 * 
	 */
	public void setContentRect(C2D_Image image,int _cx, int _cy, int _cw, int _ch)
	{
		if (image == null)
		{
			return;
		}
		m_contentRect.m_x = (short) _cx;
		m_contentRect.m_y = (short) _cy;
		m_contentRect.m_width = (short) _cw;
		m_contentRect.m_height = (short) _ch;
		float _tw = image.textureWidth() / image.getZoomOut();
		float _th = image.textureHight() / image.getZoomOut();
		float ltX = _cx / _tw;
		float ltY = _cy / _th;
		float rbX = (_cx + _cw) / _tw;
		float rbY = (_cy + _ch) / _th;
		float textureCoordinates[] =
		{ ltX, ltY, rbX, ltY, ltX, rbY, rbX, rbY };
		setTextureCoordinates(textureCoordinates);
	}

	/**
	 * 设置内容区域尺寸
	 * 
	 * @param width
	 *            内容区域宽度
	 * @param height
	 *            内容区域高度
	 * @return 本对象
	 */
	public C2D_ImageClip setContentSize(int width, int height)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, width, height);
		return this;
	}

	/**
	 * 设置内容区域位置
	 * 
	 * @param cX
	 *            内容区域坐标X
	 * @param cY
	 *            内容区域坐标Y
	 * @return 本对象
	 */
	public C2D_ImageClip setContentPos(int cX, int cY)
	{
		setContentRect(cX, cY, m_contentRect.m_width, m_contentRect.m_height);
		return this;
	}

	/**
	 * 设置内容区域高度
	 * 
	 * @param cY
	 *            内容区域坐标X
	 * @param cH
	 *            内容区域高度H
	 * @return 本对象
	 */
	public C2D_ImageClip setContentYH(int cY, int cH)
	{
		setContentRect(m_contentRect.m_x, cY, m_contentRect.m_width, cH);
		return this;
	}

	/**
	 * 设置内容区域宽度，返回自身
	 * 
	 * @param cW
	 *            内容区域宽度W
	 * @return 返回自身
	 */
	public C2D_ImageClip setContentW(int cW)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, cW, m_contentRect.m_height);
		return this;
	}

	/**
	 * 设置内容区域高度，返回自身
	 * 
	 * @param cH
	 *            内容区域高度H
	 * @return 返回自身
	 */
	public C2D_ImageClip setContentH(int cH)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, m_contentRect.m_width, cH);
		return this;
	}

	/**
	 * 获取内容区域，你在外部不应该修改这个返回值，如需设置， 请用setContentRect
	 * 
	 * @return C2D_RectS 内容区域
	 */
	public C2D_RectS getContentRect()
	{
		return m_contentRect;
	}

	/**
	 * 获取内容区域x坐标
	 * 
	 * @return 内容区域x坐标
	 */
	public int getContentX()
	{
		return m_contentRect.m_x;
	}

	/**
	 * 获取内容区域y坐标
	 * 
	 * @return 内容区域y坐标
	 */
	public int getContentY()
	{
		return m_contentRect.m_y;
	}

	/**
	 * 获取内容区域宽度
	 * 
	 * @return 内容区域宽度
	 */
	public int getContentW()
	{
		return m_contentRect.m_width;
	}

	/**
	 * 获取内容区域高度
	 * 
	 * @return 内容区域高度
	 */
	public int getContentH()
	{
		return m_contentRect.m_height;
	}

	/**
	 * 获取图片
	 * 
	 * @return 图片
	 */
	public C2D_Image getImage()
	{
		return m_image;
	}

	/**
	 * 设置显示尺寸，跟贴图裁剪区域大小无关 是贴图裁剪区域被缩放之后，显示在屏幕上的像素尺寸
	 * 
	 * @param width
	 * @param height
	 */
	public void setShowSize(float width, float height)
	{
		showSize.setValue(width, height);
		float _w = showSize.m_width;
		float _h = showSize.m_height;
		float _z = 0.0f;
		float[] vertices = new float[]
		{ 0, 0, _z, _w, 0, _z, 0, _h, _z, _w, _h, _z };
		setVertices(vertices);
	}
	private static final C2D_SizeF sizeTemp = new C2D_SizeF();

	/**
	 * 获取显示大小
	 * 
	 * @return 显示大小
	 */
	public C2D_SizeF getShowSize()
	{
		sizeTemp.setValue(showSize);
		return sizeTemp;
	}

	/**
	 * 设置贴图裁剪区域并且将显示大小调整为裁剪区的大小，
	 * 相当于调用setTexureRegion和以裁剪去尺寸_cw、_ch为参数调用setShowSize
	 * 
	 * @param _cx
	 *            左上角X坐标
	 * @param _cy
	 *            左上角Y坐标
	 * @param _cw
	 *            切片宽度
	 * @param _ch
	 *            切片高度
	 */
	public void setContentRectAndSize(int _cx, int _cy, int _cw, int _ch)
	{
		setContentRect(_cx, _cy, _cw, _ch);
		setShowSize(_cw, _ch);
	}

	/**
	 * /** 设置贴图裁剪区域并且将显示大小调整为裁剪区的大小，
	 * 相当于调用setTexureRegion和以裁剪去尺寸_cw、_ch为参数调用setShowSize
	 * 
	 * @param rect
	 *            内容区域
	 */
	public void setContentRectAndSize(C2D_RectS rect)
	{
		if (rect != null)
		{
			setContentRectAndSize(rect.m_x, rect.m_y, rect.m_width, rect.m_height);
		}
	}

	/**
	 * 设置翻转标志
	 * 
	 * @param transform
	 *            翻转标志
	 */
	public void setTransform(byte transform)
	{
		m_transFlag = transform;
	}

	/**
	 * 获取翻转标志
	 * 
	 * @return 翻转标志
	 */
	public byte getTransform()
	{
		return m_transFlag;
	}

	/**
	 * 设置锚点(0~1之间的数值[0,0代表左下角,1,1代表右上角])
	 * 
	 * @param anchorX
	 * @param anchorY
	 */
	public void setAnchor(float anchorX, float anchorY)
	{
		anchor.m_x = anchorX;
		anchor.m_y = anchorY;
	}

	/**
	 * 获取锚点
	 * 
	 * @return 锚点
	 */
	public C2D_PointF getAnchor()
	{
		return anchor;
	}

	/**
	 * 绘制贴图区域
	 * 
	 * @param g
	 *            绘图句柄
	 * @param x
	 *            目标x坐标
	 * @param y
	 *            目标y坐标
	 */
	public void draw(float x, float y)
	{
		draw(m_image, x, y, 0, 0);
	}

	/**
	 * 绘制贴图区域
	 * 
	 * @param x
	 *            目标x坐标
	 * @param y
	 *            目标y坐标
	 * @param transFlagP
	 *            翻转标记
	 */
	public void draw(float x, float y, int transFlagP)
	{
		draw(m_image,x, y, transFlagP, 0);
	}
	/**
	 * 绘制贴图区域
	 * 
	 * @param x
	 *            目标x坐标
	 * @param y
	 *            目标y坐标
	 * @param transFlagP
	 *            翻转标记
	 * @param color
	 *            过滤颜色
	 */
	public void draw(float x, float y, int transFlagP, int color)
	{
		draw(m_image, x, y, transFlagP, color);
	}
	/**
	 * 绘制贴图区域
	 * 
	 * @param image
	 *            目标图片
	 * @param x
	 *            目标x坐标
	 * @param y
	 *            目标y坐标
	 * @param transFlagP
	 *            翻转标记
	 * @param color
	 *            过滤颜色
	 */
	public void draw(C2D_Image image,float x, float y, int transFlagP, int color)
	{
		if(image==null)
		{
			return;
		}
		C2D_Graphics.setTextureState(C2D_Graphics.TexState_TEXTURE);
		if (color == 0)
		{
			C2D_Graphics.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
		else
		{
			float r = ((color >> 16) & 0xFF) / 255.0f;
			float g = ((color >> 8) & 0xFF) / 255.0f;
			float b = ((color >> 0) & 0xFF) / 255.0f;
			float a = ((color >> 24) & 0xFF) / 255.0f;
			C2D_Graphics.glColor4f(r, g, b, a);
		}
		C2D_Graphics.glPushMatrix();
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mVerticesBufferID);
		C2D_Graphics.glVertexPointer(3, C2D_Graphics.GL_FLOAT, 0, 0);
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mTextureBufferID);
		C2D_Graphics.glTexCoordPointer(2, C2D_Graphics.GL_FLOAT, 0, 0);
		C2D_Graphics.glBindTexture(C2D_Graphics.GL_TEXTURE_2D, image.mTexuteID);
		C2D_Graphics.glTranslatef(-anchor.m_x * showSize.m_width + x, -anchor.m_y * showSize.m_height + y, 0);
		int transFlagCom = C2D_GdiGraphics.getMixedTrans((byte) m_transFlag, (byte) transFlagP);
		if (transFlagCom != 0 || transFlagP != 0 || m_transFlag != 0)
		{
			// C2D_MiscUtil.log("--");
		}
		switch (transFlagCom)
		{
		case C2D_GdiGraphics.TRANS_NONE:
			break;
		case C2D_GdiGraphics.TRANS_VERTICAL:
			C2D_Graphics.glTranslatef(0, showSize.m_height, 0);
			C2D_Graphics.glScalef(1.0f, -1.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_HORIZENTAL:
			C2D_Graphics.glTranslatef(showSize.m_width, 0, 0);
			C2D_Graphics.glScalef(-1.0f, 1.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_ROTATE180:
			C2D_Graphics.glTranslatef(showSize.m_width, showSize.m_height, 0);
			C2D_Graphics.glScalef(-1.0f, -1.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_FOLD_RT2LB:
			C2D_Graphics.glTranslatef(0, showSize.m_height, 0);
			C2D_Graphics.glRotatef(-90, 0.0f, 0.0f, 1.0f);
			C2D_Graphics.glTranslatef(showSize.m_width, 0, 0);
			C2D_Graphics.glScalef(-1.0f, 1.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_ROTATE90:
			C2D_Graphics.glTranslatef(showSize.m_height, 0, 0);
			C2D_Graphics.glRotatef(90, 0.0f, 0.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_ROTATE270:
			C2D_Graphics.glTranslatef(0, showSize.m_width, 0);
			C2D_Graphics.glRotatef(270, 0.0f, 0.0f, 1.0f);
			break;
		case C2D_GdiGraphics.TRANS_FOLD_LT2RB:
			C2D_Graphics.glTranslatef(0, showSize.m_width, 0);
			C2D_Graphics.glRotatef(-90, 0.0f, 0.0f, 1.0f);
			C2D_Graphics.glTranslatef(0, showSize.m_height, 0);
			C2D_Graphics.glScalef(1.0f, -1.0f, 1.0f);
			break;
		}
		C2D_Graphics.glDrawArrays(C2D_Graphics.GL_TRIANGLE_STRIP, 0, 4);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * 不切换部分状态，使用指定的变换矩阵绘制贴图切块，并使用指定的透明度 用于时间轴中的遍历绘制
	 * 
	 * @param zoomLevel
	 *            缩放比率
	 * @param xS
	 *            目标点x坐标
	 * @param yS
	 *            目标点y坐标
	 * @param transform
	 *            变形
	 * @param parentAlpha
	 *            父容器的透明度
	 * @param degreeRotate
	 *            旋转角度
	 */
	public void drawWithTransform(float zoomLevel, float xS, float yS, C2D_ValueTransform transform, float parentAlpha, float degreeRotate)
	{
		// draw(xS,yS,0);
		C2D_Graphics.setTextureState(C2D_Graphics.TexState_TEXTURE);
		C2D_Graphics.glPushMatrix();
		float alpha = parentAlpha * transform.alpha;
		C2D_Graphics.glColor4f(1.0f, 1.0f, 1.0f, alpha);
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mVerticesBufferID);
		C2D_Graphics.glVertexPointer(3, C2D_Graphics.GL_FLOAT, 0, 0);
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mTextureBufferID);
		C2D_Graphics.glTexCoordPointer(2, C2D_Graphics.GL_FLOAT, 0, 0);
		C2D_Graphics.glBindTexture(C2D_Graphics.GL_TEXTURE_2D, m_image.mTexuteID);
		float tx = xS + transform.pos.m_x * zoomLevel;
		float ty = yS + transform.pos.m_y * zoomLevel;

		if (tx != 0 || ty != 0)
		{
			C2D_Graphics.glTranslatef(tx, ty, 0.0f);
		}
		float degree = degreeRotate + transform.rotateDegree;
		if (degree % 360 != 0)
		{
			C2D_Graphics.glRotatef(degree, 0, 0, 1);
		}
		float zoomX = zoomLevel * transform.scale.m_x;
		float zoomY = zoomLevel * transform.scale.m_y;
		if (zoomX != 1 || zoomY != 1)
		{
			C2D_Graphics.glScalef(zoomX, zoomY, 1.0f);
		}
		C2D_Graphics.glTranslatef(-showSize.m_width * (transform.anchor.m_x + anchor.m_x), -showSize.m_height * (transform.anchor.m_y + anchor.m_y), 0);
		C2D_Graphics.glDrawArrays(C2D_Graphics.GL_TRIANGLE_STRIP, 0, 4);
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * 根据传入的变形数值，生成指定参数的OpenGL变形矩阵
	 * 
	 * @param value
	 *            变形数值
	 */
	protected void genMatrixGL(C2D_ValueTransform value)
	{

	}

	/**
	 * 生成顶点坐标
	 * 
	 * @param vertices
	 */
	public void setVertices(float[] vertices)
	{
		mVerticesBuffer = vertices;
		if (mVerticesBufferO == null)
		{
			mVerticesBufferO = FloatBuffer.allocate(vertices.length);
			mVerticesBufferO.order();
		}
		else
		{
			mVerticesBufferO.clear();
		}
		mVerticesBufferO.put(mVerticesBuffer);
		mVerticesBufferO.position(0);
		if (mVerticesBufferID < 0)
		{
			IntBuffer buf = IntBuffer.allocate(1);
			C2D_Graphics.glGenBuffers(1, buf);
			mVerticesBufferID = buf.array()[0];
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mVerticesBufferID);
			C2D_Graphics.glBufferData(C2D_Graphics.GL_ARRAY_BUFFER, mVerticesBuffer.length * 4, mVerticesBufferO, C2D_Graphics.GL_STATIC_DRAW);
		}
		else
		{
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mVerticesBufferID);
			C2D_Graphics.glBufferSubData(C2D_Graphics.GL_ARRAY_BUFFER, 0, mVerticesBuffer.length * 4, mVerticesBufferO);
		}
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * 生成顶点索引
	 * 
	 * @param indices
	 */
	// protected void setIndices(short[] indices)
	// {
	// if (mIndicesBuffer == null || mIndicesBuffer.capacity() != indices.length
	// * 2)
	// {
	// ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
	// ibb.order(ByteOrder.nativeOrder());
	// mIndicesBuffer = ibb.asShortBuffer();
	// }
	// else
	// {
	// mIndicesBuffer.clear();
	// }
	// mIndicesBuffer.put(indices);
	// mIndicesBuffer.position(0);
	// mNumOfIndices = indices.length;
	// }

	/**
	 * 生成贴图坐标
	 * 
	 * @param textureCoords
	 */
	private void setTextureCoordinates(float[] mTextureBufferT)
	{
		mTextureBuffer = mTextureBufferT;
		if (mTextureBufferO == null || mTextureBufferO.capacity() != mTextureBufferT.length)
		{
			ByteBuffer ibb = ByteBuffer.allocateDirect(mTextureBufferT.length * 4);
			ibb.order(ByteOrder.nativeOrder());
			mTextureBufferO = ibb.asFloatBuffer();
		}
		for (int i = 0; i < mTextureBuffer.length; i++)
		{
			mTextureBufferO.put(mTextureBuffer[i]);
		}
		mTextureBufferO.position(0);
		if (mTextureBufferID < 0)
		{
			IntBuffer buf = IntBuffer.allocate(1);
			C2D_Graphics.glGenBuffers(1, buf);
			mTextureBufferID = buf.array()[0];
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mTextureBufferID);
			C2D_Graphics.glBufferData(C2D_Graphics.GL_ARRAY_BUFFER, mTextureBuffer.length * 4, mTextureBufferO, C2D_Graphics.GL_STATIC_DRAW);
		}
		else
		{
			C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, mTextureBufferID);
			C2D_Graphics.glBufferSubData(C2D_Graphics.GL_ARRAY_BUFFER, 0, mTextureBuffer.length * 4, mTextureBufferO);
		}
		C2D_Graphics.glBindBuffer(C2D_Graphics.GL_ARRAY_BUFFER, 0);
	}

	public void onRelease()
	{
		if (m_image != null)
		{
			m_image.doRelease(this);
			m_image = null;
		}
		if (mVerticesBufferID > 0)
		{
			IntBuffer intBuf = IntBuffer.allocate(1);
			intBuf.order();
			intBuf.put(mVerticesBufferID);
			intBuf.position(0);
			C2D_Graphics.glDeleteBuffers(1, intBuf);
			mVerticesBufferID = -1;
		}
		if (mTextureBufferID > 0)
		{
			IntBuffer intBuf = IntBuffer.allocate(1);
			intBuf.order();
			intBuf.put(mTextureBufferID);
			intBuf.position(0);
			C2D_Graphics.glDeleteBuffers(1, intBuf);
			mTextureBufferID = -1;
		}
		mVerticesBuffer = null;
		mVerticesBufferO = null;
		mTextureBuffer = null;
		mTextureBufferO = null;
		showSize = null;
		anchor = null;
		m_contentRect = null;
	}

	public C2D_ImageClip cloneSelf()
	{
		C2D_ImageClip newInstance = new C2D_ImageClip();
		newInstance.m_transFlag = m_transFlag;
		newInstance.m_image = m_image;
		if (m_contentRect != null)
		{
			newInstance.m_contentRect = m_contentRect.cloneSelf();
		}
		return newInstance;
	}
}
