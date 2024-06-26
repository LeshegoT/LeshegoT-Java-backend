package services.VodaPay.services.v3;

import org.json.simple.JSONObject;
import services.VodaPay.constants.Environment;
import services.VodaPay.utils.Config;
import services.VodaPay.utils.HeaderConstructor;
import services.VodaPay.utils.Signatures;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class NotifyPayment {

    public static JSONObject notifyPayment(JSONObject headers, JSONObject body) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, NoSuchProviderException, InvalidKeyException {

        final String VODAPAY_NOTIFY_URL = Config.getVodapayNotifyURL();
        String notifyMethod = "POST";

        Signatures signature = new Signatures();
        boolean validSignature = signature.verify(headers, body, notifyMethod, VODAPAY_NOTIFY_URL);

        if(!validSignature){
            JSONObject signatureInvalid = new JSONObject();
            signatureInvalid.put("body", null);
            signatureInvalid.put("headers",null);
            return signatureInvalid;
        }

        JSONObject bodyObject = new JSONObject();
        bodyObject.put("resultCode", "SUCCESS");
        bodyObject.put("resultStatus", "S");
        bodyObject.put("resultMessage", "success");
        JSONObject  successResponse = new JSONObject();
        successResponse.put("result",bodyObject);

        HeaderConstructor headersCons = new HeaderConstructor();
        String[] returnHeaders = headersCons.headerConstructor(notifyMethod, VODAPAY_NOTIFY_URL, successResponse, false);

        JSONObject result = new JSONObject();
        result.put("body", successResponse);
        result.put("headers",returnHeaders);

        return result;

    }

}