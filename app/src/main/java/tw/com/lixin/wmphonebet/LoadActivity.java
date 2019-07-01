package tw.com.lixin.wmphonebet;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.interfaces.LobbyBridge;
import tw.com.lixin.wmphonebet.jsonData.Client35;
import tw.com.lixin.wmphonebet.jsonData.LoginData;
import tw.com.lixin.wmphonebet.jsonData.LoginResData;
import tw.com.lixin.wmphonebet.websocketSource.BacSource;
import tw.com.lixin.wmphonebet.websocketSource.LobbySource;

public class LoadActivity extends RootActivity implements LobbyBridge {

    private LobbySource source;

    private ImageView loadImg;
    private Map<String, Integer> loadings = new HashMap<>();

    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);


        source = LobbySource.getInstance();
       // source.bind(this);
        loadings.put("loading1", R.drawable.loading1);
        loadings.put("loading2", R.drawable.loading2);
        loadings.put("loading3", R.drawable.loading3);
        loadings.put("loading4", R.drawable.loading4);
        loadings.put("loading5", R.drawable.loading5);
        loadings.put("loading6", R.drawable.loading6);

        loadings.put("loading7", R.drawable.loading7);

        loadings.put("loading8", R.drawable.loading8);
        loadings.put("loading9", R.drawable.loading9);
        loadings.put("loading10", R.drawable.loading10);
        loadings.put("loading11", R.drawable.loading11);
        loadings.put("loading12", R.drawable.loading12);
        loadings.put("loading13", R.drawable.loading13);
        pass = getPassedStr();

    }

    private void recurLoad(int loadI){
        loadImg.setImageResource(loadings.get("loading" + loadI));
        loadI++;
        if(loadI > 13) loadI = 1;
        int finalLoadI = loadI;
        delay(80, ()-> recurLoad(finalLoadI));
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadImg = findViewById(R.id.load_img);
        recurLoad(1);

        source.login(User.account(), pass, log ->{
            if(log.bOk){
                User.account(log.account);
                User.gameID(log.gameID);
                User.userName(log.userName);
                User.memberID(log.memberID);
                User.sid(log.sid);


                Log.e("yes", "yess");

                source.send(Json.to(new Client35()));
            }else {

                alert("Cannot login");
                finish();
            }
        }, failStr->{
            alert(failStr);
        });

    }

    @Override
    public void wholeDataUpdated() {
        BacSource bacSource  = BacSource.getInstance();
        bacSource.login(User.userName(), pass,log->{
            if(log.bOk){
                toActivity(LobbyActivity.class);
            }else {
                alert("Cannot login");
                finish();
            }
        }, fail->{

        });
    }

    @Override
    public void balanceUpdated() {

    }

    @Override
    public void peopleOnlineUpdate(int number) {

    }
}
