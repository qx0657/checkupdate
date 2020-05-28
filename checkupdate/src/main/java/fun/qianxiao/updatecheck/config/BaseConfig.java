package fun.qianxiao.updatecheck.config;

import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;


/**
 * Create by QianXiao
 * On 2020/5/28
 */
public class BaseConfig {
    public static String checkupdateurl = "http://qianxiao.fun/test/testupdatecheck.txt";
    public static String appdownloadfiletemporarydir = Environment.getExternalStorageDirectory() + File.separator + AppUtils.getAppName() + File.separator;
}
