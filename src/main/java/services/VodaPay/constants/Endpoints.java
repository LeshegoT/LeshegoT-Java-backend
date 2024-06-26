package services.VodaPay.constants;

public class Endpoints {

    private final String applyToken;
    private final String inquiryUserInfo;
    private  final String inquiryPaymentInfoV2;
    private  final String pay;
    private final String inquiryPaymentInfoV3;

    public Endpoints() {
        this.applyToken = "/v2/authorizations/applyToken";
        this.inquiryUserInfo = "/v2/customers/user/inquiryUserInfo";
        this.inquiryPaymentInfoV2 = "/v2/payments/inquiryPayment";
        this.pay = "/v2/payments/pay";
        this.inquiryPaymentInfoV3 = "/v3/payments/inquiryPayment";
    }

    public String getApplyToken() {

        return applyToken;
    }

    public String getInquiryUserInfo() {

        return inquiryUserInfo;
    }

    public String getInquiryPaymentInfoV2() {

        return inquiryPaymentInfoV2;
    }

    public String getPay() {

        return pay;
    }

    public String getInquiryPaymentInfoV3() {

        return inquiryPaymentInfoV3;
    }

}
