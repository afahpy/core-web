package com.coreweb;

import org.zkoss.zk.ui.Executions;

public class Config {

	public static String CSV_SEPARADOR = "\t";
	
	// 0 = normal, 1=bootstrap, 2=
	public static int TEMA = 0;  
	public static int TIPO_TEMA_NORMAL = 0;
	public static int TIPO_TEMA_BOOTSTRAP = 1;
	
	public static String USER_SYSTEMA = "SYS";
	public static String ANCHO_APP = "1024px";
	public static String ALERTAS = "alertas";
	public static String MI_ALERTAS = "mi_alertas";

	public static String ALIAS_HABILITADO_SI_O_SI = "-- Alias habilitado si o si  --";
	public static String LOGEADO = "logeado";
	public static String USUARIO = "usuario";
	public static String USUARIO_ORIGINAL = "usuarioOriginal";
	public static String LOGIN = "login";
	public static String CONTROL_INICIO = "ControlInicio";
	public static String IP_ADDRESS = "ip_address";

	public static String COLOR_BARRA = "#BCC6CC";
	public static String COLOR_BARRA_CAMBIO_USUARIO = "#EEAD2C";
	
	public static String BROWSER2_VM = "Browser2Vm";
	
	public static String PREFIX = "prefix";
	public static String APP_NAME = "/sinApp";

	public static String INIT_CLASE = "com.?.inicio.Inicio";
	public static String INIT_METODO = "init";
	public static String INIT_AFTER_LOGIN = "afterLogin";

	public static String GRUPO_ADMINISTRACION = "Administracion";
	public static String GRUPO_VENTAS = "Ventas";
	public static String GRUPO_AUDITORIA = "Auditoria";
	public static String GRUPO_IMPORTACION = "Importacion";

	// propiedad de usuario
	public static String TIPO_TIPO_USUARIO_PROPIEDAD = "Propiedad de Usuario";
	public static String TIPO_USUARIO_PROPIEDAD_SIGLA = "User-Prop";

	// botones
	public static int BOTON_YES = 0;
	public static int BOTON_NO = 1;
	public static int BOTON_CANCEL = 2;

	// Para acceder a los archivos por la Web o por el disco
	public static String DIRECTORIO_BASE_WEB = "";
	public static String DIRECTORIO_BASE_REAL = "";

	// directorios para reportes
	private static String REPORTES = "/reportes";
	public static String DIRECTORIO_WEB_REPORTES;
	public static String DIRECTORIO_REAL_REPORTES;

	// buscar elementos
	public static int CUANTOS_BUSCAR_ELEMENTOS = 500;
	public static int TAMANIO_MASCARA = 50;

	// Backup
	public static String BACKUP = "backup/";
	public static String SCRIPT = "core/script/";
	public static String SCRIPT_BACKUP = "backup.sh";
	public static String SCRIPT_VER_CATALINA = "verCatalina.sh";
	public static String DIRECTORIO_BACKUP = "";
	public static String DIRECTORIO_SCRIPT = "";

	static {
		try {
			
			if (Executions.getCurrent() != null) {
				DIRECTORIO_BASE_WEB = Executions.getCurrent().getDesktop()
						.getCurrentDirectory();
				DIRECTORIO_BASE_REAL = Executions.getCurrent().getDesktop()
						.getWebApp().getRealPath("/");
				
			}else{
				DIRECTORIO_BASE_WEB = "./";
				DIRECTORIO_BASE_REAL = "./";
			}
			APP_NAME = Executions.getCurrent().getContextPath();
			
		} catch (Exception e) {
			System.out.println("...............................................");
			e.printStackTrace();
			System.out.println("...............................................");
		}
		
		
		DIRECTORIO_WEB_REPORTES = DIRECTORIO_BASE_WEB + REPORTES;
		DIRECTORIO_REAL_REPORTES = DIRECTORIO_BASE_REAL + REPORTES;
		
		DIRECTORIO_BACKUP = DIRECTORIO_BASE_REAL + BACKUP;
		
		DIRECTORIO_SCRIPT = DIRECTORIO_BASE_REAL + SCRIPT;
		SCRIPT_BACKUP = DIRECTORIO_SCRIPT + SCRIPT_BACKUP;
		SCRIPT_VER_CATALINA = DIRECTORIO_SCRIPT + SCRIPT_VER_CATALINA;

		System.out.println("DIRECTORIO_WEB_REPORTES:"+DIRECTORIO_WEB_REPORTES);
		
		
	}

	
	public static String USUARIO_DESARROLLADOR = "UsuarioDesarrollador";
	public static String SIGLA_TIPO_USUARIO_DESARROLLADOR = "USU-DES";
	
	
	public static String LOGO_REPORTE_EMPRESA_DEFAULT = DIRECTORIO_BASE_REAL+"/core/images/logoMickey.png";
	public static int    LOGO_REPORTE_EMPRESA_ANCHO = 30;
	public static int    LOGO_REPORTE_EMPRESA_ALTO = 30;

	
	public static final String DATO_SOLO_VIEW_MODEL = "dato";

