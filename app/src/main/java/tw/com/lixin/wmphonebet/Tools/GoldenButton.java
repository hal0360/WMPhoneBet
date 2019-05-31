package tw.com.lixin.wmphonebet.Tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import tw.com.atromoby.widgets.CmdView;

public class GoldenButton extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private CmdView cmd;
    private Boolean disabled = true;

    public GoldenButton(Context context) {
        super(context);
        this.setAlpha(0.5f);
    }

    public GoldenButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setAlpha(0.5f);
    }

    public void disable(Boolean disabled){
        this.disabled = disabled;
        if(disabled){
            this.setAlpha(0.5f);
        }else{
            this.setAlpha(1.0f);
        }
    }

    public boolean isDisabled(){
        return disabled;
    }

    public void clicked(CmdView cd){
        setOnClickListener(this);
        cmd = cd;
    }

    @Override
    public void onClick(View v) {
        if(cmd != null && !disabled){
            cmd.exec(v);
        }
    }
}
