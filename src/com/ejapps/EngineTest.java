package com.ejapps;

import com.ejapps.masquerade.engine.AnalysisEngine;
import nlp.NaturalLanguageEngine;
import relations.LinkageFactory;
import utils.Log;

import java.util.List;
import java.util.Scanner;

public class EngineTest {

    public static void main(String... args) {

        Scanner scanner = new Scanner(System.in);
        NaturalLanguageEngine nlpEngine = NaturalLanguageEngine.getInstance().setDebugMode(true);
        while(true){
            System.out.print("USER : ");
            final String text = scanner.nextLine();
            String command = AnalysisEngine.getInstance().normalize(text);
            List<String> list = nlpEngine.analyzeInstantly(command, true);
            for(String res : list){
                System.out.println(LinkageFactory.MY_NAME + " : " + res);
            }
        }
    }

}
