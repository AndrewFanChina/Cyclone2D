package c2d.frame.util;

import c2d.frame.base.C2D_Scene;
import c2d.lang.math.C2D_Array;
/**
 * 场景追踪器，用于在场景被创建时的追踪
 * @author AndrewFan
 *
 */
public class C2D_SceneTracker
{
	private static boolean M_Enable = false;
	private static C2D_Array M_Array;

	public static void Enable()
	{
		M_Enable = true;
		if (M_Array == null)
		{
			M_Array = new C2D_Array();
		}
	}

	public static boolean isEnable()
	{
		return M_Enable;
	}

	public static void Disable()
	{
		M_Enable = false;
		if (M_Array != null)
		{
			M_Array.clear();
			M_Array = null;
		}
	}

	public static void TrackScene(C2D_Scene scene)
	{
		if(!M_Enable)
		{
			return;
		}
		M_Array.addElement(scene);
	}

	public static C2D_Array GetTrackedScenes()
	{
		return M_Array;
	}
}
