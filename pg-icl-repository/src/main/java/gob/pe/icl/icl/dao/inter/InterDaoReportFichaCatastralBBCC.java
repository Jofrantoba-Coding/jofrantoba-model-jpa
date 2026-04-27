/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.reports.DtoDocumentoDatoRegistral;
import gob.pe.icl.icl.dto.reports.DtoEdificacionBienComun;
import gob.pe.icl.icl.dto.reports.DtoRecapitulacionBienComun;
import gob.pe.icl.icl.dto.reports.DtoRecapitulacionEdificio;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralBBCC extends InterCrud<UnidadCatastral> {

    List<DtoEdificacionBienComun> dsEdificacionBienComun(Long idUnidadCatastral);

    List<DtoRecapitulacionEdificio> dsRecapitulacionEdificio(Long idUnidadCatastral);

    List<DtoRecapitulacionBienComun> dsRecapitulacionBienComun(Long idUnidadCatastral);

    List<DtoDocumentoDatoRegistral> dsDocumentoDatoRegistral(Long idUnidadCatastral);

}
