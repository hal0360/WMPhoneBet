package tw.com.lixin.wmphonebet.websocketSource;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tw.com.atromoby.utils.Cmd;
import tw.com.atromoby.utils.Json;
import tw.com.lixin.wmphonebet.interfaces.LobbyBridge;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.jsonData.LobbyData;
import tw.com.lixin.wmphonebet.jsonData.data.Game;
import tw.com.lixin.wmphonebet.jsonData.data.TableStage;
import tw.com.lixin.wmphonebet.models.Table;

public class LobbySource extends CasinoSource{

    private static LobbySource single_instance = null;


    public static LobbySource getInstance()
    {
        if (single_instance == null) single_instance = new LobbySource();
        return single_instance;
    }

    private LobbySource() {
        tables = new ArrayList<>();
        defineURL("ws://gameserver.a45.me:15109");
    }

    private LobbyBridge bridge;
    public List<Table> tables;
    public int pplOnline;

    public void bind(LobbyBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
    }

    public Table findTable(int id){
        for(Table tt: tables){
            if(tt.groupID == id){
                return tt;
            }
        }
        return null;
    }

    public void handle(Cmd cmd){
        super.handle(() ->{
            if(bridge != null) cmd.exec();
        });
    }

    @Override
    public void onReceive(String text) {
        LobbyData lobbyData = Json.from(text, LobbyData.class);
        switch(lobbyData.protocol) {
            case 35:
                Game bacGame = null;
                for(Game game: lobbyData.data.gameArr){
                    if (game.gameID == 101)
                        bacGame = game;
                }
                Log.e("bacGame", Json.to(bacGame));
                if(bacGame == null) return;
                for(TableStage tableStage: bacGame.groupArr){
                    if ( tableStage.gameStage != 4){
                        Table table = new Table();
                        table.setUp(tableStage.historyArr);
                        table.stage = tableStage.gameStage;
                        table.groupID = tableStage.groupID;
                        table.groupType = tableStage.groupType;
                        table.score = tableStage.bankerScore;
                        table.round = tableStage.gameNoRound;
                        table.number = tableStage.gameNo;
                        tables.add(table);
                    }
                }
                handle(() -> bridge.wholeDataUpdated());
                break;
            case 30:
                User.balance(lobbyData.data.balance);
                handle(() -> bridge.balanceUpdated());
                break;
            case 34:

                if(lobbyData.data.gameID == 101){
                    pplOnline = lobbyData.data.onlinePeople;
                    handle(() -> bridge.peopleOnlineUpdate(lobbyData.data.onlinePeople));
                }

                break;
            default:
        }

    }
}
