package tw.com.lixin.wmphonebet;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tw.com.atromoby.utils.RegisterApplication;
import tw.com.lixin.wmphonebet.Tools.CasinoSocket;
import tw.com.lixin.wmphonebet.models.Table;

public class App extends RegisterApplication {

    public static final int gameID = 101;
    public static int groupID = 1;
    public static List<Table> tables;
    public static Table curTable;


    //public static CoinStackBack coinBack;

    @Override
    public void onCreate() {
        super.onCreate();
     //   coinBack = new CoinStackBack();
        tables = new ArrayList<>();

    }

    public static void logout(){
        Log.e("app logout", "caleed");


    }


    public static void cleanSocketCalls(){

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
