package com.logique.shortner.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.logique.shortner.utils.TokenUtils;
import com.logique.shortner.models.Url;
import com.logique.shortner.repositories.UrlRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    private TokenUtils tokenUtils;

    @MockBean
    private UrlRepository urlRepository;


    @BeforeEach
    public void setUp() { 
        Url url1 = new Url("a1b2c3", "https://dumburl.test.com/dsjdjdan", new Date(), "test");
        Url url2 = new Url("a1b2c4", "https://dumburl.test.com/dsjdjdaz", new Date(), "test");
        List<Url> urls = new ArrayList<>(Arrays.asList(url1, url2)); 
        Page<Url> pagedUrls = new PageImpl(urls);
        when(this.urlRepository.findByUsername(any(Pageable.class), eq("test")))
        .thenReturn(pagedUrls);

        when(this.urlRepository.findByUsername(any(Pageable.class), not(eq("test"))))
        .thenReturn(new PageImpl(new ArrayList<>()));
    }

    @Test
    public void shouldCreateAShortenedUrl() throws Exception {
        Url url = new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test");
        when(this.urlRepository.save(any(Url.class))).thenReturn(url);
        String token = tokenUtils.createToken("test");

        mockMvc.perform(post("/urls").header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new HashMap<>()
            {{
                 put("originalURL", url.getOriginalURL());
            }})))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hash", is(url.getHash())))
            .andExpect(jsonPath("$.originalURL", is(url.getOriginalURL())));
    }

    @Test
    public void shouldGetAllUrlsFromUsername() throws Exception {
        String token = tokenUtils.createToken("test");

		mockMvc.perform(get("/urls").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].hash", is("a1b2c3")))
            .andExpect(jsonPath("$.content[0].originalURL", is("https://dumburl.test.com/dsjdjdan")))
            .andExpect(jsonPath("$.content[1].hash", is("a1b2c4")))
            .andExpect(jsonPath("$.content[1].originalURL", is("https://dumburl.test.com/dsjdjdaz")));
    }

    @Test
    public void shouldNotGetUrlsFromDifferentUser() throws Exception {
        String token = tokenUtils.createToken("anotherUser");

		mockMvc.perform(get("/urls").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void shouldRedirectToOriginalUrl() throws Exception {
        Optional<Url> url = Optional.of(new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test"));
        when(this.urlRepository.findById(eq("a1b1c1"))).thenReturn(url);

		mockMvc.perform(get("/su/a1b1c1"))
            .andExpect(redirectedUrl("https://dumburl.test.com/dsjdjdan"));
    }

    @Test
    public void shouldFailToCreateUrlsWithoutToken() throws Exception {
        Url url = new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test");
        when(this.urlRepository.save(any(Url.class))).thenReturn(url);

        mockMvc.perform(post("/urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new HashMap<>()
            {{
                 put("originalURL", url.getOriginalURL());
            }})))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailToGetAllUrlsWithoutToken() throws Exception {
		mockMvc.perform(get("/urls"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailToRedirectUrlWithHashNotFound() throws Exception {
        Optional<Url> url = Optional.of(new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test"));
        when(this.urlRepository.findById(eq("a1b1c1"))).thenReturn(url);

		mockMvc.perform(get("/su/wrongHash"))
            .andExpect(status().isNotFound());
    }

    @Test 
    public void shouldRemoveUrlSuccessfully() throws Exception {
        Optional<Url> url = Optional.of(new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test"));
        when(this.urlRepository.findById(eq("a1b1c1"))).thenReturn(url);
        doNothing().when(urlRepository).deleteById("a1b1c1");
        String token = tokenUtils.createToken("test");

        mockMvc.perform(delete("/urls/a1b1c1").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());

        verify(this.urlRepository, times(1)).deleteById("a1b1c1");
    }

    @Test 
    public void shouldFailToRemoveUrlWithoutToken() throws Exception {
        Optional<Url> url = Optional.of(new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test"));
        when(this.urlRepository.findById(eq("a1b1c1"))).thenReturn(url);
        doNothing().when(urlRepository).deleteById("a1b1c1");

        mockMvc.perform(delete("/urls/a1b1c1"))
            .andExpect(status().isForbidden());
    }

    @Test 
    public void shouldFailToRemoveUrlWrongHash() throws Exception {
        Optional<Url> url = Optional.of(new Url("a1b1c1", "https://dumburl.test.com/dsjdjdan", new Date(), "test"));
        when(this.urlRepository.findById(eq("a1b1c1"))).thenReturn(url);
        doNothing().when(urlRepository).deleteById("a1b1c1");
        String token = tokenUtils.createToken("test");

        mockMvc.perform(delete("/urls/wrongHash").header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());

        verify(this.urlRepository, times(0)).deleteById("a1b1c1");
    }

}