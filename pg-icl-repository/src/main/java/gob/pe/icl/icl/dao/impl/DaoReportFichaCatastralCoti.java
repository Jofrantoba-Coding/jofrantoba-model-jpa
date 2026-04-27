/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoReportFichaCatastralCoti;
import gob.pe.icl.icl.dto.reports.DtoCotitularCatastral;
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
public class DaoReportFichaCatastralCoti extends AbstractJpaDao<UnidadCatastral> implements InterDaoReportFichaCatastralCoti {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralCoti() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoCotitularCatastral> dsDatosCotitular(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("row_number() over( order by titular.id  ) nroCotitular,"));
        sql.append(sharedUtil.append("totalCotitularidad.totalCotitulares totalCotitulares,"));
        sql.append(sharedUtil.append("tipoTitular.descripcion  tipoTitular,"));
        sql.append(sharedUtil.append("titular.id_tipo_titular  idTipoTitular,"));
        sql.append(sharedUtil.append("titular.porcentaje porcentajeCotitular,"));
        sql.append(sharedUtil.append("titular.codigo_contribuyente codigoContribuyente,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion  tipoDocIdentidad,"));
        sql.append(sharedUtil.append("titular.numero_documento nroDoc,"));
        sql.append(sharedUtil.append("titular.nombre_ruc nombres,"));
        sql.append(sharedUtil.append("titular.apellido_paterno apellidoPaterno,"));
        sql.append(sharedUtil.append("titular.apellido_materno apellidoMaterno,"));
        sql.append(sharedUtil.append("formaAdquisicion.descripcion formaAdquisicion,"));
        sql.append(sharedUtil.append("titularidad.fecha_adquisicion fechaAdquisicion,"));
        sql.append(sharedUtil.append("tipoPersonaJuridica.descripcion condicionEspecialTitular,"));
        sql.append(sharedUtil.append("departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("titular.telefono telefono,"));
        sql.append(sharedUtil.append("titular.anexo anexo,"));
        sql.append(sharedUtil.append("titular.correo_electronico correoElectronico,"));
        sql.append(sharedUtil.append("titular.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("titular.nombre_via nombreVia,"));
        sql.append(sharedUtil.append("titular.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("titular.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("titular.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("titular.nombre_hu nombreHabilitacionUrbana,"));
        sql.append(sharedUtil.append("titular.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("titular.manzana manzana,"));
        sql.append(sharedUtil.append("titular.lote lote,"));
        sql.append(sharedUtil.append("titular.sub_lote subLote"));
        sql.append(sharedUtil.append("from icl.catastro.tgv_titularidad titularidad"));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titular titular on  titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_departamento departamento  on  titular.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_provincia provincia on  titular.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left join icl.catastro.tm_distrito distrito on titular.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 76 and is_persistente = true ) tipoTitular on titular.id_tipo_titular = tipoTitular.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on titular.id_tipo_documento  = tipoDocumento.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 94 and is_persistente = true ) tipoPersonaJuridica on titular.id_tipo_persona_juridica = tipoPersonaJuridica.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 111 and is_persistente = true ) formaAdquisicion on titularidad.id_forma_adquisicion =  formaAdquisicion.id"));
        sql.append(sharedUtil.append("left join (select count(titular.id) totalcotitulares, id_unidad_catastral from icl.catastro.tgv_titularidad titularidad "));
        sql.append(sharedUtil.append("inner join icl.catastro.tgv_titular titular on titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("group by  id_unidad_catastral) totalCotitularidad on totalCotitularidad.id_unidad_catastral =  titularidad.id_unidad_catastral"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 95 and is_persistente = true ) tipoUbicacion on  titular.id_tipo_ubicacion = tipoUbicacion.id "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from icl.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on titular.id_tipo_via = tipoVia.id "));
        sql.append(sharedUtil.append("where  titularidad.id_unidad_catastral = ?"));
        System.out.println(sql.toString());
        List<DtoCotitularCatastral> datasource = new ArrayList();
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
                        DtoCotitularCatastral dto = new DtoCotitularCatastral();
                        dto.setNroCotitular(rs.getLong("nroCotitular"));
                        dto.setTotalCotitulares(rs.getInt("totalCotitulares"));
                        dto.setTipoTitular(rs.getString("tipoTitular"));
                        dto.setPorcentajeCotitular(rs.getDouble("porcentajeCotitular"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setFormaAdquisicion(rs.getString("formaAdquisicion"));
                        dto.setFechaAdquisicion(rs.getDate("fechaAdquisicion"));
                        dto.setCondicionEspecialTitular(rs.getString("condicionEspecialTitular"));
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setCorreoElectronico(rs.getString("correoElectronico"));
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoVia(rs.getString("tipoVia"));
                        dto.setNombreVia(rs.getString("nombreVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabilitacionUrbana(rs.getString("nombreHabilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        Long idTipoTitular = rs.getLong("idTipoTitular");
                        if (idTipoTitular == 80l) {
                            dto.setNroRuc(rs.getString("nroDoc"));
                            dto.setRazonSocial(rs.getString("nombres"));
                        } else {
                            dto.setTipoDocIdentidad(rs.getString("tipoDocIdentidad"));
                            dto.setNombres(rs.getString("nombres"));
                            dto.setNroDoc(rs.getString("nroDoc"));
                            dto.setApellidoPaterno(rs.getString("apellidoPaterno"));
                            dto.setApellidoMaterno(rs.getString("apellidoMaterno"));                           
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

}
