/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sling.graphql.core.mocks;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.apache.sling.api.resource.Resource;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestDataFetcher implements DataFetcher<Object> {

    private final Resource r;
    private final String algorithm;
    private final String source;

    DigestDataFetcher(Resource r, String options, String source) {
        this.r = r;
        this.algorithm = options;
        this.source = source;
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {
        String rawValue = null;
        if ("path".equals(source)) {
            rawValue = r.getPath();
        } else if("resourceType".equals(source)) {
            rawValue = r.getResourceType();
        }

        String digest = null;
        try {
            digest = computeDigest(algorithm, rawValue);
        } catch (Exception e) {
            throw new RuntimeException("Error computing digest:" + e, e);
        }

        return algorithm + "#" + source + "#" + digest;
    }

    public static String toHexString(byte[] data) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static String computeDigest(String algorithm, String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(value.getBytes("UTF-8"));
        return toHexString(md.digest());
    }
}
