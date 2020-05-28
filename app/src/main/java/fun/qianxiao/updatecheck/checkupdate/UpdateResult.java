package fun.qianxiao.updatecheck.checkupdate;

public class UpdateResult {
    private boolean hasUpdate;
    private int newApkVersionCode;
    private String newApkVersionName;
    private String updateContent;
    private boolean isForceUpdate;
    private String newApkSize;
    private String newApkDownloadUrl;
    private String newApkMd5;

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public int getNewApkVersionCode() {
        return newApkVersionCode;
    }

    public void setNewApkVersionCode(int newApkVersionCode) {
        this.newApkVersionCode = newApkVersionCode;
    }

    public String getNewApkVersionName() {
        return newApkVersionName;
    }

    public void setNewApkVersionName(String newApkVersionName) {
        this.newApkVersionName = newApkVersionName;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
    }

    public String getNewApkSize() {
        return newApkSize;
    }

    public void setNewApkSize(String newApkSize) {
        this.newApkSize = newApkSize;
    }

    public String getNewApkDownloadUrl() {
        return newApkDownloadUrl;
    }

    public void setNewApkDownloadUrl(String newApkDownloadUrl) {
        this.newApkDownloadUrl = newApkDownloadUrl;
    }

    public String getNewApkMd5() {
        return newApkMd5;
    }

    public void setNewApkMd5(String newApkMd5) {
        this.newApkMd5 = newApkMd5;
    }
}
