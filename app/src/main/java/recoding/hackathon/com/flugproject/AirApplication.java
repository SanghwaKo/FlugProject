package recoding.hackathon.com.flugproject;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.estimote.coresdk.recognition.packets.Mirror;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.sdk.mirror.context.DisplayCallback;
import com.estimote.sdk.mirror.context.MirrorContextManager;
import com.estimote.sdk.mirror.context.Zone;
import com.estimote.sdk.mirror.core.common.exception.MirrorException;
import com.estimote.sdk.mirror.core.connection.Dictionary;
import com.estimote.sdk.mirror.core.connection.MirrorDevice;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by KSH on 2017-06-17.
 */

public class AirApplication extends Application {
    private static AirApplication mInstance;

    private String mBeaconId = ""; // Gets from a beacon, when the device is connected to it.
    private String mDeviceId = ""; // the device's uuid
    private MirrorContextManager mMirrorContextManager; // To communicate with the connected-beacon.

    private ArrayList<Flight> mFlights; // Flights information which the device gets from the airport's api
    private Flight mUserFilght; // User flight - The user can select his flight from the flights' list
    private ArrayList<Product> mCollectedProducts; // Products which the users saved.
    private int mCntSavedItems = 0;

    private Activity mCurrentActivity;

    // SingleTon
    public static AirApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mCollectedProducts = new ArrayList<>();

        mMirrorContextManager = MirrorContextManager.createMirrorContextManager(getApplicationContext());

        final BeaconManager beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.setMirrorListener(new BeaconManager.MirrorListener() {
                    @Override
                    public void onMirrorsFound(List<Mirror> mirrors) {
                        if(mirrors.size() != 0){
                            MirrorDevice mirrorDevice = new MirrorDevice(mirrors.get(0));
                            mBeaconId = mirrorDevice.deviceId.toHexString();
                            displayContentForId(mBeaconId);
                            setIsAttachedBeacon(true);
                            if(Debug.DEBUG){
                                Log.d(Constant.APP_TAG, "detectedBeacon " + mBeaconId);
                            }
                        }else{
                            setIsAttachedBeacon(false);
                            if(Debug.DEBUG){
                                Log.d(Constant.APP_TAG, "There is no beacon detected...");
                            }
                        }
                    }
                });
                beaconManager.startMirrorDiscovery();
            }
        });

        mFlights = new ArrayList<>();
    }

    private void displayContentForId(String beaconId){
        final Dictionary dictionary = new Dictionary();
        dictionary.setTemplate("air");

        // Send data to the connected-beacon.
        dictionary.put(Constant.TAG_BEACON_ID, beaconId);
        dictionary.put(Constant.TAG_DEVICE_ID, getUUID());

        if(Debug.DEBUG){
            Log.d(Constant.APP_TAG, "deviceId " + getUUID());
        }

        mMirrorContextManager.display(dictionary, Zone.WHEREVER_YOU_ARE, new DisplayCallback() {
            @Override
            public void onDataDisplayed() {
                if(Debug.DEBUG){
                    Log.d(Constant.APP_TAG, "onDataDisplayed");
                }
                // Called when sending data to the Mirror succeeded
            }

            @Override
            public void onFinish() {
                if(Debug.DEBUG){
                    Log.d(Constant.APP_TAG, "onFinish");
                }

                // Called when sending data display finished
            }

            @Override
            public void onFailure(MirrorException exception) {
                if(Debug.DEBUG){
                    Log.d(Constant.APP_TAG, "MirrorException" + exception.getMessage());
                }
                // Called when sending data to the Mirror failed
            }

            @Override
            public void onData(JSONObject data) {
                // Get data from the mirror detected..
                if(Debug.DEBUG){
                    Log.d(Constant.APP_TAG, "onData ");
                }
                ////// get the data about the products which are recommended by beacons.
            }
        });
    }

    public ArrayList<Flight> getFlights(){
        return mFlights;
    }

    public void addFlight(Flight flight){
        mFlights.add(flight);
    }

    public int getCntSavedItems(){
        return mCntSavedItems;
    }

    public void increaseCntSavedItems(){
        mCntSavedItems++;
    }

    public void setIsAttachedBeacon(boolean isAttachedBeacon){
        if(Debug.DEBUG){
            Log.d(Constant.APP_TAG, "setIsAttachedBeacon() Called " + isAttachedBeacon);
        }

        if(isAttachedBeacon){
            if(mCurrentActivity instanceof MainActivity){
                ((MainActivity)mCurrentActivity).setWithBeaconView();
            }
        }else{
            if(mCurrentActivity instanceof MainActivity){
                ((MainActivity)mCurrentActivity).setWaitBeaconSignalView();
                // We are waiting for a beacon's signal
            }
        }
    }

    public void setUserFlight(Flight flight){
        mUserFilght = flight;
        // Will be used in FlightInfoActivity
    }

    public Flight getUserFilght(){
        return mUserFilght;
    }

    public String getUUID(){
        if(mDeviceId.equals("")){
            mDeviceId = UUID.randomUUID().toString();
        }
        return mDeviceId;
    }

   public void addCollectedProduct(Product product){
       mCollectedProducts.add(product);
   }

   public ArrayList<Product> getCollectedProducts(){
       return mCollectedProducts;
   }

   public void setCurrentActivity(Activity activity){
       mCurrentActivity = activity;
   }
}
