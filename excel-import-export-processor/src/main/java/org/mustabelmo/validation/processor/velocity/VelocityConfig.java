package org.mustabelmo.validation.processor.velocity;

import io.github.belmomusta.excel.api.csv.AbstractCSVExporter;
import io.github.belmomusta.excel.api.export.AbstractExcelMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class VelocityConfig {
	protected final Predicate<Class> classPredicate = c -> !isUseFQNs();
	private final boolean useFQNs;
	private String fullCurrentClassName;
	
	
	public VelocityConfig(boolean useFQNs) {this.useFQNs = useFQNs;}
	
	public boolean isUseFQNs() {
		return useFQNs;
	}
	
	public void setFullCurrentClassName(String fullCurrentClassName) {
		this.fullCurrentClassName = fullCurrentClassName;
	}
	
	public String getFullCurrentClassName() {
		return fullCurrentClassName;
	}
	
	public String getCurrentClassName() {
		return Optional.of(fullCurrentClassName)
				.filter(c -> !isUseFQNs())
				.map(s -> s.substring(s.lastIndexOf('.') + 1))
				.orElse(fullCurrentClassName);
		
	}
	
	public String getSheetClassName() {
		return nameOfAClass(Sheet.class);
		
	}
	
	public String getAbstractCSVExporterClassName() {
		return nameOfAClass(AbstractCSVExporter.class);
	}
	
	public String getArrayListClassName() {
		return nameOfAClass(ArrayList.class);
	}
	
	public String getCollectionClassName() {
		return nameOfAClass(Collection.class);
	}
	
	public String getAbstractCSVExporterFullClassName() {
		return fullNameOfAClass(AbstractCSVExporter.class);
	}
	
	public String getArrayListFullClassName() {
		return fullNameOfAClass(ArrayList.class);
	}
	
	public String getCollectionFullClassName() {
		return fullNameOfAClass(Collection.class);
	}
	
	public String getRowClassName() {
		return nameOfAClass(Row.class);
	}
	
	public String getAbstractExcelMapperClassName() {
		return nameOfAClass(AbstractExcelMapper.class);
	}
	
	public String getSheetFullClassName() {
		return fullNameOfAClass(Sheet.class);
		
	}
	
	public String getRowFullClassName() {
		return fullNameOfAClass(Row.class);
	}
	
	public String getAbstractExcelMapperFullClassName() {
		return fullNameOfAClass(AbstractExcelMapper.class);
	}
	
	public String getStringClassName() {
		return nameOfAClass(String.class);
	}
	
	private String nameOfAClass(Class value) {
		String s = Optional.of(value)
				.filter(classPredicate.negate())
				.map(Class::getName)
				.orElse(value.getSimpleName());
		return s;
	}
	
	private String fullNameOfAClass(Class value) {
		String s = Optional.of(value)
				.filter(classPredicate)
				.map(Class::getName)
				.orElse(value.getSimpleName());
		return s;
	}
	
}
