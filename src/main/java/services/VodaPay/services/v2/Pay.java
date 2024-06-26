package services.VodaPay.services.v2;

import org.json.simple.JSONObject;
import services.VodaPay.constants.Endpoints;
import services.VodaPay.constants.Environment;
import services.VodaPay.utils.Config;
import services.VodaPay.utils.Request;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Pay {
    public static String pay(String buyerID, String amountInCents, String goodsId, int expiryTimeInHours, String requestId, String notifyUrl) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, expiryTimeInHours);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String paymentExpiry = df.format(cal.getTime());

        String method = "POST";
        Endpoints endpoints = new Endpoints();
        String endPoint = endpoints.getPay();

        JSONObject paymentAmount = new JSONObject();
        paymentAmount.put("currency", "ZAR");
        paymentAmount.put("value", amountInCents);



        JSONObject goods = new JSONObject();
        goods.put("referenceGoodsId", goodsId);
        JSONObject goodsUnitAmount = new JSONObject();
        goodsUnitAmount.put("currency", "ZAR");
        goodsUnitAmount.put("value", amountInCents);
        goods.put("goodsUnitAmount", goodsUnitAmount);
        goods.put("goodsName", "Test Payment");

        JSONObject buyer = new JSONObject();
        buyer.put("referenceBuyerId", buyerID);

        JSONObject env = new JSONObject();
        env.put("terminalType", "MINI_APP");

        JSONObject order = new JSONObject();
        order.put("goods", goods);
        order.put("env", env);
        order.put("orderDescription", "VodaPay Test Payment");
        order.put("buyer", buyer);

        JSONObject body = new JSONObject();
        body.put("productCode", "CASHIER_PAYMENT");
        body.put("salesCode", "51051000101000000011");

        body.put("paymentNotifyUrl", notifyUrl);

        body.put("paymentRequestId", requestId);
        body.put("paymentExpiryTime", paymentExpiry);
        body.put("paymentAmount", paymentAmount);
        body.put("order", order);

        String response = Request.request(method, endPoint, body);

        return response;
    }
}