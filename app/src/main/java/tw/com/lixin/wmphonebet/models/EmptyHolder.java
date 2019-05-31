package tw.com.lixin.wmphonebet.models;

import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmphonebet.R;

public class EmptyHolder  extends ItemHolder {

    public EmptyHolder(){
        super(R.layout.empty_item);

    }

    @Override
    public void onBind() {

    }

    @Override
    public void onRecycle() {

    }
}
