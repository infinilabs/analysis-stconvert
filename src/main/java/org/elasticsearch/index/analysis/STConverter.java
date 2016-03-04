package org.elasticsearch.index.analysis;

import java.io.*;
import java.util.*;

/**
 * some parts of code copied from:http://code.google.com/p/java-zhconverter/
 */
public class STConverter {

    private Properties charMap = new Properties();
    private Properties revCharMap = new Properties();
    private Set conflictingSets  = new HashSet();

    public STConverter(){


        InputStream file1 = null;
        file1 = this.getClass().getResourceAsStream("/t2s.properties");
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(file1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (is != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(is);
                charMap.load(reader);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                }
            }
        }
        initializeHelper();
    }

    private void initializeHelper() {

        Map stringPossibilities = new HashMap();
        Iterator iter = charMap.keySet().iterator();
        while (iter.hasNext()) {

            //fill revmap
            String key = (String) iter.next();
            revCharMap.put(charMap.get(key),key);

            if (key.length() >= 1) {

                for (int i = 0; i < (key.length()); i++) {
                    String keySubstring = key.substring(0, i + 1);
                    if (stringPossibilities.containsKey(keySubstring)) {
                        Integer integer = (Integer)(stringPossibilities.get(keySubstring));
                        stringPossibilities.put(keySubstring, new Integer(
                                integer.intValue() + 1));

                    } else {
                        stringPossibilities.put(keySubstring, new Integer(1));
                    }

                }
            }
        }

        iter = stringPossibilities.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (((Integer)(stringPossibilities.get(key))).intValue() > 1) {
                conflictingSets.add(key);
            }
        }
    }

    public String convert(STConvertType type,String in) {
           Map map=charMap;
        if(type== STConvertType.SIMPLE_2_TRADITIONAL){
            map=revCharMap;
        }

        StringBuilder target = new StringBuilder();
        StringBuilder source = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {

            char c = in.charAt(i);
            String key = "" + c;
            source.append(key);

            if (conflictingSets.contains(source.toString())) {
            } else if (map.containsKey(source.toString())) {
                target.append(map.get(source.toString()));
                source.setLength(0);
            } else {
                CharSequence sequence = source.subSequence(0, source.length()-1);
                source.delete(0, source.length()-1);
                mapping(map, target, new StringBuilder(sequence));
            }
        }

        mapping(map, target, source);

        return target.toString();
    }

    private static STConverter instance=new STConverter();

    public static STConverter getInstance(){
        if(instance==null){instance = new STConverter();}
        return instance;
    }

    public String convert(String text, STConvertType converterType) {
        return getInstance().convert(converterType,text);
    }

    private void mapping(Map map, StringBuilder outString, StringBuilder stackString) {
        while (stackString.length() > 0){
            if (map.containsKey(stackString.toString())) {
                outString.append(map.get(stackString.toString()));
                stackString.setLength(0);

            } else {
                outString.append("" + stackString.charAt(0));
                stackString.delete(0, 1);
            }

        }
    }

}

