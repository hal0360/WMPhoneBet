package tw.com.lixin.wmphonebet.jsonData;

import java.util.List;

public class ServerReport {
    public int errorCode;
    public String errorMessage;
    public Result result;

    public class Result{
        public List<Report> report;
    }

    public class Report{
        public String betId;
        public String betTime;
        public String bet;
        public String gname;
        public String gameResult;
        public String betResult;
        public String winLoss;
        public String event;
        public String eventChild;
    }
}
