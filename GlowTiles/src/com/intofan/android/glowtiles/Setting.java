package com.intofan.android.glowtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting {
	public static SharedPreferences settings;

	public static boolean mixColors = true;
	public static boolean randomPattern = true;
	public static int pattern = 500;
	public static int speed = 10;
	public static int strokeWidth = 5;
	public static int tileSize = 120;
	public static int color1 = 0xff000000;
	public static int color2 = 0xffffffff;

	public static void loadPreferences(Context context) {
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		mixColors = settings.getBoolean("mixColors", mixColors);
		randomPattern = settings.getBoolean("randomPattern", randomPattern);
		pattern = settings.getInt("pattern", pattern);
		speed = settings.getInt("speed", speed);
		strokeWidth = settings.getInt("strokeWidth", strokeWidth);
		tileSize = settings.getInt("tileSize", tileSize);
		color1 = settings.getInt("color1", color1);
		color2 = settings.getInt("color2", color2);
	}
}
