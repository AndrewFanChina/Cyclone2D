package c2d.frame.base;

import c2d.lang.math.C2D_Stack;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_SceneStack extends C2D_Object
{
	/** ��ǰ��̨ */
	protected C2D_Stage m_stage;
	/** ջ�б�*/
	C2D_Stack m_stack;
	/**
	 * ���캯��
	 * 
	 * @param stage
	 *            ��ǰ��̨
	 */
	public C2D_SceneStack(C2D_Stage stage)
	{
		m_stage = stage;
	}
	/**
	 * ����ǰ�Ķ��������л���ָ���ĳ������൱�����Ƴ�����ǰ�Ķ��㳡����
	 * ����ִ�дμ�������onSentTop�����������̼���ָ���ĳ������μ�
	 * pushScene��popScene�������������൱��һ��popScene��
	 * pushScene�ĵ���Ч���������м�ȥ���˴μ�������onSentTop
	 * ��onSentBack������
	 * 
	 * @param destScene
	 *            Ŀ�곡��
	 * @param transScene
	 *            ���ɳ���
	 */
	public void switchScene(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if (destScene == null)
		{
			return;
		}
		if(m_stack==null)
		{
			m_stack=new C2D_Stack();
		}
		// �Ƴ������ĳ���
		C2D_Scene current = currentScene();
		if (current != null)
		{
			m_stack.pop();
			current.c2d_removeFromStage();
			current.setStage(null);
		}
		doPush(destScene, transScene);

	}
	/**
	 * ִ��Ŀ�곡����ѹջ�ͼ���
	 * @param destScene Ŀ�곡��
	 * @param transScene ���ɳ���
	 */
	private void doPush(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if(m_stage==null)
		{
			return;
		}
		if(m_stack==null)
		{
			m_stack=new C2D_Stack();
		}
		// ��¼���ɳ���(ʵ����û��ѹջ����)
		if (transScene != null)
		{
			destScene.registerLoder(transScene);
			transScene.onLoadBegin();
			transScene.afterPushStage(m_stage);
		}
		// ѹ���³���
		m_stack.push(destScene);
		//��ʼ����
		if (transScene == null)
		{
			destScene.afterPushStage(m_stage);
			m_stage.repaintOnce();
		}
		else
		{
			destScene.readyLoading();
			new Thread(new Runnable()
			{
				public void run()
				{
					destScene.afterPushStage(m_stage);
					m_stage.repaintOnce();
					destScene.endLoading();
					transScene.onLoadEnd();
				}
			}).start();
		}
	}
	/**
	 * ��ָ����Ŀ�곡��ѹ��ջ���൱�ڻὫ�����ᵽ��̨��
	 * ����ʱ��ָ������������̨��ͬ�ĳߴ硣���Ŀǰ�Ѿ����ڳ���������ִ���Ѿ���
	 * �ڳ�����onSentBack��������ִ��Ŀ�곡����onSentTop����������ִ��
	 * Ŀ�곡����onAddToStage��������󣬱���ѭ���������������ѭ��ȷ��ĳ
	 * Щ���������˱仯������ִ�б����صĳ�����onHidden��������ִ�е�ǰ����
	 * ������onShown������
	 * @param scene
	 *            ��������
	 */
	public void pushScene(C2D_Scene scene)
	{
		pushScene(scene,null);
	}

	/**
	 * ��ָ����Ŀ�곡��ѹ��ջ���൱�ڻὫ�����ᵽ��̨��
	 * ����ʱ��ָ������������̨��ͬ�ĳߴ硣���Ŀǰ�Ѿ����ڳ���������ִ���Ѿ���
	 * �ڳ�����onSentBack��������ִ��Ŀ�곡����onSentTop����������ִ��
	 * Ŀ�곡����onAddToStage��������󣬱���ѭ���������������ѭ��ȷ��ĳ
	 * Щ���������˱仯������ִ�б����صĳ�����onHidden��������ִ�е�ǰ����
	 * ������onShown������
	 * ���ͨ�����ɳ���ִ�м��أ����ɳ�����Ŀ�곡���������֮���Զ��ᱻ�Ƴ���ʾ��
	 * 
	 * @param destScene
	 *            Ŀ�곡��
	 * @param transScene
	 *            ���ɳ���
	 */
	public void pushScene(final C2D_Scene destScene, final C2D_TransScene transScene)
	{
		if (destScene == null)
		{
			return;
		}
		// ���ص�ǰ����
		C2D_Scene current = currentScene();
		if(current!=null)
		{
			current.c2d_sentBack();
		}
		doPush(destScene, transScene);
	}

	/**
	 * ������ǰ����
	 */
	public void popScene()
	{
		C2D_Scene scene = currentScene();
		if (scene == null||m_stack==null||m_stage==null)
		{
			return;
		}
		m_stack.pop();
		// �Ƴ������ĳ���
		scene.c2d_removeFromStage();
		scene.setStage(null);
		// ��ʾ�����ǵĳ���
		scene = currentScene();
		if (scene!=null)
		{
			scene.c2d_sentTop();
			scene.layoutChanged();
		}
		m_stage.repaintOnce();
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return ��������
	 */
	public C2D_Scene currentScene()
	{
		if(m_stack==null)
		{
			return null;
		}
		return (C2D_Scene) m_stack.peek();
	}

	/**
	 * ���Ƴ����ڵ�
	 */
	protected void onPaint_C2D(C2D_Graphics g)
	{
		C2D_Scene scene = currentScene();
		if (scene == null)
		{
			return;
		}
		scene.onPaint(g);
	}

	/**
	 * ���и��£��������Ƿ�Ҫ���κ��������߼�
	 * 
	 * @return �Ƿ���Ҫ���κ��������߼�
	 */
	public boolean onUpdate()
	{
		C2D_Scene scene = currentScene();
		if (scene != null)
		{
			if (!scene.onLoadingUpdate())
			{
				scene.onUpdate(m_stage);
			}
		}
		return false;
	}
	/**
	 * �Զ�����
	 */
	public void onAutoUpdate()
	{
		C2D_Scene current = currentScene();
		if (current != null)
		{
			current.onAutoUpdate();
		}
	}
	/**
	 * ִ���ͷ�
	 */
	public void onRelease()
	{
		popAll();
		m_stack=null;
	}

	public int getSize()
	{
		if(m_stack==null)
		{
			return 0;
		}
		return  m_stack.getSize();
	}
	/**
	 * �鿴�Ƿ�Ϊ��
	 * @return
	 */
	public boolean isEmpty()
	{
		if(m_stack==null)
		{
			return true;
		}
		return  m_stack.isEmpty();
	}
	/**
	 * �������г���
	 */
	public void popAll()
	{
		if(m_stack!=null)
		{
			int sc = m_stack.getSize();
			for (int i = 0; i < sc; i++)
			{
				popScene();
			}
			m_stack.clear();	
		}
	}


}
