package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohaoliu on 2017/7/13.
 * explain: 代理商管理得适配器
 */

public class AgentMangeAdapter extends BaseAdapter {
    private List<AgentBean> data;
    private LayoutInflater inflater;
    private Context mContext;

    public AgentMangeAdapter(List<AgentBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_agent_mange, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        AgentBean bean=data.get(position);
        holder.agentItemStoreName.setText(bean.getName());
        holder.agentItemTime.setText(bean.getInsertTime());
        holder.agentItemState.setText(bean.getStoreState());
        return convertView;
    }





    static class ViewHolder {
        @BindView(R.id.agent_item_storeName)
        TextView agentItemStoreName;
        @BindView(R.id.agent_item_time)
        TextView agentItemTime;
        @BindView(R.id.agent_item_state)
        TextView agentItemState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
