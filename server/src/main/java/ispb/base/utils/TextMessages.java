package ispb.base.utils;


public class TextMessages {

    private String initPaymentName;

    public String getInitPaymentName(String contractNumber) {
        String result = initPaymentName;
        result = result.replaceAll("\\{contract_number\\}", contractNumber);
        return result;
    }
}
