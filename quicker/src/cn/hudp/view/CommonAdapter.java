package cn.hudp.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用Adapter
 * 
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015年7月1日
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mList;
	protected int mItemLayoutId;

	public CommonAdapter(Context context, List<T> list, int itemLayoutId) {
		this.mContext = context;
		this.mList = list;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}

	public abstract void convert(ViewHolder viewHolder, T itemData);

	public void onDateChange(List<T> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}
}
