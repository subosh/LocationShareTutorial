package com.example.subosh.locationsharetutorial;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {
    private ResultReceiver resultReceiver;
    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            String errormessage="";
            resultReceiver=intent.getParcelableExtra(Constants.RECEIVER);
            Location location=intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location==null){
                return;
            }
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            List<Address> addresses=null;
            try{
                addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            }
            catch (Exception exception){
                errormessage=exception.getMessage();
            }
            if (addresses==null||addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT,errormessage);
            }
            else {
                Address address=addresses.get(0);
                ArrayList<String> addressfragment=new ArrayList<>();
                for (int i=0;i<=address.getMaxAddressLineIndex();i++){
                    addressfragment.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressfragment));
            }
        }

    }
    private void deliverResultToReceiver(int resultcode,String addressMessage){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage);
        resultReceiver.send(resultcode,bundle);

    }
}
