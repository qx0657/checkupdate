package fun.qianxiao.updatecheck.checkupdate;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.lang.reflect.Field;

import fun.qianxiao.updatecheck.checkupdate.model.IDownloadModel;
import fun.qianxiao.updatecheck.checkupdate.presenter.CheckUpdatePresenter;
import fun.qianxiao.updatecheck.checkupdate.presenter.ICheckUpdatePresenter;
import fun.qianxiao.updatecheck.checkupdate.view.ICheckUpdateView;
import fun.qianxiao.updatecheck.config.BaseConfig;

public class CheckUpdateManager implements ICheckUpdateView {
    private Context context;
    private ICheckUpdatePresenter iCheckUpdatePresenter;
    private AlertDialog dialog;

    public interface CheckUpdateCallBack {
        void CheckUpdateFinish();
    }

    public CheckUpdateManager(Context context) {
        this.context = context;
        iCheckUpdatePresenter = new CheckUpdatePresenter(context,this);
    }

    public void CheckUpdate(boolean isSlience, CheckUpdateCallBack checkUpdateCallBack) {
        iCheckUpdatePresenter.CheckUpdate(isSlience, checkUpdateCallBack);
    }

    public void CheckUpdate(boolean isSlience) {
        iCheckUpdatePresenter.CheckUpdate(isSlience, null);
    }

    boolean isupdating = false;
    boolean isApkFullyDownloaded = false;
    @Override
    public void ShowUpdateDialog(final UpdateResult updateResult) {
        final String apksavefilepath = BaseConfig.appdownloadfiletemporarydir + AppUtils.getAppName() + " V." + updateResult.getNewApkVersionName() + ".apk";

        if(FileUtils.isFileExists(apksavefilepath) && FileUtils.getFileMD5ToString(apksavefilepath).equals(updateResult.getNewApkMd5().toUpperCase())){
            isApkFullyDownloaded = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder = builder.setTitle("发现新版本(V." + updateResult.getNewApkVersionName() + ")")
                .setMessage(updateResult.getUpdateContent())
                .setCancelable(false)
                //点击事件在下面设置 防止点击后dialog消失
                .setPositiveButton(isApkFullyDownloaded?"立即安装":"立即更新", null);
        if (!updateResult.isForceUpdate()) {
            builder = builder.setNegativeButton("暂不更新", null);
        }
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击立即更新/立即安装
                if(isApkFullyDownloaded){
                    //立即安装
                    if (Build.VERSION.SDK_INT >= 26 && context.getPackageManager().canRequestPackageInstalls()) {
                        //AppUtils.installApp(apksavefilepath);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
                        Uri uri = FileProvider.getUriForFile(Utils.getApp(), authority, new File(apksavefilepath));
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }else{
                        ToastUtils.showShort("请开启未知应用安装权限");
                        Intent intent = new Intent();
                        Uri packageURI = Uri.parse("package:"+context.getPackageName());
                        intent.setData(packageURI);
                        if (Build.VERSION.SDK_INT >= 26) {
                            intent.setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        }else {
                            intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
                        }
                        context.startActivity(intent);
                    }
                    return;
                }
                if(!isupdating){
                    isupdating = true;
                }else{
                    return;
                }
                hideNegativeButton(true);
                iCheckUpdatePresenter.DownloadNewApk(updateResult.getNewApkDownloadUrl(),apksavefilepath, new IDownloadModel.DownloadListener() {

                    @Override
                    public void OnDownLoadFileStart() {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDownloadProgress(0);
                            }
                        });
                    }

                    @Override
                    public void OnDownLoadFileProgress(final int percent) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDownloadProgress(percent);
                            }
                        });
                    }

                    @Override
                    public void OnDownLoadFileSuccess(String filename) {
                        if (FileUtils.getFileMD5ToString(apksavefilepath).equals(updateResult.getNewApkMd5().toUpperCase())) {
                            ToastUtils.showShort("下载并校验成功");
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //进行安装
                                    AppUtils.installApp(apksavefilepath);
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            ToastUtils.showShort("文件校验失败");
                            FileUtils.delete(apksavefilepath);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideNegativeButton(false);
                                    isupdating = false;
                                    setDownloadProgress("立即更新");
                                }
                            });
                        }

                    }

                    @Override
                    public void OnDownLoadFileError(String e) {
                        ToastUtils.showShort(e);
                        if(e.contains("无存储权限")){
                            PermissionUtils.permission(PermissionConstants.STORAGE).request();
                        }
                        FileUtils.delete(apksavefilepath);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideNegativeButton(false);
                                isupdating = false;
                                setDownloadProgress("立即更新");
                            }
                        });
                    }
                });
            }
        });
    }

    Button mMessageView = null;

    /**
     * 使用反射更改dialog积极按钮文字
     *
     * @param percent
     */
    private void setDownloadProgress(int percent) {
        setDownloadProgress(percent + "%");
    }

    private void setDownloadProgress(String text) {
        if (mMessageView == null) {
            try {
                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = mAlert.get(dialog);
                //通过反射修改message字体大小和颜色
                Field mMessage = mAlertController.getClass().getDeclaredField("mButtonPositive");
                mMessage.setAccessible(true);
                Button mMessageView = (Button) mMessage.get(mAlertController);
                mMessageView.setText(text);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            mMessageView.setText(text);
        }
    }

    /**
     * 隐藏取消更新按钮
     */
    private void hideNegativeButton(boolean flag){
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            //通过反射修改message字体大小和颜色
            Field mMessage = mAlertController.getClass().getDeclaredField("mButtonNegative");
            mMessage.setAccessible(true);
            Button mMessageView = (Button) mMessage.get(mAlertController);
            if(mMessageView!=null){
                mMessageView.setVisibility(flag? View.GONE: View.VISIBLE);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
