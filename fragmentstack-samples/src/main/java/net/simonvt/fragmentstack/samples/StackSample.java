package net.simonvt.fragmentstack.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import net.simonvt.fragmentstack.FragmentStack;
import net.simonvt.menudrawer.MenuDrawer;

public class StackSample extends FragmentActivity
    implements ContentFragment.Callback, NavigationFragment.OnMenuClickListener {

  private MenuDrawer drawer;

  private FragmentStack stack;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    drawer = MenuDrawer.attach(this);
    drawer.setupUpIndicator(this);
    drawer.setSlideDrawable(R.drawable.ic_drawer);

    getSupportFragmentManager().beginTransaction()
        .add(drawer.getMenuContainer().getId(), new NavigationFragment())
        .commit();

    stack = FragmentStack.forContainer(this, drawer.getContentContainer().getId(),
        new FragmentStack.Callback() {
          @Override public void onStackChanged(int stackSize, Fragment topFragment) {
            drawer.setDrawerIndicatorEnabled(stackSize <= 1);
            if (!drawer.isMenuVisible()) {
              ContentFragment f = (ContentFragment) topFragment;
              getActionBar().setTitle(f.getMenuEntry());
            }
          }
        });
    stack.setDefaultAnimation(R.anim.fade_in_front, R.anim.fade_out_back, R.anim.fade_in_back,
        R.anim.fade_out_front);

    drawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
      @Override public void onDrawerStateChange(int oldState, int newState) {
        switch (newState) {
          case MenuDrawer.STATE_CLOSED:
            ContentFragment f = (ContentFragment) stack.peek();
            f.setMenuVisibility(true);

            if (!stack.commit()) {
              getActionBar().setTitle(f.getMenuEntry());
            }
            break;

          default:
            stack.peek().setMenuVisibility(false);
            getActionBar().setTitle(R.string.app_name);
            break;
        }
      }

      @Override public void onDrawerSlide(float openRatio, int offsetPixels) {
      }
    });

    if (savedInstanceState == null) {
      stack.replace(ContentFragment.class, "Entry #11", ContentFragment.getArgs("Entry #1", 1));
      stack.commit();
    } else {
      stack.restoreState(savedInstanceState);
    }
  }

  @Override public void onBackPressed() {
    final int drawerState = drawer.getDrawerState();
    if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
      drawer.closeMenu();
      return;
    }

    if (stack.pop(true)) return;

    super.onBackPressed();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    stack.saveState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        final int drawerState = drawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
          drawer.closeMenu();
        } else if (!stack.pop(drawerState == MenuDrawer.STATE_CLOSED)) {
          drawer.toggleMenu();
        }
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void goDeeper(String menuEntry, int toLevel) {
    stack.push(ContentFragment.class, menuEntry + toLevel,
        ContentFragment.getArgs(menuEntry, toLevel));
    stack.commit();
  }

  @Override public void onMenuItemClicked(String entryString) {
    stack.replace(ContentFragment.class, entryString + 1, ContentFragment.getArgs(entryString, 1));
    drawer.closeMenu();
  }
}
