package tech.eebase.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EEBaseResourceBundle extends ResourceBundle {
    static final Logger LOG = LoggerFactory.getLogger(EEBaseResourceBundle.class);

    Map<String, Map<String, Object>> resources;
    
    public EEBaseResourceBundle() {
        resources = new HashMap<>();
        loadResourceFile("ru", "/i18n/messages-utf8-ru.txt");
        loadResourceFile("en", "/i18n/messages-utf8-en.txt");
    }

    private void loadResourceFile(String lang, String resourceName) {
        try {
            HashMap<String, Object> langResources = loadResourceFile(resourceName);
            LOG.info("Loaded lang {} from {}", lang, resourceName);
            resources.put(lang, langResources);
        } catch (Exception e) {
            LOG.error("Cannot load " + lang + "-messages resource (" + resourceName + ")", e);
        }
    }
    
    private HashMap<String, Object> loadResourceFile(String resourceName) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        
        Pattern patt = Pattern.compile("([^=]*)=([^=]*)");
        
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8.name())) {
            try (BufferedReader br = new BufferedReader(isr)) {
                String line = br.readLine();
                while (line != null) {
                    line = line.trim();
                    
                    if (!line.isEmpty() && line.charAt(0) != '#') {
                        Matcher m = patt.matcher(line);
                        if (m.matches()) {
                            String key = m.group(1);
                            String value = m.group(2);
                            result.put(key, value);
                        }
                    }
                    
                    line = br.readLine();
                }
            }
        }
        
        return result;
    }
    
    @Override
    protected Object handleGetObject(String key) {
        String language = getCurrentLanguage();
        LOG.debug("getResource {} {}", key, language);
        Map<String, Object> langResources = resources.get(language);
        if (langResources == null) {
            LOG.warn("No resources language '{}'", language);
            return key;
        }
        Object resource = langResources.get(key);
        if (resource == null) {
            LOG.warn("No resource for key '{}' and language '{}'", key, language);
            return key;
        }
        
        return resource;
    }

    @Override
    public Enumeration<String> getKeys() {
        String language = getCurrentLanguage();
        LOG.debug("getResourcesKeys {} ", language);
        
        Map<String, Object> langResources = resources.get(language);
        if (langResources == null) {
            return Collections.emptyEnumeration();
        }
            
        return Collections.enumeration(langResources.keySet());
    }
    
    private String getCurrentLanguage() {
        return "en"; // TODO Need to calculate value according with user settings
    }

}
