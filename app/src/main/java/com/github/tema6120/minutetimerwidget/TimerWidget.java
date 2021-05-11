package com.github.tema6120.minutetimerwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TimerWidget extends AppWidgetProvider {
    private static final String PREFERENCES_NAME = "DISPLAY";
    private static final String ACTION_BACKSPACE_BUTTON_CLICKED = "ACTION_BACKSPACE_BUTTON_CLICKED";
    private static final String ACTION_1_BUTTON_CLICKED = "ACTION_1_BUTTON_CLICKED";
    private static final String ACTION_2_BUTTON_CLICKED = "ACTION_2_BUTTON_CLICKED";
    private static final String ACTION_3_BUTTON_CLICKED = "ACTION_3_BUTTON_CLICKED";
    private static final String ACTION_4_BUTTON_CLICKED = "ACTION_4_BUTTON_CLICKED";
    private static final String ACTION_5_BUTTON_CLICKED = "ACTION_5_BUTTON_CLICKED";
    private static final String ACTION_6_BUTTON_CLICKED = "ACTION_6_BUTTON_CLICKED";
    private static final String ACTION_7_BUTTON_CLICKED = "ACTION_7_BUTTON_CLICKED";
    private static final String ACTION_8_BUTTON_CLICKED = "ACTION_8_BUTTON_CLICKED";
    private static final String ACTION_9_BUTTON_CLICKED = "ACTION_9_BUTTON_CLICKED";
    private static final String ACTION_0_BUTTON_CLICKED = "ACTION_0_BUTTON_CLICKED";
    private static final String ACTION_START_BUTTON_CLICKED = "ACTION_START_BUTTON_CLICKED";
    private static final String EXTRA_APP_WIDGET_ID = "EXTRA_APP_WIDGET_ID";
    private static final int MAX_DIGITS = 3;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetId);
        }
    }

    private void updateWidget(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_timer);
        WidgetTuner widgetTuner = new WidgetTuner();
        widgetTuner.context = context;
        widgetTuner.views = views;
        widgetTuner.appWidgetId = appWidgetId;
        widgetTuner.setOnClick(R.id.backspaceButton, ACTION_BACKSPACE_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button1, ACTION_1_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button2, ACTION_2_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button3, ACTION_3_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button4, ACTION_4_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button5, ACTION_5_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button6, ACTION_6_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button7, ACTION_7_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button8, ACTION_8_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button9, ACTION_9_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.button0, ACTION_0_BUTTON_CLICKED);
        widgetTuner.setOnClick(R.id.startButton, ACTION_START_BUTTON_CLICKED);
        widgetTuner.updateDisplayText();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static class WidgetTuner {
        Context context;
        RemoteViews views;
        int appWidgetId;

        void setOnClick(int viewId, String action) {
            Intent intent = new Intent(context, TimerWidget.class);
            intent.setAction(action);
            intent.putExtra(EXTRA_APP_WIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
            views.setOnClickPendingIntent(viewId, pendingIntent);
        }

        void updateDisplayText() {
            SharedPreferences prefs =
                    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            String prefKey = String.valueOf(appWidgetId);
            String timerValue = prefs.getString(prefKey, "0");
            String timerDisplayingValue =
                    context.getString(R.string.timer_displaying_value, timerValue);
            views.setTextViewText(R.id.displayTextView, timerDisplayingValue);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int appWidgetId = intent.getIntExtra(EXTRA_APP_WIDGET_ID, -1);
        if (action != null && appWidgetId != -1) {
            switch (action) {
                case ACTION_BACKSPACE_BUTTON_CLICKED:
                    deleteLastDigit(context, appWidgetId);
                    break;
                case ACTION_1_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 1);
                    break;
                case ACTION_2_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 2);
                    break;
                case ACTION_3_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 3);
                    break;
                case ACTION_4_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 4);
                    break;
                case ACTION_5_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 5);
                    break;
                case ACTION_6_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 6);
                    break;
                case ACTION_7_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 7);
                    break;
                case ACTION_8_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 8);
                    break;
                case ACTION_9_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 9);
                    break;
                case ACTION_0_BUTTON_CLICKED:
                    appendDigit(context, appWidgetId, 0);
                    break;
                case ACTION_START_BUTTON_CLICKED:
                    setTimer(context, appWidgetId);
                    break;
            }
        }
        super.onReceive(context, intent);
    }

    private void deleteLastDigit(
            Context context,
            int appWidgetId
    ) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String prefKey = String.valueOf(appWidgetId);
        String timerValue = prefs.getString(prefKey, "0");
        if (timerValue.equals("0")) return;
        String newValue;
        if (timerValue.length() == 1) {
            newValue = "0";
        } else {
            newValue = timerValue.substring(0, timerValue.length() - 1);
        }
        prefs.edit()
                .putString(prefKey, newValue)
                .apply();
        updateWidget(context, appWidgetId);
    }

    private void appendDigit(
            Context context,
            int appWidgetId,
            int digit
    ) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String prefKey = String.valueOf(appWidgetId);
        String timerValue = prefs.getString(prefKey, "0");
        if (timerValue.length() >= MAX_DIGITS) return;
        if (timerValue.startsWith("0")) {
            if (digit == 0) return;
            timerValue = String.valueOf(digit);
        } else {
            timerValue = timerValue + digit;
        }
        prefs.edit()
                .putString(prefKey, timerValue)
                .apply();
        updateWidget(context, appWidgetId);
    }

    private void setTimer(
            Context context,
            int appWidgetId
    ) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String prefKey = String.valueOf(appWidgetId);
        String timerValue = prefs.getString(prefKey, "0");
        if (!timerValue.equals("0")) {
            prefs.edit()
                    .putString(prefKey, "0")
                    .apply();
            updateWidget(context, appWidgetId);
            int seconds = Integer.valueOf(timerValue) * 60;
            sendIntentToTimerApp(context, seconds);
        }
    }

    private void sendIntentToTimerApp(
            Context context,
            int seconds
    ) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.timer_app_was_not_found, Toast.LENGTH_LONG)
                    .show();
        }
    }
}