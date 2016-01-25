package c2d.frame.com.view;

import c2d.frame.com.C2D_Patch9;
import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Image;
/**
 * 以九片图作为背景的视图容器，可以在其初始化之后，得到其内部容器。
 * 在其内部容器中添加你需要的内容
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
	 * 初始化并返回内容缓冲视图
	 * 
	 * @param p9Img
	 *            九片图资源
	 * @param p9Adge
	 *            九片图边缘大小
	 * @param margin
	 *            内容边距
	 * @param bgColor
	 *            背景颜色
	 * @return 内容缓冲视图
	 */
	public C2D_ViewUtil init(C2D_Image p9Img, int p9Adge, int margin, int bgColor)
	{
		//背景九图
		C2D_Patch9 bgVIew = new C2D_Patch9(p9Img);
		bgVIew.setAdgeSize(p9Adge, p9Adge);
		bgVIew.setSize(m_width, m_height);
		addChild(bgVIew, 1);
		//内容缓冲
		C2D_ViewUtil contentView = null;
		contentView = new C2D_ViewUtil();
		addChild(contentView, 2);
		contentView.setSize(m_width - margin * 2, m_height - margin * 2);
		contentView.setToParentCenter();
		contentView.setBGColor(bgColor);
		//设置九片图的填充背景色
		bgVIew.setFillColor(C2D_Color.makeARGB(bgColor));
		return contentView;
	}
}
