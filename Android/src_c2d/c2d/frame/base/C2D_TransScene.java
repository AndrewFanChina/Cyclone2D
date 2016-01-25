package c2d.frame.base;

import c2d.frame.ext.scene.C2D_SceneUtil;

public abstract class C2D_TransScene extends C2D_SceneUtil
{
	public abstract void onProcessChanged(int process);
	protected abstract void onLoadBegin();
	protected abstract void onLoadEnd();
}
