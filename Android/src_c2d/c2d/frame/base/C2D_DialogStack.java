package c2d.frame.base;

import c2d.plat.gfx.C2D_Graphics;


public class C2D_DialogStack extends C2D_SceneStack
{
	public C2D_DialogStack(C2D_Stage stage)
	{
		super(stage);
	}
	/**
	 * ���и��£��������Ƿ�Ҫ���κ��������߼�
	 * @return �Ƿ���Ҫ���κ��������߼�
	 */
	public boolean onUpdate()
	{
		C2D_Scene scene = currentScene();
		if (scene != null)
		{
			if(!scene.onLoadingUpdate())
			{
				scene.onUpdate(m_stage);
				if(scene instanceof C2D_Dialog)
				{
					return !((C2D_Dialog)scene).isFloated();
				}
			}
		}
		return false;
	}
	/**
	 * ���Ƴ����ڵ�
	 */
	protected final void onPaint_C2D(C2D_Graphics g)
	{
		C2D_Scene scene = currentScene();
		if (scene == null)
		{
			return;
		}
		scene.onPaint(g);
	}
}
