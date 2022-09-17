package com.qadr.bankapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CountryControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(
            WebApplicationContext context,
            RestDocumentationContextProvider restDocumentationContextProvider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .uris()
                        .withHost("api-env.eba-irpspqyp.us-east-1.elasticbeanstalk.com")
                        .withScheme("http")
                )
                .build();
    }

    @Test
    void getAllCountries() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/country/all/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()))
                );
    }

    @Test
    void getCountriesByContinent() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/country/continent/{continent}", "africa"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("continent")
                                                .description("continent, It must not be null or empty")
                                )
                        )
                );
    }

    @Test
    void findCountryByCallCode() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/country/call_code/{code}", "+234"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("code")
                                                .description("country call code e.g +1 for USA, It must not be null or empty")
                                )
                        )
                );
    }

    @Test
    void findCountryByCode() throws Exception {
        String code = "NG";
        mockMvc.perform(RestDocumentationRequestBuilders.get("/country/code/{code}", code))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(code))
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("code")
                                                .description("country code e.g US for USA, It must not be null or empty")
                                )
                        )
                );
    }

    @Test
    void findCountryByName() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/country/name/{name}", "nigeria"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").isNotEmpty())
                .andDo(
                        document("{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("name")
                                                .description("country name, It must not be null or empty")
                                )
                        )
                );
    }
}