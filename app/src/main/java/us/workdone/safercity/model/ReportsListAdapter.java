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

    // todo sort list

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
        if (item.details.trim().isEmpty()) {
            v.findViewById(R.id.layoutDetails).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.layoutDetails).setVisibility(View.VISIBLE);
            details.setText(item.details.trim());
        }
        location.setText(item.location);
        time.setText(
                DateUtils.formatSameDayTime(
                        item.time.getTime(),
                        new Date().getTime(),
                        DateFormat.DEFAULT,
                        DateFormat.SHORT));
        return v;
    }

}