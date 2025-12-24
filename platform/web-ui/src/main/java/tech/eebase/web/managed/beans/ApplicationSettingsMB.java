package tech.eebase.web.managed.beans;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("appSettings")
@ApplicationScoped
public class ApplicationSettingsMB implements Serializable {

    private static final long serialVersionUID = 5986785944158034116L;

    public String getSystemName() {
        return "EEBase"; // TODO Need to make system setting
    }
}
