/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoReportFichaCatastralActEco;
import gob.pe.icl.icl.dto.reports.DtoActividadEconomica;
import gob.pe.icl.icl.dto.reports.DtoAutorizacionAnuncio;
import gob.pe.icl.icl.dto.reports.DtoConductorActEco;
import gob.pe.icl.icl.dto.reports.DtoDomicilioFisConductorActEco;
import gob.pe.icl.icl.dto.reports.DtoInformacionComplActEco;
import gob.pe.icl.icl.dto.reports.DtoObservaciones;
import gob.pe.icl.icl.dto.reports.DtoTipoActividadEconomica;
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
public class DaoReportFichaCatastralActEco extends AbstractJpaDao<UnidadCatastral> implements InterDaoReportFichaCatastralActEco {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralActEco() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoConductorActEco> dsConductorActEco(Long idUnidadCatastral, Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoConductor.descripcion tipoConductor,"));
        sql.append(sharedUtil.append("actividadEconomica.nombre_comercial nombreComercial,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion tipoDocIdentidad,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_ruc nroRuc,"));
        sql.append(sharedUtil.append("actividadEconomica.nombre nombre,"));
        sql.append(sharedUtil.append("condicionConductor.descripcion condicionConductor"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_actividad_economica actividadEconomica"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on actividadEconomica.id_tipo_doc_ident = tipoDocumento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 317 and is_persistente = true ) tipoConductor on actividadEconomica.id_tipo_conductor = tipoConductor.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 320 and is_persistente = true ) condicionConductor on actividadEconomica.id_condicion_conductor = condicionConductor.id"));
        sql.append(sharedUtil.append("where actividadEconomica.is_persistente = true and actividadEconomica.id_unidad_catastral= ?"));
        sql.append(sharedUtil.append("and actividadEconomica.id = ?"));
        //System.out.println(sql.toString());
        List<DtoConductorActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    ps.setLong(2, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoConductorActEco dto = new DtoConductorActEco();
                        dto.setTipoDeConductor(rs.getString("tipoConductor"));
                        dto.setNombreComercial(rs.getString("nombreComercial"));
                        dto.setTipoDocumentoIdentidad(rs.getString("tipoDocIdentidad"));
                        dto.setNroDoc(rs.getString("nroDocumento"));
                        dto.setNroRuc(rs.getString("nroRuc"));
                        dto.setApellidosYNombresRazonSocialConductor(rs.getString("nombre"));
                        dto.setCondicionDelConductor(rs.getString("condicionConductor"));
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
    public List<DtoDomicilioFisConductorActEco> dsDomicilioFisConductorActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia ,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("conductorDomicilio.telefono telefono,"));
        sql.append(sharedUtil.append("conductorDomicilio.anexo anexo,"));
        sql.append(sharedUtil.append("conductorDomicilio.fax fax,"));
        sql.append(sharedUtil.append("conductorDomicilio.correo_electronico correoElectronico,"));
        sql.append(sharedUtil.append("conductorDomicilio.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("conductorDomicilio.nombre_via nombreVia,"));
        sql.append(sharedUtil.append("conductorDomicilio.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("conductorDomicilio.numero_edificcion nroEdificacion,"));
        sql.append(sharedUtil.append("conductordomicilio.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("conductorDomicilio.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("conductorDomicilio.nombre_hu nombreHabitilitacionUrbana,"));
        sql.append(sharedUtil.append("conductorDomicilio.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("conductorDomicilio.manzana manzana,"));
        sql.append(sharedUtil.append("conductorDomicilio.lote lote,"));
        sql.append(sharedUtil.append("conductorDomicilio.sub_lote subLote"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_conductor_domicilio conductorDomicilio"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_departamento departamento  on  conductorDomicilio.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_provincia provincia on  conductorDomicilio.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_distrito distrito on conductorDomicilio.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on conductorDomicilio.id_tipo_via = tipoVia.id "));
        sql.append(sharedUtil.append("where conductorDomicilio.is_persistente = true"));
        sql.append(sharedUtil.append("and conductorDomicilio.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoDomicilioFisConductorActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDomicilioFisConductorActEco dto = new DtoDomicilioFisConductorActEco();
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setCodigoDeVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoVia"));
                        dto.setNombreDeVia(rs.getString("nombreVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNombreDeEdificacion(rs.getString("nroEdificacion"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHu(rs.getString("nombreHabitilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setFax(rs.getString("fax"));
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
    public List<DtoTipoActividadEconomica> dsTipoActividadEconomica(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct tipoActEconoEspecifico.codigo codigoActividad,"));
        sql.append(sharedUtil.append("tipoActEconoEspecifico.descripcion descripcionActividad"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_autorizacion_municipal autorizacionMunicipal"));
        sql.append(sharedUtil.append("inner join icl.catastro.tm_tipo_act_econo_especifico tipoActEconoEspecifico on tipoActEconoEspecifico.id = autorizacionMunicipal.id_tipo_act_econo_especifico  and tipoActEconoEspecifico.is_persistente = true"));
        sql.append(sharedUtil.append("where autorizacionMunicipal.is_persistente = true"));
        sql.append(sharedUtil.append("and autorizacionMunicipal.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoTipoActividadEconomica> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoTipoActividadEconomica dto = new DtoTipoActividadEconomica();
                        dto.setCodigoActividad(rs.getString("codigoActividad"));
                        dto.setDescripcionDeLaActividad(rs.getString("descripcionActividad"));
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
    public List<DtoActividadEconomica> dsActividadEconomica(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct actividadEconomica.numero_expediente nroExpediente,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_licencia nroLicencia,"));
        sql.append(sharedUtil.append("actividadEconomica.fecha_expedicion fechaExpedicion,"));
        sql.append(sharedUtil.append("actividadEconomica.fecha_vencimiento fechaVencimiento,"));
        sql.append(sharedUtil.append("actividadEconomica.inicio_actividad inicioActividad,"));
        sql.append(sharedUtil.append("actividadEconomica.predio_catastral_aa predioCatastralAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.predio_catastral_av predioCatastralAreaVerificada,"));
        sql.append(sharedUtil.append("actividadEconomica.via_publica_aa viaPublicaAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.via_publica_av viaPublicaAreaVerificada,"));
        sql.append(sharedUtil.append("actividadEconomica.bien_comun_aa bienComunAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.bien_comun_av bienComunAreaVerificada"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_actividad_economica actividadEconomica"));
        sql.append(sharedUtil.append("where actividadEconomica.is_persistente = true"));
        sql.append(sharedUtil.append("and actividadEconomica.id = ?"));
        //System.out.println(sql.toString());
        List<DtoActividadEconomica> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoActividadEconomica dto = new DtoActividadEconomica();
                        dto.setNroDeExpediente(rs.getString("nroExpediente"));
                        dto.setNroDeLicencia(rs.getString("nroLicencia"));
                        dto.setFechaDeExpedicion(rs.getDate("fechaExpedicion"));
                        dto.setFechaDeVencimiento(rs.getDate("fechaVencimiento"));
                        dto.setInicioDeActividad(rs.getDate("inicioActividad"));
                        dto.setPredioCatastralAreaAutorizada(rs.getDouble("predioCatastralAreaAutorizada"));
                        dto.setPredioCatastralAreaVerificada(rs.getDouble("predioCatastralAreaVerificada"));
                        dto.setViaPublicaAreaAutorizada(rs.getDouble("viaPublicaAreaAutorizada"));
                        dto.setViaPublicaAreaVerificada(rs.getDouble("viaPublicaAreaVerificada"));
                        dto.setBienComunAreaAutorizada(rs.getDouble("bienComunAreaAutorizada"));
                        dto.setBienComunAreaVerificada(rs.getDouble("bienComunAreaVerificada"));
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
    public List<DtoAutorizacionAnuncio> dsAutorizacionAnuncio(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct anuncio.descripcion anuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_lados nroLados,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.area_autorizada_anuncio areaAutorizadaAnuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.area_verificada_anuncio areaVerificadaAnuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_expediente nroExpediente,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_licencia nroLicencia ,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.fecha_expedicion fechaExpedicion,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.fecha_vencimiento fechaVencimiento"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_autorizacion_anuncio autorizacionAnuncio"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 231 and is_persistente = true ) anuncio on autorizacionAnuncio.id_anuncio = anuncio.id"));
        sql.append(sharedUtil.append("where autorizacionAnuncio.is_persistente = true "));
        sql.append(sharedUtil.append("and autorizacionAnuncio.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoAutorizacionAnuncio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoAutorizacionAnuncio dto = new DtoAutorizacionAnuncio();
                        dto.setDescripcionDelTipoDeAnuncio(rs.getString("anuncio"));
                        dto.setNroDeLados(rs.getString("nroLados"));
                        dto.setAreaAutorizadaDelAnuncio(rs.getDouble("areaAutorizadaAnuncio"));
                        dto.setAreaVerificadaDelAnuncio(rs.getDouble("areaVerificadaAnuncio"));
                        dto.setNroExpediente(rs.getString("nroExpediente"));
                        dto.setNroLicencia(rs.getString("nroLicencia"));
                        dto.setFechaDeExpedicion(rs.getDate("fechaExpedicion"));
                        dto.setFechaDeVencimiento(rs.getDate("fechaVencimiento"));
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
    public List<DtoInformacionComplActEco> dsInformacionComplActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionDeclarante.descripcion condicionDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.condicion_declarante_otro condicionDeclaranteOtro,"));
        sql.append(sharedUtil.append("documentos.descripcion documentoPresentados,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.documento_presentado_otro documentoPresentadosOtro,"));
        sql.append(sharedUtil.append("estadoLlenado.descripcion estadoLlenadoFicha,"));
        sql.append(sharedUtil.append("mantenimiento.descripcion mantenimiento"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_informacion_compl_act_econ informacionComplementariaActEcon"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 174 and is_persistente = true) condicionDeclarante on  informacionComplementariaActEcon.id_condicion_declarante = condicionDeclarante.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 312 and is_persistente = true ) estadoLlenado on  informacionComplementariaActEcon.id_estado_llenado = estadoLlenado.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 330 and is_persistente = true ) mantenimiento on  informacionComplementariaActEcon.id_mantenimiento  = mantenimiento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 325 and is_persistente = true ) documentos on  informacionComplementariaActEcon.id_documento_presentado  = documentos.id"));
        sql.append(sharedUtil.append("where informacionComplementariaActEcon.is_persistente = true"));
        sql.append(sharedUtil.append("and informacionComplementariaActEcon.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoInformacionComplActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoInformacionComplActEco dto = new DtoInformacionComplActEco();
                        dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setCondicionDeclaranteOtros(rs.getString("condicionDeclaranteOtro"));
                        dto.setDocumentosPresentados(rs.getString("documentoPresentados"));
                        dto.setDocumentosPresentadosOtros(rs.getString("documentoPresentadosOtro"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
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
    public List<DtoObservaciones> dsObservacionesActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select informacionComplementariaActEcon.observacion observaciones,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.numero_documento_decla dniDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.nombre_declarante nombresDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.apellido_declarante apellidosDeclarante, "));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.fecha_declarante fecha"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_informacion_compl_act_econ informacionComplementariaActEcon"));
        sql.append(sharedUtil.append("where informacionComplementariaActEcon.is_persistente = true"));
        sql.append(sharedUtil.append("and informacionComplementariaActEcon.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoObservaciones> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoObservaciones dto = new DtoObservaciones();
                        dto.setObservaciones(rs.getString("observaciones"));
                        dto.setDniDeclarante(rs.getString("dniDeclarante"));
                        dto.setNombresDeclarante(rs.getString("nombresDeclarante"));
                        dto.setApellidosDeclarante(rs.getString("apellidosDeclarante"));
                        dto.setFecha(rs.getDate("fecha"));
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
