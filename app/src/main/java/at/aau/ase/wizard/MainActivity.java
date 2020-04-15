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
        new ServerRequestTask().execute("Some request");    }

    private void startClient() {
        // todo
    }

    class ServerRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            client.sendMessage(new TextMessage(strings[0]));
            return "Request send";
        }

        @Override
        protected void onPostExecute(String taskRes) {
            tv_res.setText(taskRes);
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
