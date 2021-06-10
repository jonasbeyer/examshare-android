package de.twisted.imagepicker.imageeditengine.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import de.twisted.imagepicker.imageeditengine.BaseFragment;

public class FragmentUtil {

	public static boolean hadFragment(AppCompatActivity activity) {
		return activity.getSupportFragmentManager().getBackStackEntryCount() != 0;
	}

	public static void replaceFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, 0, 0, 0);

		transaction.replace(contentId, fragment, fragment.getClass().getSimpleName());

		transaction.addToBackStack(null);
		transaction.commit();
	}


	public static void addFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

		String tag = fragment.getClass().getSimpleName() + (fragment.getFragmentId() != -1 ? fragment.getFragmentId() : "");
		transaction.add(contentId, fragment, tag);
		transaction.commit();
	}

	public static void removeFragment(AppCompatActivity activity, BaseFragment fragment) {
		activity.getSupportFragmentManager().beginTransaction()
			.remove(fragment)
			.commit();
	}


	public static void showFragment(AppCompatActivity activity, BaseFragment fragment) {
		activity.getSupportFragmentManager().beginTransaction()
			.show(fragment)
			.commit();
	}

	public static void hideFragment(AppCompatActivity activity, BaseFragment fragment) {
		activity.getSupportFragmentManager().beginTransaction()
			.hide(fragment)
			.commit();
	}

	public static void attachFragment(AppCompatActivity activity, BaseFragment fragment) {
		activity.getSupportFragmentManager().beginTransaction()
			.attach(fragment)
			.commit();
	}

	public static void detachFragment(AppCompatActivity activity, BaseFragment fragment) {
		activity.getSupportFragmentManager().beginTransaction()
			.detach(fragment)
			.commit();
	}

	public static Fragment getFragmentByTag(AppCompatActivity appCompatActivity, String tag)
	{
		return appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);
	}

}