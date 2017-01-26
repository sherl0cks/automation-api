package com.rhc.automation.jenkinsfile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GeneratorConstants {
    public static final Set<String> SUPPORTED_BUILD_TOOLS = new HashSet<String>( Arrays.asList("node-0.10", "node-4", "mvn-3", "sh", "s2i"));
}
