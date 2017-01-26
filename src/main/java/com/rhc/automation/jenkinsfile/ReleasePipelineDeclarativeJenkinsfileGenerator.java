package com.rhc.automation.jenkinsfile;

import com.rhc.automation.model.Application;
import com.rhc.automation.model.Engagement;
import com.rhc.automation.model.OpenShiftCluster;
import com.rhc.automation.model.Project;

public class ReleasePipelineDeclarativeJenkinsfileGenerator implements ReleasePipelineDialectSpecificGenerator {
    @Override
    public String initializeScript() {
        StringBuilder script = new StringBuilder();
        script.append( "pipeline {\n" );
        script.append( "  agent any\n" );
        script.append( "  stages {\n\n" );
        return script.toString();
    }

    @Override
    public String generateCodeCheckoutStage( Engagement engagement, String applicationName ) {
        StringBuilder script = new StringBuilder();
        script.append( "    stage ('Code Checkout') {\n" );
        script.append( "       steps {\n" );
        script.append( String.format( "         %s\n", CommonJenkinsfileScriptsGenerator.createCodeCheckoutScript() ) );
        script.append( "       }\n" );
        script.append( "    }\n\n" );
        return script.toString();
    }

    @Override
    public String generateBuildAppStage( Engagement engagement, String applicationName ) {
        StringBuilder script = new StringBuilder();
        Application app = EngagementDAO.getApplicationFromBuildProject( engagement, applicationName );

        // s2i builds here assume there is no app build, only a container image build
        if ( app.getBuildTool().equals( "s2i" ) ) {
            return script.toString();
        }

        script.append( "    stage ('Build App') {\n" );
        script.append( "      steps {\n" );
        script.append( "        script {\n" );
        script.append( CommonJenkinsfileScriptsGenerator.createBuildAppScript( app ) );
        script.append( "        }\n" );
        script.append( "      }\n" );

        script.append( "    }\n\n" );
        return script.toString();
    }

    @Override
    public String generateBuildImageAndDeployToDevStage( Engagement engagement, String applicationName ) {
        StringBuilder script = new StringBuilder();
        script.append( "    stage ('Build Image and Deploy to Dev') {\n" );
        script.append( "      steps {\n" );
        script.append( String.format( "      %s", CommonJenkinsfileScriptsGenerator.createBuildImageScript( engagement, applicationName ) ) );
        script.append( "      }\n" );
        script.append( "    }\n\n" );
        return script.toString();
    }

    @Override
    public String generateAllPromotionStages( Engagement engagement, String applicationName ) {
        StringBuilder script = new StringBuilder();
        OpenShiftCluster srcCluster = EngagementDAO.getBuildCluster( engagement );
        Project srcProject = EngagementDAO.getBuildProjectForApplication( engagement, applicationName );

        for ( OpenShiftCluster cluster : engagement.getOpenshiftClusters() ) {
            for ( Project project : EngagementDAO.getPromotionProjectsForApplication( engagement, applicationName ) ) {
                script.append( "    stage ('Deploy to " ).append( project.getName() ).append( "') {\n" );
                script.append( "      steps {\n" );
                script.append( String.format( "      %s", CommonJenkinsfileScriptsGenerator.createPromotionStageScript( engagement, cluster, project, srcCluster, srcProject, applicationName ) ) );
                script.append( "      }\n" );
                script.append( "    }\n\n" );
                srcProject = project;
            }
            srcCluster = cluster;
        }
        return script.toString();
    }

    @Override
    public String finalizeScript() {
        StringBuilder script = new StringBuilder();
        script.append( "  }\n" );
        script.append( "}\n" );
        return script.toString();
    }
}
