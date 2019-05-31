package tw.com.lixin.wmphonebet.global;


import tw.com.lixin.wmphonebet.App;

public class User {
    public static String account(){
        return App.getStr("account",null);
    }

    public static void account(String val){
        App.putStr("account",val);
    }

    public static String userName(){
        return App.getStr("userName",null);
    }

    public static void userName(String val){
        App.putStr("userName",val);
    }

    public static int memberID(){

        return App.getInt("memberID",0);
    }

    public static void memberID(int val){
        App.putInt("memberID",val);
    }

    public static String sid(){
        return App.getStr("sid",null);
    }

    public static void sid(String val){
        App.putStr("sid",val);
    }

    public static int gameID(){
        return App.getInt("gameID",0);
    }

    public static void balance(float val){
        App.putFloat("balance",val);
    }

    public static float balance(){
        return App.getFloat("balance",0f);
    }

    public static void gameID(int val){
        App.putInt("gameID",val);
    }

    public static void logout(){
        account(null);
        memberID(0);
    }
}
