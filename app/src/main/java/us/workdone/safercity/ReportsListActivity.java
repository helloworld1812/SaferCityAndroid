package us.workdone.safercity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.workdone.safercity.model.Report;
import us.workdone.safercity.model.ReportsListAdapter;

public class ReportsListActivity extends AppCompatActivity {

    // TODO ellipses for truncated details view

    private ListView listView;
    private ReportsListAdapter adapter;
    private View refreshActionItem;

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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            refreshActionItem = findViewById(R.id.actionRefresh);
            sendUpdateListReq();
        }
    }

    public void createReport(View view) {
        Intent intent = new Intent(this, CreateReportActivity.class);
        startActivity(intent);
    }

    private void sendUpdateListReq() {
        Animation animation = new RotateAnimation(0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        refreshActionItem.startAnimation(animation);
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
            refreshActionItem.clearAnimation();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.actionRefresh:
                sendUpdateListReq();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
