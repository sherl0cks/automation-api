package com.rhc.automation.jenkinsfile;

import com.rhc.automation.model.Application;
import com.rhc.automation.model.Engagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleasePipelineGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleasePipelineGenerator.class);
    private static final PipelineDialect DEFAULT_DIALECT = PipelineDialect.JENKINSFILE_ORIGINAL;

    public static String generate( Engagement engagement, String applicationName) {
        return ReleasePipelineGenerator.generate(engagement, applicationName, DEFAULT_DIALECT);
    }

    public static String generate(Engagement engagement, String applicationName, PipelineDialect dialect) {
        StringBuilder script = new StringBuilder();
        ReleasePipelineGenerator.validateInput(engagement, applicationName);
        ReleasePipelineDialectSpecificGenerator dialectSpecificGenerator = getDialectSpecificGenerator(dialect);

        script.append(dialectSpecificGenerator.initializeScript());
        script.append(dialectSpecificGenerator.generateCodeCheckoutStage(engagement, applicationName));
        script.append(dialectSpecificGenerator.generateBuildAppStage(engagement, applicationName));
        script.append(dialectSpecificGenerator.generateBuildImageAndDeployToDevStage(engagement, applicationName));
        script.append(dialectSpecificGenerator.generateAllPromotionStages(engagement, applicationName));
        script.append(dialectSpecificGenerator.finalizeScript());

        String finalScript = script.toString();
        LOGGER.debug(finalScript);

        return finalScript;
    }

    private static void validateInput(final Engagement engagement, final String applicationName) {
        if (applicationName == null || applicationName.isEmpty()) {
            throw new RuntimeException("applicationName cannot be null or empty");
        }
        Application app = EngagementDAO.getApplicationFromBuildProject(engagement, applicationName);
        if (app == null) {
            throw new RuntimeException(String.format("Unable to find application '%s' in your Engagement object. Double check your data and configuration. Be aware that search is currently case sensitive.", applicationName));
        }
        if (app.getBuildTool() == null || app.getBuildTool().isEmpty()) {
            throw new RuntimeException("A build tool must be set for the application. Currently support tools are: "
                    + GeneratorConstants.SUPPORTED_BUILD_TOOLS);
        }
    }

    private static ReleasePipelineDialectSpecificGenerator getDialectSpecificGenerator(PipelineDialect dialect) {
        switch (dialect) {
            case JENKINSFILE_DECLARATIVE:
                return new ReleasePipelineDeclarativeJenkinsfileGenerator();
            case JENKINSFILE_ORIGINAL:
                return new ReleasePipelineOriginalJenkinsfileGenerator();
            default:
                return new ReleasePipelineOriginalJenkinsfileGenerator();
        }
    }
}
