package tech.eebase.web.managed.beans.pub;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class IndexPage {

    public String getName() {
        return "test";
    }
}
