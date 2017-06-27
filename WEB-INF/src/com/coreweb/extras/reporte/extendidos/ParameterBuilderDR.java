package com.coreweb.extras.reporte.extendidos;

import net.sf.dynamicreports.report.base.DRParameter;
import net.sf.dynamicreports.report.builder.ParameterBuilder;

public class ParameterBuilderDR<T> extends ParameterBuilder {

	public ParameterBuilderDR(String name, T value) {
		super(name, value);
	}

	public ParameterBuilderDR(String name, Class<T> valueClass) {
		super(name, valueClass);
	}
	
}
