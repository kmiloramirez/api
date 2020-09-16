package com.finantory.api.seguridad;

import com.finantory.api.seguridad.filtro.FiltroAutenticacionWeb;
import com.finantory.api.seguridad.filtro.FiltroAutorizacion;
import com.finantory.api.seguridad.servicio.UsuariosDetalleServicio;
import com.finantory.api.seguridad.servicio.jwt.IJwtServicio;
import com.finantory.api.seguridad.servicio.seguridad.SeguridadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UsuariosDetalleServicio usuarioDetalleServicio;
    @Autowired
    private IJwtServicio jwtServicio;
    @Autowired
    private SeguridadServicio seguridadServicio;



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder buillder) throws Exception {

        buillder.userDetailsService(usuarioDetalleServicio).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/seguridad/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(obtenerFiltroAutenticacionWeb())
                .addFilter(obtenerFiltroAutorizacion())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().configurationSource(corsConfigurationSource());
    }

    private FiltroAutenticacionWeb obtenerFiltroAutenticacionWeb() throws Exception {
        return new FiltroAutenticacionWeb(authenticationManager(), jwtServicio);
    }


    private FiltroAutorizacion obtenerFiltroAutorizacion() throws Exception {
        return new FiltroAutorizacion(authenticationManager(), jwtServicio);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200","https://fuleofront.herokuapp.com"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterSeguridad(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
