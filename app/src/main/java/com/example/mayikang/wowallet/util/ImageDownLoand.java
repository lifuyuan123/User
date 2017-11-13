package com.example.mayikang.wowallet.util;

import android.util.Log;

import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.cookie.CookieUtil;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaoHaoWit on 2017/9/15.
 */

public class ImageDownLoand {
    private OkHttpClient httpClient;


    public void  downImg( String url){
        httpClient=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Log.e("save","进入");
                        Cookie cookie=cookies.get(0);
                        Log.e("picasso-获取-cookie",cookie.toString());
                        Log.e("picasso-获取-name",cookie.name());
                        Log.e("picasso-获取-domain",cookie.domain());
                        Log.e("picasso-获取-path",cookie.path());
                        Log.e("picasso-获取-value",cookie.value());
                        Log.e("picasso","---------------------");

                        HttpCookie hc=new HttpCookie(cookie.name(),cookie.value());
                        hc.setDomain(cookie.domain());
                        hc.setPath(cookie.path());
                        Log.e("hc-cookie",hc.toString());
                        Log.e("hc-value",hc.getValue());
                        Log.e("hc-name",hc.getName());
                        Log.e("hc-domain",hc.getDomain());
                        Log.e("hc-path",hc.getPath());
                        Log.e("hc","---------------------");
                        //更新本地 cookie
                        CookieUtil.instance().updateCookie(URI.create(url.toString()),hc);

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {

                        List<Cookie> cookieList = new ArrayList<>();
                        //获取本地持久化 cookie
                        HttpCookie hc=CookieUtil.instance().getLocalCookie();
                        //本地无 Cookie 时返回空
                        if(hc!=null){
                            Cookie cookie=new Cookie.Builder()
                                    .name(hc.getName())
                                    .value(hc.getValue())
                                    .domain(hc.getDomain())
                                    .path(hc.getPath())
                                    .build();
                            cookieList.add(cookie);
                        }

                        return cookieList;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request.Builder request = chain.request().newBuilder();
                        //注入 token
                        request.addHeader("TOKEN", SPFUtil.get(Tag.TAG_TOKEN,"none-token").toString());

                        return chain.proceed(request.build());


                    }
                }).build();



    }

}
