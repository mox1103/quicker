package cn.hudp.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import cn.hudp.quicker.R;

/**
 * 上拉刷新下拉加载ListView
 * 
 * @author HuDP on 2015/06/19
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {
	/**
	 * 下拉刷新布局
	 */
	private View view_update;
	/**
	 * 底部布局
	 */
	private View view_footer;
	/**
	 * 第一个Itme
	 */
	private int firstVisibleItem;
	/**
	 * 最后一个可见的Item的Index
	 */
	private int lastVisibleItem;
	/**
	 * 总的Item的数量
	 */
	private int totalItemCount;
	/** 是否正在下拉刷新 */
	private boolean isPullDownLoading;
	/** 是否正在上拉加载 */
	private boolean isPullUpLoading;

	private IPullUpListener iPullUpListener;
	private IPullDownListener iPullDownListener;

	private long startTime;
	private long endTime;

	public MyListView(Context context) {
		super(context);
		initViews(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews(context);
	}

	private void initViews(Context mContext) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		view_update = mInflater.inflate(R.layout.header, null);
		view_footer = mInflater.inflate(R.layout.footer, null);
		view_footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		view_update.findViewById(R.id.update_layout).setVisibility(View.GONE);
		this.addHeaderView(view_update);
		/** 添加到ListView底部 */
		this.addFooterView(view_footer);
		/** 添加滚动监听事件 */
		this.setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
			if (!isPullUpLoading) {
				isPullUpLoading = true;
				view_footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
				if (iPullUpListener != null)
					iPullUpListener.onPullUpLoad();
			}
		}
		if (firstVisibleItem == 0 && scrollState == SCROLL_STATE_IDLE) {
			if (!isPullDownLoading) {
				isPullDownLoading = true;
				view_update.findViewById(R.id.update_layout).setVisibility(View.VISIBLE);
				if (iPullDownListener != null)
					iPullDownListener.onPullDownLoad();
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
		isPullDownLoading = false;
		isPullUpLoading = false;
		view_footer.setVisibility(View.GONE);
		view_update.setVisibility(View.GONE);
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
