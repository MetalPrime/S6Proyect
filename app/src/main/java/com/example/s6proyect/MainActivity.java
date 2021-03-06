package com.example.s6proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Button buttonMain;
    private EditText username;
    private EditText password;
    private boolean onActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMain = findViewById(R.id.buttonMain);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        initCliente();

        onActive = true;

        buttonMain.setOnClickListener(
                view -> {
                    Gson gson = new Gson();
                    String name = username.getText().toString();
                    String passport = password.getText().toString();
                    String id = UUID.randomUUID().toString();
                    User userData = new User(name,passport,id);

                    String json = gson.toJson(userData);
                    sendMessage(json);
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

                        System.out.println("Esperando...");


                        while(onActive) {
                            //No se define hasta que el cliente manda un elemento
                            String line = reader.readLine();
                            System.out.println("Conectado");
                            Log.w("Answer","Recibido" + line + '\n');
                            Gson gsonMessage = new Gson();
                            Message recibeMsg = gsonMessage.fromJson(line,Message.class);



                            if(line!=null){

                                runOnUiThread(
                                        () ->{
                                            if(recibeMsg!=null){
                                                Log.e("Estado",recibeMsg.getMessage());
                                                if(recibeMsg.getMessage().equals("Bienvenido")) {
                                                    Intent i = new Intent(this, ResultActivity.class);
                                                    startActivity(i);
                                                    Log.e("Funciona", "inicioo");
                                                    byte [] mensajeBytes = recibeMsg.getMessage().getBytes();
                                                    try {
                                                        String utf = new String(mensajeBytes,"UTF-8");
                                                        Toast.makeText(this, utf, Toast.LENGTH_SHORT).show();
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }

                                                    onActive = false;

                                                } else {
                                                    byte [] mensajeBytes = recibeMsg.getMessage().getBytes();
                                                    try {
                                                        String utf = new String(mensajeBytes,"UTF-8");
                                                        Toast.makeText(this, utf, Toast.LENGTH_SHORT).show();
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                        }
                                );

                            }

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