package c2d.lang.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import c2d.lang.app.C2D_App;
import c2d.lang.util.debug.C2D_Debug;

/**
 * IO������
 */
public class C2D_IOUtil
{
	/** ��ǰ�������� */
	protected static int CON_COUNT = 0;
	/** ����ʧ��ʱ����ೢ�Դ��� */
	public static int MaxTryTime = 3;

	/**
	 * ����������
	 */
	public static void increaseCon()
	{
		CON_COUNT++;
		// C2D_Debug.log("[infor]","CON_COUNT:"+CON_COUNT);
	}

	/**
	 * ����������
	 */
	public static void decreaseCon()
	{
		CON_COUNT--;
		// C2D_Debug.log("[infor]","CON_COUNT:"+CON_COUNT);
	}

	// ��ȡ���############################################################################################
	/**
	 * ��ȡ������Դ
	 * 
	 * @param resName
	 *            the res name
	 * @return the data input stream
	 */
	public static final DataInputStream getDataInputStream(String resName)
	{
		DataInputStream dis = null;
		try
		{
			android.content.res.AssetManager am = C2D_App.getApp().getAssets();
			java.io.InputStream is = am.open(resName);
			if (is != null)
			{
				dis = new DataInputStream(is);
			}
		}
		catch (Exception e)
		{
			C2D_Debug.logException(e);
		}		
		return dis;
	}

	private static String FarUrl = null;

	/**
	 * ����Զ�̷�����·������������Դ��ȡʧ��ʱ�������Դ�Զ�̵�ַ��ȡ��
	 * 
	 * @param url
	 *            Զ�˷�����·��
	 */
	public static void setFarUrl(String url)
	{
		FarUrl = url;
	}

	public String getFarUrl()
	{
		return FarUrl;
	}

