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
	 * ����֧������
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
		params.put("productName", m_productName); // ��Ʒ���� ��Ϊ��
		params.put("description", m_payInfor.description);// ��Ʒ����
		params.put("point", m_payInfor.money); // �Ʒѵ��� ��Ϊ��
		params.put("extraInfo", m_payInfor.payFlag); // CP��չ��Ϣ ��Ϊ��
		params.put("timeout", "30000"); // ���ó�ʱʱ�� ��Ϊ�� Ĭ��1����
		payHandler.sendMessage(new Message());
	}
	/**
	 * ��ʼ��
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
							// sms =����֧��,alipay =֧����,card=��ֵ��,yinLian=
							// ����,tenpay=�Ƹ�ͨ��upayAcc=�ű��˻�֧��
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
						Log.e("������֧���ɹ�--->", "code=" + code + "----" + "payType=" + payType + "----" + "extraInfo=" + extraInfo + "----" + "point=" + point + "----" + "tradeId=" + tradeId + "----" + "amount=" + amount);
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
							// sms =����֧��,alipay =֧����,card=��ֵ��,yinLian=
							// ������tenpay=�Ƹ�ͨ,upayAcc=�ű��˻�
							payType = payResult.getString("payType");
							point = payResult.getString("point");
							extraInfo = payResult.getString("extraInfo");
							tradeId = payResult.getString("tradeId");
							amount = payResult.getString("amount");
						}
						catch (JSONException e)
						{
							e.printStackTrace();
							Log.e("������֧��ʧ��---Exception-->", e.getMessage());
						}
						Log.e("������֧��ʧ��--->", "code=" + code + "----" + "payType=" + payType + "----" + "extraInfo=" + extraInfo + "----" + "point=" + point + "----" + "tradeId=" + tradeId + "----" + "amount=" + amount);
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
	 * �����Ѿ�����
	 */
	public static void SetActived()
	{
		PayRecord.Actived=true;
		PayRecord.saveRecord(C2D_App.getApp());
	}
	/**
	 * �鿴�Ƿ��Ѿ�����
	 * @return
	 */
	public static boolean isActived()
	{
		return PayRecord.Actived;
	}
	/**
	 * ����Ƿ���Ҫ֧���������Ҫ֧����������֧���Ի���
	 * 
	 * @param stage
	 *            ��ǰ����̨
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
	 * ����Ƿ���Ҫ֧���������Ҫ֧���������̽���֧��
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
