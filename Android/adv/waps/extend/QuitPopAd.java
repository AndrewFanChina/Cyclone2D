package waps.extend;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import c2d.app.C2D_App;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;

public class QuitPopAd
{

	private static Dialog dialog;

	private static QuitPopAd quitPopAd;

	public static QuitPopAd getInstance()
	{
		if (quitPopAd == null)
		{
			quitPopAd = new QuitPopAd();
		}
		return quitPopAd;
	}

	/**
	 * չʾ�������
	 * 
	 * @param context
	 */
	public static void ShutDown(final C2D_App context)
	{
		final QuitPopAd quitPopAd = QuitPopAd.getInstance();
		C2D_App.getApp().getHandler().post(new Runnable()
		{
			public void run()
			{
				dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);// �ڶ�����ʽ����,�ɸ����Լ�Ӧ�û���Ϸ�еĲ��ֽ�������
				View view = null;
				// �жϲ�������Ƿ��ѳ�ʼ����ɣ�����ȷ���Ƿ��ܳɹ����ò������
				if (AppConnect.getInstance(context).hasPopAd(context))
				{
					if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
					{
						view = quitPopAd.getQuitView_Portrait(context, dialog);
					}
					else
					{
						view = quitPopAd.getQuitView_Landscape(context, dialog);
					}

				}
				if (view != null)
				{
					ArrayList<View> views = view.getTouchables();
					Button quitBtn=(Button)views.get(1);
					quitBtn.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							C2D_App.ShutDown();
						}
					});
					dialog.setContentView(view);
					dialog.show();
				}
				else
				{
					new AlertDialog.Builder(context).setTitle("�˳���ʾ").setMessage("ȷ��Ҫ�˳���ǰӦ����").setPositiveButton("ȷ��", new AlertDialog.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							if (dialog != null)
							{
								dialog.cancel();
							}
							C2D_App.ShutDown();
						}
					}).setNegativeButton("ȡ��", new AlertDialog.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
						}
					}).create().show();

				}

			}
		});

	}

	/**
	 * �ر��������Ի���
	 */
	public void close()
	{
		if (dialog != null && dialog.isShowing())
		{
			dialog.cancel();
		}
	}

	/**
	 * ��ȡ������ʽ���˳�����
	 * 
	 * @param context
	 * @param dialog
	 *            �����˳����ֵ�dialog
	 * @return
	 */
	private LinearLayout getQuitView_Portrait(final Context context, final Dialog dialog)
	{
		// ��С���ֻ�������Ļ�ж�
		int displaySize = SDKUtils.getDisplaySize(context);

		// ���ñ��Ⲽ�ֵ���������ΪԲ��
		float num = 10f;
		float[] outerR = new float[]
		{ num, num, num, num, 0, 0, 0, 0 };
		ShapeDrawable title_layout_shape = new ShapeDrawable(new RoundRectShape(outerR, null, null));
		title_layout_shape.getPaint().setColor(Color.argb(240, 10, 10, 10));

		// ���ð�ť���ֵ������׽�ΪԲ��
		float[] outerR2 = new float[]
		{ 0, 0, 0, 0, num, num, num, num };
		ShapeDrawable btn_layout_shape = new ShapeDrawable(new RoundRectShape(outerR2, null, null));
		btn_layout_shape.getPaint().setColor(Color.argb(240, 20, 20, 20));

		// ����㲼��
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.argb(80, 0, 0, 0));
		layout.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.VERTICAL);

		// �����ŷű��⣬popAd�Ĳ���
		final RelativeLayout r_layout = new RelativeLayout(context);
		r_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		r_layout.setGravity(Gravity.CENTER);

		// ���Ⲽ��
		LinearLayout title_layout = new LinearLayout(context);
		TextView textView = new TextView(context);
		textView.setText("ȷ��Ҫ�˳���");
		textView.setTextSize(18);
		textView.setTextColor(Color.WHITE);
		title_layout.setId((int) (System.currentTimeMillis()));
		if (displaySize == 320)
		{
			title_layout.setPadding(10, 10, 0, 10);
		}
		else if (displaySize == 240)
		{
			title_layout.setPadding(10, 5, 0, 5);
		}
		else
		{
			title_layout.setPadding(15, 15, 0, 15);
		}
		title_layout.setBackgroundDrawable(title_layout_shape);

		title_layout.addView(textView);

		// ��ȡ��������
		LinearLayout pop_layout = AppConnect.getInstance(context).getPopAdView(context);

		if (pop_layout == null)
		{
			return null;
		}

		pop_layout.setBackgroundColor(Color.argb(200, 40, 40, 40));
		pop_layout.setId((int) (System.currentTimeMillis() + 1));
		pop_layout.setPadding(5, 0, 5, 0);

		// ��ť�鲼��
		LinearLayout btn_layout = new LinearLayout(context);
		btn_layout.setGravity(Gravity.CENTER);
		btn_layout.setOrientation(LinearLayout.HORIZONTAL);
		btn_layout.setPadding(3, 10, 3, 10);
		btn_layout.setBackgroundDrawable(btn_layout_shape);
		btn_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		Button okButton = new Button(context);
		okButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1F));
		okButton.setText(" �� �� ");
		okButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (dialog != null)
				{
					dialog.cancel();
				}
				((Activity) context).finish();
			}
		});

		Button cancelButton = new Button(context);
		cancelButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1F));
		cancelButton.setText(" ȡ �� ");
		cancelButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.cancel();
			}
		});

		Button moreButton = new Button(context);
		moreButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1F));
		moreButton.setText(" �� �� ");
		moreButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AppConnect.getInstance(context).showOffers(context);
				if (dialog != null)
				{
					dialog.cancel();
				}
			}
		});

		btn_layout.addView(okButton);
		btn_layout.addView(cancelButton);
		btn_layout.addView(moreButton);

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_LEFT, pop_layout.getId());
		params1.addRule(RelativeLayout.ALIGN_RIGHT, pop_layout.getId());

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.BELOW, title_layout.getId());

		r_layout.addView(title_layout, params1);
		r_layout.addView(pop_layout, params2);

		// �����ŷ�r_layout(�����popAd����)�Ͱ�ť�Ĳ���
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		l_layout.setOrientation(LinearLayout.VERTICAL);
		l_layout.addView(r_layout);
		l_layout.addView(btn_layout);

		layout.addView(l_layout);

		dialog.setOnCancelListener(new OnCancelListener()
		{

			@Override
			public void onCancel(DialogInterface dialog)
			{
				r_layout.removeAllViews();
			}
		});

		return layout;
	}

	/**
	 * ��ȡ������ʽ���˳�����
	 * 
	 * @param context
	 * @param dialog
	 *            �����˳����ֵ�dialog
	 * @return
	 */
	private LinearLayout getQuitView_Landscape(final Context context, final Dialog dialog)
	{

		// ���ñ��Ⲽ�ֵ���������ΪԲ��
		float num = 10f;
		float[] outerR = new float[]
		{ num, num, 0, 0, 0, 0, num, num };
		ShapeDrawable title_layout_shape = new ShapeDrawable(new RoundRectShape(outerR, null, null));
		title_layout_shape.getPaint().setColor(Color.argb(200, 10, 10, 10));

		// ���ð�ť���ֵ������׽�ΪԲ��
		float[] outerR2 = new float[]
		{ 0, 0, num, num, num, num, 0, 0 };
		ShapeDrawable btn_layout_shape = new ShapeDrawable(new RoundRectShape(outerR2, null, null));
		btn_layout_shape.getPaint().setColor(Color.argb(200, 20, 20, 20));

		// ����㲼��
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.argb(80, 0, 0, 0));
		layout.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		// �����ŷű��⣬popAd����ť������岼��
		final RelativeLayout r_layout = new RelativeLayout(context);
		r_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		r_layout.setGravity(Gravity.CENTER);

		// ���Ⲽ��
		LinearLayout title_layout = new LinearLayout(context);
		TextView textView = new TextView(context);
		textView.setText("ȷ��Ҫ�˳���");
		textView.setTextSize(18);
		textView.setEms(1);
		textView.setTextColor(Color.WHITE);
		title_layout.setId((int) (System.currentTimeMillis()));
		title_layout.setPadding(10, 10, 10, 0);
		title_layout.setBackgroundDrawable(title_layout_shape);

		title_layout.addView(textView);

		LinearLayout pop_layout = null;
		// ��ȡ��������
		int height_full = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

		int height_tmp = height_full - 75;// 75Ϊ�豸״̬���ӱ������ĸ߶�

		int height = height_tmp - 55;// 55Ϊ�Զ���
		if (height_full <= 480)
		{
			pop_layout = AppConnect.getInstance(context).getPopAdView(context, height, height);
		}
		else
		{
			pop_layout = AppConnect.getInstance(context).getPopAdView(context);
		}

		if (pop_layout == null)
		{
			return null;
		}
		pop_layout.setBackgroundColor(Color.argb(200, 40, 40, 40));
		pop_layout.setId((int) (System.currentTimeMillis() + 1));
		pop_layout.setPadding(2, 0, 2, 0);

		// ��ť�鲼��
		LinearLayout btn_layout = new LinearLayout(context);
		btn_layout.setOrientation(LinearLayout.VERTICAL);
		btn_layout.setBackgroundDrawable(btn_layout_shape);
		btn_layout.setPadding(3, 8, 3, 3);
		btn_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

		// ��ť�����ж������Ӳ���
		LinearLayout top_layout = new LinearLayout(context);
		top_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		top_layout.setOrientation(LinearLayout.VERTICAL);
		top_layout.setGravity(Gravity.TOP);
		// ��ť�����еײ����Ӳ���
		LinearLayout bottom_layout = new LinearLayout(context);
		bottom_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		bottom_layout.setGravity(Gravity.BOTTOM);

		Button okButton = new Button(context);
		okButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		okButton.setText(" �� �� ");
		okButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (dialog != null)
				{
					dialog.cancel();
				}
				((Activity) context).finish();
			}
		});

		Button cancelButton = new Button(context);
		cancelButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		cancelButton.setText(" ȡ �� ");
		cancelButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.cancel();
			}
		});

		Button moreButton = new Button(context);
		moreButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		moreButton.setText(" �� �� ");
		moreButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AppConnect.getInstance(context).showOffers(context);
				if (dialog != null)
				{
					dialog.cancel();
				}
			}
		});

		top_layout.addView(okButton);
		top_layout.addView(cancelButton);

		bottom_layout.addView(moreButton);

		btn_layout.addView(top_layout);
		btn_layout.addView(bottom_layout);

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_TOP, pop_layout.getId());
		params1.addRule(RelativeLayout.ALIGN_BOTTOM, pop_layout.getId());

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.RIGHT_OF, title_layout.getId());

		r_layout.addView(title_layout, params1);
		r_layout.addView(pop_layout, params2);

		// �����ŷ�r_layout(�����popAd����)�Ͱ�ť�Ĳ���
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		l_layout.setOrientation(LinearLayout.HORIZONTAL);
		l_layout.addView(r_layout);
		l_layout.addView(btn_layout);

		layout.addView(l_layout);

		dialog.setOnCancelListener(new OnCancelListener()
		{

			@Override
			public void onCancel(DialogInterface dialog)
			{
				r_layout.removeAllViews();
			}
		});

		return layout;
	}

	private class ShowQuitPopAdTask extends AsyncTask<Void, Void, LinearLayout>
	{
		Context context;
		Dialog dialog;

		ShowQuitPopAdTask(Context context, Dialog dialog)
		{
			this.context = context;
			this.dialog = dialog;
		}

		@Override
		protected LinearLayout doInBackground(Void... params)
		{

			LinearLayout pop_layout = null;

			if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			{
				pop_layout = getQuitView_Portrait(context, dialog);
			}
			else
			{
				pop_layout = getQuitView_Landscape(context, dialog);
			}

			return pop_layout;
		}

		@Override
		protected void onPostExecute(LinearLayout result)
		{
			if (result != null)
			{
				dialog.setContentView(result);
				dialog.show();
			}
		}
	}
}
