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
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
public class TerminologyBuilderDTO {

	@CsvBindByName(column = "Code")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String code;

	@CsvBindByName(column = "Display")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String display;

	@CsvBindByName(column = "CodeSystem")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String codeSystem;
	
	@CsvBindByName(column = "DisplayName")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String displayName;
	
	@CsvBindByName(column = "status")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String status;

	@CsvBindByName(column = "Not Selectable")
	@Schema(minLength = 0, maxLength = DEFAULT_STRING_MAX_SIZE)
	private boolean notSelectable;
	
	public String getDescription() {
		return isValueSet() ? getDisplayName() : getDisplay();
	}

	public boolean isActive() {
		return !isDeprecated() && !notSelectable;
	}

	public boolean isDeprecated() {
		if (status == null) return false;
		return status.equalsIgnoreCase("deprecated");
	}

	
	private boolean isValueSet() {
		return !StringUtils.isEmpty(displayName);
	}
	
}
