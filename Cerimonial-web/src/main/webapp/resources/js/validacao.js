

//---------------EMPRESA----------------------

function alteraEtapaEmpresa(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}

function exibePopoverEmpresa(dica){
  $('#'+dica).popover('show');
}
function escondePopoverEmpresa(dica){
  $('#'+dica).popover('hide');
}
function validaFormularioEmpresa(){
  var inptTipo = $('#inptTipo').val();
  var inptNome = $('#inptNome').val();
  var inptCpf = $('#inptCpf').val();
  var inptCnpj = $('#inptCnpj').val();
  var inptEmail = $('#inptEmail').val();
  var inptTelefone1 = $('#inptTelefone1').val();
  
  if(inptTipo === ''){
    alteraEtapaEmpresa(1);
    $('#inptTipo').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptCpf === ''){
    alteraEtapaEmpresa(1);
    $('#inptCpf').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptCnpj === ''){
    alteraEtapaEmpresa(1);
    $('#inptCnpj').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptNome === ''){
    alteraEtapaEmpresa(1);
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptEmail === ''){
    alteraEtapaEmpresa(1);
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptTelefone1 === ''){
    alteraEtapaEmpresa(1);
    $('#inptTelefone1').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  var inptCep = $('#inptCep').val();
  var inptBairro = $('#inptBairro').val();
  var inptLog = $('#inptLog').val();
  var inptEstado = $('#inptEstado').val();
  var inptCidade = $('#inptCidade').val();

  if(inptCep === ''){
    alteraEtapaEmpresa(2);
    $('#inptCep').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaEmpresa(2);
    $('#inptBairro').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaEmpresa(2);
    $('#inptCep').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaEmpresa(2);
    $('#inptLog').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaEmpresa(2);
    $('#inptEstado').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaEmpresa(2);
    $('#inptCidade').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  


  console.log(inptTipo);
}


//------------------------USUARIO--------------------------------

function validaFormularioUsuario(){
  var inptNome = $('#inptNome').val();
  var inptLogin = $('#inptLogin').val();
  var inptEmail = $('#inptEmail').val();
  var inptSenha = $('#inptSenha').val();
  var inptConfirmSenha = $('#inptConfirmSenha').val();
  
  if(inptNome === ''){
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptLogin === ''){
    $('#inptLogin').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptEmail === ''){
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptSenha === ''){
    $('#inptSenha').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptConfirmSenha === ''){
    $('#inptConfirmSenha').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
}




//---------------------CLIENTE -------------------------------------

function validaFormularioCliente(){
  var inptNome = $('#inptNome').val();
  var inptCpf = $('#inptCpf').val();
  var inptRg = $('#inptRg').val();
  var inptEmail = $('#inptEmail').val();
  var inptTelefone1 = $('#inptTelefone1').val();
  
 if(inptNome === ''){
    alteraEtapaCliente(1);
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
 
  if(inptCpf === ''){
    alteraEtapaCliente(1);
    $('#inptCpf').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptRg === ''){
    alteraEtapaCliente(1);
    $('#inptRg').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptEmail === ''){
    alteraEtapaCliente(1);
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptTelefone1 === ''){
    alteraEtapaCliente(1);
    $('#inptTelefone1').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  var inptCep = $('#inptCep').val();
  var inptBairro = $('#inptBairro').val();
  var inptLog = $('#inptLog').val();
  var inptEstado = $('#inptEstado').val();
  var inptCidade = $('#inptCidade').val();

  if(inptCep === ''){
    alteraEtapaCliente(2);
    $('#inptCep').focus();
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaCliente(2);
    $('#inptBairro').focus();
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaCliente(2);
    $('#inptCep').focus();
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaCliente(2);
    $('#inptLog').focus();
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaCliente(2);
    $('#inptEstado').focus();
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaCliente(2);
    $('#inptCidade').focus();
    return;
  }  

}


function alteraEtapaCliente(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}


//---------------------COLABORADOR------------------------------------

function validaFormularioColaborador(){
  var inptNome = $('#inptNome').val();
  var inptCpf = $('#inptCpf').val();
  var inptEmail = $('#inptEmail').val();
  var inptTelefone1 = $('#inptTelefone1').val();
  
 if(inptNome === ''){
    alteraEtapaCliente(1);
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
 
  if(inptCpf === ''){
    alteraEtapaCliente(1);
    $('#inptCpf').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptEmail === ''){
    alteraEtapaCliente(1);
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptTelefone1 === ''){
    alteraEtapaCliente(1);
    $('#inptTelefone1').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  var inptCep = $('#inptCep').val();
  var inptBairro = $('#inptBairro').val();
  var inptLog = $('#inptLog').val();
  var inptEstado = $('#inptEstado').val();
  var inptCidade = $('#inptCidade').val();

  if(inptCep === ''){
    alteraEtapaCliente(2);
    $('#inptCep').focus();
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaCliente(2);
    $('#inptBairro').focus();
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaCliente(2);
    $('#inptCep').focus();
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaCliente(2);
    $('#inptLog').focus();
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaCliente(2);
    $('#inptEstado').focus();
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaCliente(2);
    $('#inptCidade').focus();
    return;
  }  

}


function alteraEtapaColaborador(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}


//---------------FORNECEDOR----------------------

function alteraEtapaFornecedor(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}


function validaFormularioFornecedor(){
  var inptTipo = $('#inptTipo').val();
  var inptNome = $('#inptNome').val();
  var inptCpf = $('#inptCpf').val();
  var inptCnpj = $('#inptCnpj').val();
  var inptEmail = $('#inptEmail').val();
  var inptTelefone1 = $('#inptTelefone1').val();
  
  if(inptTipo === ''){
    alteraEtapaFornecedor(1);
    $('#inptTipo').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptCpf === ''){
    alteraEtapaFornecedor(1);
    $('#inptCpf').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptCnpj === ''){
    alteraEtapaFornecedor(1);
    $('#inptCnpj').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptNome === ''){
    alteraEtapaFornecedor(1);
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  if(inptEmail === ''){
    alteraEtapaFornecedor(1);
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptTelefone1 === ''){
    alteraEtapaFornecedor(1);
    $('#inptTelefone1').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
  var inptCep = $('#inptCep').val();
  var inptBairro = $('#inptBairro').val();
  var inptLog = $('#inptLog').val();
  var inptEstado = $('#inptEstado').val();
  var inptCidade = $('#inptCidade').val();

  if(inptCep === ''){
    alteraEtapaFornecedor(2);
    $('#inptCep').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaFornecedor(2);
    $('#inptBairro').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaFornecedor(2);
    $('#inptCep').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaFornecedor(2);
    $('#inptLog').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaFornecedor(2);
    $('#inptEstado').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaFornecedor(2);
    $('#inptCidade').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }  


  console.log(inptTipo);
}



//-----------------Contato Inicial-------------------

function alteraEtapaContato(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}



//---------------------CONTRATANTE -------------------------------------

function validaFormularioContratante(){
  var inptNome = $('#inptNome').val();
  var inptCpf = $('#inptCpf').val();
  var inptRg = $('#inptRg').val();
  var inptEmail = $('#inptEmail').val();
 
  
 if(inptNome === ''){
    alteraEtapaContratante(1);
    $('#inptNome').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
 
  if(inptCpf === ''){
    alteraEtapaContratante(1);
    $('#inptCpf').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptRg === ''){
    alteraEtapaContratante(1);
    $('#inptRg').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  if(inptEmail === ''){
    alteraEtapaContratante(1);
    $('#inptEmail').focus();
    $('html, body').animate({ scrollTop: $('#form').offset().top }, 800);
    return;
  }
  
 
  var inptCep = $('#inptCep').val();
  var inptBairro = $('#inptBairro').val();
  var inptLog = $('#inptLog').val();
  var inptEstado = $('#inptEstado').val();
  var inptCidade = $('#inptCidade').val();

  if(inptCep === ''){
    alteraEtapaContratante(2);
    $('#inptCep').focus();
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaContratante(2);
    $('#inptBairro').focus();
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaContratante(2);
    $('#inptCep').focus();
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaContratante(2);
    $('#inptLog').focus();
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaContratante(2);
    $('#inptEstado').focus();
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaContratante(2);
    $('#inptCidade').focus();
    return;
  }  

}


function alteraEtapaContratante(etapa){
  $('#tabEtapas a[href="#etapa'+etapa+'"]').tab('show');
}