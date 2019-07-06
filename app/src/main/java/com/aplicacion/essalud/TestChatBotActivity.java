package com.aplicacion.essalud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aplicacion.essalud.models.Horario;
import com.aplicacion.essalud.models.ItemCita;
import com.aplicacion.essalud.models.database.LocalDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static com.aplicacion.essalud.methods.Methods.decrypt;
import static com.aplicacion.essalud.methods.Methods.encrypt;

public class TestChatBotActivity extends AppCompatActivity implements AIListener {

    /*
     * Si se deseara agregar respuestas del chatbot como images, cards, custom payload
     * solo se debe usar la clase Inflater y modificar la librería andorid-chat-ui
     * que actualmente se encuentra respondiendo unicamente mediante mensajes de texto
     * */

    private AIService aiService;
    private AIDataService aiDataService;
    private TextToSpeech textToSpeech;
    private ChatView chatView;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat_bot);
        chatView = (ChatView) findViewById(R.id.chatView);
        // Modificación de ToolBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Asistente EsSalud");

        firebaseDatabase = FirebaseDatabase.getInstance();
        // Parametros de DialogFlow
        final AIConfiguration config =
                new AIConfiguration("1b2544d053b847e2adac824b37a67de5",
                        AIConfiguration.SupportedLanguages.Spanish,
                        AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiDataService = new AIDataService(config);
        // Parametros iniciales de texto a voz
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(new Locale("spa", "MEX"));
            }
        });
        //Recepción de mensaje
        sendMessageToDialogFlow("Hola yo soy " + getIntent().getStringExtra("USERNAME"));
        //Envío de mensaje
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                chatView.addMessage(new ChatMessage(chatMessage.getMessage(), System.currentTimeMillis(), ChatMessage.Type.SENT));
                sendMessageToDialogFlow(chatMessage.getMessage());
                chatView.getInputEditText().getText().clear();
                return false;
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void sendMessageToDialogFlow(String message) {
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(message);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    return aiDataService.request(aiRequest);

                } catch (final AIServiceException ignored) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    onResult(aiResponse);
                }
            }
        }.execute(aiRequest);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onResult(AIResponse response) {
        String Fecha = "";
        String Hora = "";
        String Medico = "";
        final int MedicoId = new Random().nextInt((20 - 1) + 1) + 1;
        String Consultorio = "";
        String Servicio = "";
        String SalonLetter = ((new Random().nextInt((2 - 1) + 1) + 1) == 1) ? "A" : "B";
        int SalonNumber = new Random().nextInt((12 - 1) + 1) + 1;
        Consultorio = SalonLetter + "-" + SalonNumber;
        final Result result = response.getResult();
        final HashMap<String, JsonElement> params = result.getParameters();
        if (params != null && !params.isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString().replace('"', ' ').trim();
                if (entry.getValue().isJsonObject()) {
                    value = entry.getValue().getAsJsonObject().get("name").toString().replace('"', ' ').trim();
                }
                if (key.equals("date")) {
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = dateformat.parse(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateformat = new SimpleDateFormat("MMMM dd yyyy");
                    Fecha = dateformat.format(date);
                }
                if (key.equals("servicio")) {
                    Servicio = value;
                }
                if (key.equals("medico")) {
                    Medico = value;
                }
                if (key.equals("time")) {
                    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
                    Date time = null;
                    try {
                        time = timeformat.parse(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timeformat = new SimpleDateFormat("hh:mm:ss aa");
                    Hora = timeformat.format(time);
                }
            }
        }
        String chatBotMessage = response.getResult().getFulfillment().getSpeech().toString();
        chatView.addMessage(new ChatMessage(chatBotMessage, System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "EsSaludBOT"));
        textToSpeech.speak(chatBotMessage, TextToSpeech.QUEUE_FLUSH, null, null);
        if (!TextUtils.isEmpty(Fecha) && !TextUtils.isEmpty(Hora) && !TextUtils.isEmpty(Servicio) && !TextUtils.isEmpty(Medico) && !TextUtils.isEmpty(Consultorio)) {
            final String finalFecha = Fecha;
            final String finalHora = Hora;
            final String finalConsultorio = Consultorio;
            final String finalMedico = Medico;
            final String finalServicio = Servicio;
            firebaseDatabase.getReference("citas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int CitaId = (int) dataSnapshot.getChildrenCount() + 1;
                    SharedPreferences PREFERENCES = getSharedPreferences(LocalDB.PREFS_NAME, MODE_PRIVATE);
                    final String PacienteId = decrypt(PREFERENCES.getString(encrypt(LocalDB.PREF_PACIENTE_ID), null));

                    firebaseDatabase.getReference("citas").child(Integer.toString(CitaId))
                            .setValue(new HashMap<String, String>() {{
                                put("FECHA", finalFecha);
                                put("HORA", finalHora);
                                put("CONSULTORIO", finalConsultorio);
                                put("MEDICO", finalMedico);
                                put("MEDICO_ID", Integer.toString(MedicoId));
                                put("SERVICIO", finalServicio);
                                put("PACIENTE_ID", PacienteId);
                            }});
                    String me = "Día: " + finalFecha + "\n" +
                            "Hora: " + finalHora + "\n" +
                            "Servicio: " + finalServicio + "\n" +
                            "Médico: " + finalMedico + "\n" +
                            "Consultorio: " + finalConsultorio;
                    chatView.addMessage(new ChatMessage(me, System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "Datos de la Cita"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itmDash) {
            startActivity(new Intent(this, MenuActivity.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
