package fun.qianxiao.updatecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fun.qianxiao.updatecheck.checkupdate.CheckUpdateManager;


public class MainActivity extends AppCompatActivity {
    private Button bt_checkupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_checkupdate = f(R.id.bt_checkupdate);
        //进入软件自动检测更新
        new CheckUpdateManager(this).CheckUpdate(true);

        bt_checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击更新
                new CheckUpdateManager(MainActivity.this).CheckUpdate(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <E> E f(int id){
        return (E)findViewById(id);
    }
}
