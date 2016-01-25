package waps.extend;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;

public class LoadingPopAd {
	
	private final static Handler mHandler = new Handler();
	private static LoadingPopAd loadingAppPopAd;
	
	public static LoadingPopAd getInstance(){
		if(loadingAppPopAd == null){
			loadingAppPopAd = new LoadingPopAd();
		}
		if (Looper.myLooper() == null) {
			Looper.prepare();
		}
		return loadingAppPopAd;
	}
	
	/**
	 * 获取开屏布局
	 * @param context
	 * @param time
	 * @return
	 */
	public View getContentView(Context context, int time){
		
		return getLoadingLayout(context, time);
	}
	
	private LinearLayout getLoadingLayout(final Context context, final int time){
		
		// 整体布局
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		int bg_id = context.getResources().getIdentifier("loading_bg", "drawable", context.getPackageName());
		if(bg_id != 0){
			layout.setBackgroundResource(bg_id);
		}
		// 加载广告图片和倒计时的布局,用与
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setGravity(Gravity.CENTER);
		// 设置LayoutParams,划分额外空间比例为6分之5(具体权重比例可根据自己需求自定义)
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f));
		// 加载图片的布局
		RelativeLayout pop_layout = new RelativeLayout(context);
		
		TextView timeView = new TextView(context);
		timeView.setText("剩余" + time + "秒");
		timeView.setTextSize(10);
		timeView.setTextColor(Color.BLACK);
		timeView.setPadding(8, 3, 6, 2);
		
		int num = 12;
		// 对手机进行屏幕判断
		int displaySize = SDKUtils.getDisplaySize(context);
		if(displaySize == 320){
			num = 8;
		}else if(displaySize == 240){
			num = 6;
		}else if(displaySize == 720){
			num = 16;
		}else if(displaySize == 1080){
			num = 20;
		}
		float[] outerRadii = new float[] { 0, 0, num, num, 0, 0, num, num};
		ShapeDrawable timeView_shapeDrawable = new ShapeDrawable();
		timeView_shapeDrawable.setShape(new RoundRectShape(outerRadii, null, null));
		timeView_shapeDrawable.getPaint().setColor(Color.argb(255, 255, 255, 255));
		timeView.setBackgroundDrawable(timeView_shapeDrawable);
		//异步执行倒计时
		//异步加载广告图片
		new ShowPopAdTask(context, pop_layout, timeView).execute();
		new TimeCountDownTask(timeView, time).execute();
		
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 5f));
		textView.setText("正在启动,请稍后...");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.WHITE);
		
		l_layout.addView(pop_layout);
		
		layout.addView(l_layout);
		layout.addView(textView);
		return layout;
	}
	
	private class TimeCountDownTask extends AsyncTask<Void, Void, Boolean>{
		
		TextView timeView;
		int limit_time = 0;
		TimeCountDownTask(TextView timeView, int time){
		this.timeView = timeView;
		this.limit_time = time;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			
			while(limit_time > 0){
				mHandler.post(new Runnable(){
					@Override
					public void run() {
						timeView.setText("剩余" + limit_time + "秒");
					}
				});
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				limit_time--;
			}
			return null;
		}
	}
	
	private class ShowPopAdTask extends AsyncTask<Void, Void, Boolean>{
		
		Context context;
		RelativeLayout pop_layout;
		LinearLayout popAdView;
		TextView timeView;
		int height_full = 0;
		int height = 0;
		ShowPopAdTask(Context context, RelativeLayout pop_layout, TextView timeView){
			this.context = context;
			this.pop_layout = pop_layout;
			this.timeView = timeView;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				height_full = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
				
				int height_tmp = height_full - 75;//75为设备状态栏加标题栏的高度
				
				height = height_tmp * 5/6;
				
				while(true){
					if(((Activity)context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
						&& height_full <= 480){
						popAdView = AppConnect.getInstance(context).getPopAdView(context, height, height);
					}else{
						popAdView = AppConnect.getInstance(context).getPopAdView(context);
					}
					if(popAdView != null){
						mHandler.post(new Runnable(){
				
							@Override
							public void run() {
								pop_layout.addView(popAdView);
								
								popAdView.setId(1);
								//倒计时布局所需的LayoutParams
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_TOP, popAdView.getId());
								params.addRule(RelativeLayout.ALIGN_RIGHT, popAdView.getId());
								// 对手机进行屏幕判断
								int displaySize = SDKUtils.getDisplaySize(context);
								if(displaySize == 320){
									params.topMargin=1;
									params.rightMargin=1;
								}else if(displaySize == 240){
									params.topMargin=1;
									params.rightMargin=1;
								}else if(displaySize == 720){
									params.topMargin=3;
									params.rightMargin=3;
								}else if(displaySize == 1080){
									params.topMargin=4;
									params.rightMargin=4;
								}else{
									params.topMargin=2;
									params.rightMargin=2;
								}
								pop_layout.addView(timeView, params);
							}
						});
						break;
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
