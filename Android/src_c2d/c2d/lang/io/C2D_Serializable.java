package c2d.lang.io;


public interface C2D_Serializable
{
	/**
	 * ����¼���ݴ��л������ת�����ַ�������¼����+�ָ���+��¼���ݡ�
	 * 
	 * @return ���л��ַ���
	 */
	public String serializeOut();

	/**
	 * �����ݴ��л����룬ת�������ü�¼��������¼����
	 * 
	 * @return �Ƿ�����ɹ�
	 */
	public boolean serializeIn(String data);

	/**
	 * д��������ֵ
	 * 
	 * @param value
	 *            ������ֵ
	 */
	public void writeInt(int value);

	/**
	 * ��ȡ������ֵ�������ȡʧ�ܣ��᷵��-1���������쳣��Ϣ
	 * 
	 * @return ������ֵ
	 */
	public int readInt();

	/**
	 * д��������ֵ����
	 * 
	 * @param value
	 *            ������ֵ����
	 */
	public void writeInts(int value[]);

	/**
	 * ��ȡ������ֵ����
	 * 
	 * @return ��ȡ������ֵ
	 */
	public int[] readInts();

	/**
	 * д����������ֵ
	 * 
	 * @param value
	 *            ��������ֵ
	 */
	public void writeLong(long value);

	/**
	 * ��ȡ��������ֵ�������ȡʧ�ܣ��᷵��-1���������쳣��Ϣ
	 * 
	 * @return ��������ֵ
	 */
	public long readLong();

	/**
	 * д����������ֵ����
	 * 
	 * @param value
	 *            ��������ֵ����
	 */
	public void writeLongs(long value[]);

	/**
	 * ��ȡ��������ֵ����
	 * 
	 * @return ��ȡ������ֵ
	 */
	public long[] readLongs();

	/**
	 * д����������ֵ
	 * 
	 * @param value
	 *            ��������ֵ
	 */
	public void writeFloat(float value);

	/**
	 * ��ȡ��������ֵ�������ȡʧ�ܣ��᷵��-1���������쳣��Ϣ
	 * 
	 * @return ��������ֵ
	 */
	public float readFloat();

	/**
	 * д����������ֵ����
	 * 
	 * @param value
	 *            ��������ֵ����
	 */
	public void writeFloats(float value[]);

	/**
	 * ��ȡ��������ֵ����
	 * 
	 * @return ��ȡ������ֵ
	 */
	public float[] readFloats();

	/**
	 * д��������ֵ
	 * 
	 * @param value
	 *            ������ֵ
	 */
	public void writeBoolean(boolean value);

	/**
	 * ��ȡ������ֵ
	 * 
	 * @return ��ȡ������ֵ
	 */
	public boolean readBoolean();

	/**
	 * д����������
	 * 
	 * @param value
	 *            ��������
	 */
	public void writeBooleans(boolean value[]);

	/**
	 * ��ȡ������ֵ����
	 * 
	 * @return ��ȡ������ֵ
	 */
	public boolean[] readBooleans();

	/**
	 * д���ַ�����ֵ
	 * 
	 * @param value
	 *            �ַ�����ֵ
	 */
	public void writeString(String value);

	/**
	 * ��ȡ�ַ�����ֵ�������ȡʧ�ܣ��᷵��null
	 * 
	 * @return ������ֵ
	 */
	public String readString();

	/**
	 * д���ַ�������ֵ����
	 * 
	 * @param value
	 *            �ַ�����ֵ����
	 */
	public void writeStrings(String value[]);

	/**
	 * ��ȡ�ַ�����ֵ����
	 * 
	 * @return ��ȡ������ֵ
	 */
	public String[] readStrings();

	/**
	 * �Ƿ������ݿ��Զ�ȡ
	 * 
	 * @return �Ƿ������ݿ��Զ�ȡ
	 */
	public boolean canRead();

	/**
	 * ���Զ�ȡͷ�����Ƿ���ָ�����ַ�����
	 * 
	 * @return ��ȡ�����ַ���
	 */
	public String tryReadHeadData();

	/**
	 * ���ö�ȡλ�á�������ȡλ�ù��㣬��ͷ��ʼ��ȡ�� �������Ҫ��ζ�ȡͬһ����¼�Ļ�����Ҫʹ�õ����������
	 */
	public void resetReadPos();

	/**
	 * ����������ݣ������ö�ȡλ�á�
	 */
	public void clearContent();
}
