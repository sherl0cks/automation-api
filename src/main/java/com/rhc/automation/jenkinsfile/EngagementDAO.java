package com.rhc.automation.jenkinsfile;

import com.rhc.automation.model.Application;
import com.rhc.automation.model.Engagement;
import com.rhc.automation.model.OpenShiftCluster;
import com.rhc.automation.model.Project;

import java.util.ArrayList;
import java.util.List;

public class EngagementDAO {

    public static Application getApplicationFromBuildProject( final Engagement engagement, final String applicationName ) {
        for ( OpenShiftCluster cluster : engagement.getOpenshiftClusters() ) {
            for ( Project project : cluster.getOpenshiftResources().getProjects() ) {
                if ( project.getEnvironmentType().equals( Project.EnvironmentTypeEnum.BUILD ) ) {
                    for ( Application app : project.getApps() ) {
                        if ( app.getName() != null && app.getName().equals( applicationName ) ) {
                            return app;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Project getBuildProjectForApplication( final Engagement engagement, final String applicationName ) {
        for ( OpenShiftCluster cluster : engagement.getOpenshiftClusters() ) {
            for ( Project project : cluster.getOpenshiftResources().getProjects() ) {
                if ( project.getEnvironmentType().equals( Project.EnvironmentTypeEnum.BUILD ) ) {
                    for ( Application app : project.getApps() ) {
                        if ( app.getName() != null && app.getName().equals( applicationName ) ) {
                            return project;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<Project> getPromotionProjectsForApplication( final Engagement engagement, final String applicationName ) {
        List<Project> projects = new ArrayList<>();

        for ( OpenShiftCluster cluster : engagement.getOpenshiftClusters() ) {
            for ( Project project : cluster.getOpenshiftResources().getProjects() ) {
                if ( project.getEnvironmentType().equals( Project.EnvironmentTypeEnum.PROMOTION ) ) {
                    for ( Application app : project.getApps() ) {
                        if ( app.getName() != null && app.getName().equals( applicationName ) ) {
                            projects.add( project );
                        }
                    }
                }
            }
        }
        return projects;
    }


    /**
     * There should only be a single build project declared across all clusters. It is invalid to have more than one.
     */
    public static OpenShiftCluster getClusterWithBuildProject( final Engagement engagement ) {
        for ( OpenShiftCluster cluster : engagement.getOpenshiftClusters() ) {
            for ( Project project : cluster.getOpenshiftResources().getProjects() ) {
                if ( project.getEnvironmentType().equals( Project.EnvironmentTypeEnum.BUILD ) ) {
                    return cluster;
                }
            }
        }
        return null;
    }

    public static OpenShiftCluster getBuildCluster( Engagement engagement ) {
        return engagement.getOpenshiftClusters().get( 0 );
    }
}
