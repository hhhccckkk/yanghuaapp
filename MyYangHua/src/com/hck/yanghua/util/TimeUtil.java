package com.hck.yanghua.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";

	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;

	public static String forTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			return "未知";
		}
		long delta = new Date().getTime() - date.getTime();
		if (delta < 1L * ONE_MINUTE) {
			long seconds = toSeconds(delta);
			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
		} else if (delta < 45L * ONE_MINUTE) {
			long minutes = toMinutes(delta);
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		} else if (delta < 24L * ONE_HOUR) {
			long hours = toHours(delta);
			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
		} else {
			return time.substring(0, 11);
		}

	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}


}
