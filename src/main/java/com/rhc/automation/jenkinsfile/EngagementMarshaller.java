/*
 * Copyright (C) 2016 Original Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rhc.automation.jenkinsfile;

import com.rhc.automation.model.Engagement;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class EngagementMarshaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementMarshaller.class);
    //private static final OkHttpClient client = new OkHttpClient();

    public static Engagement getEngagementFromFileOnClasspath(final String fileName) {
        InputStream is = EngagementMarshaller.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new RuntimeException("Could not find the specified file: " + fileName);
        }
        Engagement engagement = getEngagementFromInputStream(is);
        return engagement;
    }

    public static Engagement getEngagementFromFileInWorkingDirectory(final String fileName) {
        Path p = FileSystems.getDefault().getPath(fileName);
        LOGGER.debug(p.toString());
        InputStream is;
        try {
            is = Files.newInputStream(p);
        } catch (IOException e) {
            throw new RuntimeException("Could not find the specified file: " + fileName);
        }
        Engagement engagement = getEngagementFromInputStream(is);
        return engagement;
    }

//    public static Engagement getEngagementFromUrl(final String url) {
//        Request request = new Request.Builder().url(url).build();
//        try {
//            Response response = client.newCall(request).execute();
//            if (response.code() != 200) {
//                throw new RuntimeException("The http configuration returned a status code of " + response.code()
//                        + ". We only support 200. Here is the response message: " + response.message());
//            } else {
//                InputStream is = response.body().byteStream();
//                Engagement engagement = getEngagementFromInputStream(is);
//                return engagement;
//            }
//        } catch (java.io.IOException e) {
//            throw new RuntimeException("Could not open the specified url: " + url + "\n\n" + e.getStackTrace());
//        }
//    }

    public static Engagement getEngagementFromInputStream(final InputStream stream) {
        String fileContents = null;
        try {
            fileContents = IOUtils.toString(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug(fileContents);
        Engagement engagement = null;// = new GsonBuilder().create().fromJson(fileContents, Engagement.class);
        return engagement;
    }
}
