package services;

import databases.mybatis.mapper.CommMapper;
import nlp.NaturalLanguageEngine;
import org.apache.ibatis.session.SqlSession;
import relations.ActionFraction;
import relations.KnowledgeFraction;
import relations.TypedPair;
import server.comm.DataMap;
import utils.Log;
import utils.NumberUnit;
import utils.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CommonSVC extends BaseService {

    public List<KnowledgeFraction> getMetaphors(){
        List<KnowledgeFraction> retVal = new Vector<>();

        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            List<DataMap> list = commMapper.getMetaphors();

            for(DataMap rs : list){
                KnowledgeFraction know = new KnowledgeFraction();
                know.setWord(rs.getString("includer"));
                know.setRefWord(rs.getString("includee"));
                know.setFrequency(rs.getInt("frequency"));
                retVal.add(know);
            }
        }

        return retVal;
    }

    public String getDirectResponse(String msg){
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            return commMapper.getDirectResponse(msg);
        }catch(Exception e){
            return "";
        }
    }

    public List<NumberUnit> getNumberDictionary(){
        List<NumberUnit> retVal = new ArrayList<>();

        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            List<DataMap> list = commMapper.getNumberDictionary();

            for(DataMap rs : list){
                NumberUnit time = new NumberUnit(rs.getString("desc"), rs.getString("tag"), rs.getInt("value"));
                retVal.add(time);
            }
        }
        return retVal;
    }

    public List<TimeUnit> getTimeDictionary(){
        List<TimeUnit> retVal = new ArrayList<>();
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            List<DataMap> list = commMapper.getTimeDictionary();

            for(DataMap rs : list){
                boolean sa = false;
                if(rs.getInt("standalone") == 1) sa = true;
                TimeUnit time = new TimeUnit(rs.getString("desc"), rs.getString("meaning"), rs.getInt("diff"), sa);
                retVal.add(time);
            }
        }
        return retVal;
    }

    public List<KnowledgeFraction> getStaticBases(){
        List<KnowledgeFraction> retVal = new ArrayList<>();
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            List<DataMap> list = commMapper.getStaticBases();

            for(DataMap rs : list){
                KnowledgeFraction know = new KnowledgeFraction();
                know.setWord(rs.getString("serialWord"));
                know.setRefWord(rs.getString("intention"));
                know.setFrequency(rs.getInt("frequency"));
                retVal.add(know);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return retVal;
    }

    public List<ActionFraction> getActions(){
        List<ActionFraction> retVal = new ArrayList<>();
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            List<DataMap> list = commMapper.getActions();

            for(DataMap rs : list){
                String original = rs.getString("original");
                String objectSerial = rs.getString("objectSerial");
                String verbSerial = rs.getString("verbSerial");
                String intentionCode = rs.getString("intentionCode");
                String desc = rs.getString("desc");
                int frequency = rs.getInt("frequency");
                String keyValue = rs.getString("keyValue");

                ActionFraction know = new ActionFraction(original, objectSerial, verbSerial, intentionCode, desc, frequency, keyValue);
                retVal.add(know);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return retVal;
    }

    public List<KnowledgeFraction> getKnowledges(){
        List<KnowledgeFraction> retVal = new ArrayList<>();

        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            List<DataMap> list = commMapper.getKnowledges();

            for(DataMap rs : list){
                KnowledgeFraction know = new KnowledgeFraction();
                know.setWord(rs.getString("word"));
                know.setRefWord(rs.getString("refWord"));
                know.setFrequency(rs.getInt("frequency"));
                retVal.add(know);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return retVal;
    }

    public int getFrequentBetween(TypedPair word, TypedPair ref){
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            Integer rowCount = commMapper.getFrequentBetween(word.getFirst(), ref.getFirst(), word.getSecond(), ref.getSecond());
            if(rowCount == null) return 0;
            return rowCount;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getFrequentOfStatic(String sWord, String sTag, String intention){
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            Integer rowCount = commMapper.getFrequentOfStatic(sWord, sTag, intention);
            if(rowCount == null) return 0;
            return rowCount;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getFrequentBetweenForMeta(TypedPair word, TypedPair ref){
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);
            Integer rowCount = commMapper.getFrequentBetweenForMeta(word.getFirst(), ref.getFirst(), word.getSecond(), ref.getSecond());
            if(rowCount == null) return 0;
            return rowCount;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean saveStaticSentence(String sWord, String sTag, String intention, String resp){
        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            resp = resp.equals("") ? "NULL" : "\'" + resp + "\'";

            if(getFrequentOfStatic(sWord, sTag, intention) == 0) {
                commMapper.saveStaticSentence(sWord, sTag, intention, resp);
            } else {
                commMapper.updateStaticSentence(sWord, sTag, intention);
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveMetaLink(TypedPair word, TypedPair ref){
        TypedPair param1, param2;
        param1 = word;
        param2 = ref;

        if(getFrequentBetweenForMeta(param2, param1) > 0 || param1.equals(param2)){
            Log.e("Insert Operation violating recurrence policies called - Ignoring");
            return false;
        }

        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            if(getFrequentBetweenForMeta(param1, param2) == 0) {
                commMapper.saveMetaLink(param1.getFirst(), param2.getFirst(), param1.getSecond(), param2.getSecond());
            }else{
                commMapper.updateMetaLink(param1.getFirst(), param2.getFirst(), param1.getSecond(), param2.getSecond());
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveKnowledgeLink(TypedPair word, TypedPair ref, boolean reverse){
        TypedPair param1, param2;

        param1 = word;
        param2 = ref;

        try (SqlSession sqlSession = super.getSession()){
            CommMapper commMapper = sqlSession.getMapper(CommMapper.class);

            if(getFrequentBetween(param1, param2) == 0) {
                commMapper.saveKnowledgeLink(param1.getFirst(), param2.getFirst(), param1.getSecond(), param2.getSecond(), (reverse? 1 : 0));
            } else {
                commMapper.updateKnowledgeLink(param1.getFirst(), param2.getFirst(), param1.getSecond(), param2.getSecond());
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public DataMap instantResponse(String msg){
        NaturalLanguageEngine nlpEngine = NaturalLanguageEngine.getInstance().setDebugMode(true);

        DataMap map = new DataMap();
        map.put("normalized", nlpEngine.normalizeString(msg));
        map.put("analysis", nlpEngine.analyzeInstantly(msg, true));
        map.put("response", "Under Developing");

        return map;
    }

}
