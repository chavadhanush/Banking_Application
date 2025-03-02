package com.ltp.banking_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message =  "username cannot be blank")
	@NonNull
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message =  "password cannot be blank")
    @NonNull
	@Column(nullable = false)
	private String password;



}