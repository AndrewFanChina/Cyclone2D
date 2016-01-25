package c2d.frame.base;


public class C2D_Dialog extends C2D_Scene
{
	
	private int m_dialogResult = -1;
	private boolean m_floated = false;

	public boolean isFloated()
	{
		return m_floated;
	}

	public void setFloated(boolean floated)
	{
		this.m_floated = floated;
	}

	public int getDialogResult()
	{
		return m_dialogResult;
	}

	public void setDialogResult(int dialogResult)
	{
		this.m_dialogResult = dialogResult;
	}

	public C2D_Dialog()
	{
	}

	protected void onAddedToStage()
	{
	}

	protected void onRemovedFromStage()
	{
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
		C2D_Dialog top = stage.currentDialog();
		if (!this.equals(top))
		{
			return false;
		}
		stage.popDialog();
		stage.releaseKeys();
		return true;
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
		if(m_atStage!=null)
		{
			m_pStack=m_atStage.m_dialogStack;	
		}
	}
	/**
	 * �Ƿ��Ƕ���Ի���
	 * @return �Ƿ��Ƕ���Ի���
	 */
	public boolean isTop()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null)
		{
			return false;
		}
		return this.equals(stage.currentDialog());
	}
	protected void onHidden()
	{

	}

	protected void onShown()
	{

	}

	protected void onSentBack()
	{
		// TODO Auto-generated method stub
		
	}

	protected void onSentTop()
	{
		// TODO Auto-generated method stub
		
	}
}
