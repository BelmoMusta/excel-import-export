package io.github.belmomusta.exporter.processor.velocity;

import java.util.Objects;

public class FormatterWrapper implements Comparable<FormatterWrapper> {
	private String fullFQN;
	private String instanceName;
	private String simpleName;
	
 	public FormatterWrapper(String fullFQN){
		this.fullFQN = fullFQN;
		int index = fullFQN.lastIndexOf('.');
		simpleName = fullFQN.substring(index + 1);
		instanceName = "l"+simpleName;
		
	}
	
	public String getFullFQN() {
		return fullFQN;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	@Override
	public String toString() {
		return fullFQN;
	}
	
	@Override
	public int compareTo(FormatterWrapper o) {
		return fullFQN.compareTo(o.fullFQN);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FormatterWrapper that = (FormatterWrapper) o;
		return fullFQN.equals(that.fullFQN);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(fullFQN);
	}
	
	public String getSimpleName() {
		return simpleName;
	}
}
