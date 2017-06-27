package com.coreweb.extras.reporte;


import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import com.coreweb.Config;
import com.coreweb.util.Misc;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates extends ReporteDefinicion {

	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder boldStyleRight;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder style8FontSize;
	public static final StyleBuilder style8FontSizeRight;
	public static final StyleBuilder style10FontSize;
	public static final StyleBuilder style10FontSizeRight;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnStyleCenter;
	public static final StyleBuilder columnStyleLeft;
	public static final StyleBuilder columnStyleRigth;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;
	public static final StyleBuilder styleHTML;
	public static final StyleBuilder box;
	public static final StyleBuilder textStyleRigth;



	Misc m = new Misc();

	static {
		StyleBuilders stl = new StyleBuilders();
		ComponentBuilders cmp = new ComponentBuilders();
		
		styleHTML = stl.style().setMarkup(Markup.STYLED);
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		boldStyleRight = stl.style(rootStyle).bold().setHorizontalAlignment(HorizontalAlignment.RIGHT);
		italicStyle = stl.style(rootStyle).italic();
		boldCenteredStyle = stl.style(boldStyle).setAlignment(
				HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		style8FontSize = stl.style().setFontSize(8);
		style8FontSizeRight = stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT).setFontSize(8);
		style10FontSize = stl.style().setFontSize(10);
		style10FontSizeRight = stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT).setFontSize(10);
		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);

		box = stl.style().setBorder(stl.pen1Point());

		columnStyle = stl.style(rootStyle)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(stl.pen1Point());

		columnStyleCenter = stl.style(rootStyle)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER);

		columnStyleLeft = stl.style(rootStyle)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.LEFT);

		columnStyleRigth = stl.style(rootStyle)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.RIGHT);

		textStyleRigth = stl.style(rootStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();
		groupStyle = stl.style(boldStyle).setHorizontalAlignment(
				HorizontalAlignment.LEFT);
		// subtotalStyle =
		// stl.style(boldStyle).setTopBorder(stl.pen1Point()).setBackgroundColor(new
		// Color(56));
		subtotalStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.RIGHT)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();


	}
	
	public ReportTemplateBuilder reportTemplate = template().setLocale(Locale.ENGLISH)
			.setColumnStyle(columnStyle)
			.setColumnTitleStyle(columnTitleStyle)
			.setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
			.setSubtotalStyle(subtotalStyle).highlightDetailEvenRows()
			.crosstabHighlightEvenRows()
			.setCrosstabGroupStyle(stl.style(columnTitleStyle))
			.setCrosstabGroupTotalStyle(stl.style(columnTitleStyle)
					.setBackgroundColor(new Color(140, 140, 140)))
			.setCrosstabGrandTotalStyle(stl.style(columnTitleStyle)
					.setBackgroundColor(new Color(140, 140, 140)))
			.setCrosstabCellStyle(stl.style(columnStyle).setBorder(
					stl.pen1Point()))
			.setTableOfContentsCustomizer(tableOfContentsCustomizer()
					.setHeadingStyle(0, stl.style(rootStyle).bold()));

	public ComponentBuilder footerComponent = cmp.pageXofY().setStyle(
			stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));

	
	

	public ComponentBuilder createMembretePrincipal(String empresa, String logoEmpresa, int logoAncho, int logoAlto,
			String titulo, String user) {
		

		String us = ("Usuario: " + user + "                        ")
				.substring(0, 25).trim();

		VerticalListBuilder main = cmp.verticalList();

		VerticalListBuilder cab = cmp.verticalList();
		
		HorizontalListBuilder row1 = cmp.horizontalList();
		HorizontalListBuilder row2 = cmp.horizontalList();

		row1.add(cmp.text(empresa).setStyle(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT));
		row1.add(cmp.text("Generado: " + m.dateHoyToString()).setHorizontalAlignment(HorizontalAlignment.RIGHT));

		row2.add(cmp.text(titulo).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT));
		row2.add(cmp.text(us).setHorizontalAlignment(HorizontalAlignment.RIGHT));
		
		
		cab.add(row1).add(row2);
		
		// poner el logo
		if (logoEmpresa.trim().length() == 0){
			logoEmpresa = Config.LOGO_REPORTE_EMPRESA_DEFAULT;
			logoAncho = Config.LOGO_REPORTE_EMPRESA_ANCHO;
			logoAlto = Config.LOGO_REPORTE_EMPRESA_ALTO;
		}
		HorizontalListBuilder filaLogo = cmp.horizontalList();		
		ImageBuilder img = cmp.image(logoEmpresa).setWidth(logoAncho).setHeight(logoAlto);
		filaLogo.add(img);
		filaLogo.add(cab);
		
		main.add(filaLogo);
		main.add(cmp.line());
		main.add(cmp.verticalGap(10));
		
		return main;
	}

}