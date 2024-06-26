package services.VodaPay.services.v3;

import org.json.simple.JSONObject;
import services.VodaPay.constants.Endpoints;
import services.VodaPay.utils.Request;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class InquiryPayment {
    public static String inquiryPayment(String paymentId) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {

        JSONObject body = new JSONObject();
        body.put("paymentId", paymentId);
        String method = "POST";
        Endpoints endpoints = new Endpoints();
        String endPoint = endpoints.getInquiryPaymentInfoV3();
        Request req = new Request();
        String response = req.request(method,endPoint,body);
        return response;
    }
}