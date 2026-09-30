package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.PaginatedResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurudeen.propertyfind.dto.property.PropertyCreateDto;
import com.nurudeen.propertyfind.dto.property.PropertyCreateResponseDto;
import com.nurudeen.propertyfind.dto.property.PropertySearchDto;
import com.nurudeen.propertyfind.dto.property.PropertyResponseDto;
import com.nurudeen.propertyfind.service.FileStorageService;
import com.nurudeen.propertyfind.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PropertyControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyService propertyService;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private PropertyController propertyController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();
    }

    @Test
    void testCreateProperty_Success() throws Exception {
        PropertyCreateDto dto = new PropertyCreateDto();
        dto.setTitle("Luxury Villa");
        dto.setDescription("A beautiful villa by the sea");
        dto.setAddress("123 Ocean Ave");
        dto.setCity("Miami");
        dto.setState("FL");
        dto.setCountry("USA");
        dto.setPricePerYear(new BigDecimal("120000.00"));
        dto.setBedroom(4);
        dto.setBathroom(3);
        dto.setArea(2500.5);
        dto.setImageUrls(Collections.singletonList("http://example.com/image.jpg"));

        PropertyCreateResponseDto responseDto = new PropertyCreateResponseDto();
        responseDto.setTitle("Luxury Villa");
        responseDto.setCity("Miami");

        when(propertyService.createProperty(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Luxury Villa"))
                .andExpect(jsonPath("$.city").value("Miami"));
    }

    @Test
    void testSearchProperties_Success() throws Exception {
        PropertySearchDto searchDto = new PropertySearchDto();
        searchDto.setKeyword("Searchable");
        searchDto.setCity("New York");

        PropertyResponseDto propDto = new PropertyResponseDto();
        propDto.setTitle("Searchable Apartment");
        propDto.setCity("New York");

        PaginatedResponseDto<PropertyResponseDto> paginatedResponse = 
            new PaginatedResponseDto<>();
        paginatedResponse.setData(Collections.singletonList(propDto));

        when(propertyService.searchProperties(any())).thenReturn(paginatedResponse);

        mockMvc.perform(post("/api/properties/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Searchable Apartment"))
                .andExpect(jsonPath("$.data[0].city").value("New York"));
    }
}
