package us.workdone.safercity.model;

import android.content.Context;
import java.text.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

import us.workdone.safercity.R;

public class ReportsListAdapter extends ArrayAdapter<Report> {

    private static final int LAYOUT_RES = R.layout.list_item_report;

    public ReportsListAdapter(Context context) {
        super(context, LAYOUT_RES);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) { v = LayoutInflater.from(getContext()).inflate(LAYOUT_RES, parent, false); }
        Report item = getItem(position);

        // set status marker icons
        View dangerousMarker = v.findViewById(R.id.dangerousMarker);
        dangerousMarker.setVisibility(item.isDangerous ? View.VISIBLE : View.INVISIBLE);

        View unreadMarker = v.findViewById(R.id.unreadMarker);
        unreadMarker.setVisibility(View.INVISIBLE); // TODO impl visibility functionality

        // main report content
        TextView title = (TextView) v.findViewById(R.id.titleText);
        TextView details = (TextView) v.findViewById(R.id.detailsText);
        TextView location = (TextView) v.findViewById(R.id.locationText);
        TextView time = (TextView) v.findViewById(R.id.timeText);

        title.setText(item.title);
        details.setText(item.details);
        location.setText(item.location);
        time.setText(
                DateUtils.formatSameDayTime(
                        item.time.getTime(),
                        new Date().getTime(),
                        DateFormat.MEDIUM,
                        DateFormat.SHORT)); // format date pretty
        return v;
    }

}
