package io.github.belmomusta.exporter.processor.types;

import io.github.belmomusta.exporter.processor.velocity.FieldMethodPair;

import java.util.TreeSet;

public class FieldMethodSet extends TreeSet<FieldMethodPair> {
	public FieldMethodSet() {
		super((a, b) -> {
			if (a.getOrder() == b.getOrder()) {
				return 1;
			}
			return a.getOrder() - b.getOrder();
		});
	}
}
