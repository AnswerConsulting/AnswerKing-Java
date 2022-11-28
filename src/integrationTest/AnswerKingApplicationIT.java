package com.answerdigital.answerking;

import com.answerdigital.answerking.utility.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AnswerKingApplicationIT extends AbstractContainerBaseTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProductsReturnListOfProductObjects() throws Exception {
        //when
        RequestBuilder request = MockMvcRequestBuilders.get("/products");
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(response.getContentAsString().isEmpty());
        ObjectMapper mapper = new ObjectMapper();
        assertEquals("Burger", mapper.readTree(response.getContentAsString()).get(0).get("name").textValue());
        assertEquals("300g Beef", mapper.readTree(response.getContentAsString()).get(0).get("description").textValue());
        assertEquals(6.69, mapper.readTree(response.getContentAsString()).get(0).get("price").asDouble());
    }
}
