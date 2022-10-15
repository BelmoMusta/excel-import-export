package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

public abstract class AbstractCar {
	@ToColumn
	public abstract String getStringId();
	
	@ToColumn
	protected abstract String stringId();
	
	@ToColumn
	public String getFromAbstract() {
		return null;
	}
}