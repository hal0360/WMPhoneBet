package tw.com.lixin.wmphonebet;

import android.content.res.Configuration;
import android.os.Bundle;

import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.Tools.LoadDialog;


public abstract class WMActivity extends RootActivity {

    private LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        loadDialog = new LoadDialog(this);


    }

    public void loading(){
        loadDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        loadDialog.dismiss();
    }


    public void unloading(){
        loadDialog.dismiss();
    }


    public int orientation(){
        return getResources().getConfiguration().orientation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
