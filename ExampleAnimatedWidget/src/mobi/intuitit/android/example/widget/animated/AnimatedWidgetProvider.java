/*
 * Copyright (C) 2009 Bo Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mobi.intuitit.android.example.widget.animated;

import mobi.intuitit.android.x.launcher.side.HppIntent;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Simple example for a frame animation and a tween animation
 * 
 * @author bo
 * 
 */
public class AnimatedWidgetProvider extends AppWidgetProvider {

    static final String TAG = "Animated Widget Example";

    static final String PNAME = AnimatedWidgetProvider.class.getPackage().toString();

    public static final String ACTION_CLICK = PNAME + ".ACTION_CLICK";

    public static final String EXTRA_CLICK_APPWIDGET_ID = PNAME + ".CLICK_APPWIDGET_ID";
    public static final String EXTRA_CLICK_VIEW_ID = PNAME + ".CLICK_VIEW_ID";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, action + "");

        if (ACTION_CLICK.equals(action)) {
            onClick(context, intent.getIntExtra(EXTRA_CLICK_APPWIDGET_ID, -1), intent.getIntExtra(
                    EXTRA_CLICK_VIEW_ID, -1));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_TWEEN_ANIMATION_STARTED.equals(action)) {
            // Get extras out
            onTweenAnimStart(
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_APPWIDGET_ID, -1),
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_VIEW_ID, -1), 
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_ANIMATION_ID, -1));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_TWEEN_ANIMATION_REPEATED.equals(action)) {
            onTweenAnimRepeat(
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_APPWIDGET_ID, -1),
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_VIEW_ID, -1), 
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_ANIMATION_ID, -1));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_TWEEN_ANIMATION_ENDED.equals(action)) {
            onTweenAnimEnd(
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_APPWIDGET_ID, -1),
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_VIEW_ID, -1), 
                    intent.getIntExtra(HppIntent.EXTRA.EXTRA_ANIMATION_ID, -1));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_FRAME_ANIMATION_STARTED.equals(action)) {
            onFrameAnimStart(intent);
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_FRAME_ANIMATION_STOPPED.equals(action)) {
            onFrameAnimStop(intent);
        } else if (HppIntent.ERROR.ERROR_FRAME_ANIMATION.equals(action)) {
            Log.e(TAG, "Frame Anim: " + intent.getStringExtra(HppIntent.EXTRA.EXTRA_ERROR_MESSAGE));
        } else if (HppIntent.ERROR.ERROR_TWEEN_ANIMATION.equals(action)) {
            Log.e(TAG, "Tween Anim: " + intent.getStringExtra(HppIntent.EXTRA.EXTRA_ERROR_MESSAGE));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_IN_VIEWPORT.equals(action)) {
            // The screen this widget is installed on comes into the viewport
            // Shake myself
            context.sendBroadcast(new Intent(HppIntent.ACTION.ACTION_START_TWEEN_ANIMATION)
                    .putExtra(HppIntent.EXTRA.EXTRA_APPWIDGET_ID, intent.getIntExtra(HppIntent.EXTRA.EXTRA_APPWIDGET_ID, -1))
                    .putExtra(HppIntent.EXTRA.EXTRA_VIEW_ID, R.id.text_hello)
                    .putExtra(HppIntent.EXTRA.EXTRA_ANIMATION_ID, R.anim.shake));
        } else if (HppIntent.NOTIFICATION.NOTIFICATION_OUT_VIEWPORT.equals(action)) {
            // The screen this widget is installed on goes out of the viewport
        } else
            super.onReceive(context, intent); // Handle normal widget intent
    }

    /**
     * 
     * @param intent
     */
    private void onFrameAnimStart(Intent intent) {

    }

    /**
     * 
     * @param intent
     */
    private void onFrameAnimStop(Intent intent) {

    }

    private void onTweenAnimStart(int appWidgetId, int viewId, int animId) {

    }

    private void onTweenAnimRepeat(int appWidgetId, int viewId, int animId) {

    }

    private void onTweenAnimEnd(int appWidgetId, int viewId, int animId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Construct views
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);

        // Instead of being put it in xml, the animation is assigned here to
        // show you that it can be dynamically changed. For example, in a
        // weather widget, you may need this to do multiple step weather transition.
        views.setImageViewResource(R.id.anim_view, R.anim.muybridge);

        // Tell the widget manager
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            setOnClickListener(context, appWidgetId, views, R.id.btn_start);
            setOnClickListener(context, appWidgetId, views, R.id.btn_stop);
            setOnClickListener(context, appWidgetId, views, R.id.btn_shake);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    /**
     * Set pending intent in remote views
     * 
     * @param context
     * @param views
     *            Content remote views
     * @param viewId
     *            The view listening to click
     */
    void setOnClickListener(Context context, int appWidgetId, RemoteViews views, int viewId) {
        Intent active = new Intent(context.getApplicationContext(), AnimatedWidgetProvider.class);

        active.setAction(ACTION_CLICK);

        active.putExtra(EXTRA_CLICK_APPWIDGET_ID, appWidgetId);
        active.putExtra(EXTRA_CLICK_VIEW_ID, viewId);

        // This is tricky, be aware that you can only have one set of extras for
        // any given PendingIntent action+data+category+component pair.
        active.setData(Uri.parse(appWidgetId + ":" + viewId));

        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 0, active, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(viewId, actionPendingIntent);
    }

    /**
     * Callback
     * 
     * @param viewId
     */
    void onClick(Context context, int appWidgetId, int viewId) {

        switch (viewId) {
        case R.id.btn_start:
            // If the widget is installed on a non-Home++ home, sending out this intent would either
            // get a error intent back or no response at all
            context.sendBroadcast(new Intent(HppIntent.ACTION.ACTION_START_FRAME_ANIMATION).putExtra(
                    HppIntent.EXTRA.EXTRA_APPWIDGET_ID, appWidgetId)
                    .putExtra(HppIntent.EXTRA.EXTRA_IMAGEVIEW_ID, R.id.anim_view));
            break;
        case R.id.btn_stop:
            context.sendBroadcast(new Intent(HppIntent.ACTION.ACTION_STOP_FRAME_ANIMATION).putExtra(
                    HppIntent.EXTRA.EXTRA_APPWIDGET_ID, appWidgetId)
                    .putExtra(HppIntent.EXTRA.EXTRA_IMAGEVIEW_ID, R.id.anim_view));
            break;
        case R.id.btn_shake:
            context.sendBroadcast(new Intent(HppIntent.ACTION.ACTION_START_TWEEN_ANIMATION).putExtra(
                    HppIntent.EXTRA.EXTRA_APPWIDGET_ID, appWidgetId)
                    .putExtra(HppIntent.EXTRA.EXTRA_VIEW_ID, R.id.text_hello)
                    .putExtra(HppIntent.EXTRA.EXTRA_ANIMATION_ID, R.anim.shake));
            break;
        default:
        }
    }

}
