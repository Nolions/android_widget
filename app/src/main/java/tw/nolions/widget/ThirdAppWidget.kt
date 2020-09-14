package tw.nolions.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*


private val mSharedPrefFile = "com.example.android.appwidgetsample"
private val COUNT_KEY = "count"

/**
 * Implementation of App Widget functionality.
 */
class ThirdAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateThirdAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}


internal fun updateThirdAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val prefs = context.getSharedPreferences(mSharedPrefFile, 0)
    var count = prefs.getInt(COUNT_KEY + appWidgetId, 0)
    count++

    val pendingUpdate = PendingIntent.getBroadcast(
        context, appWidgetId, getIntent(context, appWidgetId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    setView(
        context,
        appWidgetManager,
        appWidgetId,
        count,
        SimpleDateFormat("HH:mm:ss", Locale.TAIWAN).format(Date()),
        pendingUpdate
    )

    updateSharedPreferences(prefs, appWidgetId, count)
}

internal fun getIntent(context: Context, appWidgetId: Int): Intent {
    val intent = Intent(context, ThirdAppWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    val idArray = intArrayOf(appWidgetId)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

    return intent
}

internal fun setView(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    count: Int,
    time: String,
    pendingIntent: PendingIntent
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.third_app_widget)
    views.setTextViewText(
        R.id.appwidget_update3, context.resources.getString(
            R.string.date_count_format,
            count,
            time
        )
    )
    views.setTextViewText(R.id.appwidget_id3, "$appWidgetId")
    views.setOnClickPendingIntent(R.id.button_update, pendingIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun updateSharedPreferences(prefs: SharedPreferences, appWidgetId: Int, count: Int) {
    val prefEditor = prefs.edit()
    prefEditor.putInt(COUNT_KEY + appWidgetId, count)
    prefEditor.apply()
}