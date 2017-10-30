package com.ejapps.masquerade.engine;

import com.ejapps.masquerade.analysis.model.Feature;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import java.util.List;

public class AnalysisEngine extends Engine{

    private static AnalysisEngine instance;

    private Komoran komoran;
    private String modelPath = "./models";
    private String dicPath = "./user_data/NIADic.user";

    public static AnalysisEngine getInstance(){
        if(instance == null) instance = new AnalysisEngine();
        return instance;
    }

    private AnalysisEngine(){
        if(komoran == null) {
            komoran = new Komoran(modelPath);
            komoran.setUserDic(dicPath);
        }
    }

    @Override
    public Feature analyze(String text){
        return null;
    }

    @Override
    public String normalize(String text){
        final String normalized = OpenKoreanTextProcessorJava.normalize(text).toString();
        final String cleaned = super.clearParticle(normalized);

        return cleaned;
    }

    @Override
    public Feature normalizeFeature(String text){
        final String normalized = OpenKoreanTextProcessorJava.normalize(text).toString();
        final String cleaned = super.clearParticle(normalized);

        List<List<Pair<String, String>>> result = komoran.analyze(cleaned);
        for (List<Pair<String, String>> eojeolResult : result) {
            for (Pair<String, String> wordMorph : eojeolResult) {
                System.out.print(wordMorph + " ");
            }
            System.out.println();
        }

        Feature feature = new Feature(text);
        feature.setNormalized(normalized);

        return feature;
    }

}
