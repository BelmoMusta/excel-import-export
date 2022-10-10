package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

public class Inner {
	String id;
	
	@ToColumn
	public String getId() {
		return id;
	}
}
