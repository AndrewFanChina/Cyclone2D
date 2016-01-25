package c2d.frame.event;

import c2d.frame.base.C2D_Stage;

public abstract class C2D_Event_Stage extends C2D_Event
{
	/**
	 * 事件执行函数
	 * @param carrier 事件载体，这里是一个舞台对象
	 * @param sceneState 舞台的状态标志
	 * @return 该事件是否完结，即不再需要响应
	 */
	public abstract boolean doEvent(C2D_Stage carrier,int stageState);

}
