/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.security;

import br.com.cerimonial.entity.Login;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.LoginService;
import br.com.cerimonial.service.UsuarioService;
import br.com.cerimonial.utils.ConstantsProject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 *
 * @author Gustavo Hoffmann
 */
public class Realm extends AuthorizingRealm {
    
    private final UsuarioService usuarioService = lookupUsuarioServiceBean();
    private final LoginService loginService = lookupLoginServiceBean();
    

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {

        Usuario usuario = (Usuario) SecurityUtils.getSubject().getPrincipal();

        if (usuario != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            SimpleAuthorizationInfo infoGrupo = new SimpleAuthorizationInfo();

            if (!usuario.isMaster()) {
                //Nome do grupo
                infoGrupo.addRole("CLIENTE");

                //lista de permissoes
                List<String> listaPermissoes = new ArrayList<String>();
                listaPermissoes.add("TESTE");
                
                info.addStringPermissions(new ArrayList<String>(new HashSet<String>(listaPermissoes)));
            }

            return info;
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {

        try {
            String senha = "";
            for (char c : (char[]) at.getCredentials()) {
                senha += c;
            }

            Usuario usuario = usuarioService.getUsuarioByLoginSenha(at.getPrincipal().toString(), senha);
            if (usuario != null) {
                try {
                    // Criar Login
                    Login login = new Login();
                    login.setDataLogin(new Date());
                    login.setHora(new Date());

                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

                    login.setIp(request.getRemoteAddr());
                    login.setUsuario(usuario);

                    String headers = "";
                    for (String k : requestMap.keySet()) {
                        headers += k + "=" + requestMap.get(k) + "\n";
                    }

                    login.setCabecalho(headers);

                    loginService.save(login);

                } catch (Exception ex) {
                    Logger.getLogger(Realm.class.getSimpleName()).log(Level.WARNING, null, ex);
                }

                Md5Hash hashedPassword = new Md5Hash(senha, usuario.getSalt());
                return new SimpleAuthenticationInfo(usuario, hashedPassword, new Salt(usuario.getSalt()), usuario.getLogin());

            } else {
                Logger.getLogger(Realm.class.getSimpleName()).log(Level.WARNING, null, "Falha na autenticação, não carregou o salt ...");
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(Realm.class.getSimpleName()).log(Level.WARNING, null, ex);
            return null;
        }
    }

    private class Salt implements ByteSource {

        private final String salt;

        private Salt(String salt) {
            this.salt = salt;
        }

        @Override
        public byte[] getBytes() {
            return salt.getBytes();
        }

        @Override
        public String toHex() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String toBase64() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private UsuarioService lookupUsuarioServiceBean() {
        try {
            Context c = new InitialContext();
            return (UsuarioService) c.lookup("java:global/"+ConstantsProject.appName+"/"+ConstantsProject.moduleEjbName+"/UsuarioService!br.com.cerimonial.service.UsuarioService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private LoginService lookupLoginServiceBean() {
        try {
            Context c = new InitialContext();
            return (LoginService) c.lookup("java:global/"+ConstantsProject.appName+"/"+ConstantsProject.moduleEjbName+"/LoginService!br.com.cerimonial.service.LoginService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    

    

    
}
