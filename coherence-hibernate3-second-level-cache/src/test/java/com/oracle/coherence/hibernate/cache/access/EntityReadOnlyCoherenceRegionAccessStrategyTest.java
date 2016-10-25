/*
 * File: EntityReadOnlyCoherenceRegionAccessStrategyTest.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.coherence.hibernate.cache.access;

import com.oracle.coherence.hibernate.cache.region.CoherenceRegion;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.cache.access.SoftLock;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A EntityReadOnlyCoherenceRegionAccessStrategyTest is a test of EntityReadOnlyCoherenceAccessStrategy behavior.
 *
 * @author Randy Stafford
 */
public class EntityReadOnlyCoherenceRegionAccessStrategyTest
extends AbstractCoherenceRegionAccessStrategyTest
{


    // ---- Subclass responsibility

    /**
     * Return a new CoherenceRegion of the appropriate subtype.
     *
     * @return a CoherenceRegion of the appropriate subtype
     */
    protected CoherenceRegion newCoherenceRegion()
    {
        return newCoherenceEntityRegion();
    }

    /**
     * Return a new CoherenceRegion of the appropriate subtype.
     *
     * @return a CoherenceRegion of the appropriate subtype
     */
    protected CoherenceRegionAccessStrategy newCoherenceRegionAccessStrategy()
    {
        return newEntityReadOnlyCoherenceRegionAccessStrategy();
    }


    // ---- Test cases

    /**
     * Tests EntityReadOnlyCoherenceRegionAccessStrategy.getRegion().
     */
    @Test
    public void testGetRegion()
    {
        EntityRegion entityRegion = getEntityRegionAccessStrategy().getRegion();
        assertTrue("Expect instance of EntityRegion", entityRegion instanceof EntityRegion);
    }

    /**
     * Tests EntityReadOnlyCoherenceRegionAccessStrategy.insert().
     */
    @Test
    public void testInsert()
    {
        EntityRegionAccessStrategy accessStrategy = getEntityRegionAccessStrategy();

        Object key = "testInsert";
        Object value = "testInsert";
        Object version = null;

        boolean cacheWasModified = accessStrategy.insert(key, value, version);
        assertFalse("Expect no cache modification from read-only access strategy insert", cacheWasModified);
    }

    /**
     * Tests EntityReadOnlyCoherenceRegionAccessStrategy.afterInsert().
     */
    @Test
    public void testAfterInsert()
    {
        EntityRegionAccessStrategy accessStrategy = getEntityRegionAccessStrategy();

        Object key = "testAfterInsert";
        Object value = "testAfterInsert";
        Object version = null;

        boolean cacheWasModified = accessStrategy.afterInsert(key, value, version);
        assertTrue("Expect cache modification from read-only access strategy afterInsert", cacheWasModified);
        assertTrue("Expect cache to contain key after afterInsert", accessStrategy.getRegion().contains(key));
        assertEquals("Expect to get same value put", value, accessStrategy.get(key, System.currentTimeMillis()));
    }

    /**
     * Tests EntityReadOnlyCoherenceRegionAccessStrategy.update().
     */
    @Test
    public void testUpdate()
    {
        try
        {
            EntityRegionAccessStrategy accessStrategy = getEntityRegionAccessStrategy();

            Object key = "testUpdate";
            Object value = "testUpdate";
            Object currentVersion = null;
            Object previousVersion = null;

            accessStrategy.update(key, value, currentVersion, previousVersion);
            fail("Expect CacheException updating read-only access strategy");
        }
        catch (UnsupportedOperationException ex)
        {
            assertEquals("Expect writes not supported message", CoherenceRegionAccessStrategy.WRITE_OPERATIONS_NOT_SUPPORTED_MESSAGE, ex.getMessage());
        }
    }

    /**
     * Tests EntityReadOnlyCoherenceRegionAccessStrategy.afterUpdate().
     */
    @Test
    public void testAfterUpdate()
    {
        try
        {
            EntityRegionAccessStrategy accessStrategy = getEntityRegionAccessStrategy();

            Object key = "testAfterUpdate";
            Object value = "testAfterUpdate";
            Object currentVersion = null;
            Object previousVersion = null;
            SoftLock softLock = null;

            accessStrategy.afterUpdate(key, value, currentVersion, previousVersion, softLock);
            fail("Expect CacheException updating read-only access strategy");
        }
        catch (UnsupportedOperationException ex)
        {
            assertEquals("Expect writes not supported message", CoherenceRegionAccessStrategy.WRITE_OPERATIONS_NOT_SUPPORTED_MESSAGE, ex.getMessage());
        }
    }


}