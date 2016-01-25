package waps.extend;

import android.view.View;
import android.widget.LinearLayout;
import c2d.app.C2D_App;
import cn.waps.AppConnect;
import dreamland.tetris.alpha.R;

public class MiniAdv
{
	private static LinearLayout M_miniLayout;
	public static void showMiniAdv(C2D_App context)
	{
		// 迷你广告调用方式
		if(context==null)
		{
			return;
		}
		View mBarView = View.inflate(context, R.layout.adv_1, null);
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		context.addContentView(mBarView, layoutParams1);
		M_miniLayout = (LinearLayout) context.findViewById(R.id.scrollAdv);

		// AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120));//设置迷你广告背景颜色
		// AppConnect.getInstance(this).setAdForeColor(Color.YELLOW);//设置迷你广告文字颜色
		// M_miniLayout = (LinearLayout) context.findViewById(R.id.miniAdLinearLayout);
		
		AppConnect.getInstance(context).showMiniAd(context, M_miniLayout, 10);// 10秒刷新一次
	}
	public static void hideMiniAdv()
	{
		if(M_miniLayout!=null)
		{
			M_miniLayout.setVisibility(View.INVISIBLE);
		}
	}
}
