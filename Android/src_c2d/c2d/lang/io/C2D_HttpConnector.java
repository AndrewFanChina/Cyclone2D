package c2d.lang.io;


/**
 * 网络连接类
 */
public class C2D_HttpConnector
{
	/**
	 * 构造函数
	 */
	public C2D_HttpConnector()
	{
	}

	/**
	 * 以Get方式异步读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param getUrl
	 *            读取的路径
	 * @param receiver
	 *            消息接受者
	 * @param requestCode
	 *            请求类型
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
	 * 以Get方式异步读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param getUrl
	 *            读取的路径
	 * @param receiver
	 *            消息接受者
	 * @param requestCode
	 *            请求类型
	 */
	public void requestMsgAsynGet(final String getUrl, final String getContent,
			final C2D_HttpMsgReceiver receiver, final int requestCode)
	{
		requestMsgAsynGet(getUrl+"?"+getContent, receiver,requestCode);
	}

	/**
	 * 以Post方式发送数据，并异步读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param postUrl
	 *            连接的路径
	 * @param content
	 *            发送的内容
	 * @param receiver
	 *            消息接受者
	 * @param requestCode
	 *            请求类型
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
	 * 以Get方式同步载入数据直到完成，本函数执行时会阻滞当前线程，直到读取完成才会返回。
	 * 
	 * @param getUrl
	 *            路径
	 * @param requestCode
	 *            请求类型
	 * @return C2D_HttpMessage 消息
	 */
	public C2D_HttpMessage requestMsgSynGet(String getUrl,int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgSynGet(getUrl);
		return new C2D_HttpMessage(requestCode,pkg.getLoadData());
	}
	/**
	 * 以Get方式同步载入数据直到完成，本函数执行时会阻滞当前线程，直到读取完成才会返回。
	 * 
	 * @param getUrl
	 *            路径
	 * @param getContent
	 * 			  内容
	 * @param requestCode
	 *            请求类型
	 * @return C2D_HttpMessage 消息
	 */
	public C2D_HttpMessage requestMsgSynGet(String getUrl, String getContent,int requestCode)
	{
		return requestMsgSynGet(getUrl+"?"+getContent,requestCode);
	}

	/**
	 * 以Post方式发送数据，并同步载入数据直到完成，本函数执行时会阻滞当前线程，直到读取完成才会返回。
	 * 
	 * @param postUrl
	 *            连接的路径
	 * @param content
	 *            发送的内容
	 * @param requestCode
	 *            请求类型
	 * @return C2D_HttpMessage 消息
	 */
	public C2D_HttpMessage requestMsgSynPost(final String postUrl,
			final String content, int requestCode)
	{
		C2D_HttpPackage pkg=new C2D_HttpPackage();
		pkg.requestMsgSynPost(postUrl,content);
		return new C2D_HttpMessage(requestCode,pkg.getLoadData());
	}
}
