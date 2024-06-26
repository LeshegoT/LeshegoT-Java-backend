package services.VodaPay.utils;

import org.json.simple.JSONObject;
import services.VodaPay.constants.Environment;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
public class Request {

    private static String VODAPAY_BASEURL;

    public static void setVodapayBaseURL(String baseURL) {
        VODAPAY_BASEURL = baseURL;
    }

    public static String request(String method, String endPoint, JSONObject data) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {

        HeaderConstructor headers = new HeaderConstructor();

        URI uri = URI.create(VODAPAY_BASEURL + endPoint);
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .headers(headers.headerConstructor(method, endPoint, data))
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 299) {
            return response.body();
        } else {
            return "Error occurred: " + response.statusCode();
        }
    }
}
