package net.simonvt.fragmentstack.samples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavigationFragment extends ListFragment {

  public interface OnMenuClickListener {
    void onMenuItemClicked(String entryString);
  }

  private OnMenuClickListener listener;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    listener = (OnMenuClickListener) activity;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final int entryCount = 6;
    String[] entries = new String[entryCount];
    for (int i = 0; i < entryCount; i++) {
      entries[i] = "Entry #" + i;
    }

    setListAdapter(
        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, entries));
  }

  @Override public void onListItemClick(ListView l, View v, int position, long id) {
    listener.onMenuItemClicked("Entry #" + position);
  }
}
