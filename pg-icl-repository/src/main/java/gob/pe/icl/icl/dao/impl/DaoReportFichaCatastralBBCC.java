/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoReportFichaCatastralBBCC;
import gob.pe.icl.icl.dto.reports.DtoDocumentoDatoRegistral;
import gob.pe.icl.icl.dto.reports.DtoEdificacionBienComun;
import gob.pe.icl.icl.dto.reports.DtoRecapitulacionBienComun;
import gob.pe.icl.icl.dto.reports.DtoRecapitulacionEdificio;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastralBBCC extends AbstractJpaDao<UnidadCatastral> implements InterDaoReportFichaCatastralBBCC {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralBBCC() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoEdificacionBienComun> dsEdificacionBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct interior.nombre_edificacion,"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion tipoDeEdificacion"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_interior interior "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 18 and is_persistente = true) tipoEdificacion on tipoEdificacion.id = interior.id_tipo_edificacion"));
        sql.append(sharedUtil.append("where  interior.is_persistente = true and interior.id_unidad_catastral = ?"));
        List<DtoEdificacionBienComun> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoEdificacionBienComun dto = new DtoEdificacionBienComun();
                        dto.setNombreEdificacion(rs.getString("nombre_edificacion"));
                        dto.setTipoDeEdificacion(rs.getString("tipoDeEdificacion"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoRecapitulacionEdificio> dsRecapitulacionEdificio(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadcatastral.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.porcentaje porcentaje,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_terreno_comun areaTerrenoComun,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_construida_comun areaConstruidaComun,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_otras_instalacion_comun areaOtrasInstalacionComun"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral "));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_recapitulacion_edificios recapitulacionEdificios on recapitulacionEdificios.id_unidad_catastral = unidadCatastral.id and recapitulacionEdificios.is_persistente  = true"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente  = true and unidadcatastral.id = ?"));
        List<DtoRecapitulacionEdificio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoRecapitulacionEdificio dto = new DtoRecapitulacionEdificio();
                        dto.setEdificio(rs.getString("codigoEdificacion"));
                        dto.setPorcentaje(rs.getDouble("porcentaje"));
                        dto.setAtc(rs.getDouble("areaTerrenoComun"));
                        dto.setAcc(rs.getDouble("areaConstruidaComun"));
                        dto.setAoic(rs.getDouble("areaOtrasInstalacionComun"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoRecapitulacionBienComun> dsRecapitulacionBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastralRef.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_entrada codigoEntrada,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_piso codigoPiso,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_unidad codigoUnidad,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.porcentaje porcentaje,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_terreno_comun areaTerrenoComun,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_construida_comun areaConstruidaComun,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_otras_instalacion_comun areaOtrasInstalacionComun"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_recapitulacion_bc recapitulacionBienesComunes"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_unidad_catastral unidadCatastralRef on unidadCatastralRef.id=recapitulacionBienesComunes.id_unidad_catastral_ref"));
        sql.append(sharedUtil.append("where recapitulacionBienesComunes.is_persistente  = true and recapitulacionBienesComunes.id_unidad_catastral = ?"));
        sql.append(sharedUtil.append("order by recapitulacionBienesComunes.id_unidad_catastral_ref asc"));
        List<DtoRecapitulacionBienComun> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoRecapitulacionBienComun dto = new DtoRecapitulacionBienComun();
                        dto.setEdificacion(rs.getString("codigoEdificacion"));
                        dto.setEntrada(rs.getString("codigoEntrada"));
                        dto.setPiso(rs.getString("codigoPiso"));
                        dto.setUnidad(rs.getString("codigoUnidad"));
                        dto.setPorcentaje(rs.getDouble("porcentaje"));
                        dto.setAtc(rs.getDouble("areaTerrenoComun"));
                        dto.setAcc(rs.getDouble("areaConstruidaComun"));
                        dto.setAoic(rs.getDouble("areaOtrasInstalacionComun"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDocumentoDatoRegistral> dsDocumentoDatoRegistral(Long idUnidadCatastral) {
        return new ArrayList();
    }

}
