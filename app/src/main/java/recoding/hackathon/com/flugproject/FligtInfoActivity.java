package recoding.hackathon.com.flugproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by KSH on 2017-06-17.
 */

public class FligtInfoActivity extends Activity implements AdapterView.OnItemClickListener{
    private ListView mFlightListView;
    private ArrayList<Flight> mFlights;
    private FlightListAdapter mFlightAdapter;
    private AirApplication mAirApplication;

    @Override
    protected void onCreate(Bundle saved){
        super.onCreate(saved);

        setContentView(R.layout.flight_info_page);
        mAirApplication = AirApplication.getInstance();
        mAirApplication.setCurrentActivity(this);

        mFlights = mAirApplication.getFlights();

        mFlightListView = (ListView)findViewById(R.id.flight_listview);
        mFlightAdapter = new FlightListAdapter(this, mFlights);
        mFlightListView.setAdapter(mFlightAdapter);
        mFlightListView.setOnItemClickListener(this);
    }

    private class FlightListAdapter extends BaseAdapter{
        private ArrayList<Flight> flights;
        private Context context;

        public FlightListAdapter(Context context, ArrayList<Flight> flights){
            this.flights = flights;
            this.context = context;
        }

        @Override
        public int getCount() {
            return flights.size();
        }

        @Override
        public Object getItem(int position) {
            return flights.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FlightView flightView;
            Flight flight = flights.get(position);

            if(convertView == null){
                flightView = new FlightView(context);
            }else{
                flightView = (FlightView)convertView;
            }

            String schedule = flight.getScheduleTime();
            String time = schedule.substring(11, schedule.length());
            flightView.setInfo(flight.getAirlineCode() + " - " + flight.getArrivalAirport());

            flightView.setTime(time);

            return flightView;
        }
    }

    private class FlightView extends RelativeLayout{
        private TextView flightInfo;
        private TextView flightTime;

        public FlightView(Context context){
            super(context);

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.flight_row, this, true);

            flightInfo = (TextView)findViewById(R.id.flight_info);
            flightTime = (TextView)findViewById(R.id.flight_time);
        }

        public void setInfo(String info){
            flightInfo.setText(info);
        }

        public void setTime(String time){
            flightTime.setText(time);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Flight flight = mAirApplication.getFlights().get(position);
        mAirApplication.setUserFlight(flight);
        Toast.makeText(FligtInfoActivity.this, "Selected a flight : " + flight.getAirlineCode(), Toast.LENGTH_LONG).show();
        finish();
        // Set User Flight
    }
}
