package com.coreweb.extras.reporte.extendidos;

import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

public class VariableBuilderDR<T> extends VariableBuilder {

	//column
	public VariableBuilderDR(ValueColumnBuilder<?, ?> column, Calculation calculation) {
		super(column, calculation);
	}

	public VariableBuilderDR(String name, ValueColumnBuilder<?, ?> column, Calculation calculation) {
		super(name, column, calculation);
	}

	//field
	public VariableBuilderDR(FieldBuilderDR<?> field, Calculation calculation) {
		super(field, calculation);
	}

	public VariableBuilderDR(String name, FieldBuilderDR<?> field, Calculation calculation) {
		super(name, field, calculation);
	}

	//simple expression
	public VariableBuilderDR(DRIExpression<?> expression, Calculation calculation) {
		super(expression, calculation);
	}

	public VariableBuilderDR(String name, DRIExpression<?> expression, Calculation calculation) {
		super(name, expression, calculation);
	}



	
}
