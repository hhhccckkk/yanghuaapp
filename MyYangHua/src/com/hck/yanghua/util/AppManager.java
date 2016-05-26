package com.hck.yanghua.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;

public class AppManager {

	private static Set<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new HashSet<>();
		}
		activityStack.add(activity);
	}

	public void AppExit() {
		try {

			for (Activity activity : activityStack) {
				activity.finish();
			}
			System.exit(0);
		} catch (Exception e) {
		}
	}

	public void removeActivity(Activity activity) {
		if (activityStack != null) {
			activityStack.remove(activity);
		}
	}
}