	public static String WINDOW_POPUP = "windowsPopUp";
	public static String MODO_SOLO_VIEW_MODEL = "modo";
	public static String MODO_DISABLE = "disable";
	public static String MODO_NO_DISABLE = "si-editable";
	public static String MODO_EDITABLE = "editar-si-tiene-permisos";

	public static String TIPO_STRING = "String";
	public static String TIPO_NUMERICO = "Numerico";
	public static String TIPO_BOOL = "Bool";
	public static String TIPO_DATE = "Date";
	public static String TIPO_CHARACTER = "Character";

	public static String IMAGEN_OK = "/core/images/accept_.png";
	public static String IMAGEN_CANCEL = "/core/images/delete_.png";
	public static String IMAGEN_CHECK = "/core/images/tick.png";
	
	public static String EXTENSION_DOCUMENTO = "pdf-odt-txt-docx-rtf";
	public static String EXTENSION_IMAGEN = "jpg-jpeg-gif-png-tif";
	public static String EXTENSION_PLANILLA = "xlsx-csv-ods";

	/********************************************************************************************/
	/********************************************************************************************/
	// alertas
	public static String ICONO_ACEPTAR_16X16 = "/core/images/accept_.png";
	public static String ICONO_ANULAR_16X16 = "/core/images/delete_.png";
	public static String ICONO_EXCLAMACION_16X16 = "/core/images/exclamation.png";
	public static String ICONO_EXCLAMACION_YELLOW_16X16 = "/core/images/exclamation_yellow.png";

	public static String ALERTAS_ZUL = "/core/misc/alertas.zul";

	public static String ALERTA_NIVEL_INFORMATIVO = "Alerta informativa";
	public static String ALERTA_NIVEL_ERROR = "Alerta error";

	public static String SIGLA_NIVEL_ALERTA_INFORMATIVA = "ALER-INFO";
	public static String SIGLA_NIVEL_ALERTA_ERROR = "ALER-ERROR";
	
	public static String SIGLA_TIPO_ALERTA_UNO = "DESTINO-UNO";
	public static String SIGLA_TIPO_ALERTA_MUCHOS = "DESTINO-MUCHOS";
	public static String SIGLA_TIPO_ALERTA_COMUNITARIA = "DESTINO-COMUN";
	
	public static String NRO_ALERTA = "ALER";
	
	public static String CREAR_ALERTA_ZUL = "/core/misc/crearAlerta.zul";

	public static String MENSAJE_POPUP_ZUL = "/core/componente/mensajePopUp.zul";
	
	/*** esto debería estar en el core */

	public static String ID_TIPO_TIPO_ALERTA = "Tipo Alerta";

	public static String[] ARR_TIPO_ALERTAS = { ID_TIPO_TIPO_ALERTA,
			SIGLA_TIPO_ALERTA_MUCHOS, "Muchos destinos, muchos canceladores",
			SIGLA_TIPO_ALERTA_UNO, "Un destino, un cancelador",
			SIGLA_TIPO_ALERTA_COMUNITARIA, "Muchos destinos, algun cancelador", };

	public static String ID_TIPO_NIVEL_ALERTA = "Nivel Alerta";

	public static String[] ARR_TIPO_NIVEL_ALERTAS = { ID_TIPO_NIVEL_ALERTA,
			SIGLA_NIVEL_ALERTA_INFORMATIVA, ALERTA_NIVEL_INFORMATIVO,
			SIGLA_NIVEL_ALERTA_ERROR, ALERTA_NIVEL_ERROR, };
	
	
	/********************************************************************************************/
	/********************************************************************************************/
	
	
	public static String STR_ERROR_BUSCAR_ELEMENTO = "Debe ingresar un criterio de filtro";
	
	// la clase controlador..
	public static String CONTROLADOR_BUSCADOR_PRODUCTOS = "ControladorBuscadorProductos";
	public static String CONTROLADOR_MIS_VENTAS = "ControladorMisVentas";
	public static String CONTROLADOR_VISOR_CTACTE = "ControladorVisorCtaCte";
	
	//Formatos de moneda
	public static String FORMAT_MONEDA_LOCAL = "#,##0";
	public static String FORMAT_MONEDA_EXTRANJERA = "#,##0.0000";
	
	public static final int CTRL_A = 65;
	public static final int CTRL_B = 66;
	public static final int CTRL_C = 67;
	public static final int CTRL_F = 70;
	public static final int CTRL_I = 73;
	public static final int CTRL_L = 76;
	public static final int CTRL_S = 83;
	public static final int CTRL_U = 85;
	public static final int CTRL_V = 86;
	public static final int CTRL_X = 88;
	public static final int CTRL_Y = 89;
	public static final int CTRL_Z = 90;	

}
