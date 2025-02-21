package io.vitech.flights.tracker.openai.template;

public class ContentTemplate {
    public static String REQUEST_CONTENT_TEMPLATE= "Give me response in format json without additional details where have these structure { status : true, data: [{user data}]}, " +
            "set key status if you find a solution at least 1 element, set true if don't have success - set false and I want from you to populate missing elements, where have - null, empty or N/A in the response data :  %s";

    public static String REQUEST_AI_STATISTIC_CONTENT_TEMPLATE= "Give me statistic in format json without additional details where have these structure" +
            " { status : {true or false}, data: { topDestiantion: {calculated top arrival airport}, topAirline: {topAirline}, topAircraftType: {most used aircraft type}, topBusinessDay: {day of the week with the most flights} }, " +
            "I want from you to analyze this raw data :  %s";


    public static String getTemplate(String objectToPopulate) {
        return String.format(REQUEST_CONTENT_TEMPLATE, objectToPopulate);
    }
}
