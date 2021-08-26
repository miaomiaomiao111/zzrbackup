package com.xuecheng.test;

import org.junit.Test;

import java.util.Base64;

/**
 * <p></p>
 *
 * @Description:
 */
public class JwtTest {

    @Test
    public void header() {

        byte[] decode = Base64.getDecoder().decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

        System.out.println(new String(decode));

    }


    @Test
    public void paload() {

        byte[] decode = Base64.getDecoder().decode("eyJhdWQiOlsieHVlY2hlbmctcmVzb3VyY2UiXSwicGF5bG9hZCI6eyIxMTc3MTQ0MjA5NDYzMTI4MTI1Ijp7InJlc291cmNlcyI6bnVsbCwidXNlcl9hdXRob3JpdGllcyI6eyJyX3hjX2NvbSI6WyJ4Y19jb3Vyc2VfYmFzZV9saXN0IiwieGNfY291cnNlX2Jhc2Vfc2F2ZSIsInhjX2NvdXJzZV9iYXNlX2VkaXQiLCJ4Y19jb3Vyc2VfYmFzZV9kZWwiLCJ4Y19jb3Vyc2VfYmFzZV92aWV3IiwieGNfdGVhY2hwbGFuX3ZpZXciLCJ4Y190ZWFjaHBsYW5fc2F2ZV9tb2RpZnkiLCJ4Y190ZWFjaHBsYW5fZGVsIiwieGNfbWFya2V0X3NhdmVfbW9kaWZ5IiwieGNfbWFya2V0X3ZpZXciLCJ4Y19jb3Vyc2VfcHVibGlzaCIsInhjX21lZGlhX2xpc3QiLCJ4Y19tZWRpYV9zYXZlIiwieGNfbWVkaWFfcHJldmlldyIsInhjX21lZGlhX2RlbCIsInhjX2NvbXBhbnlfbW9kaWZ5IiwieGNfY29tcGFueV92aWV3IiwieGNfdGVhY2hlcl9saXN0IiwieGNfdGVhY2hlcl9zYXZlIiwieGNfdGVhY2hlcl9tb2RpZnkiLCJ4Y190ZWFjaHBsYW53b3JrX2xpc3QiLCJ4Y190ZWFjaHBsYW53b3JrX3NhdmVfbW9kaWZ5IiwieGNfdGVhY2hwbGFud29ya19kZWwiLCJ4Y193b3JrcmVjb3JkX2xpc3QiXX19fSwidXNlcl9uYW1lIjoieGMtdXNlci1maXJzdCIsInNjb3BlIjpbInJlYWQiXSwibW9iaWxlIjoiMTUwMjIyMjIyMjciLCJleHAiOjE2NTg5MzIwMDksImNsaWVudF9hdXRob3JpdGllcyI6WyJST0xFX1hDX0FQSSJdLCJqdGkiOiJkNzEwNjI0Yy1jZGMzLTQwMTktOTg3OS1mMDNhNDBiN2YyM2QiLCJjbGllbnRfaWQiOiJ4Yy1jb20tcGxhdGZvcm0ifQ");

        System.out.println(new String(decode));

    }

    @Test
    public void signature() {

        byte[] decode = Base64.getDecoder().decode("2bD1Aji3sut6MyQ6t80NaGhb6-QQYa-LE6sRHDAGVRk");

        System.out.println(new String(decode));

    }


}
