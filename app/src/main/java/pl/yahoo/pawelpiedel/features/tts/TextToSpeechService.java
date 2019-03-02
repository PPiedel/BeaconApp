package pl.yahoo.pawelpiedel.features.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.inject.Inject;

import pl.yahoo.pawelpiedel.data.place.Place;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;
import timber.log.Timber;

public class TextToSpeechService implements TextToSpeech.OnInitListener {
    private static final int BORDER_DISTANCE = 3;
    private static final String MESSAGE_ID = "MessageId";
    private final Context context;
    private final TextToSpeech textToSpeech;

    @Inject
    public TextToSpeechService(@ApplicationContext Context context) {
        this.context = context;
        this.textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {

    }

    public void speak(Place place, double distance) {
        String sentence = distance < BORDER_DISTANCE && place.getPlaceType().equals("DANGER") ?
                dangerMessage(place, distance) :
                comonMessage(place, distance);
        speak(sentence);
    }

    @NotNull
    private String dangerMessage(Place place, double distance) {
        return "Uważaj! Jesteś w odległości około " + distance + " metrów od miejsca " + place.getName();
    }

    @NotNull
    private String comonMessage(Place place, double distance) {
        return "Zbliżasz się do " + place.getName() + ". Odległość to około " + distance + "metrów.";
    }

    private void speak(String sentence) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, MESSAGE_ID);
        textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, map);
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }
}
