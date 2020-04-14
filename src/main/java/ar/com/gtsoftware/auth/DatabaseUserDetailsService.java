package ar.com.gtsoftware.auth;

import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.dto.domain.UsuariosGruposDto;
import ar.com.gtsoftware.search.UsuariosSearchFilter;
import ar.com.gtsoftware.service.UsuariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UsuariosService usuariosService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsuariosSearchFilter sf = UsuariosSearchFilter.builder().login(username).build();
        UsuariosDto usuario = usuariosService.findFirstBySearchFilter(sf);
        if (usuario == null) {
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
        }
        List<UsuariosGruposDto> usuariosGrupos = usuariosService.obtenerRolesUsuario(usuario.getId());
        List<String> roles = usuariosGrupos.stream().map(x -> x.getNombreGrupo()).collect(Collectors.toList());

        return JwtUserDetails.builder()
                .id(usuario.getId())
                .loginName(usuario.getLogin())
                .password(usuario.getPassword())
                .userRoles(roles)
                .completeUserName(usuario.getNombreUsuario())
                .sucursalId(usuario.getIdSucursal().getId())
                .sucursalName(usuario.getIdSucursal().getNombreSucursal())
                .build();

    }

}


