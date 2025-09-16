package br.com.alura.projeto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@ActiveProfiles("test")
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return home page with welcome message")
    void shouldReturnHomePageWithWelcomeMessage() throws Exception {
        mockMvc.perform(get("/oi"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/oi"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("Projeto Alura")))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("Bem-vinda ao teste")))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("Pessoa Desenvolvedora Java")));
    }

    @Test
    @DisplayName("should return home page with HTML content in message")
    void shouldReturnHomePageWithHtmlContentInMessage() throws Exception {
        mockMvc.perform(get("/oi"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/oi"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("<h1>Projeto Alura</h1>")))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("<p>Bem-vinda ao teste")))
                .andExpect(model().attribute("message", 
                        org.hamcrest.Matchers.containsString("<b>Pessoa Desenvolvedora Java</b>")));
    }

    @Test
    @DisplayName("should handle GET request to home endpoint")
    void shouldHandleGetRequestToHomeEndpoint() throws Exception {
        mockMvc.perform(get("/oi"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/oi"));
    }
}

