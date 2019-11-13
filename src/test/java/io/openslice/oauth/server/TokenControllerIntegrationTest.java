/*-
 * ========================LICENSE_START=================================
 * io.openslice.oauth.server
 * %%
 * Copyright (C) 2019 openslice.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package io.openslice.oauth.server;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.openslice.oauth.server.config.AuthorizationServerApplication;
import io.openslice.oauth.server.controller.TokenController;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AuthorizationServerApplication.class, TokenController.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TokenControllerIntegrationTest {

    @Autowired
    private TokenController sut;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TokenStore tokenStore;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldLoadContext() {
        assertThat(sut).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListOfTokens() throws Exception {
        List<String> tokens = retrieveTokens();

        assertThat(tokens).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListOfTokensWhenTokenStoreReturnNull() throws Exception {
        when(tokenStore.findTokensByClientId("sampleClientId")).thenReturn(null);

        List<String> tokens = retrieveTokens();

        assertThat(tokens).isEmpty();
    }

    @Test
    public void shouldReturnNotEmptyListOfTokens() throws Exception {
        when(tokenStore.findTokensByClientId("sampleClientId")).thenReturn(generateTokens(1));

        List<String> tokens = retrieveTokens();

        assertThat(tokens).hasSize(1);
    }

    @Test
    public void shouldReturnListOfTokens() throws Exception {
        when(tokenStore.findTokensByClientId("sampleClientId")).thenReturn(generateTokens(10));

        List<String> tokens = retrieveTokens();

        assertThat(tokens).hasSize(10);
    }

    private List<String> retrieveTokens() throws Exception {
        return mapper.readValue(mockMvc.perform(get("/tokens")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<String>>() {
        });
    }

    private List<OAuth2AccessToken> generateTokens(int count) {
        List<OAuth2AccessToken> result = new ArrayList<>();
        IntStream.range(0, count).forEach(n -> result.add(new DefaultOAuth2AccessToken("token" + n)));
        return result;
    }
}
