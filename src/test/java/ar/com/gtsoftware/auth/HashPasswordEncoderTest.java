package ar.com.gtsoftware.auth;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class HashPasswordEncoderTest {

    public static final String RAW_PASSWORD = "Cambiame";
    public static final String ENCODED_PASSWORD = "1IQv3Mv/KY06djR3gfuP4PHREk8KdEwx/ivZQbM3/MY=";
    private HashPasswordEncoder passwordEncoder = new HashPasswordEncoder();

    @Test
    void shouldEncode() {
        String result = passwordEncoder.encode(RAW_PASSWORD);
        assertThat(result, is(ENCODED_PASSWORD));
    }

    @Test
    void shouldMatchPasswords() {
        boolean matches = passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD);
        assertThat(matches, is(true));
    }
}