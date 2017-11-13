package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.QRCodeUtil;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by liuha on 2017/5/8.
 */

public class QrDialog extends Dialog {
    private CircleImageView userIcon;
    private TextView userName;
    private ImageView userSex;
    private TextView userPhone;
    private ImageView qrImg;
    private UserBean ub;
    private Context context;
    public QrDialog(Context context) {
        super(context);
        this.context=context;
    }

    public QrDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }
    //初始化
    private void init() {
        initView();
    }
    //初始化视图
    private void initView() {
        setContentView(R.layout.dialog_qr_layout);
        userIcon= (CircleImageView) findViewById(R.id.qr_userIcon);
        userName= (TextView) findViewById(R.id.qr_userName);
        userSex= (ImageView) findViewById(R.id.qr_userSex);
        userPhone= (TextView) findViewById(R.id.qr_userLocation);
        qrImg= (ImageView) findViewById(R.id.qr_Img);

        /**
         * 设置数据
         */

        if(ub!=null){
            PicassoUtil.getPicassoObject().load(ub.getUrl()).
                    resize(DpUtils.dpToPx(context,60),DpUtils.dpToPx(context,60)).into(userIcon);
            userName.setText(ub.getUsername());
            //默认是男性
            if(0==ub.getSex() || 1==ub.getSex()){
                 PicassoUtil.getPicassoObject().load(R.mipmap.icon_sex_male_blue).into(userSex);
            }else {
                PicassoUtil.getPicassoObject().load(R.mipmap.icon_sex_femal_red).into(userSex);
            }

            userPhone.setText(StringUtil.isBlank(ub.getPhone())?"手机号:对方暂未设置":ub.getPhone());

            String QrCode="";

            if(ub.getIsShareMaker()==1){
                QrCode="userId="+ub.getId();
            }else {
                if(!StringUtil.isBlank(ub.getInvitationCode())){
                    QrCode="userId="+ub.getId()+"&iid="+ub.getInvitationCode();
                }else {
                    QrCode="userId="+ub.getId();
                }
            }


            Bitmap bmp= QRCodeUtil.createQRCode(QrCode,DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80),null);
            if(bmp!=null){
                qrImg.setImageBitmap(bmp);
            }

        }

    }

    //设置头像的方法
    public void setUserIcon(Bitmap mBitmap){
        userIcon.setImageBitmap(mBitmap);
    }
    //设置用户名
    public void setUserName(String name){
        userName.setText(name);
    }
    //设置性别图标
    public void setUserSex(Bitmap mBitmap){
        userSex.setImageBitmap(mBitmap);
    }


    //设置二维码
    public void setUserQr(Bitmap mBitmap){
        qrImg.setImageBitmap(mBitmap);
    }

    public void setUser(UserBean ub){
        this.ub=ub;
    }

}
