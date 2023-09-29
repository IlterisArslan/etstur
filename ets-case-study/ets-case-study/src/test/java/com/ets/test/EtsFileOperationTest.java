package com.ets.test;

import com.ets.controller.EtsFileController;
import com.ets.entity.EtsFile;
import com.ets.model.FileResponse;
import com.ets.service.EtsFileService;
import com.ets.util.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class EtsFileOperationTest {

    @Mock private EtsFileService etsFileService;

    @InjectMocks
    private EtsFileController etsFileController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(etsFileController)
                .build();
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGenerateAuthTokenAndGetFile() throws Exception {
        User usr = new User("etstur", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.getToken(usr);

        Assertions.assertNotNull(token);

        // get by id
        FileResponse resp = new FileResponse();
        Long fileId = 21L;
        Mockito.when(etsFileService.fetchEtsFileWithInfo(fileId)).thenReturn(resp);
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8082/ets/api/file/21").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk());

        // get all files
        List<EtsFile> etsFileList = new ArrayList<>();
        EtsFile etsFile = new EtsFile();
        etsFile.setId(123L);
        etsFile.setExtension(".jpeg");
        etsFile.setName("junit");
        etsFile.setPath("home/etstur/");
        etsFileList.add(etsFile);
        Mockito.when(etsFileService.fetchEtsFileList()).thenReturn(etsFileList);
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8082/ets/api/files").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGenerateAuthTokenAndDeleteFile() throws Exception {
        User usr = new User("etstur", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.getToken(usr);

        Assertions.assertNotNull(token);

        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8082/ets/api/file/21").header("Authorization", token)).andExpect(MockMvcResultMatchers.status().isOk());
    }


}

