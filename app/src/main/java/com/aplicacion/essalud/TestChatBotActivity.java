package com.aplicacion.essalud;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;

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

import static com.aplicacion.essalud.methods.Methods.showSnackBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat_bot);
        chatView = (ChatView) findViewById(R.id.chatView);
        // Modificación de ToolBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Asistente ChatBot");
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
        sendMessageToDialogFlow("Hola");
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

    @Override
    public void onResult(AIResponse response) {
        // Respuesta de DialogFlow
        String chatBotMessage = response.getResult().getFulfillment().getSpeech().toString();
        // Añadido a la interfaz
        chatView.addMessage(new ChatMessage(chatBotMessage, System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "EsSaludBot"));
        // Lectura por altavoz
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
            case R.id.itmDash:
                startActivity(new Intent(this, MenuActivity.class));
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
