package us.workdone.safercity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import static android.text.format.DateUtils.*;

import java.util.Calendar;

import us.workdone.safercity.model.Report;

public class CreateReportActivity extends AppCompatActivity {


    /*
    todo
    tabbing to next focus should include date
    allow scrolling when soft keyboard is out
    keep toolbar in place when soft keyboard is out
    input validation (eg should not allow extension beyond charlimit)
     */

    private Calendar selectedCal;
    private TextView selectedDateTime;
    private static final int DATETIME_FORMATTER_FLAGS =
            FORMAT_SHOW_TIME
            | FORMAT_SHOW_DATE
            | FORMAT_SHOW_WEEKDAY
            | FORMAT_ABBREV_TIME
            | FORMAT_ABBREV_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedCal = Calendar.getInstance();
        setContentView(R.layout.activity_create_report);

        selectedDateTime = (TextView) findViewById(R.id.inputTime);
        updateSelectedDateTime();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setSelectedTime(int hourOfDay, int minute) {
        selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedCal.set(Calendar.MINUTE, minute);
        updateSelectedDateTime();
    }

    public void setSelectedDate(int year, int month, int dayOfMonth) {
        selectedCal.set(Calendar.YEAR, year);
        selectedCal.set(Calendar.MONTH, month);
        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateSelectedDateTime();
    }

    private void updateSelectedDateTime() {
        selectedDateTime.setText(
                DateUtils.formatDateTime(
                        this, selectedCal.getTimeInMillis(), DATETIME_FORMATTER_FLAGS));
    }

    public void pickDate(View v) {
        new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
    }
    public void pickTime(View v) {
        new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
    }

    public void submitReport() {
        Report newReport = new Report(
                ((TextView) findViewById(R.id.inputTitle)).getText().toString(),
                ((TextView) findViewById(R.id.inputLocation)).getText().toString(),
                selectedCal.getTime(),
                ((TextView) findViewById(R.id.inputDetails)).getText().toString()

        );
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                GlobalUtils.BACKEND_URL, newReport.toJSON(),
                System.out::println, e -> e.printStackTrace());
        GlobalUtils.getInstance(this).addToRequestQueue(req);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.actionPostReport:
                submitReport();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            CreateReportActivity source = (CreateReportActivity) getActivity();
            source.setSelectedTime(hourOfDay, minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            CreateReportActivity source = (CreateReportActivity) getActivity();
            source.setSelectedDate(year, month, dayOfMonth);
            source.pickTime(null);
        }
    }
}
