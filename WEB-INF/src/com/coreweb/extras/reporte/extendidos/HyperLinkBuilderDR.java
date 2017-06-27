package com.coreweb.extras.reporte.extendidos;

import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

public class HyperLinkBuilderDR extends HyperLinkBuilder {

	public HyperLinkBuilderDR() {
		super();
	}

	public HyperLinkBuilderDR(String link) {
		super(link);
	}

	public HyperLinkBuilderDR(DRIExpression<String> linkExpression) {
		super(linkExpression);
	}
}
