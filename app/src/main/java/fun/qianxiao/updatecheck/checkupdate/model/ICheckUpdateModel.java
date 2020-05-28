package fun.qianxiao.updatecheck.checkupdate.model;

import fun.qianxiao.updatecheck.checkupdate.UpdateResult;

public interface ICheckUpdateModel {
    void CheckUpdate(CheckUpdateCallBack checkUpdateCallBack);

    interface CheckUpdateCallBack {
        void OnFinish(UpdateResult updateResult);

        void OnError(String e);
    }
}
