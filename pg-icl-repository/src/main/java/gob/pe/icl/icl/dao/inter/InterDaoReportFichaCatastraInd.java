/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.reports.DtoCaracteristicasTitularidad;
import gob.pe.icl.icl.dto.reports.DtoConstrucciones;
import gob.pe.icl.icl.dto.reports.DtoDescripcionPredio;
import gob.pe.icl.icl.dto.reports.DtoDocumentos;
import gob.pe.icl.icl.dto.reports.DtoDomicilioFisTitularCatastral;
import gob.pe.icl.icl.dto.reports.DtoEvaluacionPredioCatastral;
import gob.pe.icl.icl.dto.reports.DtoIdentificacionTitularCatastral;
import gob.pe.icl.icl.dto.reports.DtoIncripcionPredioCatastral;
import gob.pe.icl.icl.dto.reports.DtoInformacionComplementaria;
import gob.pe.icl.icl.dto.reports.DtoObrasComplementarias;
import gob.pe.icl.icl.dto.reports.DtoObservaciones;
import gob.pe.icl.icl.dto.reports.DtoServiciosPredio;
import gob.pe.icl.icl.dto.reports.DtoUbicacionPredioCatastral;
import gob.pe.icl.icl.dto.reports.DtoUnidadCatastral;
import gob.pe.icl.icl.dto.reports.DtoZonificacion;
import gob.pe.icl.icl.dto.reports.DtoLinderos;
import gob.pe.icl.icl.dto.reports.DtoPorcentajeBienComun;
import gob.pe.icl.icl.dto.reports.DtoUbicacionPredCatastralHabUrb;
import gob.pe.icl.icl.entity.UnidadCatastral;

import java.util.List;

/**
 *
 * @author jtorresb
 */
 public interface InterDaoReportFichaCatastraInd extends InterCrud<UnidadCatastral> {

    DtoUnidadCatastral dsMain(Long idUnidadCatastral);

    List<DtoUbicacionPredioCatastral> dsUbicacionPredioCatastral(Long idUnidadCatastral);
    
    List<DtoUbicacionPredCatastralHabUrb> dsUbicacionPredCatastralHabUrb(Long idUnidadCatastral);
    
    List<DtoIdentificacionTitularCatastral> dsIdentificacionTitularCatastral(Long idUnidadCatastral);

    List<DtoDomicilioFisTitularCatastral> dsDomicilioFisTitularCatastral(Long idUnidadCatastral);

    List<DtoCaracteristicasTitularidad> dsCaractTitularidadCatastral(Long idUnidadCatastral);

    List<DtoDescripcionPredio> dsDescripcionPredioCatastral(Long idUnidadCatastral);
    
    List<DtoZonificacion> dsZonificacion(Long idUnidadCatastral);
    
    List<DtoLinderos> dsLinderos (Long idUnidadCatastral);

    List<DtoServiciosPredio> dsServiciosPredioCatastral(Long idUnidadCatastral);

    List<DtoConstrucciones> dsConstruccionesCatastral(Long idUnidadCatastral);
    
    List<DtoPorcentajeBienComun> dsPorcentajeBienComun(Long idUnidadCatastral);

    List<DtoObrasComplementarias> dsObrasComplementariasCatastral(Long idUnidadCatastral);

    List<DtoDocumentos> dsDocumentosCatastral(Long idUnidadCatastral);

    List<DtoIncripcionPredioCatastral> dsInscripcionPredioCatastral(Long idUnidadCatastral);

    List<DtoInformacionComplementaria> dsInfoComplementariaCatastral(Long idUnidadCatastral);

    List<DtoObservaciones> dsObservacionesCatastral(Long idUnidadCatastral);

    List<DtoEvaluacionPredioCatastral> dsEvaluacionPredioCatastral(Long idUnidadCatastral);

}
