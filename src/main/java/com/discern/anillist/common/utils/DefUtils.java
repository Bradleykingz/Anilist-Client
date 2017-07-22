package com.discern.anillist.common.utils;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.discern.anillist.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefUtils {
    private static Typeface roboto;

/*
    private void showNotifications(Context context, String title, String message){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
*/

    public static void clearSharedPreferences(Context context){
        File sharedPreferenceFile = new File(context.getFilesDir().getPath() + context.getPackageName()+ "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
    }

    public static String getFileExt(File file){
        return file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
    }
    public static class ViewUtils {

        public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

            if (tv.getTag() == null) {
                tv.setTag(tv.getText());
            }
            ViewTreeObserver vto = tv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {

                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    }
                }
            });

        }

        private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                                final int maxLine, final String spanableText, final boolean viewMore) {
            String str = strSpanned.toString();
            SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

            if (str.contains(spanableText)) {


                ssb.setSpan(new MySpannable(false){
                    @Override
                    public void onClick(View widget) {
                        if (viewMore) {
                            tv.setLayoutParams(tv.getLayoutParams());
                            tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                            tv.invalidate();
                            makeTextViewResizable(tv, -1, "View Less", false);
                        } else {
                            tv.setLayoutParams(tv.getLayoutParams());
                            tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                            tv.invalidate();
                            makeTextViewResizable(tv, 3, "View More", true);
                        }
                    }
                }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

            }
            return ssb;

        }


/*
        public static void buildDrawer(final AppCompatActivity activity, final Context context, Toolbar toolbar){

            String email = BaseApplication.getInstance().getActiveUserEmail();
            String username = BaseApplication.getInstance().getActiveUsername();
            String face_url = BaseApplication.getInstance().getActiveFaceUrl();

            IProfile iProfile = new ProfileDrawerItem()
                    .withEmail(email)
                    .withName(username)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            context.startActivity(intent);
                            return true;
                        }
                    })
                    .withIcon(Uri.parse(Constants.SOCKET_DOMAIN + face_url));


            AccountHeader accountHeader = new AccountHeaderBuilder()
                    .withActivity(activity)
                    .withHeaderBackground(new ImageHolder(Uri.parse(Constants.SOCKET_DOMAIN + BaseApplication.getInstance().getActiveBackground())))
                    .withCompactStyle(false)
                    .addProfiles(iProfile)
                    .build();


            final Drawer drawer = new DrawerBuilder()
                    .withActivity(activity)
                    .withItemAnimator(new DefaultItemAnimator())
                    .withAccountHeader(accountHeader)
                    .withToolbar(toolbar)
                    .addDrawerItems(

                            new PrimaryDrawerItem()
                                    .withIcon(R.drawable.ic_home_)
                                    .withIdentifier(1)
                                    .withName("Drawers"),

                            new PrimaryDrawerItem()
                                    .withName("Interests")
                                    .withIdentifier(3)
                                    .withIcon(R.drawable.ic_divide),

                            new SectionDrawerItem().withName("Settings"),

                            new SecondaryDrawerItem()
                                    .withIdentifier(9)
                                    .withIcon(R.drawable.ic_settings)
                                    .withName("Settings"),

                            new SecondaryDrawerItem()
                                    .withIdentifier(10)
                                    .withName("Help and Feedback")
                                    .withIcon(R.drawable.ic_info),

                            new SecondaryDrawerItem()
                                    .withName("Invite")
                                    .withIcon(R.drawable.ic_menu_share)
                                    .withIdentifier(8)


                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                            Intent intent = null;
                            switch ((int) drawerItem.getIdentifier()){
                                case 1:
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("Clicked_On_Shops", "ShopClick");
                                    FirebaseAnalytics.getInstance(context).logEvent("ClickedSomething",bundle1);
//                                    intent = new Intent(context, ShopsActivity.class);
                                    break;
                                case 2:
//                                    intent = new Intent(context, ServicesActivity.class);
                                    break;

                                case 3:
//                                    intent = new Intent(context, AllCategoryActivity.class);
                                    break;



                                case 8:
                                    onInviteClicked();
                                    break;

                                case 9:
                                    intent = new Intent(context, SettingsActivity.class);
                                    break;

                                case 10:
                                    intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String []{"bradstarart@gmail.com"});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, new String [] {"Feedback and Suggestion"});
                                    activity.startActivity(Intent.createChooser(intent, "Send Feedback"));
                                    break;

                                case 111:
//                                    intent = new Intent(context, AllSokoProducts.class);
                                    break;

                                case 54:
//                                    intent = new Intent(context, NoMoneyKidsActivity.class);
                                    break;

                                default:
                                    break;
                            }
                            if (intent!=null){
//                           ActivityTransitionLauncher.with(MeActivity.this).from(view).launch(intent);
                                //  ActivityTransitionLauncher.with(MainActivity.this).from(v).launch(intent);
                                context.startActivity(intent);
                            }
                            return false;
                        }

                        // TODO: 02/02/17 fixme
                        private void onInviteClicked() {
                            Intent intent = new AppInviteInvitation.IntentBuilder(activity.getString(R.string.invitation_title))
                                    .setDeepLink(Uri.parse(activity.getString(R.string.deep_link)))
                                    .setCustomImage(Uri.parse("https://i.imgur.com/cVLRWs3.jpg"))
                                    .setMessage("Join Soko Nyumbani")
                                    .build();

                            activity.startActivityForResult(intent, 9988);
                        }
                    })
                    .build();

            activity.setSupportActionBar(toolbar);
            blendColors(R.color.white, R.color.md_blue_grey_300, 1);
            toolbar.setNavigationIcon(R.drawable.ic_hamburger_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen()){
                        drawer.closeDrawer();
                    }

                    if (!drawer.isDrawerOpen()){
                        drawer.openDrawer();
                    }
                }
            });
        }
*/


        public static void tintSystemBars(final Context context, final AppCompatActivity appCompatActivity) {
            // Initial colors of each system bar.
            final int statusBarColor = ContextCompat.getColor(context, R.color.accent);
            final int toolbarColor = ContextCompat.getColor(context, R.color.white);

            // Desired final colors of each bar.
            final int statusBarToColor = context.getResources().getColor(R.color.md_yellow_50);
            final int toolbarToColor = context.getResources().getColor(R.color.background_grey);

            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // Use animation position to blend colors.
                    float position = animation.getAnimatedFraction();

                    // Apply blended color to the status bar.
                    int blended = blendColors(statusBarColor, statusBarToColor, position);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appCompatActivity.getWindow().setStatusBarColor(blended);
                    }

                    // Apply blended color to the ActionBar.
                    blended = blendColors(toolbarColor, toolbarToColor, position);
                    ColorDrawable background = new ColorDrawable(blended);
                    appCompatActivity.getSupportActionBar().setBackgroundDrawable(background);

                }
            });

            anim.setDuration(500).start();
        }

        private static int blendColors(int from, int to, float ratio) {
            final float inverseRatio = 1f - ratio;

            final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
            final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
            final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

            return Color.rgb((int) r, (int) g, (int) b);
        }


        public static void setCollapsedColor(final Context context, AppBarLayout appBarLayout,
                                              final CollapsingToolbarLayout toolbarLayout, final Toolbar toolbar){
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                int scrollFlags = -1;
                @Override
                public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {

                    if (scrollFlags ==-1){
                        scrollFlags = appBarLayout.getTotalScrollRange();
                    }

                    if (toolbarLayout.getHeight() + verticalOffset < (2* ViewCompat.getMinimumHeight(toolbarLayout))){
                        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
                    }else {
                        toolbar.setBackgroundColor(Color.TRANSPARENT);
                    }

                }
            });

        }

    }


    /**
     * Use this method to get the first letters in a string input with multiple words
     * @param input
     * String input
     * @return
     * first letters
     */
    public static String getFirstLetters(String input){
        StringBuilder sb = new StringBuilder();
        for(String s : input.split(" ")){
            sb.append(s.charAt(0));
        }
        return sb.toString();
    }

    /**
     * Get the first word in a sentence
     * @param text
     * @return
     */

    public static String getFirstWord(String text) {
        if (text.indexOf(' ') > -1) { // Check if there is more than one word.
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }

    @NonNull
    public static String getEmailDomain(String s){
        return s.substring(s.indexOf("@")+1);
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 160);
    }

    public static  int getBackgroundColour(View view){
        int color;
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable){
            color = ((ColorDrawable) background).getColor();
            return color;
        }
        else return 0;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatK(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatK(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatK(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String tofirstCaps(String string){
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


    public static int getRandomColor(){
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }
    public static String formatPrice(int number){
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(number);
    }

    public static void clearCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    private static long getDirectorySize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (File aFileList : fileList) {
                // Recursive call if it's a directory
                if (aFileList.isDirectory()) {
                    result += getDirectorySize(aFileList);
                } else {
                    // Sum the file size in bytes
                    result += aFileList.length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static  String getReadableSize(File cacheDir){
        return humanReadableByteCount(getDirectorySize(cacheDir), true);
    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static boolean containsIllegals(String toExamine) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    private static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);

        }

        @Override
        public void onClick(View widget) {

        }
    }
}
