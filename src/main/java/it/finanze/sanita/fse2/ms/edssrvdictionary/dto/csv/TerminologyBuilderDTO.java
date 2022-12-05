/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
