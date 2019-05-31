package tw.com.lixin.wmphonebet;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tw.com.atromoby.utils.RegisterApplication;

public class App extends RegisterApplication {

    public static CasinoSocket socket;
    public static final int gameID = 101;
    public static int groupID = 1;
    public static List<Table> tables;
    public static Table curTable;
    public static Group group;


    //public static CoinStackBack coinBack;

    @Override
    public void onCreate() {
        super.onCreate();
     //   coinBack = new CoinStackBack();
        socket = new CasinoSocket();
        tables = new ArrayList<>();

        group = new Group();
    }

    public static void logout(){
        Log.e("app logout", "caleed");
        socket.close();
        socket.cleanCallbacks();

    }


    public static void cleanSocketCalls(){
        socket.cleanCallbacks();
    }

    public static Table findTable(int id){
        for(Table tt: tables){
            if(tt.groupID == id){

                return tt;
            }
        }
        return null;
    }



}
