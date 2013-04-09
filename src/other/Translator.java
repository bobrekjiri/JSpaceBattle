package other;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import app.Configuration;
import app.XmlHelper;

public class Translator {

    private static Translator translator;
    private String languageCode;
    private Map<String, String> translations;

    private Translator() {
        languageCode = Configuration.getInstance().get("language");
        init();
    };

    private void init() {
        try {
            loadTranslations();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void loadTranslations() throws Exception {
        translations = new HashMap<String, String>();
        File file = new File(String.format("content/translations/%s.xml", this.languageCode));

        Document document = XmlHelper.getDocument(file);
        NodeList nodes = document.getElementsByTagName("trans-unit");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element transUnit = (Element) nodes.item(i);
            String key = transUnit.getElementsByTagName("source").item(0).getTextContent();
            String value = transUnit.getElementsByTagName("target").item(0).getTextContent();
            translations.put(key, value);
        }
    }

    public static Translator getInstance() {
        if (Translator.translator == null) {
            Translator.translator = new Translator();
        }
        return Translator.translator;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguage(String code) {
        languageCode = code;
        init();
    }

    public String translate(String text) {
        return translations.containsKey(text) ? translations.get(text) : text;
    }
}
