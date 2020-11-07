package io.github.belmomusta.excel.importexport.objects;

import java.net.URL;

public class TestUtils {
	public static String getCarsFile(Class<?> cls) {
		final URL resource = cls.getClassLoader().getResource("cars.xlsx");
		return resource.getFile();
	}
}
