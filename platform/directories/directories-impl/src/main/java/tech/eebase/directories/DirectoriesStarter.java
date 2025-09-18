package tech.eebase.directories;

import java.lang.reflect.Modifier;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import tech.eebase.directories.annotations.Directory;

@Singleton
@Startup
public class DirectoriesStarter {
    
    private static final Logger LOG = LoggerFactory.getLogger(DirectoriesStarter.class);

    @PersistenceContext(unitName = "eebase-main-pu")
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        LOG.debug("Directores module init started");
        
        Set<EntityType<?>> entities = em.getMetamodel().getEntities();
        for (EntityType<?> entity : entities) {
            Class<?> entityClass = entity.getJavaType();
            if (DirectoryBase.class.isAssignableFrom(entityClass)) {
                if (!Modifier.isAbstract(entityClass.getModifiers())) {
                    Directory directoryAnnotation = entityClass.getAnnotation(Directory.class);
                    if (directoryAnnotation != null) {
                        LOG.info("Found directory class {}", entityClass.getName());
                    } else {
                        LOG.warn("Found class {} child of {} but has no annotation {}", 
                                entityClass.getName(),
                                DirectoryBase.class.getName(),
                                Directory.class.getName());
                    }
                } else {
                    LOG.debug("Found class {} child of {} but class is abstract, base for other directories", 
                            entityClass.getName(),
                            DirectoryBase.class.getName());
                }
            }
        }
        
        LOG.info("Directores module init finished");
    }
}
