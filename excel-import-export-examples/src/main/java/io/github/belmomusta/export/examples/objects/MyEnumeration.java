package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

@Excel
public enum MyEnumeration {
	
	A,
	B,
	
	;
	@ToColumn
	public String getValue(){
		return this.name();
	}
}