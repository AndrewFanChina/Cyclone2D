package c2d.lang.io;


public interface C2D_Serializable
{
	/**
	 * 将记录数据串行化输出，转换成字符串【记录描述+分隔符+记录内容】
	 * 
	 * @return 串行化字符串
	 */
	public String serializeOut();

	/**
	 * 将数据串行化输入，转换并设置记录描述、记录内容
	 * 
	 * @return 是否输入成功
	 */
	public boolean serializeIn(String data);

	/**
	 * 写出整形数值
	 * 
	 * @param value
	 *            整形数值
	 */
	public void writeInt(int value);

	/**
	 * 读取整形数值，如果读取失败，会返回-1，并产生异常信息
	 * 
	 * @return 整形数值
	 */
	public int readInt();

	/**
	 * 写出整形数值数组
	 * 
	 * @param value
	 *            整形数值数组
	 */
	public void writeInts(int value[]);

	/**
	 * 读取整形数值数组
	 * 
	 * @return 读取到的数值
	 */
	public int[] readInts();

	/**
	 * 写出长整形数值
	 * 
	 * @param value
	 *            长整形数值
	 */
	public void writeLong(long value);

	/**
	 * 读取长整形数值，如果读取失败，会返回-1，并产生异常信息
	 * 
	 * @return 长整形数值
	 */
	public long readLong();

	/**
	 * 写出长整形数值数组
	 * 
	 * @param value
	 *            长整形数值数组
	 */
	public void writeLongs(long value[]);

	/**
	 * 读取长整形数值数组
	 * 
	 * @return 读取到的数值
	 */
	public long[] readLongs();

	/**
	 * 写出浮点型数值
	 * 
	 * @param value
	 *            浮点型数值
	 */
	public void writeFloat(float value);

	/**
	 * 读取浮点型数值，如果读取失败，会返回-1，并产生异常信息
	 * 
	 * @return 浮点型数值
	 */
	public float readFloat();

	/**
	 * 写出浮点型数值数组
	 * 
	 * @param value
	 *            浮点型数值数组
	 */
	public void writeFloats(float value[]);

	/**
	 * 读取浮点型数值数组
	 * 
	 * @return 读取到的数值
	 */
	public float[] readFloats();

	/**
	 * 写出布尔数值
	 * 
	 * @param value
	 *            布尔数值
	 */
	public void writeBoolean(boolean value);

	/**
	 * 读取布尔数值
	 * 
	 * @return 读取到的数值
	 */
	public boolean readBoolean();

	/**
	 * 写出布尔数组
	 * 
	 * @param value
	 *            布尔数组
	 */
	public void writeBooleans(boolean value[]);

	/**
	 * 读取布尔数值数组
	 * 
	 * @return 读取到的数值
	 */
	public boolean[] readBooleans();

	/**
	 * 写出字符串数值
	 * 
	 * @param value
	 *            字符串数值
	 */
	public void writeString(String value);

	/**
	 * 读取字符串数值，如果读取失败，会返回null
	 * 
	 * @return 整形数值
	 */
	public String readString();

	/**
	 * 写出字符串形数值数组
	 * 
	 * @param value
	 *            字符串数值数组
	 */
	public void writeStrings(String value[]);

	/**
	 * 读取字符串数值数组
	 * 
	 * @return 读取到的数值
	 */
	public String[] readStrings();

	/**
	 * 是否还有数据可以读取
	 * 
	 * @return 是否还有数据可以读取
	 */
	public boolean canRead();

	/**
	 * 尝试读取头数据是否是指定的字符串，
	 * 
	 * @return 读取到的字符串
	 */
	public String tryReadHeadData();

	/**
	 * 重置读取位置。即将读取位置归零，从头开始读取。 如果你需要多次读取同一个记录的话，需要使用到这个函数。
	 */
	public void resetReadPos();

	/**
	 * 清空内容数据，并重置读取位置。
	 */
	public void clearContent();
}
