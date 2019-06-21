package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import tw.com.atromoby.utils.Json;
import tw.com.atromoby.widgets.ItemsView;
import tw.com.atromoby.widgets.Popup;
import tw.com.atromoby.widgets.SpinList;
import tw.com.lixin.wmphonebet.R;
import tw.com.lixin.wmphonebet.global.Url;
import tw.com.lixin.wmphonebet.global.User;
import tw.com.lixin.wmphonebet.jsonData.ServerMoney;
import tw.com.lixin.wmphonebet.jsonData.ServerReport;
import tw.com.lixin.wmphonebet.models.QuotaHolder;
import tw.com.lixin.wmphonebet.models.RecordHolder;

public class ReportPopup extends Popup {

    private ItemsView recordView;
    private ConstraintLayout quoteHeader, betHeader;
    private Context context;
    private boolean isBet = true;
    private TextView betBtn, quoteBtn;
    private String todayStr, dateStr;
    private SimpleDateFormat sdf;

    public ReportPopup(Context context) {
        super(context, R.layout.report_popup);

        this.context = context;
        recordView = findViewById(R.id.record_view);
        quoteHeader = findViewById(R.id.quote_header);
        betHeader = findViewById(R.id.bet_header);
        betBtn = findViewById(R.id.bet_record_btn);
        quoteBtn = findViewById(R.id.quote_record_btn);
        SpinList spinList = findViewById(R.id.spin_list);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("us"));
        todayStr = sdf.format(new Date());
        spinList.init(new String[]{context.getString(R.string.today), context.getString(R.string.yesterday), context.getString(R.string.last_week), context.getString(R.string.last_month), context.getString(R.string.all_times)});
        spinList.setColor("#f8ce82");
        spinList.selected(i->{
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            if(i == 0){
                cal.add(Calendar.DAY_OF_YEAR, -1);
            }else if(i == 1){
                cal.add(Calendar.DAY_OF_YEAR, -2);
            }else if(i == 2){
                cal.add(Calendar.DAY_OF_YEAR, -7);
            }else if(i == 3){
                cal.add(Calendar.DAY_OF_YEAR, -30);
            }else{
                cal.add(Calendar.DAY_OF_YEAR, -360);
            }
            dateStr = sdf.format(cal.getTime());
            getBets();
        });


        clicked(R.id.close_btn,v -> dismiss());

        clicked(R.id.bet_record_btn, v->{
            betBtn.setBackgroundResource(R.drawable.blue_grad_color);
            quoteBtn.setBackgroundResource(0);
            isBet = true;
            getBets();
        });
        clicked(R.id.quote_record_btn, v->{
            betBtn.setBackgroundResource(0);
            quoteBtn.setBackgroundResource(R.drawable.blue_grad_color);
            isBet = false;
            getBets();
        });

    }

    private void getBets(){
        recordView.delete();
        RequestTask requestTask = new RequestTask(context, Url.Report, null);
        if(isBet) {
            quoteHeader.setVisibility(View.GONE);
            betHeader.setVisibility(View.VISIBLE);
            requestTask.send("cmd=GetMemberReport&lang=cn&sid=" + User.sid() + "&startTime=" + dateStr + "&endTime=" + todayStr);
            requestTask.onSuccess(r->{
                ServerReport serReport = Json.from(r.content, ServerReport.class);

                for(ServerReport.Report report: serReport.result.report){
                    recordView.add(new RecordHolder(report));
                }

            });
        }else{
            quoteHeader.setVisibility(View.VISIBLE);
            betHeader.setVisibility(View.GONE);
            requestTask.send("cmd=GetMemberMoneyReport&lang=cn&sid=" + User.sid() + "&startTime=" + dateStr + "&endTime=" + todayStr);
            requestTask.onSuccess(r->{
                ServerMoney serverMoney = Json.from(r.content, ServerMoney.class);

                for(ServerMoney.Report report: serverMoney.result.report){
                    recordView.add(new QuotaHolder(report));
                }

            });
        }

    }
}

