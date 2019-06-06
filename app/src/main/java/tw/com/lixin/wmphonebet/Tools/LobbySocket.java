package tw.com.lixin.wmphonebet.Tools;

import android.os.Handler;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.Cmd;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.jsonData.LobbyData;
import tw.com.lixin.wmphonebet.jsonData.data.Game;
import tw.com.lixin.wmphonebet.jsonData.data.TableStage;

public class LobbySocket extends CasinoSocket {


    private CmdStr cmdLog;

    public LobbySocket(){
        webUrl = Url.Lobby;
    }


    public void bind(){

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



                Server20 server20 = Json.from(text, Server20.class);
                //App.curTable.stage = server20.data.gameStage;
                if(server20.data.gameID == App.gameID && server20.data.groupID == App.groupID){
                    App.group.pro20(server20.data);
                }

                break;
            case 30:
                Server20 server20 = Json.from(text, Server20.class);
                //App.curTable.stage = server20.data.gameStage;
                if(server20.data.gameID == App.gameID && server20.data.groupID == App.groupID){
                    App.group.pro20(server20.data);
                }
                break;
            case 0:
                if(cmdLog != null)
                    handler.post(() -> cmdLog.exec(text));
                break;
            case 34:
                Server26 server26 = Json.from(text, Server26.class);
                if(server26.data.gameID == App.gameID){
                    Table ffTable = App.findTable(server26.data.groupID);
                    if(ffTable != null){
                        ffTable.setUp(server26.data.historyArr);
                        ffTable.groupType = server26.data.groupType;
                        ffTable.round = server26.data.historyArr.size();
                        ffTable.playCount = server26.data.historyData.playerCount;
                        ffTable.bankCount = server26.data.historyData.bankerCount;
                        ffTable.tieCount = server26.data.historyData.tieCount;
                        ffTable.playPairCount = server26.data.historyData.playerPairCount;
                        ffTable.bankPairCount = server26.data.historyData.bankerPairCount;

                    }else{
                        Log.e("ssds", "not catched");
                    }
                    if(server26.data.groupID == App.groupID){
                        App.group.pro26(server26.data);
                    }
                }
                break;
            default:

        }
    }

}