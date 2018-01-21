package ru.alphadoub.voting.web;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.alphadoub.voting.model.User;
import ru.alphadoub.voting.service.UserService;
import ru.alphadoub.voting.to.UserTo;
import ru.alphadoub.voting.util.UserUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alphadoub.voting.UserTestData.*;

public class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String URL = ProfileRestController.URL;

    @Autowired
    UserService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jacksonObjectMapper.writeValueAsString(USER1)));
    }

    @Test
    public void testUpdate() throws Exception {
        UserTo updated = new UserTo(null, "UPDATED user", "updated@gmail.com", "updatedPassword");
        mockMvc.perform(put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andDo(print());
        assertMatch(service.get(USER1_ID), UserUtil.updateFromTo(new User(USER1), updated));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL))
                .andDo(print())
                .andExpect(status().isOk());
        assertMatch(service.getAll(), ADMIN, USER2, USER3);
    }
}