package c2d.lang.io;


/**
 * ����������
 */
public class C2D_HttpConnector
{
	/**
	 * ���캯��
	 */
	public C2D_HttpConnector()
	{
	}

	/**
	 * ��Get��ʽ�첽��ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param getUrl
	 *            ��ȡ��·��
	 * @param receiver
	 *            ��Ϣ������
	 * @param requestCode
	 *            ��������
	 */
	public void requestMsgAsynGet(final String getUrl,
			final C2D_HttpMsgReceiver receiver, final int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgAsyn(getUrl, new C2D_HttpPkgReceiver()
		{
			public void receiveData(byte[] pkgData,String err)
			{
				if(receiver!=null)
				{
					receiver.receiveMsg(new C2D_HttpMessage(requestCode,pkgData,err));
				}
			}
		},"GET");
	}
	/**
	 * ��Get��ʽ�첽��ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param getUrl
	 *            ��ȡ��·��
	 * @param receiver
	 *            ��Ϣ������
	 * @param requestCode
	 *            ��������
	 */
	public void requestMsgAsynGet(final String getUrl, final String getContent,
			final C2D_HttpMsgReceiver receiver, final int requestCode)
	{
		requestMsgAsynGet(getUrl+"?"+getContent, receiver,requestCode);
	}

	/**
	 * ��Post��ʽ�������ݣ����첽��ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param postUrl
	 *            ���ӵ�·��
	 * @param content
	 *            ���͵�����
	 * @param receiver
	 *            ��Ϣ������
	 * @param requestCode
	 *            ��������
	 */
	public void requestMsgAsynPost(final String postUrl, final String content,
			final C2D_HttpMsgReceiver receiver, final int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgAsynPost(postUrl,content, new C2D_HttpPkgReceiver()
		{
			public void receiveData(byte[] pkgData,String err)
			{
				if(receiver!=null)
				{
					receiver.receiveMsg(new C2D_HttpMessage(requestCode,pkgData,err));
				}
			}
		});
	}

	/**
	 * ��Get��ʽͬ����������ֱ����ɣ�������ִ��ʱ�����͵�ǰ�̣߳�ֱ����ȡ��ɲŻ᷵�ء�
	 * 
	 * @param getUrl
	 *            ·��
	 * @param requestCode
	 *            ��������
	 * @return C2D_HttpMessage ��Ϣ
	 */
	public C2D_HttpMessage requestMsgSynGet(String getUrl,int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgSynGet(getUrl);
		return new C2D_HttpMessage(requestCode,pkg.getLoadData());
	}
	/**
	 * ��Get��ʽͬ����������ֱ����ɣ�������ִ��ʱ�����͵�ǰ�̣߳�ֱ����ȡ��ɲŻ᷵�ء�
	 * 
	 * @param getUrl
	 *            ·��
	 * @param getContent
	 * 			  ����
	 * @param requestCode
	 *            ��������
	 * @return C2D_HttpMessage ��Ϣ
	 */
	public C2D_HttpMessage requestMsgSynGet(String getUrl, String getContent,int requestCode)
	{
		return requestMsgSynGet(getUrl+"?"+getContent,requestCode);
	}

	/**
	 * ��Post��ʽ�������ݣ���ͬ����������ֱ����ɣ�������ִ��ʱ�����͵�ǰ�̣߳�ֱ����ȡ��ɲŻ᷵�ء�
	 * 
	 * @param postUrl
	 *            ���ӵ�·��
	 * @param content
	 *            ���͵�����
	 * @param requestCode
	 *            ��������
	 * @return C2D_HttpMessage ��Ϣ
	 */
	public C2D_HttpMessage requestMsgSynPost(final String postUrl,
			final String content, int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgSynPost(postUrl,content);
		return new C2D_HttpMessage(requestCode,pkg.getLoadData());
	}
}
