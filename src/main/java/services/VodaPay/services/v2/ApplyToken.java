package services.VodaPay.services.v2;

import org.json.simple.JSONObject;
import services.VodaPay.constants.Endpoints;
import services.VodaPay.utils.Request;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


public class ApplyToken {
    public static String applyToken(String  authCode, boolean isRefresh) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        JSONObject refreshBody = new JSONObject();
        refreshBody.put("grantType", "REFRESH_TOKEN");
        refreshBody.put("refreshToken",authCode);

        JSONObject authBody = new JSONObject();
        authBody.put("grantType", "AUTHORIZATION_CODE");
        authBody.put("authCode",authCode);

        JSONObject body;
        if(isRefresh){
            body = refreshBody;
        }else{
            body = authBody;
        }
        String method = "POST";
        Endpoints endpoints = new Endpoints();
        String endPoint = endpoints.getApplyToken();
        String response = Request.request(method,endPoint,body);
        return response;
    }

    public static String applyToken(String authCode) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        JSONObject authBody = new JSONObject();
        authBody.put("grantType", "AUTHORIZATION_CODE");
        authBody.put("authCode", authCode);

        JSONObject body = authBody;
        String method = "POST";
        Endpoints endpoints = new Endpoints();
        String endPoint = endpoints.getApplyToken();
        String response = Request.request(method, endPoint, body);

        return response;
    }

}