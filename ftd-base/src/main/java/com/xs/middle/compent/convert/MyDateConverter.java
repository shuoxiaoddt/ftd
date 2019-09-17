package com.xs.middle.compent.convert;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyDateConverter implements Converter {

    private final String dateformatter;

    private static final String DATEFORMATTER = "yyyy-MM-dd HH:mm:ss";

    public MyDateConverter() {
        super();
        this.dateformatter = DATEFORMATTER;
    }

    public MyDateConverter(String dateformatter) {
        super();
        this.dateformatter = dateformatter;
    }

    @Override
    public boolean canConvert(Class type) {
        return "java.util.Date".equals(type.getName());
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Date date = (Date) source;
        writer.setValue(format(this.dateformatter, date));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        try {
            return parse(this.dateformatter, reader.getValue());
        } catch (Exception e) {
            throw new ConversionException(e.getMessage(), e);
        }
    }

    private static String format(String pattern, Date date) {
        if (date == null) {
            return null;
        } else {
            return new SimpleDateFormat(pattern).format(date);
        }
    }

    private static Date parse(String pattern, String text) throws ParseException {
        if (text == null || "".equals(text.trim()) || "null".equals(text.trim().toLowerCase())) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(text);
    }

}
