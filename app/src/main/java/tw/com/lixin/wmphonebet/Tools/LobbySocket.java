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
import tw.com.lixin.wmcasino.App;
import tw.com.lixin.wmcasino.global.User;
import tw.com.lixin.wmcasino.jsonData.Server10;
import tw.com.lixin.wmcasino.jsonData.Server20;
import tw.com.lixin.wmcasino.jsonData.Server22;
import tw.com.lixin.wmcasino.jsonData.Server24;
import tw.com.lixin.wmcasino.jsonData.Server25;
import tw.com.lixin.wmcasino.jsonData.Server26;
import tw.com.lixin.wmcasino.jsonData.Server30;
import tw.com.lixin.wmcasino.jsonData.Server31;
import tw.com.lixin.wmcasino.jsonData.Server34;
import tw.com.lixin.wmcasino.jsonData.Server35;
import tw.com.lixin.wmcasino.jsonData.Server38;
import tw.com.lixin.wmcasino.jsonData.data.Server23;
import tw.com.lixin.wmcasino.models.Table;

public class LobbySocket extends Ca {

    private WebSocket webSocket = null;
    public Cmd cmdOpen, cmdFail;
    private CmdStr cmdLog;
    private Handler handler;
    public boolean connected = false;


    private class ProtolNum{
        public int protocol;
    }

    public LobbySocket(){
        handler = new Handler();
    }

    public void start(String url){
        close();
        OkHttpClient client = new OkHttpClient();
        webSocket = client.newWebSocket(new Request.Builder().url(url).build(), this);
        client.dispatcher().executorService().shutdown();
    }

    public void onSuccess(Cmd cmd){
        cmdOpen = cmd;
    }

