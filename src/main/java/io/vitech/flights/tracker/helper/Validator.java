package io.vitech.flights.tracker.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlType;

public class Validator {
    private static final int DEFAULT_AUTOCOMPLETE_MIN_LENGTH = 2;
    public static Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static boolean isValidAutocomplete(final String str) {
        if(StringUtils.isEmpty(str)) {
            return false;
        }
        if(str.length() < DEFAULT_AUTOCOMPLETE_MIN_LENGTH) {
            LOGGER.warn("Autocomplete string is too short [{}]", str);
            return false;
        }
        return true;
    }
}
