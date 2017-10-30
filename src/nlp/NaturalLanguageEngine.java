package nlp;

import com.ejapps.masquerade.analysis.ITrigger;
import com.ejapps.masquerade.engine.AnalysisEngine;
import exceptions.PurposeSizeException;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;
import relations.Linkage;
import relations.LinkageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a on 2017-04-19.
 */
public class NaturalLanguageEngine {

    private static final int MEMORY_SIZE = 100;

    public static final int MODE_DEBUG = 100;
    public static final int MODE_REAL = 200;
    private static List<Linkage> memory;

    private boolean finalPurpose = false;
    private int mode = MODE_REAL;
    private Komoran komoran;
    private LinkageFactory linkageFactory;

    private String modelPath = "./models";
    private String dicPath = "./user_data/NIADic.user";

    private static NaturalLanguageEngine instance; // Singleton instance
    private static AnalysisEngine analysisEngine;

    private NaturalLanguageEngine(){
        try {
            System.out.println(" ::: NLP Engine has been started successfully ::: \n");
        }catch(Exception e){
            System.out.println(" ::: An error occured while Starting NLP Engine ::: \n");
        }

    }

    {
        analysisEngine = AnalysisEngine.getInstance();
        komoran = new Komoran(modelPath);
        linkageFactory = new LinkageFactory();
        memory = new ArrayList<>();
    }

    public static NaturalLanguageEngine getInstance(){
        if(instance == null) instance = new NaturalLanguageEngine();
        return instance;
    }

    public static NaturalLanguageEngine setDebugMode(boolean mode){
        if(mode) instance.mode = MODE_DEBUG;
        else instance.mode = MODE_REAL;
        return instance;
    }

    private void memorize(Linkage linkage){
        if(memory.size() > MEMORY_SIZE) {
            memory.clear();
        }
        memory.add(linkage);
    }

    public String normalizeString(String msg){
        return analysisEngine.normalize(msg);
    }

    public List<String> analyzeInstantly(String text, boolean enableSimilarityMode){
        linkageFactory.setSimilarityMode(enableSimilarityMode);
        List<List<Pair<String, String>>> result = komoran.analyze(text);
        linkageFactory.setMorphemes(result, text);
        if(mode == MODE_DEBUG) {
            for (List<Pair<String, String>> eojeolResult : result) {
                for (Pair<String, String> wordMorph : eojeolResult) {
                    System.out.print(wordMorph + "   ");
                }
                System.out.println();
            }
        }
        Linkage linkage = linkageFactory.link();

        memorize(linkage);

        // TODO 임시 설계 - 응답 클래스가 별도로 생성될 경우, 변경이 필수적임
        Pair<List<String>, List<String>> resPair = linkage.interaction(new ITrigger() {
            @Override
            public boolean run(HashMap<String, Object> extra,  List<String> ref) {

                return false;
            }
        }, this);

        List<String> merged = new ArrayList<>();
        if(resPair != null && resPair.getFirst() != null && resPair.getSecond() != null) {
            merged.addAll(resPair.getFirst());
            merged.addAll(resPair.getSecond());
        }

        return merged;
    }

    public boolean isFinalPurpose() {
        return finalPurpose;
    }

    public void setFinalPurpose(boolean finalPurpose) {
        this.finalPurpose = finalPurpose;
    }
}
