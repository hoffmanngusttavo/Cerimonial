/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CerimoniaEvento;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.EvolucaoPreenchimento;
import br.com.cerimonial.entity.FestaCerimonia;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EvolucaoPreenchimentoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EvolucaoPreenchimentoService extends BasicService<EvolucaoPreenchimento> {

    private EvolucaoPreenchimentoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EvolucaoPreenchimentoRepository(em);

    }

    @Override
    public EvolucaoPreenchimento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public EvolucaoPreenchimento save(EvolucaoPreenchimento entity) throws Exception {

        isValid(entity);

        return repository.create(entity);
    }

    public List<EvolucaoPreenchimento> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(EvolucaoPreenchimento entity) {
        if (entity == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    public void validarPorcentagemPreenchimento(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }

        if (eventoPessoa.isContratante()) {

            validarPorcentagemPreenchimentoContratante(eventoPessoa);

        } else {

            //TODO: VALIDAR DE ACORDO COM O TIPO DO ENVOLVIDO
        }

    }

    /**
     * Vai contabilizar a porcentagem de preenchimento do contratante.
     *
     * @param eventoPessoa
     */
    public void validarPorcentagemPreenchimentoContratante(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }

        double totalCamposValidar = 11;
        double totalCamposValidos = 0;
        StringBuilder sb = new StringBuilder();
        Pessoa pessoa = eventoPessoa.getPessoa();

        if (StringUtils.isNotBlank(pessoa.getNome())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Nome, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getCpf())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Cpf, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getRg())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Rg, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getEmail())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Email, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getTelefoneCelular()) || StringUtils.isNotBlank(pessoa.getTelefoneResidencial())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Telefone, \n");
        }

        if (pessoa.getDataNascimento() != null) {
            totalCamposValidos += 1;
        } else {
            sb.append("Data de nascimento, \n");
        }

        if (pessoa.getEndereco() == null) {
            sb.append("Endereço, \n");
        } else {

            Endereco endereco = pessoa.getEndereco();

            if (endereco != null) {

                if (StringUtils.isNotBlank(endereco.getCep())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Cep, \n");
                }

                if (StringUtils.isNotBlank(endereco.getBairro())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Bairro, \n");
                }

                if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Logradouro, \n");
                }

                if (endereco.getCidade() != null) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Cidade, \n");
                }

                if (endereco.getEstado() != null) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Estado, \n");
                }
            } else {

                sb.append("Endereço, \n");
            }

        }

        double porcentagem = (totalCamposValidos / totalCamposValidar) * 100;

        evolucaoPreenchimento.setPorcentagemConcluida((int) porcentagem);

        evolucaoPreenchimento.setMensagem(sb.toString());

        eventoPessoa.setEvolucaoPreenchimento(evolucaoPreenchimento);

    }

    /**
     * Vai contabilizar a porcentagem de preenchimento do contratante.
     *
     * @param eventoPessoa
     */
    public void validarPorcentagemPreenchimentoNoivo(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }

        double totalCamposValidar = 12;
        double totalCamposValidos = 0;
        StringBuilder sb = new StringBuilder();
        Pessoa pessoa = eventoPessoa.getPessoa();

        if (StringUtils.isNotBlank(pessoa.getNome())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Nome, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getCpf())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Cpf, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getRg())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Rg, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getEmail())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Email, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getTelefoneCelular()) || StringUtils.isNotBlank(pessoa.getTelefoneResidencial())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Telefone, \n");
        }

        if (pessoa.getDataNascimento() != null) {
            totalCamposValidos += 1;
        } else {
            sb.append("Data de nascimento, \n");
        }

        if (pessoa.getEndereco() == null) {
            sb.append("Endereço, \n");
        } else {

            Endereco endereco = pessoa.getEndereco();

            if (endereco != null) {

                if (StringUtils.isNotBlank(endereco.getCep())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Cep, \n");
                }

                if (StringUtils.isNotBlank(endereco.getBairro())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Bairro, \n");
                }

                if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Logradouro, \n");
                }

                if (endereco.getCidade() != null) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Cidade, \n");
                }

                if (endereco.getEstado() != null) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Estado, \n");
                }
            } else {

                sb.append("Endereço, \n");
            }

        }

        if (CollectionUtils.isNotBlank(pessoa.getContatosFamiliares())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Contato familiar \n");
        }

        double porcentagem = (totalCamposValidos / totalCamposValidar) * 100;

        evolucaoPreenchimento.setPorcentagemConcluida((int) porcentagem);

        evolucaoPreenchimento.setMensagem(sb.toString());

        eventoPessoa.setEvolucaoPreenchimento(evolucaoPreenchimento);

    }

    public void validarPorcentagemPreenchimentoEvento(Evento evento) {

        if (evento == null) {
            throw new GenericException("Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = evento.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(evento);
        }

        if (evento.isEventoCasamento() || evento.isEventoBodas()) {

            evolucaoPreenchimento = validarPorcentagemPreenchimentoCasamento(evolucaoPreenchimento, evento);

        } else if (evento.isEventoAniversario15Anos()) {

            evolucaoPreenchimento = validarPorcentagemPreenchimentoCasamento(evolucaoPreenchimento, evento);

        } else if (evento.isEventoAniversario()) {

            evolucaoPreenchimento = validarPorcentagemPreenchimentoCasamento(evolucaoPreenchimento, evento);

        } else if (evento.isEventoAniversarioInfantil()) {

            evolucaoPreenchimento = validarPorcentagemPreenchimentoCasamento(evolucaoPreenchimento, evento);
        }

        evento.setEvolucaoPreenchimento(evolucaoPreenchimento);

    }

    private EvolucaoPreenchimento validarPorcentagemPreenchimentoCasamento(EvolucaoPreenchimento evolucaoPreenchimento, Evento evento) {

        if (evolucaoPreenchimento != null && evento != null) {

            double totalCamposValidar = 15;
            double totalCamposValidos = 0;
            StringBuilder sb = new StringBuilder();

            if (evento.getQuantidadeConvidados() != null && evento.getQuantidadeConvidados() > 0) {
                totalCamposValidos += 1;
            } else {
                sb.append("Quantidade de convidados, \n");
            }

            if (evento.getDataInicio() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Data de início, \n");
            }

            if (evento.getHoraInicio() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Hora de início, \n");
            }

            CerimoniaEvento cerimonia = evento.getCerimoniaEvento();

            if (cerimonia != null) {

                if (StringUtils.isNotBlank(cerimonia.getNomeLocalEvento())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Nome do local da cerimônia, \n");
                }

                Endereco endereco = cerimonia.getEndereco();

                if (endereco != null) {

                    if (StringUtils.isNotBlank(endereco.getCep())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Cep, \n");
                    }

                    if (StringUtils.isNotBlank(endereco.getBairro())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Bairro, \n");
                    }

                    if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Logradouro, \n");
                    }

                    if (endereco.getCidade() != null) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Cidade, \n");
                    }

                    if (endereco.getEstado() != null) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Estado, \n");
                    }
                } else {

                    sb.append("Endereço da cerimônia, \n");
                }

            } else {
                sb.append("Dados da cerimônia, \n");
            }
            
            FestaCerimonia festaCerimonia = evento.getFestaCerimonia();
            
            if (festaCerimonia != null) {

                if (StringUtils.isNotBlank(festaCerimonia.getNomeLocalFesta())) {
                    totalCamposValidos += 1;
                } else {
                    sb.append("Nome do local da festa, \n");
                }

                Endereco endereco = festaCerimonia.getEndereco();

                if (endereco != null) {

                    if (StringUtils.isNotBlank(endereco.getCep())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Cep, \n");
                    }

                    if (StringUtils.isNotBlank(endereco.getBairro())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Bairro, \n");
                    }

                    if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Logradouro, \n");
                    }

                    if (endereco.getCidade() != null) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Cidade, \n");
                    }

                    if (endereco.getEstado() != null) {
                        totalCamposValidos += 1;
                    } else {
                        sb.append("Estado, \n");
                    }
                } else {

                    sb.append("Endereço da festa, \n");
                }

            } else {
                sb.append("Dados da festa, \n");
            }

            double porcentagem = (totalCamposValidos / totalCamposValidar) * 100;

            evolucaoPreenchimento.setPorcentagemConcluida((int) porcentagem);

            evolucaoPreenchimento.setMensagem(sb.toString());

        }

        return evolucaoPreenchimento;
    }

    /**
     * Método vai retornar o nome do css que deve atribuir ao panel de acordo
     * com a porcentagem do preenchimento
     *
     * @param evolucaoPreenchimento
     * @return
     */
    public String getNomePanelEvolucaoPreenchimento(EvolucaoPreenchimento evolucaoPreenchimento) {

        if (evolucaoPreenchimento != null) {

            if (evolucaoPreenchimento.getPorcentagemConcluida() == 100) {
                return "success";
            }

            if (evolucaoPreenchimento.getPorcentagemConcluida() > 0 && evolucaoPreenchimento.getPorcentagemConcluida() < 100) {
                return "warning";
            }
        }

        return "primary";
    }

    public EvolucaoPreenchimento getEvolucaoDadosContratante(Evento evento) {

        if (evento == null) {
            return null;
        }

        if (!evento.isEventoProprioContratante()) {

            EventoPessoa contratante = evento.getContratante();

            if (contratante != null && contratante.getEvolucaoPreenchimento() != null) {
                return contratante.getEvolucaoPreenchimento();
            }
        }

        return null;
    }

    public EvolucaoPreenchimento getEvolucaoDadosNoivo(Evento evento) {

        if (evento == null) {
            return null;
        }

        if (evento.isEventoCasamento() || evento.isEventoBodas()) {

            EventoPessoa noivo = evento.getNoivo();

            if (noivo != null && noivo.getEvolucaoPreenchimento() != null) {
                return noivo.getEvolucaoPreenchimento();
            }
        }

        return null;

    }

    public EvolucaoPreenchimento getEvolucaoDadosNoiva(Evento evento) {
        if (evento == null) {
            return null;
        }

        if (evento.isEventoCasamento() || evento.isEventoBodas()) {

            EventoPessoa noiva = evento.getNoiva();

            if (noiva != null && noiva.getEvolucaoPreenchimento() != null) {
                return noiva.getEvolucaoPreenchimento();
            }
        }

        return null;
    }

    public EvolucaoPreenchimento getEvolucaoDadosEvento(Evento evento) {
        if (evento == null) {
            return null;
        }

        if (evento.getEvolucaoPreenchimento() != null) {
            return evento.getEvolucaoPreenchimento();
        }

        return null;
    }

}
