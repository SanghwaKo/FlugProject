package recoding.hackathon.com.flugproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private AirApplication mAirApplication;

    // URL..
    private static String mFlightInfoURL = "http://air.trimmer.io/flights?deviceId=1\\&beaconId=1";
    // Cannot get the data after the Hackathon, because of inaccessibility to the airport's api anymore.
    private static String mProductBaseURL = "http://air.trimmer.io/products/";
    private String mDeviceIdURL = "?deviceId=1";// example : m1?deviceId=1";

    private String[] mProductIds = new String[8];
    private ArrayList<Product> mProducts;
    private boolean mIsLoadedData = false;
    private int mCurrentProductIndex = 0;
    private View mProductColor;

    // UI
    private LinearLayout mLayoutMenus;

    private TextView mSavedItems;
    private TextView mProductName;
    private ImageView mProductImg;
    private TextView mProductInfo;

    // Wait till any beacon nearby is detected...
    private LinearLayout mLayoutWaitingSignal;
    private RelativeLayout mLayoutProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        mAirApplication = AirApplication.getInstance();
        mAirApplication.setCurrentActivity(this);

        mLayoutWaitingSignal = (LinearLayout)findViewById(R.id.layout_waiting_beaconsignal);
        mLayoutWaitingSignal.setVisibility(View.VISIBLE);
        mSavedItems = (TextView)findViewById(R.id.saved_bookmark);
        mLayoutProduct = (RelativeLayout)findViewById(R.id.layout_products);

        refreshCntBookmark();

        mLayoutMenus = (LinearLayout)findViewById(R.id.layout_menus);
        mProductName = (TextView)findViewById(R.id.product_name);
        mProductImg = (ImageView)findViewById(R.id.product_img);
        mProductInfo = (TextView)findViewById(R.id.product_info);
        mProductColor = findViewById(R.id.product_color);

        for(int i=0; i<4; i++){
            mProductIds[i] = "m" + (i+1);
            mProductIds[i+4] = "w" + (i+1);
        }
        mProducts = new ArrayList<>();
        new GetData().execute();
    }

    private void refreshCntBookmark(){
        mSavedItems.setText(getString(R.string.saved).replace("%%DD", mAirApplication.getCntSavedItems() + ""));
    }

    public void setWithBeaconView(){
        mLayoutWaitingSignal.setVisibility(View.GONE);
        mLayoutProduct.setVisibility(View.VISIBLE);
    }

    public void setWaitBeaconSignalView(){
        mLayoutProduct.setVisibility(View.GONE);
        mLayoutWaitingSignal.setVisibility(View.VISIBLE);
    }

    public void onMenu(View view){
        //Setting....
        if(mLayoutMenus.getVisibility() == View.VISIBLE){
            mLayoutMenus.setVisibility(View.GONE);
        }else{
            mLayoutMenus.setVisibility(View.VISIBLE);
        }
    }

    // Skip the product
    public void onSkipProduct(View view){
        mCurrentProductIndex++;
        if(mCurrentProductIndex < mProducts.size()){
            showProductImage();
        }
    }

    // Save the product
    public void onSaveProduct(View view){
        mCurrentProductIndex++;
        if(mCurrentProductIndex < mProducts.size()){
            showProductImage();
            mAirApplication.increaseCntSavedItems();
            refreshCntBookmark();
        }
    }

    public void onCollected(View view){

    }

    public void onBasket(View view){

    }

    public void onMyFlight(View view){
        Intent intent = new Intent(getApplicationContext(), FligtInfoActivity.class);
        startActivity(intent);
    }

    public void onSettings(View view){

    }

    public void onLogout(View view){
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mIsLoadedData = true;
            showProductImage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(mFlightInfoURL);

            if(jsonStr != null){
                try{
                    if(Debug.DEBUG){
                        Log.d(Constant.APP_TAG, "Flights " + jsonStr);
                    }

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray flights = jsonObj.getJSONArray(Constant.TAG_FLIGHT);

                    for(int i=0; i<flights.length(); i++){
                        JSONObject oneFlight = flights.getJSONObject(i);
                        Flight flight = new Flight();

                        if(oneFlight.has(Constant.TAG_AIR_CODE)){
                            flight.setAirlineCode(oneFlight.getString(Constant.TAG_AIR_CODE));
                        }
                        if(oneFlight.has(Constant.TAG_AIR_NAME)){
                            flight.setAirlineName(oneFlight.getString(Constant.TAG_AIR_NAME));
                        }
                        if(oneFlight.has(Constant.TAG_AIR_FNUMBER)){
                            flight.setFlightNumber(oneFlight.getString(Constant.TAG_AIR_FNUMBER));
                        }
                        if(oneFlight.has(Constant.TAG_DEPARTURE)){
                            flight.setDepartureAirport(oneFlight.getString(Constant.TAG_DEPARTURE));
                        }
                        if(oneFlight.has(Constant.TAG_ARRIVAL)){
                            flight.setArrivalAirport(oneFlight.getString(Constant.TAG_ARRIVAL));
                        }
                        if(oneFlight.has(Constant.TAG_SCHEDULED_TIME)){
                            flight.setScheduleTime(oneFlight.getString(Constant.TAG_SCHEDULED_TIME));
                        }
                        if(oneFlight.has(Constant.TAG_BOARDING_TIME)){
                            flight.setBoardingTime(oneFlight.getString(Constant.TAG_BOARDING_TIME));
                        }
                        if(oneFlight.has(Constant.TAG_TERMINAL)){
                            flight.setTerminal(oneFlight.getString(Constant.TAG_TERMINAL));
                        }
                        if(oneFlight.has(Constant.TAG_GATE)){
                            flight.setGate(oneFlight.getString(Constant.TAG_GATE));
                        }
                        mAirApplication.addFlight(flight);
                    }

                }catch (JSONException ex){
                    Log.e(Constant.APP_TAG, "JSONException " + ex.getMessage());
                }
            }
            for(int i=0; i<mProductIds.length; i++){
                Product newProduct = new Product();

                String productString = handler.makeServiceCall(mProductBaseURL + mProductIds[i] + mDeviceIdURL);
                if(Debug.DEBUG){
                    Log.d(Constant.APP_TAG, "Product #" + i + " " + productString);
                }

                if(productString != null){
                    try{
                        JSONObject proJson = new JSONObject(productString);
                        if(proJson.has(Constant.TAG_P_ID)){
                            newProduct.setProductId(proJson.getString(Constant.TAG_P_ID));
                        }
                        if(proJson.has(Constant.TAG_P_TITLE)){
                            newProduct.setTitle(proJson.getString(Constant.TAG_P_TITLE));
                        }
                        if(proJson.has(Constant.TAG_P_DESC)){
                            newProduct.setDescription(proJson.getString(Constant.TAG_P_DESC));
                        }
                        if(proJson.has(Constant.TAG_P_PRICE)){
                            newProduct.setPrice(Integer.parseInt(proJson.getString(Constant.TAG_P_PRICE)));
                        }
                        if(proJson.has(Constant.TAG_P_IMAGE)){
                            newProduct.setImgUrl(proJson.getString(Constant.TAG_P_IMAGE));
                        }
                        if(proJson.has(Constant.TAG_P_COLOR)){
                            newProduct.setColor(proJson.getString(Constant.TAG_P_COLOR));
                        }
                        if(proJson.has(Constant.TAG_P_URL)){
                            newProduct.setUrl(proJson.getString(Constant.TAG_P_URL));
                        }
                        mProducts.add(newProduct);
                    }catch (JSONException ex){
                        Log.e(Constant.APP_TAG, "JSONException " + ex.getMessage());
                    }
                }
            }
            return null;
        }
    }

    private void showProductImage(){
        Product product = mProducts.get(mCurrentProductIndex);
        LoadingImageTask imageLoading = new LoadingImageTask(product);
        imageLoading.execute();
    }

    // Loading the image from the url and set it in the ImageView.
    private class LoadingImageTask extends AsyncTask<String, Void, Bitmap>{
        private Product product;

        public LoadingImageTask(Product product){
            this.product = product;
        }

        protected Bitmap doInBackground(String... urls){
          //  String urlOfImage = urls[0];
            Bitmap bitmap = null;

            try{
                InputStream is = new URL(product.getImgUrl()).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            }catch (Exception ex){
                Log.e(Constant.APP_TAG, "Error in LoadingImageTask " + ex.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result){
            mProductName.setText(product.getTitle());
            mProductInfo.setText(product.getDescription());
            mProductImg.setImageBitmap(result);
            mProductColor.setBackgroundColor(Color.parseColor(product.getColor()));
        }
    }
}
