package tw.com.lixin.wmphonebet;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.interfaces.LobbyBridge;
import tw.com.lixin.wmphonebet.jsonData.LoginData;
import tw.com.lixin.wmphonebet.jsonData.LoginResData;
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
        LoginData loginData = new LoginData( User.account(), pass);
        source.login(User.userName(), pass, log ->{
            if(logRespend.data.bOk){
                User.account(logRespend.data.account);
                User.gameID(logRespend.data.gameID);
                User.userName(logRespend.data.userName);
                User.memberID(logRespend.data.memberID);
                User.sid(logRespend.data.sid);
                App.socket.send(Json.to(new Client35()));
            }else {
                alert("Cannot login");
                finish();
            }
        });


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

        delay(1000, ()->{
            App.socket.start(Url.Lobby);
        });

    }

    private void setTables(){

        for(TableStage tableStage: bacGame.groupArr){
            if ( tableStage.gameStage != 4){
                // CasinoRoad casinoRoad = new CasinoRoad(tableStage.historyArr);

                Table table = new Table();
                table.setUp(tableStage.historyArr);
                table.stage = tableStage.gameStage;
                table.groupID = tableStage.groupID;

                table.groupType = tableStage.groupType;

                table.score = tableStage.bankerScore;
                table.round = tableStage.gameNoRound;
                table.number = tableStage.gameNo;

                // table.mainRoad = road;
                        /*
                table.casinoRoad = casinoRoad;
                table.secRoad = new SecRoad(table.casinoRoad.sortedRoad);
                table.secRoadPreB = new SecRoad(table.casinoRoad.sortedRoadB);
                table.secRoadPreP = new SecRoad(table.casinoRoad.sortedRoadP);
                table.thirdRoad = new ThirdRoad(table.casinoRoad.sortedRoad);
                table.thirdRoadPreB = new ThirdRoad(table.casinoRoad.sortedRoadB);
                table.thirdRoadPreP = new ThirdRoad(table.casinoRoad.sortedRoadP);
                table.fourthRoad = new FourthRoad(table.casinoRoad.sortedRoad);
                table.fourthRoadPreB = new FourthRoad(table.casinoRoad.sortedRoadB);
                table.fourthRoadPreP = new FourthRoad(table.casinoRoad.sortedRoadP);
                */
                App.tables.add(table);
            }
        }

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
