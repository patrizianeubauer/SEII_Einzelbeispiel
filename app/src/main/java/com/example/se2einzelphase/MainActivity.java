package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button btnAbschicken;
    private TextView tvResponse;
    private Thread thread;
    private EditText etnInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbschicken = findViewById(R.id.btnAbschicken);
        tvResponse = findViewById(R.id.tvServerResponse);
        etnInput = findViewById(R.id.etnInput);

        btnAbschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Network stuff = new Network();
                thread = new Thread(stuff);
                thread.start();
            }
        });
    }

    class Network  implements Runnable {
        public PrintWriter out;
        public BufferedReader in;

        public void run() {
            int p = 53212;
            String h = "se2-isys.aau.at";
            try {
                InetAddress serverAddr = InetAddress.getByName(h);
                Socket socket = new Socket(serverAddr, p);

                // connection -> setup the read (in) and write (out)
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                try {
                    // send message to server
                    out.println(etnInput.getText());

                    // read message from server
                    String response = in.readLine();
                    tvResponse.setText(response);
                } catch (Exception e) {
                    System.out.println("Error");
                } finally {
                    in.close();
                    out.close();
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }
}