# 欢迎使用浅笑检查更新模块
*原生dialog弹窗，支持显示进度，一键更新，一键安装，新手作品，不喜勿喷，欢迎交流，欢迎建议*

**依赖**

    implementation 'com.github.qx0657:checkupdate:v.1.1'

**另需依赖**

    //工具库
    implementation 'com.blankj:utilcode:1.28.6'
    //异步
    implementation 'io.reactivex:rxandroid:1.1.0'
    implementation 'io.reactivex:rxjava:1.1.5'
    //okhttp3
    implementation 'com.squareup.okhttp3:okhttp:4.7.2'
    implementation 'org.conscrypt:conscrypt-android:2.2.1'


**权限**

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />



## 使用
设置远程更新配置文件地址（utf-8文本文件）

    BaseConfig.checkupdateurl = "http://qianxiao.fun/test/testupdatecheck.txt";

远程配置文件格式（json字符串）

    {"newversioncode":6,"newversionname":"1.5","updatacontent":"更新内容：\n1.新增强大功能。\n2.修复无数Bug。","isforceupdate":0,"newapksize":"4.47M","downloadurl":"http://qianxiao.fun/sw/test/1.apk","newapkmd5"="736f68c982ce072e224acd4e4637987d"}


非静默检查更新

    new CheckUpdateManager(context).CheckUpdate(false);

静默检查更新

    new CheckUpdateManager(context).CheckUpdate(true);

有结束回调检查更新，第一个参数为是否静默

    new CheckUpdateManager(this).CheckUpdate(false,new CheckUpdateManager.CheckUpdateCallBack(){

            @Override
            public void CheckUpdateFinish() {
                // ...
            }
        });


## 效果
![效果图](https://t1.picb.cc/uploads/2020/05/28/tJGcPe.gif "效果图")
*https://t1.picb.cc/uploads/2020/05/28/tJGcPe.gif*
## 其他
浅笑博客：<http://blog.qianxiao.fun>

### End
