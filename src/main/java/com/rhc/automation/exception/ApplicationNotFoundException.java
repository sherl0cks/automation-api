package com.rhc.automation.exception;

public class ApplicationNotFoundException extends Exception {
    private String applicationName;

    public ApplicationNotFoundException(String applicationName) {
        super( String.format( "Unable to find application '%s' in your Engagement object. Double check your data and configuration. Be aware that search is currently case sensitive.", applicationName) );
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
