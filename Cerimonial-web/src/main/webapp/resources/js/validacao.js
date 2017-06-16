

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
    return;
  }
  
  if(inptCpf === ''){
    alteraEtapaEmpresa(1);
    $('#inptCpf').focus();
    return;
  }
  
  if(inptCnpj === ''){
    alteraEtapaEmpresa(1);
    $('#inptCnpj').focus();
    return;
  }
  
  if(inptNome === ''){
    alteraEtapaEmpresa(1);
    $('#inptNome').focus();
    return;
  }
  
  if(inptEmail === ''){
    alteraEtapaEmpresa(1);
    $('#inptEmail').focus();
    return;
  }
  if(inptTelefone1 === ''){
    alteraEtapaEmpresa(1);
    $('#inptTelefone1').focus();
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
    return;
  }  
  if(inptBairro === ''){
    alteraEtapaEmpresa(2);
    $('#inptBairro').focus();
    return;
  } 
  
  if(inptCep === ''){
    alteraEtapaEmpresa(2);
    $('#inptCep').focus();
    return;
  } 
  
  if(inptLog === ''){
    alteraEtapaEmpresa(2);
    $('#inptLog').focus();
    return;
  }  
  
  if(inptEstado === ''){
    alteraEtapaEmpresa(2);
    $('#inptEstado').focus();
    return;
  } 
  
  if(inptCidade === ''){
    alteraEtapaEmpresa(2);
    $('#inptCidade').focus();
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
    return;
  }
  
  if(inptLogin === ''){
    $('#inptLogin').focus();
    return;
  }
  
  if(inptEmail === ''){
    $('#inptEmail').focus();
    return;
  }
  if(inptSenha === ''){
    $('#inptSenha').focus();
    return;
  }
  if(inptConfirmSenha === ''){
    $('#inptConfirmSenha').focus();
    return;
  }
}
