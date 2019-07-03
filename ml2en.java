import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java port of ml2en.js
 * ml2en.js Copyright Kailash Nadh [GPL-2.0]
 * https://github.com/knadh/ml2en
 *
 * ml2en.java Copyright Subin Siby [GPL-3.0]
 * https://github.com/subins2000/ml2en
 */
public class ml2en {

    Map<String, String> _vowels = new HashMap<String, String>();
    Map<String, String> _compounds = new HashMap<String, String>();
    Map<String, String> _consonants = new HashMap<String, String>();
    Map<String, String> _chil = new HashMap<String, String>();
    Map<String, String> _modifiers = new HashMap<String, String>();

    public void initVars() {
        _vowels.put("അ", "a");
        _vowels.put("ആ", "aa");
        _vowels.put("ഇ", "i");
        _vowels.put("ഈ", "ee");
        _vowels.put("ഉ", "u");
        _vowels.put("ഊ", "oo");
        _vowels.put("ഋ", "ru");
        _vowels.put("എ", "e");
        _vowels.put("ഏ", "e");
        _vowels.put("ഐ", "ai");
        _vowels.put("ഒ", "o");
        _vowels.put("ഓ", "o");
        _vowels.put("ഔ", "au");

        _compounds.put("ക്ക", "kk");
        _compounds.put("ഗ്ഗ", "gg");
        _compounds.put("ങ്ങ", "ng");
        _compounds.put("ക്ക", "kk");
        _compounds.put("ച്ച", "cch");
        _compounds.put("ജ്ജ", "jj");
        _compounds.put("ഞ്ഞ", "nj");
        _compounds.put("ട്ട", "tt");
        _compounds.put("ണ്ണ", "nn");
        _compounds.put("ത്ത", "tth");
        _compounds.put("ദ്ദ", "ddh");
        _compounds.put("ദ്ധ", "ddh");
        _compounds.put("ന്ന", "nn");
        _compounds.put("ന്ത", "nth");
        _compounds.put("ങ്ക", "nk");
        _compounds.put("ണ്ട", "nd");
        _compounds.put("ബ്ബ", "bb");
        _compounds.put("പ്പ", "pp");
        _compounds.put("മ്മ", "mm");
        _compounds.put("യ്യ", "yy");
        _compounds.put("ല്ല", "ll");
        _compounds.put("വ്വ", "vv");
        _compounds.put("ശ്ശ", "sh");
        _compounds.put("സ്സ", "s");
        _compounds.put("ക്സ", "ks");
        _compounds.put("ഞ്ച", "nch");
        _compounds.put("ക്ഷ", "ksh");
        _compounds.put("മ്പ", "mp");
        _compounds.put("റ്റ", "tt");
        _compounds.put("ന്റ", "nt");
        _compounds.put("ന്ത", "nth");
        _compounds.put("ന്ത്യ", "nthy");

        _consonants.put("ക", "k");
        _consonants.put("ഖ", "kh");
        _consonants.put("ഗ", "g");
        _consonants.put("ഘ", "gh");
        _consonants.put("ങ", "ng");
        _consonants.put("ച", "ch");
        _consonants.put("ഛ", "chh");
        _consonants.put("ജ", "j");
        _consonants.put("ഝ", "jh");
        _consonants.put("ഞ", "nj");
        _consonants.put("ട", "t");
        _consonants.put("ഠ", "dt");
        _consonants.put("ഡ", "d");
        _consonants.put("ഢ", "dd");
        _consonants.put("ണ", "n");
        _consonants.put("ത", "th");
        _consonants.put("ഥ", "th");
        _consonants.put("ദ", "d");
        _consonants.put("ധ", "dh");
        _consonants.put("ന", "n");
        _consonants.put("പ", "p");
        _consonants.put("ഫ", "ph");
        _consonants.put("ബ", "b");
        _consonants.put("ഭ", "bh");
        _consonants.put("മ", "m");
        _consonants.put("യ", "y");
        _consonants.put("ര", "r");
        _consonants.put("ല", "l");
        _consonants.put("വ", "v");
        _consonants.put("ശ", "sh");
        _consonants.put("ഷ", "sh");
        _consonants.put("സ", "s");
        _consonants.put("ഹ", "h");
        _consonants.put("ള", "l");
        _consonants.put("ഴ", "zh");
        _consonants.put("റ", "r");

        _chil.put("ൽ", "l");
        _chil.put("ൾ", "l");
        _chil.put("ൺ", "n");
        _chil.put("ൻ", "n");
        _chil.put("ർ", "r");
        _chil.put("ൿ", "k");

        _modifiers.put("ു്", "u");
        _modifiers.put("ാ", "aa");
        _modifiers.put("ി", "i");
        _modifiers.put("ീ", "ee");
        _modifiers.put("ു", "u");
        _modifiers.put("ൂ", "oo");
        _modifiers.put("ൃ", "ru");
        _modifiers.put("െ", "e");
        _modifiers.put("േ", "e");
        _modifiers.put("ൈ", "y");
        _modifiers.put("ൊ", "o");
        _modifiers.put("ോ", "o");
        _modifiers.put("ൌ", "ou");
        _modifiers.put("ൗ", "au");
        _modifiers.put("ഃ", "a");
    }

