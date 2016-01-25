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
	 * ��ȡ��������
	 * @param context
	 * @param time
	 * @return
	 */
	public View getContentView(Context context, int time){
		
		return getLoadingLayout(context, time);
	}
	
	private LinearLayout getLoadingLayout(final Context context, final int time){
		
		// ���岼��
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		int bg_id = context.getResources().getIdentifier("loading_bg", "drawable", context.getPackageName());
		if(bg_id != 0){
			layout.setBackgroundResource(bg_id);
		}
		// ���ع��ͼƬ�͵���ʱ�Ĳ���,����
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setGravity(Gravity.CENTER);
		// ����LayoutParams,���ֶ���ռ����Ϊ6��֮5(����Ȩ�ر����ɸ����Լ������Զ���)
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f));
		// ����ͼƬ�Ĳ���
		RelativeLayout pop_layout = new RelativeLayout(context);
		
		TextView timeView = new TextView(context);
		timeView.setText("ʣ��" + time + "��");
		timeView.setTextSize(10);
		timeView.setTextColor(Color.BLACK);
		timeView.setPadding(8, 3, 6, 2);
		
		int num = 12;
		// ���ֻ�������Ļ�ж�
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
		//�첽ִ�е���ʱ
		//�첽���ع��ͼƬ
		new ShowPopAdTask(context, pop_layout, timeView).execute();
		new TimeCountDownTask(timeView, time).execute();
		
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 5f));
		textView.setText("��������,���Ժ�...");
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
						timeView.setText("ʣ��" + limit_time + "��");
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
				
				int height_tmp = height_full - 75;//75Ϊ�豸״̬���ӱ������ĸ߶�
				
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
								//����ʱ���������LayoutParams
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_TOP, popAdView.getId());
								params.addRule(RelativeLayout.ALIGN_RIGHT, popAdView.getId());
								// ���ֻ�������Ļ�ж�
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
