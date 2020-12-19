package com.cq.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cq.musicplayer.myTool.PhoneUserBean;
import com.cq.musicplayer.myTool.QQUserBean;
import com.google.gson.Gson;
import com.mob.MobSDK;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1111250139";//官方获取的APPID——QQ开发放平台
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private CheckBox cb_xy;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //检查是否勾选用户隐私协议
        cb_xy = findViewById(R.id.cb_xy);
        checked = cb_xy.isChecked();
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, LoginActivity.this);

    }

    /**
     * QQ授权登录按钮事件监听
     * @param v
     */
    public void buttonQQLogin(View v) {
        if(checked){
            //勾选了
            MobSDK.submitPolicyGrantResult(checked, null);

            /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
             官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
             第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
            mIUiListener = new BaseUiListener();
            //all表示获取所有权限
            mTencent.login(LoginActivity.this, "all", mIUiListener);
        }else{
            //未勾选
            Toast.makeText(this, "请勾选用户协议后,再使用一键登录!", Toast.LENGTH_SHORT).show();
        }


    }



    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                final QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        //是一个json串response.tostring，直接使用gson解析就好
                        Log.e(TAG, "登录成功" + response.toString());
                        //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                        // Nickname  昵称
                        //Figureurl_qq_1 //头像
                        QQUserBean qqUserBean = new Gson().fromJson(response.toString(),QQUserBean.class);
                        //传递qq授权成功的对象到下一个Activity中

                        Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UserBean", qqUserBean);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }

                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onWarning(int i) {

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 手机号码登录按钮事件监听
     * @param view
     */
    public void buttonPhoneLogin(View view) {
        if(checked){
            //勾选了
            MobSDK.submitPolicyGrantResult(checked, null);

            //调用ui界面
            sendCode(getApplicationContext());
        }else{
            //未勾选
            Toast.makeText(this, "请勾选用户协议后,再使用一键登录!", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 立即体验按钮事件监听(游客登录)
     * @param view
     */
    public void buttonNoLogin(View view) {
        if(checked){
            //勾选了
            MobSDK.submitPolicyGrantResult(checked, null);

            //直接进入首页

            Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
            startActivity(intent);
            finish();

        }else{
            //未勾选
            Toast.makeText(this, "请勾选用户协议后,再使用一键登录!", Toast.LENGTH_SHORT).show();
        }

    }




    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    // 国家代码，如“86”
                    String country = (String) phoneMap.get("country");
                    // 手机号码，如“13800138000”
                    String phone = (String) phoneMap.get("phone");
                    // TODO 利用国家代码和手机号码进行后续的操作
                    PhoneUserBean phoneUserBean = new PhoneUserBean(phone,country);
                    Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UserBean",phoneUserBean);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                    finish();
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    /**
     * 返回键(按下返回键相当于执行了Home键)
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //调用HOME键
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意，这个地方最重要，关于解释，自己google吧
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }


}