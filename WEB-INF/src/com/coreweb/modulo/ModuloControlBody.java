package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.domain.Modulo;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.templateABM.Body;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public class ModuloControlBody extends Body {

	@Init(superclass = true)
	public void initModuloControlBody() {
	}

	@AfterCompose(superclass = true)
	public void afterComposeModuloControlBody() {
	}

	@Override
	public Assembler getAss() {
		// TODO Auto-generated method stub
		return new AssemblerModulo();
	}

	@Override
	public DTO getDTOCorriente() {
		// TODO Auto-generated method stub
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (ModuloDTO) dto;

	}

	@Override
	public DTO nuevoDTO() throws Exception {
		// TODO Auto-generated method stub
		return AssemblerModulo.getDTOModulo();
	}

	@Override
	public String getEntidadPrincipal() {
		// TODO Auto-generated method stub
		return Modulo.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	ModuloDTO dto = AssemblerModulo.getDTOModulo();

	public ModuloDTO getDto() {
		return dto;
	}

	public void setDto(ModuloDTO dto) {
		this.dto = dto;
	}

	private MyArray selectedModulo = null;
	private MyArray selectedFormulario = null;
	private MyArray selectedOperacion = null;

	public MyArray getSelectedModulo() {
		return selectedModulo;
	}

	public void setSelectedModulo(MyArray selectedModulo) {
		this.selectedModulo = selectedModulo;

		MyArray aux = new MyArray();
		aux.setPos6(new ArrayList<MyArray>());
		this.setSelectedFormulario(aux);

		MyArray aux2 = new MyArray();
		this.selectedOperacion = aux2;
		List aux3 = new ArrayList();
		this.selectedOperacion.setPos7(aux3);

	}

	public MyArray getSelectedFormulario() {
		return selectedFormulario;
	}

	public void setSelectedFormulario(MyArray selectedFormulario) {
		this.selectedFormulario = selectedFormulario;

		MyArray aux2 = new MyArray();
		this.selectedOperacion = aux2;
		List aux = new ArrayList();
		this.selectedOperacion.setPos7(aux);
	}

	public MyArray getSelectedOperacion() {
		return selectedOperacion;
	}

	public void setSelectedOperacion(MyArray selectedOperacion) {
		this.selectedOperacion = selectedOperacion;

	}

	@Command()
	@NotifyChange("*")
	public void eliminarModulo() {
		if (this.selectedModulo != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el modulo junto con todos sus formularios y operaciones?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getModulos().remove(this.selectedModulo);
			}
			this.setSelectedModulo(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarModulo() {

		if (mensajeAgregar("Agregar un nuevo modulo?")) {
			MyArray nMod = new MyArray();
			nMod.setPos1("--editar--");
			nMod.setPos2("--editar--");
			nMod.setPos3(new ArrayList<MyArray>());
			nMod.setPos4(true);
			this.getDto().getModulos().add(nMod);
			this.setSelectedModulo(nMod);
			this.setSelectedFormulario(null);
			// this.setSelectedOperacion(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void eliminarFormulario() {
		if (this.selectedFormulario != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el formulario junto con todas sus operaciones?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.selectedModulo.getPos3())
						.remove(selectedFormulario);
			}
			this.setSelectedFormulario(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarFormulario() {

		if (mensajeAgregar("Agregar un nuevo formulario?")) {
			MyArray nForm = new MyArray();
			nForm.setPos1("--editar--");
			nForm.setPos2("--editar--");
			nForm.setPos3("--editar--");
			nForm.setPos4("--editar--");

			MyPair forHabilitado = new MyPair();
			forHabilitado.setId(new Long(2));
			forHabilitado.setText("NO");
			nForm.setPos5(forHabilitado);
			
			nForm.setPos6(new ArrayList<MyArray>());
			nForm.setPos7(true);
			
			((List) this.selectedModulo.getPos3()).add(nForm);
			this.setSelectedFormulario(nForm);
			// this.setSelectedOperacion(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void eliminarOperacion() {
		if (this.selectedOperacion != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar la operacion?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.selectedFormulario.getPos6())
						.remove(this.selectedOperacion);

			}
			MyArray aux = new MyArray();
			aux.setPos7(new ArrayList<MyArray>());
			this.setSelectedOperacion(aux);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarOperacion() {

		if (mensajeAgregar("Agregar una nueva operación?")) {
			// falta modificar
			MyArray nOper = new MyArray();
			nOper.setPos1("--editar--");
			nOper.setPos2("--editar--");
			nOper.setPos3("--editar--");

			MyPair opHabilitado = new MyPair();
			opHabilitado.setId(new Long(2));
			opHabilitado.setText("NO");
			nOper.setPos4(opHabilitado);

			nOper.setPos5("--editar--");
			nOper.setPos6(selectedFormulario.getId());
			nOper.setPos7(new ArrayList());
			nOper.setPos8(true);
			
			((List) this.selectedFormulario.getPos6()).add(nOper);
			this.setSelectedOperacion(nOper);
		}
	}
	
	@Command
	public void validarNombreMod() {
		try {
			MyPair validado = validar(dto.getListaNombresModulos(),
					this.selectedModulo.getPos1());
			if (validado.getId().longValue() != new Long(0)) {
				if (validado.getId().longValue() == new Long(1)){
					this.selectedModulo.setPos1("--editar--");
					throw new Exception("El campo nombre es obligatorio");
				}
				if (validado.getId().longValue() == new Long(2)){
					this.selectedModulo.setPos1("--editar--");
					throw new Exception("El nombre de la operacion ya existe");
				}	
			}
		} catch (Exception e) {
			mensajeError(e.getMessage());
		}
	}
	@Command
	public void validarAliasFor() {
		try {
			MyPair validado = validar(dto.getListaAliasFormularios(),
					this.selectedFormulario.getPos4());
			if (validado.getId().longValue() != new Long(0)) {
				
				if (validado.getId().longValue() == new Long(1)){
					this.selectedFormulario.setPos4("--editar--");
					throw new Exception("El campo alias es obligatorio");
				}					
				if (validado.getId().longValue() == new Long(2)){
					this.selectedFormulario.setPos4("--editar--");
					throw new Exception("El alias del formulario ya existe");
				}				
			}
		} catch (Exception e) {
			mensajeError(e.getMessage());
		}
	}

	@Command
	public void validarAliasOp() {
		try {
			MyPair validado = validar(dto.getListaAliasOperaciones(),
					this.selectedOperacion.getPos1());
			if (validado.getId().longValue() != new Long(0)) {
				if (validado.getId().longValue() == new Long(1)){
					this.selectedOperacion.setPos1("--editar--");
					throw new Exception("El campo alias es obligatorio");
				}
				if (validado.getId().longValue() == new Long(2)){
					this.selectedOperacion.setPos1("--editar--");
					throw new Exception("El alias de la operacion ya existe");
				}	
			}
		} catch (Exception e) {
			mensajeError(e.getMessage());
		}
	}
/*
	@Command
	public void validarIdTexto() {
		try {
			MyPair validado = validar(dto.getListaIdTextoOperaciones(),
					this.selectedOperacion.getPos5());
			if (validado.getId().longValue() != new Long(0)) {
				this.selectedOperacion.setPos5("--editar--");
				if (validado.getId().longValue() == new Long(1)){
					throw new Exception("El camo idTexto es obligatorio");
				}
				if (validado.getId().longValue() == new Long(2)){
					throw new Exception("El idTexto de la operacion ya existe");
				}
			}
		} catch (Exception e) {
			mensajeError(e.getMessage());
		}
	}
*/
	public MyPair validar(List<Object> lista, Object cadena) {
		// si esta en la lista debe retornar false
		MyPair valido = new MyPair();
		if (cadena.equals("")) {
			valido.setId(new Long(1));
		} else if (lista.contains(cadena)) {
			valido.setId(new Long(2));
		}
		return valido;
	}
	
	
	@Override
	public boolean verificarAlGrabar() {
		return true;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		String error = new String("El nombre del modulo no puede ser vacío");
		return error;
	}
}
