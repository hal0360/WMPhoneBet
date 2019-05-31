package tw.com.lixin.wmphonebet.models;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;

import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.jsonData.ServerMoney;

public class QuotaHolder extends ItemHolder {

    private ServerMoney.Report report;

    public QuotaHolder(ServerMoney.Report re){
        super(R.layout.report_quota_item);
        report = re;
    }

    @Override
    public void onBind() {

        ConstraintLayout root = findViewById(R.id.root_view);
        if(position%2 == 0){
            root.setBackgroundColor(Color.parseColor("#e1e1e1"));
        }else{
            root.setBackgroundColor(Color.parseColor("#c0c0c0"));
        }


        if(report == null){
            return;
        }


        setTextView(R.id.quota_time, report.time);
        setTextView(R.id.quota_type, report.opCode);
        setTextView(R.id.quota_after, report.afterMoney);
        setTextView(R.id.quota_money, report.money);
        setTextView(R.id.quota_before, report.beforeMoney);
        setTextView(R.id.quota_no, report.sn);
    }

    @Override
    public void onRecycle() {

    }
}