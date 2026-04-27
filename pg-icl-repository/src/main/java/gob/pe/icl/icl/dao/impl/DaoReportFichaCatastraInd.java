/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
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
import gob.pe.icl.icl.dto.reports.DtoUbicacionPredCatastralHabUrb;
import gob.pe.icl.icl.dto.reports.DtoPorcentajeBienComun;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import gob.pe.icl.icl.dao.inter.InterDaoReportFichaCatastraInd;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastraInd extends AbstractJpaDao<UnidadCatastral> implements InterDaoReportFichaCatastraInd {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastraInd() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public DtoUnidadCatastral dsMain(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_departamento codigoDepartamento,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_provincia codigoProvincia,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_distrito codigoDistrito,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_sector codigoSector,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_manzana codigoManzana,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_lote codigoLote,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_entrada codigoEntrada,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_piso codigoPiso,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_unidad codigoUnidad,"));
        sql.append(sharedUtil.append("unidadCatastral.dc dc,"));
        sql.append(sharedUtil.append("(case when lotecatastral.cuc is not null and unidadCatastral.cuc_uc is not null then lotecatastral.cuc || '-' ||   lpad(CAST(unidadCatastral.cuc_uc AS VARCHAR), 4, '0') else "));
        sql.append(sharedUtil.append("(case when lotecatastral.cuc  is not null  and unidadCatastral.cuc_uc is null then lotecatastral.cuc  end) end) cuc,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_contribuyente_rentas codigoContribuyenteRentas,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_predial_rentas codigoPredialRentas"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?" ));
        DtoUnidadCatastral dto = new DtoUnidadCatastral();
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
                        dto.setCuc(rs.getString("cuc"));
                        dto.setCodigoDepartamento(rs.getString("codigoDepartamento"));
                        dto.setCodigoProvincia(rs.getString("codigoProvincia"));
                        dto.setCodigoDistrito(rs.getString("codigoDistrito"));
                        dto.setCodigoSector(rs.getString("codigoSector"));
                        dto.setCodigoManzana(rs.getString("codigoManzana"));
                        dto.setCodigoLote(rs.getString("codigoLote"));
                        dto.setCodigoEdificacion(rs.getString("codigoEdificacion"));
                        dto.setCodigoEntrada(rs.getString("codigoEntrada"));
                        dto.setCodigoPiso(rs.getString("codigoPiso"));
                        dto.setCodigoUnidad(rs.getString("codigoUnidad"));
                        dto.setDc(rs.getLong("dc"));
                        dto.setCodigoContribuyenteRentas(rs.getString("codigoContribuyenteRentas"));
                        dto.setCodigoPredialRentas(rs.getString("codigoPredialRentas"));
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return dto;
    }

    @Override
    public List<DtoUbicacionPredioCatastral> dsUbicacionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct via.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoDeVia,"));
        sql.append(sharedUtil.append("via.descripcion nombreDeVia,"));
        sql.append(sharedUtil.append("tipoPuerta.descripcion tipoDePuerta ,"));
        sql.append(sharedUtil.append("(case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra)!= '' then  TRIM(numeracion.numero)||'-'|| TRIM(numeracion.letra) else (case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra) = '' then  TRIM(numeracion.numero) else (case when TRIM(numeracion.numero) = '' and TRIM(numeracion.letra) != '' then  TRIM(numeracion.letra) else'' end )end)end ) nroMunicipal,"));
        sql.append(sharedUtil.append("tipoCondicionNumeracion.descripcion condNumer,"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion tipoDeEdificacion,"));
        sql.append(sharedUtil.append("tipoInterior.descripcion tipoDeInterior,"));
        sql.append(sharedUtil.append("(case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior)!= '' then  TRIM(interior.interior)||'-'|| TRIM(interior.letra_interior) else (case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior) = '' then  TRIM(interior.interior) else (case when TRIM(interior.interior) = '' and TRIM(interior.letra_interior) != '' then  TRIM(interior.letra_interior) else'' end )end)end ) nroInterior"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_interior interior on interior.id_unidad_catastral = unidadCatastral.id and interior.is_persistente=true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente=true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_numeracion numeracion on numeracion.id = interior.id_numeracion and numeracion.is_persistente=true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tg_manzana_via manzanaVia on manzanaVia.id= numeracion.id_manzana_via and manzanaVia.is_persistente=true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tg_via_cuadra viacuadra on viacuadra.id = manzanaVia.id_via_cuadra and viacuadra.is_persistente=true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tm_via via on via.id = viacuadra.id_via and via.is_persistente=true"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on  tipoVia.id = via.id_tipo_via"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 2 and is_persistente = true) tipoPuerta on tipoPuerta.id = numeracion.id_tipo_puerta"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 17 and is_persistente = true) tipoCondicionNumeracion on tipoCondicionNumeracion.id = numeracion.id_condicion_numeracion"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 18 and is_persistente = true) tipoEdificacion on tipoEdificacion.id = interior.id_tipo_edificacion"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 19 and is_persistente = true) tipoInterior on tipoInterior.id = interior.id_tipo_interior"));
        sql.append(sharedUtil.append("where  unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        /*sql.append(sharedUtil.append("group by via.codigo_via, tipoVia.descripcion , via.descripcion ,tipoPuerta.descripcion ,"));
        sql.append(sharedUtil.append("(case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra)!= '' then  TRIM(numeracion.numero)||'-'|| TRIM(numeracion.letra) else (case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra) = '' then  TRIM(numeracion.numero) else (case when TRIM(numeracion.numero) = '' and TRIM(numeracion.letra) != '' then  TRIM(numeracion.letra) else'' end )end)end ) , tipoCondicionNumeracion.descripcion,"));
        sql.append(sharedUtil.append("(case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior)!= '' then  TRIM(interior.interior)||'-'|| TRIM(interior.letra_interior) else (case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior) = '' then  TRIM(interior.interior) else (case when TRIM(interior.interior) = '' and TRIM(interior.letra_interior) != '' then  TRIM(interior.letra_interior) else'' end )end)end ),"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion , tipoInterior.descripcion ;"));*/
        //System.out.println("dsUbicacionPredioCatastral: " + sql.toString());
        List<DtoUbicacionPredioCatastral> datasource = new ArrayList();
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
                        DtoUbicacionPredioCatastral dto = new DtoUbicacionPredioCatastral();
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoDeVia"));
                        dto.setNombreDeVia(rs.getString("nombreDeVia"));
                        dto.setTipoDePuerta(rs.getString("tipoDePuerta"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setCondNumer(rs.getString("condNumer"));
                        dto.setTipoDeEdificacion(rs.getString("tipoDeEdificacion"));
                        dto.setTipoDeInterior(rs.getString("tipoDeInterior"));
                        dto.setNroInterior(rs.getString("nroInterior"));
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
    public List<DtoUbicacionPredCatastralHabUrb> dsUbicacionPredCatastralHabUrb(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct habilitacionUrbana.codigo codigoHu,"));
        sql.append(sharedUtil.append("habilitacionUrbana.descripcion nombreHabilitacionUrbana,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.lote_urbano lote,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.manzana_urbana manzana,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.sub_lote_urbano subLote"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente=true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tgv_lote_habilitacion_urbana loteHabilitacionUrbana  on loteHabilitacionUrbana.id_lote_catastral = loteCatastral.id and loteHabilitacionUrbana.is_persistente=true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_habilitacion_urbana habilitacionUrbana on habilitacionUrbana.id  =loteHabilitacionUrbana.id_habilitacion_urbana and habilitacionUrbana.is_persistente=true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        //sql.append(sharedUtil.append("group by habilitacionUrbana.codigo , habilitacionUrbana.descripcion ,loteHabilitacionUrbana.zona_sector_etapa,"));
        //sql.append(sharedUtil.append("loteHabilitacionUrbana.lote_urbano , loteHabilitacionUrbana.manzana_urbana , loteHabilitacionUrbana.sub_lote_urbano"));
        List<DtoUbicacionPredCatastralHabUrb> datasource = new ArrayList();
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
                        DtoUbicacionPredCatastralHabUrb dto = new DtoUbicacionPredCatastralHabUrb();
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabilitacionUrbana(rs.getString("nombreHabilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
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
    public List<DtoIdentificacionTitularCatastral> dsIdentificacionTitularCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastral.id,"));
        sql.append(sharedUtil.append("tipoTitular.descripcion tipo_titular,"));
        sql.append(sharedUtil.append("estadoCivil.descripcion estado_civil,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion tipo_doc_identidad,"));
        sql.append(sharedUtil.append("titular.id_tipo_titular  id_tipo_titular,"));
        sql.append(sharedUtil.append("titular.numero_documento  nro_doc,"));
        sql.append(sharedUtil.append("titular.nombre_ruc nombres,"));
        sql.append(sharedUtil.append("titular.apellido_paterno apellido_paterno,"));
        sql.append(sharedUtil.append("titular.apellido_materno apellido_materno,"));
        sql.append(sharedUtil.append("titular.id_tipo_persona_juridica persona_juridica,"));
        sql.append(sharedUtil.append("tipoPersonaJuridica.descripcion tipo_persona_juridica "));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titularidad titularidad on unidadCatastral.id=titularidad.id_unidad_catastral and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titular titular on titularidad.id=titular.id_titularidad  and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 76 and is_persistente = true ) tipoTitular on  titular.id_tipo_titular=tipoTitular.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 77 and is_persistente = true ) estadoCivil on  titular.id_estado_civil=estadoCivil.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on titular.id_tipo_documento=tipoDocumento.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 94 and is_persistente = true ) tipoPersonaJuridica on titular.id_tipo_persona_juridica=tipoPersonaJuridica.id "));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true "));
        sql.append(sharedUtil.append("and  unidadCatastral.id = ? "));
        sql.append(sharedUtil.append("order by titular.id asc limit 1 offset 0"));
        List<DtoIdentificacionTitularCatastral> datasource = new ArrayList();
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
                        DtoIdentificacionTitularCatastral dto = new DtoIdentificacionTitularCatastral();
                        dto.setTipoDeTitular(rs.getString("tipo_titular"));
                        dto.setPersonaJuridica(rs.getString("tipo_persona_juridica"));
                        Long idTipoTitular = rs.getLong("id_tipo_titular");
                        if (idTipoTitular == 80l) {
                            dto.setNroRuc(rs.getString("nro_doc"));
                            dto.setRazonSocial(rs.getString("nombres"));
                        } else {
                            dto.setTipoDocIdentidad(rs.getString("tipo_doc_identidad"));
                            dto.setNombres(rs.getString("nombres"));
                            dto.setNroDoc(rs.getString("nro_doc"));
                            dto.setApellidoPaterno(rs.getString("apellido_paterno"));
                            dto.setApellidoMaterno(rs.getString("apellido_materno"));
                            dto.setEstadoCivil(rs.getString("estado_civil"));
                        }
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
    public List<DtoDomicilioFisTitularCatastral> dsDomicilioFisTitularCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("tipoUbicacion.descripcion ubicacion,"));
        sql.append(sharedUtil.append("departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("titular.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("titular.nombre_via nombreDeVia,"));
        sql.append(sharedUtil.append("titular.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("titular.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("titular.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("titular.nombre_hu nombreHabitilitacionUrbana,"));
        sql.append(sharedUtil.append("titular.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("titular.manzana manzana,"));
        sql.append(sharedUtil.append("titular.lote lote,"));
        sql.append(sharedUtil.append("titular.sub_lote subLote,"));
        sql.append(sharedUtil.append("titular.telefono telefono,"));
        sql.append(sharedUtil.append("titular.anexo anexo,"));
        sql.append(sharedUtil.append("titular.correo_electronico correoElectronico"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titularidad titularidad  on unidadCatastral.id  = titularidad.id_unidad_catastral and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tgv_titular titular  on titularidad.id  =  titular.id_titularidad  and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_departamento departamento  on  titular.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_provincia provincia on  titular.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_distrito distrito on titular.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 95 and is_persistente = true ) tipoUbicacion on  titular.id_tipo_ubicacion = tipoUbicacion.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on titular.id_tipo_via = tipoVia.id"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true  and unidadCatastral.id = ? and"));
        sql.append(sharedUtil.append("titular.id="));
        sql.append(sharedUtil.append("(select  min(titular.id) from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titularidad titularidad on titularidad.id_unidad_catastral = unidadCatastral.id and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titular titular on titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.id=? and unidadCatastral.is_persistente = true)"));
        List<DtoDomicilioFisTitularCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    ps.setLong(2, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDomicilioFisTitularCatastral dto = new DtoDomicilioFisTitularCatastral();
                        dto.setUbicacion(rs.getString("ubicacion"));
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoVia"));
                        dto.setNombreDeVia(rs.getString("nombreDeVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabitilitacionUrbana(rs.getString("nombreHabitilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setCorreoElectronico(rs.getString("correoElectronico"));
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
    public List<DtoCaracteristicasTitularidad> dsCaractTitularidadCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionTitularidad.descripcion condicion_titularidad,"));
        sql.append(sharedUtil.append("formaAdquisicion.descripcion forma_adquisicion ,"));
        sql.append(sharedUtil.append("titularidad.fecha_adquisicion fechaAdquision "));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral "));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titularidad titularidad  on titularidad.id_unidad_catastral = unidadCatastral.id"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titular titular on titular.id_titularidad  = titularidad.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 96 and is_persistente = true ) condicionTitularidad on  titularidad.id_condicion_titularidad = condicionTitularidad.id "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 111 and is_persistente = true ) formaAdquisicion on titularidad.id_forma_adquisicion =  formaAdquisicion.id "));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadCatastral.id = ?"));
        sql.append(sharedUtil.append("group by unidadCatastral.id ,titularidad.id,titularidad.id_condicion_titularidad ,condicionTitularidad.descripcion ,	titularidad.id_forma_adquisicion,formaAdquisicion.descripcion ,titularidad.fecha_adquisicion"));
        List<DtoCaracteristicasTitularidad> datasource = new ArrayList();
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
                        DtoCaracteristicasTitularidad dto = new DtoCaracteristicasTitularidad();
                        dto.setCondicionTitular(rs.getString("condicion_titularidad"));
                        dto.setFormaAdquision(rs.getString("forma_adquisicion"));
                        dto.setFechaAdquision(rs.getDate("fechaAdquision"));
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
    public List<DtoDescripcionPredio> dsDescripcionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("clasificacionPredio.descripcion clasificacionPredio,"));
        sql.append(sharedUtil.append("clasificacionPredioOtro.descripcion clasificacionPredioOtro,"));
        sql.append(sharedUtil.append("predioEn.descripcion predioCatastral,"));
        sql.append(sharedUtil.append("unidadcatastral.predio_en_otro predioCatastralOtro,"));
        sql.append(sharedUtil.append("usoEspecifico.codigo codigoUso,"));
        sql.append(sharedUtil.append("usoEspecifico.descripcion usoPredioCastral,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_adquirida areaTerrenoAdquirida,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_verificada areaTerrenoVerficada"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_uso_especifico usoEspecifico  on unidadCatastral.id_uso_especifico = usoEspecifico.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 112 and is_persistente = true) clasificacionPredio on unidadCatastral.id_clasificacion_predio = clasificacionPredio.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 135 and is_persistente = true) predioEn on unidadCatastral.id_predio_en =  predioEn.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 134 and is_persistente = true) clasificacionPredioOtro on unidadCatastral.id_clasificacion_predio = clasificacionPredioOtro.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        /*sql.append(sharedUtil.append("group by clasificacionPredio.descripcion,"));
        sql.append(sharedUtil.append("clasificacionPredioOtro.descripcion,"));
        sql.append(sharedUtil.append("predioEn.descripcion,"));
        sql.append(sharedUtil.append("unidadcatastral.predio_en_otro,"));
        sql.append(sharedUtil.append("usoEspecifico.codigo,"));
        sql.append(sharedUtil.append("usoEspecifico.descripcion,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_adquirida,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_verificada"));*/
        List<DtoDescripcionPredio> datasource = new ArrayList();
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
                        DtoDescripcionPredio dto = new DtoDescripcionPredio();
                        dto.setClasificacionPredio(rs.getString("clasificacionPredio"));
                        dto.setClasificacionPredioOtro(rs.getString("clasificacionPredioOtro"));
                        dto.setPredioCatastral(rs.getString("predioCatastral"));
                        dto.setPredioCatastralOtro(rs.getString("predioCatastralOtro"));
                        dto.setCodigoUso(rs.getString("codigoUso"));
                        dto.setUsoPredioCastral(rs.getString("usoPredioCastral"));
                        dto.setAreaTerrenoAdquirida(rs.getDouble("areaTerrenoAdquirida"));
                        dto.setAreaTerrenoVerficada(rs.getDouble("areaTerrenoVerficada"));
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
    public List<DtoZonificacion> dsZonificacion(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("zonificacion.descripcion zonificacion"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral and loteCastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tgv_lote_zonificacion loteZonificacion  on loteCastral.id = loteZonificacion.id_lote_catastral and loteZonificacion.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tm_zonificacion zonificacion  on loteZonificacion.id_zonificacion = zonificacion.id and zonificacion.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoZonificacion> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoZonificacion dto = new DtoZonificacion();
                        dto.setZonificacion(rs.getString("zonificacion"));
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
    public List<DtoLinderos> dsLinderos(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("tiposLinderos.descripcion tipoLinderos,"));
        sql.append(sharedUtil.append("linderos.medida_campo medidaCampo,"));
        sql.append(sharedUtil.append("linderos.colindancia colindanciaCampo"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral and loteCastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join icl.catastro.tgv_linderos linderos on loteCastral.id = linderos.id_lote_catastral and linderos.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 224 and is_persistente = true) tiposLinderos on linderos.id_tipo_lindero = tiposLinderos.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ? order by tiposLinderos.descripcion"));
        List<DtoLinderos> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoLinderos dto = new DtoLinderos();
                        dto.setMedidaCampo(rs.getDouble("medidaCampo"));
                        dto.setColindanciaCampo(rs.getString("colindanciaCampo"));
                        dto.setTipoLinderos(rs.getString("tipoLinderos"));
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
    public List<DtoServiciosPredio> dsServiciosPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_luz = '1' then 'SI' else 'NO' end) luz,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_agua ='1'then 'SI' else 'NO' end) agua,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_telefono ='1' then 'SI' else 'NO' end) telefono,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_desague = '1'then 'SI' else 'NO' end) desague,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_gas = '1' then 'SI' else 'NO' end) gas,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_internet ='1' then 'SI' else 'NO' end) internet,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_cable = '1' then 'SI' else 'NO' end) conexionTv"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and  unidadCatastral.id= ?"));
        List<DtoServiciosPredio> datasource = new ArrayList();
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
                        DtoServiciosPredio dto = new DtoServiciosPredio();
                        dto.setLuz(rs.getString("luz"));
                        dto.setAgua(rs.getString("agua"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setDesague(rs.getString("desague"));
                        dto.setGas(rs.getString("gas"));
                        dto.setInternet(rs.getString("internet"));
                        dto.setConexionTv(rs.getString("conexionTv"));
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
    public List<DtoConstrucciones> dsConstruccionesCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select nivel.descripcion nroPisoSotano,"));
        sql.append(sharedUtil.append("construcciones.id_mes ,"));
        sql.append(sharedUtil.append("mes.descripcion  mes,"));
        sql.append(sharedUtil.append("construcciones.anio anio,"));
        sql.append(sharedUtil.append("materialEstructural.descripcion mep,"));
        sql.append(sharedUtil.append("estadoConservacion.descripcion ecs,"));
        sql.append(sharedUtil.append("estadoConstruccion.descripcion ecc,"));
        sql.append(sharedUtil.append("construcciones.muros murosColumnas,"));
        sql.append(sharedUtil.append("construcciones.techos techos,"));
        sql.append(sharedUtil.append("construcciones.pisos pisos,"));
        sql.append(sharedUtil.append("construcciones.puertas_ventanas puertasVentanas,"));
        sql.append(sharedUtil.append("construcciones.revestimiento revest,"));
        sql.append(sharedUtil.append("construcciones.banios banios,"));
        sql.append(sharedUtil.append("construcciones.instalaciones_electricas instElectricas,"));
        sql.append(sharedUtil.append("construcciones.area_verificada areaVerificada,"));
        sql.append(sharedUtil.append("uca.descripcion uca"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_construcciones construcciones on construcciones.id_unidad_catastral = unidadCatastral.id and construcciones.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 248 and is_persistente = true) nivel on  construcciones.id_nivel = nivel.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 249 and is_persistente = true) mes on  construcciones.id_mes = mes.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 167 and is_persistente = true) materialEstructural on construcciones.id_material_estructural =materialEstructural.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 168 and is_persistente = true) estadoConservacion on construcciones.id_estado_conservacion =  estadoConservacion.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 169 and is_persistente = true) estadoConstruccion on construcciones.id_estado_construccion =  estadoConstruccion.id"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 170 and is_persistente = true) uca on construcciones.id_uca = uca.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ? order by construcciones.id_nivel asc"));
        List<DtoConstrucciones> datasource = new ArrayList();
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
                        DtoConstrucciones dto = new DtoConstrucciones();
                        dto.setNroPisoSotano(rs.getString("nroPisoSotano"));
                        dto.setMes(rs.getString("mes"));
                        dto.setAnio(rs.getLong("anio"));
                        dto.setMep(rs.getString("mep"));
                        dto.setEcs(rs.getString("ecs"));
                        dto.setEcc(rs.getString("ecc"));
                        dto.setMurosColumnas(rs.getString("murosColumnas"));
                        dto.setTechos(rs.getString("techos"));
                        dto.setPisos(rs.getString("pisos"));
                        dto.setPuertasVentanas(rs.getString("puertasVentanas"));
                        dto.setRevest(rs.getString("revest"));
                        dto.setBanios(rs.getString("banios"));
                        dto.setInstElectricas(rs.getString("instElectricas"));
                        dto.setAreaVerificada(rs.getDouble("areaVerificada"));
                        dto.setUca(rs.getString("uca"));
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
    public List<DtoPorcentajeBienComun> dsPorcentajeBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastral.porc_bc_terreno_legal terrenoLegal,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_terreno_fisico terrenoFisico,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_construccion_legal construccionesLegal,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_construccion_fisico construccionesFisico"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        List<DtoPorcentajeBienComun> datasource = new ArrayList();
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
                        DtoPorcentajeBienComun dto = new DtoPorcentajeBienComun();
                        dto.setTerrenoLegal(rs.getString("terrenoLegal"));
                        dto.setTerrenoFisico(rs.getString("terrenoFisico"));
                        dto.setConstruccionesLegal(rs.getString("construccionesLegal"));
                        dto.setConstruccionesFisico(rs.getString("construccionesFisico"));
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
    public List<DtoObrasComplementarias> dsObrasComplementariasCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct tipoOtrasInstalaciones.codigo codigo,"));
        sql.append(sharedUtil.append("tipoOtrasInstalaciones.descripcion descripcion,"));
        sql.append(sharedUtil.append("mes.descripcion mes,"));
        sql.append(sharedUtil.append("otrasInstalaciones.anio anio,"));
        sql.append(sharedUtil.append("materialEstructural.descripcion mep,"));
        sql.append(sharedUtil.append("estadoConservacion.descripcion esc,"));
        sql.append(sharedUtil.append("estadoConstruccion.descripcion ecc,"));
        sql.append(sharedUtil.append("otrasInstalaciones.producto_total productoTotal ,"));
        sql.append(sharedUtil.append("tipoOtrasInstalaciones.unidad_medida unidadMedida,"));
        sql.append(sharedUtil.append("uca.descripcion uca"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_otras_instalaciones otrasInstalaciones on otrasInstalaciones.id_unidad_catastral = unidadCatastral.id and otrasInstalaciones.is_persistente = true"));
        sql.append(sharedUtil.append("inner join icl.catastro.tm_tipo_otras_instalaciones tipoOtrasInstalaciones on tipoOtrasInstalaciones.id  = otrasInstalaciones.id_tipo_otras_instalaciones and tipoOtrasInstalaciones.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 249 and is_persistente = true) mes on mes.id = otrasInstalaciones.id_mes"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 167 and is_persistente = true) materialEstructural on materialEstructural.id = otrasInstalaciones.id_material_estructural"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 168 and is_persistente = true) estadoConservacion on estadoConservacion.id = otrasInstalaciones.id_estado_conservacion"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 169 and is_persistente = true) estadoConstruccion on estadoConstruccion.id = otrasInstalaciones.id_estado_construccion"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 170 and is_persistente = true) uca on uca.id = otrasInstalaciones.id_uca"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoObrasComplementarias> datasource = new ArrayList();
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
                        DtoObrasComplementarias dto = new DtoObrasComplementarias();
                        dto.setCodigo(rs.getString("codigo"));
                        dto.setDescripcion(rs.getString("descripcion"));
                        dto.setMes(rs.getString("mes"));
                        dto.setAnio(rs.getLong("anio"));
                        dto.setMep(rs.getString("mep"));
                        dto.setEcs(rs.getString("esc"));
                        dto.setEcc(rs.getString("ecc"));
                        dto.setProductoTotal(rs.getDouble("productoTotal"));
                        dto.setUnidadMedida(rs.getString("unidadMedida"));
                        dto.setUca(rs.getString("uca"));
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
    public List<DtoDocumentos> dsDocumentosCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoDocumento.descripcion tipoDocumento,"));
        sql.append(sharedUtil.append("documentos.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("documentos.fecha_documento fecha,"));
        sql.append(sharedUtil.append("documentos.area_autorizada areaAutorizada"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_documentos documentos on documentos.id_unidad_catastral = unidadCatastral.id  and documentos.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from icl.catastro.tm_parametrias where id_parametria_padre = 171 and is_persistente = true) tipoDocumento on documentos.id_tipo_documento = tipoDocumento.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoDocumentos> datasource = new ArrayList();
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
                        DtoDocumentos dto = new DtoDocumentos();
                        dto.setTipoDocumento(rs.getString("tipoDocumento"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setFecha(rs.getDate("fecha"));
                        dto.setAreaAutorizada(rs.getDouble("areaAutorizada"));
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
    public List<DtoIncripcionPredioCatastral> dsInscripcionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoPartidaRegistral.descripcion tipoPartidadRegistral,"));
        sql.append(sharedUtil.append("inscripcioncatastral.numero numero ,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fojas fojas,"));
        sql.append(sharedUtil.append("inscripcioncatastral.asiento asiento,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fecha_inscripcion fechaInscripcionPredio,"));
        sql.append(sharedUtil.append("declaratoriafabrica.descripcion declaratoriaFabrica,"));
        sql.append(sharedUtil.append("inscripcioncatastral.inscripcion_fabrica asInsFabrica,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fecha_inscripcion_fabrica  fechaInscripcionFabrica"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_inscripcion_catastral inscripcionCatastral on inscripcionCatastral.id_unidad_catastral = unidadcatastral.id and inscripcionCatastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 172 and is_persistente = true) tipoPartidaRegistral on  inscripcioncatastral.id_tipo_partida_registral = tipoPartidaRegistral.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 173 and is_persistente = true) declaratoriafabrica on inscripcioncatastral.id_declaratoria_fabrica = declaratoriafabrica.id"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadcatastral.id  = ?"));
        List<DtoIncripcionPredioCatastral> datasource = new ArrayList();
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
                        DtoIncripcionPredioCatastral dto = new DtoIncripcionPredioCatastral();
                        dto.setTipoPartidadRegistral(rs.getString("tipoPartidadRegistral"));
                        dto.setNumero(rs.getString("numero"));
                        dto.setFojas(rs.getString("fojas"));
                        dto.setAsiento(rs.getString("asiento"));
                        dto.setFechaInscripcionPredio(rs.getDate("fechaInscripcionPredio"));
                        dto.setDeclaratoriaFabrica(rs.getString("declaratoriaFabrica"));
                        dto.setAsInsFabrica(rs.getString("asInsFabrica"));
                        dto.setFechaInscripcionFabrica(rs.getDate("fechaInscripcionFabrica"));
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
    public List<DtoInformacionComplementaria> dsInfoComplementariaCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionDeclarante.descripcion condicionDeclarante,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion td,"));
        sql.append(sharedUtil.append("litigantes.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("litigantes.nombres apellidosNombresLitigantes,"));
        sql.append(sharedUtil.append("litigantes.codigo_contribuyente codigoContribuyente,"));
        sql.append(sharedUtil.append("estadoLlenado.descripcion estadoLlenadoFicha,"));
        sql.append(sharedUtil.append("informacionComplementaria.nro_habitantes nroHabitantes,"));
        sql.append(sharedUtil.append("informacionComplementaria.nro_familias nroFamilias,"));
        sql.append(sharedUtil.append("mantenimiento.descripcion mantenimiento"));
        //sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        //sql.append(sharedUtil.append("left join icl.catastro.tgv_informacion_compl informacionComplementaria  on unidadcatastral.id =  informacionComplementaria.id_unidad_catastral and informacionComplementaria.is_persistente = true"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_informacion_compl informacionComplementaria  "));
        sql.append(sharedUtil.append("left join icl.catastro.tgv_litigantes litigantes on  informacionComplementaria.id_unidad_catastral = litigantes.id_unidad_catastral and litigantes.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 174 and is_persistente = true) condicionDeclarante on  informacionComplementaria.id_condicion_declarante = condicionDeclarante.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on litigantes.id_tipo_documento = tipoDocumento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 312 and is_persistente = true ) estadoLlenado on  informacionComplementaria.id_estado_llenado = estadoLlenado.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 175 and is_persistente = true ) mantenimiento on informacionComplementaria.id_mantenimiento =  mantenimiento.id"));
        sql.append(sharedUtil.append("where informacionComplementaria.is_persistente = true and informacionComplementaria.id_unidad_catastral  = ?"));
        List<DtoInformacionComplementaria> datasource = new ArrayList();
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
                        DtoInformacionComplementaria dto = new DtoInformacionComplementaria();
                        dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setTd(rs.getString("td"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setApellidosNombresLitigantes(rs.getString("apellidosNombresLitigantes"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
                        dto.setNroHabitantes(rs.getInt("nroHabitantes"));
                        dto.setNroFamilias(rs.getInt("nroFamilias"));
                        dto.setMantenimiento(rs.getString("mantenimiento"));
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
    public List<DtoObservaciones> dsObservacionesCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        List<DtoObservaciones> datasource = new ArrayList();
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
                        DtoObservaciones dto = new DtoObservaciones();
                        /*dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setTd(rs.getString("td"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setApellidosNombresLitigantes(rs.getString("apellidosNombresLitigantes"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
                        dto.setNroHabitantes(rs.getInt("nroHabitantes"));
                        dto.setNroFamilias(rs.getInt("nroFamilias"));
                        dto.setMantenimiento(rs.getString("mantenimiento"));*/
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
    public List<DtoEvaluacionPredioCatastral> dsEvaluacionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct informacionComplementaria.eva_lote_colindante  areaLoteColidante,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_area_publica areaPublica,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_jardin_aislamiento jardinAislamiento,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_intangible  areaIntangible"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_informacion_compl informacionComplementaria  on informacionComplementaria.id_unidad_catastral = unidadcatastral.id and informacionComplementaria.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadcatastral.id  = ?"));
        //sql.append(sharedUtil.append("group  by unidadcatastral.id , informacionComplementaria.eva_lote_colindante , informacionComplementaria.eva_area_publica , informacionComplementaria.eva_jardin_aislamiento , informacionComplementaria.eva_intangible ;"));
        List<DtoEvaluacionPredioCatastral> datasource = new ArrayList();
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
                        DtoEvaluacionPredioCatastral dto = new DtoEvaluacionPredioCatastral();
                        dto.setAreaLoteColidante(rs.getDouble("areaLoteColidante"));
                        dto.setAreaPublica(rs.getDouble("areaPublica"));
                        dto.setJardinAislamiento(rs.getDouble("jardinAislamiento"));
                        dto.setAreaIntangible(rs.getDouble("areaIntangible"));
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
}
