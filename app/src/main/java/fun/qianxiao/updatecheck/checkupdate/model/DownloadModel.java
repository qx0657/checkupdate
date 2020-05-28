package fun.qianxiao.updatecheck.checkupdate.model;

import android.Manifest;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadModel implements IDownloadModel {
    @Override
    public void DownLoadFile(String fileurl, final String fallpath, final DownloadListener downloadListener) {
        //检查存储权限
        if(!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            downloadListener.OnDownLoadFileError("无存储权限");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .build();
        Request request = new Request.Builder()
                .url(fileurl)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    LogUtils.i("total", total);
                    downloadListener.OnDownLoadFileStart();
                    if (is != null) {
                        FileUtils.createOrExistsFile(fallpath);
                        File file = new File(fallpath);// 设置路径
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            downloadListener.OnDownLoadFileProgress((int) ((((double) process) / ((double) total)) * 100));
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    downloadListener.OnDownLoadFileSuccess(fallpath);
                } catch (Exception e) {
                    downloadListener.OnDownLoadFileError("下载出错(" + e.toString() + ")");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                downloadListener.OnDownLoadFileError("onFailure(" + e.toString() + ")");
            }
        });
    }
}
