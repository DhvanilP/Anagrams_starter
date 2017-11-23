/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashSet<String> wordSetWithMin = new HashSet<>();
    private HashMap<String, ArrayList<String>> letterToWords = new HashMap<String, ArrayList<String>>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(sortLetters(word));
            if (letterToWords.get(sortLetters(word)) == null) {
                letterToWords.put(sortLetters(word), new ArrayList<String>());
            }
            letterToWords.get(sortLetters(word)).add(word);
        }
        for (String str : wordSet) {
            if (letterToWords.get(str).size() >= MIN_NUM_ANAGRAMS) {
                wordSetWithMin.add(str);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //here the word must be a valid dictionary word and base must not be
        //present in it as a substring
        if (word.contains(base))
            return false;
        if (wordSet.contains(sortLetters(word))) {
            if (letterToWords.get(sortLetters(word)).contains(word))
                return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        return letterToWords.get(sortLetters(targetWord));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < 26; i++) {
            String temp = word + ((char) ('a' + i));
            Log.d("Here", temp);
            String sortTemp = sortLetters(temp);
            if (wordSet.contains(sortTemp)) {
                for (int j = 0; j < letterToWords.get(sortTemp).size(); j++)

                    if (!letterToWords.get(sortTemp).get(j).contains(word)) {
                        result.add(letterToWords.get(sortTemp).get(j));
                    }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random random = new Random();
        int a = random.nextInt(wordSetWithMin.size());
        int i = 0;
        for (String str : wordSetWithMin) {
            if (i == a) {
                return str;
            }
            i++;
        }
        return letterToWords.get("badge").get(0);
    }

    private static String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
