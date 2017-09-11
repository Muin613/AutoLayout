package com.mh.autolayout.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author ZhengJingle
 *
 */

public class Auto1Utils {

	private static int displayWidth;
	private static int displayHeight;
	
	private static int designWidth;
	private static int designHeight;
	
	private static double textPixelsRate;
	
	public static void setSize(Activity act, boolean hasStatusBar, int designWidth, int designHeight){
		if(act==null || designWidth<1 || designHeight<1)return;
		
        Display display = act.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
        int height = display.getHeight();
        
		if (hasStatusBar) {
			height -= getStatusBarHeight(act);
		}
		
		Auto1Utils.displayWidth=width;
		Auto1Utils.displayHeight=height;

		Auto1Utils.designWidth=designWidth;
		Auto1Utils.designHeight=designHeight;
		
		double displayDiagonal=Math.sqrt(Math.pow(Auto1Utils.displayWidth, 2)+Math.pow(Auto1Utils.displayHeight, 2));
		double designDiagonal=Math.sqrt(Math.pow(Auto1Utils.designWidth, 2)+Math.pow(Auto1Utils.designHeight, 2));
		Auto1Utils.textPixelsRate=displayDiagonal/designDiagonal;
	}
	
    public static int getStatusBarHeight(Context context)
    {
		int result = 0;
		try {
			int resourceId = context.getResources().getIdentifier(
					"status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = context.getResources().getDimensionPixelSize(
						resourceId);
			}
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}
		return result;
    }
    
    public static void auto(Activity act){
    	if(act==null || displayWidth<1 || displayHeight<1)return;
    	
    	View view=act.getWindow().getDecorView();
    	auto(view);
    }
    
    public static void auto(View view){
    	if(view==null || displayWidth<1 || displayHeight<1)return;

		Auto1Utils.autoTextSize(view);
		Auto1Utils.autoSize(view);
		Auto1Utils.autoPadding(view);
		Auto1Utils.autoMargin(view);
    	
    	if(view instanceof ViewGroup){
    		auto((ViewGroup)view);
    	}
    	
    }
    
    private static void auto(ViewGroup viewGroup){
    	int count = viewGroup.getChildCount();
    	
		for (int i = 0; i < count; i++) {

			View child = viewGroup.getChildAt(i);
			
			if(child!=null){
				auto(child);
			}
		}
    }
    
    public static void autoMargin(View view){
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
            return;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if(lp == null)return ;

        lp.leftMargin = getDisplayWidthValue(lp.leftMargin);
        lp.topMargin = getDisplayHeightValue(lp.topMargin);
        lp.rightMargin = getDisplayWidthValue(lp.rightMargin);
        lp.bottomMargin = getDisplayHeightValue(lp.bottomMargin);
        
    }

    public static void autoPadding(View view){
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();

        l = getDisplayWidthValue(l);
        t = getDisplayHeightValue(t);
        r = getDisplayWidthValue(r);
        b = getDisplayHeightValue(b);

        view.setPadding(l, t, r, b);
    }

    public static void autoSize(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (lp == null) return;
        
        if(lp.width>0){
        	lp.width = getDisplayWidthValue(lp.width);
        }

        if(lp.height>0){
        	lp.height = getDisplayHeightValue(lp.height);
        }
        
    }
    
    public static void autoTextSize(View view){
    	if(view instanceof TextView){
    		double designPixels=((TextView) view).getTextSize();
    		double displayPixels=textPixelsRate*designPixels;
    		((TextView) view).setIncludeFontPadding(false);
    		((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) displayPixels);
    	}
    }

    public static int getDisplayWidthValue(int designWidthValue){
    	if(designWidthValue<2){
    		return designWidthValue;
    	}
        return designWidthValue * displayWidth / designWidth;
    }

    public static int getDisplayHeightValue(int designHeightValue){
    	if(designHeightValue<2){
    		return designHeightValue;
    	}
        return designHeightValue * displayHeight / designHeight;
    }
}
