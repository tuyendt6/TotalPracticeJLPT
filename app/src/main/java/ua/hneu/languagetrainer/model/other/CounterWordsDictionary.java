package ua.hneu.languagetrainer.model.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ua.hneu.languagetrainer.App;
import ua.hneu.languagetrainer.model.DictionaryAbstr;
import ua.hneu.languagetrainer.model.EntryAbstr;
import ua.hneu.languagetrainer.model.grammar.GrammarDictionary;
import ua.hneu.languagetrainer.model.grammar.GrammarRule;
import ua.hneu.languagetrainer.service.CounterWordsService;

import android.util.Log;

public class CounterWordsDictionary extends DictionaryAbstr {
    private ArrayList<CounterWord> entries = new ArrayList<CounterWord>();

    public void add(CounterWord counterWord) {
        entries.add(counterWord);
    }

    public int size() {
        return entries.size();
    }

    public CounterWord get(int i) {
        return entries.get(i);
    }

    public void remove(CounterWord g) {
        entries.remove(g);
    }

    public Set<CounterWord> getRandomEntries(int size) {
        Set<CounterWord> random = new HashSet<CounterWord>();
        if (App.userInfo.getNumberOfCounterWordsInLevel() <= App.userInfo.getLearnedCounterWords())
            return random;
        Random rn = new Random();

        while (random.size() <= size) {
            int i = rn.nextInt(entries.size());
            if (entries.get(i).getLearnedPercentage() < 1) {
                random.add(entries.get(i));
            }
        }
        return random;
    }

    public ArrayList<CounterWord> getEntries() {
        return entries;
    }

    public ArrayList<String> getAllTranslations() {
        ArrayList<String> translation = new ArrayList<String>();
        for (CounterWord e : entries) {
            translation.add(e.translationsToString() + "");
        }
        return translation;
    }

    public CounterWord fetchRandom() {
        int a = new Random().nextInt(entries.size() - 1);
        return entries.get(a);
    }

    public ArrayList<String> getAllWords() {
        ArrayList<String> words = new ArrayList<String>();
        for (CounterWord e : entries) {
            words.add(e.getWord());
        }
        return words;
    }

    public ArrayList<String> getAllTranscriptions() {
        ArrayList<String> transcription = new ArrayList<String>();
        for (CounterWord e : entries) {
            transcription.add(e.getTranscription());
        }
        return transcription;
    }

    public void addEntriesToDictionaryAndGet(int size) {
        Random rn = new Random();

        while (this.size() < size) {
            int i = rn.nextInt(App.allCounterWordsDictionary.size());
            CounterWord cw = App.allCounterWordsDictionary.get(i);
            // if the word is not learned
            if (cw.getLearnedPercentage() < 1) {
                this.entries.add(cw);
            }
        }
    }

    public CounterWordsDictionary search(String query) {
        CounterWordsDictionary result = new CounterWordsDictionary();
        for (CounterWord cw : entries) {
            if ((cw.getHiragana().toLowerCase())
                    .startsWith(query.toLowerCase())
                    || (cw.getRomaji().toLowerCase()).startsWith(query
                    .toLowerCase())
                    || (cw.getTranscription().toLowerCase()).startsWith(query
                    .toLowerCase()))
                result.add(cw);
            else {
                String[] words = cw.translationsToString().split("\\b");
                for (String w : words) {
                    if ((w.trim().toLowerCase())
                            .startsWith(query.toLowerCase())) {
                        result.add(cw);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<String> allToString() {
        return getAllWords();
    }
}
