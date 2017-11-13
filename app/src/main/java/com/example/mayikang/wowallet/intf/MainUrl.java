package com.example.mayikang.wowallet.intf;

/**
 * Created by mayikang on 17/5/23.
 */

public class MainUrl {

    //192.168.1.49:8080
    //www.woqianbao8.com
    //www.woqianbao8.com
    //192.168.1.138s:8080/wow
    //www.woqianbao8.com


    //user 下通用page查询
    public static String basePageQueryUrl = "http://www.woqianbao8.com/pageSearch$ajax.htm";

    //通用单条查询
    public static String baseSingleQueryUrl = "http://www.woqianbao8.com/singleSearch$ajax.htm";

    //显示登录
    public static String loginUrl = "http://www.woqianbao8.com/admin/login.htm";
    //username
    //password

    //退出登录
    public static String logoutUrl = "http://www.woqianbao8.com/user/logOutToApp$ajax.htm";

    //查询用户余额
    public static String queryUserBalanceUrl = "http://www.woqianbao8.com/user/getUserAmount$ajax.htm";

    //判断用户是否收藏了该店铺
    public static String queryIsStoreCollectedUrl = "http://www.woqianbao8.com/user/isFavorite$ajax.htm";
    //storeId

    //用户添加或取消收藏店铺
    public static String addOrDeleteCollectedStoreUrl = "http://www.woqianbao8.com/user/Favorite$ajax.htm";
    //userId
    //storeId

    //查询收藏的店铺
    //通用 pagesearch
    //ctype=favorite&jf=store&cond={user:{id:1}}

    //上传图片

    public static final String uploadFileUrl = "http://www.woqianbao8.com/upload/uploadImgByAccessory.htm";


    //查询账单列表
    public static String queryBillUrl = "http://www.woqianbao8.com/user/obtionOrderformList$ajax.htm";
    //time

    //查询用户订单
    public static final String indentUrl = "http://www.woqianbao8.com/user/getOrderList$ajax.htm";

    //注册获取图片验证码
    public static final String getPicCode = "http://www.woqianbao8.com/verify_output$ajax.htm";

    //修改用户信息
    public static final String amendUserUrl = "http://www.woqianbao8.com/user/update_userInfo$ajax.htm";

    //用户余额转账
    public static final String balanceTransferUrl = "http://www.woqianbao8.com/user/remittanceTofriend$ajax.htm";

    //判断用户是否已经设置了支付密码
    public static final String isHasPayPasswordUrl = "http://www.woqianbao8.com/user/isHavePayPassword$ajax.htm";

    //用户设置支付密码
    public static final String setPayPasswordUrl = "http://www.woqianbao8.com/user/setPayPasswoed$ajax.htm";

    //修改支付密码
    public static final String updatePayPasswordUrl = "http://www.woqianbao8.com/user/updateInPayPassword$ajax.htm";

    //解除好友关系
    public static final String relieveFriendShipUrl = "http://www.woqianbao8.com/user/relieveFriendShip$ajax.htm";
    //friendId

    //查询用户粉丝
    public static final String queryUserFansUrl = "http://www.woqianbao8.com/user/singleSearch$ajax.htm";

    //修改登录密码
    public static final String updateLoginPasswordUrl = "http://www.woqianbao8.com/user/updatepassword$ajax.htm";


    //上传图片
    public static final String UpImgUrl = "http://www.woqianbao8.com/upload/uploadImgByAccessory.htm";

    //修改用户名
    public static final String amendUserName = "http://www.woqianbao8.com/user/modifyUsername$ajax.htm";

    //获取图片验证码
    public static final String PictureCodeUrl = "http://www.woqianbao8.com/verify_output$ajax.htm";

    //验证原手机号码
    public static final String VerifyOldTelUrl = "http://www.woqianbao8.com/user/validation_code$ajax.htm";

    //向原手机发送验证码
    public static final String SendOldPhoneUrl = "http://www.woqianbao8.com/verify_sms$ajax.htm";

    //验证新手机号
    public static final String VerifyNewTelUrl = "http://www.woqianbao8.com/user/newMoblel$ajax.htm";

    //向新手机发送验证码
    public static final String SendSmsNewTelUrl = "http://www.woqianbao8.com/verify_sms$ajax.htm";

    //绑定手机
    public static final String BindNewTelUrl = "http://www.woqianbao8.com/user/updatePhone$ajax.htm";

    //添加好友
    public static final String AddFriendsUrl = "http://www.woqianbao8.com/user/addFriend$ajax.htm";
    //查询用户信息
    public static final String GetUserMessage = "http://www.woqianbao8.com/singleSearch$ajax.htm";

    //修改
    public static final String SetMessageStatus = "http://www.woqianbao8.com/user/dataModify$ajax.htm";


