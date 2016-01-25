package c2d.mod.script;

import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;

/**
 * C2DSVM��
 */
public class C2D_VM
{
	// ---- ͨ�ó��� -----------------------------------------------------------
	/** The Constant DEF_STACK_SIZE. */
	static final int DEF_STACK_SIZE = 128; // Ĭ�϶�ջ��С
	// ---- ��Ա���� -----------------------------------------------------------
	/** The current thread. */
	private static C2D_Thread currentThread; //�ű�
	// ---- ָ���������붨�� ---------------------------------------------------
	/** The Constant INSTR_MOV. */
	private static final int INSTR_MOV = 0;
	/** The Constant INSTR_ADD. */
	private static final int INSTR_ADD = 1;
	/** The Constant INSTR_SUB. */
	private static final int INSTR_SUB = 2;
	/** The Constant INSTR_MUL. */
	private static final int INSTR_MUL = 3;
	/** The Constant INSTR_DIV. */
	private static final int INSTR_DIV = 4;
	/** The Constant INSTR_MOD. */
	private static final int INSTR_MOD = 5;
	/** The Constant INSTR_EXP. */
	private static final int INSTR_EXP = 6;
	/** The Constant INSTR_NEG. */
	private static final int INSTR_NEG = 7;
	/** The Constant INSTR_INC. */
	private static final int INSTR_INC = 8;
	/** The Constant INSTR_DEC. */
	private static final int INSTR_DEC = 9;
	/** The Constant INSTR_AND. */
	private static final int INSTR_AND = 10;
	/** The Constant INSTR_OR. */
	private static final int INSTR_OR = 11;
	/** The Constant INSTR_XOR. */
	private static final int INSTR_XOR = 12;
	/** The Constant INSTR_NOT. */
	private static final int INSTR_NOT = 13;
	/** The Constant INSTR_SHL. */
	private static final int INSTR_SHL = 14;
	/** The Constant INSTR_SHR. */
	private static final int INSTR_SHR = 15;
	/** The Constant INSTR_CONCAT. */
	private static final int INSTR_CONCAT = 16;
	/** The Constant INSTR_GETCHAR. */
	private static final int INSTR_GETCHAR = 17;
	/** The Constant INSTR_SETCHAR. */
	private static final int INSTR_SETCHAR = 18;
	/** The Constant INSTR_JMP. */
	private static final int INSTR_JMP = 19;
	/** The Constant INSTR_JE. */
	private static final int INSTR_JE = 20;
	/** The Constant INSTR_JNE. */
	private static final int INSTR_JNE = 21;
	/** The Constant INSTR_JG. */
	private static final int INSTR_JG = 22;
	/** The Constant INSTR_JL. */
	private static final int INSTR_JL = 23;
	/** The Constant INSTR_JGE. */
	private static final int INSTR_JGE = 24;
	/** The Constant INSTR_JLE. */
	private static final int INSTR_JLE = 25;
	/** The Constant INSTR_PUSH. */
	private static final int INSTR_PUSH = 26;
	/** The Constant INSTR_POP. */
	private static final int INSTR_POP = 27;
	/** The Constant INSTR_CALL. */
	private static final int INSTR_CALL = 28;
	/** The Constant INSTR_RET. */
	private static final int INSTR_RET = 29;
	/** The Constant INSTR_CALLHOST. */
	private static final int INSTR_CALLHOST = 30;
	/** The Constant INSTR_PAUSE. */
	private static final int INSTR_PAUSE = 31;
	/** The Constant INSTR_EXIT. */
	private static final int INSTR_EXIT = 32;
	/** The Constant INSTR_PRINT. */
	private static final int INSTR_PRINT = 33;
	// ---- ָ�����Ƿ� -------------------------------------------------------------
	//�������������ָ��ִ�е�ʱ�������Ӧ�����Ƿ�
	/** The Constant instroMnemonics. */
	static final String[] instroMnemonics = new String[]
	{
			"Mov", "Add", "Sub", "Mul", "Div", "Mod", "Exp", "Neg", "Inc", "Dec", "And", "Or", "XOr", "Not", "ShL", "ShR", "Concat", "GetChar", "SetChar", "Jmp", "JE", "JNE",
			"JG", "JL", "JGE", "JLE", "Push", "Pop", "Call", "Ret", "CallHost", "Pause", "Exit", "Print"
	};
	// ---- ���������� ------------------------------------------------------
	/** The Constant OP_TYPE_NULL. */
	static final int OP_TYPE_NULL = -1; // δ���������
	/** The Constant OP_TYPE_INT. */
	static final int OP_TYPE_INT = 0; // ����
	//#if Platform=="Android"
	/** The Constant OP_TYPE_FLOAT. */
	static final int OP_TYPE_FLOAT = 1; // ������
	//#endif
	/** The Constant OP_TYPE_STRING_INDEX. */
	static final int OP_TYPE_STRING_INDEX = 2; // �ַ���
	/** The Constant OP_TYPE_ABS_STACK_INDEX. */
	static final int OP_TYPE_ABS_STACK_INDEX = 3; // ���Զ�ջ����(��������ʹ����������������������Ԫ��)
	/** The Constant OP_TYPE_REL_STACK_INDEX. */
	static final int OP_TYPE_REL_STACK_INDEX = 4; // ��Զ�ջ����(ʹ�ñ�������������)
	/** The Constant OP_TYPE_INSTR_INDEX. */
	static final int OP_TYPE_INSTR_INDEX = 5; // ָ������(������תĿ��)
	/** The Constant OP_TYPE_FUNC_INDEX. */
	static final int OP_TYPE_FUNC_INDEX = 6; // ��������(����Callָ����)
	/** The Constant OP_TYPE_HOST_API_CALL_INDEX. */
	static final int OP_TYPE_HOST_API_CALL_INDEX = 7; // ��Ӧ�ó���API��������(����CallHostָ����)
	/** The Constant OP_TYPE_REG. */
	static final int OP_TYPE_REG = 8; // �Ĵ������������������_RetVal
	/** The Constant OP_TYPE_RET_ASYN. */
	static final int OP_TYPE_RET_ASYN = 9; // �첽�˳���־
	/** The Constant OP_TYPE_RET_SYN. */
	static final int OP_TYPE_RET_SYN = 10; // ͬ���˳���־

