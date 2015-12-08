/*
 * Copyright (c) 2013-2015 Cinchapi Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cinchapi.concourse.importer;

import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.cinchapi.concourse.Concourse;
import com.cinchapi.concourse.util.Convert;
import com.cinchapi.concourse.util.Convert.ResolvableLink;
import com.google.common.collect.Multimap;

/**
 * Base class for an importer to bring data from flat files into Concourse. The
 * subclass must implement the {@link #importFile(String)} method. A
 * {@link #concourse client connection} to Concourse is provided so that the
 * implementation can choose the specific methods for optimally importing the
 * data from the file.
 * <p>
 * This framework does not mandate anything about the structure of the raw data
 * so it is possible to use this implementation to handle things like CSV files,
 * XML files, SQL dumps, email documents, etc.
 * </p>
 * <h2>Import into new record</h2>
 * <p>
 * By default, all the data is imported into new records.
 * </p>
 * <h2>Import into existing record(s)</h2>
 * <p>
 * It is possible to import all the data in a group into one or more existing
 * records by specifying a <strong>resolveKey</strong> in the
 * {@link #doImport(Concourse, Multimap, String)} method.
 * </p>
 * <p>
 * For each group, the importer will find all the records that have at least one
 * of the same values mapped from {@code resolveKey} as defined in the group
 * data. The importer will then import all the group data into all of those
 * record.
 * </p>
 * <p>
 * <strong>NOTE:</strong> It is not possible to specify the Primary Key of a
 * record as the resolveKey. The Primary Key is metadata which isn't necessarily
 * known to the raw data. Therefore, we prefer that record resolution happens in
 * terms that are known by the raw data.
 * </p>
 * <h2>Specifying links in raw data</h2>
 * <p>
 * It is possible to link the record into which group data is imported to
 * another existing record. There is a special format for specifying
 * <strong>resolvable links</strong> in raw data. The subclass can convert the
 * raw data to that format by calling the
 * {@link Convert#stringToResolvableLinkSpecification(String, String)} on the
 * raw data before converting it to a multimap.
 * </p>
 * <p>
 * When the importer encounters a {@link ResolvableLink}, similar to a
 * resolveKey, it finds all the records who have the value for the specified key
 * and links the record into which the group data is imported to all those
 * records.
 * </p>
 * <h4>Example</h4>
 * <table>
 * <tr>
 * <th>account_number</th>
 * <th>customer</th>
 * <th>account_type</th>
 * </tr>
 * <tr>
 * <td>12345</td>
 * <td>@&lt;customer_id&gt;@678@&lt;customer_id&gt;@</td>
 * <td>SAVINGS</td>
 * </tr>
 * </table>
 * <p>
 * Lets assume in this scenario, I've imported customer data. Now, I want to
 * link the "customer" key in each account record that is created to the
 * previously created customer records. By specifying the raw customer value as
 * 
 * {@code @<customer_id>@678@<customer_id>@} I am saying that the system should
 * find all the records where customer_id is equal to 678 and link the
 * "customer" key in the account record that is being created to all those
 * records.
 * <p>
 * 
 * @param <T> the object type used for file validation
 * @author jnelson
 */
<<<<<<< HEAD
public abstract class Importer<T> {
=======
@NotThreadSafe
public abstract class Importer {
>>>>>>> c50f069728b15c45ff1bb78c8203a8b3d8fab8ff

    /**
     * The connection to Concourse.
     */
    protected final Concourse concourse;

    /**
     * Construct a new instance.
     * 
     * @param concourse
     */
    protected Importer(Concourse concourse) {
        this.concourse = concourse;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            // NOTE: We close the Importer in a shutdown hook so that the
            // process does not inflate the amount of time for importing

            @Override
            public void run() {
                close();
            }

        }));
    }

    /**
     * Close the importer and release any resources that it has open.
     */
    public final void close() {
        concourse.exit();
    }

    /**
     * Import the data contained in {@code file} into {@link Concourse} and
     * return the record ids where the data was imported.
     * 
     * @param file
     * @return the records affected by the import
     */
    @Nullable
    public abstract Set<Long> importFile(String file);

<<<<<<< HEAD
    /**
     * Check {@code line} to determine if is valid for the the file format that
     * is supported by the importer.
     * 
     * @param object - is an object of the file being imported
     * @throws IllegalArgumentException if the object is
     *             not acceptable for the file format
     */
    protected abstract void validateFileFormat(T object)
            throws IllegalArgumentException;
=======
    @Nullable
    public abstract Set<Long> importString(String data);
>>>>>>> c50f069728b15c45ff1bb78c8203a8b3d8fab8ff

}
