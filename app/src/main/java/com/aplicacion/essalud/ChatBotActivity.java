package com.aplicacion.essalud;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.aplicacion.essalud.methods.Methods.showSnackBar;

public class ChatBotActivity extends AppCompatActivity implements AIListener {

    private TextInputLayout tilClientMessage;
    private TextInputLayout tilChatBotMessage;
    private FloatingActionButton btnAudioTextSend;
    private Toolbar mToolbar;
    // DialogFLow
    private TextToSpeech textToSpeech;
    private AIService aiService;
    private AIDataService aiDataService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        // Declaración de controles
        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        tilClientMessage = (TextInputLayout) findViewById(R.id.tilClientMessage);
        tilChatBotMessage = (TextInputLayout) findViewById(R.id.tilChatBotMessage);
        btnAudioTextSend = (FloatingActionButton) findViewById(R.id.btnAudioTextSend);
        // Configuracion de ToolBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ChatBot");
        // Parametros iniciales de DialogFlow
        final AIConfiguration config = new AIConfiguration("1b2544d053b847e2adac824b37a67de5",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiDataService = new AIDataService(config);
        // Mensaje de bienvenida
        sendMessage("Hola");
        // Parametros iniciales del servicio local de Escucha y texto
        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle bundle) {
                // Evento que sucede cuando el usuario deja de hablar y suelta el floacting button
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null)
                    // Añade mensaje al contenedor
                    addMessageToContainer(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        btnAudioTextSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Captura de texto ingresado por el usuario
                String clientMessage = Objects.requireNonNull(tilClientMessage.getEditText()).getText().toString();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Evento que sucede cuando se presiona el
                        // Si no hay texto escrito en el campo cliente
                        if (TextUtils.isEmpty(clientMessage)) {
                            // Animacion de zoom
                            btnAudioTextSend.startAnimation(AnimationUtils.loadAnimation(ChatBotActivity.this, R.anim.zoomin));
                            // DialogFlow empieza a escuchar
                            aiService.startListening();
                            // Servicio de traduccion oral a escrita empieza a escuchar
                            speechRecognizer.startListening(speechRecognizerIntent);
                            // Limpieza del campo de entrada de cliente
                            Objects.requireNonNull(tilClientMessage.getEditText()).setText("");
                            // Mostrar estado de escucha de los servicios
                            Objects.requireNonNull(tilClientMessage.getEditText()).setHint("Escuchando...");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Evento que sucede cuando se deja de presionar el boton
                        // Si hay texto en el campo de texto del cliente
                        if (!TextUtils.isEmpty(clientMessage)) {
                            // Añade mensaje al contenedor
                            addMessageToContainer(clientMessage);
                            // Envio de mensaje del usuario a DialogFLow
                            sendMessage(clientMessage);
                            // Limpieza del campo de entrada de cliente
                            Objects.requireNonNull(tilClientMessage.getEditText()).setText("");
                            // Cambio de icono del boton: enviar por grabar
                            btnAudioTextSend.setImageResource(R.drawable.ic_mic_white_24dp);
                        } else {
                            // Animacion de Zoom reversa
                            btnAudioTextSend.startAnimation(AnimationUtils.loadAnimation(ChatBotActivity.this, R.anim.zoomout));
                            // Servicio local de escucha detenido
                            speechRecognizer.stopListening();
                            // Campo de entrada vacio y disponible
                            Objects.requireNonNull(tilClientMessage.getEditText()).setHint("Escribe un mensaje");
                        }
                        break;
                }
                return false;
            }
        });
        Objects.requireNonNull(tilClientMessage.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cambio de icono del boton: grabar por enviar
                btnAudioTextSend.setImageResource(R.drawable.ic_send_white_24dp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(new Locale("spa", "MEX"));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void addMessageToContainer(String message) {
        // Captura de mensajes en el contenedor
        String containerMessages = Objects.requireNonNull(tilChatBotMessage.getEditText()).getText().toString();
        // Nuevo Mensaje
        String newContainerMessage = ((TextUtils.isEmpty(containerMessages)) ? "" : "\n> ") + message;
        // Adicion del mensaje al contenedor
        Objects.requireNonNull(tilChatBotMessage.getEditText()).setText(newContainerMessage);
    }

    @SuppressLint("StaticFieldLeak")
    private void sendMessage(String message) {
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(message);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            private AIError aiError;

            @SuppressLint("WrongThread")
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    return aiDataService.request(aiRequest);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    onResult(aiResponse);
                } else {
                    onError(aiError);
                }
            }
        }.execute(aiRequest);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult(); // Respuesta de DialogFlow
        // Mensaje de DialogFlow
        String chatBotMessage = result.getFulfillment().getSpeech().toString();
        // Añade mensaje al contenedor
        addMessageToContainer(chatBotMessage);
        // Llamada al servicio local de lectura por voz
        textToSpeech.speak(chatBotMessage, TextToSpeech.QUEUE_FLUSH, null, null);
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
        int id = item.getItemId();
        switch (id) {
            case R.id.itmAudio:
                showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Audio", Snackbar.LENGTH_LONG));
                break;
            case R.id.itmCalendar:
                showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Calendario", Snackbar.LENGTH_LONG));
                break;
            case R.id.itmOther:
                showSnackBar(Snackbar.make(findViewById(android.R.id.content), "Otro", Snackbar.LENGTH_LONG));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
