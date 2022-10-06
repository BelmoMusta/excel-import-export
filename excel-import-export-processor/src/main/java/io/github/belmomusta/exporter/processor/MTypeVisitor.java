package io.github.belmomusta.exporter.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.TypeKindVisitor7;
import java.util.List;

public class MTypeVisitor extends TypeKindVisitor7<Void, List<Element>> {
	
	@Override
	public Void visitDeclared(DeclaredType t, List<Element> list) {
		list.add(t.asElement());
		return super.visitDeclared(t, list);
	}
}
