package services;

import databases.mybatis.mapper.WebMapper;
import databases.paginator.ListBox;
import databases.paginator.PageInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.session.SqlSession;
import server.comm.DataMap;
import server.rest.DataMapUtil;
import utils.Log;

import java.util.List;

public class WebSVC extends BaseService {

    public DataMap loginWeb(String id, String pw){
        try(SqlSession sqlSession = super.getSession()){
            WebMapper webMapper = sqlSession.getMapper(WebMapper.class);
            return webMapper.getUser(id, pw);
        }
    }

    public ListBox getListOfLogs(DataMap map){
        Pair<Integer, Integer> indexPair = DataMapUtil.extractIndice(map);
        final int page = indexPair.getLeft();
        int limit = indexPair.getRight();
        int realPage = (page - 1) * limit;
        int total;
        List<DataMap> list;
        try(SqlSession sqlSession = super.getSession()){
            WebMapper webMapper = sqlSession.getMapper(WebMapper.class);
            list = webMapper.getListOfLogs(map.getString("search"), realPage, limit);
            total = webMapper.getCountOfLogs(map.getString("search"));
        }

        PageInfo pageInfo = new PageInfo(limit, page);
        pageInfo.commit(total);

        ListBox listBox = new ListBox(pageInfo, list);

        return listBox;
    }

}
