package pay;

public class PayInfor
{
	String money;
	String description;
	String payFlag;
	PayEvent event;
	public PayInfor(String moneyT,String descriptionT,String payFlagT,PayEvent eventT)
	{
		money=moneyT;
		description=descriptionT;
		payFlag=payFlagT;
		event=eventT;
	}
}
