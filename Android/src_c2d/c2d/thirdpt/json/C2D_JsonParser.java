package c2d.thirdpt.json;

import c2d.lang.util.debug.C2D_Debug;
import c2d.thirdpt.json.org.JSONObject;

/**
 * Json处理类
 * @author AndrewFan
 *
 */
public class C2D_JsonParser
{

	/**
	 * 从字符串构建json对象
	 * 
	 * @param json
	 *            json字符串
	 * @return json对象
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
	 * 从json中获得指定的数值，转换成整形返回
	 * 
	 * @param json
	 *            json字符串
	 * @param key
	 *            键
	 * @return 对应key键的值
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
	 * 从json中获得跟指定键对应的字符串值并返回
	 * 
	 * @param json
	 *            json字符串
	 * @param key
	 *            键
	 * @return 对应key键的值
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
