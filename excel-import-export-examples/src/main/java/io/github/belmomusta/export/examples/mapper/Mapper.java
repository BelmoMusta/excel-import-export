package io.github.belmomusta.export.examples.mapper;

import io.github.belmomusta.export.examples.objects.Car;
import io.github.belmomusta.exporter.api.annotation.ExcelMapper;

@ExcelMapper
public interface Mapper {
	void map(Car car);
}
