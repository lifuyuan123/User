package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by haohaoliu on 2017/7/10.
 * explain:选择成为代理商得adapter
 */

public class ChooseAgentAdapter extends BaseExpandableListAdapter{
    private List<AgentBean> data;
    private List<List<AgentBean>> child;
    private LayoutInflater inflater;
    private Context mContext;

    public ChooseAgentAdapter(List<AgentBean> data, List<List<AgentBean>> child, Context mContext) {
        this.data = data;
        this.child = child;
        this.mContext = mContext;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return data.size()<0?0:data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final AgentBean bean=data.get(groupPosition);
        convertView =inflater.inflate(R.layout.item_agent_parent,null);
        final ImageView agent_parent_choose= (ImageView) convertView.findViewById(R.id.agent_parent_choose);
        RelativeLayout agent_parent_choose_layout= (RelativeLayout) convertView.findViewById(R.id.agent_parent_choose_layout);
        TextView agent_parent_tc= (TextView) convertView.findViewById(R.id.agent_parent_tc);
        RelativeLayout agent_parent_unfold_layout= (RelativeLayout) convertView.findViewById(R.id.agent_parent_unfold_layout);
        ImageView agent_parent_unfold= (ImageView) convertView.findViewById(R.id.agent_parent_unfold);

        agent_parent_tc.setText(bean.getName());
        agent_parent_choose_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.isCheck()){
                    bean.setCheck(false);
                    agent_parent_choose.setImageResource(R.drawable.ic_nocheck);
                }else {
                    bean.setCheck(true);
                    agent_parent_choose.setImageResource(R.drawable.ic_check);
                }
            }
        });

        if(bean.isCheck()){//选中
            agent_parent_choose.setImageResource(R.drawable.ic_check);
        }else {//未选中
            agent_parent_choose.setImageResource(R.drawable.ic_nocheck);
        }

        if(isExpanded){
            agent_parent_unfold.setImageResource(R.drawable.ico_listup);
        }else {
            agent_parent_unfold.setImageResource(R.drawable.ico_listdown);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AgentBean bean=child.get(groupPosition).get(childPosition);
        convertView=inflater.inflate(R.layout.item_agent_child,null);
        TextView agent_child_price= (TextView) convertView.findViewById(R.id.agent_child_price);
        TextView agent_child_storeNumber= (TextView) convertView.findViewById(R.id.agent_child_storeNumber);
        TextView agent_child_storePrice= (TextView) convertView.findViewById(R.id.agent_child_storePrice);
        agent_child_price.setText(new DecimalFormat("######0.00").format(bean.getValue())+"");
        agent_child_storeNumber.setText(bean.getTotNum()+"");
        agent_child_storePrice.setText(new DecimalFormat("######0.00").format(bean.getTotAmount())+"");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public List<AgentBean> getGroupData(){
        return data;
    }
}
