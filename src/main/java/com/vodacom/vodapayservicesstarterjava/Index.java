package com.vodacom.vodapayservicesstarterjava;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import services.VodaPay.services.v2.ApplyToken;
import services.VodaPay.services.v2.InquiryPayment;
import services.VodaPay.services.v2.Pay;
import services.VodaPay.services.v2.InquiryUserInfo;
import services.VodaPay.services.v2.NotifyPayment;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class Index {

    private static final Logger log = LoggerFactory.getLogger(Index.class);
    @PostMapping("/userInfo")
    public JSONObject login(@RequestBody JSONObject body) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException, ParseException, JSONException {
        String applyTokenResponse = ApplyToken.applyToken((String) body.get("authCode"));
        JSONObject result = (JSONObject) getJSONObject(applyTokenResponse).get("result");
        log.info(result.toString());
        if(!result.get("resultCode").equals("SUCCESS")){
            return getJSONObject(applyTokenResponse);
        }

        String userInfoResponse = InquiryUserInfo.inquiryUserInfo((String) getJSONObject(applyTokenResponse).get("accessToken"));

        JSONObject userInformation = (JSONObject) getJSONObject(userInfoResponse).get("userInfo");
        JSONArray contactInfoArray = new JSONArray(userInformation.get("contactInfos").toString());
        Object contactEmail =  contactInfoArray.get(0);
        Object contactNo =  contactInfoArray.get(1);
        // Extract user email and phone number
        JSONObject userDetails = new JSONObject();
        userDetails.put("userID", userInformation.get("userId"));
        userDetails.put("username", userInformation.get("nickName"));
        userDetails.put("firstName", ((JSONObject)userInformation.get("userName")).get("firstName"));
        userDetails.put("lastName", ((JSONObject)userInformation.get("userName")).get("lastName"));

        userDetails.put("email", getJSONObject(contactEmail.toString()).get("contactNo"));
        userDetails.put("phone", getJSONObject(contactNo.toString()).get("contactNo"));


        JSONObject response = new JSONObject();
        response.put("results", "success");
        response.put("userInfo", userDetails);

        return response;
    }

    @PostMapping("/inquiryPayment")
    public JSONObject inquiryPaymentAPI(@RequestBody JSONObject body) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException, ParseException {
        String paymentInfo = InquiryPayment.inquiryPayment((String) body.get("paymentId"));

        return getJSONObject(paymentInfo);
    }

    @PostMapping("/pay")
    public JSONObject pay(@RequestBody JSONObject body) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException, ParseException {
        String paymentResponse = Pay.pay((String) body.get("buyerID"),(String) body.get("amountInCents"), (String) body.get("goodsId"),(int) body.get("expiryTimeInHours"), (String) body.get("requestId"), (String) body.get("notifyUrl"));
        JSONObject redirecrUrlObject = (JSONObject) getJSONObject(paymentResponse).get("redirectActionForm");
        JSONObject response = new JSONObject();
        response.put("result", "success");
        response.put("paymentId",getJSONObject(paymentResponse).get("paymentId"));
        response.put("redirectUrl", redirecrUrlObject);

        return response;
    }

    @PostMapping("/payment-notify")
    public  JSONObject payment(@RequestBody JSONObject body, @RequestHeader("signature") String signature, @RequestHeader("request-time") String requestTime, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, NoSuchProviderException, InvalidKeyException {
        JSONObject headers = new JSONObject();
        headers.put("signature", signature);
        headers.put("request-time", requestTime);

        JSONObject notifyObject = NotifyPayment.notifyPayment(headers,body);
        if ((notifyObject.get("headers") == null) && (notifyObject.get("body")) == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        String[] resHeaders = (String[]) notifyObject.get("headers");
        for(int i = 0; i<resHeaders.length;i+=2){
            response.setHeader(resHeaders[i], resHeaders[i+1]);
        }

        return (JSONObject) notifyObject.get("body");

    }

    private JSONObject getJSONObject(String JSONString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(JSONString);
        return jsonObject;
    }
}
