package corendonlmsv2.connectivity;

import corendonlmsv2.main.util.StringUtil;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Eases internalization string translation lookups by providing a simple
 * controller to handle string requests
 *
 * @author Emile Pels
 */
public final class LanguageController
{

    /**
     * The file name for the resource bundle containing the translated strings
     */
    private static final String BUNDLE_FILENAME = "MessagesBundle";
    
    private static ResourceBundle bundle;

    //Class should be used solely as a global, static controller
    private LanguageController()
    {
    }

    /**
     * 
     * @param language 
     */
    public static void setLanguage(Languages language)
    {
        Locale locale;
        if (language == null || (locale = language.getLocale()) == null)
        {
            throw new IllegalArgumentException("Language can not be null.");
        }

        bundle = ResourceBundle.getBundle(BUNDLE_FILENAME, locale);
    }
    
    /**
     * Gets a string for the given key from the resource bundle specified in
     * the language controller. The controller's language should be set through
     * calling setLanguage prior to calling getString; if this did not happen,
     * no exceptions are thrown and the language is implicitly set to English
     * 
     * @param key The key for the desired string
     * @return The string for the given key
     */
    public static String getString(String key)
    {
        if (bundle == null) 
        {
            setLanguage(Languages.English);
        }
        
        return bundle.getString(key);
    }

    /**
     * Languages enumeration to create a locale for the language controller
     */
    public enum Languages
    {

        English("en", "US"),
        Nederlands("nl", "NL");

        private final String language, country;

        /**
         * Creates a new Languages instance
         *
         * @param language General language
         * @param country Country for this language
         */
        private Languages(String language, String country)
        {
            this.language = language;
            this.country = country;
        }
        
        /**
         * Constructs a new locale based on the language and country fields
         *
         * @return A new locale based on the language and country fields
         */
        private Locale getLocale()
        {
            //If the language or country is null or whitespace,
            //return the default locale; if not, construct a new instance
            //from said fields
            return StringUtil.isStringNullOrWhiteSpace(language)
                    || StringUtil.isStringNullOrWhiteSpace(country)
                    ? Locale.getDefault()
                    : new Locale(language, country);
        }
    }
}
