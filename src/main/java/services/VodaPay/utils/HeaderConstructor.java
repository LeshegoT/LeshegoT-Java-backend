package services.VodaPay.utils;

import org.json.simple.JSONObject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HeaderConstructor {

    private static String VODAPAY_CLIENT_ID;

    static {
        VODAPAY_CLIENT_ID = Config.getVodapayClientId();

    }


    public static String[] headerConstructor(String method, String endPoint, JSONObject bodyData, boolean isRequest) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, IOException, InvalidKeyException {

        String timeStampKey;
        if(isRequest){
            timeStampKey = "request-time";
        }else{
            timeStampKey = "response-time";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String time = df.format(new Date());
        Signatures signature = new Signatures();
        String signatureValue = signature.generate(method,endPoint,time,bodyData);
        String[] headers = {
                "Content-Type", "application/json",
                "client-id",VODAPAY_CLIENT_ID,
                "signature","algorithm=RSA256, keyVersion=1, signature="+signatureValue,
                timeStampKey,time

        };

       return headers;
    }

    public static String[] headerConstructor(String method, String endPoint, JSONObject bodyData) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, IOException, InvalidKeyException {
        String timeStampKey = "request-time";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String time = df.format(new Date());
        Signatures signature = new Signatures();
        String signatureValue = signature.generate(method,endPoint,time,bodyData);
        String[] headers = {
                "Content-Type", "application/json",
                "client-id",VODAPAY_CLIENT_ID,
                "signature","algorithm=RSA256, keyVersion=1, signature="+signatureValue,
                timeStampKey,time

        };

        return headers;
    }

}