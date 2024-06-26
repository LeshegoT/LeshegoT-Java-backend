package com.vodacom.vodapayservicesstarterjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.VodaPay.services.v2.ApplyToken;
import services.VodaPay.utils.Config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@SpringBootApplication
public class VodapayServicesStarterJavaApplication {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, InterruptedException {

		Config.setServerBaseURL("http://mock.vision.vodacom.aws.corp/mock/api/v1/payments/notifyPayment.htm");
		Config.setEnvironment("sandbox");
		Config.setVodapayClientId("2020122653946739963336");
		Config.setVodapayMerchantId("216620000000188034591");
		Config.setVodapayPublicKeyPath("src/main/java/certificates/rsa_public_key.pem");
		Config.setPrivateKeyPath("src/main/java/certificates/rsa_private_key.pem");

		SpringApplication.run(VodapayServicesStarterJavaApplication.class, args);
	}

}
