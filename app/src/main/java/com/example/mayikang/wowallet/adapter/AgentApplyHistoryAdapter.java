package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohaoliu on 2017/7/27.
 * explain:申请套餐的历史记录的adapter
 */

public class AgentApplyHistoryAdapter extends RecyclerView.Adapter<AgentApplyHistoryAdapter.AgentApplyHistoryHolder> {
    private Context mContext;
    private List<AgentBean> data;
    private LayoutInflater inflater;

    public AgentApplyHistoryAdapter(List<AgentBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AgentApplyHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_agent_history, null);

        return new AgentApplyHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(AgentApplyHistoryHolder holder, int position) {
        AgentBean bean=data.get(position);
        holder.agentHistoryTc.setText(bean.getName());
        //1 通过审核，2审核中 3,审核不通过4,套餐用完
        switch (bean.getAgetState()){
            case 1:
                holder.agentHistoryState.setText("购买成功");
                break;
            case 2:
                holder.agentHistoryState.setText("审核中");
                break;
            case 3:
                holder.agentHistoryState.setText("审核不通过");
                break;
            case 4:
                holder.agentHistoryState.setText("套餐用完");
                break;
        }
        holder.agentHistoryDate.setText(bean.getInsertTime());
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class AgentApplyHistoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.agent_history_tc)
        TextView agentHistoryTc;
        @BindView(R.id.agent_history_state)
        TextView agentHistoryState;
        @BindView(R.id.agent_history_date)
        TextView agentHistoryDate;

        public AgentApplyHistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
