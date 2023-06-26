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

import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class TerminologyBuilderDTO {

	@CsvBindByName(column = "Code")
	private String code;

	@CsvBindByName(column = "Display")
	private String display;

	@CsvBindByName(column = "CodeSystem")
	private String codeSystem;
	
	@CsvBindByName(column = "DisplayName")
	private String displayName;
	
	@CsvBindByName(column = "status")
	private String status;

	@CsvBindByName(column = "Not Selectable")
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
