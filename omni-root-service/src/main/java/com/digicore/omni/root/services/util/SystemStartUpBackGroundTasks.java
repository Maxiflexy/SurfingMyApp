package com.digicore.omni.root.services.util;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.omni.data.lib.modules.backoffice.model.BackOfficeProfile;
import com.digicore.omni.data.lib.modules.backoffice.model.PaymentProcessor;
import com.digicore.omni.data.lib.modules.backoffice.repository.BackOfficeProfileRepository;
import com.digicore.omni.data.lib.modules.backoffice.repository.PaymentProcessorRepository;

import com.digicore.omni.data.lib.modules.common.enums.PaymentLinkMode;
import com.digicore.omni.data.lib.modules.common.enums.PaymentStatus;
import com.digicore.omni.data.lib.modules.common.models.Setting;
import com.digicore.omni.data.lib.modules.common.models.UserAccount;
import com.digicore.omni.data.lib.modules.common.permission.model.Authority;
import com.digicore.omni.data.lib.modules.common.permission.model.Role;
import com.digicore.omni.data.lib.modules.common.permission.repository.AuthorityRepository;
import com.digicore.omni.data.lib.modules.common.permission.repository.RoleRepository;
import com.digicore.omni.data.lib.modules.common.repository.UserRepository;
import com.digicore.omni.data.lib.modules.merchant.model.PaymentLink;
import com.digicore.omni.data.lib.modules.merchant.model.Transaction;
import com.digicore.omni.data.lib.modules.merchant.repository.PaymentLinkRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.SettingRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oluwatobi ogunwuyi on 26/07/2022
 */
