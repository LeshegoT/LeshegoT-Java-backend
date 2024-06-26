package services.VodaPay.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Stack;

public class Signatures {


    static Path executingPath = Paths.get(System.getProperty("user.dir"));
    private static final Path privateKeyPath = Paths.get(executingPath.toString(), Config.getPrivateKeyPath());
    private static final Path publicKeyPath = Paths.get(executingPath.toString(), Config.getVodapayPublicKeyPath());

    public static String generatePayload(String method, String endPoint, String requestTime, JSONObject body) throws JsonProcessingException {
        ObjectMapper jsonEncode = new ObjectMapper();
        String unsignedPayload = method+" "+ endPoint+"\n"+ Config.getVodapayClientId()+"."+requestTime+'.'+ jsonEncode.writeValueAsString(body);
        return unsignedPayload;
    }

    public static String generate(String method,String endPoint, String requestTime, JSONObject body ) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, IOException {
        String unsignedContent = generatePayload(method,endPoint,requestTime,body);
        String privateKeyData = new String(Files.readAllBytes(privateKeyPath), Charset.defaultCharset());
        String privateKeyPEM = privateKeyData
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));
        PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(unsignedContent.getBytes("UTF-8"));
        byte[] s = sign.sign();
        return Base64.getEncoder().encodeToString(s);
    }

    public static boolean verify(JSONObject headers, JSONObject body, String method, String endPoint) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, InvalidKeyException, SignatureException {
        String requestTime = (String) headers.get("request-time");
        String[] splitSig =  ((String) headers.get("signature")).split(",");
        Stack stack = new Stack();
        for(int i = 0; i<splitSig.length;i++){
            stack.push(splitSig[i]);
        }
        String sigToVerify = ((String) (stack.pop())).substring(10);
        String unsignedContent = generatePayload(method,endPoint,requestTime,body);
        String publicKeyData = new String(Files.readAllBytes(publicKeyPath), Charset.defaultCharset());
        String publicKeyPEM = publicKeyData
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(unsignedContent.getBytes());
        byte[] signatureByte = Base64.getDecoder().decode(sigToVerify);
        boolean verifies = sig.verify(signatureByte);
        return verifies;
    }
}
