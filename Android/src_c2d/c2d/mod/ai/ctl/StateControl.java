package c2d.mod.ai.ctl;
/**
 * ¼òµ¥µÄ×´Ì¬¿ØÖÆÆ÷
 * @author AndrewFan
 *
 */
public class StateControl
{
	private int m_gameResult = 0;
	
	public int getState()
	{
		return m_gameResult;
	}
	
	public boolean atState(int state)
	{
		return m_gameResult==state;
	}
	
	public void setState(int state)
	{
		m_gameResult=state;
	}
	
}
