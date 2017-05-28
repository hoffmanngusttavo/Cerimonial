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
import br.com.cerimonial.utils.Criptografia;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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

    @EJB
    private UsuarioService usuarioService;
    @EJB
    private LoginService loginService;

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

            Usuario usuario = (Usuario) SecurityUtils.getSubject().getPrincipal();

            String senha = "";
            for (char c : (char[]) at.getCredentials()) {
                senha += c;
            }

            usuario = usuarioService.getUsuarioByLoginSenha(at.getPrincipal().toString(), senha);
            if (usuario != null) {
                try {
                    // Criar Login
                    Login login = new Login();
                    login.setData(new Date());
                    login.setHora(new Date());

                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

                    login.setIp(request.getRemoteAddr());
                    login.setUsuario(usuario);

                    String headers = "";
                    Enumeration<String> cabecalho = request.getHeaderNames();
                    for (String k : requestMap.keySet()) {
                        headers += k + "=" + requestMap.get(k) + "\n";
                    }

                    login.setCabecalho(headers);

                    loginService.create(login);

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

        @Override
        public boolean isEmpty() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
