package fun.qianxiao.updatecheck.checkupdate.model;

import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fun.qianxiao.updatecheck.checkupdate.UpdateResult;
import fun.qianxiao.updatecheck.config.BaseConfig;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckUpdateModel implements ICheckUpdateModel {
    Observable<UpdateResult> sender;
    Observer<UpdateResult> receiver;

    @Override
    public void CheckUpdate(final CheckUpdateCallBack checkUpdateCallBack) {
        sender = Observable.create(new Observable.OnSubscribe<UpdateResult>() {
            @Override
            public void call(Subscriber<? super UpdateResult> subscriber) {
                UpdateResult updateResult = new UpdateResult();
                OkHttpClient okHttpClient = new OkHttpClient
                        .Builder()
                        .build();
                final String url = BaseConfig.checkupdateurl;
                if(TextUtils.isEmpty(url)){
                    subscriber.onError(new Throwable("检查更新远程地址为空"));
                    return;
                }
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                final Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String responsestr = response.body().string();
                    final JSONObject jsonObject = new JSONObject(responsestr);
                    int newversioncode = jsonObject.getInt("newversioncode");
                    updateResult.setNewApkVersionCode(newversioncode);
                    if (newversioncode > AppUtils.getAppVersionCode()) {
                        updateResult.setHasUpdate(true);
                        String newversionname = jsonObject.getString("newversionname");
                        String updatacontent = jsonObject.getString("updatacontent");
                        boolean isforceupdate = jsonObject.getInt("isforceupdate") == 1;
                        String newapksize = jsonObject.getString("newapksize");
                        String downloadurl = jsonObject.getString("downloadurl");
                        String newapkmd5 = jsonObject.getString("newapkmd5");
                        updateResult.setNewApkVersionName(newversionname);
                        updateResult.setUpdateContent(updatacontent);
                        updateResult.setForceUpdate(isforceupdate);
                        updateResult.setNewApkSize(newapksize);
                        updateResult.setNewApkDownloadUrl(downloadurl);
                        updateResult.setNewApkMd5(newapkmd5);
                    } else {
                        updateResult.setHasUpdate(false);
                    }
                    subscriber.onNext(updateResult);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        receiver = new Observer<UpdateResult>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                checkUpdateCallBack.OnError(e.toString());
            }

            @Override
            public void onNext(UpdateResult updateResult) {
                checkUpdateCallBack.OnFinish(updateResult);
            }
        };
        sender.subscribe(receiver);
    }
}
