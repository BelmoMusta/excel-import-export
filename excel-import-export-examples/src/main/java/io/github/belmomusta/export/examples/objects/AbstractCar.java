package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

public abstract class AbstractCar {
	private String nameFromAbstract;
	
	@ToColumn public String getNameFromAbstract() {
		return nameFromAbstract;
	}
	
	public void setNameFromAbstract(String name) {
		this.nameFromAbstract = name;
	}
}
