package c2d.frame.com.view;

import c2d.frame.com.C2D_Patch9;
import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Image;
/**
 * �Ծ�Ƭͼ��Ϊ��������ͼ���������������ʼ��֮�󣬵õ����ڲ�������
 * �����ڲ��������������Ҫ������
 * @author AndrewFan
 *
 */
public class C2D_P9View extends C2D_ViewUtil
{
	public C2D_P9View(int width, int height)
	{
		setSize(width, height);
	}
	/**
	 * ��ʼ�����������ݻ�����ͼ
	 * 
	 * @param p9Img
	 *            ��Ƭͼ��Դ
	 * @param p9Adge
	 *            ��Ƭͼ��Ե��С
	 * @param margin
	 *            ���ݱ߾�
	 * @param bgColor
	 *            ������ɫ
	 * @return ���ݻ�����ͼ
	 */
	public C2D_ViewUtil init(C2D_Image p9Img, int p9Adge, int margin, int bgColor)
	{
		//������ͼ
		C2D_Patch9 bgVIew = new C2D_Patch9(p9Img);
		bgVIew.setAdgeSize(p9Adge, p9Adge);
		bgVIew.setSize(m_width, m_height);
		addChild(bgVIew, 1);
		//���ݻ���
		C2D_ViewUtil contentView = null;
		contentView = new C2D_ViewUtil();
		addChild(contentView, 2);
		contentView.setSize(m_width - margin * 2, m_height - margin * 2);
		contentView.setToParentCenter();
		contentView.setBGColor(bgColor);
		//���þ�Ƭͼ����䱳��ɫ
		bgVIew.setFillColor(C2D_Color.makeARGB(bgColor));
		return contentView;
	}
}
