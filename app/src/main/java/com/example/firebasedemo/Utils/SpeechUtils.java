package com.example.firebasedemo.Utils;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.firebasedemo.Interface.CitySpeechRecognitionListener;

import java.util.ArrayList;

public class SpeechUtils {
    private Fragment fragment;
    private CitySpeechRecognitionListener citySpeechRecognitionListener;
    private ActivityResultLauncher<Intent> speechRecognitionLauncher;

    public SpeechUtils() {
        // Empty constructor
    }

    public void setFragmentAndRegisterLauncher(Fragment fragment, CitySpeechRecognitionListener listener) {
        this.fragment = fragment;
        this.citySpeechRecognitionListener = listener;

        speechRecognitionLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                ArrayList<String> placesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                if (placesText != null && placesText.size() > 0) {
                                    String results = placesText.get(0);
                                    citySpeechRecognitionListener.onSpeechRecognitionResult(results);
                                }
                            }
                        }
                    }
                }
        );
    }

    public void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        speechRecognitionLauncher.launch(intent);
    }
}
