package at.aau.ase.wizard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.NetworkClientKryo;

public class MainActivity extends AppCompatActivity {
    Button btnServer, btnClient;
    String hostname = "se2-demo.aau.at";
    NetworkClientKryo client = null;
    TextView tv_res = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnServer = findViewById(R.id.buttonServer);
        btnServer.setOnClickListener(v -> startServer());
        btnClient = findViewById(R.id.buttonClient);
        btnClient.setOnClickListener(v -> startClient());

        tv_res = findViewById(R.id.textView_res);

        client = new NetworkClientKryo();
        client.registerClass(TextMessage.class);
        client.registerCallback(basemessage -> {
            if (basemessage instanceof TextMessage) {
                Log.i("SERVER RESPONSE:", basemessage.toString());
            } else {
                Log.i("ERROR:", "Not a textmessage: "+basemessage.toString());
            }
        });
        new NetworkThread().start();
    }

    private void startServer() {

        MessageThread t = new MessageThread();

        t.start();

    }

    private void startClient() {
        // todo
    }
    class MessageThread extends Thread {
        public void run() {
            String msg = "Some String";
            client.sendMessage(new TextMessage(msg));
            Log.i("REQUEST SEND", msg);

        }


    }

    class NetworkThread extends Thread {
        public void run () {
            try {
                client.connect(hostname);
                Log.i("SERVER CONNECTION:", "Connection to server "+hostname+" successful");
            } catch (IOException e) {
                Log.e("SERVER CONNECTION:", "Could not connect to server "+hostname);
                e.printStackTrace();
            }
        }
    }
}
