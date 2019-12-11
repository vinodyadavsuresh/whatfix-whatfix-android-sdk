package sdk.whatfix.com.picker;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import sdk.whatfix.com.network.NetworkConnection;

public class Picker {
    JSONArray jsonArray;
    JSONObject emitingObject;
    View child;
    Activity activity1;
    ViewGroup rootView;
    Map< String,String> pickerDetails=new HashMap<>();
    public int count=0;
    String imageString;

    public void gettingComponentId(Activity activity) {
        activity1 = activity;
        rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        attachWhatfixTouchEvent(rootView);

    }


    public void attachWhatfixTouchEvent(final View v1) {

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (NetworkConnection.pickerState.equals("picker_start")){
                    if (count==1){
                        imageString=Screenshot.takescreenshot(rootView);
                        gettingViewDetails(v);
                    }
                }else {
                    count=0;

                }

            }
        });

        if (v1 instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v1;
            for (int i = 0; i < vg.getChildCount(); i++) {
                child = vg.getChildAt(i);
                attachWhatfixTouchEvent(child);
            }
        }
    }

    public void gettingViewDetails(View v ){
        if (v.getId()==rootView.getId()){

        }else {


            pickerDetails.put("id",activity1.getResources().getResourceEntryName(v.getId()));
            pickerDetails.put("type",activity1.getResources().getResourceTypeName(v.getId()));
            pickerDetails.put("name",activity1.getResources().getResourceName(v.getId()));
            pickerDetails.put("height", String.valueOf(v.getHeight()));
            pickerDetails.put("width", String.valueOf(v.getWidth()));
            jsonArray=new JSONArray();
            emitingObject=new JSONObject();
            getmargins(v);

            for (String key:pickerDetails.keySet()){
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("attribute",key);
                    jsonObject.put("value",pickerDetails.get(key));
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                emitingObject.put("marks",jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }



           NetworkConnection.mSocket.emit("element_picked",emitingObject.toString());



        }

    }

    public void getmargins(View view){


        View rootLayout = view.getRootView().findViewById(android.R.id.content);

        int[] viewLocation = new int[2];
        view.getLocationInWindow(viewLocation);

        int[] rootLocation = new int[2];
        rootLayout.getLocationInWindow(rootLocation);

        int relativeLeft = viewLocation[0] - rootLocation[0];
        int relativeTop  = viewLocation[1] - rootLocation[1];



        try {
          emitingObject.put("image",imageString);
           emitingObject.put("left",relativeLeft);
           emitingObject.put("top",relativeTop);
           emitingObject.put("width",view.getWidth());
           emitingObject.put("height",view.getHeight());
           emitingObject.put("image_height",rootView.getHeight());
           emitingObject.put("image_width",rootView.getWidth());

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}