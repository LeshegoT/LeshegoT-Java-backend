package services.VodaPay.services.v2;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.VodaPay.constants.Endpoints;
import services.VodaPay.constants.Environment;
import services.VodaPay.utils.Config;
import services.VodaPay.utils.Request;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class InquiryUserInfo {
    public static String inquiryUserInfo(String accessToken) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException, ParseException, JSONException {

        final String  VODAPAY_CLIENT_ID = Config.getVodapayClientId();
        JSONObject body = new JSONObject();
        body.put("authClientId", VODAPAY_CLIENT_ID);
        body.put("accessToken",accessToken);
        String method = "POST";
        Endpoints endpoints = new Endpoints();
        String endPoint = endpoints.getInquiryUserInfo();
        String response = Request.request(method,endPoint,body);

        return response;
    }

    private static JSONObject getJSONObject(String JSONString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(JSONString);
        return jsonObject;
    }
}