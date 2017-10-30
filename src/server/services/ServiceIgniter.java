package server.services;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.server.Server;
import server.comm.DataMap;
import server.comm.RestProcessor;
import server.response.Response;
import server.response.ResponseConst;
import server.rest.DataMapUtil;
import server.rest.RestConstant;
import server.rest.RestUtil;
import services.CommonSVC;
import services.WebSVC;
import spark.Service;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;
import utils.Log;

import java.util.Calendar;

/**
 * @author 함의진
 * @version 2.0.0
 * 서버 실행을 위한 이그니션 클래스
 * @description (version 2.0.0) Json 스트링 Transformer를 Lambda 식으로 대체함.
 * Jul-21-2017
 */
public class ServiceIgniter {

    private Service comm;
    private Service service;

    private CommonSVC commonSVC;
    private WebSVC webSVC;

    {
        commonSVC = new CommonSVC();
        webSVC = new WebSVC();
    }

    public static final int LOG_DEFAULT_LENGTH = 500;
    private int logLength = LOG_DEFAULT_LENGTH;

    public static ServiceIgniter instance;

    public static ServiceIgniter getInstance() {
        if (instance == null) instance = new ServiceIgniter();
        return instance;
    }

    private ServiceIgniter() {
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, new EmbeddedJettyFactory((i, j, k) -> {
            Server server = new Server();
            server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 5000000);
            return server;
        }));

    }

    public void igniteServiceServer() {

        service = Service.ignite().port(RestConstant.REST_SERVICE);
        service.before((req, res) -> {
            DataMap map = RestProcessor.makeProcessData(req.raw());
            Log.e("[Connection] Service Server [" + Calendar.getInstance().getTime().toString() + "] :: [" + req.pathInfo() + "] FROM [" + RestUtil.extractIp(req.raw()) + "] :: " + map);

            res.type(RestConstant.RESPONSE_TYPE_JSON);
        });

        service.path("/web", ()->{
            service.post("/login", (req, res)->{
                DataMap map = RestProcessor.makeProcessData(req.raw());

                if(DataMapUtil.isValid(map, "account", "password")){
                    final String id = map.getString("account");
                    final String password = map.getString("password");
                    DataMap user = webSVC.loginWeb(id, password);

                    if(user == null) return new Response(ResponseConst.CODE_FAILURE, ResponseConst.MSG_FAILURE);
                    else {
                        DataMapUtil.mask(user, "password");
                        return new Response(ResponseConst.CODE_SUCCESS, ResponseConst.MSG_SUCCESS, user);
                    }
                }else{
                    return new Response(ResponseConst.CODE_INVALID_PARAM, ResponseConst.MSG_INVALID_PARAM);
                }
            }, RestUtil::toJson);

            service.get("/logs", (req, res)->{
                DataMap map = RestProcessor.makeProcessData(req.raw());
                return new Response(ResponseConst.CODE_SUCCESS, ResponseConst.MSG_SUCCESS, webSVC.getListOfLogs(map));
            }, RestUtil::toJson);

            service.get("/instant", (req, res)->{
                DataMap map = RestProcessor.makeProcessData(req.raw());
                return new Response(ResponseConst.CODE_SUCCESS, ResponseConst.MSG_SUCCESS, commonSVC.instantResponse(map.getString("msg")));
            }, RestUtil::toJson);

        });

    }
}