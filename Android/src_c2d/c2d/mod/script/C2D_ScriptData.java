package c2d.mod.script;

import java.io.DataInputStream;
import java.util.Vector;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;

/**
 * �ű��ṹ������
 */
class C2D_ScriptData
{
	/** ȫ�ֱ�����С */
	int iGlobalDataSize;

	/** _Main()�����Ƿ���� */
	boolean iIsMainFuncPresent;

	/** _Main()����ID. */
	int iMainFuncIndex;

	/** ָ���� */
	C2D_Instr[] pInstrs;

	/** ������. */
	C2D_Func[] pFuncTable;

	/** �����ַ�����. */
	Vector stringTableNative = null;

	/** ����ʱջ��С. */
	int stackSize;

	/** ��ʹ�ô��� */
	int usedTime = 0;
	/** ����iD */
	String fileName;

	/**
	 * C2DS�ű����ݶ���
	 * 
	 * @param name
	 *            �ű��ļ�����
	 */
	C2D_ScriptData(String name)
	{
		stringTableNative = new Vector(); // �����ַ�����
		fileName = name;
	}

	/**
	 * ����Դ���ص��ڴ�.
	 * 
	 * @return boolean
	 */
	boolean loadRes()
	{
		if (fileName == null)
		{
			return false;
		}
		String path = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + fileName;
		path += ".bin";
		// ---- ���ļ�����
		DataInputStream pScriptFile = C2D_IOUtil.getDataInputStream(path);
		if (pScriptFile == null)
		{
			return false;
		}
		// ��ʱ����
		int intData = 0;
		short shortData = 0;
		boolean booleanData = false;
		byte byteData = 0;
		// ---- ��ȡͷ��

		try
		{
			// ��ִ֤���ļ�����֤��
			String pstrIDString = C2D_IOUtil.readString(null, pScriptFile);
			if (!C2D_ScriptManager.CSE_ID_STRING.equals(pstrIDString))
			{
				return false;
			}

			// ��֤�汾��
			short iMajorVersion = C2D_IOUtil.readShort(shortData, pScriptFile);
			short iMinorVersion = C2D_IOUtil.readShort(shortData, pScriptFile);

			if (iMajorVersion != C2D_ScriptManager.VERSION_MAJOR || iMinorVersion != C2D_ScriptManager.VERSION_MINOR)
			{
				return false;
			}

			// ��ȡ����ջ��С

			stackSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			if (stackSize == 0)
			{
				stackSize = C2D_VM.DEF_STACK_SIZE;
			}

			// ��ȡȫ�����ݴ�С
			iGlobalDataSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// ���_Main()�����Ƿ����

			iIsMainFuncPresent = C2D_IOUtil.readBoolean(booleanData, pScriptFile);

			// _Main()'s��������

			iMainFuncIndex = C2D_IOUtil.readShort(shortData, pScriptFile);

			// ��ȡ���ȼ���ʱ��Ƭ

			int iPriorityType = C2D_IOUtil.readShort(shortData, pScriptFile); // ���ȼ�����
			int iTimesliceDur = C2D_IOUtil.readInt(intData, pScriptFile); // �û������ʱ��Ƭ

			// ---- ��ȡָ����

			// ��ȡָ�����

			int iSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// Ϊָ��������ռ�

			pInstrs = new C2D_Instr[iSize];
			for (int i = 0; i < iSize; i++)
			{
				pInstrs[i] = new C2D_Instr();
			}

			// ��ȡָ������

			for (int iCurrInstrIndex = 0; iCurrInstrIndex < iSize; ++iCurrInstrIndex)
			{
				// ��ȡ������

				pInstrs[iCurrInstrIndex].iOpcode = C2D_IOUtil.readShort(shortData, pScriptFile);
				pInstrs[iCurrInstrIndex].sOpcode = C2D_VM.instroMnemonics[pInstrs[iCurrInstrIndex].iOpcode];
				// ��ȡ����������

				pInstrs[iCurrInstrIndex].iOpCount = C2D_IOUtil.readByte(byteData, pScriptFile);

				int iOpCount = pInstrs[iCurrInstrIndex].iOpCount;

				// ���ɲ������б�

				C2D_Value[] pOpList = new C2D_Value[iOpCount];

				// ��ȡÿ����������Ԫ

				for (int iCurrOpIndex = 0; iCurrOpIndex < iOpCount; ++iCurrOpIndex)
				{
					pOpList[iCurrOpIndex] = new C2D_Value();

					// ��ȡ����������
					pOpList[iCurrOpIndex].iType = C2D_IOUtil.readByte(byteData, pScriptFile);

					// ���ݲ��������ͣ���ȡ����������

					switch (pOpList[iCurrOpIndex].iType)
					{
					// ����

					case C2D_VM.OP_TYPE_INT:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readInt(intData, pScriptFile);
						break;

					// ������
					//#if Platform=="Android"
					case C2D_VM.OP_TYPE_FLOAT:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readInt(intData, pScriptFile);
						break;
					//#endif
					// �ַ�������

					case C2D_VM.OP_TYPE_STRING_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						pOpList[iCurrOpIndex].iOffsetIndex = 1;
						break;

					// ָ������

					case C2D_VM.OP_TYPE_INSTR_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// ����ջ��ַ

					case C2D_VM.OP_TYPE_ABS_STACK_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// ���ջ��ַ

					case C2D_VM.OP_TYPE_REL_STACK_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						pOpList[iCurrOpIndex].iOffsetIndex = C2D_IOUtil.readShort(shortData, pScriptFile);
						;
						break;

					// ��������

					case C2D_VM.OP_TYPE_FUNC_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// ��Ӧ�ó���API����

					case C2D_VM.OP_TYPE_HOST_API_CALL_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// �Ĵ���

					case C2D_VM.OP_TYPE_REG:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;
					}
				}

				// ���������б�ֵ��ָ����
				pInstrs[iCurrInstrIndex].pOpList = pOpList;
			}

			// ---- ��ȡ�ַ�����

			// ��ȡ����

			int iStringTableSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// ��������ַ��������ȡ

			if (iStringTableSize > 0)
			{

				// ��ȡÿһ���ַ���
				for (int iCurrStringIndex = 0; iCurrStringIndex < iStringTableSize; ++iCurrStringIndex)
				{
					// ppstrStringTable[iCurrStringIndex] =
					// AFIO.readString(pScriptFile);
					loadConstString(C2D_IOUtil.readString(null, pScriptFile));
				}
			}

			// ---- ��ȡ������

			// ��ȡ����

			int iFuncTableSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			pFuncTable = new C2D_Func[iFuncTableSize];

			// ��ȡÿһ������

			for (int iCurrFuncIndex = 0; iCurrFuncIndex < iFuncTableSize; ++iCurrFuncIndex)
			{
				// ��ʼ��
				pFuncTable[iCurrFuncIndex] = new C2D_Func();

				// ��ȡ��ڵ�
				int iEntryPoint = C2D_IOUtil.readShort(shortData, pScriptFile);

				// ��ȡ��������
				int iParamCount = C2D_IOUtil.readByte(byteData, pScriptFile);

				// ��ȡ�ֲ����ݴ�С
				int iLocalDataSize = C2D_IOUtil.readShort(shortData, pScriptFile);

				// ����ջ��С
				int iStackFrameSize = iParamCount + 1 + iLocalDataSize;

				// ��ȡ������
				String iFunName = C2D_IOUtil.readString(null, pScriptFile);
				// ����������д��������
				pFuncTable[iCurrFuncIndex].iEntryPoint = iEntryPoint;
				pFuncTable[iCurrFuncIndex].iParamCount = iParamCount;
				pFuncTable[iCurrFuncIndex].iLocalDataSize = iLocalDataSize;
				pFuncTable[iCurrFuncIndex].iStackFrameSize = iStackFrameSize;
				pFuncTable[iCurrFuncIndex].iFunctionName = iFunName;
			}
			// ��ȡ�ɹ�
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			// ---- �ر��ļ�����
			if (pScriptFile != null)
			{
				try
				{
					pScriptFile.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				pScriptFile = null;
			}
		}
		return true;

	}

	// ��ȡ��̬�ַ���
	/**
	 * Load const string.
	 * 
	 * @param s
	 *            the s
	 */
	public void loadConstString(String s)
	{
		stringTableNative.addElement(s);
	}

	// /**
	// * ��ʾ��ǰ�ַ�����
	// *
	// * @param forbid the forbid
	// */
	// private boolean needPrint = false;
	// void displayStringTable(boolean forbid)
	// {
	// if (!needPrint && !forbid)
	// {
	// return;
	// }
	// needPrint = false;
	// Console.WriteLine();
	// Console.WriteLine("-----------------" + stringTableDynamic.Count+
	// "-----------------");
	// IEnumerator ie=stringTableDynamic.Values.GetEnumerator();
	// while (ie.MoveNext())
	// {
	// Console.WriteLine((String)ie.Current);
	// }
	// Console.WriteLine("------------------------------------");
	// }
	// �ͷ���Դ
	/**
	 * Release res.
	 */
	void doRelease()
	{
		if (pInstrs != null)
		{
			if (pInstrs != null)
			{
				for (int i = 0; i < pInstrs.length; i++)
				{
					pInstrs[i].ReleaseRes();
					pInstrs[i] = null;
				}
			}
			pInstrs = null;
		}
		if (pFuncTable != null)
		{
			for (int i = 0; i < pFuncTable.length; i++)
			{
				pFuncTable[i] = null;
			}
			pFuncTable = null;
		}
	}
}
