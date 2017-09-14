/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.service.ModeloPropostaService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ModeloPropostaCrudMB")
@ViewScoped
public class ModeloPropostaCrudMB extends BasicControl {

    private LazyDataModel<ModeloProposta> lazyLista;
    private Long id;
    private ModeloProposta entity;
    private UploadedFile file;

    @EJB
    private ModeloPropostaService service;

    private AbstractFilter filtros;
    private SelectItemUtils selectItemUtils;

    /**
     * Evento invocado ao abrir o xhtml na edição de um cliente objetivo de
     * carregar os dados do cliente
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getEntity(id);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new ModeloProposta();
        }

        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        try {
            if (entity != null && entity.getId() != null) {
                service.delete(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * fornecedor
     *
     * @return
     */
    public synchronized String save() {
        try {
            if (entity != null) {

                if (file != null && file.getSize() > 0) {
                    entity.setArquivo(new Arquivo(file.getFileName(), file.getContentType(), file.getContents()));
                }

                service.save(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     */
    public void baixarArquivo() {
        try {
            ArquivoUtils.carregarArquivo(entity.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para remover download do arquivo
     * TODO[FIXME]
     */
    public void removerArquivo() {
        try {
            entity.setArquivo(null);
            createFacesInfoMessage("Anexo removido, clique em salvar para concluir a remoção");
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de index para listar os clientes
     *
     * @return
     */
    public LazyDataModel<ModeloProposta> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<ModeloProposta>() {

                @Override
                public ModeloProposta getRowData(String rowKey) {
                    List<ModeloProposta> list = (List<ModeloProposta>) getWrappedData();
                    for (ModeloProposta cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(ModeloProposta object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<ModeloProposta> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countAll();
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<ModeloProposta> clientes = service.findRangeListagem(max, offset, sortField, sortAscDesc);
                    return clientes;
                }
            };
            //
            int count = service.countAll();
            lazyLista.setRowCount(count);
            //
        }
        return lazyLista;
    }

    public List<SelectItem> getComboTipoEvento() {
        return selectItemUtils.getComboTipoEvento();
    }

    public LazyDataModel<ModeloProposta> getLazyLista() {
        return lazyLista;
    }

    public void setLazyLista(LazyDataModel<ModeloProposta> lazyLista) {
        this.lazyLista = lazyLista;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModeloProposta getEntity() {
        return entity;
    }

    public void setEntity(ModeloProposta entity) {
        this.entity = entity;
    }

    public AbstractFilter getFiltros() {
        return filtros;
    }

    public void setFiltros(AbstractFilter filtros) {
        this.filtros = filtros;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
