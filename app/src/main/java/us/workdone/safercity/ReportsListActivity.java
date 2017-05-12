package us.workdone.safercity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.workdone.safercity.model.Report;
import us.workdone.safercity.model.ReportsListAdapter;

public class ReportsListActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_REPORT = 1;

    private ListView listView;
    private ReportsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this::createReport);

        listView = (ListView) findViewById(R.id.reportsList);
        adapter = new ReportsListAdapter(this);
        listView.setAdapter(adapter);
        sendUpdateListReq();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendUpdateListReq();
    }

    public void createReport(View view) {
        Intent intent = new Intent(this, CreateReportActivity.class);
        startActivity(intent);
    }

    private void sendUpdateListReq() {
        Request req = new JsonArrayRequest(Request.Method.GET, GlobalUtils.BACKEND_URL,
                null, this::updateList, e -> e.printStackTrace());
        GlobalUtils.getInstance(this).addToRequestQueue(req);
    }

    private void updateList(JSONArray data) {
        try {
            List<Report> lst = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                lst.add(Report.fromJSON(data.getJSONObject(i)));
            }
            Collections.reverse(lst);
            adapter.clear();
            adapter.addAll(lst);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
