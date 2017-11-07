/**
 * $Id: LenientTimestampEditor.java,v 1.1 2012/09/11 06:36:25 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.util;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

/**
 * PropertyEditor for <code>java.sql.Timestamp</code>, lenient when parsing.
 * 
 * @author ResourceOne BizFoundation Team: ganjp
 * 
 * @since 1.0
 */
public class LenientTimestampEditor extends PropertyEditorSupport {
    public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("LenientTimestamp");
    private static final String TIMESTAMP_FORMAT = "timestamp.format";
    private static final String FORMAT_DELIM = ";";

    private final DateFormat dateFormat;

    private final boolean allowEmpty;

    private final int exactDateLength;


    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     * @param dateFormat DateFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     */
    public LenientTimestampEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     * <p>The "exactDateLength" parameter states that IllegalArgumentException gets
     * thrown if the String does not exactly match the length specified. This is useful
     * because SimpleDateFormat does not enforce strict parsing of the year part,
     * not even with <code>setLenient(false)</code>. Without an "exactDateLength"
     * specified, the "01/01/05" would get parsed to "01/01/0005".
     * @param dateFormat DateFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     * @param exactDateLength the exact expected length of the date String
     */
    public LenientTimestampEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }


    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        }
        else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        }
        else {
            setValue(parseLenient(dateFormat, text));
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    public String getAsText() {
        Timestamp value = (Timestamp) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }
    
    public static Timestamp parseLenient(DateFormat lastFormat, String source) {
        //  using formats defined in .properties
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setLenient(false);//严格的方式验证日期格式，避免弱匹配模式下绑定到Timestamp值不正确的问题
        StringTokenizer st = new StringTokenizer(resourceBundle.getString(TIMESTAMP_FORMAT), FORMAT_DELIM);
        while (st.hasMoreTokens()) {
            String format = st.nextToken();
            
            dateFormat.applyPattern(format);
            try {
                return new Timestamp(dateFormat.parse(source).getTime());
            }
            catch (ParseException e) {
            }
        }

        //  using default format
        try {
            return new Timestamp(DEFAULT_FORMAT.parse(source).getTime());
        }
        catch (ParseException e) {
            //  using lastFormat from caller
            if(lastFormat != null) {
                try {
                    return new Timestamp(lastFormat.parse(source).getTime());
                }
                catch (ParseException e1) {
                    return null;
//                  throw new IllegalArgumentException("Could not parse date: " + e4.getMessage());
                }
            } else {
                return null;
//              throw new IllegalArgumentException("Could not parse date: " + e3.getMessage());
            }
        }
    }
}
