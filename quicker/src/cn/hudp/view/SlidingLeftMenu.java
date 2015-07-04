package cn.hudp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import cn.hudp.quicker.R;

/**
 * 左菜单滑动
 * 
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015年6月29日
 */
public class SlidingLeftMenu extends HorizontalScrollView {
	private int mScreenWidth; // 屏幕宽度
	private int mMenuRightPadding = 40; // dp
	private int mMenuWidth;// 菜单宽度
	private int moveScroolX; // 移动多少发生跳动
	private boolean once;
	private boolean isOpen;
	private ViewGroup content;

	public SlidingLeftMenu(Context context) {
		this(context, null);
	}

	public SlidingLeftMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingLeftMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mScreenWidth = getScreenWidth(context);
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingLeftMenu, defStyleAttr, 0);
		int n = ta.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
//			case R.styleable.SlidingLeftMenu_rightPadding:
//				mMenuRightPadding = ta.getDimensionPixelSize(attr, (int) TypedValue
//						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));
//				break;
			}
		}
		ta.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/** 设置宽度 */
		if (!once) {
			LinearLayout ll = (LinearLayout) getChildAt(0);
			ViewGroup menu = (ViewGroup) ll.getChildAt(0);
			content = (ViewGroup) ll.getChildAt(1);
			// dp 转化为 px
			mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding,
					content.getResources().getDisplayMetrics());
			mMenuWidth = mScreenWidth - mMenuRightPadding;
			menu.getLayoutParams().width = mMenuWidth;
			content.getLayoutParams().width = mScreenWidth;
			moveScroolX = mMenuWidth / 2;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
		content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOpen) {
					smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				}
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > moveScroolX) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 切换菜单状态
	 */
	public void toggleMenu() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		} else {
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return screenWidth
	 */
	private int getScreenWidth(Context mContext) {
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}
}
