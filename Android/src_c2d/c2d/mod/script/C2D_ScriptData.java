package c2d.mod.script;

import java.io.DataInputStream;
import java.util.Vector;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;

/**
 * 脚本结构数据类
 */
class C2D_ScriptData
{
	/** 全局标量大小 */
	int iGlobalDataSize;

	/** _Main()函数是否存在 */
	boolean iIsMainFuncPresent;

	/** _Main()函数ID. */
	int iMainFuncIndex;

	/** 指令流 */
	C2D_Instr[] pInstrs;

	/** 函数表. */
	C2D_Func[] pFuncTable;

	/** 本地字符串表. */
	Vector stringTableNative = null;

	/** 运行时栈大小. */
	int stackSize;

	/** 被使用次数 */
	int usedTime = 0;
	/** 自身iD */
	String fileName;

	/**
	 * C2DS脚本数据对象
	 * 
	 * @param name
	 *            脚本文件名称
	 */
	C2D_ScriptData(String name)
	{
		stringTableNative = new Vector(); // 本地字符串表
		fileName = name;
	}

	/**
	 * 将资源加载到内存.
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
		// ---- 打开文件输入
		DataInputStream pScriptFile = C2D_IOUtil.getDataInputStream(path);
		if (pScriptFile == null)
		{
			return false;
		}
		// 临时数据
		int intData = 0;
		short shortData = 0;
		boolean booleanData = false;
		byte byteData = 0;
		// ---- 读取头部

		try
		{
			// 验证执行文件的验证码
			String pstrIDString = C2D_IOUtil.readString(null, pScriptFile);
			if (!C2D_ScriptManager.CSE_ID_STRING.equals(pstrIDString))
			{
				return false;
			}

			// 验证版本号
			short iMajorVersion = C2D_IOUtil.readShort(shortData, pScriptFile);
			short iMinorVersion = C2D_IOUtil.readShort(shortData, pScriptFile);

			if (iMajorVersion != C2D_ScriptManager.VERSION_MAJOR || iMinorVersion != C2D_ScriptManager.VERSION_MINOR)
			{
				return false;
			}

			// 读取运行栈大小

			stackSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			if (stackSize == 0)
			{
				stackSize = C2D_VM.DEF_STACK_SIZE;
			}

			// 读取全局数据大小
			iGlobalDataSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// 检查_Main()函数是否存在

			iIsMainFuncPresent = C2D_IOUtil.readBoolean(booleanData, pScriptFile);

			// _Main()'s函数索引

			iMainFuncIndex = C2D_IOUtil.readShort(shortData, pScriptFile);

			// 读取优先级和时间片

			int iPriorityType = C2D_IOUtil.readShort(shortData, pScriptFile); // 优先级类型
			int iTimesliceDur = C2D_IOUtil.readInt(intData, pScriptFile); // 用户定义的时间片

			// ---- 读取指令流

			// 读取指令个数

			int iSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// 为指令流分配空间

			pInstrs = new C2D_Instr[iSize];
			for (int i = 0; i < iSize; i++)
			{
				pInstrs[i] = new C2D_Instr();
			}

			// 读取指令数据

			for (int iCurrInstrIndex = 0; iCurrInstrIndex < iSize; ++iCurrInstrIndex)
			{
				// 读取操作码

				pInstrs[iCurrInstrIndex].iOpcode = C2D_IOUtil.readShort(shortData, pScriptFile);
				pInstrs[iCurrInstrIndex].sOpcode = C2D_VM.instroMnemonics[pInstrs[iCurrInstrIndex].iOpcode];
				// 读取操作数个数

				pInstrs[iCurrInstrIndex].iOpCount = C2D_IOUtil.readByte(byteData, pScriptFile);

				int iOpCount = pInstrs[iCurrInstrIndex].iOpCount;

				// 生成操作数列表

				C2D_Value[] pOpList = new C2D_Value[iOpCount];

				// 读取每个操作数单元

				for (int iCurrOpIndex = 0; iCurrOpIndex < iOpCount; ++iCurrOpIndex)
				{
					pOpList[iCurrOpIndex] = new C2D_Value();

					// 读取操作数类型
					pOpList[iCurrOpIndex].iType = C2D_IOUtil.readByte(byteData, pScriptFile);

					// 根据操作数类型，读取操作数数据

					switch (pOpList[iCurrOpIndex].iType)
					{
					// 整型

					case C2D_VM.OP_TYPE_INT:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readInt(intData, pScriptFile);
						break;

					// 浮点型
					//#if Platform=="Android"
					case C2D_VM.OP_TYPE_FLOAT:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readInt(intData, pScriptFile);
						break;
					//#endif
					// 字符串索引

					case C2D_VM.OP_TYPE_STRING_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						pOpList[iCurrOpIndex].iOffsetIndex = 1;
						break;

					// 指令索引

					case C2D_VM.OP_TYPE_INSTR_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// 绝对栈地址

					case C2D_VM.OP_TYPE_ABS_STACK_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// 相对栈地址

					case C2D_VM.OP_TYPE_REL_STACK_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						pOpList[iCurrOpIndex].iOffsetIndex = C2D_IOUtil.readShort(shortData, pScriptFile);
						;
						break;

					// 函数索引

					case C2D_VM.OP_TYPE_FUNC_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// 主应用程序API索引

					case C2D_VM.OP_TYPE_HOST_API_CALL_INDEX:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;

					// 寄存器

					case C2D_VM.OP_TYPE_REG:
						pOpList[iCurrOpIndex].iData = C2D_IOUtil.readShort(shortData, pScriptFile);
						break;
					}
				}

				// 将操作数列表赋值到指令流
				pInstrs[iCurrInstrIndex].pOpList = pOpList;
			}

			// ---- 读取字符串表

			// 读取表长度

			int iStringTableSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			// 如果存在字符串表，则读取

			if (iStringTableSize > 0)
			{

				// 读取每一个字符串
				for (int iCurrStringIndex = 0; iCurrStringIndex < iStringTableSize; ++iCurrStringIndex)
				{
					// ppstrStringTable[iCurrStringIndex] =
					// AFIO.readString(pScriptFile);
					loadConstString(C2D_IOUtil.readString(null, pScriptFile));
				}
			}

			// ---- 读取函数表

			// 读取表长度

			int iFuncTableSize = C2D_IOUtil.readShort(shortData, pScriptFile);

			pFuncTable = new C2D_Func[iFuncTableSize];

			// 读取每一个函数

			for (int iCurrFuncIndex = 0; iCurrFuncIndex < iFuncTableSize; ++iCurrFuncIndex)
			{
				// 初始化
				pFuncTable[iCurrFuncIndex] = new C2D_Func();

				// 读取入口点
				int iEntryPoint = C2D_IOUtil.readShort(shortData, pScriptFile);

				// 读取参数个数
				int iParamCount = C2D_IOUtil.readByte(byteData, pScriptFile);

				// 读取局部数据大小
				int iLocalDataSize = C2D_IOUtil.readShort(shortData, pScriptFile);

				// 计算栈大小
				int iStackFrameSize = iParamCount + 1 + iLocalDataSize;

				// 读取函数名
				String iFunName = C2D_IOUtil.readString(null, pScriptFile);
				// 将所有内容写到函数表
				pFuncTable[iCurrFuncIndex].iEntryPoint = iEntryPoint;
				pFuncTable[iCurrFuncIndex].iParamCount = iParamCount;
				pFuncTable[iCurrFuncIndex].iLocalDataSize = iLocalDataSize;
				pFuncTable[iCurrFuncIndex].iStackFrameSize = iStackFrameSize;
				pFuncTable[iCurrFuncIndex].iFunctionName = iFunName;
			}
			// 读取成功
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			// ---- 关闭文件输入
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

	// 读取静态字符串
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
	// * 显示当前字符串表
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
	// 释放资源
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
