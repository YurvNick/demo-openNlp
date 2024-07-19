package com.example;


import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String modelPath = "en-sent.bin";
        String filePath = "file_log.txt";

        try {
            InputStream modelIn = new FileInputStream(modelPath);
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

            List<String> sentences = readAndExtractSentences(filePath, sentenceDetector);

            for (String sentence : sentences) {
                System.out.println(sentence);
                System.out.println("new line");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readAndExtractSentences(String filePath, SentenceDetectorME sentenceDetector) throws IOException {
        List<String> sentences = new ArrayList<>();
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
        StringBuilder fragment = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            fragment.append(line);
            if (fragment.length() > 2) {
                extractSentencesFromFragment(fragment.toString(), sentenceDetector, sentences);
                fragment.setLength(0);
            }
        }
        if (!fragment.isEmpty()) {
            extractSentencesFromFragment(fragment.toString(), sentenceDetector, sentences);
        }

        reader.close();
        return sentences;
    }

    private static void extractSentencesFromFragment(String fragment, SentenceDetectorME sentenceDetector, List<String> sentences) {
        String[] detectedSentences = sentenceDetector.sentDetect(fragment);
        Collections.addAll(sentences, detectedSentences);
    }
}