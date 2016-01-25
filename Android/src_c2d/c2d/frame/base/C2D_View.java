package c2d.frame.base;

import c2d.frame.com.list.C2D_ListStyle;
import c2d.lang.math.C2D_SortableArray;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_PointI;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_SizeI;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * ��ͼ�࣬��ͼ������ĳ���豸����Ļ�����ڴ滻��ͼ���ϵ�һ�鲻͸�������� ������ӵ���Լ��ı���ɫ������ͼƬ���Լ���ͼ�ڵĶ������ͼ���߿ؼ�����ͼ
 * �Ĳ����ǣ��Ȼ��Ʊ���ɫ������Ʊ���ͼƬ���ٻ�������ͼ���ӿؼ��������ǵ� ����˳����������z��Ƚ�������
 * ��ͼ���������򽫻�������������ͳߴ��Զ�������¡�
 * 
 * @author AndrewFan
 * 
 */
public class C2D_View extends C2D_Widget
{
	/** �����б� */
	public C2D_SortableArray m_nodeList = new C2D_SortableArray();
	/** ��ͼ���. */
	protected float m_width;
	/** ��ͼ�߶�. */
	protected float m_height;
	/** ����ɫ����������ã���û�б���ɫ **/
	protected C2D_Color m_bgColor;
	/** ����ͼƬ��Ƭ����������ã���û�б���ͼƬ **/
	protected C2D_ImageClip m_bgImgClip;
	/** ����ͼƬ����������ã���û�б���ͼƬ **/
	private C2D_Image m_bgImg;
	/** ����ͼƬ������ */
	protected C2D_PointI m_bgImgPos = new C2D_PointI();
	/** ���ý���ͼƬ���ӽڵ���·���ʾ */
	protected boolean m_focusImgBack = false;
	/** ����ͼƬ��ê�� */
	protected int m_bgImgAnchor;
	/**
	 * �Ƿ�Χס���㣬�����ǰ������ĳ���ڵ�ӵ�н��㣬�Զ��л�����ʱ���޷��뿪��ǰ��ͼ
	 */
	protected boolean m_besiege;

	/**
	 * Ĭ��ʹ�þֲ��ػ���ͼ���͹�����ͼ
	 */
	public C2D_View()
	{
	}

	/**
	 * ����µ��ӽڵ㣬ʹ���ӽڵ����е�z�����������ӵ��ӽڵ�����ͼ�� Ĭ�ϻὫ����ͼ�Ĵ�С�Զ����óɵ�ǰ��ͼ�Ĵ�С��
	 * ������Ҫע����ӵ�˳�򣬾��������Զ����µ�˳����ӣ�����������ȷ��λ�ã��ռ䣬�������Ϣ��
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @return �����Ƿ����ɹ�
	 */
	public boolean addChild(C2D_Widget child)
	{
		if (child == null)
		{
			return false;
		}
		if (child instanceof C2D_View)
		{
			C2D_View c = (C2D_View) child;
			if (c.getWidth() == 0 || c.getHeight() == 0)
			{
				((C2D_View) (child)).setSize(m_width, m_height);
			}
		}
		m_nodeList.addElement(child);
		child.m_parentNode = this;
		m_needOrder = true;
		child.layoutChanged();
		child.accountScene();
		child.accountStage();
		return true;
	}

	/**
	 * ��ȡ���
	 * 
	 * @return �ؼ����
	 */
	public float getWidth()
	{
		return m_width;
	}