	/**
	 * ��ȡԶ�˷�������Դ�����ڵ�ǰ��FarUrl
	 * 
	 * @param url
	 *            ��Դ����
	 * @return ��ȡ������Դ
	 */
	public static byte[] readHttpData(String url)
	{
		HttpURLConnection httpCon = null;
		InputStream inS = null;
		int rc;
		rc = 0;
		byte[] data = null;
		try
		{
			do
			{
				C2D_IOUtil.increaseCon();
				URL urlCon = new URL(url);
				httpCon = (HttpURLConnection) urlCon.openConnection();
				if (httpCon == null)
				{
					break;
				}
				httpCon.setRequestMethod("GET");
				rc = httpCon.getResponseCode();
				if (rc != HttpURLConnection.HTTP_OK)
				{
					break;
				}
				inS = httpCon.getInputStream();
				int len = (int) httpCon.getContentLength();
				if (len > 0)
				{
					int actual = 0;
					int bytesread = 0;
					data = new byte[len];
					while ((bytesread != len) && (actual != -1))
					{
						actual = inS.read(data, bytesread, len - bytesread);
						bytesread += actual;
					}
				}
				else
				{
					data = C2D_IOUtil.readByBuffer(inS);
				}
			}
			while (false);
		}
		catch (Exception e)
		{
			C2D_Debug.logErr("<NET error>"+ url + " (" + e.getMessage() + ")");
			e.printStackTrace();
		}
		finally
		{
			if (inS != null)
			{
				try
				{
					inS.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				inS = null;
			}
			if (httpCon != null)
			{
				try
				{
					httpCon.disconnect();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				httpCon = null;
			}
			C2D_IOUtil.decreaseCon();
		}
		return data;
	}

	/**
	 * ��ȡ���ж�������Դ
	 * 
	 * @param resName
	 *            the res name
	 * @return the byte[]
	 */
	public static final byte[] readRes_Bytes(String resName)
	{
		byte[] bytes = readRes_Bytes(resName, 0, -1);
		if (bytes == null && FarUrl != null)
		{
			bytes = readHttpData("http://" + FarUrl + resName);
		}
		if (bytes == null)
		{
			C2D_Debug.log("��Error��in reading file :" + resName);
		}
		return bytes;
	}

	/**
	 * ��ȡ���ж�������Դ
	 * 
	 * @param resName
	 *            the res name
	 * @param skip
	 *            the skip
	 * @param len
	 *            the len
	 * @return the byte[]
	 */
	public static final byte[] readRes_Bytes(String resName, int skip, int len)
	{
		byte data[] = null;
		DataInputStream dis = null;
		try
		{
			dis = getDataInputStream(resName);
			if (dis == null)
			{
				return null;
			}
			if (skip > 0)
			{
				dis.skip((long) skip);
			}
			if (len <= 0)
			{
				try
				{
					len = dis.available();
					data = new byte[len];
					dis.read(data, 0, len);
				}
				catch (Exception e)
				{
					// C2D_Debug.log("can't get available");
				}
				if (data == null)
				{
					data = readByBuffer(dis);
				}
			}
		}
		catch (Exception e)
		{
			if (e != null)
			{
				C2D_Debug.log("exception :" + e.getMessage());
			}
		}
		finally
		{
			try
			{
				if (dis != null)
				{
					dis.close();
				}
				dis = null;
			}
			catch (Exception e)
			{
			}
		}
		return data;
	}

	/**
	 * ʹ�û����ȡָ���������е���������
	 * 
	 * @param dis
	 *            ������
	 * @return ��ȡ������
	 */
	public static byte[] readByBuffer(InputStream dis)
	{
		byte data[] = null;
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		try
		{
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			byte buf[] = new byte[1024];
			int lenRead = 0;
			do
			{
				lenRead = dis.read(buf, 0, buf.length);
				if (lenRead > 0)
				{
					dos.write(buf, 0, lenRead);
				}
			}
			while (lenRead >= 0);
			data = baos.toByteArray();
		}
		catch (Exception e)
		{
			C2D_Debug.log("reading exception,msg:" + e.getMessage());
		}
		finally
		{
			try
			{
				if (dos != null)
				{
					dos.close();
					dos = null;
				}
				if (baos != null)
				{
					baos.close();
					baos = null;
				}
			}
			catch (Exception e)
			{
			}
		}
		return data;
	}

	/**
	 * ��ȡbyte
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the byte
	 * @throws Exception
	 *             the exception
	 */
	public static final byte readByte(byte data, DataInputStream dIn) throws Exception
	{
		return (byte) (dIn.readByte() & 0xFF);
	}

	// ��ȡshort
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the short
	 * @throws Exception
	 *             the exception
	 */
	public static final short readShort(short data, DataInputStream dIn) throws Exception
	{
		return dIn.readShort();
	}

	// ��ȡint
	/**
	 * Read int.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	public static final int readInt(int data, DataInputStream dIn) throws Exception
	{
		return dIn.readInt();
	}
	// ��ȡһά����byte
	/**
	 * Read byte.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the byte[]
	 * @throws Exception
	 *             the exception
	 */
	public static final byte[] readByte(byte data[], DataInputStream dIn) throws Exception
	{
		int len = dIn.readShort();
		byte reData[] = data;
		if (reData == null)
		{
			reData = new byte[len];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readByte(reData[i], dIn); // ����
		}
		return reData;
	}

	// ��ȡһά����short
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the short[]
	 * @throws Exception
	 *             the exception
	 */
	public static final short[] readShort(short data[], DataInputStream dIn) throws Exception
	{
		int len = dIn.readShort();
		short reData[] = data;
		if (reData == null)
		{
			reData = new short[len];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = dIn.readShort(); // ����
		}
		return reData;
	}

	// ��ȡ��ά����short
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the short[][]
	 * @throws Exception
	 *             the exception
	 */
	public static final short[][] readShort(short data[][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readShort();
		short reData[][] = data;
		if (reData == null)
		{
			reData = new short[len][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readShort(reData[i], dIn);
		}
		return reData;
	}

	// ��ȡ��ά����short
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the short[][][]
	 * @throws Exception
	 *             the exception
	 */
	public static final short[][][] readShort(short data[][][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readShort();
		short reData[][][] = data;
		if (reData == null)
		{
			reData = new short[len][][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readShort(reData[i], dIn);
		}
		return reData;
	}

	// ��ȡ��ά����short
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the short[][][][]
	 * @throws Exception
	 *             the exception
	 */
	public static final short[][][][] readShort(short data[][][][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readShort();
		short reData[][][][] = data;
		if (reData == null)
		{
			reData = new short[len][][][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readShort(reData[i], dIn);
		}
		return reData;
	}

	// ��ȡһά����int
	/**
	 * Read int.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the int[]
	 * @throws Exception
	 *             the exception
	 */
	public static final int[] readInt(int data[], DataInputStream dIn) throws Exception
	{
		int len = dIn.readInt();
		int reData[] = data;
		if (reData == null)
		{
			reData = new int[len];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = dIn.readInt(); // ����
		}
		return reData;
	}

	// ��ȡ��ά����int
	/**
	 * Read int.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the int[][]
	 * @throws Exception
	 *             the exception
	 */
	public static final int[][] readInt(int data[][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readInt();
		int reData[][] = data;
		if (reData == null)
		{
			reData = new int[len][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readInt(reData[i], dIn);
		}
		return reData;
	}

	// ��ȡ��ά����int
	/**
	 * Read int.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the int[][][]
	 * @throws Exception
	 *             the exception
	 */
	public static final int[][][] readInt(int data[][][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readInt();
		int reData[][][] = data;
		if (reData == null)
		{
			reData = new int[len][][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readInt(reData[i], dIn);
		}
		return reData;
	}

	// ��ȡ��ά����int
	/**
	 * Read short.
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return the int[][][][]
	 * @throws Exception
	 *             the exception
	 */
	public static final int[][][][] readShort(int data[][][][], DataInputStream dIn) throws Exception
	{
		int len = dIn.readInt();
		int reData[][][][] = data;
		if (reData == null)
		{
			reData = new int[len][][][];
		}
		for (int i = 0; i < reData.length; i++)
		{
			reData[i] = readInt(reData[i], dIn);
		}
		return reData;
	}

	/**
	 * Gets the short data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the short data
	 */
	public static final short[][][][] getShortData(short data[][][][], String res)
	{
		short reData[][][][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readShort(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			// MiscUtil.log(e.getMessage());
		}
		return reData;
	}

	/**
	 * Gets the short data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the short data
	 */
	public static final short[][][] getShortData(short data[][][], String res)
	{
		short reData[][][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readShort(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			// MiscUtil.log(e.getMessage());
		}
		return reData;
	}

	/**
	 * Gets the short data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the short data
	 */
	public static final short[][] getShortData(short data[][], String res)
	{
		short reData[][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readShort(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			// MiscUtil.log("---------------------------------------------load "+res+" error"+e);
			e.printStackTrace();
		}
		return reData;
	}

	/**
	 * Gets the short data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the short data
	 */
	public static final short[] getShortData(short data[], String res)
	{
		short reData[] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readShort(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			C2D_Debug.log("load " + res + " error" + e);
		}
		return reData;
	}

	// ---------------------- int -------------------------------------
	/**
	 * Gets the int data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the int data
	 */
	public static final int[][][][] getIntData(int data[][][][], String res)
	{
		int reData[][][][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readShort(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			C2D_Debug.log("load " + res + " error" + e);
		}
		return reData;
	}

	/**
	 * Gets the int data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the int data
	 */
	public static final int[][][] getIntData(int data[][][], String res)
	{
		int reData[][][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readInt(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			C2D_Debug.log("load " + res + " error" + e);
		}
		return reData;
	}

	/**
	 * Gets the int data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the int data
	 */
	public static final int[][] getIntData(int data[][], String res)
	{
		int reData[][] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readInt(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			C2D_Debug.log("load " + res + " error" + e);
			e.printStackTrace();
		}
		return reData;
	}

	/**
	 * Gets the int data.
	 * 
	 * @param data
	 *            the data
	 * @param res
	 *            the res
	 * @return the int data
	 */
	public static final int[] getIntData(int data[], String res)
	{
		int reData[] = null;
		try
		{
			DataInputStream dataIn = getDataInputStream(res);
			reData = readInt(reData, dataIn);
			dataIn.close();
		}
		catch (Exception e)
		{
			C2D_Debug.log("load " + res + " error" + e);
		}
		return reData;
	}

	/**
	 * Read res_ ints.
	 * 
	 * @param resName
	 *            the res name
	 * @param skip
	 *            the skip
	 * @param len
	 *            the len
	 * @return the int[]
	 */
	public static final int[] readRes_Ints(String resName, int skip, int len)
	{
		int data[] = null;
		DataInputStream dis = null;
		try
		{
			dis = getDataInputStream(resName);
			if (skip > 0)
			{
				dis.skip((long) skip);
			}
			if (len <= 0)
			{
				len = dis.available();
			}
			data = new int[len];
			for (int i = 0; i < len; i++)
			{
				data[i] = dis.readInt();
			}
		}
		catch (Exception e)
		{
			C2D_Debug.log("readRes_Bytes error" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (dis != null)
				{
					dis.close();
				}
				dis = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * Read res_ ints.
	 * 
	 * @param resName
	 *            the res name
	 * @param skip
	 *            the skip
	 * @param data
	 *            the data
	 */
	public static final void readRes_Ints(String resName, int skip, int data[])
	{
		DataInputStream dis = null;
		int len = data.length;
		try
		{
			dis = getDataInputStream(resName);
			if (skip > 0)
			{
				dis.skip((long) skip);
			}
			for (int i = 0; i < len; i++)
			{
				data[i] = dis.readInt();
			}
		}
		catch (Exception e)
		{
			C2D_Debug.log("readRes_Bytes error" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (dis != null)
				{
					dis.close();
				}
				dis = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	// ---------------------------Boolean-------------------------------
	/**
	 * ��ȡBoolean����
	 * 
	 * @param data
	 *            the data
	 * @param dIn
	 *            the d in
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public static final boolean readBoolean(boolean data, DataInputStream dIn) throws Exception
	{
		byte reData = 0;
		reData = readByte(reData, dIn);
		return reData != 0;
	}

	// ---------------------------String-------------------------------
	/**
	 * Read string.
	 * 
	 * @param s
	 *            the s
	 * @param dIn
	 *            the d in
	 * @return the string
	 */
	public static final String readString(String s, DataInputStream dIn)
	{
		String sRead = null;
		try
		{
			if (dIn != null)
			{
				sRead = dIn.readUTF();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			C2D_Debug.log(e.getMessage());
		}
		return sRead;
	}

	/* ���ļ��ж�ȡ�ַ����� */
	/**
	 * Read string array.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the string[]
	 */
	public static final String[] readStringArray(String fileName)
	{
		int i, nbStrings;
		DataInputStream data = null;
		String[] strings = null;
		try
		{
			data = C2D_IOUtil.getDataInputStream(fileName);
			nbStrings = data.readUnsignedShort();
			strings = new String[nbStrings];
			for (i = 0; i < nbStrings; i++)
			{
				strings[i] = data.readUTF();
			}
		}
		catch (Exception e)
		{
			C2D_Debug.log("Exception in getStringArray : " + e);
			strings = null;
		}
		finally
		{
			try
			{
				data.close();
			}
			catch (Exception e)
			{
			}
		}
		return strings;
	}

	// ������############################################################################################
	// ���short
	/**
	 * Write short.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeShort(short data, DataOutputStream dos) throws Exception
	{
		dos.writeShort(data);
	}

	// ���һά����short
	/**
	 * Write short.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeShort(short data[], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeShort(data[i], dos);
		}
	}

	// �����ά����short
	/**
	 * Write short.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeShort(short data[][], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeShort(data[i], dos);
		}
	}

	// �����ά����short
	/**
	 * Write short.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeShort(short data[][][], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeShort(data[i], dos);
		}
	}

	// ���byte
	/**
	 * Write byte.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeByte(byte data, DataOutputStream dos) throws Exception
	{
		dos.writeByte(data);
	}

	// ���һά����byte
	/**
	 * Write byte.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeByte(byte data[], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeByte(data[i], dos);
		}
	}

	// �����ά����byte
	/**
	 * Write byte.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeByte(byte data[][], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeByte(data[i], dos);
		}
	}

	// �����ά����byte
	/**
	 * Write byte.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeByte(byte data[][][], DataOutputStream dos) throws Exception
	{
		writeShort((short) data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeByte(data[i], dos);
		}
	}

	// ���int
	/**
	 * Write int.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeInt(int data, DataOutputStream dos) throws Exception
	{
		dos.writeInt(data);
	}

	// ���һά����int
	/**
	 * Write int.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeInt(int data[], DataOutputStream dos) throws Exception
	{
		writeInt(data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeInt(data[i], dos);
		}
	}

	// �����ά����int
	/**
	 * Write int.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeInt(int data[][], DataOutputStream dos) throws Exception
	{
		writeInt(data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeInt(data[i], dos);
		}
	}

	// �����ά����int
	/**
	 * Write int.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeInt(int data[][][], DataOutputStream dos) throws Exception
	{
		writeInt(data.length, dos);
		for (int i = 0; i < data.length; i++)
		{
			writeInt(data[i], dos);
		}
	}

	/**
	 * Write string.
	 * 
	 * @param s
	 *            the s
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeString(String s, DataOutputStream dos) throws Exception
	{
		dos.writeUTF(s);
	}

	/**
	 * Write boolean.
	 * 
	 * @param data
	 *            the data
	 * @param dos
	 *            the dos
	 * @throws Exception
	 *             the exception
	 */
	public static final void writeBoolean(boolean data, DataOutputStream dos) throws Exception
	{
		byte resData = (byte) (data ? 1 : 0);
		dos.writeByte(resData);
	}

	// ��������############################################################################################
	/**
	 * ������ݻ���.
	 * 
	 * @param content
	 *            ��Ҫ������������ֽ�����
	 */
	public static final void deConfuse(byte content[])
	{
		for (int i = 0; i < content.length; i++)
		{
			content[i] ^= 0xFF;
		}
	}
}
