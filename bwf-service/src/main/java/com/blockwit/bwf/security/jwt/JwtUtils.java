package com.blockwit.bwf.security.jwt;

import com.blockwit.bwf.security.jwt.exceptions.JwtBadSignatureException;
import com.blockwit.bwf.security.jwt.exceptions.JwtExpirationException;
import com.blockwit.bwf.security.jwt.model.JwtUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static com.nimbusds.jose.JWSAlgorithm.HS256;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public final class JwtUtils {

    private final static String AUDIENCE_WEB = "web";
    private final static String ROLES_CLAIM = "roles";

    public static SignedJWT extractAndDecodeJwt(HttpServletRequest request) throws ParseException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = authHeader.substring("Bearer ".length());
        return JwtUtils.parse(token);
    }

    public static void checkAuthenticationAndValidity(SignedJWT jwt, String secretKey) throws ParseException, JOSEException {
        JwtUtils.assertNotExpired(jwt);
        JwtUtils.assertValidSignature(jwt, secretKey);
    }

    public static Authentication buildAuthenticationFromJwt(SignedJWT jwt, HttpServletRequest request) throws ParseException {

        String username = JwtUtils.getUsername(jwt);
        Collection<? extends GrantedAuthority> authorities = JwtUtils.getRoles(jwt);
        Date creationDate = JwtUtils.getIssueTime(jwt);
        JwtUser userDetails = new JwtUser(username, creationDate, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    public static String generateHMACToken(String subject, Collection<? extends GrantedAuthority> roles, String secret, int expirationInMinutes) throws JOSEException {
        return generateHMACToken(subject, AuthorityListToCommaSeparatedString(roles), secret, expirationInMinutes);
    }

    public static String generateHMACToken(String subject, String roles, String secret, int expirationInMinutes) throws JOSEException {
        JWSSigner signer = new MACSigner(secret);

        // You can read about it in RFC7519 -  https://tools.ietf.org/html/rfc7519#page-8
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issueTime(currentDate())
                .expirationTime(expirationDate(expirationInMinutes))
                .claim(ROLES_CLAIM, roles) // not in RFC 7519 ?
                .audience(AUDIENCE_WEB)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    private static Date expirationDate(int expirationInMinutes) {
        return new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000);
    }

    public static void assertNotExpired(SignedJWT jwt) throws ParseException {
        if (DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
            throw new JwtExpirationException("Token has expired");
        }
    }

    public static void assertValidSignature(SignedJWT jwt, String secret) throws ParseException, JOSEException {
        if (!verifyHMACToken(jwt, secret)) {
            throw new JwtBadSignatureException("Signature is not valid");
        }
    }

    public static SignedJWT parse(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    public static boolean verifyHMACToken(SignedJWT jwt, String secret) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(secret);
        return jwt.verify(verifier);
    }

    private static String AuthorityListToCommaSeparatedString(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
        return StringUtils.join(authoritiesAsSetOfString, ", ");
    }

    public static String getUsername(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getSubject();
    }

    public static Collection<? extends GrantedAuthority> getRoles(SignedJWT jwt) throws ParseException {
        Collection<? extends GrantedAuthority> authorities;
        String roles = jwt.getJWTClaimsSet().getStringClaim(ROLES_CLAIM);
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        return authorities;
    }

    public static Date getIssueTime(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getIssueTime();
    }

}