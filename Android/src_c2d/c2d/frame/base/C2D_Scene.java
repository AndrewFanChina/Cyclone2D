package c2d.frame.base;

import bvr.path.C2D_BvrNode;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.event.C2D_EventPool_Scene;
import c2d.frame.util.C2D_SceneTracker;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Graphics;

/**
 * --onAddedToStage()����������ӵ���̨ʱ�ᷢ����
 * �������������Ҫ����������Դ��Ҫע���ʱ�򣬲�Ҫ�ظ�������Դ����Ϊ����������ܻᱻ��ε��ã���μ��غ��Ƴ���
 * --onRemovedFromStage()����������̨�Ƴ�ʱ�ᷢ����
 * 
 * @author AndrewFan
 */
public abstract class C2D_Scene extends C2D_ViewUtil implements C2D_BvrNode
{
	/** ����״̬ --��ӵ���̨ -- */
	public static final int SCENE_AddToStage = 0;
	/** ����״̬ --����̨�Ƴ� -- */
	public static final int SCENE_RemovedFromStage = 1;
	/** ����״̬ --�ƶ���ǰ̨ -- */
	public static final int SCENE_SentTop = 2;
	/** ����״̬ --�ƶ�����̨ -- */
	public static final int SCENE_SentBack = 3;
	/** ����״̬ --������ʾ -- */
	public static final int SCENE_Shown = 4;
	/** ����״̬ --�������� -- */
	public static final int SCENE_Hidden = 5;
	/** ����״̬ --��ɵ�һ�λ滭 -- */
	public static final int SCENE_Painted = 6;
	/** �¼���-������ʾ������ */
	private C2D_EventPool_Scene m_Events_Scene;
	/** ��ǰ����ؼ� */
	private C2D_Widget m_focusedNow;
	/** ��һ������ؼ� */
	private C2D_Widget m_focusedNext;
	/** ��ʱ���㶥�� */
	private C2D_PointF fwPosNew = new C2D_PointF();
	private C2D_PointF fwPosNow = new C2D_PointF();
	private C2D_PointF fwPosTry = new C2D_PointF();
	/**
	 * ��Ҫ����ˢ��
	 */
	public boolean m_alwaysRepaint = false;
	/** �������ȵ�������ʽ */
	public static int SearchFocus_DIR = 0;
	/** �ۺϾ����������ʽ */
	public static int SearchFocus_DIC = 1;
	/** �Ӿ��йٵ�������ʽ */
	public static int SearchFocus_LOOK = 2;
	/** ZOrder�����������ʽ */
	public static int SearchFocus_ZORDER = 3;
	/** ���ƶ����ʱ�Ĳ������� */
	protected int m_searchType = SearchFocus_LOOK;
	/**
	 * ���ɳ���
	 */
	protected C2D_TransScene m_transScene;
	/** ��ǰ�����ļ��ؽ��� */
	protected int m_process;
	/** �Ƿ������ڹ��ɳ��� */
	protected boolean m_inLoading;
	/** ������ */
	protected C2D_SceneStack m_pStack;
	private int m_painted = -1;

	/**
	 * ���쳡��
	 */
	public C2D_Scene()
	{
		setName(this.toString());
		C2D_SceneTracker.TrackScene(this);
	}

	/**
	 * ���õ�ǰ�������ڵ���̨
	 * 
	 * @param stageAt
	 *            ���ڵ���̨
	 */
	void setStage(C2D_Stage stageAt)
	{
		m_atStage = stageAt;
		if (m_atStage != null)
		{
			m_pStack = m_atStage.m_sceneStack;
		}
	}

	/**
	 * ���㵱ǰ�������ڵ���̨���� ��������������̨֮�󣬲Ż���ڴ˶���
	 * 
	 * @return ���ڵ���̨����
	 */
	public C2D_Stage accountStage()
	{
		return m_atStage;
	}

	/**
	 * �������ڵĳ�������
	 * 
	 * @return ���ڵĳ�������
	 */
	public C2D_Scene accountScene()
	{
		return this;
	}

	/**
	 * ����Ƿ��ڼ�����
	 * 
	 * @return
	 */
	protected boolean onLoadingUpdate()
	{
		// ���������
		if (m_transScene != null && !m_inLoading)
		{
			m_transScene.c2d_removeFromStage();
			m_transScene = null;
		}
		// ���¼����߼�
		if (m_transScene != null)
		{
			if (m_transScene != null)
			{
				m_transScene.onUpdate(m_transScene.m_atStage);
			}
			// �������Զ��߼������ڳ����п��ܱ��л������������»��
			if (m_transScene != null)
			{
				m_transScene.onAutoUpdate();
			}
			return true;
		}
		return false;
	}

