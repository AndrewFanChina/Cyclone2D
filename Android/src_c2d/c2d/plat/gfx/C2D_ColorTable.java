package c2d.plat.gfx;

import java.io.DataInputStream;
import java.util.Hashtable;

import c2d.lang.io.C2D_IOUtil;


/**
 * <p>
 * Title:调色板映射表类
 * </p>
 * <p>
 * Copyright: Copyright (c) AndrewFan
 * </p>.
 *
 * @author AndrewFan
 */
class C2D_ColorTable
{
	/** 颜色表 */
	private Hashtable hashColor=new Hashtable();
	
	/**
	 * 创建调色板映射表
	 *
	 * @param resName the res name
	 */
	C2D_ColorTable(String resName)
	{
		DataInputStream dis = null;
		try
		{
			dis = C2D_IOUtil.getDataInputStream(resName);
			int datas[][]=new int[2][];
			short len = 0;
			for(int di=0;di<datas.length;di++)
			{
				len = C2D_IOUtil.readShort(len, dis);
				datas[di]=new int[len];
				for (int i = 0; i < len; i++)
				{
					datas[di][i] = C2D_IOUtil.readInt(datas[di][i], dis);
				}
			}
			for(int di=0;di<datas[0].length;di++)
			{
				hashColor.put(new Integer(datas[0][di]), new Integer(datas[1][di]));
//				System.out.println(Integer.toHexString(datas[0][di])+"-----"+Integer.toHexString(datas[1][di]));
			}

		}
		catch (Exception e)
		{
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
	}

	/**
	 * 返回颜色长度
	 *
	 * @return 颜色个数
	 */
	public int getColorCount()
	{
		return hashColor.size();
	}

	/**
	 * 释放资源
	 *
	 */
	void doRelease()
	{
		if (hashColor != null)
		{
			hashColor.clear();
			hashColor = null;
		}

	}

	/**
	 * 改变PNG图片数据中的调色板
	 *
	 * @param png数据
	 */
	void changePngPalette(byte[] pngData)
	{
		byte[] palData = getPngPal(pngData);
		if(palData!=null)
		{
			applyPmt(palData);
			changeColor(pngData, palData);	
		}
	}

	/**
	 * 从PNG图片数据获得调色板(包括数据块和CRC，PLTE前面的数据长度是指包含3字节一个颜色的数据部分长度，如果N种颜色，数据长度是N*3，在运算时要加上4)
	 *
	 * @param pngData the png data
	 * @return the png pal
	 */
	private static byte[] getPngPal(byte[] pngData)
	{
		int startIndex = 0;
		for (int i = 0; i < pngData.length - 8; i++)
		{
			if (pngData[i] == 'P' && pngData[i + 1] == 'L' && pngData[i + 2] == 'T' && pngData[i + 3] == 'E')
			{
				startIndex = i;
				break;
			}
		}
		if (startIndex == 0)
			return null;
		int palDataLength = (pngData[startIndex - 4] & 0xff) << 24 | (pngData[startIndex - 3] & 0xff) << 16 | (pngData[startIndex - 2] & 0xff) << 8 | (pngData[startIndex - 1] & 0xff) + 4;
		byte[] palData = new byte[palDataLength];
		for (int i = 0; i < palDataLength; i++)
		{
			palData[i] = pngData[startIndex + 4 + i];
		}
		return palData;
	}
	/**
	 * 设置声音音量
	 *
	 * @param palData the pal data
	 */
	private void applyPmt(byte[] palData)
	{
		int colorJ;
		int palLen = (palData.length - 4) / 3;
		for (int j = 0; j < palLen; j++)
		{
			colorJ = (0xFF << 24) | ((palData[j * 3] & 0xFF) << 16) | ((palData[j * 3 + 1] & 0xFF) << 8) | (palData[j * 3 + 2] & 0xFF);
			Integer colorSrc=new Integer(colorJ);
			Object colorDest = hashColor.get(colorSrc);
			if(colorDest!=null)
			{
				int mappedColor=((Integer)colorDest).intValue();
				palData[j * 3] = (byte) ((mappedColor >> 16) & 0xFF);
				palData[j * 3 + 1] = (byte) ((mappedColor >> 8) & 0xFF);
				palData[j * 3 + 2] = (byte) ((mappedColor) & 0xFF);
			}
		}
		updatePalCrc(palData);
	}

	/**
	 * 更新CRC
	 *
	 * @param data the data
	 */
	private static void updatePalCrc(byte[] data)
	{
		int colorLength = data.length - 4;
		byte[] palData = new byte[data.length];

		palData[0] = (byte) 'P';
		palData[1] = (byte) 'L';
		palData[2] = (byte) 'T';
		palData[3] = (byte) 'E';

		for (int i = 0; i < colorLength; i++)
		{
			palData[i + 4] = data[i];
		}

		long value = 0xffffffffL;

		if (crcTable == null)
			makeCrcTable();

		for (int i = 0; i < palData.length; i++)
		{
			value = crcTable[(int) ((value ^ palData[i]) & 0xff)] ^ (value >> 8);
		}
		int crc = (int) (value ^ 0xffffffffL);
		data[colorLength] = (byte) ((crc >> 24) & 0xff);
		data[colorLength + 1] = (byte) ((crc >> 16) & 0xff);
		data[colorLength + 2] = (byte) ((crc >> 8) & 0xff);
		data[colorLength + 3] = (byte) (crc & 0xff);
	}

	/** crc表 */
	private static long[] crcTable;

	/**
	 * 生成CRC表
	 */
	private static void makeCrcTable()
	{
		crcTable = new long[256];
		long value;

		for (int i = 0; i < 256; i++)
		{
			value = (long) i;

			for (int j = 0; j < 8; j++)
			{
				if ((value & 1) != 0)
				{
					value = 0xedb88320L ^ (value >> 1);
				}
				else
				{
					value = value >> 1;
				}
			}
			crcTable[i] = value;
		}
	}

	/**
	 * 改变PNG图片中的调色板信息
	 *
	 * @param png图片数据
	 * @param pal调色板数据
	 */
	private static void changeColor(byte pngData[], byte palData[])
	{
		int i = 0, j = 0;
		int dataSize = pngData.length;
		do
		{
			if (j >= dataSize)
			{
				break;
			}
			if (pngData[j] == 'P' && pngData[j + 1] == 'L' && pngData[j + 2] == 'T' && pngData[j + 3] == 'E')
			{
				i = j;
				break;
			}
			j++;
		}
		while (true);
		if (i == 0)
		{
			return;
		}
		j = (pngData[i - 4] & 0xff) << 24 | (pngData[i - 3] & 0xff) << 16 | (pngData[i - 2] & 0xff) << 8 | (pngData[i - 1] & 0xff) + 4;
		for (int k = 0; k < j; k++)
		{
			pngData[i + 4 + k] = palData[k];
		}
	}

}
