/*
 * File: CoherenceEntityRegion.java
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

package com.oracle.coherence.hibernate.cache.region;

import com.oracle.coherence.hibernate.cache.access.CoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.EntityNonstrictReadWriteCoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.EntityReadOnlyCoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.EntityReadWriteCoherenceRegionAccessStrategy;
import com.tangosol.net.NamedCache;
import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import java.util.Properties;

import static org.hibernate.cache.access.AccessType.*;

/**
 * An CoherenceEntityRegion is a CoherenceTransactionalDataRegion intended to cache Hibernate entities.
 *
 * @author Randy Stafford
 * @author mubin
 */
public class CoherenceEntityRegion
extends CoherenceTransactionalDataRegion
implements EntityRegion
{


    // ---- Constructors

    /**
     * Complete constructor.
     *
     * @param namedCache the NamedCache implementing this CoherenceEntityRegion
     * @param settings the Hibernate settings object
     * @param properties configuration properties for this CoherenceEntityRegion
     * @param metadata the description of the data in this CoherenceEntityRegion
     */
    public CoherenceEntityRegion(NamedCache namedCache, Settings settings, Properties properties, CacheDataDescription metadata)
    {
        super(namedCache, settings, properties, metadata);
    }


    // ---- interface org.hibernate.cache.EntityRegion

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        debugf("%s.buildAccessStrategy(%s)", this, accessType);

        if (NONSTRICT_READ_WRITE.equals(accessType)) {
            return new EntityNonstrictReadWriteCoherenceRegionAccessStrategy(this, getSettings());
        } else if (READ_ONLY.equals(accessType)) {
            return new EntityReadOnlyCoherenceRegionAccessStrategy(this, getSettings());
        } else if (READ_WRITE.equals(accessType)) {
            return new EntityReadWriteCoherenceRegionAccessStrategy(this, getSettings());
        } else if (TRANSACTIONAL.equals(accessType)) {
            throw new CacheException(CoherenceRegionAccessStrategy.TRANSACTIONAL_STRATEGY_NOT_SUPPORTED_MESSAGE);
        } else {
            throw new CacheException("Unknown AccessType: " + accessType);
        }

    }


}