	/**
	 * ���ص�ǰӵ�н���Ŀؼ���ÿ������ֻ��ӵ��һ������û�н���ؼ�
	 * 
	 * @return ǰӵ�н���Ŀؼ�
	 */
	public C2D_Widget getFoucusedWidget()
	{
		if (m_focusedNow != null && m_focusedNow.m_focused)
		{
			return m_focusedNow;
		}
		return null;
	}

	/**
	 * ��ָ���Ŀؼ�����Ϊ��ǰӵ�н���Ŀؼ���ÿ������ֻ��ӵ��һ������û�н���ؼ��� �����һ������ӵ�н���Ŀؼ����ý��㣬�������κθı䡣
	 * 
	 * @param widget
	 *            ǰӵ�н���Ŀؼ�
	 */
	public void setFocusedWidget(C2D_Widget widget)
	{
		if (widget == null)
		{
			return;
		}
		if (!contains(widget))
		{
			return;
		}
		if (!widget.getFocusable())
		{
			return;
		}
		// ������ת��
		if (m_focusedNow != null)
		{
			m_focusedNow.m_focused = false;
		}
		C2D_Widget orgFox = m_focusedNow;
		m_focusedNow = widget;
		if (m_focusedNow != null)
		{
			m_focusedNow.m_focused = true;
		}
		// �����¼�
		if (orgFox != null)
		{
			orgFox.lostFocus(m_focusedNow);
		}
		if (m_focusedNow != null)
		{
			m_focusedNow.gotFocus(orgFox);
		}
	}

	/** �����ǰ���㴦��Χ����ͼ����ʹ��Χ��Ѱ�� */
	private C2D_View m_besiegeView;

	/**
	 * ������ָ���ؼ���ָ�������ƶ����㣬ע��������ʽ
	 * ��ָ������ͼ��Ѱ�ң�λ��ָ���ؼ���ĳ�������ϵĿ���ӵ�н��㲢�ҷ����صĿؼ��ļ��� <br>
	 * ������ָ���ؼ���ָ�������ƶ����㣬ע�����صĿؼ����ܻ�ý���<br>
	 * �������ȵ�������ʽ��<br>
	 * ����Ѱ��ʱ������ѡ���Ϸ����з�Χ��<Y��Y��ӽ��Ŀؼ� ����Ѱ��ʱ������ѡ���·����з�Χ��>Y��Y��ӽ��Ŀؼ�
	 * ����Ѱ��ʱ������ѡ�������з�Χ��<X��X��ӽ��Ŀؼ� ����Ѱ��ʱ������ѡ���ҷ����з�Χ��>X��X��ӽ��Ŀؼ� <br>
	 * �ۺϾ����������ʽ��<br>
	 * ����Ѱ��ʱ������ѡ���Ϸ����з�Χ��<Y��X����+Y����ӽ��Ŀؼ� ����Ѱ��ʱ������ѡ���·����з�Χ��>Y��X����+Y����ӽ��Ŀؼ�
	 * ����Ѱ��ʱ������ѡ�������з�Χ��<X��X����+Y����ӽ��Ŀؼ� ����Ѱ��ʱ������ѡ���ҷ����з�Χ��>X��X����+Y����ӽ��Ŀؼ�<br>
	 * �Ӿ��йٵ�������ʽ��������ָ����ľ�����Ϊ������������ֱָ����ľ�����Ϊ������ָ����������<br>
	 * ����Ѱ��ʱ������ѡ���Ϸ����з�Χ��<Y��Y����+X����ƫ��*2���С�Ŀؼ�
	 * ����Ѱ��ʱ������ѡ���·����з�Χ��>Y��Y����+X����ƫ��*2���С�Ŀؼ�
	 * ����Ѱ��ʱ������ѡ�������з�Χ��<X��X����+Y����ƫ��*2���С�Ŀؼ�
	 * ����Ѱ��ʱ������ѡ���ҷ����з�Χ��>X��X����+Y����ƫ��*2���С�Ŀؼ�<br>
	 * @param widget
	 *            ָ���Ŀؼ�
	 * @param direction
	 *            ָ���ؼ���ָ������0,1,2,3�ֱ��Ӧ��������
	 * @return �Ƿ�ı�����
	 */
	public boolean moveFocus(C2D_Widget widget, int direction)
	{
		if (widget == null)
		{
			return false;
		}
		// ȷ��ָ���ؼ������Ͻ�
		fwPosNow.setValue(widget.getLeftTop());
		// ����Ѱ��ָ���������Ƿ��п���ӵ�н���Ŀؼ�
		m_focusedNext = null;
		m_besiegeView = null;
		if (m_focusedNow != null)
		{
			C2D_View vNow = m_focusedNow.getParentNode();
			if (vNow != null && vNow.getBesiege())
			{
				m_besiegeView = vNow;
			}
		}
		searchFoxWidget(this, widget, direction);
		m_besiegeView = null;
		if (m_focusedNext != null)
		{
			setFocusedWidget(m_focusedNext);
			m_focusedNext = null;
			return true;
		}
		return false;
	}

