package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bm.library.PhotoView;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.HttpTask.XDownloadCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.ScreenUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


@Route(path = "/main/act/gallery_scan")
public class GalleryScanActivity extends BaseAppcompatActivity {


    @BindView(R.id.vp)
    ViewPager vp;


    public List<String> data=new ArrayList<>();

    @Autowired(name = "index")
    public int index=0;

    @Autowired(name = "data")
    public Bundle bundle;

    @BindView(R.id.indicator)TextView tvIndicator;


    private PagerAdapter adapter;
    private HttpServiceImpl http;

    private OkHttpClient client;

    private  OkHttpDownloader downloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        data=bundle.getStringArrayList("list");
        initImageLoad();
        initVP();


        if(data!=null){
            vp.setAdapter(adapter);
            vp.setOffscreenPageLimit(9);
            if(index<=data.size()){
                vp.setCurrentItem(index,true);
                tvIndicator.setText((index+1)+"/"+data.size());
            }
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    index=position;
                    tvIndicator.setText((index+1)+"/"+data.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }
    //初始化加载图片
    private void initImageLoad() {
        client=new OkHttpClient();
        downloader=new OkHttpDownloader(client);


    }

    @Override
    public int initLayout() {
        return R.layout.activity_gallery_scan;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }


    private void initVP(){
        vp.setPageMargin(40);
        if(adapter==null){
            adapter=new PagerAdapter() {
                @Override
                public int getCount() {
                    return data==null?0:data.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view==object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, final int position) {
                    PhotoView view = new PhotoView(GalleryScanActivity.this);
                    view.enable();
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            final CommonDialog dia=new CommonDialog(GalleryScanActivity.this);
                            dia.setTitle("保存图片");
                            dia.setContent("下载图片到本地保存？");
                            dia.setConfirmClickListener("确认", new CommonDialog.ConfirmClickListener() {
                                @Override
                                public void clickConfirm() {
                                     http.downloadFile(null,data.get(position),null,null,new XDownloadCallback(){
                                         @Override
                                         public void onDownloadComplete(File file) {
                                             LogUtil.e("xiazaiwancheng");
                                             if(file!=null){
                                                 Toast.makeText(GalleryScanActivity.this, "已保存至 sd卡/com.example.mayikang.wowallet/download路径下", Toast.LENGTH_SHORT).show();
                                             }else {
                                                 Toast.makeText(GalleryScanActivity.this, "null", Toast.LENGTH_SHORT).show();

                                             }
                                         }

                                         @Override
                                         public void onError(Throwable ex) {
                                            LogUtil.e("error-"+ex.toString());
                                         }

                                         @Override
                                         public void onCancelled(org.xutils.common.Callback.CancelledException cex) {

                                         }

                                         @Override
                                         public void onFinished() {
                                             LogUtil.e("finish");
                                         }

                                         @Override
                                         public void onWaiting() {

                                         }

                                         @Override
                                         public void onStarted() {
                                            LogUtil.e("下载连接",data.get(position));
                                         }

                                         @Override
                                         public void onDownloading(long total, long current) {

                                         }
                                     });
                                     dia.dismiss();
                                }
                            });
                            dia.setCancelClickListener("取消", new CommonDialog.CancelClickListener() {
                                @Override
                                public void clickCancel() {
                                    dia.dismiss();
                                }
                            });
                            dia.show();
                            return true;
                        }
                    });
                    showLoading(true,"加载中");
                   /* PicassoUtil.getPicassoObject().with(GalleryScanActivity.this).
                            load(data.get(position))
                            .resize(ScreenUtil.getScreenWidth(GalleryScanActivity.this),ScreenUtil.getScreenWidth(GalleryScanActivity.this)).
                            into(view, new Callback() {
                                @Override
                                public void onSuccess() {
                                     dismissLoading();
                                }

                                @Override
                                public void onError() {
                                    dismissLoading();
                                }
                            });*/
                    Picasso picasso=new Picasso.Builder(GalleryScanActivity.this).downloader(downloader).build();
                    picasso.load(data.get(position)).resize(ScreenUtil.getScreenWidth(GalleryScanActivity.this),ScreenUtil.getScreenWidth(GalleryScanActivity.this)).
                            into(view, new Callback() {
                                @Override
                                public void onSuccess() {
                                    dismissLoading();
                                }

                                @Override
                                public void onError() {
                                    dismissLoading();
                                }
                            });




                    container.addView(view);
                    return view;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);

                }
            };
        }

    }

}
