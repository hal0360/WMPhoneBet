package tw.com.lixin.wmphonebet.Tools;

import tw.com.atromoby.utils.Json;
import tw.com.lixin.wmphonebet.App;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.jsonData.LobbyData;
import tw.com.lixin.wmphonebet.jsonData.data.Game;
import tw.com.lixin.wmphonebet.jsonData.data.TableStage;
import tw.com.lixin.wmphonebet.models.Table;

public class LobbySocket extends CasinoSocket {

       private LobbyBridge bridge;

    public LobbySocket(){
        webUrl = Url.Lobby;
    }

    public void bind(LobbyBridge bridge){
        this.bridge = bridge;
    }

    public void unbind(){
        this.bridge = null;
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
                        App.tables.add(table);
                    }
                }
                if(bridge != null) handler.post(() -> bridge.wholeDataUpdated());
                break;
            case 30:
                User.balance(lobbyData.data.balance);
                if(bridge != null) handler.post(() -> bridge.balanceUpdated());
                break;
            case 34:
                if(bridge != null) handler.post(() -> bridge.peopleOnlineUpdate(lobbyData.data.onlinePeople));
                break;
            default:

        }
    }

}