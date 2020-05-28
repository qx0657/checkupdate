package fun.qianxiao.updatecheck.checkupdate.model;

public interface IDownloadModel {
    void DownLoadFile(String fileurl, String fallpath, IDownloadModel.DownloadListener downloadListener);

    interface DownloadListener {
        void OnDownLoadFileStart();

        void OnDownLoadFileProgress(int percent);

        void OnDownLoadFileSuccess(String filename);

        void OnDownLoadFileError(String e);
    }
}