    //判断电话号码是否被占用
    public static final String isOccupy = "http://www.woqianbao8.com/phoneIsOccupy$ajax.htm";

    //验证图片验证码，获取短信验证码
    public static final String getPhoenCode = "http://www.woqianbao8.com/verify_sms$ajax.htm";

    //判断短信验证码是否正确
    public static final String picCodeiSture = "http://www.woqianbao8.com/judgePictureCode$ajax.htm";

    //注册用户
    public static final String regist = "http://www.woqianbao8.com/addNewUser$ajax.htm";

    //查询好友转账记录
    public static final String FriendsMoneyUrl = "http://www.woqianbao8.com/user/obtionTransferFlow$ajax.htm";

    //忘记支付密码修改
    public static final String FrogetPayUrl = "http://www.woqianbao8.com/forgotPayPwd$ajax.htm";

    //忘记密码
    public static final String forgetpsw = "http://www.woqianbao8.com/forgotpassword$ajax.htm";


    //同意添加好友
    public static final String AggreAddFriends = "http://www.woqianbao8.com/user/isAgree$ajax.htm";
    //不同意添加好友
    public static final String DisAggreAddFriends = "http://www.woqianbao8.com/user/isAgree$ajax.htm";

    //查询用户朋友
    public static final String queryUserFriendUrl = "http://www.woqianbao8.com/user/obtionDriends$ajax.htm";

    //添加店铺评论
    public static final String addStoreComment = "http://www.woqianbao8.com/user/addStoreComment$ajax.htm";

    //下单
    public static final String getOrder = "http://www.woqianbao8.com/user/saveOrder$ajax.htm";
    //请求Sign
    public static final String getPaySign = "http://www.woqianbao8.com/user/getPaySign$ajax.htm";

    //余额支付
    public static final String payByBalanceUrl = "http://www.woqianbao8.com/user/balancePayOrder$ajax.htm?";
    //ofId=1&userId=1

    //版本更新
    public static final String UpApp = "http://www.woqianbao8.com/last_version.htm";
    //下载app
    public static final String DownLoadApp = "http://www.woqianbao8.com/Download/ObtionApp.htm";
    //获取粉丝列表
    public static final String getFansList = "http://www.woqianbao8.com/user/queryFans$ajax.htm";
    //获取分红列表
    public static final String getdevident = "http://www.woqianbao8.com/store/rebate$ajax.htm";
    //提交套餐
    public static final String CommentAgentResult = "http://www.woqianbao8.com/user/saveApply$ajax.htm";
    //查询我的代理
    public static final String GetAgentList = "http://www.woqianbao8.com/agent_manager/query$ajax.htm";
    //查询我的评价
    public static final String GetMineCommentList = "http://www.woqianbao8.com/user/user_evaluation$ajax.htm";
    //查询收支明细
    public static final String Getincome = "http://www.woqianbao8.com/user/obtionUserTransFlow$ajax.htm";
    //查询代理套餐
    public static final String GetAgintlists = "http://www.woqianbao8.com/agent_manager/query$ajax.htm";
    //查询代理用户
    public static final String GetAgintUser = "http://www.woqianbao8.com/getAgentStoreList$ajax.htm";
    //添加代理店铺
    public static final String AddNewStore = "http://www.woqianbao8.com/manage/addAgentAppStore$ajax.htm";
    //获取微信签名
    public static final String WeiXinPay = "http://www.woqianbao8.com/preOrder$ajax.htm";
    //支付宝提现
    public static final String ConfirmAli = "http://www.woqianbao8.com/user/ali_withdraw_deposit$ajax.htm";

    //修改真实姓名0
    public static final String ChangRealName = "http://www.woqianbao8.com/user/modify_realName$ajax.htm"; //修改真实姓名
    //修改支付宝号
    public static final String ChangAlipayNumber = "http://www.woqianbao8.com/user/modify_alipayNumber$ajax.htm";
    //查询当前用户申请的套餐
    public static final String VerifyAgent = "http://www.woqianbao8.com/appQueryAgent$ajax.htm";
    //查询当前用户是否有支付密码
    public static final String HasPayPassWord = "http://www.woqianbao8.com/user/isPayPassword$ajax.htm";
    //加载用户协议
    public static final String Protocol = "http://www.woqianbao8.com/agreement/to_agreement.htm";
    //加载附近店铺
    public static final String PullNearStoreUrl = "http://www.woqianbao8.com/searchNearbyStore$ajax.htm";

    //高德逆地理编码
    public static final String gaode="http://restapi.amap.com/v3/geocode/geo";
    //搜索店铺或者商品
    public static final String GoosNameOrShopName="http://www.woqianbao8.com/searchStoreNameOrGoodsName$ajax.htm";

}