    // ______ transliterate a malayalam string to english phonetically
    String transliterate(String input) {
        // replace zero width non joiners
        input = input.replace("/[\u200B-\u200D\uFEFF]/g", "");

        // replace modified compounds first
        input = _replaceModifiedGlyphs(_compounds, input);

        // replace modified non-compounds
        input = _replaceModifiedGlyphs(_vowels, input);
        input = _replaceModifiedGlyphs(_consonants, input);

        String k = "", v = "";

        // replace unmodified compounds
        for (Map.Entry<String, String> pair : _compounds.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k + "്([\\w])", Pattern.UNICODE_CASE).matcher(input).replaceAll(v + "$1" );	// compounds ending in chandrakkala but not at the end of the word
            input = Pattern.compile(k + "്", Pattern.UNICODE_CASE).matcher(input).replaceAll(v + "u" );	// compounds ending in chandrakkala have +'u' pronunciation
            input = Pattern.compile(k, Pattern.UNICODE_CASE).matcher(input).replaceAll(v + "a" );	// compounds not ending in chandrakkala have +'a' pronunciation
        }

        // glyphs not ending in chandrakkala have +'a' pronunciation
        for (Map.Entry<String, String> pair : _consonants.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k + "(?!്)", Pattern.UNICODE_CASE).matcher(input).replaceAll(v + "a" );
        }

        // glyphs ending in chandrakkala not at the end of a word
        for (Map.Entry<String, String> pair : _consonants.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k + "്(?![\\s).;,\"'\\/])", Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE).matcher(input).replaceAll(v);
        }

        // remaining glyphs ending in chandrakkala will be at end of words and have a +'u' pronunciation
        for (Map.Entry<String, String> pair : _consonants.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k + "്", Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE).matcher(input).replaceAll(v + "u" );
        }

        // remaining consonants
        for (Map.Entry<String, String> pair : _consonants.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k, Pattern.UNICODE_CASE).matcher(input).replaceAll(v);
        }

        // vowels
        for (Map.Entry<String, String> pair : _vowels.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k, Pattern.UNICODE_CASE).matcher(input).replaceAll(v);
        }

        // chillu glyphs
        for (Map.Entry<String, String> pair : _chil.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k, Pattern.UNICODE_CASE).matcher(input).replaceAll(v);
        }

        // anusvaram 'am' at the end
        input = input.replaceAll("ം", "m");

        // replace any stray modifiers that may have been left out
        for (Map.Entry<String, String> pair : _modifiers.entrySet()) {
            k = pair.getKey();
            v = pair.getValue();

            input = Pattern.compile(k, Pattern.UNICODE_CASE).matcher(input).replaceAll(v);
        }

        // capitalize first letter of sentences for better aeshetics
        input = capitalizeFirstLetterInEverySentence(input);

        return input;
    }

    // ______ replace modified glyphs
    String _replaceModifiedGlyphs(Map glyphs, String input) {
        // see if a given set of glyphs have modifiers trailing them
        Matcher match;
        Pattern re = Pattern.compile("(" + String.join("|", _getKeys(glyphs)) + ")(" + String.join("|", _getKeys(_modifiers)) + ")", Pattern.UNICODE_CASE);

        // if yes, replace the glpyh with its roman equivalent, and the modifier with its

        match = re.matcher(input);

        while (match.find()) {
            input = Pattern.compile(match.group(0), Pattern.UNICODE_CASE).matcher(input).replaceAll(glyphs.get(match.group(1)) + _modifiers.get(match.group(2)));
        }

        return input;
    }

    // ______ get the keys of an object literal
    String[] _getKeys(Map<String, String> o) {
        Set<String> keys = o.keySet();
        String[] keysStr = keys.toArray(new String[keys.size()]);
        return keysStr;
    }

    /**
     * https://tech.chitgoks.com/2010/09/24/capitalize-first-letter-of-every-sentence-in-java/
     * @param content Input
     * @return Capitalized output
     */
    public static String capitalizeFirstLetterInEverySentence(String content) {
        Pattern capitalize = Pattern.compile("([\\?!\\.]\\s*)([a-z])");
        Matcher m = capitalize.matcher(content);
        while (m.find()) {
            content = m.replaceFirst(m.group(1) + m.group(2).toUpperCase());
            m = capitalize.matcher(content);
        }

        // Capitalize the first letter of the string.
        content = String.format("%s%s", Character.toUpperCase(content.charAt(0)), content.substring(1));

        return content;
    }

    public ml2en() {
        initVars();
    }

    public String convert(String input) {
        return transliterate(input);
    }

}
