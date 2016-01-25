package c2d.frame.ext.scene;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_Event_Button;
import c2d.frame.event.C2D_Event_Update;
import c2d.lang.math.type.C2D_SizeI;
import c2d.mod.C2D_Consts;

/**
 * ��������ť��ȷ���ͷ��أ���һ����ʾ�ı���������ѳ����� 1�����ڶԻ���ȫ������Ļ�ײ��Ǵ�ɫ��
 * 2���Ի���ı�����һ����Ƭͼ����m_bgView����,���Ĵ�С�����û����á� 3���Ի�������ݴ����m_totalView�У����Ĵ�С�ͱ�����ͬ��
 * 4���Ի��������д����������ť���ֱ���ȷ���ͷ��أ������Ѿ�Ԥ�����¼����á� 5���Ի��������к���һ���ı���������ʾ˵�����֡�
 * 6����������еİ�ť�¼���Ҫ����ʵ�֡�
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_AlertScene extends C2D_SceneUtil
{
	/** ������ͼ */
	public C2D_View w_bgView;
	/** ������ͼ */
	public C2D_ViewUtil w_totalView;
	/** ˵�����ֿؼ� */
	public C2D_TextBox w_introTxt;
	/** ȷ����ť */
	public C2D_View w_btnOk;
	/** ���ذ�ť */
	public C2D_View w_btnBack;
	//--------------------������Ϣ---------------------------
	/** Ĭ�Ͻ����С */
	protected C2D_SizeI m_viewSize = new C2D_SizeI(320, 240);
	/** Ĭ�Ͻ����С */
	protected C2D_SizeI m_textSize = new C2D_SizeI(300, 160);

	public C2D_AlertScene(C2D_SizeI viewSize, C2D_SizeI textSize)
	{
		m_viewSize.setValue(viewSize);
		m_textSize.setValue(textSize);
	}

	// ��ť�����¼�
	protected C2D_Event_Button btn_event = new C2D_Event_Button()
	{
		protected boolean doEvent(C2D_Widget carrier, int btnState)
		{
			if (carrier == null)
			{
				return false;
			}
			String strName = carrier.getName();
			if (strName != null)
			{
				if (strName.equals("ok"))
				{
					onBtnOkPressed();
				}
				else if (strName.equals("back"))// ����ǰһ����
				{
					onBtnBackPressed();
				}
			}
			return false;
		}
	};
	protected C2D_Event_Update m_backKeyEvent = new C2D_Event_Update()
	{
		protected boolean doEvent(C2D_Widget carrier)
		{
			//���ؼ���Ӧ
			if (getStageAt().isKeyBackOrRSoft())
			{
				getStageAt().releaseKeys();
				onKeyBackPressed();
				return true;
			}
			return false;
		}
	};

	protected void onAddedToStage()
	{
		super.onAddedToStage();
		if (!isEmpty())
		{
			return;
		}
		//������ɫ
		setBGColor(0);
		//����������z:0��
		w_bgView = onCreateBgView(m_viewSize.m_width, m_viewSize.m_height);
		//������ͼ��z:10��
		w_totalView = new C2D_ViewUtil();
		w_totalView.setSize(w_bgView.getWidth(), w_bgView.getHeight());
		addChild(w_totalView, 10);
		w_totalView.setToParentCenter();
		// ��������ͼ�������˵����tz:10��
		w_introTxt = new C2D_TextBox();
		w_introTxt.setLimitWidth(m_textSize.m_width);
		w_totalView.addChild(w_introTxt, 10);
		w_introTxt.setPosPer(50, 40);
		w_introTxt.setAnchor(C2D_Consts.HVCENTER);
		w_introTxt.setColor(0xaf360a);
		// ��������ͼ���ȷ����ť��tz:20��
		w_btnOk = onCreateBackButton();
		w_totalView.addChild(w_btnOk, 20);
		w_btnOk.setPosPer(70, 80);
		w_btnOk.setName("ok");
		// ��������ͼ��ӷ��ذ�ť��tz:30��
		w_btnBack = onCreateOkButton();
		w_totalView.addChild(w_btnBack, 30);
		w_btnBack.setPosPer(30, 80);
		w_btnBack.setName("back");
		// �����¼�
		Events_Update().add(m_backKeyEvent);
		// Ĭ�����ý���Ϊȷ��
		getSceneAt().setFocusedWidget(w_btnOk);
		// �Զ�������
		doSelfConfig();
	}

	protected abstract void doSelfConfig();

	protected abstract C2D_View onCreateOkButton();
	
	protected abstract C2D_View onCreateBackButton();

	protected abstract C2D_View onCreateBgView(int width, int height);

	protected abstract void onBtnBackPressed();

	protected abstract void onBtnOkPressed();
	
	protected void onKeyBackPressed()
	{
		close();
	}

	public void onRelease()
	{
		super.onRelease();
		w_bgView = null;
		w_totalView = null;
		w_introTxt = null;
		w_btnOk = null;
		w_btnBack = null;
	}
}
