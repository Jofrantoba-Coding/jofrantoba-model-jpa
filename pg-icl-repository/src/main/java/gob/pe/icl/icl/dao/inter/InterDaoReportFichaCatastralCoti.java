/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.reports.DtoCotitularCatastral;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralCoti extends InterCrud<UnidadCatastral> {

    List<DtoCotitularCatastral> dsDatosCotitular(Long idUnidadCatastral);
}
