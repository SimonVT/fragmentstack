package net.simonvt.fragmentstack.samples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ContentFragment extends Fragment {

  public interface Callback {
    void goDeeper(String menuEntry, int toLevel);
  }

  private static final String ARG_MENU_ENTRY =
      "net.simonvt.fragmentstack.ContentFragment.menuEntry";
  private static final String ARG_CONTENT_LEVEL =
      "net.simonvt.fragmentstack.ContentFragment.contentLevel";

  private Callback callback;

  private String menuEntry;
  private int contentLevel;

  public static Bundle getArgs(String menuEntry, int contentLevel) {
    Bundle args = new Bundle();
    args.putString(ARG_MENU_ENTRY, menuEntry);
    args.putInt(ARG_CONTENT_LEVEL, contentLevel);
    return args;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    callback = (Callback) activity;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    menuEntry = args.getString(ARG_MENU_ENTRY);
    contentLevel = args.getInt(ARG_CONTENT_LEVEL);

    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.content, container, false);

    ((TextView) v.findViewById(R.id.menuEntry)).setText(menuEntry);
    ((TextView) v.findViewById(R.id.contentLevel)).setText("We're at level " + contentLevel);
    v.findViewById(R.id.goDeeper).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        callback.goDeeper(menuEntry, contentLevel + 1);
      }
    });

    return v;
  }

  public String getMenuEntry() {
    return menuEntry;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.add(0, 1, 0, "A menu item!")
        .setIcon(R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case 1:
        Toast.makeText(getActivity(), "I has options items!11", Toast.LENGTH_SHORT).show();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
