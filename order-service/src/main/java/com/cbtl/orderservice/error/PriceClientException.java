package com.cbtl.orderservice.error;

public class PriceClientException extends Exception {
    private final int responseStatus;

    public PriceClientException(int responseStatus) {
        super();
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus() {
        return responseStatus;
    }
}
