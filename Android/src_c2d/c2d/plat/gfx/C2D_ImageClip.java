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
 * ͼƬ�п���
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ImageClip extends C2D_Object
{
	protected float[] mVerticesBuffer = null; // ��������
	protected FloatBuffer mVerticesBufferO = null;// ��������
	protected int mVerticesBufferID = -1; // �������黺��ID
	protected float[] mTextureBuffer; // ��ͼ��������
	protected FloatBuffer mTextureBufferO; // ��ͼ�����������
	protected int mTextureBufferID = -1; // ��ͼ�������黺��ID
	// protected int mIndexBufferID=-1; // �������黺��ID
	protected int mNumOfIndices = -1; // �������
	private C2D_Image m_image;
	/** ͼƬ */
	/** ��ת��־ */
	protected byte m_transFlag = 0;

	/** ��������λ��ͼƬ�ϵľ�������ֻ�д��ھ��������ڵ����زŻᱻ��ʾ���� */
	private C2D_RectS m_contentRect = new C2D_RectS();
	/** ��ʾ��С */
	protected C2D_SizeF showSize = new C2D_SizeF();
	protected C2D_PointF anchor = new C2D_PointF(); // ê������
	// protected static Matrix4 matrixAnchorCommon=new Matrix4();//ͨ��ê�����
	// protected ShortBuffer mIndicesBuffer = null; // ������������

	/**
	 * ����һ���յ���ͼ��Ƭ
	 */
	public C2D_ImageClip()
	{
	}

	/**
	 * ʹ����ͼ������ͼ����һ����ͼ��Ƭ��ʹ��Ĭ����ͼ���꣬������ͼƬ��ʹ��Ĭ�ϳߴ�
	 * 
	 * @param image
	 */
	public C2D_ImageClip(C2D_Image image)
	{
		setImage(image);
	}

	/**
	 * ����һ��ͼƬ����Ƭ������Ƭ��Ĭ��ʹ��ͼƬ��������С����ʹ��(0,0,imgW,imgH)
	 * 
	 * @param fileName
	 *            ͼƬ�ļ����ƣ�λ����Դ�ļ��е�Ĭ��ͼƬĿ¼
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
	 * �鿴���п��Ƿ���Ч������δָ���κ���ͼ���߳ߴ�Ϊ0ʱ����ʧЧ
	 * 
	 * @return ���п��Ƿ���Ч
	 */
	public boolean isInvalid()
	{
		return (m_image == null || m_contentRect.m_width == 0 || m_contentRect.m_height == 0);
	}

	/**
	 * ����������ͼ����ָ���Ƿ��Զ��������ô�С
	 * 
	 * @param image
	 *            �µ���ͼ,�������null�����п齫ʧЧ
	 * @param resetSize
	 *            �Զ������趨��С
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
	 * ����������ͼ�����Զ��������ô�С
	 * 
	 * @param image
	 */
	public void setImage(C2D_Image image)
	{
		setImage(image, true);
	}

	/**
	 * ������ͼ�Ĳü����򣬼���Ƭ��ԭGDIλͼ�е�λ�úͳߴ� ע�⣺��Ƭ���������������ԭGDIλͼ��
	 * 
	 * @param rect
	 *            ��������
	 */
	public void setContentRect(C2D_RectS rect)
	{
		if (rect != null)
		{
			setContentRect(rect.m_x, rect.m_y, rect.m_width, rect.m_height);
		}
	}
	/**
	 * ������ͼ�Ĳü����򣬼���Ƭ��ԭGDIλͼ�е�λ�úͳߴ� ע�⣺��Ƭ���������������ԭGDIλͼ��
	 * 
	 * @param _cx
	 *            ���Ͻ�X����
	 * @param _cy
	 *            ���Ͻ�Y����
	 * @param _cw
	 *            ��Ƭ���
	 * @param _ch
	 *            ��Ƭ�߶�
	 * 
	 */
	public void setContentRect(int _cx, int _cy, int _cw, int _ch)
	{
		setContentRect(m_image,_cx, _cy, _cw, _ch);
	}
	/**
	 * ������ͼ�Ĳü����򣬼���Ƭ��ԭGDIλͼ�е�λ�úͳߴ� ע�⣺��Ƭ���������������ԭGDIλͼ��
	 * 
	 * @param image Ŀ��ͼƬ
	 * 
	 * @param _cx
	 *            ���Ͻ�X����
	 * @param _cy
	 *            ���Ͻ�Y����
	 * @param _cw
	 *            ��Ƭ���
	 * @param _ch
	 *            ��Ƭ�߶�
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
	 * ������������ߴ�
	 * 
	 * @param width
	 *            ����������
	 * @param height
	 *            ��������߶�
	 * @return ������
	 */
	public C2D_ImageClip setContentSize(int width, int height)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, width, height);
		return this;
	}

	/**
	 * ������������λ��
	 * 
	 * @param cX
	 *            ������������X
	 * @param cY
	 *            ������������Y
	 * @return ������
	 */
	public C2D_ImageClip setContentPos(int cX, int cY)
	{
		setContentRect(cX, cY, m_contentRect.m_width, m_contentRect.m_height);
		return this;
	}

	/**
	 * ������������߶�
	 * 
	 * @param cY
	 *            ������������X
	 * @param cH
	 *            ��������߶�H
	 * @return ������
	 */
	public C2D_ImageClip setContentYH(int cY, int cH)
	{
		setContentRect(m_contentRect.m_x, cY, m_contentRect.m_width, cH);
		return this;
	}

	/**
	 * �������������ȣ���������
	 * 
	 * @param cW
	 *            ����������W
	 * @return ��������
	 */
	public C2D_ImageClip setContentW(int cW)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, cW, m_contentRect.m_height);
		return this;
	}

	/**
	 * ������������߶ȣ���������
	 * 
	 * @param cH
	 *            ��������߶�H
	 * @return ��������
	 */
	public C2D_ImageClip setContentH(int cH)
	{
		setContentRect(m_contentRect.m_x, m_contentRect.m_y, m_contentRect.m_width, cH);
		return this;
	}

	/**
	 * ��ȡ�������������ⲿ��Ӧ���޸��������ֵ���������ã� ����setContentRect
	 * 
	 * @return C2D_RectS ��������
	 */
	public C2D_RectS getContentRect()
	{
		return m_contentRect;
	}

	/**
	 * ��ȡ��������x����
	 * 
	 * @return ��������x����
	 */
	public int getContentX()
	{
		return m_contentRect.m_x;
	}

	/**
	 * ��ȡ��������y����
	 * 
	 * @return ��������y����
	 */
	public int getContentY()
	{
		return m_contentRect.m_y;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return ����������
	 */
	public int getContentW()
	{
		return m_contentRect.m_width;
	}

	/**
	 * ��ȡ��������߶�
	 * 
	 * @return ��������߶�
	 */
	public int getContentH()
	{
		return m_contentRect.m_height;
	}

	/**
	 * ��ȡͼƬ
	 * 
	 * @return ͼƬ
	 */
	public C2D_Image getImage()
	{
		return m_image;
	}

	/**
	 * ������ʾ�ߴ磬����ͼ�ü������С�޹� ����ͼ�ü���������֮����ʾ����Ļ�ϵ����سߴ�
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
	 * ��ȡ��ʾ��С
	 * 
	 * @return ��ʾ��С
	 */
	public C2D_SizeF getShowSize()
	{
		sizeTemp.setValue(showSize);
		return sizeTemp;
	}

	/**
	 * ������ͼ�ü������ҽ���ʾ��С����Ϊ�ü����Ĵ�С��
	 * �൱�ڵ���setTexureRegion���Բü�ȥ�ߴ�_cw��_chΪ��������setShowSize
	 * 
	 * @param _cx
	 *            ���Ͻ�X����
	 * @param _cy
	 *            ���Ͻ�Y����
	 * @param _cw
	 *            ��Ƭ���
	 * @param _ch
	 *            ��Ƭ�߶�
	 */
	public void setContentRectAndSize(int _cx, int _cy, int _cw, int _ch)
	{
		setContentRect(_cx, _cy, _cw, _ch);
		setShowSize(_cw, _ch);
	}

	/**
	 * /** ������ͼ�ü������ҽ���ʾ��С����Ϊ�ü����Ĵ�С��
	 * �൱�ڵ���setTexureRegion���Բü�ȥ�ߴ�_cw��_chΪ��������setShowSize
	 * 
	 * @param rect
	 *            ��������
	 */
	public void setContentRectAndSize(C2D_RectS rect)
	{
		if (rect != null)
		{
			setContentRectAndSize(rect.m_x, rect.m_y, rect.m_width, rect.m_height);
		}
	}

	/**
	 * ���÷�ת��־
	 * 
	 * @param transform
	 *            ��ת��־
	 */
	public void setTransform(byte transform)
	{
		m_transFlag = transform;
	}

	/**
	 * ��ȡ��ת��־
	 * 
	 * @return ��ת��־
	 */
	public byte getTransform()
	{
		return m_transFlag;
	}

	/**
	 * ����ê��(0~1֮�����ֵ[0,0�������½�,1,1�������Ͻ�])
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
	 * ��ȡê��
	 * 
	 * @return ê��
	 */
	public C2D_PointF getAnchor()
	{
		return anchor;
	}

	/**
	 * ������ͼ����
	 * 
	 * @param g
	 *            ��ͼ���
	 * @param x
	 *            Ŀ��x����
	 * @param y
	 *            Ŀ��y����
	 */
	public void draw(float x, float y)
	{
		draw(m_image, x, y, 0, 0);
	}

	/**
	 * ������ͼ����
	 * 
	 * @param x
	 *            Ŀ��x����
	 * @param y
	 *            Ŀ��y����
	 * @param transFlagP
	 *            ��ת���
	 */
	public void draw(float x, float y, int transFlagP)
	{
		draw(m_image,x, y, transFlagP, 0);
	}
	/**
	 * ������ͼ����
	 * 
	 * @param x
	 *            Ŀ��x����
	 * @param y
	 *            Ŀ��y����
	 * @param transFlagP
	 *            ��ת���
	 * @param color
	 *            ������ɫ
	 */
	public void draw(float x, float y, int transFlagP, int color)
	{
		draw(m_image, x, y, transFlagP, color);
	}
	/**
	 * ������ͼ����
	 * 
	 * @param image
	 *            Ŀ��ͼƬ
	 * @param x
	 *            Ŀ��x����
	 * @param y
	 *            Ŀ��y����
	 * @param transFlagP
	 *            ��ת���
	 * @param color
	 *            ������ɫ
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
	 * ���л�����״̬��ʹ��ָ���ı任���������ͼ�п飬��ʹ��ָ����͸���� ����ʱ�����еı�������
	 * 
	 * @param zoomLevel
	 *            ���ű���
	 * @param xS
	 *            Ŀ���x����
	 * @param yS
	 *            Ŀ���y����
	 * @param transform
	 *            ����
	 * @param parentAlpha
	 *            ��������͸����
	 * @param degreeRotate
	 *            ��ת�Ƕ�
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
	 * ���ݴ���ı�����ֵ������ָ��������OpenGL���ξ���
	 * 
	 * @param value
	 *            ������ֵ
	 */
	protected void genMatrixGL(C2D_ValueTransform value)
	{

	}

	/**
	 * ���ɶ�������
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
	 * ���ɶ�������
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
	 * ������ͼ����
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
