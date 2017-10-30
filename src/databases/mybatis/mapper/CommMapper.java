package databases.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import server.comm.DataMap;

import java.util.List;

public interface CommMapper {

    DataMap getListOfKnowledge();

    String getDirectResponse(@Param("msg") String msg);

    List<DataMap> getMetaphors();

    List<DataMap> getNumberDictionary();

    List<DataMap> getTimeDictionary();

    List<DataMap> getStaticBases();

    List<DataMap> getActions();

    List<DataMap> getKnowledges();

    Integer getFrequentBetween(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

    Integer getFrequentOfStatic(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3);

    Integer getFrequentBetweenForMeta(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

    void saveStaticSentence(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

    void updateStaticSentence(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3);

    void saveMetaLink(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

    void updateMetaLink(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

    void saveKnowledgeLink(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4, @Param("param5") int param5);

    void updateKnowledgeLink(@Param("param1") String param1, @Param("param2") String param2, @Param("param3") String param3, @Param("param4") String param4);

}
