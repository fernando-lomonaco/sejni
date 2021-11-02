package br.com.lomonaco.sejni.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenUtil {

    companion object {
        const val CLAIM_KEY_USERNAME = "sub"
        const val CLAIM_KEY_ROLE = "role"
        const val CLAIM_KEY_CREATED = "created"
    }

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    private val expiration: Long = 38200000

    fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(SignatureAlgorithm.HS512, secret.toByteArray())
            .compact()
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaimsToken(token)
        if (claims != null) {
            val username = claims.subject
            val expirationDate = claims.expiration
            val now = Date(System.currentTimeMillis())
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true
            }
        }
        return false
    }


    private fun getClaimsToken(token: String): Claims? {
        return try {
            Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJws(token).body
        } catch (e: Exception) {
            null
        }
    }

    fun getUserNameFromToken(token: String): String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

    fun getToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims[CLAIM_KEY_USERNAME] = userDetails.username
        claims[CLAIM_KEY_CREATED] = Date()
        userDetails.authorities.forEach { authority: GrantedAuthority? ->
            claims[CLAIM_KEY_ROLE] = authority!!.authority
        }
        return generateToken(claims)
    }


}