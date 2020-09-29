package com.example.s6proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Button buttonMain;
    private EditText username;
    private EditText password;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMain = findViewById(R.id.buttonMain);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        initCliente();

        buttonMain.setOnClickListener(
                view -> {
                    sendMessage("Click desde cliente");
                }
        );
    }

    private void initCliente(){
        new Thread(
                () ->{
                    try {
                        //2. mandar solicitud de conexión
                        socket = new Socket("192.168.0.4",5000);

                        // 3. conexión establecida
                        System.out.println("Cliente conectado");
                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(true) {
                            System.out.println("Esperando...");
                            //No se define hasta que el cliente manda un elemento
                            String line = reader.readLine();
                            System.out.println("Recibido");
                            System.out.println("Recibido" + line + '\n');
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private void sendMessage(String msg) {
        new Thread(
                () -> {
                    try {
                        writer.write(msg+"\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}