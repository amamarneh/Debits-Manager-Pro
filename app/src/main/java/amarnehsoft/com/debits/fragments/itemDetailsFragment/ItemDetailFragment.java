package amarnehsoft.com.debits.fragments.itemDetailsFragment;

import android.support.v4.app.Fragment;

/**
 * Created by jcc on 8/26/2017.
 */

public abstract class ItemDetailFragment<T> extends Fragment{
    protected T mItem;
    public void refresh(T item){
        mItem = item;
        refreshView();
    }

    protected abstract void refreshView();
}
