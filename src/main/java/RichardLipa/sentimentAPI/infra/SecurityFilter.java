package RichardLipa.sentimentAPI.infra;


import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import RichardLipa.sentimentAPI.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwtToken = recuperarToken(request);
        if(jwtToken != null){

            var subjet = tokenService.getSubject((String) jwtToken);
            var usuario = usuarioRepository.findByEmail(subjet);

            // le pasamos como parametros el usuario encontrado, credenciales en null si no hay,
            // y usuario.getAuthorities() de sprint securiti en entidad Usuario(lista de roles)
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // si no hay token pasa a control de la clase SecurityConfiguratios y metods de  de sprin security
        filterChain.doFilter(request, response);// paraque continue la candena de filtros

    }

    private Object recuperarToken(HttpServletRequest request) {
        var  authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return  authorizationHeader.replace("Bearer ", "");
        }
        return  null;
    }

}