    public void onFail(Cmd cmd){
        cmdFail = cmd;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        connected = true;
        if(cmdOpen != null){
            handler.post(() -> cmdOpen.exec());
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        ProtolNum protolNum = Json.from(text, ProtolNum.class);

        Log.e("ssds", text);

        switch(protolNum.protocol) {
            case 20:
                Server20 server20 = Json.from(text, Server20.class);
                //App.curTable.stage = server20.data.gameStage;
                if(cmd20 != null && server20.data.gameID == App.gameID && server20.data.groupID == App.groupID)
                    handler.post(() -> cmd20.exec(server20.data));
                break;
            case 0:
                if(cmdLog != null)
                    handler.post(() -> cmdLog.exec(text));
                break;
            case 26:
                Server26 server26 = Json.from(text, Server26.class);
                if(server26.data.gameID == App.gameID){
                    Table ffTable = App.findTable(server26.data.groupID);
                    if(ffTable != null){
                        ffTable.setUp(server26.data.historyArr);
                        ffTable.groupType = server26.data.groupType;
                        ffTable.round = server26.data.historyArr.size();
                        ffTable.playCount =  server26.data.historyData.playerCount;
                        ffTable.bankCount = server26.data.historyData.bankerCount;
                        ffTable.tieCount = server26.data.historyData.tieCount;
                        ffTable.playPairCount = server26.data.historyData.playerPairCount;
                        ffTable.bankPairCount = server26.data.historyData.bankerPairCount;

                    }else{
                        Log.e("ssds", "not catched");
                    }
                    if(cmd26 != null && server26.data.groupID == App.groupID){
                        handler.post(() -> cmd26.exec(server26.data));
                    }
                }
                break;
            case 22:
                Server22 server22 = Json.from(text, Server22.class);
                if(cmd22 != null && server22.data.gameID == App.gameID && server22.data.groupID == App.groupID)
                    handler.post(() -> cmd22.exec(server22.data));
                break;
            case 25:
                Server25 server25 = Json.from(text, Server25.class);
                if(cmd25 != null && server25.data.gameID == App.gameID && server25.data.groupID == App.groupID)
                    handler.post(() -> cmd25.exec(server25.data));
                break;
            case 10:
                Server10 server10 = Json.from(text, Server10.class);
                if(cmd10 != null && server10.data.gameID == App.gameID && server10.data.groupID == App.groupID)
                    handler.post(() -> cmd10.exec(server10.data));
                break;
            case 24:
                Server24 server24 = Json.from(text, Server24.class);
                if(cmd24 != null && server24.data.gameID == App.gameID && server24.data.groupID == App.groupID)
                    handler.post(() -> cmd24.exec(server24.data));
                break;
            case 31:
                Server31 server31 = Json.from(text, Server31.class);
                if(cmd31 != null && server31.data.gameID == App.gameID && server31.data.groupID == App.groupID && User.memberID() == server31.data.memberID)
                    handler.post(() -> cmd31.exec(server31.data));
                break;
            case 34:
                Server34 server34 = Json.from(text, Server34.class);
                if(cmd34 != null && server34.data.gameID == App.gameID)
                    handler.post(() -> cmd34.exec(server34.data));
                break;
            case 35:
                Server35 server35 = Json.from(text, Server35.class);
                if(cmd35 != null)
                    handler.post(() -> cmd35.exec(server35.data));
                break;
            case 30:
                Server30 server30 = Json.from(text, Server30.class);
                User.balance(server30.data.balance);
                if(cmd30 != null)
                    handler.post(() -> cmd30.exec(server30.data));
                break;
            case 38:
                Server38 server38 = Json.from(text, Server38.class);
               // App.curTable.betSec = server38.data.timeMillisecond/1000;
                if(cmd38 != null && server38.data.gameID == App.gameID && server38.data.groupID == App.groupID)
                    handler.post(() -> cmd38.exec(server38.data));
                break;
            case 23:
                Server23 server23 = Json.from(text, Server23.class);
                if(server23.data.gameID == App.gameID && server23.data.memberID == User.memberID()){
                    User.balance(server23.data.balance);
                    if(cmd23 != null)
                        handler.post(() -> cmd23.exec(server23.data));
                }
                break;
            default:

        }

    }

    public void send(String message){
        webSocket.send(message);
    }

    public void onLogin(CmdStr cmd){
        cmdLog = cmd;
    }

    public void receive10(Server10.CmdData cmd){
        cmd10 = cmd;
    }

    public void receive26(Server26.CmdData cmd){
        cmd26 = cmd;
    }

    public void receive20(Server20.CmdData cmd){
        cmd20 = cmd;
    }

    public void receive22(Server22.CmdData cmd){
        cmd22 = cmd;
    }

    public void receive24(Server24.CmdData cmd){
        cmd24 = cmd;
    }

    public void receive25(Server25.CmdData cmd){
        cmd25 = cmd;
    }

    public void receive31(Server31.CmdData cmd){
        cmd31 = cmd;
    }

    public void receive34(Server34.CmdData cmd){
        cmd34 = cmd;
    }

    public void receive35(Server35.CmdData cmd){
        cmd35 = cmd;
    }

    public void receive38(Server38.CmdData cmd){
        cmd38 = cmd;
    }

    public void receive30(Server30.CmdData cmd){
        cmd30 = cmd;
    }

    public void receive23(Server23.CmdData cmd){
        cmd23 = cmd;
    }

    public void close(){
        Log.e("onclose", "caleed");
        if(webSocket == null) return;
        webSocket.close(1000,null);
        webSocket = null;
        cleanCallbacks();
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        //   Log.e("onMessageByte", bytes.toString());
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e("onClosing", "bye bye");
        //webSocket.close(1000, null);
        connected = false;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.e("onClosing", "god bye");
        connected = false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e("failed", t.toString());
        this.webSocket = null;
        if(cmdFail != null){
            handler.post(() -> cmdFail.exec());
        }
    }

    public void cleanCallbacks(){
        cmdFail = null;
        cmdOpen = null;
        cmd35 = null;
        cmdLog = null;
cmd26 = null;
 cmd20 = null;
 cmd10 = null;
 cmd25 = null;
 cmd22 = null;
     cmd24 = null;
       cmd31 = null;
       cmd30 = null;
    cmd34 = null;
    cmd38 = null;
        cmd23 = null;
        handler.removeCallbacksAndMessages(null);
    }
}