	/**
	 * ��ȡ�ؼ��ĸ߶�
	 * 
	 * @return �߶�
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * �жϵ�ǰ��ͼ�Ƿ����ָ���Ŀؼ����������Լ�
	 * 
	 * @param widget
	 *            ָ���Ŀؼ�
	 * @return �Ƿ����
	 */
	public boolean contains(C2D_Widget widget)
	{
		if (widget == null)
		{
			return false;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget child = (C2D_Widget) m_nodeList.elementAt(i);
			if (widget.equals(child))
			{
				return true;
			}
			if (child instanceof C2D_View)
			{
				if (((C2D_View) child).contains(widget))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * �жϵ�ǰ�����Ľ���ؼ��Ƿ��ڱ���ͼ���ӽڵ��У������Լ���
	 * 
	 * @return �Ƿ������ǰ���ؼ�
	 */
	public boolean containsFocus()
	{
		C2D_Scene scene = getSceneAt();
		if (scene == null)
		{
			return false;
		}
		C2D_Widget focus = scene.getFoucusedWidget();
		if (focus == null)
		{
			return false;
		}
		if (focus.equals(this) || contains(focus))
		{
			return true;
		}
		return false;
	}

	/**
	 * ���ô�С���������߶�
	 * 
	 * @param width
	 *            �����õĿ��
	 * @param height
	 *            �����õĸ߶ȶ�
	 * @return ��������
	 */
	public C2D_View setSize(float width, float height)
	{
		if (width > 0 && height > 0 && (width != m_width || height != m_height))
		{
			m_width = width;
			m_height = height;
			layoutChanged();
			setUpdatePos();
		}
		refreshHotRegion(true);
		return this;
	}

	/**
	 * ���ô�С���������߶�
	 * 
	 * @param size
	 *            �����õĳߴ�
	 * @return �����Ƿ�ɹ�����
	 */
	public C2D_View setSize(C2D_SizeI size)
	{
		if (size == null)
		{
			return null;
		}
		return setSize(size.m_width, size.m_height);
	}

	private static final C2D_Color readOnlyColor = new C2D_Color();

	/**
	 * ��ȡ������ɫ��ע���ȡ�Ķ����ǹ������󣬶����޸Ĳ������ͼ����Ӱ�졣 �����Ҫ�޸���ͼ�ı���ɫ��Ӧ��ʹ��setBGColor
	 * 
	 * @return ������ɫ
	 */
	public C2D_Color getBGColor()
	{
		if (m_bgColor != null)
		{
			readOnlyColor.setColor(m_bgColor);
			return readOnlyColor;
		}
		return null;
	}

	/**
	 * ���ñ�����ɫ�����Դ���null����ʾû�б�����ɫ
	 * 
	 * @param bgColor
	 *            ������ɫ
	 */
	public void setBGColor(C2D_Color bgColor)
	{
		if ((m_bgColor == bgColor) || (m_bgColor != null && m_bgColor.equals(bgColor)))
		{
			return;
		}
		m_bgColor = bgColor;
		layoutChanged();
	}

	/**
	 * ���ñ�����ɫ
	 * 
	 * @param bgColor
	 *            ������ɫ
	 */
	public void setBGColor(int bgColor)
	{
		setBGColor(new C2D_Color(bgColor));
	}

	/**
	 * ��ȡ����ͼƬ
	 * 
	 * @return ����ͼƬ
	 */
	public C2D_ImageClip getBGImage()
	{
		return m_bgImgClip;
	}

	/**
	 * ���ñ���ͼƬ��Ƭ�����Դ���null������û�б���ͼƬ��Ĭ��ת�Ʋ�������Ȩ
	 * 
	 * @param bgImageClip
	 *            ����ͼƬ��Ƭ
	 */
	public void setBGImage(C2D_ImageClip bgImageClip)
	{
		setBGImage(bgImageClip, true);
	}

	/**
	 * ���ñ���ͼƬ��Ƭ�����Դ���null������û�б���ͼƬ
	 * 
	 * @param bgImageClip
	 *            ����ͼƬ
	 * @param changeOwner
	 *            �Ƿ�ת�ƹ�bgImageClip��Ȩ
	 */
	public void setBGImage(C2D_ImageClip bgImageClip, boolean changeOwner)
	{
		releaseImageClip();
		releaseBgImage();
		if (bgImageClip != null)
		{
			m_bgImgClip = bgImageClip;
			if (changeOwner)
			{
				m_bgImgClip.transHadler(this);
			}
		}
	}

	/**
	 * ���ñ���ͼƬ�����Դ���null������û�б���ͼƬ
	 * 
	 * @param bgImage
	 *            ����ͼƬ
	 */
	public C2D_View setBGImage(C2D_Image bgImage)
	{
		releaseBgImage();
		releaseImageClip();
		if (bgImage != null)
		{
			m_bgImg = bgImage;
			m_bgImgClip = new C2D_ImageClip(m_bgImg);
			m_bgImgClip.transHadler(this);
		}
		layoutChanged();
		return this;
	}

	/**
	 * ���ñ���ͼƬ�����Դ���null������û�б���ͼƬ
	 * 
	 * @param imgName
	 *            ����ͼƬ���ƣ�Ĭ��λ��imgs_otherĿ¼
	 */
	public void setBGImage(String imgName)
	{
		setBGImage(C2D_Image.createImage(imgName));
	}

	/**
	 * �����б����ͼƬ�����Դ���null������û�б���ͼƬ
	 * 
	 * @param bgClip
	 *            ����ͼƬ��Ƭ
	 * @param x
	 *            �����ǰ�ؼ���x����
	 * @param y
	 *            �����ǰ�ؼ���y����
	 * @param anchor
	 *            ê��
	 */
	public void setBGImage(C2D_ImageClip bgClip, int x, int y, int anchor)
	{
		if (!(m_bgImgClip != null && m_bgImgClip == bgClip))
		{
			releaseImageClip();
		}
		m_bgImgClip = bgClip;
		m_bgImgPos.setValue(x, y);
		m_bgImgAnchor = anchor;
		layoutChanged();
	}

	/**
	 * �����б����ͼƬ�����Դ���null������û�б���ͼƬ
	 * 
	 * @param bgClip
	 *            ����ͼƬ��Ƭ
	 * @param x
	 *            �����ǰ�ؼ���x����
	 * @param y
	 *            �����ǰ�ؼ���y����
	 */
	public void setBGImage(C2D_ImageClip bgClip, int x, int y)
	{
		setBGImage(bgClip, x, y, 0);
	}

	/**
	 * ���ñ���ͼƬ�������
	 * 
	 * @param x
	 *            ���x������
	 * @param y
	 *            ���Y������
	 */
	public void setBGImagePos(int x, int y)
	{
		m_bgImgPos.setValue(x, y);
	}

	/**
	 * ���ø�����ͼƬ��ͬ�Ĵ�С���������ͼƬ���ڵĻ�
	 */
	public void setSizeOfBGImg()
	{
		if (m_bgImgClip != null)
		{
			this.setSize(m_bgImgClip.getContentW(), m_bgImgClip.getContentH());
			layoutChanged();
		}
	}

	/**
	 * ���ý���ͼƬ���ӽڵ���·���ʾ
	 * 
	 * @param back
	 *            �Ƿ���
	 */
	public void setFocusImgBack(boolean back)
	{
		if (m_focusImgBack != back)
		{
			m_focusImgBack = back;
			layoutChanged();
		}
	}

	/**
	 * ����Ƿ������˽���ͼƬ���ӽڵ���·���ʾ
	 * 
	 * @return �Ƿ�����
	 */
	public boolean getFocusImgBack()
	{
		return m_focusImgBack;
	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
	 */
	protected void onPaint(C2D_Graphics g)
	{
		if (g == null || !m_visible || m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		// �õ����Ͻ�����
		getLeftTop(point_com1);
		int pX = (int) point_com1.m_x;
		int pY = (int) point_com1.m_y;

		if (m_bgColor != null)
		{
			g.fillRect(pX, pY, m_width, m_height, m_bgColor.getColor(), 0);
		}
		if (m_bgImgClip != null)
		{
			m_bgImgClip.draw(pX + m_bgImgPos.m_x, pY + m_bgImgPos.m_y);
		}
		if (m_focusImgBack)
		{
			paintFocus(g, m_width, m_height);
		}
		paintSonList(g);
		if (!m_focusImgBack)
		{
			paintFocus(g, m_width, m_height);
		}

	}

	/**
	 * �����ӽڵ�
	 * 
	 * @param g
	 */
	protected void paintSonList(C2D_Graphics g)
	{
		paintSonList(g, m_nodeList);

	}

	/**
	 * ����ָ�������е��ӽڵ�
	 * 
	 * @param g
	 * @param list
	 */
	protected void paintSonList(C2D_Graphics g, C2D_SortableArray list)
	{
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Widget w = ((C2D_Widget) list.m_datas[i]);
			w.onPaint(g);
		}
	}

	/**
	 * ��ȡ��Բ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ�� �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ����
	 * ���β������������Ƿ�ɹ�ȡ�á�
	 * 
	 * @param resultRect
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * ��ȡ�ؼ������Ͻǵ��������꣬���ڿؼ���˵������������꣬������ͼ��˵���������ϽǾ������� ��
	 * ע��������丸��㼶�к��л�����ͼ(BufferedView)�Ļ���������꽫����������ӽ�����Ļ�����ͼ
	 */
	public boolean getLeftTop(C2D_PointF value)
	{
		if (value != null)
		{
			C2D_PointF newPos = C2D_GdiGraphics.applyAnchor(m_xToTop, m_yToTop, m_width, m_height, m_anchor);
			value.setValue(newPos);
			return true;
		}
		return false;
	}

	/**
	 * ���ӽڵ�ĽǶȻ�ȡ��ǰ�ؼ������Ͻ�����������꣬���ڿؼ���˵�� �Ǿ������꣬������ͼ��˵���������ϽǾ�������
	 */
	public boolean getPosByChild(C2D_PointF value)
	{
		return getLeftTop(value);
	}

	private static final C2D_PointF point_com = new C2D_PointF();

	/**
	 * ���ӽڵ�ĽǶȻ�ȡ��ǰ�ؼ���ռ�����򣬰������ϽǾ����������ͼ��С
	 * 
	 * @param value
	 *            ���ڷ���ֵ�洢
	 * @return �Ƿ�ɹ����
	 */
	public boolean getRectByChild(C2D_RectF value)
	{
		if (value == null)
		{
			return false;
		}
		getPosByChild(point_com);
		value.setValue(point_com.m_x, point_com.m_y, m_width, m_height);
		return true;
	}

	/**
	 * ���¾������꣬�ӵ�ԪҲҪ����һ��ˢ��
	 */
	public void refreshPos()
	{
		float xOld = m_xToTop;
		float yOld = m_yToTop;
		super.refreshPos();
		if (xOld != m_xToTop || yOld != m_yToTop)
		{
			m_needUpdateLT = true;
		}
	}

	/**
	 * ����µ��ӽڵ㣬��Ϊ�½ڵ�ָ��z���򣬽��������z����ֵ�����нڵ������zֵԽС��������ĻԽ�
	 * ���checkRepeat����Ϊ�棬���ֹ�ظ�������Ϊ�ӽڵ�,ע�⣺����ظ���ʹ������õ�Ч
	 * ������Ҫע����ӵ�˳�򣬾��������Զ����µ�˳����ӣ�����������ȷ��λ�ã��ռ䣬�������Ϣ��
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @param zOrder
	 *            z����ֵ
	 * @param checkRepeat
	 *            �Ƿ����ظ�
	 * @return �����Ƿ����ɹ�
	 */
	public boolean addChild(C2D_Widget child, int zOrder, boolean checkRepeat)
	{
		if (child == null)
		{
			return false;
		}
		child.setZOrder(zOrder);
		if (checkRepeat)
		{
			if (m_nodeList.contains(child))
			{
				return false;
			}
		}
		addChild(child);
		return true;
	}

	/**
	 * . ����µ��ӽڵ㣬��Ϊ�½ڵ�ָ��z���򣬽��������z����ֵ�����нڵ������,zֵԽС��������ĻԽ�
	 * ������ظ�.������Ҫע����ӵ�˳�򣬾��������Զ����µ�˳����ӣ�����������ȷ��λ�ã��ռ䣬�������Ϣ��
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @param zOrder
	 *            z����ֵ
	 * @return �����Ƿ����ɹ�
	 */
	public boolean addChild(C2D_Widget child, int zOrder)
	{
		if (child == null)
		{
			return false;
		}
		child.setZOrder(zOrder);
		return addChild(child);
	}

	/**
	 * ��ȡָ���±���ӽڵ�
	 * 
	 * @param childID
	 *            �ӽڵ��±�
	 * @return �ӽڵ�
	 */
	public C2D_Widget getChildAt(int childID)
	{
		if (childID < 0 || childID >= m_nodeList.m_length)
		{
			return null;
		}
		return (C2D_Widget) m_nodeList.m_datas[childID];
	}

	/**
	 * ��ȡָ�����ӽڵ�
	 * 
	 * @param flag
	 *            �ӽڵ��־
	 * @return �ӽڵ�
	 */
	public C2D_Widget getChildByFlag(int flag)
	{
		if (m_nodeList == null)
		{
			return null;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (son != null && son.getFlag() == flag)
			{
				return son;
			}
		}
		return null;
	}

	/**
	 * ��ȡָ�����ӽڵ�
	 * 
	 * @param flag
	 *            �ӽڵ��־
	 * @return �ӽڵ�
	 */
	public C2D_Widget getChildByFlag(String flag)
	{
		if (flag == null)
		{
			return null;
		}
		int size = m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (flag.equals(son.getStrFlag()))
			{
				return son;
			}
		}
		return null;
	}

	/**
	 * ����ֱ���ӽڵ�ĸ���
	 * 
	 * @return ֱ���ӽڵ�ĸ���
	 */
	public int getChildCount()
	{
		return m_nodeList.m_length;
	}

	/**
	 * �鿴��ǰ��ͼ�Ƿ�Ϊ�յģ��������κ��ӽڵ�
	 * 
	 * @return �Ƿ�Ϊ��
	 */
	public boolean isEmpty()
	{
		return m_nodeList.m_length == 0;
	}

	/**
	 * ɾ��ָ�����ӽڵ�
	 * 
	 * @param child
	 *            �ӽڵ�
	 * @return �����Ƿ�ɾ���ɹ�
	 */
	public boolean removeChild(C2D_Widget child)
	{
		if (child == null)
		{
			return false;
		}
		if (m_nodeList.remove(child))
		{
			layoutChanged();
			if (child.m_removedEvt != null)
			{
				child.m_removedEvt.onRemovedFromView(child);
			}
			return true;
		}
		return false;
	}

	/**
	 * ɾ��ָ���±���ӽڵ�
	 * 
	 * @param childID
	 *            �ӽڵ��±�
	 * @return �����Ƿ�ɾ���ɹ�
	 */
	public boolean removeChildAt(int childID)
	{
		C2D_Widget child = getChildAt(childID);
		if(child!=null)
		{
			if (m_nodeList.removeElementAt(childID))
			{
				layoutChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * ��������ӽڵ�
	 */
	public void removeAllChild()
	{
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			removeChildAt(0);
		}
	}

	/**
	 * �������е��ӽڵ㣬����Ѿ������򲻻��ٴβ���
	 */
	public void orderChildren()
	{
		if (!m_needOrder)
		{
			return;
		}
		m_nodeList.quickSort2();
		m_needOrder = false;
		layoutChanged();
	}

	/**
	 * �Զ������������������Ҫ���������ݣ���ִ�е����� ������Ҫ��������ȡ��㲻Ӧ��ʹ�����������
	 */
	protected void onAutoUpdate()
	{
		super.onAutoUpdate();
		// ˢ������Z����
		if (m_needOrder)
		{
			m_nodeList.quickSort2();
			m_needOrder = false;
		}
		// �����ӵ�Ԫ
		onAutoUpdateSonList();
		// ������ϴ���
		onAutoUpdateOther();

	}

	/**
	 * �Զ���������
	 */
	protected void onAutoUpdateSonList()
	{
		autoUpdateList(m_nodeList);
	}

	/**
	 * �Զ�����ָ���������е����
	 */
	protected void autoUpdateList(C2D_SortableArray list)
	{
		C2D_Widget childI;
		// ��������
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			childI = (C2D_Widget) list.m_datas[i];
			if (childI != null)
			{
				childI.onAutoUpdate();
			}
		}
	}

	/**
	 * ������ϴ���
	 */
	public void onAutoUpdateOther()
	{
	}

	/**
	 * ���������ӽڵ㣬ִ�и���
	 * 
	 * @param stage
	 *            ��ǰ����
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		updateSelf(stage);
		updateList(stage, m_nodeList);
	}

	/**
	 * ����ָ���������е����
	 */
	protected void updateList(C2D_Stage stage, C2D_SortableArray list)
	{
		// �����ӽڵ�ĸ����¼�
		if (list != null)
		{
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				if (list != null)
				{
					C2D_Widget wI = (C2D_Widget) list.m_datas[i];
					if (wI != null)
					{
						wI.onUpdate(stage);
					}
				}
				else
				{
					break;
				}
			}
		}
	}

	/**
	 * ������Ҫ����λ�ã���������Ҷ��Ҫ��������λ�á�
	 */
	protected void setUpdatePos()
	{
		m_needUpdatePos = true;
		m_needUpdateLT = true;
		setUpdateSonListPos();
	}

	/**
	 * ֪ͨ�����б����
	 */
	protected void setUpdateSonListPos()
	{
		setUpdateListPos(m_nodeList);
	}

	/**
	 * ָ֪ͨ����������Χ�ڵĶ�����Ҫ����λ��
	 * 
	 * @param list
	 */
	protected void setUpdateListPos(C2D_SortableArray list)
	{
		if (list == null)
		{
			return;
		}
		int len = list.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Widget childI = (C2D_Widget) list.m_datas[i];
			if (childI != null)
			{
				childI.setUpdatePos();
			}
		}
	}

	/**
	 * �����Ƿ�Χס���㣬�������Զ��뿪��ǰ��ͼ
	 * 
	 * @param besiege
	 *            �Ƿ�Χס����
	 */
	public void setBesiege(boolean besiege)
	{
		m_besiege = besiege;
	}

	/**
	 * ��⵱ǰ��ͼ�Ƿ�Χ������
	 * 
	 * @return �Ƿ�Χ������
	 */
	public boolean getBesiege()
	{
		return m_besiege;
	}

	/**
	 * ж�ر���ͼƬ�����ӵ�еĻ�
	 */
	private void releaseBgImage()
	{
		if (m_bgImg != null)
		{
			m_bgImg.doRelease(this);
			m_bgImg = null;
		}
	}

	/**
	 * ж����Ƭ�����ӵ�еĻ�
	 */
	private void releaseImageClip()
	{
		if (m_bgImgClip != null)
		{
			m_bgImgClip.doRelease(this);
			m_bgImgClip = null;
		}
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		releaseContent();
	}
	/**
	 * ж�����ݣ����������ͱ������еı���
	 */
	protected void releaseContent()
	{
		if (m_nodeList != null)
		{
			int size=m_nodeList.size();
			for (int i = 0; i < size; i++)
			{
				((C2D_Widget) m_nodeList.elementAt(i)).doRelease();
			}
			m_nodeList.clear();
		}
		m_bgColor = null;
		releaseBgImage();
		releaseImageClip();
	}

	/**
	 * ��ɸı䲼�֣���Ҫ����̨�㱨����Σ����������͸���ģ� ��û�б���ɫ�ͱ���ͼ����ô�Ὣ�㱨�Ĺ��̽�������
	 */
	public void layoutChanged()
	{
		super.layoutChanged();
		setUpdatePos();
	}

	/**
	 * �����Ƿ�ɼ�.
	 * 
	 * @param visibleNew
	 *            �Ƿ�ɼ� TODO...�����������
	 */
	public void setVisible(boolean visibleNew)
	{
		super.setVisible(visibleNew);
		for (int i = 0; i < m_nodeList.size(); i++)
		{
			((C2D_Widget) m_nodeList.elementAt(i)).m_needUpdateVisible = true;
		}
	}

	/**
	 * �������ӽڵ��ύ�����ݱ仯
	 */
	protected void noticedByChild()
	{
		noticeParent();
	}
	/**
	 * ���ݱ���ͼƬ���ã����õ�ǰ�ı���ͼƬ
	 * @param bgImgCfg
	 */
	public void setBgImgCfg(C2D_ListStyle bgImgCfg)
	{
		C2D_ImageClip bgClip=null;
		int x=0;
		int y=0;
		int imgAnchor=0;
		if (bgImgCfg != null)
		{
			bgClip=bgImgCfg.m_imgClip;

			imgAnchor= bgImgCfg.m_imgAnchor;
			if(bgImgCfg.m_imgPos!=null)
			{
				x=bgImgCfg.m_imgPos.m_x;
				y=bgImgCfg.m_imgPos.m_y;
			}
			m_bgColor = bgImgCfg.m_bgColor;
		}
		setBGImage(bgClip, (int) (x * getWidth() / 100),(int) (y * getHeight() / 100),imgAnchor );
	}
}