	// ---- ��Ա���� -----------------------------------------------------------------------------
	/**
	 * ResolveStackIndex () ��þ���ջ��������������ֱ�ӷ��أ�����������Ե�ǰ��ջ��ܣ������Ҫ���ϵ�ǰջ��������󷵻�.
	 * 
	 * @param iIndex
	 *            int
	 * @return int
	 */
	static int ResolveStackIndex(int iIndex)
	{
		return (iIndex < 0 ? iIndex += currentThread.Stack.iFrameIndex : iIndex);
	}

	/**
	 * RunScript () ִ�е�ǰ����Ľű���ֱ���а���������߽ű��˳�.
	 * 
	 * @param thread
	 *            C2DSThread
	 */
	public static final void C2DS_RunScript(C2D_Thread thread)
	{
		//���õ�ǰ�߳�
		currentThread = thread;
		if (currentThread == null || !currentThread.isRunning)
		{
			return;
		}
		// ��ǰ�ű��Ƿ���ͣ?
		if (currentThread.pausedFrame > 0)
			
		{
			currentThread.pausedFrame--;
			if (currentThread.pausedFrame > 0)
			{
				return;
			}
		}
		//ѭ���˳���־
		boolean iExitLoop = false;
		//��ʼ�ű�ѭ��
		while (currentThread.isRunning && currentThread.pausedFrame == 0)
		{
			//׼��һ��ָ��ָ��Ŀ�����������ıȽ�
			int iCurrInstr = currentThread.iCurrInstr;
			if (iCurrInstr < 0 || iCurrInstr >= currentThread.scriptData.pInstrs.length)
			{
				print("��������Ч\n");
				currentThread.isRunning = false;
				iExitLoop = true;
				return;
			}
			//�õ���ǰ������
			int iOpcode = currentThread.scriptData.pInstrs[iCurrInstr].iOpcode;
			//��ӡָ����Ϣ
			printDebugInfor("\t");
			if (iOpcode < 10)
			{
				printDebugInfor(" " + iOpcode);
			}
			else
			{
				printDebugInfor("" + iOpcode);
			}
			printDebugInfor(" " + instroMnemonics[iOpcode] + " ");
			//���ݵ�ǰ��ָ��ָ��ִ�в����룬ֻҪ�ű�û����ͣ
			switch (iOpcode)
			{
			// ---- �����Ʋ���
			// ��ֵ
			case INSTR_MOV:
				// ��ѧ����
			case INSTR_ADD:
			case INSTR_SUB:
			case INSTR_MUL:
			case INSTR_DIV:
			case INSTR_MOD:
			case INSTR_EXP:
				// λ����
			case INSTR_AND:
			case INSTR_OR:
			case INSTR_XOR:
			case INSTR_SHL:
			case INSTR_SHR:
			{
				//���Ŀ��������ı��ظ���(����0)
				C2D_Value Dest = ResolveOpValue(0);
				//���Դ�������ı��ظ���(����1)
				C2D_Value Source = ResolveOpValue(1);
				//����ָ�ִ�в���
				switch (iOpcode)
				{
				//��ֵ
				case INSTR_MOV:
					// �������ջԪ����ͬ��������/*??*/
					if (ResolveOpPntr(0) == ResolveOpPntr(1))
					{
						break;
					}
					// ����ԭջԪ�ص�Ŀ��ջԪ��
					CopyValue(Dest, Source);
					break;
				//�ӷ�
				case INSTR_ADD:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData += ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF += sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				//����
				case INSTR_SUB:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData -= ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF -= sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				//�˷�
				case INSTR_MUL:
					//#if Platform=="Android"
					if ((Source.iType == OP_TYPE_FLOAT || Source.iType == OP_TYPE_INT) && (Dest.iType == OP_TYPE_FLOAT || Dest.iType == OP_TYPE_INT))
					{
						//#endif
						if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
						{
							Dest.iData *= ResolveOpAsInt(1);
						}
						//#if Platform=="Android"
						else
						{
							if (Dest.iType == OP_TYPE_FLOAT && Source.iType == OP_TYPE_INT)
							{
								float destF = ResolveOpAsFloat(0);
								int sourceI = ResolveOpAsInt(1);
								float value = sourceI * destF;
								Dest.iData = C2D_Math.floatToIntBits(value);
							}
							else if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_FLOAT)
							{
								int destI = ResolveOpAsInt(0);
								float sourceF = ResolveOpAsFloat(1);
								float value = sourceF * destI;
								Dest.iData = C2D_Math.floatToIntBits(value);
							}
							else if (Dest.iType == OP_TYPE_FLOAT && Source.iType == OP_TYPE_FLOAT)
							{
								float destF = ResolveOpAsFloat(0);
								float sourceF = ResolveOpAsFloat(1);
								float value = sourceF * destF;
								Dest.iData = C2D_Math.floatToIntBits(destF);
							}
						}
					}
					//#endif
					else
					{
						print("error: can't use MUL between these type operators[" + Source.iType + "," + Dest.iType + "]");
						currentThread.isRunning = false;
						iExitLoop = true;
						break;
					}
					break;
				//����
				case INSTR_DIV:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData /= ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF /= sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				// ȡģ
				case INSTR_MOD:
					// ȡģֻ����������
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData %= ResolveOpAsInt(1);
					}
					break;
				// ȡ��(�ݲ�֧��)
				case INSTR_EXP:
					//                                if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					//                                {
					//                                    Dest.iData = (int)MathUtil.Pow(Dest.iData, ResolveOpAsInt(1));
					//                                }
					//                                else
					//                                {
					//                                    float destF = ResolveOpAsFloat(0);
					//                                    float sourceF = ResolveOpAsFloat(1);
					//                                    destF = (float)MathUtil.Pow(destF, sourceF);
					//                                    Dest.iData += MathUtil.floatToInt(destF);
					//                                }
					break;
				// ��
				case INSTR_AND:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData &= ResolveOpAsInt(1);
					}
					break;
				// ��
				case INSTR_OR:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData |= ResolveOpAsInt(1);
					}
					break;
				//���
				case INSTR_XOR:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData ^= ResolveOpAsInt(1);
					}
					break;
				// ����
				case INSTR_SHL:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData <<= ResolveOpAsInt(1);
					}
					break;
				// ����
				case INSTR_SHR:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData >>= ResolveOpAsInt(1);
					}
					break;
				}
				//��ӡ������
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				break;
			}
			// ---- һԪ����
			case INSTR_NEG:
			case INSTR_NOT:
			case INSTR_INC:
			case INSTR_DEC:
			{
				// ��ò���������
				int iDestStoreType = GetOpType(0);
				// ���Ŀ�������
				C2D_Value Dest = ResolveOpValue(0);
				switch (iOpcode)
				{
				// ȡ��
				case INSTR_NEG:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData = -Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(-destF);
					}
					//#endif
					break;
				// ȡ��
				case INSTR_NOT:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData = ~Dest.iData;
					}
					break;
				// ����
				case INSTR_INC:
					if (Dest.iType == OP_TYPE_INT)
					{
						++Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(destF + 1);
					}
					//#endif
					break;
				// �Լ�
				case INSTR_DEC:
					if (Dest.iType == OP_TYPE_INT)
					{
						--Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(destF - 1);
					}
					//#endif
					break;
				}
				//��ӡ������
				PrintOpIndir(0);
				break;
			}
			// ---- �ַ�����
			case INSTR_CONCAT:
			{
				// ��ò�����
				C2D_Value Dest = ResolveOpValue(0);
				//����ַ�������
				String pstrSourceString = ResolveOpAsString(1);
				//���Ŀ��ֵ���Ͳ����ַ�������ʲôҲ����
				if (Dest.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				String newString = ResolveOpAsString(0) + pstrSourceString;
				//����Ƕ�̬�ַ�����ɾ��ԭ�ȵ�����
				currentThread.releaseValueString(Dest);
				int key = currentThread.addString(newString); //����µ�����
				Dest.setAsDynamicString(key);
				//��ӡ������
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				break;
			}
			case INSTR_GETCHAR:
			{
				//��ò�����
				C2D_Value Dest = ResolveOpValue(0);
				//���Ŀ��ֵ���Ͳ����ַ�������ʲôҲ����
				if (Dest.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				//����Դ�ַ���,
				String pstrSourceString = ResolveOpAsString(1);
				//������ȡ����
				int iSourceIndex = ResolveOpAsInt(2);
				String newString = pstrSourceString.charAt(iSourceIndex) + "";
				currentThread.releaseValueString(Dest);
				int key = currentThread.addString(newString); //����µ�����
				Dest.setAsDynamicString(key);
				//��ӡ������
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				break;
			}
			case INSTR_SETCHAR:
			{
				// ���Ŀ������
				int iDestIndex = ResolveOpAsInt(1);
				// ��֤����
				C2D_Value value = ResolveOpValue(0);
				if (value.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				// ���Դ�ַ���
				String pstrSourceString = ResolveOpAsString(2);
				//����
				int key = value.iData;
				if (value.iOffsetIndex < 0)
				{
					String oldString = ResolveOpAsString(0);
					String newString = oldString.substring(0, iDestIndex) + pstrSourceString.charAt(0) + oldString.substring(iDestIndex + 1, oldString.length() - (iDestIndex + 1));
					currentThread.setString(key, newString);
				}
				else
				{
					print("error: can't modify static string");
					currentThread.isRunning = false;
					iExitLoop = true;
				}
				//��ӡ������
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				break;
			}
			// ---- ������֧
			case INSTR_JMP:
			{
				//��ӡ������
				PrintOpValue(0);
				int iTargetIndex = ResolveOpAsInstrIndex(0);
				currentThread.iCurrInstr = iTargetIndex;
				break;
			}
			case INSTR_JE:
			case INSTR_JNE:
			case INSTR_JG:
			case INSTR_JL:
			case INSTR_JGE:
			case INSTR_JLE:
			{
				//��ȡ����������
				C2D_Value Op0 = ResolveOpValue(0);
				C2D_Value Op1 = ResolveOpValue(1);
				//��ȡĿ��ָ������
				int iTargetIndex = ResolveOpAsInstrIndex(2);
				//��ת�ж�
				boolean iJump = false;
				//#if Platform=="Android"
				double d0, d1;
				//#else
//@								int d0 = 0, d1 = 0;
				//#endif
				switch (iOpcode)
				{
				//�����ȵĻ���ת
				case INSTR_JE:
				{
					switch (Op0.iType)
					{
					case OP_TYPE_INT:
						if (Op0.iData == Op1.iData)
						{
							iJump = true;
						}
						break;
					//#if Platform=="Android"
					case OP_TYPE_FLOAT:
						if (Op0.iData == Op1.iData)
						{
							iJump = true;
						}
						break;
					//#endif
					case OP_TYPE_STRING_INDEX:
						String string0 = (String) currentThread.getString(Op0);
						String string1 = (String) currentThread.getString(Op1);
						if (string0.equals(string1))
						{
							iJump = true;
						}
						break;
					}
					break;
				}
				//����������ת
				case INSTR_JNE:
				{
					switch (Op0.iType)
					{
					case OP_TYPE_INT:
						if (Op0.iData != Op1.iData)
						{
							iJump = true;
						}
						break;
					//#if Platform=="Android"
					case OP_TYPE_FLOAT:
						if (Op0.iData != Op1.iData)
						{
							iJump = true;
						}
						break;
					//#endif
					case OP_TYPE_STRING_INDEX:
						String string0 = (String) currentThread.getString(Op0);
						String string1 = (String) currentThread.getString(Op1);
						if (!string0.equals(string1))
						{
							iJump = true;
						}
						break;
					}
					break;
				}
				//���������ת
				case INSTR_JG:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 > d1)
					{
						iJump = true;
					}
					break;
				// ���С����ת
				case INSTR_JL:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 < d1)
					{
						iJump = true;
					}
					break;
				// ���ڵ�����ת
				case INSTR_JGE:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 >= d1)
					{
						iJump = true;
					}
					break;
				// С�ڵ�����ת
				case INSTR_JLE:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 <= d1)
					{
						iJump = true;
					}
					break;
				}
				//��ӡ������
				PrintOpValue(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				printDebugInfor(" ");
				//���������ת
				if (iJump)
				{
					currentThread.iCurrInstr = iTargetIndex;
					printDebugInfor("(True)");
				}
				else
				{
					printDebugInfor("(False)");
				}
				break;
			}
			// ---- ջ�����ӿ�
			case INSTR_PUSH:
			{
				// ��ò�����
				C2D_Value Source = ResolveOpValue(0);
				C2D_Value Dest = new C2D_Value();
				CopyValue(Dest, Source);
				//��������ѹջ
				currentThread.Stack.Push(Dest);
				//��ӡ������
				PrintOpValue(0);
				break;
			}
			case INSTR_POP:
			{
				//����ջ��Ԫ�ص�ָ��������
				C2D_Value dest = ResolveOpPntr(0);
				C2D_Value src = currentThread.Stack.Peek();
				CopyValue(dest, src);
				currentThread.Stack.Pop();
				//��ӡĿ��
				PrintOpIndir(0);
				break;
			}
			// ---- �������ýӿ�
			case INSTR_CALL:
			{
				//���ݵ�һ����������ú���
				int iFuncIndex = ResolveOpAsFuncIndex(0);
				//���̽�ָ��ָ�������������ú��λ��
				++currentThread.iCurrInstr;
				//���ú���
				CallFunc(iFuncIndex);
				break;
			}
			case INSTR_RET:
			{
				//�ӵ�ǰջ��Ԫ���л�õ�ǰ����������ʹ���������Ӧ�ĺ����ṹ
				C2D_Value FuncIndex = currentThread.Stack.Peek();
				currentThread.Stack.Pop();
				//����Ƿ����첽�˳���־
				if (FuncIndex.iType == OP_TYPE_RET_ASYN)
				{
					iExitLoop = true;
				}
				//����Ƿ���ͬ���˳���־/*���ﲻӦ��ֹͣ�߳�*/
				//if (FuncIndex.iType == OP_TYPE_RET_SYN)
				//{
				//    currentThread.iIsRunning = false;
				//}
				// ��õ���ǰ����������
				C2D_Func CurrFunc = GetFunc(FuncIndex.iData);
				int iFrameIndex = FuncIndex.iOffsetIndex;
				//��ջ�ж�ȡ���ص�ַ�ṹ����λ�ڵ�ǰ�ֲ����ݿ���·�һ����Ԫ
				C2D_Value ReturnAddr = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - (CurrFunc.iLocalDataSize + 1));
				//����ջ��ܣ��������ص�ַһ��
				currentThread.Stack.PopFrame(CurrFunc.iStackFrameSize);
				//�ָ�ԭ������ջ�������
				currentThread.Stack.iFrameIndex = iFrameIndex;
				//��ת�����ص�ַ
				currentThread.iCurrInstr = ReturnAddr.iData;
				//��ӡ���ص�ַ
				printDebugInfor("" + ReturnAddr.iData);
				/*����Ƿ�Ӧ��ֹͣ�̣߳�����̲߳��Ǵ�main�������������Ǹ�����û��main������Ӧ�ó���ʹ��
				 ͬ�������첽���õķ�ʽ�������̵߳Ļ������ҵ���ջӦ��Ϊ�յĻ���˵�����Ǵ�main����������
				 ���ǿ���ֹͣ����̵߳Ļ�ˡ������main���������Ļ���Ӧ�õȵ�Exitָ����ֹͣ�̻߳��*/
				if (currentThread.Stack.iFrameIndex == 0)
				{
					currentThread.isRunning = false;
					iExitLoop = true;
				}
				break;
			}
			case INSTR_CALLHOST:
			{
				//������0������API����ID
				C2D_Value HostAPICall = ResolveOpValue(0);
				int iHostAPICallIndex = HostAPICall.iData;
				if (currentThread.getExcutor().m_funHandler != null)
				{
					currentThread.getExcutor().m_funHandler.handleFunction(currentThread, iHostAPICallIndex);
				}
				C2D_ScriptManager scriptManager = currentThread.getExcutor().m_scripManager;
				//����ѹջ��Host����
				if (iHostAPICallIndex >= 0 && iHostAPICallIndex < scriptManager.g_HostsLen.length)
				{
					for (int i = 0; i < scriptManager.g_HostsLen[iHostAPICallIndex]; i++)
					{
						currentThread.Stack.Pop();
					}
				}
				else
				{
					print("-error host address-");
					return;
				}
				String sw = currentThread.getSwitchTo();
				if (sw != null)
				{
					currentThread.getExcutor().switchToThread(sw);
				}
				break;
			}
			// ---- ����
			//        case INSTR_PAUSE:
			//        {
			//          // ����Ѿ���ͣ��ʲôҲ����
			//
			//          if (currentThread.iIsPaused)
			//          {
			//            break;
			//          }
			//
			//          //�����ͣʱ��
			//
			//          int iPauseDuration = ResolveOpAsInt(0);
			//
			//          //�����ָ�ʱ��
			//
			//          currentThread.iPauseEndTime = System.currentTimeMillis() +
			//              iPauseDuration;
			//
			//          //��ͣ�ű�
			//
			//          currentThread.iIsPaused = true;
			//
			//          //��ӡ��ͣʱ��
			//
			//          PrintOpValue(0);
			//          break;
			//        }
			case INSTR_EXIT:
				//�Ӳ�����0��ȡ�˳���
				C2D_Value ExitCode = ResolveOpValue(0);
				int iExitCode = ExitCode.iData;
				//�˳�ִ��ѭ��
				iExitLoop = true;
				//��ӡ�˳���
				PrintOpValue(0);
				//ֻ��main�����Ľ�β���Ż�����exitָ���ʱ��Ӧ��ֹͣ�߳�����
				currentThread.isRunning = false;
				break;
			case INSTR_PRINT:
				//�Ӳ�����0��ȡ��ӡ��Ϣ
				String infor = ResolveOpAsString(0);
				printDebugInfor(infor);
				//��ӡ��Ϣ
				PrintOpValue(0);
				break;
			}
			printDebugInfor("\n");
			//            currentThread.displayStringTable(false);
			//�����ǰָ��ָ��û�б�ָ��ı䣬������1
			if (iCurrInstr == currentThread.iCurrInstr)
			{
				++currentThread.iCurrInstr;
			}
			//�˳�ִ��ѭ���ж�
			if (iExitLoop)
			{
				break;
			}
		}
		currentThread = null;
	}

	/**
	 * CopyValue () ����һ��Vlaue�ṹ������һ���������ַ�����.
	 * 
	 * @param pDest
	 *            Value
	 * @param Source
	 *            Value
	 */
	private static void CopyValue(C2D_Value pDest, C2D_Value Source)
	{
		//��ɾ��ԭ�ȵ��ַ���(����еĻ�)
		currentThread.releaseValueString(pDest);
		//������ֵ
		pDest.iType = Source.iType;
		pDest.iData = Source.iData;
		pDest.iOffsetIndex = Source.iOffsetIndex;
		//�����µ��ַ���(�����Ҫ�Ļ�)
		if (pDest.iType == OP_TYPE_STRING_INDEX)
		{
			String sNew = currentThread.getString(pDest) + "";
			int keyNew = currentThread.addString(sNew);
			pDest.setAsDynamicString(keyNew);
		}
	}

	/**
	 * CoereceValueToInt () ��һ����ֵǿ��ת��������ֵ.
	 * 
	 * @param Val
	 *            Value
	 * @return int
	 */
	static int CoerceValueToInt(C2D_Value Val)
	{
		// �������ʹ���
		switch (Val.iType)
		{
		// ����ֱ�ӷ���
		case OP_TYPE_INT:
			return Val.iData;
			// ������
			//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float fFloat = CoerceValueToFloat(Val);
			return (int) fFloat;
			//#endif
			// �ַ���
		case OP_TYPE_STRING_INDEX:
			return C2D_Math.stringToInt((String) currentThread.getString(Val));
			//�������Ƿ�
		default:
			return 0;
		}
	}

	//#if Platform=="Android"
	/**
	 * CoereceValueToFloat () ǿ��ת��һ��Value�ṹ�ɸ�������.
	 * 
	 * @param Val
	 *            Value
	 * @return float
	 */
	static float CoerceValueToFloat(C2D_Value Val)
	{
		// �������ͽ���
		switch (Val.iType)
		{
		//���ͣ�ʹ��ǿ��ת��
		case OP_TYPE_INT:
			return (float) Val.iData;
			//�����ͣ�ʹ�ø�ʽת��
		case OP_TYPE_FLOAT:
			return C2D_Math.intBitsToFloat(Val.iData);
			//�ַ���
		case OP_TYPE_STRING_INDEX:
			return (float) C2D_Math.stringToFloat((String) currentThread.getString(Val));
			//�������Ƿ�
		default:
			return 0;
		}
	}

	//#endif
	/**
	 * CoereceValueToString () ǿ��ת��һ��Value�ṹΪ�ַ���.
	 * 
	 * @param Val
	 *            Value
	 * @return String
	 */
	static String CoerceValueToString(C2D_Value Val)
	{
		// ����Vlaue��ǰ����������
		switch (Val.iType)
		{
		case OP_TYPE_INT:
			return Val.iData + "";
			//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float f = C2D_Math.intBitsToFloat(Val.iData);
			return f + "";
			//#endif
		case OP_TYPE_STRING_INDEX:
			return (String) currentThread.getString(Val);
		default:
			return null;
		}
	}

	/**
	 * GetOpType () ��ȡ��ǰָ���ָ���������Ĳ���������.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int GetOpType(int iOpIndex)
	{
		//��ȡ��ǰָ��
		int iCurrInstr = currentThread.iCurrInstr;
		//��ȡ����������
		return currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex].iType;
	}

	/**
	 * ResolveOpStackIndex () ������������վ�����������Ǿ��Ի������.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpStackIndex(int iOpIndex)
	{
		// �õ���ǰָ��
		int iCurrInstr = currentThread.iCurrInstr;
		// �õ�����������
		C2D_Value OpValue = currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex];
		//�������ͷ���ջ����
		switch (OpValue.iType)
		{
		//����ջ����
		case OP_TYPE_ABS_STACK_INDEX:
			return OpValue.iData;
			//���ջ������������
		case OP_TYPE_REL_STACK_INDEX:
		{
			// �����ǻ���ַ
			int iBaseIndex = OpValue.iData;
			// �ٻ�ñ�������
			int iOffsetIndex = OpValue.iOffsetIndex;
			// ��ñ���ֵ
			C2D_Value StackValue = currentThread.Stack.GetStackValue(iOffsetIndex);
			// ���ڣ��ڻ���ַ�����ӵ�ǰ����ֵ����þ��Ե�ַ��ע��ʹ�ü���
			return iBaseIndex - StackValue.iData;
		}
		// �����������0���������ǲ�Ӧ�������������
		default:
			return 0;
		}
	}

	/**
	 * ResolveOpValue () ������������������ص���ֵ�ṹ.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return Value
	 */
	private static C2D_Value ResolveOpValue(int iOpIndex)
	{
		// ��õ�ǰָ��
		int iCurrInstr = currentThread.iCurrInstr;
		// ��ȡ������
		if (iCurrInstr < 0 || iCurrInstr >= currentThread.scriptData.pInstrs.length || iOpIndex < 0 || iOpIndex >= currentThread.scriptData.pInstrs[iCurrInstr].pOpList.length)
		{
			C2D_Debug.log("error");
		}
		C2D_Value OpValue = currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex];
		//���ݲ��������ͣ���������ֵ
		switch (OpValue.iType)
		{
		// ջ����
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			//����������ʹ�������ر�ջԪ��ָ�����Ӧ��ջԪ��
			int iAbsIndex = ResolveOpStackIndex(iOpIndex);
			return currentThread.Stack.GetStackValue(iAbsIndex);
		}
		// �Ĵ���
		case OP_TYPE_REG:
			return currentThread._RetVal;
			// ������������Ե�ǰջԪ��
		default:
			return OpValue;
		}
	}

	/**
	 * ResolveOpType () ���ص�ǰָ��ָ���������Ĳ��������� resolved type.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	protected static int ResolveOpType(int iOpIndex)
	{
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		return OpValue.iType;
	}

	/**
	 * ResolveOpAsInt () ��������ֵǿ�ƽ�������������.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsInt(int iOpIndex)
	{
		//��ò�����
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//ǿ��ת������������
		int iInt = CoerceValueToInt(OpValue);
		return iInt;
	}

	//#if Platform=="Android"
	/**
	 * ResolveOpAsFloat () ��������ֵǿ�ƽ����ɸ���������.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return float
	 */
	private static float ResolveOpAsFloat(int iOpIndex)
	{
		//��ò�����
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//ǿ��ת���ɸ�������
		float fFloat = CoerceValueToFloat(OpValue);
		return fFloat;
	}

	//#endif
	/**
	 * ResolveOpAsString () ������ǿ��װ�����������ַ���.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return String
	 */
	private static String ResolveOpAsString(int iOpIndex)
	{
		//��ò�����
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//ǿ��ת�����ַ����󷵻�
		String pstrString = CoerceValueToString(OpValue);
		return pstrString;
	}

	/**
	 * ResolveOpAsInstrIndex () ǿ��ת��������Ϊָ������.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsInstrIndex(int iOpIndex)
	{
		//�����ֵ
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//������ֵ
		return OpValue.iData;
	}

	/**
	 * ResolveOpAsFuncIndex () ǿ��ת��������Ϊ����ID.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsFuncIndex(int iOpIndex)
	{
		// ��ȡ������
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		// ���غ���ID
		return OpValue.iData;
	}

	/**
	 * ResolveOpPntr () ����������������һ��ָ�����Ľṹ��ָ�룬ֻ�����ǼĴ�������ջ��ַ����.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return Value
	 */
	private static C2D_Value ResolveOpPntr(int iOpIndex)
	{
		//��ò���������
		int iIndirMethod = GetOpType(iOpIndex);
		// �������ͷ���ָ��
		switch (iIndirMethod)
		{
		//�Ĵ���
		case OP_TYPE_REG:
			return currentThread._RetVal;
			//ջ����
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			int iStackIndex = ResolveOpStackIndex(iOpIndex);
			return currentThread.Stack.pElmnts[ResolveStackIndex(iStackIndex)];
		}
		}
		// Return null for anything else
		return null;
	}

	/**
	 * GetFunc () Returns the function corresponding to the specified index.
	 * 
	 * @param iIndex
	 *            int
	 * @return Func
	 */
	private static C2D_Func GetFunc(int iIndex)
	{
		return currentThread.scriptData.pFuncTable[iIndex];
	}

	/**
	 * CallFunc () ��������.
	 * 
	 * @param iIndex
	 *            int
	 */
	private static void CallFunc(int iIndex)
	{
		C2D_Func DestFunc = GetFunc(iIndex);
		//����ջ�������
		int iFrameIndex = currentThread.Stack.iFrameIndex;
		//�����ص�ַ������ǰָ��ָ��ѹջ
		C2D_Value ReturnAddr = new C2D_Value();
		ReturnAddr.iData = currentThread.iCurrInstr;
		currentThread.Stack.Push(ReturnAddr);
		//ѹ��ֲ����ݿռ�+1�Ĵ�С(�����1��Ϊ���غ�������)
		currentThread.Stack.PushFrame(DestFunc.iLocalDataSize + 1);
		//�����غ���������ԭջ������������ջ��Ԫ����
		C2D_Value FuncIndex = new C2D_Value();
		FuncIndex.iData = iIndex;
		FuncIndex.iOffsetIndex = iFrameIndex;
		currentThread.Stack.SetStackValue(currentThread.Stack.iTopIndex - 1, FuncIndex);
		//��ת��ָ���ĺ������
		currentThread.iCurrInstr = DestFunc.iEntryPoint;
	}

	/**
	 * ���ݺ��������غ���ID.
	 * 
	 * @param callThread
	 * @param pstrName
	 *            String
	 * @return int
	 */
	static int GetFuncIndexByName(C2D_Thread callThread, String pstrName)
	{
		if(callThread!=null)
		{
			//��������
			for (int iFuncIndex = 0; iFuncIndex < callThread.scriptData.pFuncTable.length; ++iFuncIndex)
			{
				if (pstrName.equals(callThread.scriptData.pFuncTable[iFuncIndex].iFunctionName))
				{
					return iFuncIndex;
				}
			}
		}
		//û���ҵ�����-1
		return -1;
	}

	/**
	 * ����Ӧ�ó������һ���ű��������첽���ã����̸���ʱ��Ƭ���У�ֱ���䷵��.
	 * 
	 * @param callThread
	 *            int
	 * @param pstrName
	 *            String
	 */
	public static void C2DS_CallScriptFunc(C2D_Thread callThread, String pstrName)
	{
		//����VM�ĵ�ǰ״̬
		C2D_Thread prevThread = currentThread;
		currentThread = callThread;
		//�������ֻ�ú���ID
		int iFuncIndex = GetFuncIndexByName(callThread,pstrName);
		//ȷ����������
		if (iFuncIndex == -1)
		{
			return;
		}
		//���ú���
		CallFunc(iFuncIndex);
		//�����첽�˳���־
		C2D_Value StackBase = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - 1);
		StackBase.iType = OP_TYPE_RET_ASYN;
		//SetStackValue(g_iCurrThread, currentThread.Stack.iTopIndex - 1, StackBase);
		callThread.isRunning = true;
		//����ű����벻�ж�����ֱ���䷵��
		C2DS_RunScript(callThread);
		//---- ����������
		//�ָ�VM״̬
		currentThread = prevThread;
	}

	/**
	 * ����Ӧ�ó�����һ���ű���������˼�ǵ�����ű���ͬ����ʽִ��
	 * (�˺����൱����ָ���ű��߳��е�ǰִ��λ��ǿ�в���һ���������ã�������ʱ��������ִ�У�
	 * ֻ�����úú�����ڵ����Ϣ�������̻߳��ʱ��Ƭ���ִ�кͷ��ء�ע�����ַ�ʽ���ܴ��������⡣).
	 * 
	 * @param pstrName
	 *            String
	 */
	public static void C2DS_InvokeScriptFunc(C2D_Thread callThread, String pstrName)
	{
		//���ݺ�������ú���ID
		int iFuncIndex = GetFuncIndexByName(callThread,pstrName);
		//ȷ����������
		if (iFuncIndex == -1)
		{
			return;
		}
		callThread.isRunning = true;
		//���ú���
		CallFunc(iFuncIndex);
		//����ͬ���˳���
		C2D_Value StackBase = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - 1);
		StackBase.iType = OP_TYPE_RET_SYN;
		//SetStackValue(g_iCurrThread, currentThread.Stack.iTopIndex - 1, StackBase);
	}

	/**
	 * PrintOpIndir () ��ӡջ��ַ.
	 * 
	 * @param iOpIndex
	 *            int
	 */
	private static void PrintOpIndir(int iOpIndex)
	{
		//��ȡ����������
		int iIndirMethod = GetOpType(iOpIndex);
		//��ӡ
		switch (iIndirMethod)
		{
		//�Ĵ���
		case OP_TYPE_REG:
			printDebugInfor("_RetVal");
			break;
		//ջ����
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			int iStackIndex = ResolveOpStackIndex(iOpIndex);
			printDebugInfor("[ " + iStackIndex + " ]");
			break;
		}
		}
	}

	/**
	 * PrintOpValue () ��ӡ��������ֵ.
	 * 
	 * @param iOpIndex
	 *            int
	 */
	private static void PrintOpValue(int iOpIndex)
	{
		// ��ò�����
		C2D_Value Op = ResolveOpValue(iOpIndex);
		// ��ӡ
		switch (Op.iType)
		{
		case OP_TYPE_NULL:
			printDebugInfor("Null");
			break;
		case OP_TYPE_INT:
			printDebugInfor("" + Op.iData);
			break;
		//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float f = ResolveOpAsFloat(iOpIndex);
			printDebugInfor("" + f);
			break;
		//#endif
		case OP_TYPE_STRING_INDEX:
			printDebugInfor("\"" + ResolveOpAsString(iOpIndex) + "\"");
			break;
		case OP_TYPE_INSTR_INDEX:
			printDebugInfor("" + Op.iData);
			break;
		case OP_TYPE_HOST_API_CALL_INDEX:
		{
			printDebugInfor("call host " + iOpIndex);
			break;
		}
		}
	}

	//�����̨�����Ϣ
	/**
	 * Prints the.
	 * 
	 * @param s
	 *            the s
	 */
	static void print(String s)
	{
		//    MiscUtil.println(s);
	}

	//���debug��Ϣ
	/**
	 * Prints the debug infor.
	 * 
	 * @param s
	 *            the s
	 */
	private static void printDebugInfor(String s)
	{
		//    C2D_MiscUtil.log(s);
	}
}
