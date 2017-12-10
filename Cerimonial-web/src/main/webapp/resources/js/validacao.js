

//---------------EMPRESA----------------------

function alteraEtapaEmpresa(etapa) {
    $('#tabEtapas a[href="#etapa' + etapa + '"]').tab('show');
}

function exibePopoverEmpresa(dica) {
    $('#' + dica).popover('show');
}
function escondePopoverEmpresa(dica) {
    $('#' + dica).popover('hide');
}
function validaFormularioEmpresa() {
    var inptTipo = $('#inptTipo').val();
    var inptNome = $('#inptNome').val();
    var inptCpf = $('#inptCpf').val();
    var inptCnpj = $('#inptCnpj').val();
    var inptEmail = $('#inptEmail').val();
    var inptTelefone1 = $('#inptTelefone1').val();

    if (inptTipo === '') {
        alteraEtapaEmpresa(1);
        $('#inptTipo').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCpf === '') {
        alteraEtapaEmpresa(1);
        $('#inptCpf').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCnpj === '') {
        alteraEtapaEmpresa(1);
        $('#inptCnpj').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptNome === '') {
        alteraEtapaEmpresa(1);
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEmail === '') {
        alteraEtapaEmpresa(1);
        $('#inptEmail').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptTelefone1 === '') {
        alteraEtapaEmpresa(1);
        $('#inptTelefone1').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    var inptCep = $('#inptCep').val();
    var inptBairro = $('#inptBairro').val();
    var inptLog = $('#inptLog').val();
    var inptEstado = $('#inptEstado').val();
    var inptCidade = $('#inptCidade').val();

    if (inptCep === '') {
        alteraEtapaEmpresa(2);
        $('#inptCep').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptBairro === '') {
        alteraEtapaEmpresa(2);
        $('#inptBairro').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCep === '') {
        alteraEtapaEmpresa(2);
        $('#inptCep').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptLog === '') {
        alteraEtapaEmpresa(2);
        $('#inptLog').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEstado === '') {
        alteraEtapaEmpresa(2);
        $('#inptEstado').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCidade === '') {
        alteraEtapaEmpresa(2);
        $('#inptCidade').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }


    console.log(inptTipo);
}


//------------------------USUARIO--------------------------------

function validaFormularioUsuario() {
    var inptNome = $('#inptNome').val();
    var inptLogin = $('#inptLogin').val();
    var inptEmail = $('#inptEmail').val();
    var inptSenha = $('#inptSenha').val();
    var inptConfirmSenha = $('#inptConfirmSenha').val();

    if (inptNome === '') {
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptLogin === '') {
        $('#inptLogin').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEmail === '') {
        $('#inptEmail').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptSenha === '') {
        $('#inptSenha').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptConfirmSenha === '') {
        $('#inptConfirmSenha').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
}




//---------------------CLIENTE -------------------------------------

function validaFormularioCliente() {
    var inptNome = $('#inptNome').val();
    var inptCpf = $('#inptCpf').val();
    var inptRg = $('#inptRg').val();
    var inptEmail = $('#inptEmail').val();
    var inptTelefone1 = $('#inptTelefone1').val();

    if (inptNome === '') {
        alteraEtapaCliente(1);
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCpf === '') {
        alteraEtapaCliente(1);
        $('#inptCpf').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptRg === '') {
        alteraEtapaCliente(1);
        $('#inptRg').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptEmail === '') {
        alteraEtapaCliente(1);
        $('#inptEmail').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptTelefone1 === '') {
        alteraEtapaCliente(1);
        $('#inptTelefone1').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    var inptCep = $('#inptCep').val();
    var inptBairro = $('#inptBairro').val();
    var inptLog = $('#inptLog').val();
    var inptEstado = $('#inptEstado').val();
    var inptCidade = $('#inptCidade').val();

    if (inptCep === '') {
        alteraEtapaCliente(2);
        $('#inptCep').focus();
        return;
    }
    if (inptBairro === '') {
        alteraEtapaCliente(2);
        $('#inptBairro').focus();
        return;
    }

    if (inptCep === '') {
        alteraEtapaCliente(2);
        $('#inptCep').focus();
        return;
    }

    if (inptLog === '') {
        alteraEtapaCliente(2);
        $('#inptLog').focus();
        return;
    }

    if (inptEstado === '') {
        alteraEtapaCliente(2);
        $('#inptEstado').focus();
        return;
    }

    if (inptCidade === '') {
        alteraEtapaCliente(2);
        $('#inptCidade').focus();
        return;
    }

}


function alteraEtapaCliente(etapa) {
    $('#tabEtapas a[href="#etapa' + etapa + '"]').tab('show');
}


//---------------------COLABORADOR------------------------------------

function validaFormularioColaborador() {
    var inptNome = $('#inptNome').val();
    var inptCpf = $('#inptCpf').val();
    var inptEmail = $('#inptEmail').val();
    var inptTelefone1 = $('#inptTelefone1').val();

    if (inptNome === '') {
        alteraEtapaCliente(1);
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCpf === '') {
        alteraEtapaCliente(1);
        $('#inptCpf').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEmail === '') {
        alteraEtapaCliente(1);
        $('#inptEmail').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptTelefone1 === '') {
        alteraEtapaCliente(1);
        $('#inptTelefone1').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    var inptCep = $('#inptCep').val();
    var inptBairro = $('#inptBairro').val();
    var inptLog = $('#inptLog').val();
    var inptEstado = $('#inptEstado').val();
    var inptCidade = $('#inptCidade').val();

    if (inptCep === '') {
        alteraEtapaCliente(2);
        $('#inptCep').focus();
        return;
    }
    if (inptBairro === '') {
        alteraEtapaCliente(2);
        $('#inptBairro').focus();
        return;
    }

    if (inptCep === '') {
        alteraEtapaCliente(2);
        $('#inptCep').focus();
        return;
    }

    if (inptLog === '') {
        alteraEtapaCliente(2);
        $('#inptLog').focus();
        return;
    }

    if (inptEstado === '') {
        alteraEtapaCliente(2);
        $('#inptEstado').focus();
        return;
    }

    if (inptCidade === '') {
        alteraEtapaCliente(2);
        $('#inptCidade').focus();
        return;
    }

}


function alteraEtapaColaborador(etapa) {
    $('#tabEtapas a[href="#etapa' + etapa + '"]').tab('show');
}


//---------------FORNECEDOR----------------------

function alteraEtapaFornecedor(etapa) {
    $('#tabEtapas a[href="#etapa' + etapa + '"]').tab('show');
}


function validaFormularioFornecedor() {
    var inptTipo = $('#inptTipo').val();
    var inptNome = $('#inptNome').val();
    var inptCpf = $('#inptCpf').val();
    var inptCnpj = $('#inptCnpj').val();
    var inptEmail = $('#inptEmail').val();
    var inptTelefone1 = $('#inptTelefone1').val();

    if (inptTipo === '') {
        alteraEtapaFornecedor(1);
        $('#inptTipo').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCpf === '') {
        alteraEtapaFornecedor(1);
        $('#inptCpf').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCnpj === '') {
        alteraEtapaFornecedor(1);
        $('#inptCnpj').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptNome === '') {
        alteraEtapaFornecedor(1);
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEmail === '') {
        alteraEtapaFornecedor(1);
        $('#inptEmail').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptTelefone1 === '') {
        alteraEtapaFornecedor(1);
        $('#inptTelefone1').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    var inptCep = $('#inptCep').val();
    var inptBairro = $('#inptBairro').val();
    var inptLog = $('#inptLog').val();
    var inptEstado = $('#inptEstado').val();
    var inptCidade = $('#inptCidade').val();

    if (inptCep === '') {
        alteraEtapaFornecedor(2);
        $('#inptCep').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }
    if (inptBairro === '') {
        alteraEtapaFornecedor(2);
        $('#inptBairro').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCep === '') {
        alteraEtapaFornecedor(2);
        $('#inptCep').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptLog === '') {
        alteraEtapaFornecedor(2);
        $('#inptLog').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptEstado === '') {
        alteraEtapaFornecedor(2);
        $('#inptEstado').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }

    if (inptCidade === '') {
        alteraEtapaFornecedor(2);
        $('#inptCidade').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
        return;
    }


    console.log(inptTipo);
}



//-----------------Contato Inicial-------------------

function alteraEtapaContato(etapa) {
    $('#tabEtapas a[href="#etapa' + etapa + '"]').tab('show');
}



//---------------------CONTRATANTE -------------------------------------

function validaFormularioContratante() {
    var inptNome = $('#inptNome').val();
    var inptCpf = $('#inptCpf').val();
    var inptRg = $('#inptRg').val();
    var inptEmail = $('#inptEmail').val();
    var inptTelefone1 = $('#inptTelefone1').val();
    var inptTelefone2 = $('#inptTelefone2').val();


    if (inptNome === '') {
        alteraEtapaContratante(1);
        $('#inptNome').focus();
        $('#divInptNome').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }

    if (inptCpf === '') {
        alteraEtapaContratante(1);
        $('#inptCpf').focus();
        $('#div-inptCpf').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    if (inptRg === '') {
        alteraEtapaContratante(1);
        $('#inptRg').focus();
        $('#div-inptRg').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }

    if (inptEmail === '') {
        alteraEtapaContratante(1);
        $('#inptEmail').focus();
        $('#div-inptEmail').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }

    if (inptTelefone1 === '' && inptTelefone2 === '') {
        alteraEtapaContratante(1);
        $('#inptTelefone1').focus();
        $('#div-inptTelefone1').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }


    var inptCep = $('#inptCep').val();
    var inptBairro = $('#inptBairro').val();
    var inptLog = $('#inptLog').val();
    var inptEstado = $('#inptEstado').val();
    var inptCidade = $('#inptCidade').val();

    if (inptCep === '') {
        $('#div-inptCep').addClass('has-error');
        $('#inptCep').focus();

    }
    if (inptBairro === '') {
        $('#div-inptBairro').addClass('has-error');
        $('#inptBairro').focus();

    }

    if (inptLog === '') {
        $('#div-inptLog').addClass('has-error');
        $('#inptLog').focus();

    }

    if (inptEstado === '') {
        $('#div-inptEstado').addClass('has-error');
        $('#inptEstado').focus();

    }

    if (inptCidade === '') {
        $('#div-inptCidade').addClass('has-error');
        $('#inptCidade').focus();

    }

}

//---------------------FICHA CADASTRAL EVENTO -------------------------------------

function validaFormularioEvento() {
    var inptNome = $('#inptNomeEvento').val();
    var inptDataEvento = $('#inptDataEvento_input').val();
    var inptHoraInicialEvento = $('#inptHoraInicialEvento').val();
    var inptDataTerminoEvento = $('#inptDataEventoTermino_input').val();
    var inptHoraTerminoEvento = $('#inptHoraTerminoEvento').val();
    var inptQtdConvidados = $('#inptQtdConvidados').val();

    if (inptNome === '') {
        $('#inptNomeEvento').focus();
        $('#divInptNome').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    
    if (inptDataEvento === '' || inptDataEvento.length < 3) {
        $('#inptDataEvento_input').focus();
        $('#div-inptDataEvento').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    
    if (inptHoraInicialEvento === '') {
        $('#inptHoraInicialEvento').focus();
        $('#div-inptDataEvento').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    
    if (inptDataTerminoEvento === '' || inptDataTerminoEvento.length < 3) {
        $('#inptDataEventoTermino_input').focus();
        $('#div-inptDataEventoTermino').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    
    if (inptHoraTerminoEvento === '') {
        $('#inptHoraTerminoEvento').focus();
        $('#div-inptDataEventoTermino').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }
    if (inptQtdConvidados === '') {
        $('#inptQtdConvidados').focus();
        $('#div-inptQtdConvidados').addClass('has-error');
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);

    }

    if ($('#inptLocalCerimonia').val() === '') {
        $('#div-inptLocalCerimonia').addClass('has-error');
        $('#inptLocalCerimonia').focus();

    }

    if ($('#inptCep').val() === '') {
        $('#div-inptCep').addClass('has-error');
        $('#inptCep').focus();

    }
    if ($('#inptBairro').val() === '') {
        $('#div-inptBairro').addClass('has-error');
        $('#inptBairro').focus();

    }

    if ($('#inptLog').val() === '') {
        $('#div-inptLog').addClass('has-error');
        $('#inptLog').focus();

    }

    if ($('#inptEstado').val() === '') {
        $('#div-inptEstado').addClass('has-error');
        $('#inptEstado').focus();

    }

    if ($('#inptCidade').val() === '') {
        $('#div-inptCidade').addClass('has-error');
        $('#inptCidade').focus();

    }
    
    
    if ($('#inptLocalFesta').val() === '') {
        $('#div-inptLocalFesta').addClass('has-error');
        $('#inptLocalFesta').focus();

    }

    if ($('#inptCepFesta').val() === '') {
        $('#div-inptCepFesta').addClass('has-error');
        $('#inptCepFesta').focus();

    }
    if ($('#inptBairroFesta').val() === '') {
        $('#div-inptBairroFesta').addClass('has-error');
        $('#inptBairroFesta').focus();

    }

    if ($('#inptLogFesta').val() === '') {
        $('#div-inptLogFesta').addClass('has-error');
        $('#inptLogFesta').focus();

    }

    if ($('#inptEstadoFesta').val() === '') {
        $('#div-inptEstadoFesta').addClass('has-error');
        $('#inptEstadoFesta').focus();

    }

    if ($('#inptCidadeFesta').val() === '') {
        $('#div-inptCidadeFesta').addClass('has-error');
        $('#inptCidadeFesta').focus();

    }


}


//---------------------FICHA CADASTRAL EVENTO -------------------------------------

function validaFormularioCasamento() {
    
    if ($('#inptNomeNoiva').val() === '') {
        $('#div-inptNomeNoiva').addClass('has-error');
        $('#inptNomeNoiva').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#inptCpfNoiva').val() === '') {
        $('#div-inptCpfNoiva').addClass('has-error');
        $('#inptCpfNoiva').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#inptRgNoiva').val() === '') {
        $('#div-inptRgNoiva').addClass('has-error');
        $('#inptRgNoiva').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#dataNascimentoNoiva_input').val() === '') {
        $('#div-dataNascimentoNoiva').addClass('has-error');
        $('#dataNascimentoNoiva_input').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#inptEmailNoiva').val() === '') {
        $('#div-inptEmailNoiva').addClass('has-error');
        $('#inptEmailNoiva').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#inptCepNoiva').val() === '') {
        $('#div-inptCepNoiva').addClass('has-error');
        $('#inptCepNoiva').focus();
    }
    
    if ($('#inptBairroNoiva').val() === '') {
        $('#div-inptBairroNoiva').addClass('has-error');
        $('#inptBairroNoiva').focus();
    }
    
    if ($('#inptLogNoiva').val() === '') {
        $('#div-inptLogNoiva').addClass('has-error');
        $('#inptLogNoiva').focus();
    }
    
    if ($('#inptEstadoNoiva').val() === '') {
        $('#div-inptEstadoNoiva').addClass('has-error');
        $('#inptEstadoNoiva').focus();
    }
    
    if ($('#inptCidadeNoiva').val() === '') {
        $('#div-inptCidadeNoiva').addClass('has-error');
        $('#inptCidadeNoiva').focus();
    }
    
}

function validarFormularioContatoCasamento(){
    
    var valor = $('#inptNomeContato').val();
    
    if (valor === '' || valor.length < 3) {
        $('#div-inptNomeContato').addClass('has-error');
        $('#inptNomeContato').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
}



//--------------------ANIVERSARIO--------------------------

function validaFormularioAniversario() {
    
    if ($('#inptNome').val() === '') {
        $('#div-inptNome').addClass('has-error');
        $('#inptNome').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
   
    if ($('#dataNascimento_input').val() === '') {
        $('#div-dataNascimento').addClass('has-error');
        $('#dataNascimento_input').focus();
        $('html, body').animate({scrollTop: $('#form').offset().top}, 800);
    }
    
    if ($('#inptCep').val() === '') {
        $('#div-inptCep').addClass('has-error');
        $('#inptCep').focus();
    }
    
    if ($('#inptBairro').val() === '') {
        $('#div-inptBairro').addClass('has-error');
        $('#inptBairro').focus();
    }
    
    if ($('#inptLog').val() === '') {
        $('#div-inptLog').addClass('has-error');
        $('#inptLog').focus();
    }
    
    if ($('#inptEstado').val() === '') {
        $('#div-inptEstado').addClass('has-error');
        $('#inptEstado').focus();
    }
    
    if ($('#inptCidade').val() === '') {
        $('#div-inptCidade').addClass('has-error');
        $('#inptCidade').focus();
    }
    
}