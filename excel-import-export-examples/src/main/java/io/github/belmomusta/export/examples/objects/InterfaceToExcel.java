package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.Export;
import io.github.belmomusta.exporter.api.annotation.ToColumn;
import io.github.belmomusta.exporter.api.common.ExportType;

@Export(type = ExportType.EXCEL, ignoreHeaders = false)
public interface InterfaceToExcel {
	@ToColumn
	String getSomething();
	
	@ToColumn
	static String toto(){
		return "toto";
	}
}
