package c2d.thirdpt.json;

import c2d.lang.util.debug.C2D_Debug;
import c2d.thirdpt.json.org.JSONObject;

/**
 * Json������
 * @author AndrewFan
 *
 */
public class C2D_JsonParser
{

	/**
	 * ���ַ�������json����
	 * 
	 * @param json
	 *            json�ַ���
	 * @return json����
	 */
	public static JSONObject parseJson(String json)
	{
		JSONObject obj = null;
		try
		{
			obj = new JSONObject(json);
		}
		catch (Exception e)
		{
			C2D_Debug.logWarn(" parseJson:" + json + "," + e.getMessage());
		}
		return obj;
	}

	/**
	 * ��json�л��ָ������ֵ��ת�������η���
	 * 
	 * @param json
	 *            json�ַ���
	 * @param key
	 *            ��
	 * @return ��Ӧkey����ֵ
	 */
	public static int parseJson_Int(String json, String key)
	{
		int result = -1;
		try
		{
			JSONObject obj = new JSONObject(json);
			result = Integer.parseInt(obj.getString(key));
		}
		catch (Exception e)
		{
			C2D_Debug.logWarn(" parseJson_Int:" + json + "," + e.getMessage());
		}
		return result;
	}

	/**
	 * ��json�л�ø�ָ������Ӧ���ַ���ֵ������
	 * 
	 * @param json
	 *            json�ַ���
	 * @param key
	 *            ��
	 * @return ��Ӧkey����ֵ
	 */
	public static String parseJson_String(String json, String key)
	{
		String result = null;
		try
		{
			JSONObject obj = new JSONObject(json);
			result = obj.getString(key);
		}
		catch (Exception e)
		{
			C2D_Debug.logWarn(" parseJson_String:" + json + "," + e.getMessage());
		}
		return result;
	}

}
