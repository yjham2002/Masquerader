package databases.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import server.comm.DataMap;

import java.util.List;

public interface WebMapper {

    DataMap getUser(@Param("id") String id, @Param("pw") String pw);

    List<DataMap> getListOfLogs(@Param("search") String search, @Param("page") int page, @Param("limit") int limit);

    Integer getCountOfLogs(@Param("search") String search);

}
