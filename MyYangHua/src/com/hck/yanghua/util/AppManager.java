package com.hck.yanghua.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
public class AppManager {
	
	private static List<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){}
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new ArrayList<Activity>();
		}
	     activityStack.add(activity);
	}
	public void AppExit() {
		try {
			
			for (int i = 0; i < activityStack.size(); i++) {
				((Activity)activityStack.get(i)).finish();
			}
			System.exit(0);
		} catch (Exception e) {	}
	}
}