/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;

public interface IChangeSetSRV {
    /**
     * Retrieves the expected collection size after the alignment
     * @return The collection size
     * @throws OperationException If a data-layer error occurs
     */
    long getCollectionSize() throws OperationException;
}
