package game.tutorial.c2d;

import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;

/**
 * -----L01-03����ͼ����״�ṹ----- <br>
 * C2D��������ؼ�����һ������ͨ�ؼ������Ǵ����ڵڶ���ؼ���������ͼ C2D_View���С�<br>
 * 1����״�ṹ�Ĺ�����ϵ��<br>
 * ��ͼռ���˻����ϵ�һ���������������Ҳ��һ���ؼ��������ڲ����������������ӿؼ���<br>
 * �����ڲ��Ŀؼ��㼶����״�ֲ�������Ҳ��һ����ͼ����ռ������������������״ͼ�ĸ��ڵ㣬<br>
 * �����ڲ�����ͼ�����ӽڵ㣬Ҷ�ڵ��Ǹ������Ŀؼ������ı���ͼƬ�򣬾���ȡ�<br>
 * ��ͼ���Լ����ӽڵ�(addChild)�������Ƴ��ڵ�(removeChild) ����ýڵ�(getChildAt)��<br>
 * ע��getChildAt�Ǹ����ֵܽڵ�֮�������(ZOrder)����ȡ�ġ������Ҫȡ��֮ǰ���������еĿؼ���<br>
 * Ӧ��ʹ��getChildByFlag(String)����getChildByFlag(int)��������Ϊ����������ӻ�<br>
 * ���Ƴ��ؼ��������ӽڵ����������<br>
 * �����ڵ�(C2D_Scene)�ǿؼ����ĸ��ڵ㣬�ؼ��������еĿؼ�������ʹ��getSceneAt()׷�ݵ�����ڵĳ�����<br>
 * Ҳ����ʹ��getStageAt()׷�ݵ������ڵ���̨��<br>
 * 2����Ը�������ƽ�����ꡣ<br>
 * ���пؼ����̳�������C2D_Widget��,��ӵ��ƽ������m_x��m_y��ê��m_anchor��<br>
 * m_ZOrder�������ֵ�Ȼ������ԡ����ǵ�ƽ���������������꣬������丸���� ��<br>
 * ��ˣ������������ƶ�ʱ�����е�����������һ���ƶ����ڻ�ȡ��Щ���Ե�ʱ����Ҫʹ�ú� ��getXXX��<br>
 * ���õ�ʱ����Ҫʹ��setXXX����ʹ�̳е���Ҳ��Ҫ�������ã�һ������ֱ�� �ı�����ֵ��<br>
 * 3��ê������<br>
 * ê��m_anchor�����趨��ǰ�ؼ��Ķ��뷽ʽ���������������Ŀؼ�����ֵ������Ч��������ͼ��ͼƬ���ı���ȡ�<br>
 * Ŀǰ���������Ŀؼ�ֻ�о���(C2D_Sprite)���߶�(C2D_Segement)��<br>
 * 4���Զ�������������<br>
 * Z����ֵ�������ֵܽڵ�֮������򣬴���ֵԽС���ؼ�Խ�����²㡣 <br>
 * Ҳ���������(m_ZOrder)���������ֵܽڵ�����ȹ�ϵ����ֵԽС����Խ������Ļ�ڲ���Խ���ױ������ڵ������ǡ�<br>
 * 5����ͼ����������<br>
 * ��ͼ�����ϲ������Ӹ�����ͨ�ؼ����²㻹����ӵ�б���ɫ�ͱ���ͼƬ������㲻�������ǣ� ��ͼ����Ϊһ��͸����������<br>
 * ע�Ȿ��ʹ����C2D_SceneUtil������C2D_Scene�ı������࣬����ʹ������һЩ����������<br>
 * 
 * @author AndrewFan
 * 
 */
public class L01_03_View extends C2D_SceneUtil implements LessonUtil
{
	public L01_03_View()
	{
	}
	public String getBvrNodeName()
	{
		return "L01_03_View";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		//���ڵı���ɫ
		setBGColor(0x0);
		//�ڳ�������������ڲ���ͼ
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		//��ͼ2��ZOrder����Ϊ2������ڵ���ͼ1
		C2D_ViewUtil stayView2 = addInnerView(20, 200, 2, 0x880000);
		//������ͼ2��λ��
		stayView2.setPosTo(20, 100);
		//����ͼ1�����һ����ɫ��ͼ
		C2D_ViewUtil moveView = stayView1.addInnerView(20, 20, 1, 0xFFFFFF);
		//�ڰ�ɫ��ͼ�����һ����ɫ��ͼ
		moveView.addInnerView(50, 50, 1, 0x00FF00).setFlag(123);
		//����ƶ��¼�
		moveView.Events_KeyPress().add(m_moveEvent);
		//���÷��ؼ��˳�
		setBackKey(key_back);
	}

	protected void onPaint(C2D_Graphics g)
	{
		super.onPaint(g);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}

	protected void onSentBack()
	{
	}

	protected void onSentTop()
	{
	}

	protected void onShown()
	{
	}

	protected void onHidden()
	{
	}
}
