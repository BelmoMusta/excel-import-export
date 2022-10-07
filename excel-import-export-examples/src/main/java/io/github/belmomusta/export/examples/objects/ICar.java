package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

public interface ICar extends ICarCar {
	@ToColumn
	default String fofo(){
		return "";
	}
}
