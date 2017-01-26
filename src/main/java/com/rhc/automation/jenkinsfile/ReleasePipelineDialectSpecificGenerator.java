package com.rhc.automation.jenkinsfile;

import com.rhc.automation.model.Engagement;

public interface ReleasePipelineDialectSpecificGenerator {
    String initializeScript();

    String generateCodeCheckoutStage( final Engagement engagement, final String applicationName );

    String generateBuildAppStage( final Engagement engagement, final String applicationName );

    String generateBuildImageAndDeployToDevStage( final Engagement engagement, final String applicationName );

    String generateAllPromotionStages( final Engagement engagement, final String applicationName );

    String finalizeScript();
}
