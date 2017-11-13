package com.example.mayikang.wowallet.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mayikang.wowallet.event.WxPayEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.wx_pay_result_layout);
        
    	api = WXAPIFactory.createWXAPI(this, "wxebe6e5abd89af26b");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			//支付结果判定
			switch (resp.errCode){
				//成功
				case 0:
					EventBus.getDefault().post(new WxPayEvent(0));
					finish();
					break;
				//错误
				case -1:
					Log.e("onResp",resp.toString());
					EventBus.getDefault().post(new WxPayEvent(-1));
					finish();
					break;
				//取消
				case -2:
					EventBus.getDefault().post(new WxPayEvent(-2));
					finish();
					break;
			}

		}
	}
}