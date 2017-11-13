package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
@Route(path = "/main/act/user/AgentApplyStateActivity")
public class AgentApplyStateActivity extends BaseAppcompatActivity {

    @BindView(R.id.agent_apply_state_back)
    RelativeLayout agentApplyStateBack;
    @BindView(R.id.agent_apply_state_refish)
    RelativeLayout agentApplyStateRefish;
    @BindView(R.id.agent_apply_state_txt)
    TextView agentApplyStateTxt;
    private HttpServiceImpl service;
    @Autowired(name = "id")
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_agent_apply_state;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAgentState();
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.agent_apply_state_back, R.id.agent_apply_state_refish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agent_apply_state_back:
                finish();
                break;
            case R.id.agent_apply_state_refish:
                getAgentState();
                break;
        }
    }
    //获取审核状态
    private void getAgentState() {
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","agentApp");
        body.put("id",id+"");
        body.put("jf","applyAp");
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAgentState",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            JSONObject data=object.getJSONObject("data");
                            int status=data.getInt("status");
                            JSONObject applyAp=data.getJSONObject("applyAp");
                            String name=applyAp.getString("name");
                            agentApplyStateTxt.setText("代理\""+name+"\"申请成功，请等待管理员审核！");
                            switch (status){
                                case 1:
                                    Toast.makeText(AgentApplyStateActivity.this, "通过审核", Toast.LENGTH_SHORT).show();
                                    ARouter.getInstance().build("/main/user/act/AgentMangeActivity").navigation();
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(AgentApplyStateActivity.this, "请等待管理员审核！", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    finish();
                                    Toast.makeText(AgentApplyStateActivity.this, "套餐申请被拒绝！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getAgentState",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }
}
