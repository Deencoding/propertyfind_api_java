package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.property.PropertyResponseDto;
import com.nurudeen.propertyfind.entity.PropertyStatus;
import com.nurudeen.propertyfind.exception.GlobalExceptionHandler;
import com.nurudeen.propertyfind.service.FileStorageService;
import com.nurudeen.propertyfind.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PropertyStatusControllerTest {
    private PropertyService service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        service = mock(PropertyService.class);
        mvc = MockMvcBuilders.standaloneSetup(new PropertyController(service, mock(FileStorageService.class)))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void acceptsEachStatus() throws Exception {
        for (PropertyStatus value : PropertyStatus.values()) {
            PropertyResponseDto response = new PropertyResponseDto();
            response.setStatus(value);
            response.setAvailable(value == PropertyStatus.AVAILABLE);
            when(service.updateStatus(eq(10L), any())).thenReturn(response);
            mvc.perform(patch("/api/properties/10/status").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status\":\"" + value + "\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(value.name()))
                    .andExpect(jsonPath("$.available").value(value == PropertyStatus.AVAILABLE));
        }
    }

    @Test
    void rejectsMissingNullAndUnknownStatus() throws Exception {
        for (String body : new String[]{"{}", "{\"status\":null}", "{\"status\":\"INVALID\"}"}) {
            mvc.perform(patch("/api/properties/10/status").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }
}
