package cn.hudp.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.hudp.quicker.R;

/**
 * 上拉刷新下拉加载ListView
 * 
 * @author HuDP on 2015/06/19
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {
	/** 下拉刷新布局 */
	private View view_header;
	/** 底部布局 */
	private View view_footer;
	private LinearLayout ll_header, ll_footer;
	/** 第一个Itme */
	private int firstVisibleItem;
	/** 最后一个可见的Item的Index */
	private int lastVisibleItem;
	/** 总的Item的数量 */
	private int totalItemCount;
	/** 是否正在下拉刷新 */
	private boolean isPullDownLoading;
	/** 是否正在上拉加载 */
	private boolean isPullUpLoading;
	private IPullUpListener iPullUpListener;
	private IPullDownListener iPullDownListener;
	private long startTime;
	private long endTime;
	private Handler mHandler = new Handler();

	public MyListView(Context context) {
		this(context, null);
	}

	public MyListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews(context);
	}

	private void initViews(Context mContext) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		view_header = mInflater.inflate(R.layout.header_layout, null);
		view_footer = mInflater.inflate(R.layout.footer_layout, null);
		ll_header = (LinearLayout) view_header.findViewById(R.id.ll_header);
		ll_footer = (LinearLayout) view_footer.findViewById(R.id.ll_footer);
//		ll_header.setVisibility(View.GONE);
		ll_footer.setVisibility(View.GONE);
		this.addHeaderView(view_header);
		this.addFooterView(view_footer);
		this.setOnScrollListener(this);
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
			if (!isPullUpLoading) {
				isPullUpLoading = true;
				ll_footer.setVisibility(View.VISIBLE);
				if (iPullUpListener != null) {
					startTime = System.currentTimeMillis();
					iPullUpListener.onPullUpLoad();
				}
			}
		}
		if (firstVisibleItem == 0 && scrollState == SCROLL_STATE_IDLE) {
			if (!isPullDownLoading) {
				isPullDownLoading = true;
				ll_header.setVisibility(View.VISIBLE);
				if (iPullDownListener != null) {
					startTime = System.currentTimeMillis();
					iPullDownListener.onPullDownLoad();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		/** 当前最后一个Item 就是屏幕可见的第一个Item + 当前可见Item的总数 */
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	/**
	 * 加载完毕 调用
	 */
	public void loadComplete() {
		endTime = System.currentTimeMillis();
		if (endTime - startTime < 2000) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					complete();
				}
			}, 2000 - (endTime - startTime));
		}else{
			complete();
		}
	}

	private void complete() {
		isPullDownLoading = false;
		isPullUpLoading = false;
		ll_footer.setVisibility(View.GONE);
		ll_header.setVisibility(View.GONE);
	}

	/**
	 * 获取下拉刷新接口
	 * 
	 * @param iUpdateListener
	 */
	public void setPullDownListener(IPullDownListener iPullDownListener) {
		if (iPullDownListener != null)
			this.iPullDownListener = iPullDownListener;
	}

	/**
	 * 获取上拉加载接口
	 * 
	 * @param iLoadListener
	 */
	public void setPullUpListener(IPullUpListener iPullUpListener) {
		if (iPullUpListener != null)
			this.iPullUpListener = iPullUpListener;
	}

	/**
	 * 下拉刷新接口
	 */
	public interface IPullDownListener {
		public void onPullDownLoad();
	}

	/**
	 * 上拉加载接口
	 */
	public interface IPullUpListener {
		public void onPullUpLoad();
	}
}
