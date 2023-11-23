package com.acloudofgoods.security;

import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.dto.RegisterRequestDto;
import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.service.AuthenticationService;
import com.acloudofgoods.security.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Value("${initial.admin.email}")
     private String adminEmail;
    @Value("${initial.admin.first-name}")
    private String firstName;
    @Value("${initial.admin.last-name}")
    private String lastName;
    @Value("${initial.admin.password}")
    private String password;
    @Value("${initial.role.name.admin}")
    private String roleAdmin;
    @Value("${initial.role.name.user}")
    private String roleUser;
    @Value("${initial.role.permission.write}")
    private String write;
    @Value("${initial.role.permission.update}")
    private String update;
    @Value("${initial.role.permission.read}")
    private String read;
    @Value("${initial.role.permission.delete}")
    private String delete;

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service, RoleService roleService) {
        Set<Role> authorityList = new HashSet<>();
        Set<String> adminPermission = new HashSet<>();
        adminPermission.add(write);
        adminPermission.add(read);
        adminPermission.add(update);
        adminPermission.add(delete);
        Set<String> userPermission = new HashSet<>();
        userPermission.add(read);

        authorityList.add(new Role(1L, roleAdmin));
        return args -> {
            var adminRole = AuthorityDto.builder().roleName(roleAdmin).permission(adminPermission).build();
            var userRole = AuthorityDto.builder().roleName(roleUser).permission(userPermission).build();
            roleService.saveRole(adminRole);
            roleService.saveRole(userRole);
            var user = RegisterRequestDto.builder().firstname(firstName).lastname(lastName).email(adminEmail.trim())
                    .password(password).roles(authorityList).build();
            service.registerAdminWhenServerStart(user);

        };
    }
}
