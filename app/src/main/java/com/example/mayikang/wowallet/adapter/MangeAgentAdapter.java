package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;
import com.example.mayikang.wowallet.modle.javabean.HomeData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XiaoHaoWit on 2017/9/22.
 */

public class MangeAgentAdapter extends DelegateAdapter.Adapter<MangeAgentAdapter.MangeAgentHolder> {

    private static final int TYPE_a=1;
    private static final int TYPE_b=2;

    private Context mContext;
    private ArrayList<HashMap<String,Object>> data;
    private LayoutInflater inflater;
    private LayoutHelper helper;
    private int type=0;
    private RecyclerView.LayoutParams params;

    public MangeAgentAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, LayoutHelper helper, int type, RecyclerView.LayoutParams params) {
        this.mContext = mContext;
        this.data = data;
        this.helper = helper;
        this.type = type;
        this.params = params;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MangeAgentAdapter(Context mContext,ArrayList<HashMap<String, Object>> data, LayoutHelper helper, int type){
        this(mContext,data,helper,type, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT) );
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    @Override
    public int getItemViewType(int position) {

        switch (type){
            case 1:
                return TYPE_a;
            case 2:
                return TYPE_b;
            default:
                return -1;
        }
    }

    @Override
    public MangeAgentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView=null;
        switch (viewType){
            case TYPE_a:
                mView=inflater.inflate(R.layout.agent_mange_lay_a,parent,false);
                break;
            case TYPE_b:
                mView=inflater.inflate(R.layout.anget_mange_lay_b,parent,false);
                break;
        }


        return new MangeAgentHolder(mView,viewType);
    }

    @Override
    public void onBindViewHolder(MangeAgentHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TYPE_a:
                HashMap<String, Object> dataMap = data.get(position);
                if(null!=dataMap&&dataMap.containsKey("data")){
                    AgentBean bean= (AgentBean) dataMap.get("data");
                    holder.agent_mange_tc.setText(bean.getName());
                    holder.agent_mange_storeNumber.setText(bean.getAgetState()+"");
                    holder.agent_mange_storeNumbers.setText(bean.getSurplusNum()+"");
                    holder.agent_mange_gmje.setText(bean.getTotAmount()+"");
                    if(bean.getId()==0){
                        //没有数据的布局
                        holder.agent_mange_lay_a_nodata.setVisibility(View.VISIBLE);
                    }else {
                        //有数据的布局
                        holder.agent_mange_lay_a_nodata.setVisibility(View.GONE);
                    }
                }
                break;
            case TYPE_b:
                HashMap<String, Object> typeBmap = data.get(position);

                if(null!=typeBmap&&typeBmap.containsKey("data")){
                    final AgentBean typeBena= (AgentBean) typeBmap.get("data");
                    holder.agent_item_storeName.setText(typeBena.getName());
                    holder.agent_item_time.setText(typeBena.getInsertTime()+"");
                    holder.agent_item_state.setText(typeBena.getStoreState()+"");

                    holder.anget_mange_ay_b_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(typeBena!=null&&typeBena.getId()!=0){
                                ARouter.getInstance().build("/main/act/store").withInt("id",typeBena.getId()).navigation();
                            }
                        }
                    });
                }



                break;
        }


    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static  class MangeAgentHolder extends RecyclerView.ViewHolder{
        //typeA 的控件
        TextView agent_mange_tc;
        TextView agent_mange_storeNumber;
        TextView agent_mange_storeNumbers;
        TextView agent_mange_gmje;
        LinearLayout agent_mange_lay_a_nodata;
        //typeB 的控件
        TextView agent_item_storeName;
        TextView agent_item_time;
        TextView agent_item_state;
        LinearLayout anget_mange_ay_b_layout;
        

        public MangeAgentHolder(View root,int viewType) {
            super(root);
            switch (viewType){
                case TYPE_a:
                    agent_mange_tc= (TextView) root.findViewById(R.id.agent_mange_tc);
                    agent_mange_storeNumber= (TextView) root.findViewById(R.id.agent_mange_storeNumber);
                    agent_mange_storeNumbers= (TextView) root.findViewById(R.id.agent_mange_storeNumbers);
                    agent_mange_gmje= (TextView) root.findViewById(R.id.agent_mange_gmje);
                    agent_mange_lay_a_nodata= (LinearLayout) root.findViewById(R.id.agent_mange_lay_a_nodata);
                    break;
                case TYPE_b:
                    agent_item_storeName= (TextView) root.findViewById(R.id.agent_item_storeName);
                    agent_item_time= (TextView) root.findViewById(R.id.agent_item_time);
                    agent_item_state= (TextView) root.findViewById(R.id.agent_item_state);
                    anget_mange_ay_b_layout= (LinearLayout) root.findViewById(R.id.anget_mange_ay_b_layout);
                    break;
            }
        }
    }
}
