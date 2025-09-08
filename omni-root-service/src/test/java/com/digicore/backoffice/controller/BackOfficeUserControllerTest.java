package com.digicore.backoffice.controller;

import com.digicore.AbstractTest;
import com.digicore.api.helper.response.ApiResponseJson;

import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BackOfficeUserControllerTest extends AbstractTest {

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @SuppressWarnings("unchecked")
//    @Test
    void getAllUsersReturnsAListOfAllUsers() throws Exception {
        // given
        String uri = "/api/v1/get-all-users";

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        String content = mvcResult.getResponse().getContentAsString();
        ApiResponseJson<Object> apiResponseJson = jsonToObject(content);
        List<MerchantProfile> merchantList = (List<MerchantProfile>)apiResponseJson.getData();

        // then
        assertEquals(200, status);
        assertNotNull(merchantList);
    }

    // @Test
    void inviteUserTestReturnsAConfirmationEmail() throws Exception{
        // given
        String email = "mon@email.com";
        String uri = "/api/v1/invite-user?email=" + email;

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponseJson<Object> apiResponseJson = jsonToObject(content);
        Object confirmationEmail = apiResponseJson.getData();

        // then
        assertEquals(201, status);
        assertInstanceOf(String.class, confirmationEmail);
    }

    // @Test
    void inviteUserWithInvalidEmailReturnsA400() throws Exception{
        // given
        String email = "monemail.com";
        String uri = "/api/v1/invite-user?email="+email;

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponseJson<Object> apiResponseJson = jsonToObject(content);
        Object confirmationEmail = apiResponseJson.getData();

        // then
        assertEquals(400, status);
        assertEquals(List.of("email: please provide a valid email"), confirmationEmail);
    }

    @SuppressWarnings("unchecked")
    // @Test
    void confirmInviteReturnsAUserWithAssociatedEmail() throws Exception{

        // given and invited user
        String email = "seun@email.com";
        String inviteUri = "/api/v1/invite-user?email=" + email;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(inviteUri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponseJson<Object> apiResponseJson = jsonToObject(content);
        String confirmationEmail = (String)apiResponseJson.getData();
        String token = confirmationEmail.substring(confirmationEmail.lastIndexOf("/")+1);

        // given
        String uri = "/api/v1/confirm-invite" + "/" + token;

        // when
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        content = mvcResult.getResponse().getContentAsString();
        apiResponseJson = jsonToObject(content);
        Object data = apiResponseJson.getData();

        // then
        assertEquals(200, status);
        assertNotNull(data);
        assertInstanceOf(LinkedHashMap.class, data);
        LinkedHashMap<String, Object> invite = (LinkedHashMap<String, Object>)data;
        assertEquals(email, invite.get("email"));

    }

}