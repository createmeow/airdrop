package xaero.hud.util;

import java.text.ParseException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.function.Predicate;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/util/SeparatedKeysParser.class */
public class SeparatedKeysParser {
    private Predicate<Character> isSeparator;

    public SeparatedKeysParser(Predicate<Character> isSeparator) {
        this.isSeparator = isSeparator;
    }

    public String[] parseKeys(String keysString) throws ParseException {
        StringCharacterIterator sci = new StringCharacterIterator(keysString);
        StringBuilder keyBuilder = new StringBuilder(64);
        ArrayList<String> keysBuilder = new ArrayList<>();
        Predicate<Character> isSeparator = this.isSeparator;
        boolean findSeparator = false;
        while (true) {
            char c = sci.current();
            if (c == 65535) {
                break;
            }
            if (keyBuilder.length() != 0 || c != ' ') {
                if (isSeparator.test(Character.valueOf(c))) {
                    if (!findSeparator) {
                        keysBuilder.add(keyBuilder.toString());
                        keyBuilder.setLength(0);
                    }
                    findSeparator = false;
                } else {
                    if (findSeparator) {
                        throwError(c, sci.getIndex(), keysString);
                    }
                    if (c == '\'') {
                        if (keyBuilder.length() != 0) {
                            throwError('\'', sci.getIndex(), keysString);
                        }
                        sci.next();
                        keysBuilder.add(parseKeyUntilChar(keyBuilder, sci, t -> {
                            return t.charValue() == '\'';
                        }, keysString));
                        keyBuilder.setLength(0);
                        findSeparator = true;
                    } else {
                        keyBuilder.append(c);
                    }
                }
            }
            sci.next();
        }
        if (keyBuilder.length() > 0) {
            keysBuilder.add(keyBuilder.toString());
        }
        return (String[]) keysBuilder.toArray(new String[0]);
    }

    private String parseKeyUntilChar(StringBuilder keyBuilder, StringCharacterIterator sci, Predicate<Character> isEnd, String keysString) throws ParseException {
        char c;
        keyBuilder.setLength(0);
        while (true) {
            c = sci.current();
            if (c == 65535) {
                break;
            }
            if (c == '\\') {
                keyBuilder.append(sci.next());
            } else {
                if (isEnd.test(Character.valueOf(c))) {
                    break;
                }
                keyBuilder.append(c);
            }
            sci.next();
        }
        if (!isEnd.test(Character.valueOf(c))) {
            throwError(c, sci.getIndex(), keysString);
        }
        return keyBuilder.toString();
    }

    private void throwError(char unexpected, int position, String keysString) throws ParseException {
        throw new ParseException(String.format("Unexpected \"%s\" at position %d in \"%s\"!", Character.valueOf(unexpected), Integer.valueOf(position), keysString), position);
    }
}
