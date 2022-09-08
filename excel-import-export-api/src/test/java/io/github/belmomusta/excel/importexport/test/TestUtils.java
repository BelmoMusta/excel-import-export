package io.github.belmomusta.excel.importexport.test;

import java.net.URL;

public class TestUtils {
	public static String getCarsFile(Class<?> cls) {
		final URL resource = cls.getClassLoader().getResource("cars.xlsx");
		return resource.getFile();
	}
}
