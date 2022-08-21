package com.exam.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.exam.service.impl.UserDetailServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String requestHeaderToken = request.getHeader("Authorization");
		System.out.println("___________________________");
		System.out.println(requestHeaderToken);
		System.out.println("__________________________-");

		String username = null;
		String jwtToken = null;

		if (requestHeaderToken != null && requestHeaderToken.startsWith("Bearer ")) {

			jwtToken = requestHeaderToken.substring(7);

			try {
				username = this.jwtUtil.extractUsername(jwtToken);
			} catch (ExpiredJwtException e) {
				System.out.println("jwt token are expire a.");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("error e..");
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailServiceImpl.loadUserByUsername(username);
				if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
					// token is valid now set in context

					UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthentication
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
				}
			} else {
				System.out.println("token is not valid");
			}

		} else {
			System.out.println("invalid token not start with bearer");
		}

		filterChain.doFilter(request, response);

	}

}
