package c3d.util.math;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Math;

public class C3D_IOUtil extends C2D_IOUtil
{

	// ∂¡»Îfloat
	public static float readFloat(float data, DataInputStream dIn) throws Exception
	{
		int dataInt = 0;
		dataInt = readInt(dataInt, dIn);
		float fData = C2D_Math.intBitsToFloat(dataInt);
		return fData;
	}
}
