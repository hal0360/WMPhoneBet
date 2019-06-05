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
import tw.com.lixin.wmphonebet.App;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.jsonData.LoginData;
import tw.com.lixin.wmphonebet.jsonData.LoginResData;

public abstract class CasinoSocket extends WebSocketListener {

    private WebSocket webSocket = null;
    public CmdStr  cmdFail;
    public Handler handler = new Handler();
    public boolean connected = false;
    public LoginData loginData;
    public String webUrl;

    public CmdLog cmdOpen;

    public void onSuccess(CmdLog cmd){
        cmdOpen = cmd;
    }

    public void onFail(CmdStr cmd){
        cmdFail = cmd;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        send(Json.to(loginData));
    }

    public void login(String user, String pass){
        loginData = new LoginData( user, pass);
        close();
        OkHttpClient client = new OkHttpClient();
        webSocket = client.newWebSocket(new Request.Builder().url(webUrl).build(), this);
        client.dispatcher().executorService().shutdown();
    }

    public abstract void onReceive(String text);

    @Override
    public void onMessage(WebSocket webSocket, String text) {

        if(!connected){
            LoginResData logRespend = Json.from(text, LoginResData.class);
            if(logRespend.protocol == 0){
                if(logRespend.data.bOk){
                    connected = true;
                    if(cmdOpen != null){
                        handler.post(() -> cmdOpen.exec(logRespend.data));
                    }
                }else {
                    if(cmdFail != null){
                        handler.post(() -> cmdFail.exec("Fail to login. Password might be wrong"));
                    }
                }
            }

        }else {
            onReceive(text);
        }
    }

    public void send(String message){
        webSocket.send(message);
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
            handler.post(() -> cmdFail.exec(t.toString()));
        }
    }

    public void cleanCallbacks(){
        cmdFail = null;
        cmdOpen = null;
        handler.removeCallbacksAndMessages(null);
    }
}