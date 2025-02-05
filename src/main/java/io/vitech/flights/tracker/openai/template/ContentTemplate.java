package io.vitech.flights.tracker.openai.template;

public class ContentTemplate {
    public static String REQUEST_CONTENT_TEMPLATE= "Give me response in format json without additional details where have these structure { status : {status}, data: [{user data}]}, " +
            "set key status if you find a solution at least 1 element, set true if don't have success - set false and I want from you to populate missing elements, where have - null, empty or N/A in the response data :  %s";



    public static String getTemplate(String objectToPopulate) {
        return String.format(REQUEST_CONTENT_TEMPLATE, objectToPopulate);
    }
}
