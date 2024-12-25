package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractApplicationMvcIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    protected String serialize(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    protected <T> T deserialize(MvcResult mvcResult, Class<T> aClass) {
        return objectMapper.readValue(contentAsString(mvcResult), aClass);
    }

    @SneakyThrows
    protected <T> List<T> deserializeList(MvcResult mvcResult, Class<T> contentClass) {
        return deserializeCollection(mvcResult, List.class, contentClass);
    }

    @SneakyThrows
    protected <T> List<T> deserializeCollection(MvcResult mvcResult, Class<? extends Collection> collection,
            Class<T> contentClass) {
        return objectMapper.readValue(contentAsString(mvcResult), collectionType(collection, contentClass));
    }

    @SneakyThrows
    private <T> CollectionType collectionType(Class<? extends Collection> collection, Class<T> contentClass) {
        return objectMapper.getTypeFactory().constructCollectionType(collection, contentClass);
    }

    @SneakyThrows
    protected String contentAsString(MvcResult mvcResult) {
        return mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

}
