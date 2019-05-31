package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;

public class ServerMoney {
    public int errorCode;
    public String errorMessage;
    public Result result;

    public class Result{
        public List<Report> report;
    }

    public class Report{
        public String sn;
        public String time;
        public String money;
        public String opCode;
        public String afterMoney;
        public String beforeMoney;
    }
}
