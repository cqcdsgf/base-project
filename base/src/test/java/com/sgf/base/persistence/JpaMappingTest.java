package com.sgf.base.persistence;

import com.sgf.base.BaseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class JpaMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

    @PersistenceContext
    private EntityManager em;

    @Test
    public void allClassMapping() throws Exception {
        Metamodel model = em.getEntityManagerFactory().getMetamodel();
        assertThat(model.getEntities()).as("No entity mapping found").isNotEmpty();

        for (EntityType entityType : model.getEntities()) {
            String entityName = entityType.getName();
            em.createQuery("select o from " + entityName + " o").getResultList();
            logger.info("ok: " + entityName);
        }
    }

}
