package fun.qianxiao.updatecheck.checkupdate.presenter;


import fun.qianxiao.updatecheck.checkupdate.CheckUpdateManager;
import fun.qianxiao.updatecheck.checkupdate.model.IDownloadModel;

public interface ICheckUpdatePresenter {
    /**
     * 检查更新
     */
    void CheckUpdate(boolean isSilence, CheckUpdateManager.CheckUpdateCallBack checkUpdateCallBack);

    /**
     * 下载新apk
     */
    void DownloadNewApk(String url, String apkfilesavepath, IDownloadModel.DownloadListener downloadListener);
}
