package sdk.whatfix.com.network;


import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import sdk.whatfix.com.BuildConfig;
import sdk.whatfix.com.picker.Picker;

public class NetworkConnection {
    Activity activity;
    public static Socket mSocket;
    public static String pickerState;
    private static String clientId;


    public NetworkConnection(Activity activity) {
        this.activity = activity;
    }

    public void init() {
        System.out.println("iniytilize.....");
        ApplicationInfo app = null;
        try {
            app = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;

            clientId = bundle.getString("com.elenasys.a");



        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = app.metaData;

        try {

            mSocket = IO.socket(BuildConfig.SOCKET_URL);
            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport)args[0];

                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            @SuppressWarnings("unchecked")
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            //headers.put("x-clientid", Arrays.asList(identifyModel.getClientId()));
                            headers.put("x-clientid", Arrays.asList(clientId));
                        }
                    });
                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void webSocketconnect(){
        mSocket.connect();
        mSocket.on("command", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        System.out.println("Getting Msg"+data);
                        pickerState=data;
                        //Picker
                        Picker picker=new Picker();
                        picker.gettingComponentId(activity);




                    }
                });
            }
        });
    }



}
