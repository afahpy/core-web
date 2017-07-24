/**
 * esta es la clase net.sf.dynamicreports.report.builder.DynamicReports
 * pero le quité todo los y final
 * 
 */
package com.coreweb.extras.reporte;

import com.coreweb.extras.reporte.extendidos.*;

import net.sf.dynamicreports.report.builder.*;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.definition.JasperReportHandler;
import net.sf.dynamicreports.report.builder.barcode.BarcodeBuilders;
import net.sf.dynamicreports.report.builder.chart.ChartBuilders;
import net.sf.dynamicreports.report.builder.column.ColumnBuilders;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.condition.ConditionBuilders;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilders;
import net.sf.dynamicreports.report.builder.datatype.DataTypeBuilders;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.expression.ExpressionBuilders;
import net.sf.dynamicreports.report.builder.grid.GridBuilders;
import net.sf.dynamicreports.report.builder.group.GroupBuilders;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilders;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsHeadingBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.OrderType;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.dynamicreports.report.exception.DRException;

import java.awt.Color;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class ReporteDefinicion {

	public static final PageType A4 = PageType.A4;
	public static final PageType A5 = PageType.A5;
	public static final PageType LEGAL = PageType.LEGAL;
	public static final PageType OFICIO = PageType.FLSA;

	public static final String EXPORT_PDF = ".pdf";
	public static final String EXPORT_CSV = ".csv";
	public static final String EXPORT_XLSX = ".xlsx";
	public static final String EXPORT_DOCX = ".docx";

	private PageType tipoPagina = PageType.A4;
	
	
	public void setA4() {
		this.tipoPagina = A4;
	}

	public PageType getTipoPagina() {
		return tipoPagina;
	}

	public void setTipoPagina(PageType tipoPagina) {
		this.tipoPagina = tipoPagina;
	}

	public void setA5() {
		this.tipoPagina = A5;
	}

	public void setLegal() {
		this.tipoPagina = LEGAL;
	}

	public void setOficio() {
		this.tipoPagina = OFICIO;
	}

	/**
	 * A set of methods of creating report columns.<br/>
	 * It is used to display data in a multi-column layout.
	 */
	public ColumnBuilders col = new ColumnBuilders();
	/**
	 * A set of methods of customizing columns layout.
	 */
	public GridBuilders grid = new GridBuilders();
	/**
	 * A set of methods of creating report groups.
	 */
	public GroupBuilders grp = new GroupBuilders();
	/**
	 * A set of methods of creating column subtotals.
	 */
	public SubtotalBuilders sbt = new SubtotalBuilders();
	/**
	 * A set of methods of creating and customizing styles.
	 */
	public StyleBuilders stl = new StyleBuilders();
	/**
	 * A set of methods of creating components.
	 */
	public ComponentBuilders cmp = new ComponentBuilders();
	/**
	 * A set of build in expressions.<br/>
	 * Expressions are used to define various calculations, conditions, text
	 * field content, specific report groups, etc.
	 */
	public ExpressionBuilders exp = new ExpressionBuilders();
	/**
	 * A set of build in condition expressions.
	 */
	public ConditionBuilders cnd = new ConditionBuilders();
	/**
	 * A set of build in data types.
	 */
	public DataTypeBuilders type = new DataTypeBuilders();
	/**
	 * A set of methods of creating and customizing charts.
	 */
	public ChartBuilders cht = new ChartBuilders();
	/**
	 * A set of methods of creating exporters.
	 */
	public ExporterBuilders export = new ExporterBuilders();
	/**
	 * A set of methods of creating barcodes.
	 */
	public BarcodeBuilders bcode = new BarcodeBuilders();
	/**
	 * A set of methods of creating and customizing crosstabs.
	 */
	public CrosstabBuilders ctab = new CrosstabBuilders();

	
	private StyleBuilder monoSpaceStyle = stl.style().setFontName("Cousine");
	
	
	/**
	 * Creates a new report builder. The most used report builder for creating
	 * reports. It allows constructing and customizing the whole report content.
	 *
	 * @return a report builder
	 */
	public JasperReportBuilder report() {
		JasperReportBuilder r = new JasperReportBuilder();
		r.addProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
		// r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.1","title");
		// r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.2","columnFooter");
		r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.3", "pageFooter");
		// r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.4","lastPageFooter");
		// r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.5","summary");
		// r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.band.6","columnHeader");
		r.addProperty("net.sf.jasperreports.export.xls.write.header", "true");
		r.addProperty("net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.2", "columnHeader");
		r.addProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true");

		r.addProperty("net.sf.jasperreports.export.docx.flexible.row.height", "true");

		return r;
	}

	/**
	 * Creates a new concatenated report builder. This report builder allows
	 * concatenating several separated reports into one single document.
	 *
	 * @return a report builder
	 */
	public JasperConcatenatedReportBuilder concatenatedReport() {
		return new JasperConcatenatedReportBuilder();
	}

	/**
	 * Creates a new concatenated report builder. This report builder allows
	 * concatenating several separated reports into one single document.
	 *
	 * @return a report builder
	 */
	public JasperConcatenatedReportBuilder concatenatedReport(JasperReportHandler jasperReportHandler) {
		return new JasperConcatenatedReportBuilder(jasperReportHandler);
	}

	// field
	public <T> FieldBuilderDR<T> field(String name, Class<T> valueClass) {
		FieldBuilderDR<T> fieldBuilder = new FieldBuilderDR<T>(name, valueClass);
		try {
			DRIDataType<? super T, T> dataType = DataTypes.detectType(valueClass);
			fieldBuilder.setDataType(dataType);
		} catch (DRException e) {
		}
		return fieldBuilder;
	}

	public <T> FieldBuilderDR<T> field(String name, DRIDataType<? super T, T> dataType) {
		Validate.notNull(dataType, "dataType must not be null");
		FieldBuilderDR<T> fieldBuilder = new FieldBuilderDR<T>(name, dataType.getValueClass());
		fieldBuilder.setDataType(dataType);
		return fieldBuilder;
	}

	// variable
	public <T> VariableBuilderDR<T> variable(ValueColumnBuilder<?, ?> column, Calculation calculation) {
		Validate.notNull(column, "column must not be null");
		return new VariableBuilderDR<T>(column, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, ValueColumnBuilder<?, ?> column, Calculation calculation) {
		Validate.notNull(column, "column must not be null");
		return new VariableBuilderDR<T>(name, column, calculation);
	}

	public <T> VariableBuilderDR<T> variable(FieldBuilderDR<T> field, Calculation calculation) {
		Validate.notNull(field, "field must not be null");
		return new VariableBuilderDR<T>(field, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, FieldBuilderDR<T> field, Calculation calculation) {
		return new VariableBuilderDR<T>(name, field, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String fieldName, Class<?> valueClass, Calculation calculation) {
		return new VariableBuilderDR<T>(field(fieldName, valueClass), calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, String fieldName, Class<?> valueClass,
			Calculation calculation) {
		return new VariableBuilderDR<T>(name, field(fieldName, valueClass), calculation);
	}

	public <T> VariableBuilderDR<T> variable(DRIExpression<?> expression, Calculation calculation) {
		return new VariableBuilderDR<T>(expression, calculation);
	}

	public <T> VariableBuilderDR<T> variable(String name, DRIExpression<?> expression, Calculation calculation) {
		return new VariableBuilderDR<T>(name, expression, calculation);
	}

	// sort
	public SortBuilder asc(TextColumnBuilder<?> column) {
		return new SortBuilderDR(column).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(FieldBuilderDR<?> field) {
		return new SortBuilderDR(field).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(String fieldName, Class<?> valueClass) {
		return new SortBuilderDR(field(fieldName, valueClass)).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(VariableBuilderDR<?> variable) {
		return new SortBuilderDR(variable).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder asc(DRIExpression<?> expression) {
		return new SortBuilderDR(expression).setOrderType(OrderType.ASCENDING);
	}

	public SortBuilder desc(TextColumnBuilder<?> column) {
		return new SortBuilderDR(column).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(FieldBuilderDR<?> field) {
		return new SortBuilderDR(field).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(String fieldName, Class<?> valueClass) {
		return new SortBuilderDR(field(fieldName, valueClass)).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(VariableBuilderDR<?> variable) {
		return new SortBuilderDR(variable).setOrderType(OrderType.DESCENDING);
	}

	public SortBuilder desc(DRIExpression<?> expression) {
		return new SortBuilderDR(expression).setOrderType(OrderType.DESCENDING);
	}

	// hyperLink
	public HyperLinkBuilderDR hyperLink() {
		return new HyperLinkBuilderDR();
	}

	public HyperLinkBuilderDR hyperLink(String link) {
		return new HyperLinkBuilderDR(link);
	}

	public HyperLinkBuilderDR hyperLink(DRIExpression<String> linkExpression) {
		return new HyperLinkBuilderDR(linkExpression);
	}

	// margin
	public MarginBuilderDR margin() {
		return new MarginBuilderDR();
	}

	public MarginBuilderDR margin(int margin) {
		return new MarginBuilderDR(margin);
	}

	// parameter
	public <T> ParameterBuilderDR<T> parameter(String name, T value) {
		return new ParameterBuilderDR<T>(name, value);
	}

	public <T> ParameterBuilderDR<T> parameter(String name, Class<T> valueClass) {
		return new ParameterBuilderDR<T>(name, valueClass);
	}

	// query
	public QueryBuilderDR query(String text, String language) {
		return new QueryBuilderDR(text, language);
	}

	// units
	public int cm(Number value) {
		return Units.cm(value);
	}

	public int inch(Number value) {
		return Units.inch(value);
	}

	public int mm(Number value) {
		return Units.mm(value);
	}

	// template
	public ReportTemplateBuilderDR template() {
		return new ReportTemplateBuilderDR();
	}

	// table of contents
	public TableOfContentsCustomizerBuilder tableOfContentsCustomizer() {
		return new TableOfContentsCustomizerBuilder();
	}

	public TableOfContentsHeadingBuilder tableOfContentsHeading() {
		return new TableOfContentsHeadingBuilder();
	}

	public TableOfContentsHeadingBuilder tableOfContentsHeading(String label) {
		return new TableOfContentsHeadingBuilder().setLabel(label);
	}

	// dataset
	public DatasetBuilderDR dataset() {
		return new DatasetBuilderDR();
	}

	// =========== funiones propias ======================

	public static final String NEGRITA = "negrita";
	public static final String DERECHA = "derecha";
	public static final String WIDTH = "width";
	public static final String BOX = "box";
	public static final String PADDING_IZQ = "paddingIzq";
	public static final String PADDING_DER = "paddingDer";
	public static final String TABLA_IZQUIERDA = "tablaIzquierda";
	public static final String TABLA_DERECHA = "tablaDerecha";
	public static final String TABLA_CENTRADA = "tablaCentrada";
	public static final String TABLA_TITULO = "tablaTitulo";

	public static final String TIPO_STRING = "String";
	public static final String TIPO_INTEGER = "Integer";
	public static final String TIPO_LONG = "Lomg";
	public static final String TIPO_DOUBLE = "Double";
	public static final String TIPO_DOUBLE_GS = "DoubleGs";
	public static final String TIPO_DOUBLE_DS = "DoubleDolar";
	public static final String TIPO_BIGDECIMAL = "BigDecimal";
	public static final String TIPO_DATE = "Date";
	public static final String TIPO_DATEYEAR = "DateYear";
	public static final String TIPO_DATEMONTH = "DateMonth";
	public static final String TIPO_DATEDAY = "DateDay";
	public static final String TIPO_BOOLEAN = "Boolean";

	/**
	 * Ejemplo de uso, el WIDTH tiene que estar siempre al final: String estilo
	 * = NEGRITA+DERECHA+WIDTH+"20"
	 * 
	 * @param texto
	 * @param style
	 * @return
	 */
	public ComponentBuilder texto(String texto, String style) {
		if (style == null) {
			style = "";
		}

		TextFieldBuilder<String> tx = cmp.text(texto);

		StyleBuilders stl = new StyleBuilders();
		StyleBuilder stlNew = stl.style();

		if (style.indexOf(BOX) >= 0) {
			stlNew = stlNew.setBorder(stl.pen1Point());
		}

		if (style.indexOf(NEGRITA) >= 0) {
			stlNew = stlNew.bold();
		}
		if (style.indexOf(DERECHA) >= 0) {
			stlNew = stlNew.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		}
		if (style.indexOf(PADDING_IZQ) >= 0) {
			stlNew.setLeftPadding(5);
			// stlNew.setTopPadding(1);
			// stlNew.setPadding(5);
		}
		if (style.indexOf(PADDING_DER) >= 0) {
			stlNew.setRightPadding(5);
		}

		int pw = style.indexOf(WIDTH);
		if (pw >= 0) {
			String wdStr = style.substring(pw + WIDTH.length());
			int ws = Integer.parseInt(wdStr);
			tx.setWidth(ws);
		}
		tx.setStyle(stlNew);

		return tx;
	}

	public ComponentBuilder texto(String texto) {
		return cmp.text(texto);
	}

	public ComponentBuilder textoAlineadoDerecha(String texto) {
		return cmp.text(texto).setStyle(Templates.textStyleRigth);
	}

	public ComponentBuilder textoNegrita(String texto) {
		return cmp.text(texto).setStyle(Templates.boldStyle);
	}

	public ComponentBuilder textoNegritaDerecha(String texto) {
		return cmp.text(texto).setStyle(Templates.boldStyleRight);
	}

	
	public ComponentBuilder textoMonoSpace(String texto) {
		return cmp.text(texto).setStyle(monoSpaceStyle);
	}
	
	
	
	/*
	 * me genera un espacio en blanco a la izquierda. Se ve en los CV de saraki
	 * public ComponentBuilder textoParValor(String texto, Object valor) {
	 * HorizontalListBuilder out = cmp.horizontalFlowList();
	 * out.setStretchType(StretchType.NO_STRETCH);
	 * out.add(this.textoNegrita(texto+":")); out.add(this.texto(valor+""));
	 * return out; }
	 */
	public ComponentBuilder textoParValor(String texto, Object valor) {
		return cmp.text("<b>" + texto + ":</b> " + valor).setStyle(Templates.styleHTML);
	}

	public ComponentBuilder textoAutorizado() {

		VerticalListBuilder out = cmp.verticalList();
		out.setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER));
		out.add(cmp.verticalGap(100));
		out.add(cmp.horizontalFlowList().add(165,
				this.textoNegrita("---------------------------------------------------------------------")));
		out.add(cmp.horizontalFlowList().add(235, this.textoNegrita("AUTORIZADO")));
		return out;
	}

	public ComponentBuilder recuadro(ComponentBuilder dato) {

		VerticalListBuilder out = cmp.verticalList().setStyle(Templates.box)
				.setStretchType(StretchType.RELATIVE_TO_BAND_HEIGHT);
		out.add(cmp.verticalGap(5));
		out.add(dato);
		out.add(cmp.verticalGap(5));

		return out;
	}

	public ComponentBuilder espacioAncho(int ancho) {
		return this.espacio(ancho, 1);
	}

	public ComponentBuilder espacioAlto(int alto) {
		return this.espacio(1, alto);
	}

	public ComponentBuilder espacio(int ancho, int alto) {
		VerticalListBuilder out = cmp.verticalList();
		out.setFixedWidth(ancho);
		out.setFixedHeight(alto);
		return out;
	}

	public ComponentBuilder textoAncho(String texto, int ancho) {
		HorizontalListBuilder out = cmp.horizontalList().setFixedWidth(ancho);
		StyleBuilder ss = stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT);
		out.add(texto(texto).setStyle(ss));
		return out;
	}

	public ComponentBuilder textoAnchoStyle(String texto, int ancho, StyleBuilder style) {
		HorizontalListBuilder out = cmp.horizontalList().setFixedWidth(ancho);
		out.add(texto(texto).setStyle(style));
		return out;
	}

	public ComponentBuilder textoAnchoAlineadoDerecha(String texto, int ancho) {
		HorizontalListBuilder out = cmp.horizontalList().setFixedWidth(ancho);
		StyleBuilder ss = stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT);
		out.add(texto(texto).setStyle(ss));
		return out;
	}

	public ComponentBuilder textoAnchoAlineadoDerechaBackgorunColor(String texto, int ancho, Color bg) {
		HorizontalListBuilder out = cmp.horizontalList().setFixedWidth(ancho);

		StyleBuilder ss = stl.style().setBackgroundColor(bg).setHorizontalAlignment(HorizontalAlignment.RIGHT);

		out.add(texto(texto).setStyle(ss));
		return out;
	}

	public ComponentBuilder textoAnchoAlineadoIzquierdaBackgorunColor(String texto, int ancho, Color bg) {
		HorizontalListBuilder out = cmp.horizontalList().setFixedWidth(ancho);

		StyleBuilder ss = stl.style().setBackgroundColor(bg).setHorizontalAlignment(HorizontalAlignment.LEFT);

		out.add(texto(texto).setStyle(ss));
		return out;
	}

	/**
	 * Ejemplo de propiedades (el titulo siempre tiene que estar al final:
	 * String prop = TABLA_DERECHA + TABLA_TITULO + "Titulo de la tabla";
	 * 
	 * @param cols
	 * @param datos
	 * @param prop
	 * @return
	 */

	public ComponentBuilder getTabla2(String[][] cols, List<Object[]> datos, String prop) {
		HorizontalListBuilder out = cmp.horizontalList();
		VerticalListBuilder cuerpo = cmp.verticalList();

		String ESP = " ";
		String PADDING = PADDING_DER + PADDING_IZQ;

		// para centrar la tabla
		VerticalListBuilder colAux = cmp.verticalList();
		colAux.add(this.texto(""));

		int nFilas = datos.size();
		int nColumnas = cols.length;

		HorizontalListBuilder tabla = cmp.horizontalList();

		if ((prop.indexOf(TABLA_DERECHA) >= 0) || (prop.indexOf(TABLA_CENTRADA) >= 0)) {
			out.add(colAux);
		}

		// propiedades titulo
		int pt = prop.indexOf(TABLA_TITULO);
		if (pt >= 0) {
			String tiStr = prop.substring(pt + TABLA_TITULO.length());
			cuerpo.add(this.texto(tiStr, NEGRITA + PADDING));
		}

		for (int cc = 0; cc < nColumnas; cc++) {

			VerticalListBuilder columna = cmp.verticalList();

			String titulo = cols[cc][0];
			String estilo = this.BOX + PADDING + cols[cc][1];

			columna.add(this.texto(ESP + titulo + ESP, estilo));

			for (int ff = 0; ff < nFilas; ff++) {
				Object dd = datos.get(ff)[cc];
				columna.add(this.texto(ESP + dd + ESP, estilo));
			}

			tabla.add(columna);
		}

		cuerpo.add(tabla);

		out.add(cuerpo);

		if ((prop.indexOf(TABLA_IZQUIERDA) >= 0) || (prop.indexOf(TABLA_CENTRADA) >= 0)) {
			out.add(colAux);
		}

		return out;
	}

	public ComponentBuilder getTabla(String[][] cols, List<Object[]> datos, String prop) {
		return getTabla(cols, datos, prop, false);
	}

	public ComponentBuilder getTabla(String[][] cols, List<Object[]> datos, String prop, boolean siPie) {
		HorizontalListBuilder out = cmp.horizontalList();
		VerticalListBuilder cuerpo = cmp.verticalList();

		String ESP = " ";
		String PADDING = PADDING_DER + PADDING_IZQ;

		// StyleBuilder colorbk =
		// stl.style().setBackgroundColor(Color.LIGHT_GRAY);
		StyleBuilder colorbk = stl.style().setBackgroundColor(new Color(221, 221, 221));
		boolean siBkColor = true;

		// para centrar la tabla
		VerticalListBuilder colAux = cmp.verticalList();
		colAux.add(this.texto(""));

		int nFilas = datos.size();
		int nColumnas = cols.length;

		if ((prop.indexOf(TABLA_DERECHA) >= 0) || (prop.indexOf(TABLA_CENTRADA) >= 0)) {
			out.add(colAux);
		}

		// propiedades titulo
		int pt = prop.indexOf(TABLA_TITULO);
		if (pt >= 0) {
			String tiStr = prop.substring(pt + TABLA_TITULO.length());
			cuerpo.add(this.texto(tiStr, NEGRITA + PADDING));
		}

		VerticalListBuilder tabla = cmp.verticalList();
		tabla.setStyle(stl.style().setBorder(stl.pen1Point()));

		// cargar los titulos

		HorizontalListBuilder fila = cmp.horizontalList();
		for (int cc = 0; cc < nColumnas; cc++) {
			String titulo = cols[cc][0];
			String estilo = this.BOX + PADDING + cols[cc][1];
			fila.add(this.texto(ESP + titulo + ESP, NEGRITA + estilo));
		}
		fila.setStyle(colorbk);
		siBkColor = !siBkColor;
		tabla.add(fila);

		// los datos

		for (int ff = 0; ff < nFilas; ff++) {

			fila = cmp.horizontalList();

			for (int cc = 0; cc < nColumnas; cc++) {

				String siBox = "";
				if ((siPie == true) && (ff == (nFilas - 1))) {
					// ultima fila
					siBox = NEGRITA + BOX;
				}

				String estilo = PADDING + siBox + cols[cc][1];
				Object dd = datos.get(ff)[cc];
				fila.add(this.texto(ESP + dd + ESP, estilo));

			}

			if (siBkColor == true) {
				fila.setStyle(colorbk);
			}

			tabla.add(fila);
			siBkColor = !siBkColor;
		}

		cuerpo.add(tabla);
		cuerpo.add(cmp.line());

		out.add(cuerpo);

		if ((prop.indexOf(TABLA_IZQUIERDA) >= 0) || (prop.indexOf(TABLA_CENTRADA) >= 0)) {
			out.add(colAux);
		}

		return out;
	}

}
