package pay;

import pay.model.params.C2D_ParamList;
import pay.model.record.C2D_LocalRecord;
import pay.model.record.C2D_RecordItem;
import android.content.Context;

public class PayRecord
{
	static boolean Actived = false;// �Ƿ񼤻�
	static C2D_ParamList m_paramList=new C2D_ParamList();

	// ��¼��ȡ-----------------------------------------------------------
	static final int RECORD_PAY = 90;

	/**
	 * ��ȡ��¼ 
	 * @param context ������
	 */
	public static void loadRecord(Context context)
	{
		C2D_RecordItem item = new C2D_RecordItem(RECORD_PAY);
		if (C2D_LocalRecord.loadRecord(context,item))
		{
			Actived = item.readBoolean();
		}
	}

	/**
	 *  �����¼
	 * @param context ������
	 */
	static void saveRecord(Context context)
	{
		C2D_RecordItem item = new C2D_RecordItem(RECORD_PAY);
		item.writeBoolean(Actived);
		C2D_LocalRecord.saveRecord(context,item);
	}
}
