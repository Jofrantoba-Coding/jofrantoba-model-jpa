/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.reports.DtoActividadEconomica;
import gob.pe.icl.icl.dto.reports.DtoAutorizacionAnuncio;
import gob.pe.icl.icl.dto.reports.DtoConductorActEco;
import gob.pe.icl.icl.dto.reports.DtoDomicilioFisConductorActEco;
import gob.pe.icl.icl.dto.reports.DtoInformacionComplActEco;
import gob.pe.icl.icl.dto.reports.DtoObservaciones;
import gob.pe.icl.icl.dto.reports.DtoTipoActividadEconomica;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralActEco extends InterCrud<UnidadCatastral> {

    List<DtoConductorActEco> dsConductorActEco(Long idUnidadCatastral, Long idActividadEconomica);

    List<DtoDomicilioFisConductorActEco> dsDomicilioFisConductorActEco(Long idActividadEconomica);

    List<DtoTipoActividadEconomica> dsTipoActividadEconomica(Long idActividadEconomica);

    List<DtoActividadEconomica> dsActividadEconomica(Long idActividadEconomica);

    List<DtoAutorizacionAnuncio> dsAutorizacionAnuncio(Long idActividadEconomica);

    List<DtoInformacionComplActEco> dsInformacionComplActEco(Long idActividadEconomica);

    List<DtoObservaciones> dsObservacionesActEco(Long idActividadEconomica);
}
