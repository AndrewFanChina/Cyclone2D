package game.tutorial.c2d;

import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;
/**
 * -----L02-04��ʹ��ϵͳ�ı���----- <br>
 * ϵͳ�ı���(C2D_TextBox)��������ʾϵͳ���ɵ��ı���<br>
 * ��J2me�У������ı���С�����岻�ɿأ���ɫ�������ã���Android�У�����ʹ��Ĭ�ϵ��ֻ����壬<br>
 * Ҳ����ʹ���Լ����õ������ļ�,��С�����塢��ɫ�ȶ��ǿ��Կ��Ƶġ�ϵͳ�ı��Ļ����ٶ��Ǻ����ģ�<br>
 * ��J2me�п����ʵ�����ʹ��C2D_BufferedView���л��壬������ͼЧ�ʡ�����Ҳ����ѡ�����<br>
 * ���ἰ��ͼƬ�ı��������ʹ��Ԥ�����ɵ�ͼƬ�����������ʾ��<br>
 * ϵͳ�ı�����ռ��һ��������ı����䣬�����ʹ�ò�ͬ�Ĺ��캯����ȷ��ʹ�õ����ı����Ƕ����ı���<br>
 * �ı����������ƵĿ�Ⱥ͸߶ȣ������ͨ��setLimitSize(int limitW, int limitH)��<br>
 * �������޳ߴ磬ͨ��setMargin(int, int)���������ݱ߾࣬�����ݳ���ʱ���������ұ߿��<br>
 * ������������ÿ���ͨ��setText(String),����Ƕ����ı������ݽ�ʵ���Զ����У�����Խ���<br>
 * ����֮�⣬'\n'�Ļ��з�Ҳ�������С�����ͨ��setColor(C2D_Color)�������ı�����ɫ��<br>
 * setTxtBgColor(C2D_Color)�������������ı���ı�����ɫ�����������Ҫ����Ե��ɫ���ı���<br>
 * ����ͨ������setBorderColor(C2D_Color)�������Ҫ��Ч�����������ı��������ӻ�ͼ�������ġ�<br>
 * �����Ҫ���ı�ʵ�ְ��й���Ч�������Ե�����scrollFront()��scrollBack()������<br>
 * 
 * @author AndrewFan
 *
 */
public class L02_04_TxtBox extends C2D_SceneUtil implements LessonUtil
{

	public L02_04_TxtBox()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_04_TxtBox";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		setBGColor(0xFF);
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		//ʹ��Ĭ�ϵķ�ʽ��������ı���
		C2D_TextBox txtBox1 = new C2D_TextBox();
		txtBox1.setText("��������\n�˵���\n�������ɳ��");
		txtBox1.setPosTo(stayView1.getWidth()/2, stayView1.getHeight()/2);
		txtBox1.setZOrder(1);
		txtBox1.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		txtBox1.setColor(0xFFFFFF);
		//������ʾ�Ŀ��60
		txtBox1.setLimitWidth(60);
		stayView1.addChild(txtBox1);
		//ʹ�ñ���������������ı�
		C2D_TextBox txtBox2 = stayView1.addTxtBox("�󽭶�ȥ���Ծ�\nǧ�ŷ�������", stayView1.getWidth()/2, stayView1.getHeight()/4, 2, 0x00FF00);
		txtBox2.Events_KeyPress().add(m_moveEvent);
		//������ʾ�Ŀ��60
		txtBox2.setLimitWidth(60);
		//���쵥���ı���������ʾ���120
		C2D_TextBox txtBox3=new C2D_TextBox(120);
		txtBox3.setText("��ʯ���գ������İ�������ǧ��ѩ��");
		txtBox3.setPosTo(stayView1.getWidth()/2, stayView1.getHeight()*3/4);
		txtBox3.setZOrder(1);
		txtBox3.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		txtBox3.setColor(0xFF0000);
		stayView1.addChild(txtBox3);
		//���÷��ؼ��˳�
		setBackKey(key_back);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}
}