@Component
@RequiredArgsConstructor
public class SystemStartUpBackGroundTasks implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final BackOfficeProfileRepository backOfficeProfileRepository;
    private final PaymentProcessorRepository paymentProcessorRepository;

    private final SettingRepository settingRepository;
    private final PaymentLinkRepository paymentLinkRepository;

    @Value("#{${available.roles}}")
    private List<String> availableRoles;

    @Value("${payment-processor.path}")
    private String paymentProcessorFilePath;

    @Value("${general-setting.path}")
    private String generalSettingFilePath;

    @Value("${omni.root.startup.task.update.roles:true}")
    private boolean updateRoles;

    @Value("${omni.root.startup.task.update.payment-link:false}")
    private boolean updatePaymentLink;



    @Value("${omni.root.startup.task.update.payment-processors:true}")
    private boolean updatePaymentProcessors;

    @Value("${omni.root.startup.task.add.general-settings:true}")
    private boolean addGeneralSettings;

    private static final String SUPER_MAN = "SUPERMAN";
    private static final String SUPER_MAN_EMAIL = "SUPERMAN";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(updatePaymentProcessors){
            readPaymentProcessor();
        }

        if(addGeneralSettings){
            addNewGeneralSetting();
        }

        if (updateRoles) {
            readRoles();
        }


        if (isSuperUserCreatedAlready())
            return;

        Role adminRole = roleRepository.findByName("ADMIN");
        UserAccount user = new UserAccount();
        user.setFirstName(SUPER_MAN);
        user.setLastName(SUPER_MAN);
        user.setUsername(SUPER_MAN);
        user.setPassword(passwordEncoder.encode("P@ssw0rd"));
        user.setEmail(SUPER_MAN_EMAIL);
        user.setRoles(adminRole);
        user.setPermissions(new ArrayList<>(adminRole.getPermissions()));
        user.setActive(true);
        userRepository.save(user);
        saveBackOfficeAdmin(adminRole.getName());
    }

    private boolean isSuperUserCreatedAlready() {
        return userRepository.findByUsername(SUPER_MAN) != null;
    }

    private void readPaymentProcessor(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PaymentProcessor> paymentProcessors = mapper.readValue(Paths.get(paymentProcessorFilePath).toFile(), new TypeReference<List<PaymentProcessor>>() {
            });

            for(PaymentProcessor p: paymentProcessors){
                Optional<PaymentProcessor> optionalProcessor = paymentProcessorRepository.findFirstByProcessorAndFeeConfigurationTypeOrderByCreatedOnDesc(p.getProcessor(), p.getFeeConfigurationType());

                if(optionalProcessor.isPresent()){
                    PaymentProcessor paymentProcessor = optionalProcessor.get();
                    paymentProcessor.setActive(p.getActive());
                    paymentProcessor.setFeeConfigurationType(p.getFeeConfigurationType());
                    paymentProcessorRepository.save(paymentProcessor);
                }else {
                    paymentProcessorRepository.save(p);
                }
            }
        } catch (IOException e) {
            throw new ZeusRuntimeException(e.getMessage());
        }
    }

    private void addNewGeneralSetting(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Setting> settings = mapper.readValue(Paths.get(generalSettingFilePath).toFile(), new TypeReference<List<Setting>>() {
            });

            for(Setting s: settings){
                Optional<Setting> optionalSetting = settingRepository.findByName(s.getName());

                if(optionalSetting.isEmpty()){
                    settingRepository.save(s);
                }
            }
        } catch (IOException e) {
            throw new ZeusRuntimeException(e.getMessage());
        }
    }
    private void readRoles(){
            ObjectMapper mapper = new ObjectMapper();

            availableRoles.forEach(roles -> saveRole(Paths.get(roles).toFile(),mapper));

    }

    public void saveRole(File file, ObjectMapper mapper) {

        try {
            Role role = mapper.readValue(file, Role.class);
            Role existingRole = roleRepository.findByName(role.getName());
            List<Authority> authorities = getAuthorities(Objects.requireNonNullElse(existingRole, role),role);
            role =  Objects.requireNonNullElse(existingRole, role);
            role.setPermissions(authorities); // the new role is set to it's authorities /the previous role is set to the updated ;ist of authorities
            // if there was a previously existing role, also update it's role scope, description and active status fields
            if (existingRole != null){
                role.setRoleScope(role.getRoleScope());
                role.setDescription(role.getDescription());
                role.setActive(role.isActive());
            }
            // save the updated role (new or previously existing)
            roleRepository.save(role);
            List<UserAccount> usersWithRole = userRepository.findUserAccountsByRoleNameAndIsActive(role.getName());
            List<Authority> updatedAuthorities = new ArrayList<>(role.getPermissions());
            usersWithRole.forEach(user -> user.setPermissions(new ArrayList<>(updatedAuthorities)));
            userRepository.saveAll(usersWithRole);

        } catch (IOException e) {
            throw new ZeusRuntimeException(e.getMessage());
        }
    }



    private List<Authority> getAuthorities(Role existingRole, Role newRole) {
        List<Authority> newAuthorities = (List<Authority>) newRole.getPermissions();
        Set<Authority> existingAuthorities = new HashSet<>(existingRole.getPermissions());
        Set<String> existingAuthoritiesPermissions = existingAuthorities.stream()
                .map(Authority::getPermission)
                .collect(Collectors.toSet());

        newAuthorities.forEach(authority->{
            // if this new authority's permission is not contained in the previous permissions, add it, then save
            if(!existingAuthoritiesPermissions.contains(authority.getPermission())){
                // if this authority already exists in the db, then map it (to prevent db constraint violation)
                List<Authority> authorities = authorityRepository.findAllByPermission(authority.getPermission());
                if(!authorities.isEmpty()) {
                    existingAuthorities.addAll(authorities);
                }
                // otherwise it is a new one
                else{
                    existingAuthorities.add(authority);
                }
            }
        });

        // existingAuthorities now contains a combination of it's previous permissions and the new ones added
        return new ArrayList<>(existingAuthorities);
    }

    public void saveBackOfficeAdmin(String roleName){
        BackOfficeProfile backOfficeProfile = new BackOfficeProfile();
        backOfficeProfile.setBackOfficeUserId(SUPER_MAN);
        backOfficeProfile.setRole(roleName);
        backOfficeProfile.setActive(true);
        backOfficeProfile.setEmail(SUPER_MAN);
        backOfficeProfile.setDateCreated(LocalDateTime.now());
        backOfficeProfile.setFirstName(SUPER_MAN);
        backOfficeProfile.setLastName(SUPER_MAN);
        backOfficeProfile.setPhoneNumber(SUPER_MAN);
        backOfficeProfileRepository.save(backOfficeProfile);
    }



}
