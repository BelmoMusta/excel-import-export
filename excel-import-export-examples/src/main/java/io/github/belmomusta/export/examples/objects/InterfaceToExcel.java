package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

@Excel(ignoreHeaders = false)
public interface InterfaceToExcel {
	@ToColumn
	String getSomething();
	
	@ToColumn
	static String toto(){
		return "toto";
	}
}
