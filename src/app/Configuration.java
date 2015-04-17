package app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

    private final String configPath = "content/config.xml";
    private static Configuration configuration;
    private Map<String, String> properties;

    private Configuration() {
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            Document document = getDocument();
            NodeList nodeList = document.getElementsByTagName("property");
            properties = new HashMap<String, String>(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                properties.put(key, node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChanges() {
        try {
            Document document = getDocument();
            NodeList nodeList = document.getElementsByTagName("property");

            for (String key : properties.keySet()) {
                putIntoNodeList(nodeList, key, properties.get(key));
            }

            XmlHelper.saveDocument(document, configPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putIntoNodeList(NodeList nodeList, String configName, String property) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.hasAttributes() && node.getAttributes().getNamedItem("name") != null) {
                if (node.getAttributes().getNamedItem("name").getNodeValue().equals(configName)) {
                    node.setTextContent(property);
                    break;
                }
            }

        }
    }

    public void set(String configName, String configValue) {
        if (properties.containsKey(configName)) {
            properties.remove(configName);
        }
        properties.put(configName, configValue);
    }

    private Document getDocument() throws Exception {
        File file = new File(configPath);
        if (!file.exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element configuration = document.createElement("configuration");
            Element properties = document.createElement("properties");
            for (Entry<String, String> entry : getDefaultProperties().entrySet()) {
                Element property = document.createElement("property");
                property.setAttribute("name", entry.getKey());
                property.setTextContent(entry.getValue());
                properties.appendChild(property);
            }
            configuration.appendChild(properties);
            document.appendChild(configuration);
            XmlHelper.saveDocument(document, configPath);
            return document;
        }
        return XmlHelper.getDocument(file);
    }

    private Map<String, String> getDefaultProperties() {
        HashMap<String, String> properties = new HashMap<String, String>(4);
        properties.put("language", "en");
        properties.put("width", "0");
        properties.put("height", "0");
        properties.put("fullscreen", "true");
        return properties;
    }

    public String get(String configName) {
        String property = properties.get(configName);

        if (property == null) {
            try {
                String propertyValue = getDefaultProperties().get(configName);
                Document document = getDocument();
                NodeList properties = document.getElementsByTagName("properties");

                Element prop = document.createElement("property");
                prop.setAttribute("name", configName);
                prop.setTextContent(propertyValue);

                properties.item(0).appendChild(prop);
                XmlHelper.saveDocument(document, configPath);
                loadConfiguration();
                property = this.properties.get(configName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return property;
    }

    public static Configuration getInstance() {
        if (Configuration.configuration == null) {
            Configuration.configuration = new Configuration();
        }
        return Configuration.configuration;
    }
}
