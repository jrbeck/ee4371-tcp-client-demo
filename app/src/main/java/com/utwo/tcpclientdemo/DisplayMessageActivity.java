package com.utwo.tcpclientdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // set the message sent to server TextView
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.message_text);
        textView.setText(message);

        // launch the AsyncTask to send the message to the TCP server
        new AsyncTcpClient().execute(message);
    }

    private class AsyncTcpClient extends AsyncTask<String, Void, String> {
        private static final String HOST_ADDRESS = "10.0.2.2";
        private static final int PORT = 6789;

        protected String doInBackground(String... messages) {
            String responseFromServer = "";

            try {
                Socket socket = new Socket(HOST_ADDRESS, PORT);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToServer.writeBytes(messages[0] + '\n');
                responseFromServer = inFromServer.readLine();
                socket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return responseFromServer;
        }

        protected void onPostExecute(String result) {
            // when the response has been received from the server, display it to the user
            TextView textView = (TextView)findViewById(R.id.response_text);
            textView.setText(result);
        }
    }
}