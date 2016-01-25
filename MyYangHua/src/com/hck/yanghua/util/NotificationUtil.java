package com.hck.yanghua.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.hck.yanghua.R;

public class NotificationUtil {
	public static void  showNotification(Context context, int type, Intent intent,
			String titleText, String tickerText, String contentText) {
		try {
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					context);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					type, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification notification = builder
					.setSmallIcon(R.drawable.icon)
					.setContentText(contentText)
					.setTicker(tickerText)
					.setContentTitle(titleText)
					.setContentIntent(pendingIntent)
					.setAutoCancel(true)
					.setDefaults(
							Notification.DEFAULT_SOUND
									| Notification.DEFAULT_VIBRATE).build();
			manager.notify(type, notification);
		} catch (Exception e) {
		}
	}
}
