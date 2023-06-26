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
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorInstance {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Validation {
                public static final String CONSTRAINT_FIELD = "/constraint/field";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Server {
                public static final String INTERNAL = "/internal";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Resource {
                public static final String CONFLICT = "/conflict";
                public static final String NOT_FOUND = "/not-found";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class IO {
                public static final String CONVERSION = "/data-processing";
                public static final String INTEGRITY = "/data-integrity";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Fields {
                public static final String FILE = "file";
        }

}
