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
package org.apache.sling.graphql.core.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.UUID;

import org.apache.sling.graphql.api.pagination.Cursor;
import org.junit.Test;

public class CursorTest {
    private final String testValue = UUID.randomUUID().toString();

    @Test
    public void testEncoding() {
        final String encoded = Cursor.encode(testValue);
        assertNotEquals(encoded, testValue);
        assertEquals(testValue, Cursor.decode(encoded));
        assertFalse("Encoded cursor should not contain raw value in clear text", encoded.contains(testValue));
    }

    private void assertWithValue(String value, boolean isEmpty) {
        final Cursor c = new Cursor(value);
        if(value == null) {
            value = "";
        }
        assertEquals("Expecting empty Cursor", isEmpty, c.isEmpty());
        assertEquals(isEmpty, c.toString().length() == 0);
        assertEquals(value, c.getRawValue());
        assertEquals(Cursor.encode(value), c.toString());
    }

    @Test
    public void testNonEmptyValue() {
        assertWithValue(testValue, false);
    }

    @Test
    public void testNullValue() {
        assertWithValue(null, true);
    }

    @Test
    public void testEmptyValue() {
        assertWithValue("", true);
    }
}
