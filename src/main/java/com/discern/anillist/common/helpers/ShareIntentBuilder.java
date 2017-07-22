package com.discern.anillist.common.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.plus.PlusShare;


public class ShareIntentBuilder {

	public static ShareIntentBuilder create() {
        return new ShareIntentBuilder();
	}

	public ShareIntentBuilder shareByEmail(String subject, String text) {
		return shareByEmail(subject, text, null);
	}

	public ShareIntentBuilder shareByEmail(String subject, String text, @Nullable String sendToEMail) {

		Intent emailIntent;
		if (sendToEMail != null) {
			emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", sendToEMail, null));
		} else {
			emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
		}

		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, text);

		_intents.add(emailIntent);
		return this;
	}

	public ShareIntentBuilder shareBySms(String textBody) {
		return shareBySms(textBody, null);
	}

	public ShareIntentBuilder shareBySms(String textBody, @Nullable String sendToNumber) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		String uri = "sms:";
		if (sendToNumber != null) {
			uri += sendToNumber;
		}

		smsIntent.setData(Uri.parse(uri));
		smsIntent.putExtra("sms_body", textBody);

		_intents.add(smsIntent);

		return this;
	}

//	public ShareIntentBuilder shareByGooglePlus(Context context, String text, String contentUrl) {
//		Intent googlePlusIntent = new PlusShare.Builder(context).setType("text/plain")
//																.setText(text)
//																.setContentUrl(Uri.parse(contentUrl))
//																.getIntent();
//
//		_intents.add(googlePlusIntent);
//
//		return this;
//	}

	public ShareIntentBuilder shareByMessenger(String text) {
		Intent messangerIntent = new Intent(Intent.ACTION_SEND);
		messangerIntent.putExtra(Intent.EXTRA_TEXT, text);
		messangerIntent.setType("text/plain");
		messangerIntent.setPackage("com.facebook.orca");

		_intents.add(messangerIntent);

		return this;
	}

	public Intent build(final String chooserTitle, final Context context) {
		if (_intents.isEmpty()) {
			throw new IllegalStateException("Please add shares!");
		}

		final PackageManager packageManager = context.getPackageManager();

		ArrayList<LabeledIntent> extraIntents = new ArrayList<>(_intents.size() * 2);
		Intent mainIntent = null;

		for (int i = 0; i < _intents.size(); ++i) {
			Intent intent = _intents.get(i);

			List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
			if (resolveInfoList == null) {//Some devices return null...
				continue;
			}
			if (mainIntent == null && !resolveInfoList.isEmpty()) {
				mainIntent = intent;
				//This will be main chooser so we don't want duplicates
				continue;
			}
			for (ResolveInfo info : resolveInfoList) {
				// Extract the label, append it, and repackage it in a LabeledIntent
				String packageName = info.activityInfo.packageName;
				intent.setComponent(new ComponentName(packageName, info.activityInfo.name));
				extraIntents.add(new LabeledIntent(intent, packageName, info.loadLabel(packageManager), info.icon));
			}
		}

		if (mainIntent == null) {
			mainIntent = _intents.get(0);
			Log.e(ShareIntentBuilder.class.getSimpleName(), "No app can't handle such share request");
		}

		//Android must find at least 1 app for mainIntent. If we don't find any app that can handle we will see empty list
		final Intent chooser = Intent.createChooser(mainIntent, chooserTitle);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new LabeledIntent[extraIntents.size()]));
		return chooser;
	}

	private ArrayList<Intent> _intents = new ArrayList<>(8);
}