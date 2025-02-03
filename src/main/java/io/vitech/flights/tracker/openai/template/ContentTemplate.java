package io.vitech.flights.tracker.openai.template;

public class ContentTemplate {
    public static String REQUEST_CONTENT_TEMPLATE= "Give me response in format json without additional details where have key status if you find a solution to set true if don't have success to set false and I have missing elements which are null or empty I want to populate them in the response %s";



    public static String getTemplate(String objectToPopulate) {
        return String.format(REQUEST_CONTENT_TEMPLATE, objectToPopulate);
    }
}
