package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.CSV;
import io.github.belmomusta.exporter.api.annotation.Excel;
import io.github.belmomusta.exporter.api.annotation.ToColumn;

import java.util.Objects;

@CSV
@Excel(useFQNs = true, ignoreHeaders = false)
public final class Hello<T> {
	private  T id;
	@ToColumn
	public T getId() {
		return id;
	}
	
	public void setId(T id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Hello hello = (Hello) o;
		return Objects.equals(getId(), hello.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	@Excel
	public static  class World{
		@ToColumn
		protected final static Integer identi = null;
		
		public static Integer getIdenti() {
			return identi;
		}
	}
}
