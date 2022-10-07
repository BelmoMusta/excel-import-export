package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

public interface ICarCar {
	@ToColumn
	default String tototot(){
		return "";
	}
}
