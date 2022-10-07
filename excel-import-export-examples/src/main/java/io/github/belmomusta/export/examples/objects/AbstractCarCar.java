package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ColumnFormat;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

import java.util.Date;

public abstract class AbstractCarCar {
	//private Date nameFromAbstract;
	
	@ToColumn(9)
	@ColumnFormat(formatter = DateFormatter.class)
	public Date getNameFromAbstractAbstract() {
		return null;
	}
	
	public void setNameFromAbstract(Date name) {
		//this.nameFromAbstract = name;
	}
}
