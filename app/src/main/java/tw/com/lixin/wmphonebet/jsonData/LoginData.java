package tw.com.lixin.wmphonebet.jsonData;

public class LoginData {

    public Data data;
    public int protocol = 0;

    public LoginData(String user, String pass) {
        data = new Data();
        data.account = user;
        data.password = pass;
    }

    private class Data{
        String account = "ANONYMOUS";
        String password = "1234";
    }
}
