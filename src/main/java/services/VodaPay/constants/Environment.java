package services.VodaPay.constants;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {









    private final  String vodapaySandboxBaseURL;
    private final String vodapayTest4BaseURL;
    private final String vodapayProdBaseURL;







    public Environment() {
        Dotenv dotenv = Dotenv.load();
        this.vodapaySandboxBaseURL = dotenv.get("VODAPAY_SANDBOX_BASEURL");
        this.vodapayTest4BaseURL = dotenv.get("VODAPAY_TEST4_BASEURL");
        this.vodapayProdBaseURL= dotenv.get("VODAPAY_PROD_BASEURL");
    }
    public String getVodapayBaseURL(String environment) {
        switch (environment.toLowerCase()) {
            case "sandbox":
                return vodapaySandboxBaseURL;
            case "test4":
                return vodapayTest4BaseURL;
            case "prod":
                return vodapayProdBaseURL;
            default:
                throw new IllegalArgumentException("Invalid environment specified: " + environment);
        }
    }

}
