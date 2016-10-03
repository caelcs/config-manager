package uk.co.caeldev.config.manager.api.buildConfig.tests;

import uk.co.caeldev.config.manager.api.buildConfig.BuildConfig;

import java.util.Map;

import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.string;

public class BuildConfigBuilder {

    private Map<String, String> attributes = map(string(), string()).next();

    BuildConfigBuilder() {
    }

    public static BuildConfigBuilder buildConfigBuilder() {
        return new BuildConfigBuilder();
    }

    public BuildConfig build() {
        BuildConfig buildConfig = new BuildConfig();
        buildConfig.setAttributes(attributes);
        return buildConfig;
    }
}
