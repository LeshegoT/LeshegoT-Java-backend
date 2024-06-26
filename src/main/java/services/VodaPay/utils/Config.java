package services.VodaPay.utils;

import services.VodaPay.constants.Environment;

public class Config {
    private static String serverBaseURL;
    private static String privateKeyPath;
    private static String vodapayMerchantId;
    private static String vodapayPublicKeyPath;
    private static String vodapayNotifyURL;
    private static String vodapayClientId;


    public static void setServerBaseURL(String serverBaseURL) {
        Config.serverBaseURL = serverBaseURL;
    }

    public static String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public static void setPrivateKeyPath(String privateKeyPath) {
        Config.privateKeyPath = privateKeyPath;
    }


    public static void setVodapayMerchantId(String vodapayMerchantId) {
        Config.vodapayMerchantId = vodapayMerchantId;
    }

    public static String getVodapayPublicKeyPath() {
        return vodapayPublicKeyPath;
    }

    public static void setVodapayPublicKeyPath(String vodapayPublicKeyPath) {
        Config.vodapayPublicKeyPath = vodapayPublicKeyPath;
    }

    public static String getVodapayNotifyURL() {
        return vodapayNotifyURL;
    }

    public static String getVodapayClientId() {
        return vodapayClientId;
    }

    public static void setVodapayClientId(String vodapayClientId) {
        Config.vodapayClientId = vodapayClientId;
    }

    public static String setEnvironment(String environment) {
        Environment environmentInstance = new Environment();
        try {

            String baseURL = environmentInstance.getVodapayBaseURL(environment);

            Request.setVodapayBaseURL(baseURL);
            return baseURL;
        } catch (Exception e) {

            System.err.println("Error configuring Request class: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
