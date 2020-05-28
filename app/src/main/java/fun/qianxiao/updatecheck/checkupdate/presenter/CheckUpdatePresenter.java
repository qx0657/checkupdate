package fun.qianxiao.updatecheck.checkupdate.presenter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;

import fun.qianxiao.updatecheck.checkupdate.CheckUpdateManager;
import fun.qianxiao.updatecheck.checkupdate.UpdateResult;
import fun.qianxiao.updatecheck.checkupdate.model.CheckUpdateModel;
import fun.qianxiao.updatecheck.checkupdate.model.DownloadModel;
import fun.qianxiao.updatecheck.checkupdate.model.ICheckUpdateModel;
import fun.qianxiao.updatecheck.checkupdate.model.IDownloadModel;
import fun.qianxiao.updatecheck.checkupdate.view.ICheckUpdateView;
import fun.qianxiao.updatecheck.config.BaseConfig;
import fun.qianxiao.updatecheck.tool.Save;

public class CheckUpdatePresenter implements ICheckUpdatePresenter, ICheckUpdateModel.CheckUpdateCallBack {
    private Context context;
    private ICheckUpdateModel iCheckUpdateModel;
    private IDownloadModel iDownloadModel;
    private ICheckUpdateView iCheckUpdateView;
    private boolean isSilence = false;
    private CheckUpdateManager.CheckUpdateCallBack checkUpdateCallBack;

    public CheckUpdatePresenter(Context context, ICheckUpdateView iCheckUpdateView) {
        this.context = context;
        this.iCheckUpdateView = iCheckUpdateView;
        this.iCheckUpdateModel = new CheckUpdateModel();
        this.iDownloadModel = new DownloadModel();
    }

    @Override
    public void CheckUpdate(boolean isSilence, CheckUpdateManager.CheckUpdateCallBack checkUpdateCallBack) {
        this.isSilence = isSilence;
        this.checkUpdateCallBack = checkUpdateCallBack;
        iCheckUpdateModel.CheckUpdate(this);
    }

    @Override
    public void DownloadNewApk(String url,String apkfilesavepath,  IDownloadModel.DownloadListener downloadListener) {
        iDownloadModel.DownLoadFile(url,apkfilesavepath,downloadListener);
    }

    @Override
    public void OnFinish(UpdateResult updateResult) {
        if (checkUpdateCallBack != null) {
            checkUpdateCallBack.CheckUpdateFinish();
        }
        if (updateResult.isHasUpdate()) {
            iCheckUpdateView.ShowUpdateDialog(updateResult);
        } else {
            if (!isSilence) {
                ToastUtils.showShort("当前已是最新版本");
            }
        }
    }

    @Override
    public void OnError(String e) {
        if (checkUpdateCallBack != null) {
            checkUpdateCallBack.CheckUpdateFinish();
        }
        if (!isSilence) {
            ToastUtils.showShort(e);
        }
    }
}
