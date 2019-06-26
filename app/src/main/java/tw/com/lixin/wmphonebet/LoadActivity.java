package tw.com.lixin.wmphonebet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.interfaces.LobbyBridge;

public class LoadActivity extends RootActivity implements LobbyBridge {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

    }

    @Override
    public void wholeDataUpdated() {

    }

    @Override
    public void balanceUpdated() {

    }

    @Override
    public void peopleOnlineUpdate(int number) {

    }
}
