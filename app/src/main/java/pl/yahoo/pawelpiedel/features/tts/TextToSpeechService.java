package pl.yahoo.pawelpiedel.features.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;

import javax.inject.Inject;

import pl.yahoo.pawelpiedel.data.place.Place;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;
import timber.log.Timber;

public class TextToSpeechService implements TextToSpeech.OnInitListener {
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
        String sentence;
        if (distance < 3 && place.getPlaceType().equals("DANGER")) {
            sentence = "Uważaj! Jesteś w odległości około " + distance + " metrów od miejsca " + place.getName();
        } else {
            sentence = "Zbliżasz się do " + place.getName() + ". Odległość to około " + distance + "metrów.";
        }
        Timber.d("Trying to speak " + sentence);

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");

        textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, map);
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }
}
