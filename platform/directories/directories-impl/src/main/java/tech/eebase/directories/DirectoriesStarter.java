package tech.eebase.directories;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;

@Singleton
@Startup
public class DirectoriesStarter {
    
    private static final Logger LOG = LoggerFactory.getLogger(DirectoriesStarter.class);

    @PersistenceContext(unitName = "eebase-main-pu")
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        LOG.info("Directores module init started!!!");
        
        Set<EntityType<?>> entities = em.getMetamodel().getEntities();
        for (EntityType<?> entity : entities) { 
            LOG.info("**** {}", entity.getJavaType());
        }
        
        
//        instances.forEach(instance -> {
//            LOG.info("Found: " + instance.getClass().getName());
//        });
        
//        BeanManager beanManager = CDI.current().getBeanManager();
//        @SuppressWarnings("serial")
//        Set<Bean<?>> beans = beanManager.getBeans(Object.class, new DirectoryAnnotation() {});
//        for (Bean<?> bean : beans) {
//            LOG.info("Found bean {} marked as a Dictionary", bean.getBeanClass());
//        }
    }
}
