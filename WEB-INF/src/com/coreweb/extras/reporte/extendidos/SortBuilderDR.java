package com.coreweb.extras.reporte.extendidos;

import net.sf.dynamicreports.report.builder.SortBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

public class SortBuilderDR extends SortBuilder{
	
	public SortBuilderDR(TextColumnBuilder<?> column) {
		super(column);
	}

	public SortBuilderDR(FieldBuilderDR<?> field) {
		super(field);
	}

	public SortBuilderDR(VariableBuilderDR<?> variable) {
		super(variable);
	}

	public SortBuilderDR(DRIExpression<?> expression) {
		super(expression);
	}
	

}