	/**
	 * ��ָ������ͼ��Ѱ�ҽ���
	 * 
	 * @param view
	 *            Ѱ�ҷ�Χ
	 * @param widget
	 *            ָ���ؼ�
	 * @param direction
	 *            ָ������
	 */
	private void searchFoxWidget(C2D_View view, C2D_Widget widget, int direction)
	{
		if (view == null || widget == null || !view.getVisible())
		{
			return;
		}
		int size = view.m_nodeList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget child = (C2D_Widget) view.m_nodeList.elementAt(i);
			if (m_besiegeView == null || m_besiegeView.equals(view))
			{
				if (!widget.equals(child) && child.getFocusable() && child.getVisible())
				{
					fwPosTry.setValue(child.getLeftTop());
					boolean pass = false;
					if (m_searchType == SearchFocus_DIR)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y > fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) < C2D_Math
											.abs(fwPosNew.m_x - fwPosNow.m_x)));
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x
									&& fwPosTry.m_y >= fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math
											.abs(fwPosNew.m_y - fwPosNow.m_y)));
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) < C2D_Math
											.abs(fwPosNew.m_x - fwPosNow.m_x)));
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x
									&& fwPosTry.m_y >= fwPosNow.m_y
									&& (m_focusedNext == null || fwPosTry.m_y < fwPosNew.m_y || (fwPosTry.m_y == fwPosNew.m_y && C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math
											.abs(fwPosNew.m_y - fwPosNow.m_y)));
							break;
						}
					}
					else if (m_searchType == SearchFocus_DIC)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y && (m_focusedNext == null || tryDicSearch());
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x && (m_focusedNext == null || tryDicSearch());
							break;
						}
					}
					else if (m_searchType == SearchFocus_LOOK)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = fwPosTry.m_y < fwPosNow.m_y && (m_focusedNext == null || tryLookSearch(0, -1));
							break;
						case C2D_Device.key_right:
							pass = fwPosTry.m_x > fwPosNow.m_x && (m_focusedNext == null || tryLookSearch(1, 0));
							break;
						case C2D_Device.key_down:
							pass = fwPosTry.m_y > fwPosNow.m_y && (m_focusedNext == null || tryLookSearch(0, 1));
							break;
						case C2D_Device.key_left:
							pass = fwPosTry.m_x < fwPosNow.m_x && (m_focusedNext == null || tryLookSearch(-1, 0));
							break;
						}
					}
					else if (m_searchType == SearchFocus_ZORDER)
					{
						switch (direction)
						{
						case C2D_Device.key_up:
							pass = child.m_zOrder > m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder < m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_left:
							pass = child.m_zOrder < m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder > m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_right:
							pass = child.m_zOrder > m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder < m_focusedNext.m_zOrder);
							break;
						case C2D_Device.key_down:
							pass = child.m_zOrder < m_focusedNow.m_zOrder && (m_focusedNext==null || child.m_zOrder > m_focusedNext.m_zOrder);
							break;

						}
					}
					if (pass)
					{
						m_focusedNext = child;
						fwPosNew.setValue(fwPosTry);
					}
				}
			}
			if (child instanceof C2D_View)
			{
				searchFoxWidget((C2D_View) child, widget, direction);
			}
		}
	}

	private boolean tryDicSearch()
	{
		return C2D_Math.abs(fwPosTry.m_x - fwPosNow.m_x) + C2D_Math.abs(fwPosTry.m_y - fwPosNow.m_y) < C2D_Math.abs(fwPosNew.m_x - fwPosNow.m_x)
				+ C2D_Math.abs(fwPosNew.m_y - fwPosNow.m_y);
	}

	private boolean tryLookSearch(int dirX, int dirY)
	{
		int weightNew = getLookWeight(dirX,dirY,fwPosNew,fwPosNow);
		int weightTry = getLookWeight(dirX,dirY,fwPosTry,fwPosNow);
		return weightTry<weightNew;
	}
	
	private int getLookWeight(int dirX, int dirY,C2D_PointF pos1,C2D_PointF pos2)
	{
		float weightDir = C2D_Math.abs(dirX * C2D_Math.abs(pos1.m_x - pos2.m_x)) + C2D_Math.abs(dirY * C2D_Math.abs(pos1.m_y - pos2.m_y));
		float weightNeg = C2D_Math.abs(dirX * C2D_Math.abs(pos1.m_y - pos2.m_y)) + C2D_Math.abs(dirY * C2D_Math.abs(pos1.m_x - pos2.m_x));
		return (int)(weightDir+weightNeg*2);
	}

	/**
	 * �����ʾ�¼���
	 * 
	 * @return ��ʾ�¼���
	 */
	public C2D_EventPool_Scene Events_Scene()
	{
		if (m_Events_Scene == null)
		{
			m_Events_Scene = new C2D_EventPool_Scene(this);
		}
		return m_Events_Scene;
	}

	public void setSearchType(int searchType)
	{
		if (searchType < SearchFocus_DIC || searchType > SearchFocus_ZORDER)
		{
			return;
		}
		this.m_searchType = searchType;
	}

	/**
	 * ������е��¼�������Դ�ͷ�ʱ������Ҫ����
	 */
	public void clearEvents()
	{
		super.clearEvents();
		Events_Scene().clear();
	}

	/**
	 * ��������ӽڵ�
	 */
	public void removeAllChild()
	{
		super.removeAllChild();
		m_focusedNow = null;
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.doRelease();
			m_Events_Scene = null;
		}
		m_atStage = null;
		m_focusedNow = null;
	}

	/**
	 * �رգ�ע�⣬ֻ�д��ڵ�ǰ��˵ĳ����������ر�
	 * 
	 * @return �Ƿ�رճɹ�
	 */
	public boolean close()
	{
		C2D_Stage stage = m_atStage;
		if (stage == null)
		{
			return false;
		}
		C2D_Scene top = stage.currentScene();
		if (!this.equals(top))
		{
			return false;
		}
		stage.popScene();
		stage.releaseKeys();
		return true;
	}

	/**
	 * ���ü��ؽ���
	 */
	public void ressetProcess()
	{
		m_process = 0;
	}

	/**
	 * ���ü��ؽ���
	 * 
	 * @param value
	 */
	public void setProcess(int value)
	{
		int p = m_process;
		m_process = value;
		m_process = C2D_Math.limitNumber(m_process, 0, 100);
		C2D_Debug.log("--->>load process:"+value);
		if (p != m_process && m_transScene != null)
		{
			m_transScene.onProcessChanged(m_process);
		}
	}

	/**
	 * ���Ӽ��ؽ���
	 * 
	 * @param value
	 */
	public void incProcess(int value)
	{
		int p = m_process;
		m_process += value;
		m_process = C2D_Math.limitNumber(m_process, 0, 100);
		if (p != m_process && m_transScene != null)
		{
			m_transScene.onProcessChanged(m_process);
		}
	}

	/**
	 * ��ü��ؽ���
	 * 
	 * @return
	 */
	public int getProcess()
	{
		return m_process;
	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
	 */
	protected void onPaint(C2D_Graphics g)
	{
		if (m_inLoading)
		{
			if (m_transScene != null)
			{
				m_transScene.onPaint(g);
			}
			return;
		}
		super.onPaint(g);
		if (m_painted < 0)
		{
			m_painted = 0;
		}
	}

	protected void readyLoading()
	{
		m_inLoading = true;
		m_process = 0;
	}

	public void endLoading()
	{
		m_inLoading = false;
	}

	/**
	 * ����ɽ�Ŀ�곡�����뵱ǰ��̨�����Ŀ�곡������һЩ��ʼ��
	 * 
	 * @param stage
	 *            Ŀ�곡��
	 */
	protected void afterPushStage(C2D_Stage stage)
	{
		if (m_width <= 0 || m_height <= 0)
		{
			setSize(C2D_Stage.User_Size);
		}
		setStage(stage);
		c2d_addTostage();
		c2d_sentTop();
		layoutChanged();
		onAutoUpdate();
	}

	/**
	 * ע����ɳ���
	 * 
	 * @param transScene
	 *            ���ɳ���
	 */
	protected void registerLoder(C2D_TransScene transScene)
	{
		m_transScene = transScene;
	}
	/**
	 * �Ƿ��Ƕ��㳡��
	 * @return �Ƿ��Ƕ��㳡��
	 */
	public boolean isTop()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null)
		{
			return false;
		}
		return this.equals(stage.currentScene());
	}
	/**
	 * �Զ�����
	 */
	protected final void onAutoUpdate()
	{
		super.onAutoUpdate();
	}

	/**
	 * ���볡��
	 */
	protected final void c2d_addTostage()
	{
		onAddedToStage();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_AddToStage);
		}
	}

	/**
	 * �ӳ����Ƴ�
	 */
	protected final void c2d_removeFromStage()
	{
		onRemovedFromStage();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_RemovedFromStage);
		}
	}

	/**
	 * �Ƶ���̨���
	 */
	protected final void c2d_sentBack()
	{
		onSentBack();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_SentBack);
		}
	}

	/**
	 * �Ƶ���̨ǰ��
	 */
	protected final void c2d_sentTop()
	{
		onSentTop();
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_SentTop);
		}
	}

	/**
	 * ����
	 */
	protected final void c2d_hide()
	{
		onHidden();
		// ��������������¼�
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_Hidden);
		}
		m_painted = -1;
	}

	/**
	 * ��ʾ
	 */
	protected final void c2d_show()
	{
		onShown();
		// �����������ʾ�¼�
		if (m_Events_Scene != null)
		{
			m_Events_Scene.onCalled(SCENE_Shown);
		}
		layoutChanged();
	}

	/**
	 * ��鱻�ػ�
	 */
	protected void c2d_afterPaint()
	{
		if (m_painted == 0)
		{
			// ����������ػ�����¼�
			if (m_Events_Scene != null)
			{
				m_Events_Scene.onCalled(SCENE_Painted);
			}
			m_painted = 1;
		}
	}
	/**
	 * ����������ӵ���̨
	 */
	protected abstract void onAddedToStage();

	/**
	 * ������������̨���Ƴ�
	 */
	protected abstract void onRemovedFromStage();

	/**
	 * ���������ƶ���Ļ��ʱ���ã�����������������ʱ����
	 */
	protected abstract void onSentBack();

	/**
	 * ���������ƶ���ǰ̨ʱ���ã������������onAddToStage֮�� ���ã�Ҳ��������ص������󣬵����ϲ����ǳ����Ƴ�ʱ���á�
	 */
	protected abstract void onSentTop();

	/**
	 * ������̨����ʾ��onShow�����Ǹ���onAddToStageһ����֣� ������������ʵ�ԣ�����������������̨�ϳ���֮��Żᱻ���ã�����
	 * ���������г�����ѹջ���߳�ջʱ��������˷������������ã���ֻ�� ��������ѹջ��ջ�������֮����Զ������߼��У��Ż�ִ����������
	 * ʾ�ĳ�����onShown()��
	 */
	protected abstract void onShown();

	/**
	 * ������̨�����أ�onHide�����Ǹ���onAddToStageһ����֣� ������������ʵ�ԣ�����������������̨�ϳ���֮��Żᱻ���ã�����
	 * ���������г�����ѹջ���߳�ջʱ��������˷������������ã���ֻ�� ��������ѹջ��ջ�������֮����Զ������߼��У��Ż�ִ����������
	 * �صĳ�����onHidden()��
	 */
	protected abstract void onHidden();
	
	/**
	 * ����һֱˢ��
	 * 
	 * @param alwaysPaint
	 *            �Ƿ�һֱˢ��
	 */
	public final void setAlwaysPaint(boolean alwaysPaint)
	{
		m_alwaysRepaint = alwaysPaint;
	}

	public boolean beSkipped()
	{
		return false;
	}

	public String getBvrNodeName()
	{
		return "unnamed";
	}
}
