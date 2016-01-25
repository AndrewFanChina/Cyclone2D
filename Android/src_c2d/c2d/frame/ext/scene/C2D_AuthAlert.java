package c2d.frame.ext.scene;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.event.C2D_Event_ChangeStrValue;
import c2d.frame.ext.group.C2D_InputNumBox;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_SizeI;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

public abstract class C2D_AuthAlert extends C2D_AlertScene
{
	/** ��֤�뱳�� */
	C2D_View w_AuthView;
	/** ������֤���ı��� */
	protected C2D_InputNumBox w_authInput;
	/** ������֤�����ɿ� */
	protected C2D_PicTextBox w_authGen;
	/** ���������ʾ�� */
	protected C2D_TextBox w_errorBox;

	public C2D_AuthAlert(C2D_SizeI viewSize, C2D_SizeI textSize)
	{
		super(viewSize, textSize);
	}

	protected void onAddedToStage()
	{
		super.onAddedToStage();
		if (this.isEmpty())
		{
			super.onAddedToStage();
			w_introTxt.setPosPer(50, 26);
			w_btnOk.setPosPer(30, 80);
			w_btnBack.setPosPer(70, 80);
			// �����֤����ͼ��tz:5��
			w_AuthView = new C2D_View();
			w_AuthView.setSize(330, 200);
			w_totalView.addChild(w_AuthView, 20);
			w_AuthView.setAnchor(C2D_Consts.HVCENTER);
			w_AuthView.setPosPer(50, 64);
			w_AuthView.setBGImage(new C2D_ImageClip(getAuthViewBg()));
			// �����֤�������az:1��
			w_authInput = new C2D_InputNumBox(getAuthFont());
			w_AuthView.addChild(w_authInput, 1);
			w_authInput.setMaxInput(4);
			w_authInput.setPosTo(58, 66);
			w_authInput.setAnchor(C2D_Consts.VCENTER);
			w_authInput.setFocusedTxtBgColor(new C2D_Color(0xFF7241));
			w_authInput.setDelByLeft(true);
			//��ʾ��������
			w_authInput.Events_ChangeValue().add(new C2D_Event_ChangeStrValue()
			{
				protected boolean doEvent(C2D_Widget carrier, String newValue)
				{
					if(w_authInput == null||newValue==null||w_authGen==null )
					{
						return false;
					}
					String gen = w_authGen.getText();
					if(gen.startsWith(newValue)||newValue.length()==0)
					{
						w_errorBox.setText("");
					}
					else
					{
						w_errorBox.setText("��֤�����");
					}
					return false;
				}
			});
			// ���������֤���az:2��
			w_authGen = new C2D_PicTextBox(getAuthFont());
			w_authGen.setPosTo(240, 66);
			w_authGen.setAnchor(C2D_Consts.HVCENTER);
			String m_strCC = C2D_Math.getRandom(1000, 10000) + "";//�����ֵ
			w_authGen.setText(m_strCC);
			w_AuthView.addChild(w_authGen, 2);
			//������ʾ��az:3��
			w_errorBox = new C2D_TextBox();
			w_totalView.addChild(w_errorBox, 20);
			w_errorBox.setToParentCenter();
			w_errorBox.setPosPer(50, 66);
			w_errorBox.setText("");
			w_errorBox.setColor(0xFF0000);
			// ����򽹵�
			w_authInput.setFocused();
		}
	}
	/**
	 * �����֤��ͼƬ����
	 * @return ��֤��ͼƬ����
	 */
	protected abstract C2D_PTC getAuthFont();

	/** 
	 * ��ȡ��֤�뱳��ͼƬ 
	 * @return ����ͼƬ
	 */
	protected abstract C2D_Image getAuthViewBg();
	/**
	 * ����Ƿ�������ȷ����֤�룬�������������
	 * @return
	 */
	public boolean checkRightInput()
	{
		if(w_authInput == null||w_authGen==null )
		{
			return false;
		}
		String input=w_authInput.getText();
		String gen = w_authGen.getText();
		if(input==null||input.length()==0||gen==null)
		{
			w_errorBox.setText("��������֤��");
			return false;
		}
		if(input.equals(gen))
		{
			return true;
		}
		if(gen.startsWith(input))
		{
			w_errorBox.setText("���������");
		}
		else
		{
			w_errorBox.setText("��֤�����");
		}
		return false;
	}
}
