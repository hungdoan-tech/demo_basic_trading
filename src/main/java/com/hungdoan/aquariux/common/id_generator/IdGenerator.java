package com.hungdoan.aquariux.common.id_generator;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class IdGenerator {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(IdGenerator.class);

    private BaseIdGenerator generator;

    @Autowired
    public IdGenerator(Environment environment) {

        String generatorStr = environment.getProperty("IdGenerator");

        if (generatorStr == null) {
            throw new IllegalStateException("Missing IdGenerator er.properties value");
        }

        LOG.info("Loading Id Generator " + generatorStr);

        BaseIdGenerator instance = null;
        switch (generatorStr) {

            case "tsrand20":
                instance = new TimestampRandom20IdGenerator();
                break;

            default:
                throw new IllegalStateException(
                        "Unknown IdGenerator value " + generatorStr);
        }

        generator = instance;
        LOG.info("Load complete Id Generator " + generator + " sample=" + generator.getId());
    }

    /**
     * @return a unique id
     */
    public String getId() {
        return generator.getId();
    }
}