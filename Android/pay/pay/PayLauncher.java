package pay;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import c2d.app.C2D_App;
import c2d.widget.ui.C2D_Stage;

import com.upay.pay.upay_sms.UpaySms;
import com.upay.pay.upay_sms.UpaySmsCallback;
import com.upay.pay.upay_sms.UpaySmsInit;

public class PayLauncher
{
	private static String m_productName;
	private static PayDialog m_payDialog;
	private static PayInfor m_payInfor;
	private static Handler payHandler;
	private static HashMap<String, String> params=new HashMap<String, String>();
	public PayLauncher()
	{
	}

	/**
	 * 进入支付过程
	 */
	protected void startPay(C2D_Stage stage)
	{
		if (m_payDialog == null)
		{
			m_payDialog = new PayDialog(this);
		}
		m_payDialog.showDialog(stage);
	}

	public void pay()
	{
		params.clear();
		params.put("productName", m_productName); // 商品名称 可为空
		params.put("description", m_payInfor.description);// 商品描述
		params.put("point", m_payInfor.money); // 计费点数 不为空
		params.put("extraInfo", m_payInfor.payFlag); // CP扩展信息 可为空
		params.put("timeout", "30000"); // 设置超时时间 可为空 默认1分钟
		payHandler.sendMessage(new Message());
	}
	/**
	 * 初始化
	 */
	public static void Init(String productName)
	{
		m_productName = productName;
		UpaySmsInit  upayInit = new UpaySmsInit();
		upayInit.initUpay(C2D_App.getApp());
		PayRecord.loadRecord(C2D_App.getApp());
		
		payHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				UpaySms mUpay = new UpaySms();
				mUpay.pay(C2D_App.getApp(), params, new UpaySmsCallback()
				{
					String code = null;
					String payType = null;
					String point = null;
					String extraInfo = null;
					String tradeId = null;
					String amount = null;

					public void onSuccess(JSONObject payResult)
					{
						try
						{
							code = payResult.getString("code");
							// sms =话费支付,alipay =支付宝,card=充值卡,yinLian=
							// 银联,tenpay=财付通，upayAcc=优贝账户支付
							payType = payResult.getString("payType");
							point = payResult.getString("point");
							extraInfo = payResult.getString("extraInfo");
							tradeId = payResult.getString("tradeId");
							amount = payResult.getString("amount");
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						Log.e("完整版支付成功--->", "code=" + code + "----" + "payType=" + payType + "----" + "extraInfo=" + extraInfo + "----" + "point=" + point + "----" + "tradeId=" + tradeId + "----" + "amount=" + amount);
						if(m_payInfor!=null && m_payInfor.event!=null)
						{
							m_payInfor.event.onPayEnd(true);
						}
					}

					public void onFail(JSONObject payResult)
					{
						String code = null;
						String payType = null;
						String point = null;
						String extraInfo = null;
						String tradeId = null;
						String amount = null;
						try
						{
							code = payResult.getString("code");
							// sms =话费支付,alipay =支付宝,card=充值卡,yinLian=
							// 银联，tenpay=财付通,upayAcc=优贝账户
							payType = payResult.getString("payType");
							point = payResult.getString("point");
							extraInfo = payResult.getString("extraInfo");
							tradeId = payResult.getString("tradeId");
							amount = payResult.getString("amount");
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							Log.e("完整版支付失败---Exception-->", e.getMessage());
						}
						Log.e("完整版支付失败--->", "code=" + code + "----" + "payType=" + payType + "----" + "extraInfo=" + extraInfo + "----" + "point=" + point + "----" + "tradeId=" + tradeId + "----" + "amount=" + amount);
						if(m_payInfor!=null && m_payInfor.event!=null)
						{
							m_payInfor.event.onPayEnd(false);
						}
					}
				});
			}
		};
	}
	/**
	 * 设置已经激活
	 */
	public static void SetActived()
	{
		PayRecord.Actived=true;
		PayRecord.saveRecord(C2D_App.getApp());
	}
	/**
	 * 查看是否已经激活
	 * @return
	 */
	public static boolean isActived()
	{
		return PayRecord.Actived;
	}
	/**
	 * 检查是否需要支付，如果需要支付，则跳出支付对话框。
	 * 
	 * @param stage
	 *            当前的舞台
	 */
	public static void CheckPay(C2D_Stage stage, PayInfor payInfor)
	{
		if (PayRecord.Actived)
		{
			return;
		}
		m_payInfor = payInfor;
		PayLauncher p = new PayLauncher();
		p.startPay(stage);
	}

	/**
	 * 检查是否需要支付，如果需要支付，则立刻进行支付
	 * 
	 */
	public static void CheckPay(PayInfor payInfor)
	{
		if (PayRecord.Actived)
		{
			return;
		}
		m_payInfor = payInfor;
		PayLauncher p = new PayLauncher();
		p.pay();
	}
	
}
