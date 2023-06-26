/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Document(collection = "#{@snapshotBean}")
@Data
@AllArgsConstructor
public class SnapshotETY {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_INSERTIONS = "insertions";
    public static final String FIELD_DELETIONS = "deletions";
    public static final String FIELD_INSERTION_DATE = "insertion_date";

    @Id
    private String id;

    @Field(FIELD_INSERTIONS)
    private ChunksETY insertions;

    @Field(FIELD_DELETIONS)
    private ChunksETY deletions;

    @Field(FIELD_INSERTION_DATE)
    private Date insertionDate;

    public static SnapshotETY empty() {
        return new SnapshotETY(null, ChunksETY.empty(), ChunksETY.empty(), new Date());
    }

}
