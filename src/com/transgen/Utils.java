package com.transgen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final char CONTROL_LIMIT = ' ';
    private static final char PRINTABLE_LIMIT = '\u007e';
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String repr(String source) {
        if (source == null) return null;
        else {
            final StringBuilder sb = new StringBuilder();
            final int limit = source.length();
            char[] hexbuf = null;
            int pointer = 0;
            sb.append('"');
            while (pointer < limit) {
                int ch = source.charAt(pointer++);
                switch (ch) {
                    case '\0':
                        sb.append("\\0");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    default:
                        if (CONTROL_LIMIT <= ch && ch <= PRINTABLE_LIMIT) sb.append((char) ch);
                        else {
                            sb.append("\\u");
                            if (hexbuf == null)
                                hexbuf = new char[4];
                            for (int offs = 4; offs > 0; ) {

                                hexbuf[--offs] = HEX_DIGITS[ch & 0xf];
                                ch >>>= 4;
                            }
                            sb.append(hexbuf, 0, 4);
                        }
                }
            }
            return sb.append('"').toString();
        }
    }

    public static void printCharacters(String str) {
        int len = str.length();
        Map<Character, Integer> numChars = new HashMap<Character, Integer>(Math.min(len, 26));

        for (int i = 0; i < len; ++i) {
            char charAt = str.charAt(i);

            if (!numChars.containsKey(charAt)) {
                numChars.put(charAt, 1);
            } else {
                numChars.put(charAt, numChars.get(charAt) + 1);
            }
        }

        for (Character s : numChars.keySet()) {
            System.out.println(s + " " + numChars.get(s));
        }
    }

    public static boolean delete(File file) {
        File[] flist = null;
        if (file == null) return false;
        if (file.isFile()) return file.delete();
        if (!file.isDirectory()) return false;

        flist = file.listFiles();
        if (flist != null && flist.length > 0) {
            for (File f : flist) {
                if (!delete(f)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
