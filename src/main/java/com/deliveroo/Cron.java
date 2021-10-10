package com.deliveroo;

import java.util.*;

public class Cron {
    // Class constants
    public static final List<String> MINUTES = Arrays.asList(
            "0","1","2","3","4","5","6","7","8","9","10","11",
            "12","13","14","15","16","17","18","19","20","21",
            "22","23","24","25","26","27","28","29","30","31",
            "32","33","34","35","36","37","38","39","40","41",
            "42","43","44","45","46","47","48","49","50","51",
            "52","53","54","55","56","57","58","59");
    public static final List<String> HOURS = Arrays.asList(
            "0","1","2","3","4","5","6","7","8","9","10","11","12",
            "13","14","15","16","17","18","19","20","21","22","23");
    public static final List<String> DAYS_OF_MONTH = Arrays.asList(
            "1","2","3","4","5","6","7","8","9","10","11","12",
            "13","14","15","16","17","18","19","20","21","22",
            "23","24","25","26","27","28","29","30","31");
    public static final List<String> WEEKDAY_NUMBERS  = Arrays.asList(
            "0","1","2","3","4","5","6");
    public static final List<String> WEEKDAY_NAMES  = Arrays.asList(
            "SUN","MON","TUE","WED","THU","FRI","SAT");
    public static final List<String> MONTH_NUMBERS  = Arrays.asList(
            "1","2","3","4","5","6","7","8","9","10","11","12");
    public static final List<String> MONTH_NAMES = Arrays.asList(
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

    public static final String COMMA_SEPARATOR  = ",";
    public static final String REGEX_WHITESPACE = "\\s+";
    public static final String ASTERISK         = "*";
    public static final String PERIOD_PREFIX    = "*/";
    public static final char   RANGE_MARKER     = '-';

    private static final int    VALID_NUMBER_OF_TOKENS = 6;
    private static final String DAY_OF_WEEK_HEADER     = "day of week   ";
    private static final String MONTH_HEADER           = "month         ";
    private static final String DAY_OF_MONTH_HEADER    = "day of month  ";
    private static final String HOUR_HEADER            = "hour          ";
    private static final String MINUTE_HEADER          = "minute        ";
    private static final String COMMAND_HEADER         = "command       ";


    // Instance private variables
    private final String cronString;
    private String cronStringTrimmed = null;
    private List<String> minutes     = null;
    private List<String> hours       = null;
    private List<String> daysOfMonth = null;
    private List<String> months      = null;
    private List<String> daysOfWeek  = null;
    private String       command     = null;

    private boolean isParsed = false;
    private boolean isValid = true;

    public Cron(String cronString) {
        this.cronString = cronString;
        isValid = this.parse();
    }

    private boolean parse() {
        if (cronString == null || cronString.isEmpty()) return false;

        String[] tokens = cronString.trim().split(REGEX_WHITESPACE);
        if (tokens.length != VALID_NUMBER_OF_TOKENS) {
            isValid = false;
            return false;
        }
        this.cronStringTrimmed = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                + tokens[3] + " " + tokens[4] + " " + tokens[5];

        this.minutes = genericSelector(tokens[0], MINUTES, null);
        this.hours = genericSelector(tokens[1], HOURS, null);
        this.daysOfMonth = genericSelector(tokens[2], DAYS_OF_MONTH, null);
        this.months = genericSelector(tokens[3], MONTH_NUMBERS, MONTH_NAMES);
        this.daysOfWeek = genericSelector(tokens[4], WEEKDAY_NUMBERS, WEEKDAY_NAMES);
        this.command = tokens[5];

        isParsed = true;
        return true;
    }

    public List<String> getMinutes() {
        return (isParsed && isValid) ? minutes : null;
    }

    public List<String> getHours() {
        return (isParsed && isValid) ? hours : null;
    }

    public List<String> getDaysOfMonth() {
        return (isParsed && isValid) ? daysOfMonth : null;
    }

    public List<String> getMonths() {
        return (isParsed && isValid) ? months : null;
    }
    public List<String> getDaysOfWeek() {
        return (isParsed && isValid) ? daysOfWeek : null;
    }

    public String getCommnd() {
        return (isParsed && isValid) ? command : null;
    }

    public String getCronString() {
        return cronStringTrimmed;
    }

    public String getCronDetails() {
        if ( ! isValid) return null;
        String minutes = toString(MINUTE_HEADER, this.minutes);
        String hours = toString(HOUR_HEADER, this.hours);
        String daysOfMonth = toString(DAY_OF_MONTH_HEADER, this.daysOfMonth);
        String months = toString(MONTH_HEADER, this.months);
        String daysOfWeek = toString(DAY_OF_WEEK_HEADER, this.daysOfWeek);
        String command = toString(COMMAND_HEADER, Collections.singletonList(this.command));
        return minutes + "\n"
                + hours + "\n"
                + daysOfMonth + "\n"
                + months + "\n"
                + daysOfWeek + "\n"
                + command + "\n";
    }

    public static List<String> genericSelector(
            String valuesRangesPeriods,
            final List<String> values,
            final List<String> labels // Optional Values
    ) {

        String[] expressions = valuesRangesPeriods.split(COMMA_SEPARATOR);
        List<String> selection = new ArrayList<>();

        Set<String> lookUpValues = new HashSet<>(values);
        Set<String> lookUpLabels = null;
        if (labels != null) {
            lookUpLabels = new HashSet<>(labels);
        }

        for (String expression : expressions) {
            // "*" - all values
            if (expression.equalsIgnoreCase(ASTERISK)) {
                return values;
            }
            // "*/..." - periodic values
            else if (expression.startsWith(PERIOD_PREFIX)) {
                Integer period = null;
                try {
                    period = Integer.parseInt(expression.substring(2));
                } catch (NumberFormatException exception) {
                    System.out.println("Syntax Error: " + expression.substring(2) + " should be a number");
                    continue;
                }
                if (period > values.size()) {
                    System.out.println("Syntax Error: " + expression.substring(2) + " should be a less than or equal to " + values.get(values.size()-1));
                    continue;
                }
                int index = 0;
                while (index < values.size()) {
                    selection.add(values.get(index));
                    index += period;
                }
            }
            // "...-..." - ranges
            else if (expression.indexOf(RANGE_MARKER) != -1) {
                int pos = expression.indexOf(RANGE_MARKER);
                String from = expression.substring(0, pos);
                String to = expression.substring(pos + 1);
                boolean rangeStarted = false;
                boolean rangeEnded = false;
                if (lookUpValues.contains(from) && lookUpValues.contains(to)) {
                    for (String value : values) {
                        if (value.equalsIgnoreCase(from)) {
                            rangeStarted = true;
                        }
                        if (rangeStarted && !rangeEnded) {
                            selection.add(value);
                        }
                        if (value.equalsIgnoreCase(to)) {
                            rangeEnded = true;
                        }
                    }
                }
                else if (lookUpLabels != null && lookUpLabels.contains(from) && lookUpLabels.contains(to)) {
                    for (String value : labels) {
                        if (value.equalsIgnoreCase(from)) {
                            rangeStarted = true;
                        }
                        if (rangeStarted && !rangeEnded) {
                            selection.add(value);
                        }
                        if (value.equalsIgnoreCase(to)) {
                            rangeEnded = true;
                        }
                    }
                } else {
                    System.out.println("Syntax Error: Both '" + from + "' and '" + to + "' should be valid numbers or names");
                }
            }
            // individual values
            else {
                if (lookUpValues.contains(expression)) {
                    selection.add(expression);
                } else if (lookUpLabels != null && lookUpLabels.contains(expression)) {
                    selection.add(expression);
                }
            }
        }
        return hashSort(selection, values, labels);
    }

    // creates string representation with a given header
    public static String toString(String header, List<String> selectedValues) {
        StringBuilder sb = new StringBuilder(header);
        boolean loopExecuted = false;
        if (selectedValues != null) {
            for (String value : selectedValues) {
                sb.append(value).append(' ');
                loopExecuted = true;
            }
            if (loopExecuted) sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    // arranges a given set of values/names in their natural order
    public static List<String> hashSort(List<String> selectedValues, List<String> values, List<String> names) {
        List<String> sortedList = new ArrayList<>();
        if (selectedValues == null || selectedValues.isEmpty()) return sortedList;
        Set<String> lookUpSelectedValues = new HashSet<>(selectedValues);

        for (int i = 0; i < values.size(); i++) {
            if (lookUpSelectedValues.contains(values.get(i))) {
                sortedList.add(values.get(i));
            } else if (names != null && lookUpSelectedValues.contains(names.get(i))) {
                sortedList.add(names.get(i));
            }
        }
        return sortedList;
    }
}